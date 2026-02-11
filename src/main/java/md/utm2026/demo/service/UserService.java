package md.utm2026.demo.service;

import md.utm2026.demo.domain.UserEntity;
import md.utm2026.demo.repository.UserRepository;
import md.utm2026.demo.service.dto.CreateUserEntityDto;
import md.utm2026.demo.service.dto.PageResponse;
import md.utm2026.demo.service.dto.UserEntityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PageResponse<UserEntityDto> findAll(Pageable pageable) {
        LOGGER.info("Fetching users page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        var page = userRepository.findAllDtos(pageable);
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    public Optional<UserEntityDto> findById(Long id) {
        LOGGER.info("Fetching user by id={}", id);
        return userRepository.findDtoById(id);
    }

    public UserEntityDto create(CreateUserEntityDto user) {
        LOGGER.info("Creating user userName={}", user.userName());
        UserEntity entity = new UserEntity();
        applyDto(entity, user);
        UserEntity created = userRepository.save(entity);
        return userRepository.findDtoById(created.getId())
                .orElseThrow(() -> new IllegalStateException("Created user not found id=" + created.getId()));
    }

    public Optional<UserEntityDto> update(Long id, CreateUserEntityDto incoming) {
        LOGGER.info("Updating user id={}", id);
        return userRepository.findById(id).map(existing -> {
            applyDto(existing, incoming);
            userRepository.save(existing);
            return userRepository.findDtoById(id)
                    .orElseThrow(() -> new IllegalStateException("Updated user not found id=" + id));
        });
    }

    public Optional<UserEntityDto> patch(Long id, CreateUserEntityDto incoming) {
        LOGGER.info("Patching user id={}", id);
        return userRepository.findById(id).map(existing -> {
            applyPatch(existing, incoming);
            userRepository.save(existing);
            return userRepository.findDtoById(id)
                    .orElseThrow(() -> new IllegalStateException("Patched user not found id=" + id));
        });
    }

    public boolean delete(Long id) {
        LOGGER.info("Deleting user id={}", id);
        if (!userRepository.existsById(id)) {
            LOGGER.info("User not found id={}", id);
            return false;
        }
        userRepository.deleteById(id);
        return true;
    }

    private static void applyDto(UserEntity entity, CreateUserEntityDto incoming) {
        entity.setUserName(incoming.userName());
        entity.setFirstName(incoming.firstName());
        entity.setLastName(incoming.lastName());
        entity.setEmail(incoming.email());
        entity.setPhone(incoming.phone());
    }

    private static void applyPatch(UserEntity entity, CreateUserEntityDto incoming) {
        if (incoming.userName() != null) {
            entity.setUserName(incoming.userName());
        }
        if (incoming.firstName() != null) {
            entity.setFirstName(incoming.firstName());
        }
        if (incoming.lastName() != null) {
            entity.setLastName(incoming.lastName());
        }
        if (incoming.email() != null) {
            entity.setEmail(incoming.email());
        }
        if (incoming.phone() != null) {
            entity.setPhone(incoming.phone());
        }
    }
}
