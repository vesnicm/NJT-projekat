package com.fon.prijavaBackend.service;

import com.fon.prijavaBackend.JPARepo.PrijavaProgramRepository;
import com.fon.prijavaBackend.JPARepo.PrijavaRepository;
import com.fon.prijavaBackend.JPARepo.StudijskiProgramRepository;
import com.fon.prijavaBackend.dto.PrijavaProgramDTO;
import com.fon.prijavaBackend.exception.BusinessException;
import com.fon.prijavaBackend.exception.ResourceNotFoundException;
import com.fon.prijavaBackend.mapper.PrijavaProgramMapper;
import com.fon.prijavaBackend.model.Prijava;
import com.fon.prijavaBackend.model.PrijavaProgram;
import com.fon.prijavaBackend.model.StudijskiProgram;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrijavaProgramService {

    private final PrijavaProgramRepository prijavaProgramRepository;
    private final PrijavaRepository prijavaRepository;
    private final StudijskiProgramRepository studijskiProgramRepository;
    private final PrijavaProgramMapper prijavaProgramMapper;

    public PrijavaProgramService(PrijavaProgramRepository prijavaProgramRepository,
                                  PrijavaRepository prijavaRepository,
                                  StudijskiProgramRepository studijskiProgramRepository,
                                  PrijavaProgramMapper prijavaProgramMapper) {
        this.prijavaProgramRepository = prijavaProgramRepository;
        this.prijavaRepository = prijavaRepository;
        this.studijskiProgramRepository = studijskiProgramRepository;
        this.prijavaProgramMapper = prijavaProgramMapper;
    }

    @Transactional(readOnly = true)
    public List<PrijavaProgramDTO> findByPrijavaId(long prijavaId) {
        return prijavaProgramRepository.findByPrijavaId(prijavaId).stream()
                .map(prijavaProgramMapper::toDomainDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PrijavaProgramDTO> findByStudijskiProgramId(long programId) {
        return prijavaProgramRepository.findByStudijskiProgramId(programId).stream()
                .map(prijavaProgramMapper::toDomainDTO)
                .toList();
    }

    @Transactional
    public PrijavaProgramDTO create(PrijavaProgramDTO dto) {
        Prijava prijava = prijavaRepository.findById(dto.getPrijava().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prijava sa ID " + dto.getPrijava().getId() + " ne postoji"));

        StudijskiProgram program = studijskiProgramRepository.findById(dto.getStudijskiProgram().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Studijski program sa ID " + dto.getStudijskiProgram().getId() + " ne postoji"));

        List<PrijavaProgram> postojece = prijavaProgramRepository.findByPrijavaId(prijava.getId());
        boolean vecPostoji = postojece.stream()
                .anyMatch(pp -> pp.getStudijskiProgram().getId() == program.getId());
        if (vecPostoji) {
            throw new BusinessException("Ova prijava je već povezana sa ovim studijskim programom");
        }
        if (postojece.size() >= 2) {
            throw new BusinessException("Prijava može sadržati najviše 2 studijska programa");
        }

        PrijavaProgram entitet = prijavaProgramMapper.toDomainEntity(dto, prijava, program);
        PrijavaProgram sacuvan = prijavaProgramRepository.save(entitet);
        return prijavaProgramMapper.toDomainDTO(sacuvan);
    }

    @Transactional
    public void delete(long id) {
        if (!prijavaProgramRepository.existsById(id)) {
            throw new ResourceNotFoundException("Veza prijava-program sa ID " + id + " ne postoji");
        }
        prijavaProgramRepository.deleteById(id);
    }
}
