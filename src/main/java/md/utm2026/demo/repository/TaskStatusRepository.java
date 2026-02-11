package md.utm2026.demo.repository;

import md.utm2026.demo.domain.TaskStatusEntity;
import md.utm2026.demo.service.dto.TaskStatusEntityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatusEntity, Long> {

    @Query("""
            select new md.utm2026.demo.service.dto.TaskStatusEntityDto(
                ts.id,
                ts.name,
                ts.active
            )
            from TaskStatusEntity ts
            """)
    Page<TaskStatusEntityDto> findAllDtos(Pageable pageable);

    @Query("""
            select new md.utm2026.demo.service.dto.TaskStatusEntityDto(
                ts.id,
                ts.name,
                ts.active
            )
            from TaskStatusEntity ts
            where ts.id = :id
            """)
    Optional<TaskStatusEntityDto> findDtoById(@Param("id") Long id);
}
