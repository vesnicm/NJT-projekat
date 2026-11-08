/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fon.prijavaBackend.mapper;

import com.fon.prijavaBackend.dto.SrednjaSkolaDTO;
import com.fon.prijavaBackend.model.SrednjaSkola;
import org.springframework.stereotype.Component;

@Component
public class SrednjaSkolaMapper implements BaseMapper<SrednjaSkolaDTO, SrednjaSkola> {

    @Override
    public SrednjaSkolaDTO toDomainDTO(SrednjaSkola entity) {
        if (entity == null) return null;

        SrednjaSkolaDTO dto = new SrednjaSkolaDTO();
        dto.setId(entity.getId());
        dto.setSifra(entity.getSifra());
        dto.setNaziv(entity.getNaziv());
        return dto;
    }

    @Override
    public SrednjaSkola toDomainEntity(SrednjaSkolaDTO dto) {
        if (dto == null) return null;

        SrednjaSkola entity = new SrednjaSkola();
        if (dto.getId() != 0) {
            entity.setId(dto.getId());
        }
        entity.setSifra(dto.getSifra());
        entity.setNaziv(dto.getNaziv());
        return entity;
    }
}
