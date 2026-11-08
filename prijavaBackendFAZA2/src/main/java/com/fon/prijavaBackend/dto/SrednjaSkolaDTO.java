package com.fon.prijavaBackend.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class SrednjaSkolaDTO implements Serializable, DomainDTO {

    private long id;

    @NotBlank(message = "Šifra je obavezna")
    private String sifra;

    @NotBlank(message = "Naziv je obavezan")
    private String naziv;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getSifra() { return sifra; }
    public void setSifra(String sifra) { this.sifra = sifra; }
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
}
