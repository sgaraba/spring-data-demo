package md.utm2026.demo.service;

import md.utm2026.demo.domain.TagEntity;
import md.utm2026.demo.repository.TagRepository;
import md.utm2026.demo.web.dto.CreateTagEntityDto;
import md.utm2026.demo.web.dto.TagEntityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagService.class);

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagEntityDto> findAll() {
        LOGGER.info("Fetching all tags");
        return tagRepository.findAll()
                .stream()
                .map(TagEntityDto::fromEntity)
                .toList();
    }

    public Optional<TagEntityDto> findById(Long id) {
        LOGGER.info("Fetching tag by id={}", id);
        return tagRepository.findById(id).map(TagEntityDto::fromEntity);
    }

    public TagEntityDto create(CreateTagEntityDto tag) {
        LOGGER.info("Creating tag name={}", tag.name());
        TagEntity entity = new TagEntity();
        entity.setName(tag.name());
        return TagEntityDto.fromEntity(tagRepository.save(entity));
    }
}
