package com.fon.prijavaBackend.controller;

import com.fon.prijavaBackend.dto.DokumentDTO;
import com.fon.prijavaBackend.model.Dokument;
import com.fon.prijavaBackend.service.DokumentService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/dokumenti")
public class DokumentController {

    private final DokumentService dokumentService;

    public DokumentController(DokumentService dokumentService) {
        this.dokumentService = dokumentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DokumentDTO> findById(@PathVariable long id) {
        return ResponseEntity.ok(dokumentService.findById(id));
    }

    @GetMapping("/prijava/{prijavaId}")
    public ResponseEntity<List<DokumentDTO>> findByPrijavaId(@PathVariable long prijavaId) {
        return ResponseEntity.ok(dokumentService.findByPrijavaId(prijavaId));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DokumentDTO> upload(@RequestParam long prijavaId,
            @RequestParam String tip,
            @RequestParam("file") MultipartFile file) {
        DokumentDTO sacuvan = dokumentService.upload(prijavaId, tip, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(sacuvan);
    }

    @GetMapping("/{id}/preuzmi")
    public ResponseEntity<Resource> preuzmi(@PathVariable long id) throws IOException {
        Dokument dokument = dokumentService.findEntityById(id);
        Path putanja = Paths.get(dokument.getPutanja());
        Resource resurs = new UrlResource(putanja.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + dokument.getNazivFajla() + "\"")
                .body(resurs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        dokumentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/verifikuj")
    public ResponseEntity<DokumentDTO> verifikuj(@PathVariable long id,
            @RequestParam boolean verifikovan) {
        return ResponseEntity.ok(dokumentService.verifikuj(id, verifikovan));
    }
}
