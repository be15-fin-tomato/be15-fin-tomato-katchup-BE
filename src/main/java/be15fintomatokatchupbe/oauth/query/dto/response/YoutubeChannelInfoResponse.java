package be15fintomatokatchupbe.oauth.query.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record YoutubeChannelInfoResponse(
        List<Item> items
) {
        public record Item(
                String id,
                Snippet snippet,
                Statistics statistics
        ) {}

        public record Snippet(
                String title,
                Thumbnails thumbnails
        ) {}

        public record Thumbnails(
                @JsonProperty("default")
                Thumbnail defaultThumbnail
        ) {}

        public record Thumbnail(
                String url
        ) {}

        public record Statistics(
                @JsonProperty("subscriberCount")
                Long subscriberCount
        ) {}
}
