package md.utm2026.demo.repository;

import md.utm2026.demo.domain.UserEntity;
import md.utm2026.demo.web.dto.UserEntityDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("""
            select new md.utm2026.demo.web.dto.UserEntityDto(
                u.id,
                u.userName,
                u.firstName,
                u.lastName,
                u.email,
                u.phone
            )
            from UserEntity u
            """)
    Page<UserEntityDto> findAllDtos(Pageable pageable);

    @Query("""
            select new md.utm2026.demo.web.dto.UserEntityDto(
                u.id,
                u.userName,
                u.firstName,
                u.lastName,
                u.email,
                u.phone
            )
            from UserEntity u
            where u.id = :id
            """)
    Optional<UserEntityDto> findDtoById(@Param("id") Long id);

//    @EntityGraph(
//            attributePaths = {
//                    "tasks",
//                    "tasks.taskStatus",
//            }
//    )
//    Optional<UserEntity> findById(Long id);
}
