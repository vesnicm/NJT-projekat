package com.fon.prijavaBackend.controller;

import com.fon.prijavaBackend.dto.KorisnikDTO;
import com.fon.prijavaBackend.service.KorisnikService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/korisnici")
public class KorisnikController {

    private final KorisnikService korisnikService;

    public KorisnikController(KorisnikService korisnikService) {
        this.korisnikService = korisnikService;
    }

    @GetMapping
    public ResponseEntity<List<KorisnikDTO>> findAll() {
        return ResponseEntity.ok(korisnikService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KorisnikDTO> findById(@PathVariable long id) {
        return ResponseEntity.ok(korisnikService.findById(id));
    }

    @PostMapping
    public ResponseEntity<KorisnikDTO> create(@Valid @RequestBody KorisnikDTO dto) {
        KorisnikDTO sacuvan = korisnikService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sacuvan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KorisnikDTO> update(@PathVariable long id, @Valid @RequestBody KorisnikDTO dto) {
        return ResponseEntity.ok(korisnikService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        korisnikService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
