package com.fon.prijavaBackend.service;

import com.fon.prijavaBackend.JPARepo.DokumentRepository;
import com.fon.prijavaBackend.JPARepo.PrijavaRepository;
import com.fon.prijavaBackend.dto.DokumentDTO;
import com.fon.prijavaBackend.exception.BusinessException;
import com.fon.prijavaBackend.exception.ResourceNotFoundException;
import com.fon.prijavaBackend.mapper.DokumentMapper;
import com.fon.prijavaBackend.model.Dokument;
import com.fon.prijavaBackend.model.Prijava;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DokumentService {

    private final DokumentRepository dokumentRepository;
    private final PrijavaRepository prijavaRepository;
    private final DokumentMapper dokumentMapper;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public DokumentService(DokumentRepository dokumentRepository,
            PrijavaRepository prijavaRepository,
            DokumentMapper dokumentMapper) {
        this.dokumentRepository = dokumentRepository;
        this.prijavaRepository = prijavaRepository;
        this.dokumentMapper = dokumentMapper;
    }

    @Transactional(readOnly = true)
    public DokumentDTO findById(long id) {
        Dokument dokument = dokumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dokument sa ID " + id + " ne postoji"));
        return dokumentMapper.toDomainDTO(dokument);
    }

    @Transactional(readOnly = true)
    public List<DokumentDTO> findByPrijavaId(long prijavaId) {
        return dokumentRepository.findByPrijavaId(prijavaId).stream()
                .map(dokumentMapper::toDomainDTO)
                .toList();
    }

    @Transactional
    public DokumentDTO upload(long prijavaId, String tip, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("Fajl ne sme biti prazan");
        }

        Prijava prijava = prijavaRepository.findById(prijavaId)
                .orElseThrow(() -> new ResourceNotFoundException("Prijava sa ID " + prijavaId + " ne postoji"));

        String originalnoIme = file.getOriginalFilename();
        String ekstenzija = "";
        if (originalnoIme != null && originalnoIme.contains(".")) {
            ekstenzija = originalnoIme.substring(originalnoIme.lastIndexOf("."));
        }
        String sacuvanoIme = UUID.randomUUID() + ekstenzija;

        try {
            Path folder = Paths.get(uploadDir);
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }
            Path putanjaFajla = folder.resolve(sacuvanoIme);
            Files.copy(file.getInputStream(), putanjaFajla);

            Dokument dokument = new Dokument();
            dokument.setPrijava(prijava);
            dokument.setTip(tip);
            dokument.setNazivFajla(originalnoIme);          
            dokument.setPutanja(putanjaFajla.toString());   
            dokument.setDatumUploada(LocalDateTime.now());

            Dokument sacuvan = dokumentRepository.save(dokument);
            return dokumentMapper.toDomainDTO(sacuvan);

        } catch (IOException ex) {
            throw new BusinessException("Greška pri čuvanju fajla: " + ex.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Dokument findEntityById(long id) {
        return dokumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dokument sa ID " + id + " ne postoji"));
    }

    @Transactional
    public void delete(long id) {
        Dokument dokument = dokumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dokument sa ID " + id + " ne postoji"));

        try {
            Files.deleteIfExists(Paths.get(dokument.getPutanja()));
        } catch (IOException ex) {
        }
        dokumentRepository.deleteById(id);
    }

    @Transactional
    public DokumentDTO verifikuj(long id, boolean verifikovan) {
        Dokument dokument = dokumentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dokument sa ID " + id + " ne postoji"));
        dokument.setVerifikovan(verifikovan);
        return dokumentMapper.toDomainDTO(dokumentRepository.save(dokument));
    }
}
