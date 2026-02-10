package md.utm2026.demo.web.dto;

import md.utm2026.demo.domain.UserEntity;

public class UserEntityDto {

    private final Long id;
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;

    public UserEntityDto(Long id, String userName, String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
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
