package com.fon.prijavaBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DokumentDTO implements Serializable, DomainDTO {

    private long id;

    @NotNull(message = "Prijava (prijava.id) je obavezna")
    private PrijavaDTO prijava;

    @NotBlank(message = "Tip dokumenta je obavezan")
    private String tip;

    @NotBlank(message = "Naziv fajla je obavezan")
    private String nazivFajla;

    @NotBlank(message = "Putanja je obavezna")
    private String putanja;

    private LocalDateTime datumUploada;
    
    private boolean verifikovan = false;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public PrijavaDTO getPrijava() { return prijava; }
    public void setPrijava(PrijavaDTO prijava) { this.prijava = prijava; }
    public String getTip() { return tip; }
    public void setTip(String tip) { this.tip = tip; }
    public String getNazivFajla() { return nazivFajla; }
    public void setNazivFajla(String nazivFajla) { this.nazivFajla = nazivFajla; }
    public String getPutanja() { return putanja; }
    public void setPutanja(String putanja) { this.putanja = putanja; }
    public LocalDateTime getDatumUploada() { return datumUploada; }
    public void setDatumUploada(LocalDateTime datumUploada) { this.datumUploada = datumUploada; }
    public boolean isVerifikovan() { return verifikovan; }
    public void setVerifikovan(boolean verifikovan) { this.verifikovan = verifikovan; }
}
