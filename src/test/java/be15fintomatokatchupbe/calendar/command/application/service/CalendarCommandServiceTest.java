package be15fintomatokatchupbe.calendar.command.application.service;

import be15fintomatokatchupbe.calendar.command.application.dto.request.CreateScheduleRequestDto;
import be15fintomatokatchupbe.calendar.command.application.dto.request.UpdateScheduleRequestDto;
import be15fintomatokatchupbe.calendar.command.domain.aggregate.Schedule;
import be15fintomatokatchupbe.calendar.command.mapper.ScheduleCommandMapper;
import be15fintomatokatchupbe.calendar.command.repository.ScheduleRepository;
import be15fintomatokatchupbe.calendar.exception.CalendarErrorCode;
import be15fintomatokatchupbe.common.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalendarCommandServiceTest {

    private ScheduleRepository scheduleRepository;
    private ScheduleCommandMapper mapper;
    private CalendarCommandService calendarCommandService;

    @BeforeEach
    void setUp() {
        scheduleRepository = mock(ScheduleRepository.class);
        mapper = mock(ScheduleCommandMapper.class);
        calendarCommandService = new CalendarCommandService(scheduleRepository, mapper);
    }

    @Test
    void create_success() {
        Long userId = 1L;
        CreateScheduleRequestDto dto = mock(CreateScheduleRequestDto.class);
        Schedule schedule = mock(Schedule.class);

        when(mapper.toEntity(dto, userId)).thenReturn(schedule);
        when(dto.getStartTime()).thenReturn(LocalTime.of(9, 0));
        when(dto.getEndTime()).thenReturn(LocalTime.of(10, 0));

        assertDoesNotThrow(() -> calendarCommandService.create(userId, dto));
        verify(scheduleRepository).save(schedule);
    }

    @Test
    void create_invalidTime_throwsException() {
        Long userId = 1L;
        CreateScheduleRequestDto dto = mock(CreateScheduleRequestDto.class);
        Schedule schedule = mock(Schedule.class);

        when(mapper.toEntity(dto, userId)).thenReturn(schedule);
        when(dto.getStartTime()).thenReturn(LocalTime.of(14, 0));
        when(dto.getEndTime()).thenReturn(LocalTime.of(13, 0));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                calendarCommandService.create(userId, dto));
        assertEquals(CalendarErrorCode.INVALID_TIME, ex.getErrorCode());
    }

    @Test
    void update_success() {
        Long userId = 1L;
        Long scheduleId = 2L;
        UpdateScheduleRequestDto dto = mock(UpdateScheduleRequestDto.class);
        Schedule schedule = mock(Schedule.class);

        when(scheduleRepository.findByScheduleIdAndUserId(scheduleId, userId)).thenReturn(Optional.of(schedule));
        when(dto.getStartTime()).thenReturn(LocalTime.of(10, 0));
        when(dto.getEndTime()).thenReturn(LocalTime.of(12, 0));

        assertDoesNotThrow(() -> calendarCommandService.update(userId, scheduleId, dto));

        verify(schedule).update(dto);
        verify(scheduleRepository).save(schedule);
    }

    @Test
    void update_invalidTime_throwsException() {
        Long userId = 1L;
        Long scheduleId = 2L;
        UpdateScheduleRequestDto dto = mock(UpdateScheduleRequestDto.class);
        Schedule schedule = mock(Schedule.class);

        when(scheduleRepository.findByScheduleIdAndUserId(scheduleId, userId)).thenReturn(Optional.of(schedule));
        when(dto.getStartTime()).thenReturn(LocalTime.of(13, 0));
        when(dto.getEndTime()).thenReturn(LocalTime.of(11, 0));

        BusinessException ex = assertThrows(BusinessException.class, () ->
                calendarCommandService.update(userId, scheduleId, dto));
        assertEquals(CalendarErrorCode.INVALID_TIME, ex.getErrorCode());
    }

    @Test
    void update_scheduleNotFound_throwsException() {
        when(scheduleRepository.findByScheduleIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () ->
                calendarCommandService.update(1L, 1L, mock(UpdateScheduleRequestDto.class)));
        assertEquals(CalendarErrorCode.ACCESS_DENIED, ex.getErrorCode());
    }

    @Test
    void delete_success() {
        Long userId = 1L;
        Long scheduleId = 2L;
        Schedule schedule = mock(Schedule.class);

        when(scheduleRepository.findByScheduleIdAndUserId(scheduleId, userId)).thenReturn(Optional.of(schedule));

        assertDoesNotThrow(() -> calendarCommandService.delete(userId, scheduleId));
        verify(scheduleRepository).delete(schedule);
    }

    @Test
    void delete_scheduleNotFound_throwsException() {
        when(scheduleRepository.findByScheduleIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class, () ->
                calendarCommandService.delete(1L, 1L));
        assertEquals(CalendarErrorCode.ACCESS_DENIED, ex.getErrorCode());
    }
}
