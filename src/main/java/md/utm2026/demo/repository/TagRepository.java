package md.utm2026.demo.repository;

import md.utm2026.demo.domain.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
