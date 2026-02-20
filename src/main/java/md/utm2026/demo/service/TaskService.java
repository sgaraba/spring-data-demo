package md.utm2026.demo.service;

import jakarta.persistence.EntityManager;
import md.utm2026.demo.domain.TagEntity;
import md.utm2026.demo.domain.TaskEntity;
import md.utm2026.demo.domain.TaskStatusEntity;
import md.utm2026.demo.domain.UserEntity;
import md.utm2026.demo.repository.TagRepository;
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

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final EntityManager entityManager;

    public TaskService(
            TaskRepository taskRepository,
            TaskStatusRepository taskStatusRepository,
            UserRepository userRepository,
            TagRepository tagRepository,
            EntityManager entityManager
    ) {
        this.taskRepository = taskRepository;
        this.taskStatusRepository = taskStatusRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.entityManager = entityManager;
    }

    public List<TaskEntityDto> searchByTitleWithEntityManager(String titleFragment) {
        LOGGER.info("Searching tasks with EntityManager titleFragment={}", titleFragment);
        return entityManager.createQuery("""
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
                        where lower(t.title) like lower(concat('%', :titleFragment, '%'))
                        order by t.id
                        """, TaskEntityDto.class)
                .setParameter("titleFragment", titleFragment)
                .getResultList();
    }

    public Page<TaskEntity> findAll(Pageable pageable) {
        LOGGER.info("Fetching tasks page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        return taskRepository.findAll(pageable);
    }

    public Optional<TaskEntityDto> findDtoById(Long id) {
        LOGGER.info("Fetching task dto by id={}", id);
        return taskRepository.findDtoById(id);
    }

    public Optional<TaskEntity> findById(Long id) {
        LOGGER.info("Fetching task by id={}", id);
        return taskRepository.findById(id);
    }

    public TaskEntityDto create(CreateTaskEntityDto task) {
        LOGGER.info("Creating task title={}", task.title());
        TaskEntity entity = new TaskEntity();
        applyDto(entity, task);
        TaskEntity created = taskRepository.save(entity);
        return taskRepository.findDtoById(created.getId())
                .orElseThrow(() -> new IllegalStateException("Created task not found id=" + created.getId()));
    }

    public Optional<TaskEntity> addTagToTask(Long taskId, Long tagId) {
        LOGGER.info("Adding tag id={} to task id={}", tagId, taskId);
        return taskRepository.findById(taskId).map(task -> {
            TagEntity tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new IllegalStateException("Tag not found id=" + tagId));
            task.getTags().add(tag);
            return taskRepository.save(task);
        });
    }

    public Optional<TaskEntityDto> update(Long id, CreateTaskEntityDto incoming) {
        LOGGER.info("Updating task id={}", id);
        return taskRepository.findById(id).map(existing -> {
            applyDto(existing, incoming);
            taskRepository.save(existing);
            return taskRepository.findDtoById(id)
                    .orElseThrow(() -> new IllegalStateException("Updated task not found id=" + id));
        });
    }

    public Optional<TaskEntityDto> patch(Long id, CreateTaskEntityDto incoming) {
        LOGGER.info("Patching task id={}", id);
        return taskRepository.findById(id).map(existing -> {
            applyPatch(existing, incoming);
            taskRepository.save(existing);
            return taskRepository.findDtoById(id)
                    .orElseThrow(() -> new IllegalStateException("Patched task not found id=" + id));
        });
    }

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

    private TaskStatusEntity resolveTaskStatus(Long taskStatusId) {
        return taskStatusRepository.findById(taskStatusId)
                .orElseThrow(() -> new IllegalStateException("Task status not found id=" + taskStatusId));
    }

    private UserEntity resolveAssignee(Long assigneeId) {
        if (assigneeId == null) {
            return null;
        }
        return userRepository.findById(assigneeId)
                .orElseThrow(() -> new IllegalStateException("Assignee not found id=" + assigneeId));
    }
}
