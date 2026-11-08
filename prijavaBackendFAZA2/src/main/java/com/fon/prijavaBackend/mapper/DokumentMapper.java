/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fon.prijavaBackend.mapper;

import com.fon.prijavaBackend.dto.DokumentDTO;
import com.fon.prijavaBackend.model.Dokument;
import com.fon.prijavaBackend.model.Prijava;
import org.springframework.stereotype.Component;

@Component
public class DokumentMapper {

    private final PrijavaMapper prijavaMapper;

    public DokumentMapper(PrijavaMapper prijavaMapper) {
        this.prijavaMapper = prijavaMapper;
    }

    public DokumentDTO toDomainDTO(Dokument entity) {
        if (entity == null) return null;

        DokumentDTO dto = new DokumentDTO();
        dto.setId(entity.getId());
        dto.setTip(entity.getTip());
        dto.setNazivFajla(entity.getNazivFajla());
        dto.setPutanja(entity.getPutanja());
        dto.setDatumUploada(entity.getDatumUploada());
        dto.setVerifikovan(entity.isVerifikovan());
        if (entity.getPrijava() != null) {
            dto.setPrijava(prijavaMapper.toDomainDTO(entity.getPrijava()));
        }
        return dto;
    }

    public Dokument toDomainEntity(DokumentDTO dto, Prijava prijava) {
        if (dto == null) return null;

        Dokument entity = new Dokument();
        if (dto.getId() != 0) {
            entity.setId(dto.getId());
        }
        entity.setTip(dto.getTip());
        entity.setNazivFajla(dto.getNazivFajla());
        entity.setPutanja(dto.getPutanja());
        entity.setDatumUploada(dto.getDatumUploada());
        entity.setPrijava(prijava);
        entity.setVerifikovan(dto.isVerifikovan()); 
        return entity;
    }
}
