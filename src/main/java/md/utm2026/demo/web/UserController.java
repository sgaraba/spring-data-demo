package md.utm2026.demo.web;

import md.utm2026.demo.domain.UserEntity;
import md.utm2026.demo.service.UserService;
import md.utm2026.demo.web.dto.CreateUserEntityDto;
import md.utm2026.demo.web.dto.PageResponse;
import md.utm2026.demo.web.dto.UserEntityDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping
//    public PageResponse<UserEntityDto> getAll(Pageable pageable) {
//        var page = userService.findAllDto(pageable);
//        return new PageResponse<>(
//                page.getContent(),
//                page.getNumber(),
//                page.getSize(),
//                page.getTotalElements(),
//                page.getTotalPages()
//        );
//    }


//    @GetMapping
//    public PageResponse<UserEntity> getAll(@RequestParam int pageNr, @RequestParam int pageSize) {
//
//        PageRequest pageRequest = PageRequest.of(pageNr, pageSize);
//
//        Page<UserEntity> page = userService.findAll(pageRequest);
//        return new PageResponse<>(
//                page.getContent(),
//                page.getNumber(),
//                page.getSize(),
//                page.getTotalElements(),
//                page.getTotalPages()
//        );
//    }

    @GetMapping
    public PageResponse<UserEntity> getAll(Pageable pageable) {
        Page<UserEntity> page = userService.findAll(pageable);
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<UserEntity> getByEmail(@PathVariable String email) {
        return userService.findByEmailQuery(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


//    @GetMapping("/{id}")
//    public ResponseEntity<UserEntityDto> getById(@PathVariable Long id) {
//        return userService.findDtoById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }

    @PostMapping
    public ResponseEntity<UserEntityDto> create(@Valid @RequestBody CreateUserEntityDto user) {
        UserEntityDto created = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserEntityDto> update(@PathVariable Long id, @Valid @RequestBody CreateUserEntityDto user) {
        return userService.update(id, user)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserEntityDto> patch(@PathVariable Long id, @RequestBody CreateUserEntityDto user) {
        return userService.patch(id, user)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }
}
