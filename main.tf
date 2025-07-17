terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "5.81.0"
    }
  }
}
# 프로바이더 설정
provider "aws" {
  region ="ap-northeast-2"
}

# VPC 생성
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    Name = "tomato-vpc"
  }
}

# 인터넷 게이트웨이
resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "tomato-igw"
  }
}

# 퍼블릭 서브넷 1
resource "aws_subnet" "public_1" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.3.0/24"
  availability_zone       = "ap-northeast-2a"
  map_public_ip_on_launch = true

  tags = {
    Name = "tomato-public-1"
  }
}

# 퍼블릭 서브넷 2
resource "aws_subnet" "public_2" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.5.0/24"
  availability_zone       = "ap-northeast-2c"
  map_public_ip_on_launch = true

  tags = {
    Name = "tomato-public-2"
  }
}

# 프라이빗 서브넷
resource "aws_subnet" "private_1" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.4.0/24"
  availability_zone = "ap-northeast-2a"

  tags = {
    Name = "tomato-private-1"
  }
}

# 프라이빗 서브넷 2
resource "aws_subnet" "private_2" {
  vpc_id            = aws_vpc.main.id
  cidr_block        = "10.0.6.0/24"
  availability_zone = "ap-northeast-2c"

  tags = {
    Name = "tomato-private-2"
  }
}

# NAT의 탄력적 IP
resource "aws_eip" "nat" {
  tags = {
    Name = "tomato-nat-eip"
  }
}

// AWS NAT GATEWAY
resource "aws_nat_gateway" "main" {
  allocation_id = aws_eip.nat.id
  subnet_id     = aws_subnet.public_1.id

  tags = {
    Name = "tomato-nat-gateway"
  }

  depends_on = [aws_internet_gateway.main]
}

# 퍼블릭 라우팅 테이블
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }

  tags = {
    Name = "tomato-public-rt"
  }
}

resource "aws_route_table_association" "public_2" {
  subnet_id      = aws_subnet.public_2.id
  route_table_id = aws_route_table.public.id
}

# 프라이빗 라우팅 테이블
resource "aws_route_table" "private" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.main.id
  }

  tags = {
    Name = "tomato-private-rt"
  }
}

# 라우팅 테이블 public에 연결
resource "aws_route_table_association" "public_1" {
  subnet_id      = aws_subnet.public_1.id
  route_table_id = aws_route_table.public.id
}

# 라우팅 테이블 private에 연결
resource "aws_route_table_association" "private_1" {
  subnet_id      = aws_subnet.private_1.id
  route_table_id = aws_route_table.private.id
}

resource "aws_route_table_association" "private_2" {
  subnet_id      = aws_subnet.private_2.id
  route_table_id = aws_route_table.private.id
}


# ECS 클러스터 생성
resource "aws_ecs_cluster" "main" {
  name = "tomato-cluster"
}

# ECS TASK의 컨테이너 실행을 위한 role 지정
resource "aws_iam_role" "ecs_task_execution" {
  name = "ecsTaskExecutionRole"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Action = "sts:AssumeRole",
      Principal = {
        Service = "ecs-tasks.amazonaws.com"
      },
      Effect = "Allow",
      Sid    = ""
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_policy" {
  role       = aws_iam_role.ecs_task_execution.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# 로그 그룹 생성!!
resource "aws_cloudwatch_log_group" "ecs_log_group" {
  name              = "/ecs/spring-service"
  retention_in_days = 7
}


# ECS Task Definition
resource "aws_ecs_task_definition" "spring_task" {
  family                   = "spring-task"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "512"
  memory                   = "1024"
  execution_role_arn       = aws_iam_role.ecs_task_execution.arn
  task_role_arn            = aws_iam_role.ecs_task_execution.arn

  container_definitions = jsonencode([
    {
      name      = "spring-container",
      image: "897722680443.dkr.ecr.ap-northeast-2.amazonaws.com/tomato:latest"
      essential = true,
      portMappings = [
        {
          containerPort = 8080,
          protocol      = "tcp"
        },
      ],
      environment = [
        { name = "TZ", value = "Asia/Seoul" },
        { name = "DB_URL", value = "${var.db_url}" },
        { name = "DB_USERNAME", value = "${var.db_username}" },
        { name = "DB_PASSWORD", value = "${var.db_password}" },
        { name = "REDIS_PORT", value = tostring(aws_elasticache_cluster.redis.port) },
        { name = "REDIS_HOST", value = aws_elasticache_cluster.redis.cache_nodes[0].address },
        { name = "JWT_SECRET", value = "${var.jwt_secret}" },
        { name = "JWT_EXPIRATION", value = "${tostring(var.jwt_expiration)}" },
        { name = "JWT_REFRESH_EXPIRATION", value = "${tostring(var.jwt_refresh_expiration)}" },
        { name = "EMAIL_USERNAME", value = "${var.email_username}" },
        { name = "EMAIL_PASSWORD", value = "${var.email_password}" },
        { name = "EMAIL_PORT", value = "${tostring(var.email_port)}" },
        { name = "EMAIL_HOST", value = "${var.email_host}" },
        { name = "YOUTUBE_API_KEY", value = "${var.youtube_apikey}" },
        { name = "YOUTUBE_CLIENT_ID", value = "${var.youtube_client_id}" },
        { name = "YOUTUBE_CLIENT_SECRET", value = "${var.youtube_client_secret}" },
        { name = "YOUTUBE_REDIRECT_URI", value = "${var.youtube_redirect_uri}" },
        { name = "AWS_ACCESS_KEY", value = "${var.aws_access_key}" },
        { name = "AWS_SECRET_KEY", value = "${var.aws_secret_key}" },
        { name = "AWS_S3_BUCKET_NAME", value = "${var.aws_s3_bucket_name}" },
        { name = "AWS_S3_REGION", value = "${var.aws_s3_region}" },
        { name = "SHEET_ID", value = "${var.sheet_id}" },
        { name = "FACEBOOK_CLIENT_KEY", value = "${tostring(var.facebook_client_key)}" },
        { name = "FACEBOOK_CLIENT_SECRET", value = "${var.facebook_client_secret}" },
        { name = "FACEBOOK_REDIRECT_URI", value = "${var.facebook_redirect_uri}" },
        { name = "NAVER_CLIENT_KEY", value = "${var.naver_client_key}" },
        { name = "NAVER_CLIENT_SECRET", value = "${var.naver_client_secret}" },
        { name = "MONGODB_URI", value = "${var.mongodb_uri}" },
        { name = "FCM_SECRET_FILE", value = "${var.fcm_secret_file}" },
        { name = "OPENAI_API_KEY", value = "${var.openai_api_key}" },
        { name = "OPENAI_MODEL_ID", value = "${var.openai_model_id}" },
        { name = "OPENAI_RECOMMEND_MODEL_ID", value = "${var.openai_recommend_model_id}"}
      ],
      logConfiguration = {
        logDriver = "awslogs",
        options = {
          awslogs-group         = "/ecs/spring-service",
          awslogs-region        = "ap-northeast-2",
          awslogs-stream-prefix = "ecs"
        }
      }
    }
  ])

  depends_on = [
    aws_cloudwatch_log_group.ecs_log_group
  ]
}

# Security Group
resource "aws_security_group" "ecs_service" {
  name        = "ecs-service-sg"
  description = "Allow HTTP inbound and all outbound"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "Allow HTTP"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "ecs-spring-sg"
  }
}

# ALB
resource "aws_lb" "spring_alb" {
  name               = "spring-alb"
  load_balancer_type = "application"
  subnets            = [
    aws_subnet.public_1.id,
    aws_subnet.public_2.id
  ]
  security_groups    = [aws_security_group.alb.id]

  tags = {
    Name = "spring-alb"
  }
}


resource "aws_lb_target_group" "spring_tg" {
  name        = "spring-tg"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = aws_vpc.main.id
  target_type = "ip"

  health_check {
    path                = "/api/v1/actuator/health"
    protocol            = "HTTP"
    matcher             = "200-399"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }

  tags = {
    Name = "spring-tg"
  }
}


resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.spring_alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type = "redirect"
    redirect {
      port        = "443"
      protocol    = "HTTPS"
      status_code = "HTTP_301"
    }
  }
}

resource "aws_lb_listener" "https" {
  load_balancer_arn = aws_lb.spring_alb.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"
  certificate_arn   = var.backend_cert_arn  # 인증서 ARN을 변수로 주입

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.spring_tg.arn
  }
}

resource "aws_ecs_service" "spring_service" {
  name            = "spring-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.spring_task.arn
  launch_type     = "FARGATE"
  desired_count   = 1

  network_configuration {
    subnets          = [
      aws_subnet.private_1.id,
      aws_subnet.private_2.id
    ]
    assign_public_ip = false
    security_groups  = [aws_security_group.ecs_service.id]
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.spring_tg.arn
    container_name   = "spring-container"
    container_port   = 8080
  }

  depends_on = [
    aws_ecs_task_definition.spring_task
  ]
}

# ALB용 Security Group
resource "aws_security_group" "alb" {
  name        = "alb-sg"
  description = "Allow HTTP and HTTPS inbound"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "Allow HTTP"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "Allow HTTPS"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    description = "Allow all outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "alb-sg"
  }
}

###### REDIS #######
# redis subnet 그룹
resource "aws_elasticache_subnet_group" "redis" {
  name       = "tomato-redis-subnet-group"
  subnet_ids = [aws_subnet.private_1.id]

  tags = {
    Name = "tomato-redis-subnet-group"
  }
}

# redis security 그룹
resource "aws_security_group" "redis" {
  name        = "tomato-redis-sg"
  description = "Allow access to Redis"
  vpc_id      = aws_vpc.main.id

  ingress {
    from_port   = 6379
    to_port     = 6379
    protocol    = "tcp"
    security_groups = [aws_security_group.ecs_service.id] # ECS에서만 접근 허용
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "tomato-redis-sg"
  }
}

# Elastic Cache Redis 인스턴스
resource "aws_elasticache_cluster" "redis" {
  cluster_id           = "tomato-redis"
  engine               = "redis"
  engine_version       = "7.1"
  node_type            = "cache.t4g.micro"
  num_cache_nodes      = 1
  parameter_group_name = "default.redis7"
  port                 = 6379

  subnet_group_name    = aws_elasticache_subnet_group.redis.name
  security_group_ids   = [aws_security_group.redis.id]

  tags = {
    Name = "tomato-redis"
  }
}

output "redis_host" {
  value = aws_elasticache_cluster.redis.cache_nodes[0].address
}

output "redis_port" {
  value = aws_elasticache_cluster.redis.port
}
