package be15fintomatokatchupbe.user.command.application.repository;

import be15fintomatokatchupbe.user.command.domain.aggregate.PicFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PicFileRepository extends JpaRepository<PicFile, Long> {
}
