/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fon.prijavaBackend.JPARepo;

import com.fon.prijavaBackend.model.Kandidat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KandidatRepository extends JpaRepository<Kandidat, Long> {
    
    Optional<Kandidat> findByJmbg(String jmbg);
    Optional<Kandidat> findByKorisnikId(long korisnikId);
}
