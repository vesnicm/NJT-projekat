package com.fon.prijavaBackend.controller;

import com.fon.prijavaBackend.dto.PrijavaProgramDTO;
import com.fon.prijavaBackend.service.PrijavaProgramService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prijava-program")
public class PrijavaProgramController {

    private final PrijavaProgramService prijavaProgramService;

    public PrijavaProgramController(PrijavaProgramService prijavaProgramService) {
        this.prijavaProgramService = prijavaProgramService;
    }

    @GetMapping("/prijava/{prijavaId}")
    public ResponseEntity<List<PrijavaProgramDTO>> findByPrijavaId(@PathVariable long prijavaId) {
        return ResponseEntity.ok(prijavaProgramService.findByPrijavaId(prijavaId));
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<PrijavaProgramDTO>> findByStudijskiProgramId(@PathVariable long programId) {
        return ResponseEntity.ok(prijavaProgramService.findByStudijskiProgramId(programId));
    }

    @PostMapping
    public ResponseEntity<PrijavaProgramDTO> create(@Valid @RequestBody PrijavaProgramDTO dto) {
        PrijavaProgramDTO sacuvan = prijavaProgramService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sacuvan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        prijavaProgramService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
