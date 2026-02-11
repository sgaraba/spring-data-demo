package md.utm2026.demo.web;

import md.utm2026.demo.service.TaskStatusService;
import md.utm2026.demo.service.dto.CreateTaskStatusEntityDto;
import md.utm2026.demo.service.dto.PageResponse;
import md.utm2026.demo.service.dto.TaskStatusEntityDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/task-statuses")
public class TaskStatusController {

    private final TaskStatusService taskStatusService;

    public TaskStatusController(TaskStatusService taskStatusService) {
        this.taskStatusService = taskStatusService;
    }

    @GetMapping
    public PageResponse<TaskStatusEntityDto> getAll(Pageable pageable) {
        var page = taskStatusService.findAll(pageable);
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatusEntityDto> getById(@PathVariable Long id) {
        return taskStatusService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<TaskStatusEntityDto> create(@Valid @RequestBody CreateTaskStatusEntityDto taskStatus) {
        TaskStatusEntityDto created = taskStatusService.create(taskStatus);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskStatusEntityDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateTaskStatusEntityDto taskStatus
    ) {
        return taskStatusService.update(id, taskStatus)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskStatusEntityDto> patch(
            @PathVariable Long id,
            @RequestBody CreateTaskStatusEntityDto taskStatus
    ) {
        return taskStatusService.patch(id, taskStatus)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = taskStatusService.delete(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.noContent().build();
    }
}
