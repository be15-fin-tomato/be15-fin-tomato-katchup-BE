package be15fintomatokatchupbe.relation.service;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.relation.domain.PipelineUser;
import be15fintomatokatchupbe.relation.repository.PipeUserRepository;
import be15fintomatokatchupbe.user.command.application.service.UserCommendService;
import be15fintomatokatchupbe.user.command.application.support.UserHelperService;
import be15fintomatokatchupbe.user.command.domain.aggregate.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PipeUserService {
    private final UserHelperService userHelperService;
    private final PipeUserRepository pipeUserRepository;

    public void saveUserList(List<Long> userList, Pipeline pipeline){
        List<User> foundUserList = userHelperService.findValidUserList(userList);

        List<PipelineUser> pipelineUserList = foundUserList.stream().map(user ->
            PipelineUser.builder()
                    .pipeline(pipeline)
                    .user(user)
                    .build()

        ).toList();

        pipeUserRepository.saveAll(pipelineUserList);
    }
}
