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

@Entity
public class SrednjaSkola implements Serializable, DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String sifra;

    @Column(nullable = false)
    private String naziv;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getSifra() { return sifra; }
    public void setSifra(String sifra) { this.sifra = sifra; }
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
}
