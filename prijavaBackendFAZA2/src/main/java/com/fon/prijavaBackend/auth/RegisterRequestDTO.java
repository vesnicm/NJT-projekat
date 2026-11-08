/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fon.prijavaBackend.auth;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class RegisterRequestDTO {

    @NotBlank(message = "Ime je obavezno")
    private String ime;

    @NotBlank(message = "Prezime je obavezno")
    private String prezime;

    @NotBlank(message = "Email je obavezan")
    @Email(message = "Email nije validan")
    private String email;

    @NotBlank(message = "Lozinka je obavezna")
    @Size(min = 6, message = "Lozinka mora imati bar 6 karaktera")
    private String lozinka;

    @NotBlank(message = "JMBG je obavezan")
    @Pattern(regexp = "\\d{13}", message = "JMBG mora imati tačno 13 cifara")
    private String jmbg;

    @NotNull(message = "Datum rođenja je obavezan")
    @Past(message = "Datum rođenja mora biti u prošlosti")
    private LocalDate datumRodjenja;

    private String adresa;
    private String telefon;
    @NotNull(message = "Srednja škola je obavezna")
    private Long srednjaSkolaId;

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

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }
    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getLozinka() { return lozinka; }
    public void setLozinka(String lozinka) { this.lozinka = lozinka; }
    public String getJmbg() { return jmbg; }
    public void setJmbg(String jmbg) { this.jmbg = jmbg; }
    public LocalDate getDatumRodjenja() { return datumRodjenja; }
    public void setDatumRodjenja(LocalDate datumRodjenja) { this.datumRodjenja = datumRodjenja; }
    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public Long getSrednjaSkolaId() { return srednjaSkolaId; }
    public void setSrednjaSkolaId(Long srednjaSkolaId) { this.srednjaSkolaId = srednjaSkolaId; }
    public double getProsek1Razred() { return prosek1Razred; }
    public void setProsek1Razred(double prosek1Razred) { this.prosek1Razred = prosek1Razred; }
    public double getProsek2Razred() { return prosek2Razred; }
    public void setProsek2Razred(double prosek2Razred) { this.prosek2Razred = prosek2Razred; }
    public double getProsek3Razred() { return prosek3Razred; }
    public void setProsek3Razred(double prosek3Razred) { this.prosek3Razred = prosek3Razred; }
    public double getProsek4Razred() { return prosek4Razred; }
    public void setProsek4Razred(double prosek4Razred) { this.prosek4Razred = prosek4Razred; }
}
