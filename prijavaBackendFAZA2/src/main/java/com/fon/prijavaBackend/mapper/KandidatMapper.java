/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fon.prijavaBackend.mapper;

import com.fon.prijavaBackend.dto.KandidatDTO;
import com.fon.prijavaBackend.model.Kandidat;
import com.fon.prijavaBackend.model.Korisnik;
import com.fon.prijavaBackend.model.SrednjaSkola;
import org.springframework.stereotype.Component;

@Component
public class KandidatMapper {

    private final KorisnikMapper korisnikMapper;
    private final SrednjaSkolaMapper srednjaSkolaMapper;

    public KandidatMapper(KorisnikMapper korisnikMapper, SrednjaSkolaMapper srednjaSkolaMapper) {
        this.korisnikMapper = korisnikMapper;
        this.srednjaSkolaMapper = srednjaSkolaMapper;
    }

    public KandidatDTO toDomainDTO(Kandidat entity) {
        if (entity == null) return null;

        KandidatDTO dto = new KandidatDTO();
        dto.setId(entity.getId());
        dto.setJmbg(entity.getJmbg());
        dto.setDatumRodjenja(entity.getDatumRodjenja());
        dto.setAdresa(entity.getAdresa());
        dto.setTelefon(entity.getTelefon());
        if (entity.getSrednjaSkola() != null) {
            dto.setSrednjaSkola(srednjaSkolaMapper.toDomainDTO(entity.getSrednjaSkola()));
        }
        dto.setProsek1Razred(entity.getProsek1Razred());
        dto.setProsek2Razred(entity.getProsek2Razred());
        dto.setProsek3Razred(entity.getProsek3Razred());
        dto.setProsek4Razred(entity.getProsek4Razred());
        if (entity.getKorisnik() != null) {
            dto.setKorisnik(korisnikMapper.toDomainDTO(entity.getKorisnik()));
        }
        return dto;
    }

    public Kandidat toDomainEntity(KandidatDTO dto, Korisnik korisnik) {
        return toDomainEntity(dto, korisnik, null);
    }

    public Kandidat toDomainEntity(KandidatDTO dto, Korisnik korisnik, SrednjaSkola srednjaSkola) {
        if (dto == null) return null;

        Kandidat entity = new Kandidat();
        if (dto.getId() != 0) {
            entity.setId(dto.getId());
        }
        entity.setJmbg(dto.getJmbg());
        entity.setDatumRodjenja(dto.getDatumRodjenja());
        entity.setAdresa(dto.getAdresa());
        entity.setTelefon(dto.getTelefon());
        entity.setSrednjaSkola(srednjaSkola);
        entity.setProsek1Razred(dto.getProsek1Razred());
        entity.setProsek2Razred(dto.getProsek2Razred());
        entity.setProsek3Razred(dto.getProsek3Razred());
        entity.setProsek4Razred(dto.getProsek4Razred());
        entity.setKorisnik(korisnik);
        return entity;
    }
}
