/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.fon.prijavaBackend.JPARepo;

import com.fon.prijavaBackend.model.Prijava;
import com.fon.prijavaBackend.model.enums.StatusPrijave;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PrijavaRepository extends JpaRepository<Prijava, Long> {
    
    List<Prijava> findByKandidatId(long kandidatId);
    List<Prijava> findByStatus(StatusPrijave status);
    
    @Query("""
    SELECT DISTINCT p FROM Prijava p
    JOIN p.kandidat k
    JOIN k.korisnik ko
    WHERE (:status IS NULL OR p.status = :status)
      AND (:jmbg IS NULL OR k.jmbg = :jmbg)
      AND (:ime IS NULL OR LOWER(ko.ime) LIKE LOWER(CONCAT('%', :ime, '%')))
      AND (:prezime IS NULL OR LOWER(ko.prezime) LIKE LOWER(CONCAT('%', :prezime, '%')))
      AND (:programId IS NULL OR EXISTS (
              SELECT 1 FROM PrijavaProgram pp
              WHERE pp.prijava = p AND pp.studijskiProgram.id = :programId))
    """)
List<Prijava> pretraga(@Param("status") StatusPrijave status,
                        @Param("jmbg") String jmbg,
                        @Param("ime") String ime,
                        @Param("prezime") String prezime,
                        @Param("programId") Long programId);
}
