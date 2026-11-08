/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fon.prijavaBackend.mapper;

import com.fon.prijavaBackend.dto.DomainDTO;
import com.fon.prijavaBackend.model.DomainEntity;

/**
 *
 * @author dule1
 */
public interface BaseMapper<DTO extends DomainDTO, DB extends DomainEntity> {
    DTO toDomainDTO(DB entity);
    DB toDomainEntity(DTO dto);
}