package com.fon.prijavaBackend.service;

import com.fon.prijavaBackend.JPARepo.KorisnikRepository;
import com.fon.prijavaBackend.dto.KorisnikDTO;
import com.fon.prijavaBackend.exception.BusinessException;
import com.fon.prijavaBackend.exception.ResourceNotFoundException;
import com.fon.prijavaBackend.mapper.KorisnikMapper;
import com.fon.prijavaBackend.model.Korisnik;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final KorisnikMapper korisnikMapper;
    private final PasswordEncoder passwordEncoder;

    public KorisnikService(KorisnikRepository korisnikRepository,
                            KorisnikMapper korisnikMapper,
                            PasswordEncoder passwordEncoder) {
        this.korisnikRepository = korisnikRepository;
        this.korisnikMapper = korisnikMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<KorisnikDTO> findAll() {
        return korisnikRepository.findAll().stream()
                .map(korisnikMapper::toDomainDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public KorisnikDTO findById(long id) {
        Korisnik korisnik = korisnikRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik sa ID " + id + " ne postoji"));
        return korisnikMapper.toDomainDTO(korisnik);
    }

    @Transactional
    public KorisnikDTO create(KorisnikDTO dto) {
        korisnikRepository.findByEmail(dto.getEmail()).ifPresent(k -> {
            throw new BusinessException("Korisnik sa email adresom '" + dto.getEmail() + "' već postoji");
        });

        Korisnik korisnik = korisnikMapper.toDomainEntity(dto);
        korisnik.setLozinka(passwordEncoder.encode(dto.getLozinka()));   
        Korisnik sacuvan = korisnikRepository.save(korisnik);
        return korisnikMapper.toDomainDTO(sacuvan);
    }

    @Transactional
    public KorisnikDTO update(long id, KorisnikDTO dto) {
        Korisnik postojeci = korisnikRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik sa ID " + id + " ne postoji"));

        korisnikRepository.findByEmail(dto.getEmail())
                .filter(k -> k.getId() != id)
                .ifPresent(k -> {
                    throw new BusinessException("Korisnik sa email adresom '" + dto.getEmail() + "' već postoji");
                });

        postojeci.setIme(dto.getIme());
        postojeci.setPrezime(dto.getPrezime());
        postojeci.setEmail(dto.getEmail());
        if (dto.getLozinka() != null && !dto.getLozinka().isBlank()) {
            postojeci.setLozinka(passwordEncoder.encode(dto.getLozinka()));   
        }
        postojeci.setUloga(dto.getUloga());

        Korisnik sacuvan = korisnikRepository.save(postojeci);
        return korisnikMapper.toDomainDTO(sacuvan);
    }

    @Transactional
    public void delete(long id) {
        if (!korisnikRepository.existsById(id)) {
            throw new ResourceNotFoundException("Korisnik sa ID " + id + " ne postoji");
        }
        korisnikRepository.deleteById(id);
    }
}