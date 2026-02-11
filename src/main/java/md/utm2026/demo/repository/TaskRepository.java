package md.utm2026.demo.repository;

import md.utm2026.demo.domain.TaskEntity;
import md.utm2026.demo.web.dto.TaskEntityDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    @Query("""
            select new md.utm2026.demo.web.dto.TaskEntityDto(
                t.id,
                t.title,
                t.description,
                ts.name,
                a.userName
            )
            from TaskEntity t
            join t.taskStatus ts
            left join t.assignee a
            """)
    Page<TaskEntityDto> findAllDtos(Pageable pageable);

    @Query("""
            select new md.utm2026.demo.web.dto.TaskEntityDto(
                t.id,
                t.title,
                t.description,
                ts.name,
                a.userName
            )
            from TaskEntity t
            join t.taskStatus ts
            left join t.assignee a
            where t.id = :id
            """)
    Optional<TaskEntityDto> findDtoById(@Param("id") Long id);
}
