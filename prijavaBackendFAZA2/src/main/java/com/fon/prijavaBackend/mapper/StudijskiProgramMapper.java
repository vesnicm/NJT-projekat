/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fon.prijavaBackend.mapper;

import com.fon.prijavaBackend.dto.StudijskiProgramDTO;
import com.fon.prijavaBackend.model.StudijskiProgram;
import org.springframework.stereotype.Component;

@Component
public class StudijskiProgramMapper implements BaseMapper<StudijskiProgramDTO, StudijskiProgram> {

    @Override
    public StudijskiProgramDTO toDomainDTO(StudijskiProgram entity) {
        if (entity == null) return null;

        StudijskiProgramDTO dto = new StudijskiProgramDTO();
        dto.setId(entity.getId());
        dto.setNaziv(entity.getNaziv());
        dto.setOpis(entity.getOpis());
        dto.setBrojMesta(entity.getBrojMesta());
        return dto;
    }

    @Override
    public StudijskiProgram toDomainEntity(StudijskiProgramDTO dto) {
        if (dto == null) return null;

        StudijskiProgram entity = new StudijskiProgram();
        if (dto.getId() != 0) {
            entity.setId(dto.getId());
        }
        entity.setNaziv(dto.getNaziv());
        entity.setOpis(dto.getOpis());
        entity.setBrojMesta(dto.getBrojMesta());
        return entity;
    }
}
