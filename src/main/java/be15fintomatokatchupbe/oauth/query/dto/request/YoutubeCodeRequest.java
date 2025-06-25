package be15fintomatokatchupbe.oauth.query.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeCodeRequest {
    private String code;
}