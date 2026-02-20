package md.utm2026.demo.web;

import jakarta.validation.Valid;
import md.utm2026.demo.service.TagService;
import md.utm2026.demo.web.dto.CreateTagEntityDto;
import md.utm2026.demo.web.dto.TagEntityDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagEntityDto> getAll() {
        return tagService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagEntityDto> getById(@PathVariable Long id) {
        return tagService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    public ResponseEntity<TagEntityDto> create(@Valid @RequestBody CreateTagEntityDto tag) {
        TagEntityDto created = tagService.create(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
