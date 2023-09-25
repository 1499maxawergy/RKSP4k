package ru.maxawergy.rkss_5pr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.maxawergy.rkss_5pr.model.FileEntity;

@Repository
public interface FileEntityRepository  extends JpaRepository<FileEntity, Long> {
}
