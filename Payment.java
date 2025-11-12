
import java.time.LocalDateTime;
import java.util.UUID;

class Payment {
    String paymentId;
    double amount;
    String status;
    LocalDateTime timestamp;

    Payment(double amount) {
        this.paymentId = UUID.randomUUID().toString();
        this.amount = amount;
        this.status = "PENDING";
        this.timestamp = LocalDateTime.now();
    }

    void processPayment() {
        System.out.println("Processing payment of $" + amount);
        status = "COMPLETED";
        this.timestamp = LocalDateTime.now();
        System.out.println("Payment successful. Status: " + status);
    }
}

