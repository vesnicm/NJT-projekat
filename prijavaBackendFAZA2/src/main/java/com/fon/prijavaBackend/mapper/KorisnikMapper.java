/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fon.prijavaBackend.mapper;

import com.fon.prijavaBackend.dto.KorisnikDTO;
import com.fon.prijavaBackend.model.Korisnik;
import org.springframework.stereotype.Component;

@Component
public class KorisnikMapper implements BaseMapper<KorisnikDTO, Korisnik> {

    @Override
    public KorisnikDTO toDomainDTO(Korisnik entity) {
        if (entity == null) return null;

        KorisnikDTO dto = new KorisnikDTO();
        dto.setId(entity.getId());
        dto.setIme(entity.getIme());
        dto.setPrezime(entity.getPrezime());
        dto.setEmail(entity.getEmail());
        dto.setUloga(entity.getUloga());
        return dto;
    }

    @Override
    public Korisnik toDomainEntity(KorisnikDTO dto) {
        if (dto == null) return null;

        Korisnik entity = new Korisnik();
        if (dto.getId() != 0) {
            entity.setId(dto.getId());
        }
        entity.setIme(dto.getIme());
        entity.setPrezime(dto.getPrezime());
        entity.setEmail(dto.getEmail());
        entity.setLozinka(dto.getLozinka());
        entity.setUloga(dto.getUloga());
        return entity;
    }
}