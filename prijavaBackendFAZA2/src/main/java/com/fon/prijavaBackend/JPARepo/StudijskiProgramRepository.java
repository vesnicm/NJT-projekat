/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fon.prijavaBackend.JPARepo;

import com.fon.prijavaBackend.model.StudijskiProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudijskiProgramRepository extends JpaRepository<StudijskiProgram, Long> {
    
    List<StudijskiProgram> findByNazivContainingIgnoreCase(String naziv);
}
