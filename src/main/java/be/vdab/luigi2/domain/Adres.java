package be.vdab.luigi2.domain;

import org.hibernate.validator.constraints.Range;

public class Adres {
    private final String straat;
    private final String huisNr;
    @Range(min = 1000, max = 9999)
    private final int postcode;
    private final String gemeente;

    public Adres(String straat, String huisNr, int postcode, String gemeente) {
        this.straat = straat;
        this.huisNr = huisNr;
        this.postcode = postcode;
        this.gemeente = gemeente;
    }

    public String getStraat() {
        return straat;
    }

    public String getHuisNr() {
        return huisNr;
    }

    public int getPostcode() {
        return postcode;
    }

    public String getGemeente() {
        return gemeente;
    }
}
