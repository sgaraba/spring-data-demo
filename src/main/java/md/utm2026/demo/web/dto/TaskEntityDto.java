package md.utm2026.demo.web.dto;

import md.utm2026.demo.domain.TaskEntity;

public class TaskEntityDto {

    private final Long id;
    private final String title;
    private final String description;
    private final String taskStatusName;
    private final String assigneeLogin;

    public TaskEntityDto(Long id, String title, String description, String taskStatusName, String assigneeLogin) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.taskStatusName = taskStatusName;
        this.assigneeLogin = assigneeLogin;
    }

    public static TaskEntityDto fromEntity(TaskEntity entity) {
        return new TaskEntityDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getTaskStatus() != null ? entity.getTaskStatus().getName() : null,
                entity.getAssignee() != null ? entity.getAssignee().getUserName() : null
        );
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public String getAssigneeLogin() {
        return assigneeLogin;
    }
}
