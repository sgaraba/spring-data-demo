package md.utm2026.demo.service;

import md.utm2026.demo.domain.TaskStatusEntity;
import md.utm2026.demo.repository.TaskStatusRepository;
import md.utm2026.demo.service.dto.CreateTaskStatusEntityDto;
import md.utm2026.demo.service.dto.TaskStatusEntityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskStatusService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStatusService.class);

    private final TaskStatusRepository taskStatusRepository;

    public TaskStatusService(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    public Page<TaskStatusEntityDto> findAll(Pageable pageable) {
        LOGGER.info("Fetching task statuses page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return taskStatusRepository.findAllDtos(pageable);
    }

    public Optional<TaskStatusEntityDto> findById(Long id) {
        LOGGER.info("Fetching task status by id={}", id);
        return taskStatusRepository.findDtoById(id);
    }

    public TaskStatusEntityDto create(CreateTaskStatusEntityDto taskStatus) {
        LOGGER.info("Creating task status name={}", taskStatus.name());
        TaskStatusEntity entity = new TaskStatusEntity();
        applyDto(entity, taskStatus);
        TaskStatusEntity created = taskStatusRepository.save(entity);
        return taskStatusRepository.findDtoById(created.getId())
                .orElseThrow(() -> new IllegalStateException("Created task status not found id=" + created.getId()));
    }

    public Optional<TaskStatusEntityDto> update(Long id, CreateTaskStatusEntityDto incoming) {
        LOGGER.info("Updating task status id={}", id);
        return taskStatusRepository.findById(id).map(existing -> {
            applyDto(existing, incoming);
            taskStatusRepository.save(existing);
            return taskStatusRepository.findDtoById(id)
                    .orElseThrow(() -> new IllegalStateException("Updated task status not found id=" + id));
        });
    }

    public Optional<TaskStatusEntityDto> patch(Long id, CreateTaskStatusEntityDto incoming) {
        LOGGER.info("Patching task status id={}", id);
        return taskStatusRepository.findById(id).map(existing -> {
            applyPatch(existing, incoming);
            taskStatusRepository.save(existing);
            return taskStatusRepository.findDtoById(id)
                    .orElseThrow(() -> new IllegalStateException("Patched task status not found id=" + id));
        });
    }

    public boolean delete(Long id) {
        LOGGER.info("Deleting task status id={}", id);
        if (!taskStatusRepository.existsById(id)) {
            LOGGER.info("Task status not found id={}", id);
            return false;
        }
        taskStatusRepository.deleteById(id);
        return true;
    }

    private static void applyDto(TaskStatusEntity entity, CreateTaskStatusEntityDto incoming) {
        entity.setName(incoming.name());
        entity.setActive(incoming.active());
    }

    private static void applyPatch(TaskStatusEntity entity, CreateTaskStatusEntityDto incoming) {
        if (incoming.name() != null) {
            entity.setName(incoming.name());
        }
        if (incoming.active() != null) {
            entity.setActive(incoming.active());
        }
    }
}
