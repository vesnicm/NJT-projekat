package com.fon.prijavaBackend.service;

import com.fon.prijavaBackend.JPARepo.SrednjaSkolaRepository;
import com.fon.prijavaBackend.dto.SrednjaSkolaDTO;
import com.fon.prijavaBackend.exception.BusinessException;
import com.fon.prijavaBackend.exception.ResourceNotFoundException;
import com.fon.prijavaBackend.mapper.SrednjaSkolaMapper;
import com.fon.prijavaBackend.model.SrednjaSkola;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SrednjaSkolaService {

    private final SrednjaSkolaRepository srednjaSkolaRepository;
    private final SrednjaSkolaMapper srednjaSkolaMapper;

    public SrednjaSkolaService(SrednjaSkolaRepository srednjaSkolaRepository,
                               SrednjaSkolaMapper srednjaSkolaMapper) {
        this.srednjaSkolaRepository = srednjaSkolaRepository;
        this.srednjaSkolaMapper = srednjaSkolaMapper;
    }

    @Transactional(readOnly = true)
    public List<SrednjaSkolaDTO> findAll() {
        return srednjaSkolaRepository.findAll().stream()
                .map(srednjaSkolaMapper::toDomainDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public SrednjaSkolaDTO findById(long id) {
        SrednjaSkola skola = srednjaSkolaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Srednja škola sa ID " + id + " ne postoji"));
        return srednjaSkolaMapper.toDomainDTO(skola);
    }

    @Transactional
    public SrednjaSkolaDTO create(SrednjaSkolaDTO dto) {
        srednjaSkolaRepository.findBySifra(dto.getSifra()).ifPresent(s -> {
            throw new BusinessException("Srednja škola sa šifrom " + dto.getSifra() + " već postoji");
        });
        SrednjaSkola skola = srednjaSkolaMapper.toDomainEntity(dto);
        SrednjaSkola sacuvana = srednjaSkolaRepository.save(skola);
        return srednjaSkolaMapper.toDomainDTO(sacuvana);
    }

    @Transactional
    public SrednjaSkolaDTO update(long id, SrednjaSkolaDTO dto) {
        SrednjaSkola postojeca = srednjaSkolaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Srednja škola sa ID " + id + " ne postoji"));

        srednjaSkolaRepository.findBySifra(dto.getSifra())
                .filter(s -> s.getId() != id)
                .ifPresent(s -> {
                    throw new BusinessException("Srednja škola sa šifrom " + dto.getSifra() + " već postoji");
                });

        postojeca.setSifra(dto.getSifra());
        postojeca.setNaziv(dto.getNaziv());

        SrednjaSkola sacuvana = srednjaSkolaRepository.save(postojeca);
        return srednjaSkolaMapper.toDomainDTO(sacuvana);
    }

    @Transactional
    public void delete(long id) {
        if (!srednjaSkolaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Srednja škola sa ID " + id + " ne postoji");
        }
        srednjaSkolaRepository.deleteById(id);
    }
}
