package com.fon.prijavaBackend.controller;

import com.fon.prijavaBackend.dto.StudijskiProgramDTO;
import com.fon.prijavaBackend.service.StudijskiProgramService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/studijski-programi")
public class StudijskiProgramController {

    private final StudijskiProgramService studijskiProgramService;

    public StudijskiProgramController(StudijskiProgramService studijskiProgramService) {
        this.studijskiProgramService = studijskiProgramService;
    }

    @GetMapping
    public ResponseEntity<List<StudijskiProgramDTO>> findAll() {
        return ResponseEntity.ok(studijskiProgramService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudijskiProgramDTO> findById(@PathVariable long id) {
        return ResponseEntity.ok(studijskiProgramService.findById(id));
    }

    @GetMapping("/pretraga")
    public ResponseEntity<List<StudijskiProgramDTO>> pretraziPoNazivu(@RequestParam String naziv) {
        return ResponseEntity.ok(studijskiProgramService.pretraziPoNazivu(naziv));
    }

    @PostMapping
    public ResponseEntity<StudijskiProgramDTO> create(@Valid @RequestBody StudijskiProgramDTO dto) {
        StudijskiProgramDTO sacuvan = studijskiProgramService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sacuvan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudijskiProgramDTO> update(@PathVariable long id,
                                                       @Valid @RequestBody StudijskiProgramDTO dto) {
        return ResponseEntity.ok(studijskiProgramService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        studijskiProgramService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
