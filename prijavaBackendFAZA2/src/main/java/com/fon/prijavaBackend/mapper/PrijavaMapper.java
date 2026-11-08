/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fon.prijavaBackend.mapper;

import com.fon.prijavaBackend.dto.PrijavaDTO;
import com.fon.prijavaBackend.model.Kandidat;
import com.fon.prijavaBackend.model.Prijava;
import org.springframework.stereotype.Component;

@Component
public class PrijavaMapper {

    private final KandidatMapper kandidatMapper;
    private final KorisnikMapper korisnikMapper;

    public PrijavaMapper(KandidatMapper kandidatMapper, KorisnikMapper korisnikMapper) {
        this.kandidatMapper = kandidatMapper;
        this.korisnikMapper = korisnikMapper;
    }

    public PrijavaDTO toDomainDTO(Prijava entity) {
        if (entity == null) return null;

        PrijavaDTO dto = new PrijavaDTO();
        dto.setId(entity.getId());
        dto.setBrojPrijave(entity.getBrojPrijave());
        dto.setCena(entity.getCena());
        dto.setDatumPodnosenja(entity.getDatumPodnosenja());
        dto.setStatus(entity.getStatus());
        dto.setNapomena(entity.getNapomena());
        if (entity.getKandidat() != null) {
            dto.setKandidat(kandidatMapper.toDomainDTO(entity.getKandidat()));
        }
        if (entity.getPregledao() != null) {
            dto.setPregledao(korisnikMapper.toDomainDTO(entity.getPregledao()));
        }
        return dto;
    }

    public Prijava toDomainEntity(PrijavaDTO dto, Kandidat kandidat) {
        if (dto == null) return null;

        Prijava entity = new Prijava();
        if (dto.getId() != 0) {
            entity.setId(dto.getId());
        }
        entity.setDatumPodnosenja(dto.getDatumPodnosenja());
        entity.setStatus(dto.getStatus());
        entity.setNapomena(dto.getNapomena());
        entity.setKandidat(kandidat);
        return entity;
    }
}
