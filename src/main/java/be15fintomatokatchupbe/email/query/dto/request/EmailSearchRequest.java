package be15fintomatokatchupbe.email.query.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmailSearchRequest {

    private Integer page = 1;
    private Integer size = 20;
    private String campaignName;

    public int getOffset() {
        return (page - 1) * size;
    }

    public int getLimit() {
        return size;
    }
}
