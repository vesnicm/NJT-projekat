/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fon.prijavaBackend.JPARepo;

import com.fon.prijavaBackend.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {
    
    Optional<Korisnik> findByEmail(String email);
}
