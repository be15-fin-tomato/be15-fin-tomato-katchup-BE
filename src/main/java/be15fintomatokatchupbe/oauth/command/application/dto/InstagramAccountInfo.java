package be15fintomatokatchupbe.oauth.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstagramAccountInfo {
    private String accountId;
    private String name;
    private Long follower;
}
