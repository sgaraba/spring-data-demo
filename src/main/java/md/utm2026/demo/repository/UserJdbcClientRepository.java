package md.utm2026.demo.repository;

import md.utm2026.demo.web.dto.UserEntityDto;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserJdbcClientRepository {

    private final JdbcClient jdbcClient;

    public UserJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<UserEntityDto> findDtoByEmail(String email) {
        return jdbcClient.sql("""
                        select id, user_name, first_name, last_name, email, phone
                        from user_entity
                        where email = :email
                        """)
                .param("email", email)
                .query((rs, rowNum) -> new UserEntityDto(
                        rs.getLong("id"),
                        rs.getString("user_name"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ))
                .optional();
    }
}
