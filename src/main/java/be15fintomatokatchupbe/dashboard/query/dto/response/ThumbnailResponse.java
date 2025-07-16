package be15fintomatokatchupbe.dashboard.query.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThumbnailResponse {
    private String thumbnailUrl;
    private String videoId;
    private String title;
}