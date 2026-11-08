/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fon.prijavaBackend.auth;

public class LoginResponseDTO {

    private long korisnikId;
    private String ime;
    private String prezime;
    private String email;
    private String uloga;
    private Long kandidatId;
    private String token;

    public LoginResponseDTO(long korisnikId, String ime, String prezime, String email, String uloga, Long kandidatId, String token) {
        this.korisnikId = korisnikId;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.uloga = uloga;
        this.kandidatId = kandidatId;
        this.token = token;
    }
    
    public long getKorisnikId() { return korisnikId; }
    public String getIme() { return ime; }
    public String getPrezime() { return prezime; }
    public String getEmail() { return email; }
    public String getUloga() { return uloga; }
    public Long getKandidatId() { return kandidatId; }
    public String getToken() {return token; }
}