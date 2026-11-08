/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fon.prijavaBackend.JPARepo;

import com.fon.prijavaBackend.model.SrednjaSkola;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SrednjaSkolaRepository extends JpaRepository<SrednjaSkola, Long> {

    Optional<SrednjaSkola> findBySifra(String sifra);
}
