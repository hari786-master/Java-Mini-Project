
import java.io.Serializable;

class Speaker implements Serializable {
    private static final long serialVersionUID = 2L;
    String name;
    String gender;

    Speaker(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }

    String displaySpeaker() {
        return "Name: " + name + "\nGender: " + gender;
    }

}
