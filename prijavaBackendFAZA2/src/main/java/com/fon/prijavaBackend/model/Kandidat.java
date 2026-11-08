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
import java.time.LocalDate;

@Entity
public class Kandidat implements Serializable, DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String jmbg;

    @Column(nullable = false)
    private LocalDate datumRodjenja;

    private String adresa;

    private String telefon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "srednja_skola_id")
    private SrednjaSkola srednjaSkola;

    @Column(name = "prosek_1_razred")
    private double prosek1Razred;

    @Column(name = "prosek_2_razred")
    private double prosek2Razred;

    @Column(name = "prosek_3_razred")
    private double prosek3Razred;

    @Column(name = "prosek_4_razred")
    private double prosek4Razred;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "korisnik_id", nullable = false, unique = true)
    private Korisnik korisnik;

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
    public SrednjaSkola getSrednjaSkola() { return srednjaSkola; }
    public void setSrednjaSkola(SrednjaSkola srednjaSkola) { this.srednjaSkola = srednjaSkola; }
    public double getProsek1Razred() { return prosek1Razred; }
    public void setProsek1Razred(double prosek1Razred) { this.prosek1Razred = prosek1Razred; }
    public double getProsek2Razred() { return prosek2Razred; }
    public void setProsek2Razred(double prosek2Razred) { this.prosek2Razred = prosek2Razred; }
    public double getProsek3Razred() { return prosek3Razred; }
    public void setProsek3Razred(double prosek3Razred) { this.prosek3Razred = prosek3Razred; }
    public double getProsek4Razred() { return prosek4Razred; }
    public void setProsek4Razred(double prosek4Razred) { this.prosek4Razred = prosek4Razred; }
    public Korisnik getKorisnik() { return korisnik; }
    public void setKorisnik(Korisnik korisnik) { this.korisnik = korisnik; }
}
