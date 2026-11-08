package com.fon.prijavaBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

import java.io.Serializable;

public class StudijskiProgramDTO implements Serializable, DomainDTO {

    private long id;

    @NotBlank(message = "Naziv je obavezan")
    private String naziv;

    private String opis;

    @PositiveOrZero(message = "Broj mesta ne može biti negativan")
    private long brojMesta;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    public String getOpis() { return opis; }
    public void setOpis(String opis) { this.opis = opis; }
    public long getBrojMesta() { return brojMesta; }
    public void setBrojMesta(long brojMesta) { this.brojMesta = brojMesta; }
}
