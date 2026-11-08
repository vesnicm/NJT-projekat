/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fon.prijavaBackend.JPARepo;

import com.fon.prijavaBackend.model.PrijavaProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PrijavaProgramRepository extends JpaRepository<PrijavaProgram, Long> {
    List<PrijavaProgram> findByPrijavaId(long prijavaId);
    List<PrijavaProgram> findByStudijskiProgramId(long studijskiProgramId);
}
