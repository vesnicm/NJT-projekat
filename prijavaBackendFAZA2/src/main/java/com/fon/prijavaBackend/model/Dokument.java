/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fon.prijavaBackend.model;

/**
 *
 * @author dule1
 */
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Dokument implements Serializable, DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prijava_id", nullable = false)
    private Prijava prijava;

    @Column(nullable = false)
    private String tip;

    @Column(nullable = false)
    private String nazivFajla;

    @Column(nullable = false)
    private String putanja;

    @Column(nullable = false)
    private LocalDateTime datumUploada;

    @Column(nullable = false)
    private boolean verifikovan = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Prijava getPrijava() {
        return prijava;
    }

    public void setPrijava(Prijava prijava) {
        this.prijava = prijava;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getNazivFajla() {
        return nazivFajla;
    }

    public void setNazivFajla(String nazivFajla) {
        this.nazivFajla = nazivFajla;
    }

    public String getPutanja() {
        return putanja;
    }

    public void setPutanja(String putanja) {
        this.putanja = putanja;
    }

    public LocalDateTime getDatumUploada() {
        return datumUploada;
    }

    public void setDatumUploada(LocalDateTime datumUploada) {
        this.datumUploada = datumUploada;
    }
    
    public boolean isVerifikovan() {
        return verifikovan;
    }

    public void setVerifikovan(boolean verifikovan) {
        this.verifikovan = verifikovan;
    }
}
