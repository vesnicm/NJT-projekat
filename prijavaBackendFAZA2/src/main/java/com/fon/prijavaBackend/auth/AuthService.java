package com.fon.prijavaBackend.auth;

import com.fon.prijavaBackend.JPARepo.KandidatRepository;
import com.fon.prijavaBackend.JPARepo.KorisnikRepository;
import com.fon.prijavaBackend.JPARepo.SrednjaSkolaRepository;
import com.fon.prijavaBackend.exception.BusinessException;
import com.fon.prijavaBackend.exception.ResourceNotFoundException;
import com.fon.prijavaBackend.model.Kandidat;
import com.fon.prijavaBackend.model.Korisnik;
import com.fon.prijavaBackend.model.SrednjaSkola;
import com.fon.prijavaBackend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final KorisnikRepository korisnikRepository;
    private final KandidatRepository kandidatRepository;
    private final SrednjaSkolaRepository srednjaSkolaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(KorisnikRepository korisnikRepository,
                        KandidatRepository kandidatRepository,
                        SrednjaSkolaRepository srednjaSkolaRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService) {
        this.korisnikRepository = korisnikRepository;
        this.kandidatRepository = kandidatRepository;
        this.srednjaSkolaRepository = srednjaSkolaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginResponseDTO register(RegisterRequestDTO dto) {
        korisnikRepository.findByEmail(dto.getEmail()).ifPresent(k -> {
            throw new BusinessException("Nalog sa email adresom '" + dto.getEmail() + "' već postoji");
        });
        kandidatRepository.findByJmbg(dto.getJmbg()).ifPresent(k -> {
            throw new BusinessException("Kandidat sa JMBG '" + dto.getJmbg() + "' već postoji");
        });
        SrednjaSkola skola = srednjaSkolaRepository.findById(dto.getSrednjaSkolaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Srednja škola sa ID " + dto.getSrednjaSkolaId() + " ne postoji"));

        Korisnik korisnik = new Korisnik();
        korisnik.setIme(dto.getIme());
        korisnik.setPrezime(dto.getPrezime());
        korisnik.setEmail(dto.getEmail());
        korisnik.setLozinka(passwordEncoder.encode(dto.getLozinka()));
        korisnik.setUloga("KANDIDAT");
        Korisnik sacuvanKorisnik = korisnikRepository.save(korisnik);

        Kandidat kandidat = new Kandidat();
        kandidat.setJmbg(dto.getJmbg());
        kandidat.setDatumRodjenja(dto.getDatumRodjenja());
        kandidat.setAdresa(dto.getAdresa());
        kandidat.setTelefon(dto.getTelefon());
        kandidat.setSrednjaSkola(skola);
        kandidat.setProsek1Razred(dto.getProsek1Razred());
        kandidat.setProsek2Razred(dto.getProsek2Razred());
        kandidat.setProsek3Razred(dto.getProsek3Razred());
        kandidat.setProsek4Razred(dto.getProsek4Razred());
        kandidat.setKorisnik(sacuvanKorisnik);
        Kandidat sacuvanKandidat = kandidatRepository.save(kandidat);

        String token = jwtService.generisiToken(
                sacuvanKorisnik.getId(),
                sacuvanKorisnik.getEmail(),
                sacuvanKorisnik.getUloga(),
                sacuvanKandidat.getId());

        return new LoginResponseDTO(
                sacuvanKorisnik.getId(), sacuvanKorisnik.getIme(), sacuvanKorisnik.getPrezime(),
                sacuvanKorisnik.getEmail(), sacuvanKorisnik.getUloga(), sacuvanKandidat.getId(),
                token);
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO dto) {
        Korisnik korisnik = korisnikRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BusinessException("Pogrešan email ili lozinka"));

        if (!passwordEncoder.matches(dto.getLozinka(), korisnik.getLozinka())) {
            throw new BusinessException("Pogrešan email ili lozinka");
        }

        Long kandidatId = kandidatRepository.findByKorisnikId(korisnik.getId())
                .map(Kandidat::getId)
                .orElse(null);

        String token = jwtService.generisiToken(
                korisnik.getId(),
                korisnik.getEmail(),
                korisnik.getUloga(),
                kandidatId);

        return new LoginResponseDTO(
                korisnik.getId(), korisnik.getIme(), korisnik.getPrezime(),
                korisnik.getEmail(), korisnik.getUloga(), kandidatId, token);
    }
}