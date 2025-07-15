package be15fintomatokatchupbe.contract.query.service;

import be15fintomatokatchupbe.contract.query.dto.response.ContractPageResponse;
import be15fintomatokatchupbe.contract.query.dto.response.DetailInfoResponse;
import be15fintomatokatchupbe.contract.query.dto.response.DetailListResponse;
import be15fintomatokatchupbe.contract.query.dto.response.ObjectResponse;
import be15fintomatokatchupbe.contract.query.mapper.DetailMapper;
import be15fintomatokatchupbe.contract.query.mapper.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractQueryServiceTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private DetailMapper detailMapper;

    @InjectMocks
    private ContractQueryService contractQueryService;

    @BeforeEach
    void setUp() {
        // Mockito 목 객체 초기화
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetContractPage_withDetailId() {
        Long objectId = 1L;
        Long detailId = 10L;

        // Mocking responses (가짜 응답 객체 생성)
        ObjectResponse objectResponse = new ObjectResponse(1L, "계약 객체 제목");
        List<DetailListResponse> detailList = List.of(new DetailListResponse(10L, "소제목", LocalDateTime.now()));

        DetailInfoResponse detailInfo = new DetailInfoResponse(
                10L,
                "소제목",
                "내용",
                LocalDateTime.now(), // createdAt
                LocalDateTime.now(), // updatedAt
                new DetailInfoResponse.FileInfo(1L, "파일.pdf", "/path/to/file/파일.pdf", "pdf") // FileInfo 객체
        );

        // Stubbing mapper methods (매퍼 메소드 동작 정의)
        when(objectMapper.selectObjectById(objectId)).thenReturn(objectResponse);
        when(detailMapper.selectDetailsByObjectId(objectId)).thenReturn(detailList);
        when(detailMapper.selectDetailById(detailId)).thenReturn(detailInfo);

        // Call the service method (서비스 메소드 호출)
        ContractPageResponse result = contractQueryService.getContractPage(objectId, detailId);

        // Assertions (결과 검증)
        assertNotNull(result);
        assertEquals(objectResponse, result.getObject());
        assertEquals(detailList, result.getDetails());
        assertEquals(detailInfo, result.getSelectedDetail());

        // Verify mapper methods were called (매퍼 메소드 호출 확인)
        verify(objectMapper, times(1)).selectObjectById(objectId);
        verify(detailMapper, times(1)).selectDetailsByObjectId(objectId);
        verify(detailMapper, times(1)).selectDetailById(detailId);
    }

    @Test
    void testGetContractPage_withoutDetailId() {
        Long objectId = 1L;

        // Mocking responses
        ObjectResponse objectResponse = new ObjectResponse(1L, "계약 객체 제목");
        List<DetailListResponse> detailList = List.of(new DetailListResponse(20L, "다른 소제목", LocalDateTime.now()));

        // Stubbing mapper methods
        when(objectMapper.selectObjectById(objectId)).thenReturn(objectResponse);
        when(detailMapper.selectDetailsByObjectId(objectId)).thenReturn(detailList);

        // Call the service method
        ContractPageResponse result = contractQueryService.getContractPage(objectId, null);

        // Assertions
        assertNotNull(result);
        assertEquals(objectResponse, result.getObject());
        assertEquals(detailList, result.getDetails());
        assertNull(result.getSelectedDetail()); // detailId가 null이므로 selectedDetail은 null이어야 함

        // Verify mapper methods were called
        verify(objectMapper, times(1)).selectObjectById(objectId);
        verify(detailMapper, times(1)).selectDetailsByObjectId(objectId);
        verify(detailMapper, never()).selectDetailById(anyLong()); // detailId가 null이므로 selectDetailById는 호출되지 않아야 함
    }

    @Test
    void testGetContractPage_objectNotFound() {
        Long objectId = 99L; // 존재하지 않는 objectId
        Long detailId = 10L;

        // Mocking behavior: objectMapper가 null을 반환
        when(objectMapper.selectObjectById(objectId)).thenReturn(null);
        when(detailMapper.selectDetailsByObjectId(objectId)).thenReturn(Collections.emptyList()); // 객체가 없으니 상세 목록도 비어있어야 함
        when(detailMapper.selectDetailById(detailId)).thenReturn(null); // 객체가 없어도 detailId가 있으면 호출은 됨

        ContractPageResponse result = contractQueryService.getContractPage(objectId, detailId);

        assertNotNull(result);
        assertNull(result.getObject()); // Object가 null임을 확인
        assertTrue(result.getDetails().isEmpty()); // Details가 비어있음을 확인
        assertNull(result.getSelectedDetail()); // SelectedDetail이 null임을 확인

        verify(objectMapper, times(1)).selectObjectById(objectId);
        verify(detailMapper, times(1)).selectDetailsByObjectId(objectId);
        verify(detailMapper, times(1)).selectDetailById(detailId); // detailId가 전달되었으므로 호출은 되어야 함
    }

    @Test
    void testGetContractPage_detailNotFoundWithDetailId() {
        Long objectId = 1L;
        Long detailId = 99L; // 존재하지 않는 detailId

        ObjectResponse objectResponse = new ObjectResponse(1L, "계약 객체 제목");
        List<DetailListResponse> detailList = List.of(new DetailListResponse(10L, "소제목", LocalDateTime.now()));

        // Stubbing mapper methods: detailMapper가 null을 반환
        when(objectMapper.selectObjectById(objectId)).thenReturn(objectResponse);
        when(detailMapper.selectDetailsByObjectId(objectId)).thenReturn(detailList);
        when(detailMapper.selectDetailById(detailId)).thenReturn(null); // Detail이 없을 경우

        ContractPageResponse result = contractQueryService.getContractPage(objectId, detailId);

        assertNotNull(result);
        assertEquals(objectResponse, result.getObject());
        assertEquals(detailList, result.getDetails());
        assertNull(result.getSelectedDetail()); // SelectedDetail이 null임을 확인

        verify(objectMapper, times(1)).selectObjectById(objectId);
        verify(detailMapper, times(1)).selectDetailsByObjectId(objectId);
        verify(detailMapper, times(1)).selectDetailById(detailId);
    }

    @Test
    void testGetAllObjects() {
        // Mocking responses
        List<ObjectResponse> expected = List.of(
                new ObjectResponse(1L, "오브젝트1"),
                new ObjectResponse(2L, "오브젝트2")
        );

        // Stubbing mapper method
        when(objectMapper.selectAllObjects()).thenReturn(expected);

        // Call the service method
        List<ObjectResponse> result = contractQueryService.getAllObjects();

        // Assertions
        assertEquals(expected.size(), result.size());
        assertEquals(expected, result);

        // Verify mapper method was called
        verify(objectMapper, times(1)).selectAllObjects();
    }

    @Test
    void testGetAllObjects_noObjectsExist() {
        // Mocking behavior: objectMapper가 빈 리스트를 반환
        when(objectMapper.selectAllObjects()).thenReturn(Collections.emptyList());

        List<ObjectResponse> result = contractQueryService.getAllObjects();

        assertNotNull(result);
        assertTrue(result.isEmpty()); // 리스트가 비어있음을 확인

        verify(objectMapper, times(1)).selectAllObjects();
    }
}