/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fon.prijavaBackend.model;

/**
 *
 * @author dule1
 */

import com.fon.prijavaBackend.model.enums.StatusPrijave;   
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Prijava implements Serializable, DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "broj_prijave", unique = true)
    private String brojPrijave;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kandidat_id", nullable = false)
    private Kandidat kandidat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pregledao_id")
    private Korisnik pregledao;

    private double cena;

    @Column(nullable = false)
    private LocalDateTime datumPodnosenja;

    @Enumerated(EnumType.STRING)              
    @Column(nullable = false)
    private StatusPrijave status;             

    private String napomena;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getBrojPrijave() { return brojPrijave; }
    public void setBrojPrijave(String brojPrijave) { this.brojPrijave = brojPrijave; }
    public Kandidat getKandidat() { return kandidat; }
    public void setKandidat(Kandidat kandidat) { this.kandidat = kandidat; }
    public Korisnik getPregledao() { return pregledao; }
    public void setPregledao(Korisnik pregledao) { this.pregledao = pregledao; }
    public double getCena() { return cena; }
    public void setCena(double cena) { this.cena = cena; }
    public LocalDateTime getDatumPodnosenja() { return datumPodnosenja; }
    public void setDatumPodnosenja(LocalDateTime datumPodnosenja) { this.datumPodnosenja = datumPodnosenja; }
    public StatusPrijave getStatus() { return status; }        
    public void setStatus(StatusPrijave status) { this.status = status; } 
    public String getNapomena() { return napomena; }
    public void setNapomena(String napomena) { this.napomena = napomena; }
}
