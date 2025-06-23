package be15fintomatokatchupbe.client.command.application.dto.request;

import be15fintomatokatchupbe.common.validation.ValidPhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "고객사 등록 요청 DTO")
public class CreateClientCompanyRequest {

    @Schema(description = "고객사명", example = "아모레퍼시픽")
    @NotBlank
    private String clientCompanyName;

    @Schema(description = "고객사 상태 ID", example = "1")
    @NotNull
    private Long clientCompanyStatusId;

    @Schema(description = "사업자 번호", example = "1234567890")
    private Long businessId;

    @Schema(description = "매출", example = "5000000")
    private Long sales;

    @Schema(description = "사원 수", example = "30")
    private Integer numberOfEmployees;

    @Schema(description = "유선 번호", example = "02-1234-5678")
    @ValidPhoneNumber
    private String telephone;

    @Schema(description = "팩스 번호", example = "02-1234-9876")
    private String fax;

    @Schema(description = "주소", example = "서울특별시 강남구 테헤란로")
    private String address;

    @Schema(description = "상세 주소", example = "101동 202호")
    private String detailAddress;

    @Schema(description = "비고", example = "VIP 고객사입니다.")
    private String notes;

    @Schema(description = "고객사 소속 사원 목록", required = true)
    @Valid
    private List<CreateClientManagerRequest> clientManagers;

    @Schema(description = "담당자")
    private List<Long> userIds;
}
