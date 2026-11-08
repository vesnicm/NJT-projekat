package com.fon.prijavaBackend.controller;

import com.fon.prijavaBackend.dto.PrijavaDTO;
import com.fon.prijavaBackend.model.enums.StatusPrijave;
import com.fon.prijavaBackend.service.PrijavaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prijave")
public class PrijavaController {

    private final PrijavaService prijavaService;

    public PrijavaController(PrijavaService prijavaService) {
        this.prijavaService = prijavaService;
    }

    @GetMapping
    public ResponseEntity<List<PrijavaDTO>> findAll() {
        return ResponseEntity.ok(prijavaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrijavaDTO> findById(@PathVariable long id) {
        return ResponseEntity.ok(prijavaService.findById(id));
    }

    @GetMapping("/kandidat/{kandidatId}")
    public ResponseEntity<List<PrijavaDTO>> findByKandidatId(@PathVariable long kandidatId) {
        return ResponseEntity.ok(prijavaService.findByKandidatId(kandidatId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PrijavaDTO>> findByStatus(@PathVariable StatusPrijave status) {
        return ResponseEntity.ok(prijavaService.findByStatus(status));
    }

    @PostMapping
    public ResponseEntity<PrijavaDTO> create(@Valid @RequestBody PrijavaDTO dto) {
        PrijavaDTO sacuvana = prijavaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sacuvana);
    }

    // Zasebna, uža operacija (PATCH) za administratora koji menja status: PODNETA -> PRIHVACENA/ODBIJENA/POTREBNA_DOPUNA
    @PatchMapping("/{id}/status")
    public ResponseEntity<PrijavaDTO> updateStatus(@PathVariable long id,
            @RequestParam StatusPrijave status,
            @RequestParam(required = false) String napomena) {
        return ResponseEntity.ok(prijavaService.updateStatus(id, status, napomena));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        prijavaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pretraga")
    public ResponseEntity<List<PrijavaDTO>> pretraga(
            @RequestParam(required = false) StatusPrijave status,
            @RequestParam(required = false) String jmbg,
            @RequestParam(required = false) String ime,
            @RequestParam(required = false) String prezime,
            @RequestParam(required = false) Long programId) {
        return ResponseEntity.ok(prijavaService.pretraga(status, jmbg, ime, prezime, programId));
    }
}
