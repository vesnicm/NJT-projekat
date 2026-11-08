package com.fon.prijavaBackend.dto;

import com.fon.prijavaBackend.model.enums.StatusPrijave;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PrijavaDTO implements Serializable, DomainDTO {

    private long id;

    private String brojPrijave;

    private double cena;

    private KorisnikDTO pregledao;

    @NotNull(message = "Kandidat (kandidat.id) je obavezan")
    private KandidatDTO kandidat;

    private LocalDateTime datumPodnosenja;

    private StatusPrijave status;

    private String napomena;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getBrojPrijave() { return brojPrijave; }
    public void setBrojPrijave(String brojPrijave) { this.brojPrijave = brojPrijave; }
    public double getCena() { return cena; }
    public void setCena(double cena) { this.cena = cena; }
    public KorisnikDTO getPregledao() { return pregledao; }
    public void setPregledao(KorisnikDTO pregledao) { this.pregledao = pregledao; }
    public KandidatDTO getKandidat() { return kandidat; }
    public void setKandidat(KandidatDTO kandidat) { this.kandidat = kandidat; }
    public LocalDateTime getDatumPodnosenja() { return datumPodnosenja; }
    public void setDatumPodnosenja(LocalDateTime datumPodnosenja) { this.datumPodnosenja = datumPodnosenja; }
    public StatusPrijave getStatus() { return status; }
    public void setStatus(StatusPrijave status) { this.status = status; }
    public String getNapomena() { return napomena; }
    public void setNapomena(String napomena) { this.napomena = napomena; }
}
