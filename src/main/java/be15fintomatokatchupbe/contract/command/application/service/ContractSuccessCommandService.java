package be15fintomatokatchupbe.contract.command.application.service;

import be15fintomatokatchupbe.common.exception.BusinessException;
import be15fintomatokatchupbe.contract.command.domain.entity.Contract;
import be15fintomatokatchupbe.contract.command.domain.entity.ContractFile;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractFileRepository;
import be15fintomatokatchupbe.contract.command.domain.repository.ContractRepository;
import be15fintomatokatchupbe.contract.exception.ContractErrorCode;
import be15fintomatokatchupbe.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractSuccessCommandService {

    private final ContractRepository contractRepository;
    private final FileService fileService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signContract(Long contractId, List<MultipartFile> files, String password) {

        /* 계약서 ID가 있는지 확인 */
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow( () -> new BusinessException(ContractErrorCode.NOT_FOUND));

        /* ContractFile 에 파일 추가 */
        if(files != null && !files.isEmpty() ){
            List<ContractFile> fileList = fileService.uploadContractFile(files);
            String encryptPassword = passwordEncoder.encode(password);
            fileService.saveContractFile(fileList, encryptPassword);

            contract.setFile(fileList.get(0));
            contract.setUpdatedAt(LocalDateTime.now());
            contractRepository.save(contract);
        }
    }
}
