package be.vdab.luigi2.domain;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.time.LocalDate;

public class Persoon {
    private final String voornaam;
    private final String familienaam;
    @Range(min = 0, max = 69)
    private final int aantalKinderen;
    private final boolean gehuwd;
    @DateTimeFormat(style = "S-")
    private final LocalDate geboorte;
    @Valid
    private final Adres adres;

    public Persoon(String voornaam, String familienaam, int aantalKinderen, boolean gehuwd, LocalDate geboorte, Adres adres) {
        this.voornaam = voornaam;
        this.familienaam = familienaam;
        this.aantalKinderen = aantalKinderen;
        this.gehuwd = gehuwd;
        this.geboorte = geboorte;
        this.adres = adres;
    }

    public String getNaam() {
        return voornaam+" "+familienaam;
    }

    public int getAantalKinderen() {
        return aantalKinderen;
    }

    public boolean isGehuwd() {
        return gehuwd;
    }

    public LocalDate getGeboorte() {
        return geboorte;
    }

    public Adres getAdres() {
        return adres;
    }
}
