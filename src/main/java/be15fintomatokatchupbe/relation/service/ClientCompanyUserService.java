package be15fintomatokatchupbe.relation.service;

import be15fintomatokatchupbe.relation.domain.ClientCompanyUser;
import be15fintomatokatchupbe.client.command.domain.aggregate.ClientCompany;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import be15fintomatokatchupbe.relation.repository.ClientCompanyUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientCompanyUserService {

    private final ClientCompanyUserRepository repository;

    // 생성
    public void createRelations(ClientCompany clientCompany, List<User> users) {
        List<ClientCompanyUser> relations = users.stream()
                .map(user -> ClientCompanyUser.builder()
                        .clientCompany(clientCompany)
                        .user(user)
                        .build())
                .toList();

        repository.saveAll(relations);
    }

    // 하드 딜리트
    public void deleteByClientCompany(ClientCompany clientCompany) {
        repository.deleteByClientCompany(clientCompany);
    }
}