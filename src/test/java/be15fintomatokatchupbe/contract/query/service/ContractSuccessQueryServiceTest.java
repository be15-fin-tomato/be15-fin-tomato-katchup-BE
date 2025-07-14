package be15fintomatokatchupbe.contract.query.service;

import be15fintomatokatchupbe.common.dto.Pagination;
import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.command.domain.entity.Contract;
import be15fintomatokatchupbe.contract.command.domain.entity.ContractFile;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractFileRepository;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractRepository;
import be15fintomatokatchupbe.contract.exception.ContractErrorCode;
import be15fintomatokatchupbe.contract.query.dto.request.ContractSuccessRequest;
import be15fintomatokatchupbe.contract.query.dto.response.ContractSuccessDTO;
import be15fintomatokatchupbe.contract.query.dto.response.ContractSuccessResponse;
import be15fintomatokatchupbe.contract.query.dto.response.ContractViewDTO;
import be15fintomatokatchupbe.contract.query.dto.response.ContractViewResponse;
import be15fintomatokatchupbe.contract.query.mapper.ContractSuccessMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractSuccessQueryServiceTest {

    @Mock
    private ContractSuccessMapper contractSuccessMapper;
    @Mock
    private ContractFileRepository contractFileRepository;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ContractSuccessQueryService contractSuccessQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetContractSuccess_Success() {
        // Given
        ContractSuccessRequest request = ContractSuccessRequest.builder()
                .page(1)
                .size(10)
                .build();

        List<ContractSuccessDTO> mockContractSuccessList = Arrays.asList(
                ContractSuccessDTO.builder().contractId(1L).originalName("Contract 1").build(),
                ContractSuccessDTO.builder().contractId(2L).originalName("Contract 2").build()
        );
        int totalList = 20;

        when(contractSuccessMapper.getContractSuccess(request)).thenReturn(mockContractSuccessList);
        when(contractSuccessMapper.getTotalList(request)).thenReturn(totalList);

        // When
        ContractSuccessResponse response = contractSuccessQueryService.getContractSuccess(request);

        // Then
        assertNotNull(response);
        assertEquals(mockContractSuccessList, response.getContractSuccess());
        assertNotNull(response.getPagination());
        assertEquals(10, response.getPagination().getSize());
        assertEquals(1, response.getPagination().getCurrentPage());
        assertEquals(2, response.getPagination().getTotalPage());
        assertEquals(totalList, response.getPagination().getTotalCount());

        verify(contractSuccessMapper, times(1)).getContractSuccess(request);
        verify(contractSuccessMapper, times(1)).getTotalList(request);
    }

    @Test
    void testGetContractSuccess_EmptyList() {
        // Given
        ContractSuccessRequest request = ContractSuccessRequest.builder()
                .page(1)
                .size(10)
                .build();

        List<ContractSuccessDTO> mockContractSuccessList = Collections.emptyList();
        int totalList = 0;

        when(contractSuccessMapper.getContractSuccess(request)).thenReturn(mockContractSuccessList);
        when(contractSuccessMapper.getTotalList(request)).thenReturn(totalList);

        // When
        ContractSuccessResponse response = contractSuccessQueryService.getContractSuccess(request);

        // Then
        assertNotNull(response);
        assertTrue(response.getContractSuccess().isEmpty());
        assertNotNull(response.getPagination());
        assertEquals(10, response.getPagination().getSize());
        assertEquals(1, response.getPagination().getCurrentPage());
        assertEquals(0, response.getPagination().getTotalPage());
        assertEquals(totalList, response.getPagination().getTotalCount());

        verify(contractSuccessMapper, times(1)).getContractSuccess(request);
        verify(contractSuccessMapper, times(1)).getTotalList(request);
    }

    @Test
    void testGetContentView_Success() {
        // Given
        Long contractId = 1L;
        String password = "testPassword";
        String encodedPassword = "encodedTestPassword";

        Contract mockContract = Contract.builder()
                .contractId(contractId)
                .file(ContractFile.builder().fileId(100L).build()) // Assume ContractFile is an entity
                .build();

        ContractFile mockContractFile = ContractFile.builder()
                .fileId(100L)
                .password(encodedPassword)
                .build();

        List<ContractViewDTO> mockContentViewList = Arrays.asList(
                ContractViewDTO.builder().originalName("file.pdf").filePath("/path/to/file.pdf").build()
        );

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(mockContract));
        when(contractFileRepository.findById(mockContract.getFile().getFileId())).thenReturn(Optional.of(mockContractFile));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(contractSuccessMapper.getContentView(mockContract.getFile().getFileId())).thenReturn(mockContentViewList);

        // When
        ContractViewResponse response = contractSuccessQueryService.getContentView(contractId, password);

        // Then
        assertNotNull(response);
        assertEquals(mockContentViewList, response.getContractView());

        verify(contractRepository, times(1)).findById(contractId);
        verify(contractFileRepository, times(1)).findById(mockContract.getFile().getFileId());
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verify(contractSuccessMapper, times(1)).getContentView(mockContract.getFile().getFileId());
    }

    @Test
    void testGetContentView_ContractNotFound() {
        // Given
        Long contractId = 1L;
        String password = "testPassword";

        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        // When & Then
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            contractSuccessQueryService.getContentView(contractId, password);
        });

        assertEquals(ContractErrorCode.NOT_FOUND, thrown.getErrorCode());
        verify(contractRepository, times(1)).findById(contractId);
        verifyNoInteractions(contractFileRepository, passwordEncoder, contractSuccessMapper);
    }

    @Test
    void testGetContentView_ContractNotRegistered() {
        // Given
        Long contractId = 1L;
        String password = "testPassword";

        Contract mockContract = Contract.builder()
                .contractId(contractId)
                .file(null) // file is null
                .build();

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(mockContract));

        // When & Then
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            contractSuccessQueryService.getContentView(contractId, password);
        });

        assertEquals(ContractErrorCode.CONTRACT_NOT_REGISTERED, thrown.getErrorCode());
        verify(contractRepository, times(1)).findById(contractId);
        verifyNoInteractions(contractFileRepository, passwordEncoder, contractSuccessMapper);
    }

    @Test
    void testGetContentView_NonUploadFile() {
        // Given
        Long contractId = 1L;
        String password = "testPassword";

        Contract mockContract = Contract.builder()
                .contractId(contractId)
                .file(ContractFile.builder().fileId(100L).build())
                .build();

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(mockContract));
        when(contractFileRepository.findById(mockContract.getFile().getFileId())).thenReturn(Optional.empty()); // ContractFile not found

        // When & Then
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            contractSuccessQueryService.getContentView(contractId, password);
        });

        assertEquals(ContractErrorCode.NON_UPLOAD_FILE, thrown.getErrorCode());
        verify(contractRepository, times(1)).findById(contractId);
        verify(contractFileRepository, times(1)).findById(mockContract.getFile().getFileId());
        verifyNoInteractions(passwordEncoder, contractSuccessMapper);
    }

    @Test
    void testGetContentView_PasswordError() {
        // Given
        Long contractId = 1L;
        String password = "wrongPassword";
        String encodedPassword = "encodedTestPassword";

        Contract mockContract = Contract.builder()
                .contractId(contractId)
                .file(ContractFile.builder().fileId(100L).build())
                .build();

        ContractFile mockContractFile = ContractFile.builder()
                .fileId(100L)
                .password(encodedPassword)
                .build();

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(mockContract));
        when(contractFileRepository.findById(mockContract.getFile().getFileId())).thenReturn(Optional.of(mockContractFile));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false); // Password mismatch

        // When & Then
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            contractSuccessQueryService.getContentView(contractId, password);
        });

        assertEquals(ContractErrorCode.PASSWORD_ERROR, thrown.getErrorCode());
        verify(contractRepository, times(1)).findById(contractId);
        verify(contractFileRepository, times(1)).findById(mockContract.getFile().getFileId());
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
        verifyNoInteractions(contractSuccessMapper);
    }
}