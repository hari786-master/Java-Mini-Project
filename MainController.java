import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MainController {
    ArrayList<Event> events = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();
    User currentUser;
    File path = new File("text.csv");
    File data = new File("data.csv");

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        MainController controller = new MainController();
        controller.events = controller.readEvents();
        controller.readUser();

        String line = "┌───────────────────────────────┐";
        String bottom = "└───────────────────────────────┘";
        start: while (true) {
            LocalDate dateInput;
            int roleChoice = 0;
            int userOrOrg = 0;
            System.out.println("1.Sign in\n2.Sign Up\n3.Exit");
            try {
                System.out.print("Enter Your Choice: ");
                userOrOrg = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                input.nextLine();
                continue;
            }
            if (userOrOrg == 1) {
                input.nextLine();
                System.out.print("Enter Your Name: ");
                String name = input.nextLine();
                System.out.print("Enter Your Email: ");
                String email = input.nextLine();
                System.out.print("Enter Your Password: ");
                String password = input.nextLine();
                boolean isValidUser = false;
                boolean isEmailExist = false;
                for (User u : controller.users) {
                    if (u.email.equals(email.toLowerCase())) {
                        isEmailExist = true;
                        if (u.password.equals(password)) {
                            isValidUser = true;
                            if (u.role.equals("user")) {
                                roleChoice = 2;
                                controller.currentUser = u;
                            } else if (u.role.equals("organizer")) {
                                roleChoice = 1;
                            }
                            break;
                        } else {
                            System.out.println("\u001B[91mInvalid Password\u001B[0m\n");
                            continue start;
                        }
                    }
                }
                if (!isValidUser) {
                    System.out.println("\u001B[91mInvalid User\u001B[0m\n");
                    continue start;
                }

            } else if (userOrOrg == 2) {
                input.nextLine();
                System.out.print("Enter Your Name: ");
                String name = input.nextLine();
                System.out.print("Enter Your Email ID: ");
                String email = input.nextLine();
                System.out.print("Enter The Password: ");
                String password = input.nextLine();
                String role = "";
                while (true) {
                    System.out.print("Enter The Role (user or organizer) : ");
                    role = input.nextLine().toLowerCase();
                    if (role.equals("user") || role.equals("organizer")) {
                        break;
                    } else {
                        System.out.println("\u001B[91mInvalid Role\u001B[0m\n");
                    }
                }
                for (User u : controller.users) {
                    if (u.email.equals(email)) {
                        System.out.println("Email Already Exist. Please Sign In");
                        continue start;
                    }
                }
                if (role.equals("organizer"))
                    roleChoice = 1;
                else if (role.equals("user"))
                    roleChoice = 2;
                User user = new User(name, email, password, role);
                controller.users.add(user);
                controller.writeUser(user);
            } else if (userOrOrg == 3) {
                System.out.println("Thank You");
                break;
            }

            if (roleChoice == 1) {
                int choice = 0;
                eve: while (true) {
                    try {
                        System.out.println("\u001B[33m\u001B[1m" + line +
                                "\n│                               │\n│        1. Add Event           │\n│        2. Modify Event        │\n│        3. Logout              │\n│                               │\n"
                                + bottom + "\u001B[0m");
                        System.out.print("Enter Your Choice: ");
                        choice = input.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                        input.nextLine();
                    }
                    if (choice == 1) {
                        input.nextLine();
                        System.out.print("Enter the Event Name: ");
                        String eventName = input.nextLine();
                        System.out.print("Enter The Location: ");
                        String location = input.nextLine();
                        double budget = 0;
                        while (true) {
                            try {
                                System.out.print("Enter Your Budget: ");
                                budget = input.nextDouble();
                                break;
                            } catch (InputMismatchException e) {
                                System.out.println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                input.nextLine();
                                continue;
                            }
                        }
                        input.nextLine();
                        System.out.print("Enter The Category: ");
                        String category = input.nextLine();
                        int capacity = 0;
                        while (true) {
                            try {
                                System.out.print("Enter The Capacity: ");
                                capacity = input.nextInt();
                                input.nextLine();
                                break;
                            } catch (InputMismatchException e) {
                                System.out.println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                input.nextLine();
                                continue;
                            }
                        }
                        while (true) {
                            System.out.print("Enter event date (yyyy-mm-dd): ");
                            String date = input.nextLine();
                            try {
                                dateInput = LocalDate.parse(date);
                                if (!dateInput.isAfter(LocalDate.now())) {
                                    System.out.println("Date cannot be in the past. Please enter a future date.");
                                } else {
                                    break;
                                }
                            } catch (DateTimeParseException e) {
                                System.out.println("Enter a valid date (format: yyyy-MM-dd)");
                            }
                        }
                        Venue venue = new Venue(location, capacity);
                        Event event = new Event(eventName, budget, venue, category, dateInput);
                        controller.addEvents(event);

                        // controller.fileWriter("events", controller.events);

                        System.out.println("Event Added Sucessfully :)");
                    } else if (choice == 2) {
                        System.out.println(controller.displayAllEvents());
                        int eventNumber = 0;
                        while (true) {
                            try {
                                System.out.print("Enter The Event Number To Modify (Exit:0): ");
                                eventNumber = input.nextInt();
                                if (eventNumber == 0) {
                                    continue eve;
                                }
                                controller.events.get(eventNumber - 1);
                                break;
                            } catch (Exception e) {
                                System.out
                                        .println(
                                                "\u001B[91mInvalid Input \u001B[0m\n");
                                input.nextLine();
                                continue;
                            }

                        }
                        while (true) {
                            int eventChoice = 0;
                            while (true) {
                                System.out.println("\u001B[33m\u001B[1m┌──────────────────────────┐");
                                System.out.println("│                          │");
                                System.out.println("│      \u001B[38m1. Add Booth        \u001B[33m\u001B[1m│");
                                System.out.println("│      \u001B[32m2. Add Session      \u001B[33m\u001B[1m│");
                                System.out.println("│      \u001B[34m3. Add Speaker      \u001B[33m\u001B[1m│");
                                System.out.println("│      \u001B[35m4. Add Sponsor      \u001B[33m\u001B[1m│");
                                System.out.println("│      \u001B[36m5. Add Expense      \u001B[33m\u001B[1m│");
                                System.out.println("│      \u001B[37m6. Add Tickets      \u001B[33m\u001B[1m│");
                                System.out.println("│      \u001B[31m7. Exit             \u001B[33m\u001B[1m│");
                                System.out.println("│                          │");
                                System.out.println("└──────────────────────────┘\u001B[0m");
                                System.out.print("Enter Your Choice: ");
                                try {
                                    eventChoice = input.nextInt();
                                    break;
                                } catch (InputMismatchException e) {
                                    System.out.println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                    input.nextLine();
                                    continue;
                                }
                            }
                            if (eventChoice == 1) {
                                while (true) {
                                    input.nextLine();
                                    double size = 0;
                                    double cost = 0;
                                    System.out.print("Enter The Booth Name: ");
                                    String boothName = input.nextLine();
                                    while (true) {
                                        try {
                                            System.out.print("Enter The Booth Size in Square Feet: ");
                                            size = input.nextDouble();
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out
                                                    .println(
                                                            "\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                            input.nextLine();
                                            continue;
                                        }
                                    }
                                    while (true) {
                                        try {
                                            System.out.print("Enter The Booth Cost: ");
                                            cost = input.nextDouble();
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out
                                                    .println(
                                                            "\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                            input.nextLine();
                                            continue;
                                        }
                                    }

                                    Booth booth = new Booth(boothName, size, cost);
                                    controller.events.get(eventNumber - 1).addBooth(booth);
                                    System.out.println("Booth Added Sucessfully :) ");
                                    break;
                                }
                            } else if (eventChoice == 2) {
                                input.nextLine();
                                double nextTime = 9;
                                String period = "AM";
                                DecimalFormat df = new DecimalFormat("0.00");

                                par: while (true) {
                                    System.out.print("Enter The Program : ");
                                    String session = input.nextLine().trim();

                                    while (true) {
                                        try {
                                            System.out.print("Enter Duration (hours): ");
                                            int totalHour = input.nextInt();
                                            input.nextLine();

                                            double endTime = nextTime + totalHour;
                                            String endPeriod = period;

                                            if (endTime >= 12) {
                                                if (endTime > 12)
                                                    endTime -= 12;
                                                endPeriod = period.equals("AM") ? "PM" : "AM";
                                            }

                                            String startFormatted = String.format("%.0f:00 %s", nextTime, period);
                                            String endFormatted = String.format("%.0f:00 %s", endTime, endPeriod);

                                            String timeRange = startFormatted + " to " + endFormatted;

                                            controller.events.get(eventNumber - 1).schedule
                                                    .add(new Session(session, timeRange));

                                            nextTime = endTime;
                                            period = endPeriod;

                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out.println(
                                                    "\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                            input.nextLine();
                                        }
                                    }

                                    while (true) {
                                        System.out.print("\nDo You Want To Add Extra Program(y/n): ");
                                        char yesOrNo = input.nextLine().toLowerCase().charAt(0);
                                        if (yesOrNo == 'y')
                                            continue par;
                                        else if (yesOrNo == 'n')
                                            break par;
                                        else
                                            System.out.println("\u001B[91mInvalid input\u001B[0m");
                                    }
                                }

                            } else if (eventChoice == 3) {
                                input.nextLine();
                                System.out.println(controller.displayAllSession(eventNumber - 1));
                                System.out.print("Enter the Session Number To Add Speaker: ");
                                try {
                                    int secNumber = input.nextInt();
                                    System.out.print("Enter The Speaker Name: ");
                                    input.nextLine();
                                    String speakerName = input.nextLine();
                                    System.out.print("Gender: ");
                                    String gender = input.nextLine();
                                    Speaker speaker = new Speaker(speakerName, gender);
                                    controller.events.get(eventNumber - 1).schedule.get(secNumber - 1)
                                            .assignSpeaker(speaker);
                                    controller.events.get(eventNumber - 1).speakers.add(speaker);

                                } catch (Exception e) {
                                    System.out.println("\n\u001B[91mInvalid input\u001B[0m\n");
                                }

                            } else if (eventChoice == 4) {
                                input.nextLine();
                                System.out.print("Enter The Company Name: ");
                                String companyName = input.nextLine();
                                double amountSponsor = 0;
                                while (true) {
                                    try {
                                        System.out.print("Enter The Amount To Sponsor: ");
                                        amountSponsor = input.nextInt();
                                        break;
                                    } catch (InputMismatchException e) {
                                        System.out
                                                .println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                        continue;
                                    }
                                }
                                controller.events.get(eventNumber - 1).sponsors
                                        .add(new Sponsor(companyName, amountSponsor));
                                System.out.println("Sponsor Added Sucessfully");
                            } else if (eventChoice == 5) {
                                input.nextLine();
                                System.out.print("Enter The Description: ");
                                String description = input.nextLine();
                                double amount = 0;
                                while (true) {
                                    try {
                                        System.out.print("Enter The Expenses : ");
                                        amount = input.nextInt();
                                        break;
                                    } catch (InputMismatchException e) {
                                        System.out
                                                .println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                        input.nextLine();
                                        continue;
                                    }
                                }
                                controller.events.get(eventNumber - 1).expenses.add(new Expense(description, amount));
                            } else if (eventChoice == 6) {

                                double rate = 0;
                                while (true) {
                                    try {
                                        System.out.print("Enter The Price Of The Ticket: ");
                                        rate = input.nextInt();
                                        break;
                                    } catch (InputMismatchException e) {
                                        System.out
                                                .println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                        input.nextLine();
                                        continue;
                                    }
                                }
                                for (int i = 0; i < controller.events.get(eventNumber - 1).venue.capacity; i++) {
                                    controller.events.get(eventNumber - 1).ticketsSold
                                            .add(new Ticket(controller.events.get(eventNumber - 1).name,
                                                    controller.events.get(eventNumber - 1).category, rate));
                                }

                                System.out.println(controller.events.get(eventNumber - 1).ticketsSold.size()
                                        + " Tickets Allocated Sucessfully :)");
                            } else {
                                break;
                            }
                        }
                    } else if (choice == 3) {
                        controller.saveEvents(controller.events);
                        break;
                    } else {
                        System.out.println("\u001B[91mInvalid input.\u001B[0m\n");
                    }
                }
            } else if (roleChoice == 2) {
                System.out.println("\n" + controller.displayAllEvents() + "\n");
                int userEventNumber = 0;
                while (true) {
                    try {
                        System.out.print("Enter The Event Number You want to Get In(Exit:0): ");
                        userEventNumber = input.nextInt();
                        if (userEventNumber == 0)
                            continue start;
                        controller.events.get(userEventNumber - 1);
                        break;
                    } catch (InputMismatchException e) {
                        System.out
                                .println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                        input.nextLine();
                    } catch (IndexOutOfBoundsException e) {
                        System.out
                                .println("\u001B[91mInvalid Event\u001B[0m\n");
                        input.nextLine();
                    }
                }

                int userChoice = 0;
                while (true) {
                    System.out.println("\u001B[33m\u001B[1m┌──────────────────────────────┐");
                    System.out.println("│                              │");
                    System.out.println("│      \u001B[36m1. View Session         \u001B[33m\u001B[1m│");
                    System.out.println("│      \u001B[32m2. View Speaker         \u001B[33m\u001B[1m│");
                    System.out.println("│      \u001B[34m3. View Sponsor         \u001B[33m\u001B[1m│");
                    System.out.println("│      \u001B[35m4. Purchase Ticket      \u001B[33m\u001B[1m│");
                    System.out.println("│      \u001B[31m5. Logout               \u001B[33m\u001B[1m│");
                    System.out.println("│                              │");
                    System.out.println("└──────────────────────────────┘\u001B[0m");
                    try {
                        System.out.print("Enter Your Choice: ");
                        userChoice = input.nextInt();
                    } catch (InputMismatchException e) {
                        System.out
                                .println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                        input.nextLine();
                    }

                    if (userChoice == 1) {
                        System.out.println("SESSION\n");
                        System.out.println(controller.displayAllSession(userEventNumber - 1));
                    } else if (userChoice == 2) {
                        System.out.println("ALL SPEAKER\n");
                        System.out.println(controller.displayAllSpeakers(userEventNumber - 1));
                    } else if (userChoice == 3) {
                        System.out.println("ALL SPONSORS\n");
                        System.out.println(controller.displayAllSponsor(userEventNumber - 1));
                    } else if (userChoice == 4) {
                        // User currentUser = controller.users.get();

                        Event selectedEvent = controller.events.get(userEventNumber - 1);

                        if (selectedEvent.ticketsSold.isEmpty()) {
                            System.out.println("\u001B[91mSorry! No Tickets Available\u001B[0m");
                        } else {
                            Payment payment = new Payment(
                                    controller.events.get(userEventNumber - 1).ticketsSold.get(0).price);
                                    System.out.println("Enter The Payment Amount: ");
                            payment.processPayment();
                            Ticket purchasedTicket = selectedEvent.ticketsSold
                                    .remove(selectedEvent.ticketsSold.size() - 1);
                            controller.currentUser.addTicket(purchasedTicket);
                            System.out.println("\u001B[32mTicket purchased successfully!\u001B[0m");
                            System.out.println("Ticket Details: " + purchasedTicket);
                            System.out.println("Tickets Avaliable: " + selectedEvent.ticketsSold.size());
                        }
                    } else if (userChoice == 5) {
                        break;
                    }
                }

            } else {
                System.out.println("\u001B[91mInvalid Choice\u001B[0m\n");
            }
        }

    }

    String displayAllSpeakers(int eventNumber) {
        StringBuilder result = new StringBuilder();

        result.append("┌──────┬────────────────────────────────┬────────────┐\n");
        result.append("│ S.No │ Name                           │ Gender     │\n");
        result.append("├──────┼────────────────────────────────┼────────────┤\n");

        ArrayList<Speaker> speakers = events.get(eventNumber).speakers;

        for (int i = 0; i < speakers.size(); i++) {
            Speaker s = speakers.get(i);

            String name = s.name;
            String gender = s.gender;

            result.append(String.format("│ %-5d│ %-30s │ %-10s │\n", i + 1, name, gender));

            if (i != speakers.size() - 1) {
                result.append("├──────┼────────────────────────────────┼────────────┤\n");
            }
        }

        result.append("└──────┴────────────────────────────────┴────────────┘\n");

        return result.toString();
    }

    String displayAllSession(int eventNumber) {

        System.out.println(
                "\u001B[35m                         ███████╗ ███████╗ ███████╗ ███████╗ ██╗  ██████╗  ███╗   ██╗\n                         ██╔════╝ ██╔════╝ ██╔════╝ ██╔════╝ ██║ ██╔═══██╗ ████╗  ██║\n                         ███████╗ █████╗   ███████╗ ███████╗ ██║ ██║   ██║ ██╔██╗ ██║\n                         ╚════██║ ██╔══╝   ╚════██║ ╚════██║ ██║ ██║   ██║ ██║╚██╗██║\n                         ███████║ ███████╗ ███████║ ███████║ ██║ ╚██████╔╝ ██║ ╚████║\n                         ╚══════╝ ╚══════╝ ╚══════╝ ╚══════╝ ╚═╝  ╚═════╝  ╚═╝  ╚═══╝\n                         \u001B[0m");

        StringBuilder result = new StringBuilder();

        result.append(
                "\u001B[97m┌──────────────────────┬───────────────────────────────────────────────────────────┬──────────────────────┐\n");
        result.append(
                "│ Time                 │ Sessions scheduled                                        │ Speaker              │\n");
        result.append(
                "├──────────────────────┼───────────────────────────────────────────────────────────┼──────────────────────┤\n");
        int i = 0;
        for (Session s : events.get(eventNumber).schedule) {
            String time = s.timeRange;
            String action = s.topic;
            String owner = (s.speaker != null)
                    ? s.speaker.name + " (" + s.speaker.gender + ")"
                    : "Not Added Yet";

            result.append(String.format("│ %-20s │ %-57s │ %-20s │\n", time, action, owner));
            if (!(i++ == events.get(eventNumber).schedule.size() - 1))
                result.append(
                        "├──────────────────────┼───────────────────────────────────────────────────────────┼──────────────────────┤\n");
        }
        result.append(
                "└──────────────────────┴───────────────────────────────────────────────────────────┴──────────────────────┘\u001B[0m");
        return result.toString();
    }

    String displayAllSponsor(int eventNumber) {
        StringBuilder result = new StringBuilder();

        result.append("┌──────┬───────────────────────────┬─────────────┐\n");
        result.append("│ S.No │ Company Name              │ Amount      │\n");
        result.append("├──────┼───────────────────────────┼─────────────┤\n");

        ArrayList<Sponsor> sponsors = events.get(eventNumber).sponsors;

        for (int i = 0; i < sponsors.size(); i++) {
            Sponsor s = sponsors.get(i);

            String company = s.companyName;
            String amount = String.format("%.2f", s.amountSponsor);

            result.append(String.format("│ %-5d│ %-25s │ %-11s │\n", i + 1, company, amount));

            if (i != sponsors.size() - 1) {
                result.append("├──────┼───────────────────────────┼─────────────┤\n");
            }
        }

        result.append("└──────┴───────────────────────────┴─────────────┘\n");

        return result.toString();
    }

    void addEvents(Event e) {
        e.num = events.size() + 1;
        events.add(e);
    }

    void saveEvents(ArrayList<Event> events2) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(events2);
        } catch (Exception i) {

        }
    }

    @SuppressWarnings("unchecked")
    ArrayList<Event> readEvents() {
        ArrayList<Event> list = new ArrayList<>();
        if (!path.exists()) {
            return list;
        }

        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(path))) {
            list = (ArrayList<Event>) oos.readObject();
        } catch (Exception e) {
        }
        return list;
    }

    String displayAllEvents() {
        String result = "\033[33m███████╗ ██╗ ██╗ ███████╗ ███╗   ██╗ ████████╗ ███████╗\n██╔════╝ ██║ ██║ ██╔════╝ ████╗  ██║ ╚══██╔══╝ ██╔════╝\n█████╗   ██║ ██║ █████╗   ██╔██╗ ██║    ██║    ███████╗\n██╔══╝   ██║ ██║ ██╔══╝   ██║╚██╗██║    ██║    ╚════██║\n███████╗ ╚████╔╝ ███████╗ ██║ ╚████║    ██║    ███████║\n╚══════╝  ╚═══╝  ╚══════╝ ╚═╝ ╚═══╝     ╚═╝    ╚══════╝\n\n";
        for (Event e : events) {
            result += e.displayEvent() + "\n\n";
        }
        return result;
    }

    void writeUser(User u) {
        try {
            FileWriter file = new FileWriter(data, true);
            file.write(u.name + "|" + u.email.toLowerCase() + "|" + u.password + "|" + u.role + "\n");
            file.flush();
        } catch (Exception e) {

        }
    }

    void readUser() {
        try {
            FileReader fr = new FileReader(data);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {
                String arr[] = line.split("\\|");
                User user = new User(arr[0], arr[1], arr[2], arr[3]);
                users.add(user);
                line = br.readLine();
            }
        } catch (Exception e) {
        }
    }

}
