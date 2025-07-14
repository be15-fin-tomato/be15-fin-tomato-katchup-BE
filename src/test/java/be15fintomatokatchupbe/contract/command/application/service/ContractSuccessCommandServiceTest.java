package be15fintomatokatchupbe.contract.command.application.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.command.domain.entity.Contract;
import be15fintomatokatchupbe.contract.command.domain.entity.ContractFile;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractRepository;
import be15fintomatokatchupbe.contract.exception.ContractErrorCode;
import be15fintomatokatchupbe.file.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContractSuccessCommandServiceTest {

    private ContractRepository contractRepository;
    private FileService fileService;
    private PasswordEncoder passwordEncoder;
    private ContractSuccessCommandService contractSuccessCommandService;

    @BeforeEach
    void setUp() {
        contractRepository = mock(ContractRepository.class);
        fileService = mock(FileService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        contractSuccessCommandService = new ContractSuccessCommandService(contractRepository, fileService, passwordEncoder);
    }

    @Test
    void signContract_successfully() {
        // given
        Long contractId = 1L;
        String rawPassword = "1234";
        String encodedPassword = "encodedPassword";

        Contract contract = Contract.builder()
                .contractId(contractId)
                .build();

        MultipartFile file = mock(MultipartFile.class);
        ContractFile contractFile = ContractFile.builder()
                .fileId(1L)
                .build();

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(fileService.uploadContractFile(anyList())).thenReturn(List.of(contractFile));
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        // when
        contractSuccessCommandService.signContract(contractId, List.of(file), rawPassword);

        // then
        verify(fileService).saveContractFile(anyList(), eq(encodedPassword));
        verify(contractRepository).save(contract);
        assertEquals(contractFile, contract.getFile());
        assertNotNull(contract.getUpdatedAt());
    }

    @Test
    void signContract_contractNotFound_shouldThrowException() {
        // given
        Long contractId = 1L;
        when(contractRepository.findById(contractId)).thenReturn(Optional.empty());

        // when & then
        BusinessException exception = assertThrows(BusinessException.class,
                () -> contractSuccessCommandService.signContract(contractId, List.of(), "1234"));

        assertEquals(ContractErrorCode.NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void signContract_withEmptyFiles_shouldNotSaveOrUpload() {
        // given
        Long contractId = 1L;
        Contract contract = Contract.builder()
                .contractId(contractId)
                .build();

        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));

        // when
        contractSuccessCommandService.signContract(contractId, List.of(), "password");

        // then
        verify(fileService, never()).uploadContractFile(anyList());
        verify(fileService, never()).saveContractFile(anyList(), anyString());
        verify(contractRepository, never()).save(any());
    }
}
