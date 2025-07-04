package be15fintomatokatchupbe.calendar.query.controlller;

import be15fintomatokatchupbe.calendar.query.dto.pipeline.PipeLineScheduleListResponse;
import be15fintomatokatchupbe.calendar.query.dto.schedule.ScheduleListResponse;
import be15fintomatokatchupbe.calendar.query.dto.schedule.ScheduleListsAllResponse;
import be15fintomatokatchupbe.calendar.query.service.CalendarQueryService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@Tag(name = "캘린더")
@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarQueryController {

    private final CalendarQueryService calendarQueryService;

    // 날짜별 일정 조회
    @GetMapping("/schedule/{date}/daily")
    @Operation(summary = "날짜별 일정 조회", description = "사용자는 특정 날짜의 일정을 조회할 수 있다.")
    public ResponseEntity<ApiResponse<ScheduleListResponse>> getScheduleList(
            @AuthenticationPrincipal CustomUserDetail customUserDetail,
            @PathVariable Date date
            )
    {
        Long userId = customUserDetail.getUserId();

        ScheduleListResponse response = calendarQueryService.getScheduleList(userId, date);

        return ResponseEntity.ok(ApiResponse.success(response));

    }

    // 전체 일정 조회
    @GetMapping("/schedule/all")
    @Operation(summary = "전체 일정 조회", description = "사용자는 본인이 작성한 모든 일정을 조회할 수 있다.")
    public ResponseEntity<ApiResponse<ScheduleListsAllResponse>> getScheduleListsAll(
            @AuthenticationPrincipal CustomUserDetail customUserDetail
    ){
        Long userId = customUserDetail.getUserId();

        ScheduleListsAllResponse response = calendarQueryService.getScheduleListsAll(userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 파이프라인 일정 불러오기
    @GetMapping("/schedule/pipeline")
    @Operation(summary = "파이프라인 일정 불러오기", description = "사용자는 파이프라인 일정을 불러올 수 있다.")
    public ResponseEntity<ApiResponse<PipeLineScheduleListResponse>> getPipelineScheduleList(
            @AuthenticationPrincipal CustomUserDetail user
    ){

        Long userId = user.getUserId();
        PipeLineScheduleListResponse response = calendarQueryService.getPipelineScheduleLists(userId);
        return ResponseEntity.ok(ApiResponse.success(response));

    }
}
