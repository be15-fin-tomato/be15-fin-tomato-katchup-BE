package be15fintomatokatchupbe.calendar.command.application.controller;

import be15fintomatokatchupbe.calendar.command.application.dto.request.CreateScheduleRequestDto;
import be15fintomatokatchupbe.calendar.command.application.dto.request.UpdateScheduleRequestDto;
import be15fintomatokatchupbe.calendar.command.application.service.CalendarCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarCommandController {
    private final CalendarCommandService calendarCommandService;

    @PostMapping("/create")
    @Operation(summary = "캘린더 일정 등록", description = "사용자는 캘린더에 개인 일정을 등록할 수 있다.")
    public ResponseEntity<ApiResponse<Void>> createSchedule(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestBody @Valid CreateScheduleRequestDto dto
    ) {
        calendarCommandService.create(userDetail.getUserId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }

    @PatchMapping("/{scheduleId}")
    @Operation(summary = "캘린더 일정 수정", description = "사용자는 캘린더에 등록한 개인 일정 항목을 수정할 수 있다.")
    public ResponseEntity<ApiResponse<Void>> updateSchedule(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long scheduleId,
            @RequestBody UpdateScheduleRequestDto dto
    ){
        calendarCommandService.update(userDetail.getUserId(), scheduleId, dto);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @DeleteMapping("/{scheduleId}")
    @Operation(summary = "캘린더 일정 삭제", description = "사용자는 캘린더에 등록한 개인 일정을 삭제할 수 있다.")
    public ResponseEntity<ApiResponse<Void>> deleteSchedule(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long scheduleId
    ){
        calendarCommandService.delete(userDetail.getUserId(), scheduleId);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}