package com.fon.prijavaBackend.service;

import com.fon.prijavaBackend.JPARepo.StudijskiProgramRepository;
import com.fon.prijavaBackend.dto.StudijskiProgramDTO;
import com.fon.prijavaBackend.exception.ResourceNotFoundException;
import com.fon.prijavaBackend.mapper.StudijskiProgramMapper;
import com.fon.prijavaBackend.model.StudijskiProgram;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudijskiProgramService {

    private final StudijskiProgramRepository studijskiProgramRepository;
    private final StudijskiProgramMapper studijskiProgramMapper;

    public StudijskiProgramService(StudijskiProgramRepository studijskiProgramRepository,
                                    StudijskiProgramMapper studijskiProgramMapper) {
        this.studijskiProgramRepository = studijskiProgramRepository;
        this.studijskiProgramMapper = studijskiProgramMapper;
    }

    @Transactional(readOnly = true)
    public List<StudijskiProgramDTO> findAll() {
        return studijskiProgramRepository.findAll().stream()
                .map(studijskiProgramMapper::toDomainDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public StudijskiProgramDTO findById(long id) {
        StudijskiProgram program = studijskiProgramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Studijski program sa ID " + id + " ne postoji"));
        return studijskiProgramMapper.toDomainDTO(program);
    }

    @Transactional(readOnly = true)
    public List<StudijskiProgramDTO> pretraziPoNazivu(String naziv) {
        return studijskiProgramRepository.findByNazivContainingIgnoreCase(naziv).stream()
                .map(studijskiProgramMapper::toDomainDTO)
                .toList();
    }

    @Transactional
    public StudijskiProgramDTO create(StudijskiProgramDTO dto) {
        StudijskiProgram program = studijskiProgramMapper.toDomainEntity(dto);
        StudijskiProgram sacuvan = studijskiProgramRepository.save(program);
        return studijskiProgramMapper.toDomainDTO(sacuvan);
    }

    @Transactional
    public StudijskiProgramDTO update(long id, StudijskiProgramDTO dto) {
        StudijskiProgram postojeci = studijskiProgramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Studijski program sa ID " + id + " ne postoji"));

        postojeci.setNaziv(dto.getNaziv());
        postojeci.setOpis(dto.getOpis());
        postojeci.setBrojMesta(dto.getBrojMesta());

        StudijskiProgram sacuvan = studijskiProgramRepository.save(postojeci);
        return studijskiProgramMapper.toDomainDTO(sacuvan);
    }

    @Transactional
    public void delete(long id) {
        if (!studijskiProgramRepository.existsById(id)) {
            throw new ResourceNotFoundException("Studijski program sa ID " + id + " ne postoji");
        }
        studijskiProgramRepository.deleteById(id);
    }
}
