
import java.io.Serializable;
import java.util.ArrayList;

class Session implements Serializable {
    private static final long serialVersionUID = 2L;
    String topic;
    int totalHour;
    Speaker speaker;
    double time;
    String timeRange;

    ArrayList<Feedback> feedback;
    ArrayList<Poll> polls;

    Session(String topic, String timeRange) {
        this.topic = topic;
        this.timeRange = timeRange;
    }
    Session(String topic) {
        this.topic = topic;
        this.feedback = new ArrayList<>();
        this.polls = new ArrayList<>();
    }

    void assignSpeaker(Speaker s) {
        speaker = s;
    }

    void addFeedback(Feedback f) {
        feedback.add(f);
    }

    // String displaySession() {
    //     if (speaker != null) {
    //         return period + " : " + topic + " | Speaker: " + speaker.name + " Gender: " + speaker.gender;
    //     } else {
    //         return period + " : " + topic;
    //     }
    // }

    double getAverageRating() {
        double rating = 0;
        for (Feedback f : feedback) {
            rating += f.rating;
        }
        return (rating / feedback.size());
    }
}
