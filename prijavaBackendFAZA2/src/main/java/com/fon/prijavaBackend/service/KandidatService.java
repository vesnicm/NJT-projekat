package com.fon.prijavaBackend.service;

import com.fon.prijavaBackend.JPARepo.KandidatRepository;
import com.fon.prijavaBackend.JPARepo.KorisnikRepository;
import com.fon.prijavaBackend.JPARepo.SrednjaSkolaRepository;
import com.fon.prijavaBackend.dto.KandidatDTO;
import com.fon.prijavaBackend.exception.BusinessException;
import com.fon.prijavaBackend.exception.ResourceNotFoundException;
import com.fon.prijavaBackend.mapper.KandidatMapper;
import com.fon.prijavaBackend.model.Kandidat;
import com.fon.prijavaBackend.model.Korisnik;
import com.fon.prijavaBackend.model.SrednjaSkola;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KandidatService {

    private final KandidatRepository kandidatRepository;
    private final KorisnikRepository korisnikRepository;
    private final SrednjaSkolaRepository srednjaSkolaRepository;
    private final KandidatMapper kandidatMapper;

    public KandidatService(KandidatRepository kandidatRepository,
                            KorisnikRepository korisnikRepository,
                            SrednjaSkolaRepository srednjaSkolaRepository,
                            KandidatMapper kandidatMapper) {
        this.kandidatRepository = kandidatRepository;
        this.korisnikRepository = korisnikRepository;
        this.srednjaSkolaRepository = srednjaSkolaRepository;
        this.kandidatMapper = kandidatMapper;
    }

    private SrednjaSkola razresiSkolu(KandidatDTO dto) {
        if (dto.getSrednjaSkola() == null || dto.getSrednjaSkola().getId() == 0) {
            return null;
        }
        long skolaId = dto.getSrednjaSkola().getId();
        return srednjaSkolaRepository.findById(skolaId)
                .orElseThrow(() -> new ResourceNotFoundException("Srednja škola sa ID " + skolaId + " ne postoji"));
    }

    @Transactional(readOnly = true)
    public List<KandidatDTO> findAll() {
        return kandidatRepository.findAll().stream()
                .map(kandidatMapper::toDomainDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public KandidatDTO findById(long id) {
        Kandidat kandidat = kandidatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kandidat sa ID " + id + " ne postoji"));
        return kandidatMapper.toDomainDTO(kandidat);
    }

    @Transactional(readOnly = true)
    public KandidatDTO findByJmbg(String jmbg) {
        Kandidat kandidat = kandidatRepository.findByJmbg(jmbg)
                .orElseThrow(() -> new ResourceNotFoundException("Kandidat sa JMBG " + jmbg + " ne postoji"));
        return kandidatMapper.toDomainDTO(kandidat);
    }

    @Transactional
    public KandidatDTO create(KandidatDTO dto) {
        Korisnik korisnik = korisnikRepository.findById(dto.getKorisnik().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Korisnik sa ID " + dto.getKorisnik().getId() + " ne postoji"));

        kandidatRepository.findByJmbg(dto.getJmbg()).ifPresent(k -> {
            throw new BusinessException("Kandidat sa JMBG " + dto.getJmbg() + " već postoji");
        });
        kandidatRepository.findByKorisnikId(korisnik.getId()).ifPresent(k -> {
            throw new BusinessException("Korisnik već ima povezanog kandidata");
        });

        Kandidat kandidat = kandidatMapper.toDomainEntity(dto, korisnik, razresiSkolu(dto));
        Kandidat sacuvan = kandidatRepository.save(kandidat);
        return kandidatMapper.toDomainDTO(sacuvan);
    }

    @Transactional
    public KandidatDTO update(long id, KandidatDTO dto) {
        Kandidat postojeci = kandidatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kandidat sa ID " + id + " ne postoji"));

        kandidatRepository.findByJmbg(dto.getJmbg())
                .filter(k -> k.getId() != id)
                .ifPresent(k -> {
                    throw new BusinessException("Kandidat sa JMBG " + dto.getJmbg() + " već postoji");
                });

        postojeci.setJmbg(dto.getJmbg());
        postojeci.setDatumRodjenja(dto.getDatumRodjenja());
        postojeci.setAdresa(dto.getAdresa());
        postojeci.setTelefon(dto.getTelefon());
        postojeci.setSrednjaSkola(razresiSkolu(dto));
        postojeci.setProsek1Razred(dto.getProsek1Razred());
        postojeci.setProsek2Razred(dto.getProsek2Razred());
        postojeci.setProsek3Razred(dto.getProsek3Razred());
        postojeci.setProsek4Razred(dto.getProsek4Razred());

        Kandidat sacuvan = kandidatRepository.save(postojeci);
        return kandidatMapper.toDomainDTO(sacuvan);
    }

    @Transactional
    public void delete(long id) {
        if (!kandidatRepository.existsById(id)) {
            throw new ResourceNotFoundException("Kandidat sa ID " + id + " ne postoji");
        }
        kandidatRepository.deleteById(id);
    }
}
