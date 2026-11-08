package com.fon.prijavaBackend.controller;

import com.fon.prijavaBackend.dto.SrednjaSkolaDTO;
import com.fon.prijavaBackend.service.SrednjaSkolaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/srednje-skole")
public class SrednjaSkolaController {

    private final SrednjaSkolaService srednjaSkolaService;

    public SrednjaSkolaController(SrednjaSkolaService srednjaSkolaService) {
        this.srednjaSkolaService = srednjaSkolaService;
    }

    @GetMapping
    public ResponseEntity<List<SrednjaSkolaDTO>> findAll() {
        return ResponseEntity.ok(srednjaSkolaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SrednjaSkolaDTO> findById(@PathVariable long id) {
        return ResponseEntity.ok(srednjaSkolaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SrednjaSkolaDTO> create(@Valid @RequestBody SrednjaSkolaDTO dto) {
        SrednjaSkolaDTO sacuvana = srednjaSkolaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sacuvana);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SrednjaSkolaDTO> update(@PathVariable long id,
                                                  @Valid @RequestBody SrednjaSkolaDTO dto) {
        return ResponseEntity.ok(srednjaSkolaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        srednjaSkolaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
