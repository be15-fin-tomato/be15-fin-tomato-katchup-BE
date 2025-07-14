package be15fintomatokatchupbe.contract.command.application.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.command.application.dto.request.DetailCreateRequest;
import be15fintomatokatchupbe.contract.command.application.dto.request.DetailUpdateRequest;
import be15fintomatokatchupbe.contract.command.domain.entity.ContractFile;
import be15fintomatokatchupbe.contract.command.domain.entity.Detail;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractFileRepository;
import be15fintomatokatchupbe.contract.command.domain.repository.DetailRepository;
import be15fintomatokatchupbe.contract.exception.ContractErrorCode;
import be15fintomatokatchupbe.file.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DetailCommandServiceTest {

    @InjectMocks
    private DetailCommandService detailCommandService;

    @Mock
    private DetailRepository detailRepository;

    @Mock
    private ContractFileRepository contractFileRepository;

    @Mock
    private FileService fileService;

    @Mock
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDetail_success() {
        DetailCreateRequest request = new DetailCreateRequest(1L, "subTitle", "content");

        ContractFile contractFile = ContractFile.builder()
                .fileId(1L)
                .originalName("test.pdf")
                .filePath("s3/test.pdf")
                .program("pdf")
                .build();

        when(mockFile.getOriginalFilename()).thenReturn("test.pdf");
        when(mockFile.isEmpty()).thenReturn(false);
        when(fileService.uploadContractFile(any())).thenReturn(List.of(contractFile));
        when(contractFileRepository.save(any())).thenReturn(contractFile);

        detailCommandService.createDetail(request, mockFile);

        verify(detailRepository, times(1)).save(any());
    }

    @Test
    void testUpdateDetail_success() {
        DetailUpdateRequest request = new DetailUpdateRequest("newTitle", "newContent");

        Detail detail = Detail.builder()
                .detailId(1L)
                .subTitle("oldTitle")
                .content("oldContent")
                .updatedAt(LocalDateTime.now())
                .build();

        ContractFile contractFile = ContractFile.builder()
                .fileId(2L)
                .originalName("file.docx")
                .filePath("s3/file.docx")
                .program("word")
                .build();

        when(detailRepository.findById(1L)).thenReturn(Optional.of(detail));
        when(mockFile.getOriginalFilename()).thenReturn("file.docx");
        when(mockFile.isEmpty()).thenReturn(false);
        when(fileService.uploadContractFile(any())).thenReturn(List.of(contractFile));
        when(contractFileRepository.save(any())).thenReturn(contractFile);

        detailCommandService.updateDetail(1L, request, mockFile);

        verify(detailRepository, times(1)).save(any());
    }

    @Test
    void testDeleteDetail_success() {
        Detail detail = Detail.builder()
                .detailId(1L)
                .fileId(100L)
                .build();

        ContractFile file = ContractFile.builder()
                .fileId(100L)
                .filePath("dummy/path/file.pdf")
                .build();

        when(detailRepository.findById(1L)).thenReturn(Optional.of(detail));
        when(contractFileRepository.findById(100L)).thenReturn(Optional.of(file));

        detailCommandService.deleteDetail(1L);

        verify(detailRepository, times(1)).delete(detail);
        verify(contractFileRepository, times(1)).delete(file);
    }

    @Test
    void testUpdateDetail_notFound() {
        when(detailRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () ->
                detailCommandService.updateDetail(1L, new DetailUpdateRequest("title", "content"), null)
        );
    }

    @Test
    void testDeleteDetail_notFound() {
        when(detailRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () ->
                detailCommandService.deleteDetail(1L)
        );
    }
}
