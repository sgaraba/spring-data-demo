package md.utm2026.demo.web.dto;

import md.utm2026.demo.domain.TaskStatusEntity;

public class TaskStatusEntityDto {

    private final Long id;
    private final String name;
    private final Boolean active;

    public TaskStatusEntityDto(Long id, String name, Boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public static TaskStatusEntityDto fromEntity(TaskStatusEntity entity) {
        return new TaskStatusEntityDto(
                entity.getId(),
                entity.getName(),
                entity.getActive()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getActive() {
        return active;
    }
}
