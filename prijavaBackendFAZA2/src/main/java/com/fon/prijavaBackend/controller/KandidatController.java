package com.fon.prijavaBackend.controller;

import com.fon.prijavaBackend.dto.KandidatDTO;
import com.fon.prijavaBackend.service.KandidatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kandidati")
public class KandidatController {

    private final KandidatService kandidatService;

    public KandidatController(KandidatService kandidatService) {
        this.kandidatService = kandidatService;
    }

    @GetMapping
    public ResponseEntity<List<KandidatDTO>> findAll() {
        return ResponseEntity.ok(kandidatService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KandidatDTO> findById(@PathVariable long id) {
        return ResponseEntity.ok(kandidatService.findById(id));
    }

    @GetMapping("/jmbg/{jmbg}")
    public ResponseEntity<KandidatDTO> findByJmbg(@PathVariable String jmbg) {
        return ResponseEntity.ok(kandidatService.findByJmbg(jmbg));
    }

    @PostMapping
    public ResponseEntity<KandidatDTO> create(@Valid @RequestBody KandidatDTO dto) {
        KandidatDTO sacuvan = kandidatService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sacuvan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KandidatDTO> update(@PathVariable long id, @Valid @RequestBody KandidatDTO dto) {
        return ResponseEntity.ok(kandidatService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        kandidatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
