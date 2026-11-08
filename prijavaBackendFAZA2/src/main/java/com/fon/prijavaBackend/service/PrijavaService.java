package com.fon.prijavaBackend.service;

import com.fon.prijavaBackend.JPARepo.KandidatRepository;
import com.fon.prijavaBackend.JPARepo.KorisnikRepository;
import com.fon.prijavaBackend.JPARepo.PrijavaRepository;
import com.fon.prijavaBackend.dto.PrijavaDTO;
import com.fon.prijavaBackend.exception.ResourceNotFoundException;
import com.fon.prijavaBackend.mapper.PrijavaMapper;
import com.fon.prijavaBackend.model.Kandidat;
import com.fon.prijavaBackend.model.Korisnik;
import com.fon.prijavaBackend.model.Prijava;
import com.fon.prijavaBackend.model.enums.StatusPrijave;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrijavaService {

    private final PrijavaRepository prijavaRepository;
    private final KandidatRepository kandidatRepository;
    private final KorisnikRepository korisnikRepository;
    private final PrijavaMapper prijavaMapper;

    @Value("${app.prijava.cena:0}")
    private double cenaPrijave;

    public PrijavaService(PrijavaRepository prijavaRepository,
            KandidatRepository kandidatRepository,
            KorisnikRepository korisnikRepository,
            PrijavaMapper prijavaMapper) {
        this.prijavaRepository = prijavaRepository;
        this.kandidatRepository = kandidatRepository;
        this.korisnikRepository = korisnikRepository;
        this.prijavaMapper = prijavaMapper;
    }

    @Transactional(readOnly = true)
    public List<PrijavaDTO> findAll() {
        return prijavaRepository.findAll().stream()
                .map(prijavaMapper::toDomainDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PrijavaDTO findById(long id) {
        Prijava prijava = prijavaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prijava sa ID " + id + " ne postoji"));
        return prijavaMapper.toDomainDTO(prijava);
    }

    @Transactional(readOnly = true)
    public List<PrijavaDTO> findByKandidatId(long kandidatId) {
        return prijavaRepository.findByKandidatId(kandidatId).stream()
                .map(prijavaMapper::toDomainDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PrijavaDTO> findByStatus(StatusPrijave status) {
        return prijavaRepository.findByStatus(status).stream()
                .map(prijavaMapper::toDomainDTO)
                .toList();
    }

    @Transactional
    public PrijavaDTO create(PrijavaDTO dto) {
        Kandidat kandidat = kandidatRepository.findById(dto.getKandidat().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                "Kandidat sa ID " + dto.getKandidat().getId() + " ne postoji"));

        dto.setDatumPodnosenja(LocalDateTime.now());
        if (dto.getStatus() == null) {
            dto.setStatus(StatusPrijave.PODNETA);
        }

        Prijava prijava = prijavaMapper.toDomainEntity(dto, kandidat);
        prijava.setCena(cenaPrijave);
        Prijava sacuvana = prijavaRepository.save(prijava);

        sacuvana.setBrojPrijave(String.format("PR-%d-%05d", LocalDate.now().getYear(), sacuvana.getId()));
        sacuvana = prijavaRepository.save(sacuvana);
        return prijavaMapper.toDomainDTO(sacuvana);
    }

    private Korisnik trenutniKorisnik() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            return null;
        }
        return korisnikRepository.findByEmail(auth.getName()).orElse(null);
    }


    @Transactional
    public PrijavaDTO updateStatus(long id, StatusPrijave noviStatus, String napomena) {
        Prijava prijava = prijavaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prijava sa ID " + id + " ne postoji"));

        prijava.setStatus(noviStatus);
        if (napomena != null) {
            prijava.setNapomena(napomena);
        }
        Korisnik admin = trenutniKorisnik();
        if (admin != null) {
            prijava.setPregledao(admin);
        }

        Prijava sacuvana = prijavaRepository.save(prijava);
        return prijavaMapper.toDomainDTO(sacuvana);
    }

    @Transactional
    public void delete(long id) {
        if (!prijavaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Prijava sa ID " + id + " ne postoji");
        }
        prijavaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PrijavaDTO> pretraga(StatusPrijave status, String jmbg, String ime,
            String prezime, Long programId) {
        return prijavaRepository.pretraga(status, jmbg, ime, prezime, programId).stream()
                .map(prijavaMapper::toDomainDTO)
                .toList();
    }
}
