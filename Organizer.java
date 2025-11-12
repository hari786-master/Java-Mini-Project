import java.io.Serializable;
import java.util.ArrayList;

public class Organizer implements Serializable {
    private static final long serialVersionUID = 2L;
    String name;
    String email;
    String password;
    ArrayList<Event> events;

    Organizer(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        events = new ArrayList<>();
    }

    
}
