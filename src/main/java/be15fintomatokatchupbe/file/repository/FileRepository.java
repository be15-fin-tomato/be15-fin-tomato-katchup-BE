package be15fintomatokatchupbe.file.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileKey(String key);

    void deleteAllByPipeline(Pipeline pipeline);
}
