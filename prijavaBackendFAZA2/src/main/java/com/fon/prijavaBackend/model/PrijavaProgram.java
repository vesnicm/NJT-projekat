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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"prijava_id", "program_id"}))
public class PrijavaProgram implements Serializable, DomainEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prijava_id", nullable = false)
    private Prijava prijava;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "program_id", nullable = false)
    private StudijskiProgram studijskiProgram;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public Prijava getPrijava() { return prijava; }
    public void setPrijava(Prijava prijava) { this.prijava = prijava; }
    public StudijskiProgram getStudijskiProgram() { return studijskiProgram; }
    public void setStudijskiProgram(StudijskiProgram studijskiProgram) { this.studijskiProgram = studijskiProgram; }
}
