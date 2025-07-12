package be15fintomatokatchupbe.oauth.query.mapper;

import be15fintomatokatchupbe.oauth.query.dto.InstagramFullSnapshot;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InstagramQueryMapper {

    List<InstagramFullSnapshot> findLatestFullSnapshotByInfluencerId(Long influencerId);

}
