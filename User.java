import java.io.Serializable;
import java.util.ArrayList;

class User implements Serializable {
    private static final long serialVersionUID = 2L;
    int userId;
    String name;
    String email;
    String password;
    ArrayList<Ticket> ticket;
    String role;

    User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        ticket = new ArrayList<>();
    }

    boolean checkPassword(String pass) {
        return password.equals(pass);
    }

    void addTicket(Ticket t) {
        ticket.add(t);
    }

  

}
