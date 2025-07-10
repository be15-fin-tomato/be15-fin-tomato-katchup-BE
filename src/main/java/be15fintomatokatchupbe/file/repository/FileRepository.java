package be15fintomatokatchupbe.file.repository;

import be15fintomatokatchupbe.campaign.command.domain.aggregate.entity.Pipeline;
import be15fintomatokatchupbe.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileKey(String key);

    void deleteAllByPipeline(Pipeline pipeline);

    @Modifying
    @Query("DELETE FROM File f WHERE f.pipeline.pipelineId = :pipelineId AND f.fileId NOT IN :existingFileId")
    void deleteAllByPipelineIdExcept(@Param("pipelineId") Long pipelineId, @Param("existingFileId") List<Long> existingFileId);
}
