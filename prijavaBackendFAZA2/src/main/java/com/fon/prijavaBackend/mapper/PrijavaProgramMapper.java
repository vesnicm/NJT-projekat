/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fon.prijavaBackend.mapper;

import com.fon.prijavaBackend.dto.PrijavaProgramDTO;
import com.fon.prijavaBackend.model.Prijava;
import com.fon.prijavaBackend.model.PrijavaProgram;
import com.fon.prijavaBackend.model.StudijskiProgram;
import org.springframework.stereotype.Component;

@Component
public class PrijavaProgramMapper {

    private final PrijavaMapper prijavaMapper;
    private final StudijskiProgramMapper studijskiProgramMapper;

    public PrijavaProgramMapper(PrijavaMapper prijavaMapper, StudijskiProgramMapper studijskiProgramMapper) {
        this.prijavaMapper = prijavaMapper;
        this.studijskiProgramMapper = studijskiProgramMapper;
    }

    public PrijavaProgramDTO toDomainDTO(PrijavaProgram entity) {
        if (entity == null) return null;

        PrijavaProgramDTO dto = new PrijavaProgramDTO();
        dto.setId(entity.getId());
        if (entity.getPrijava() != null) {
            dto.setPrijava(prijavaMapper.toDomainDTO(entity.getPrijava()));
        }
        if (entity.getStudijskiProgram() != null) {
            dto.setStudijskiProgram(studijskiProgramMapper.toDomainDTO(entity.getStudijskiProgram()));
        }
        return dto;
    }

    public PrijavaProgram toDomainEntity(PrijavaProgramDTO dto, Prijava prijava, StudijskiProgram studijskiProgram) {
        if (dto == null) return null;

        PrijavaProgram entity = new PrijavaProgram();
        if (dto.getId() != 0) {
            entity.setId(dto.getId());
        }
        entity.setPrijava(prijava);
        entity.setStudijskiProgram(studijskiProgram);
        return entity;
    }
}
