import java.io.Serializable;

class Sponsor implements Serializable {
    private static final long serialVersionUID = 2L;
    String companyName;
    double amountSponsor;

    Sponsor(String companyName, double amountSponsor) {
        this.companyName = companyName;
        this.amountSponsor = amountSponsor;
    }

    String displaySponsor() {
        return "Company Name: " + companyName + "\nAmount Sponsor: " + amountSponsor;
    }
}
