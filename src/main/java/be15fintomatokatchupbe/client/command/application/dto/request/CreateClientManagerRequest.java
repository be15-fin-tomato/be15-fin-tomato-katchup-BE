package be15fintomatokatchupbe.client.command.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "고객사 소속 사원 등록 요청 DTO")
public class CreateClientManagerRequest {

    @Schema(description = "이름", example = "김사원")
    @NotBlank
    private String name;

    @Schema(description = "사원 상태 ID", example = "1")
    @NotNull
    private Long clientManagerStatusId;

    @Schema(description = "부서", example = "마케팅팀")
    private String department;

    @Schema(description = "직책", example = "과장")
    private String position;

    @Schema(description = "휴대폰 번호", example = "010-1234-5678")
    private String phone;

    @Schema(description = "유선 번호", example = "02-2345-6789")
    private String telephone;

    @Schema(description = "이메일", example = "staff@example.com")
    @NotBlank
    private String email;

    @Schema(description = "비고", example = "1년 이상 거래 중")
    private String notes;

}