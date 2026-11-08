package com.fon.prijavaBackend.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.time.LocalDate;

public class KandidatDTO implements Serializable, DomainDTO {

    private long id;

    @NotBlank(message = "JMBG je obavezan")
    @Pattern(regexp = "\\d{13}", message = "JMBG mora imati tačno 13 cifara")
    private String jmbg;

    @NotNull(message = "Datum rođenja je obavezan")
    @Past(message = "Datum rođenja mora biti u prošlosti")
    private LocalDate datumRodjenja;

    private String adresa;

    private String telefon;

    private SrednjaSkolaDTO srednjaSkola;

    @DecimalMin(value = "1.00", message = "Prosek I razreda ne može biti manji od 1.00")
    @DecimalMax(value = "5.00", message = "Prosek I razreda ne može biti veći od 5.00")
    private double prosek1Razred;

    @DecimalMin(value = "1.00", message = "Prosek II razreda ne može biti manji od 1.00")
    @DecimalMax(value = "5.00", message = "Prosek II razreda ne može biti veći od 5.00")
    private double prosek2Razred;

    @DecimalMin(value = "1.00", message = "Prosek III razreda ne može biti manji od 1.00")
    @DecimalMax(value = "5.00", message = "Prosek III razreda ne može biti veći od 5.00")
    private double prosek3Razred;

    @DecimalMin(value = "1.00", message = "Prosek IV razreda ne može biti manji od 1.00")
    @DecimalMax(value = "5.00", message = "Prosek IV razreda ne može biti veći od 5.00")
    private double prosek4Razred;

    @NotNull(message = "Korisnik (korisnik.id) je obavezan")
    private KorisnikDTO korisnik;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getJmbg() { return jmbg; }
    public void setJmbg(String jmbg) { this.jmbg = jmbg; }
    public LocalDate getDatumRodjenja() { return datumRodjenja; }
    public void setDatumRodjenja(LocalDate datumRodjenja) { this.datumRodjenja = datumRodjenja; }
    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public SrednjaSkolaDTO getSrednjaSkola() { return srednjaSkola; }
    public void setSrednjaSkola(SrednjaSkolaDTO srednjaSkola) { this.srednjaSkola = srednjaSkola; }
    public double getProsek1Razred() { return prosek1Razred; }
    public void setProsek1Razred(double prosek1Razred) { this.prosek1Razred = prosek1Razred; }
    public double getProsek2Razred() { return prosek2Razred; }
    public void setProsek2Razred(double prosek2Razred) { this.prosek2Razred = prosek2Razred; }
    public double getProsek3Razred() { return prosek3Razred; }
    public void setProsek3Razred(double prosek3Razred) { this.prosek3Razred = prosek3Razred; }
    public double getProsek4Razred() { return prosek4Razred; }
    public void setProsek4Razred(double prosek4Razred) { this.prosek4Razred = prosek4Razred; }
    public KorisnikDTO getKorisnik() { return korisnik; }
    public void setKorisnik(KorisnikDTO korisnik) { this.korisnik = korisnik; }
}
