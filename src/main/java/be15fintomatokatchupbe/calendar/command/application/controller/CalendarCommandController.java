package be15fintomatokatchupbe.calendar.command.application.controller;

import be15fintomatokatchupbe.calendar.command.application.dto.CreateScheduleRequestDto;
import be15fintomatokatchupbe.calendar.command.application.service.CalendarCommandService;
import be15fintomatokatchupbe.common.dto.ApiResponse;
import be15fintomatokatchupbe.config.security.model.CustomUserDetail;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/calendar")
public class CalendarCommandController {
//    private final CalendarCommandService calendarCommandService;

//    @PostMapping("/create")
//    public ResponseEntity<ApiResponse<Void>> createSchedule(
//            @AuthenticationPrincipal CustomUserDetail userDetail,
//            @RequestBody @Valid CreateScheduleRequestDto dto
//    ) {
//        calendarCommandService.create(userDetail.getUserId(), dto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
//    }

}
