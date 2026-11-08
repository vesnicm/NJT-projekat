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
public class StudijskiProgram implements Serializable, DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String naziv;

    private String opis;

    @Column(nullable = false)
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
