package md.utm2026.demo.web.dto;

import md.utm2026.demo.domain.TagEntity;

public class TagEntityDto {

    private final Long id;
    private final String name;

    public TagEntityDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static TagEntityDto fromEntity(TagEntity entity) {
        return new TagEntityDto(entity.getId(), entity.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
