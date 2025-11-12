import java.io.Serializable;
import java.util.UUID;

class Booth implements Serializable {
    private static final long serialVersionUID = 2L;

    String boothId;
    double size;
    double price;
    Sponsor assignedSponsor;
    String name;

    Booth(String name, double size, double price) {
        this(name, size, price, UUID.randomUUID().toString());
    }

    Booth(String name, double size, double price, String boothId) {
        this.boothId = boothId;
        this.price = price;
        this.size = size;
        this.name = name;
    }

    boolean isAvailable() {
        return assignedSponsor == null;
    }

    boolean assignSponsor(Sponsor sponsor) {
        if (isAvailable()) {
            this.assignedSponsor = sponsor;
            return true;
        } else
            return false;
    }

    void unassignSponsor() {
        this.assignedSponsor = null;
    }

    double getPrice() {
        return price;
    }

    public String toString() {
        return name + "|" + size + "|" + price + "|" + boothId;
    }

}
