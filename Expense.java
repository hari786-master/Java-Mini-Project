
import java.io.Serializable;

class Expense implements Serializable {
    private static final long serialVersionUID = 2L;

    String description;
    double amount;

    Expense(String description, double amount) {
        this.amount = amount;
        this.description = description;
    }

    double totalExpenses() {
        return amount;
    }

}
