package md.utm2026.demo.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import md.utm2026.demo.domain.UserEntity;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntityDto {

    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public UserEntityDto(Long id, String userName, String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public UserEntityDto(String userName, String firstName, String lastName) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static UserEntityDto fromEntity(UserEntity entity) {
        return new UserEntityDto(
                entity.getId(),
                entity.getUserName(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhone()
        );
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
