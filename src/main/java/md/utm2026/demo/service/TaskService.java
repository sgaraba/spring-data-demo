package md.utm2026.demo.service;

import md.utm2026.demo.domain.TaskEntity;
import md.utm2026.demo.domain.TaskStatusEntity;
import md.utm2026.demo.domain.UserEntity;
import md.utm2026.demo.repository.TaskRepository;
import md.utm2026.demo.repository.TaskStatusRepository;
import md.utm2026.demo.repository.UserRepository;
import md.utm2026.demo.web.dto.CreateTaskEntityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import md.utm2026.demo.web.dto.TaskEntityDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;

    public TaskService(
            TaskRepository taskRepository,
            TaskStatusRepository taskStatusRepository,
            UserRepository userRepository
    ) {
        this.taskRepository = taskRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly=true)
    public Page<TaskEntity> findAll(Pageable pageable) {
        LOGGER.info("Fetching tasks page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        // call external API
        return taskRepository.findAll(pageable);
    }

    @Transactional
    public Optional<TaskEntityDto> findDtoById(Long id) {
        LOGGER.info("Fetching task dto by id={}", id);
        return taskRepository.findDtoById(id);
    }

    @Transactional
    public Optional<TaskEntity> findById(Long id) {
        LOGGER.info("Fetching task by id={}", id);
        return taskRepository.findById(id);
    }

    @Transactional
    public TaskEntityDto create(CreateTaskEntityDto task) {
        LOGGER.info("Creating task title={}", task.title());
        TaskEntity entity = new TaskEntity();
        applyDto(entity, task);
        TaskEntity created = taskRepository.save(entity);
        return taskRepository.findDtoById(created.getId())
                .orElseThrow(() -> new IllegalStateException("Created task not found id=" + created.getId()));
    }

    @Transactional
    public Optional<TaskEntityDto> update(Long id, CreateTaskEntityDto incoming) {
        LOGGER.info("Updating task id={}", id);
        return taskRepository.findById(id).map(existing -> {
            applyDto(existing, incoming);
            taskRepository.save(existing);
            return taskRepository.findDtoById(id)
                    .orElseThrow(() -> new IllegalStateException("Updated task not found id=" + id));
        });
    }

    @Transactional
    public Optional<TaskEntityDto> patch(Long id, CreateTaskEntityDto incoming) {
        LOGGER.info("Patching task id={}", id);
        return taskRepository.findById(id).map(existing -> {
            applyPatch(existing, incoming);
            taskRepository.save(existing);
            return taskRepository.findDtoById(id)
                    .orElseThrow(() -> new IllegalStateException("Patched task not found id=" + id));
        });
    }

    @Transactional
    public boolean delete(Long id) {
        LOGGER.info("Deleting task id={}", id);
        if (!taskRepository.existsById(id)) {
            LOGGER.info("Task not found id={}", id);
            return false;
        }
        taskRepository.deleteById(id);
        return true;
    }

    private void applyDto(TaskEntity entity, CreateTaskEntityDto incoming) {
        entity.setTitle(incoming.title());
        entity.setDescription(incoming.description());
        entity.setTaskStatus(resolveTaskStatus(incoming.taskStatusId()));
        entity.setAssignee(resolveAssignee(incoming.assigneeId()));
    }

    private void applyPatch(TaskEntity entity, CreateTaskEntityDto incoming) {
        if (incoming.title() != null) {
            entity.setTitle(incoming.title());
        }
        if (incoming.description() != null) {
            entity.setDescription(incoming.description());
        }
        if (incoming.taskStatusId() != null) {
            entity.setTaskStatus(resolveTaskStatus(incoming.taskStatusId()));
        }
        if (incoming.assigneeId() != null) {
            entity.setAssignee(resolveAssignee(incoming.assigneeId()));
        }
    }

    @Transactional
    public TaskStatusEntity resolveTaskStatus(Long taskStatusId) {
        return taskStatusRepository.findById(taskStatusId)
                .orElseThrow(() -> new IllegalStateException("Task status not found id=" + taskStatusId));
    }

    @Transactional
    public UserEntity resolveAssignee(Long assigneeId) {
        if (assigneeId == null) {
            return null;
        }
        return userRepository.findById(assigneeId)
                .orElseThrow(() -> new IllegalStateException("Assignee not found id=" + assigneeId));
    }
}
