import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

class Event implements Serializable {
    private static final long serialVersionUID = 2L;
    String name;
    double budget;
    String category;
    Venue venue;
    LocalDate date;
    // static int EventNumber = 1;
    ArrayList<User> users;
    ArrayList<Session> schedule;
    ArrayList<Sponsor> sponsors;
    ArrayList<Ticket> ticketsSold;
    ArrayList<Expense> expenses;
    ArrayList<Feedback> feedback;
    ArrayList<Poll> polls;
    ArrayList<Booth> booths;
    ArrayList<Speaker> speakers;
    int num;

    Event(String name, double budget, Venue venue, String category, LocalDate date) {
        this.name = name;
        this.budget = budget;
        this.venue = venue;
        this.category = category;
        this.date = date;
        this.users = new ArrayList<>();
        this.schedule = new ArrayList<>();
        this.sponsors = new ArrayList<>();
        this.ticketsSold = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.feedback = new ArrayList<>();
        this.polls = new ArrayList<>();
        this.booths = new ArrayList<>();
        this.speakers = new ArrayList<>();
    }

    void getDate() {
        System.out.println(date);
    }

    void addUser(User u) {
        users.add(u);
    }

    void addSession(Session s) {
        schedule.add(s);
    }

    void addSponsor(Sponsor s) {
        sponsors.add(s);
    }

    void changeVenue(Venue v) {
        venue = v;
    }

    

    void addExpense(Expense e) {
        expenses.add(e);
    }

    void addBooth(Booth b) {
        booths.add(b);
    }

    double totalExpenses() {
        double result = 0;
        for (Expense e : expenses) {
            result += e.totalExpenses();
        }
        return result;
    }

    String displayEvent() {
        String reset = "\033[0m";
        String bold = "\033[1m";
        String red = "\033[31m";
        String green = "\033[32m";
        String cyan = "\033[36m";
        String yellow = "\033[33m";

        // Box borders
        String line = "┌──────────────────────────────────────────────────┐";
        String bottom = "└──────────────────────────────────────────────────┘";

        String result = "\n" + green + line;
        result += "\n│                                                  │";
        result += "\n│                " + bold + red + "   EVENT DETAILS   " + reset + green + "               │";
        result += "\n│                                                  │" + reset;
        result += green + "\n│   Name:       " + bold + cyan + name + reset;
        result += green + "\n│   Category:   " + bold + yellow + category + reset;
        result += green + "\n│   Location:   " + bold + cyan + venue.name + reset;
        result += green + "\n│   Date:       " + bold + green + date + reset;
        result += green + "\n│   Budget:     " + bold + "$" + budget + reset;
        result += green + "\n│   Event No:   " + bold + num + reset;
        result += green + "\n│                                                  │";
        result += "\n" + green + bottom + reset;
        return result;
    }

}
