package com.fon.prijavaBackend.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class PrijavaProgramDTO implements Serializable, DomainDTO {

    private long id;

    @NotNull(message = "Prijava (prijava.id) je obavezna")
    private PrijavaDTO prijava;

    @NotNull(message = "Studijski program (studijskiProgram.id) je obavezan")
    private StudijskiProgramDTO studijskiProgram;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public PrijavaDTO getPrijava() { return prijava; }
    public void setPrijava(PrijavaDTO prijava) { this.prijava = prijava; }
    public StudijskiProgramDTO getStudijskiProgram() { return studijskiProgram; }
    public void setStudijskiProgram(StudijskiProgramDTO studijskiProgram) { this.studijskiProgram = studijskiProgram; }
}
