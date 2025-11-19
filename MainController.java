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
    ArrayList<Organizer> organizer = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();
    int userIndex;
    int orgIndex;
    File path = new File("text.csv");
    File data = new File("data.csv");

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        MainController controller = new MainController();
        controller.organizer = controller.readEvents();
        // controller.organizer.add(org);
        controller.readUser();

        String line = "╭───────────────────────────────╮";
        String bottom = "                                               ╰───────────────────────────────╯";
        start: while (true) {
            LocalDate dateInput;
            int roleChoice = 0;
            int userOrOrg = 0;
            System.out.println("\n");
            System.out.println(
                    "                                                     \u001B[33m\u001B[1m╭───────────────╮");
            System.out.println("                                                     │               │");
            System.out.println(
                    "                                                     │  \u001B[36m1. Sign In   \u001B[33m\u001B[1m│");
            System.out.println(
                    "                                                     │  \u001B[32m2. Sign Up   \u001B[33m\u001B[1m│");
            System.out.println(
                    "                                                     │  \u001B[31m3. Exit      \u001B[33m\u001B[1m│");
            System.out.println("                                                     │               │");
            System.out.println("                                                     ╰───────────────╯\u001B[0m");
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
                controller.organizer = controller.readEvents();
                System.out.print("Enter Your Email: ");
                String email = input.nextLine();

                boolean isValidUser = false;
                // boolean isEmailExist = false;
                for (User u : controller.users) {
                    if (u.email.equalsIgnoreCase(email.toLowerCase())) {
                        // isEmailExist = true;
                        System.out.print("Enter Your Password: ");
                        char c[] = System.console().readPassword();
                        String pas = "";
                        for (char ch : c) {
                            pas += (ch + 1);
                        }
                        String password = new String(pas);
                        if (u.password.equals(password)) {
                            isValidUser = true;
                            if (u.role.equals("user")) {
                                roleChoice = 2;
                                controller.userIndex = controller.users.indexOf(u);
                            } else if (u.role.equals("organizer")) {
                                boolean found = false;
                                for (Organizer Org : controller.organizer) {
                                    if (Org.email.equalsIgnoreCase(u.email)) {
                                        controller.orgIndex = controller.organizer.indexOf(Org);
                                        found = true;
                                        break;
                                    }
                                }

                                if (!found) {
                                    Organizer org = new Organizer(u.name, u.email, u.password);
                                    controller.organizer.add(org);
                                    controller.orgIndex = controller.organizer.indexOf(org);
                                }

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
                String name = "";
                String email = "";
                String password = "";
                while (true) {
                    System.out.print("Enter Your Name: ");
                    name = input.nextLine();
                    if (controller.isValidName(name)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter Your Email ID: ");
                    email = input.nextLine();
                    for (User u : controller.users) {
                        if (u.email.equalsIgnoreCase(email)) {
                            System.out.println("\u001B[31m╭─────────────────────────────────────╮\n" +
                                    "│ Email Already Exist. Please Sign In │\n" +
                                    "╰─────────────────────────────────────╯\u001B[0m");
                            continue start;
                        }
                    }
                    if (controller.isValidEmail(email)) {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Enter The Password: ");
                    password = input.nextLine();
                    if (controller.isValidPassword(password)) {
                        break;
                    }
                }
                String role = "";
                while (true) {
                    System.out.print("Enter The Role (user(1) or organizer(2)) : ");
                    try {
                        roleChoice = input.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                        input.nextLine();
                        continue;
                    }
                    if (roleChoice == 1)
                        role = "user";
                    else if (roleChoice == 2)
                        role = "organizer";
                    if (role.equals("user") || role.equals("organizer")) {
                        break;
                    } else {
                        System.out.println("\u001B[91mInvalid Role\u001B[0m\n");
                    }
                }

                // if (role.equals("organizer"))
                // roleChoice = 1;
                // else if (role.equals("user"))
                // roleChoice = 2;
                String pass = "";
                for (char ch : password.toCharArray()) {
                    pass += (ch + 1);
                }
                User user = new User(name, email, pass, role);
                controller.users.add(user);
                System.out.println(
                        "\n\u001B[92m\u001B[1m" +
                                "╭────────────────────────────────────────────╮\n" +
                                "│        Account Created Successfully!       │\n" +
                                "╰────────────────────────────────────────────╯" +
                                "\u001B[0m");
                controller.writeUser(user);
                continue;
            } else if (userOrOrg == 3) {
                System.out.println(
                        "\u001B[92m╭───────────────────────────────╮\n" +
                                "│            Thank You          │\n" +
                                "╰───────────────────────────────╯\u001B[0m");
                break;
            }

            if (roleChoice == 1) {
                int choice = 0;
                eve: while (true) {
                    try {
                        System.out.println(
                                "                                               \u001B[33m\u001B[1m" +
                                        line + "\n" +
                                        "                                               │                               │\n"
                                        +
                                        "                                               │        1. View Event          │\n"
                                        +
                                        "                                               │        2. Add Event           │\n"
                                        +
                                        "                                               │        3. Modify Event        │\n"
                                        +
                                        "                                               │        4. Delete Event        │\n"
                                        +
                                        "                                               │        5. Logout              │\n"
                                        +
                                        "                                               │                               │\n"
                                        +
                                        bottom +
                                        "\u001B[0m");
                        System.out.print("Enter Your Choice: ");
                        choice = input.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                        input.nextLine();
                    }
                    if (choice == 1) {
                        System.out.println(controller.displayAllEvents());
                    } else if (choice == 2) {
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
                                System.out.print("Enter The People Count: ");
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

                        // controller.fileWriter("events",
                        // controller.organizer.get(controller.orgIndex).events);

                        System.out.println(
                                "\u001B[92m\u001B[1m" +
                                        "╭────────────────────────────────────╮\n" +
                                        "│      Event Added Successfully :)   │\n" +
                                        "╰────────────────────────────────────╯" +
                                        "\u001B[0m");
                    } else if (choice == 3) {
                        System.out.println(controller.displayAllEvents());
                        if (controller.organizer.get(controller.orgIndex).events.isEmpty()) {
                            continue;
                        }
                        int eventNumber = 0;
                        while (true) {
                            try {
                                System.out.print("Enter The Event Number To Modify (Back:0): ");
                                eventNumber = input.nextInt();
                                if (eventNumber == 0) {
                                    continue eve;
                                }
                                controller.organizer.get(controller.orgIndex).events.get(eventNumber - 1);
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
                                System.out.println(
                                        "\u001B[33m\u001B[1m                                               ╭──────────────────────────╮");
                                System.out.println(
                                        "                                               │                          │");
                                System.out.println(
                                        "                                               │      \u001B[38m1. Booth            \u001B[33m\u001B[1m│");
                                System.out.println(
                                        "                                               │      \u001B[32m2. Session          \u001B[33m\u001B[1m│");
                                System.out.println(
                                        "                                               │      \u001B[34m3. Speaker          \u001B[33m\u001B[1m│");
                                System.out.println(
                                        "                                               │      \u001B[35m4. Sponsor          \u001B[33m\u001B[1m│");
                                System.out.println(
                                        "                                               │      \u001B[36m5. Expense          \u001B[33m\u001B[1m│");
                                System.out.println(
                                        "                                               │      \u001B[37m6. Tickets          \u001B[33m\u001B[1m│");
                                System.out.println(
                                        "                                               │      \u001B[31m7. Back             \u001B[33m\u001B[1m│");
                                System.out.println(
                                        "                                               │                          │");
                                System.out.println(
                                        "                                               ╰──────────────────────────╯\u001B[0m");
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
                                    System.out.println(
                                            "\u001B[33m\u001B[1m                                               ╭──────────────────────────╮");
                                    System.out.println(
                                            "                                               │                          │");
                                    System.out.println(
                                            "                                               │      \u001B[38m1. View Booth       \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[32m2. Add Booth        \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[34m3. Update Booth     \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[35m4. Delete Booth     \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[31m5. Back             \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │                          │");
                                    System.out.println(
                                            "                                               ╰──────────────────────────╯\u001B[0m");
                                    int boothChoice = 0;
                                    while (true) {
                                        try {
                                            System.out.print("Enter Your Choice: ");
                                            boothChoice = input.nextInt();
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out
                                                    .println(
                                                            "\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                            input.nextLine();
                                        }
                                    }
                                    if (boothChoice == 1) {
                                        System.out.println(controller.displayAllBooths(eventNumber - 1));
                                    } else if (boothChoice == 2) {
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
                                                    if (size < 1) {
                                                        System.out.println("\u001B[91mInvalid input.\u001B[0m");
                                                        continue;
                                                    }
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
                                                    if (cost < 1) {
                                                        System.out.println("\u001B[91mInvalid input.\u001B[0m");
                                                        continue;
                                                    }
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
                                            controller.organizer.get(controller.orgIndex).events.get(eventNumber - 1)
                                                    .addBooth(booth);
                                            System.out.println(
                                                    "\u001B[92m\u001B[1m" +
                                                            "╭──────────────────────────────────╮\n" +
                                                            "│      Booth Added Successfully :) │\n" +
                                                            "╰──────────────────────────────────╯" +
                                                            "\u001B[0m");
                                            break;
                                        }
                                    } else if (boothChoice == 3) {
                                        ArrayList<Booth> booths = controller.organizer.get(controller.orgIndex).events
                                                .get(eventNumber - 1).booths;

                                        if (booths == null || booths.isEmpty()) {
                                            System.out.println("\u001B[91mNo booths available to update.\u001B[0m");
                                            continue;
                                        }

                                        System.out.println(controller.displayAllBooths(eventNumber - 1));
                                        int boothNum = 0;
                                        while (true) {
                                            try {
                                                System.out.print("\nEnter the booth number to update (Back: 0): ");
                                                boothNum = input.nextInt();
                                                if (boothNum == 0)
                                                    break;
                                                if (boothNum < 1
                                                        || boothNum > booths.size()) {
                                                    System.out.println("\u001B[91mInvalid booth number.\u001B[0m");
                                                    continue;
                                                }
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println(
                                                        "\u001B[91mInvalid input. Please enter a number.\u001B[0m\n");
                                                input.nextLine();
                                            }

                                        }
                                        if (boothNum == 0) {
                                            continue;
                                        }
                                        Booth boothToUpdate = booths.get(boothNum - 1);
                                        input.nextLine();

                                        System.out.println("\nUpdating Booth: " + boothToUpdate.name);
                                        System.out.println("Leave field blank to keep current value.");

                                        System.out.print("Enter new name (" + boothToUpdate.name + "): ");
                                        String newName = input.nextLine();
                                        if (!newName.trim().isEmpty()) {
                                            boothToUpdate.name = newName;
                                        }

                                        while (true) {
                                            System.out.print("Enter new size in sq.ft (" + boothToUpdate.size + "): ");
                                            String sizeStr = input.nextLine();
                                            if (sizeStr.trim().isEmpty())
                                                break;
                                            try {
                                                double newSize = Double.parseDouble(sizeStr);
                                                if (newSize < 1) {
                                                    System.out.println("\u001B[91mInvalid input.\u001B[0m");
                                                    continue;
                                                }
                                                boothToUpdate.size = newSize;
                                                break;
                                            } catch (NumberFormatException e) {
                                                System.out.println(
                                                        "\u001B[91mInvalid input. Please enter a number.\u001B[0m\n");
                                            }
                                        }

                                        while (true) {
                                            System.out.print("Enter new cost (" + boothToUpdate.price + "): ");
                                            String costStr = input.nextLine();
                                            if (costStr.trim().isEmpty())
                                                break;
                                            try {
                                                double newCost = Double.parseDouble(costStr);
                                                if (newCost < 1) {
                                                    System.out.println("\u001B[91mInvalid input.\u001B[0m");
                                                    continue;
                                                }
                                                boothToUpdate.price = newCost;
                                                break;
                                            } catch (NumberFormatException e) {
                                                System.out.println(
                                                        "\u001B[91mInvalid input. Please enter a number.\u001B[0m\n");
                                            }
                                        }

                                        System.out.println(
                                                "\u001B[92m\u001B[1m" +
                                                        "╭──────────────────────────────────╮\n" +
                                                        "│     Booth Updated Successfully!  │\n" +
                                                        "╰──────────────────────────────────╯" +
                                                        "\u001B[0m");
                                    } else if (boothChoice == 4) {
                                        ArrayList<Booth> booths = controller.organizer
                                                .get(controller.orgIndex).events
                                                .get(eventNumber - 1).booths;

                                        if (booths == null || booths.isEmpty()) {
                                            System.out.println("\u001B[91mNo booths available to delete.\u001B[0m");
                                            continue;
                                        }

                                        System.out.println(controller.displayAllBooths(eventNumber - 1));

                                        int boothNum = 0;
                                        while (true) {
                                            try {
                                                System.out.print("Enter the booth number to delete (Back: 0): ");
                                                boothNum = input.nextInt();
                                                if (boothNum == 0)
                                                    break;
                                                if (boothNum < 1 || boothNum > booths.size()) {
                                                    System.out.println("\u001B[91mInvalid booth number.\u001B[0m");
                                                    continue;
                                                }
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println(
                                                        "\u001B[91mInvalid input. Please enter a number.\u001B[0m\n");
                                                input.nextLine();
                                            }
                                        }

                                        if (boothNum == 0) {
                                            continue;
                                        }

                                        input.nextLine();
                                        Booth removedBooth = booths.get(boothNum - 1);
                                        System.out.print(
                                                "Are you sure you want to delete '" + removedBooth.name + "'? (y/n): ");
                                        String confirm = input.nextLine().trim().toLowerCase();

                                        if (confirm.equals("y") || confirm.equals("yes")) {
                                            booths.remove(boothNum - 1);
                                            System.out.println(
                                                    "\u001B[91m\u001B[1m" +
                                                            "╭──────────────────────────────────────────────╮\n" +
                                                            "│   Booth '" + removedBooth.name
                                                            + "' deleted successfully!\n" +
                                                            "╰──────────────────────────────────────────────╯" +
                                                            "\u001B[0m");
                                        } else {
                                            System.out.println(" Deletion cancelled.");
                                        }
                                    } else if (boothChoice == 5) {
                                        break;
                                    } else {
                                        System.out.println("\u001B[91mInvalid input\u001B[0m");
                                    }
                                }
                            } else if (eventChoice == 2) {
                                while (true) {
                                    System.out.println(
                                            "\u001B[33m\u001B[1m                                               ╭──────────────────────────╮");
                                    System.out.println(
                                            "                                               │                          │");
                                    System.out.println(
                                            "                                               │      \u001B[38m1. View Session     \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[32m2. Add Session      \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[34m3. Update Session   \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[35m4. Delete Session   \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[31m5. Back             \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │                          │");
                                    System.out.println(
                                            "                                               ╰──────────────────────────╯\u001B[0m");
                                    int sessionChoice = 0;
                                    while (true) {
                                        try {
                                            System.out.print("Enter Your Choice: ");
                                            sessionChoice = input.nextInt();
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out
                                                    .println(
                                                            "\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                            input.nextLine();
                                        }
                                    }
                                    if (sessionChoice == 1) {
                                        System.out.println(controller.displayAllSession(eventNumber - 1));
                                    } else if (sessionChoice == 2) {
                                        input.nextLine();

                                        ArrayList<Session> existingSessions = controller.organizer
                                                .get(controller.orgIndex).events
                                                .get(eventNumber - 1).schedule;

                                        double nextTime = 9;
                                        String period = "AM";

                                        if (existingSessions != null && !existingSessions.isEmpty()) {
                                            Session lastSession = existingSessions.get(existingSessions.size() - 1);

                                            String lastTimeRange = lastSession.timeRange;

                                            String endTimeStr = lastTimeRange.split(" to ")[1];

                                            String[] parts = endTimeStr.trim().split(" ");
                                            nextTime = Double.parseDouble(parts[0].split(":")[0]);
                                            period = parts[1].toUpperCase();
                                        }

                                        DecimalFormat df = new DecimalFormat("0.00");
                                        par: while (true) {
                                            System.out.print("Enter The Program : ");
                                            String session = input.nextLine().trim();

                                            while (true) {
                                                try {
                                                    System.out.print("Enter Duration (hours)(Max:3 hr): ");
                                                    int totalHour = input.nextInt();
                                                    if (totalHour > 3 || totalHour < 1) {
                                                        System.out.println(
                                                                "\u001B[91mInvalid input. Please enter a Correct Time \u001B[0m\n");
                                                        continue;
                                                    }
                                                    input.nextLine();

                                                    double endTime = nextTime + totalHour;
                                                    String endPeriod = period;

                                                    if (endTime >= 12) {
                                                        if (endTime > 12)
                                                            endTime -= 12;
                                                        endPeriod = period.equals("AM") ? "PM" : "AM";
                                                    }

                                                    String startFormatted = String.format("%.0f:00 %s", nextTime,
                                                            period);
                                                    String endFormatted = String.format("%.0f:00 %s", endTime,
                                                            endPeriod);

                                                    String timeRange = startFormatted + " to " + endFormatted;

                                                    controller.organizer.get(controller.orgIndex).events
                                                            .get(eventNumber - 1).schedule
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
                                                else if (yesOrNo == 'n') {
                                                    System.out.println(
                                                            "\u001B[92m\u001B[1m" +
                                                                    "╭────────────────────────────────────╮\n" +
                                                                    "│      Sessions Added Successfully   │\n" +
                                                                    "╰────────────────────────────────────╯" +
                                                                    "\u001B[0m");
                                                    break par;
                                                } else
                                                    System.out.println("\u001B[91mInvalid input\u001B[0m");
                                            }
                                        }
                                    } else if (sessionChoice == 3) {
                                        ArrayList<Session> sessions = controller.organizer
                                                .get(controller.orgIndex).events.get(eventNumber - 1).schedule;

                                        if (sessions.isEmpty()) {
                                            System.out.println(
                                                    "\u001B[91m╭────────────────────────────────────╮\n" +
                                                            "│No sessions available│\n" +
                                                            "╰────────────────────────────────────╯\u001B[0m\n");
                                            continue;
                                        }

                                        System.out.println(controller.displayAllSession(eventNumber - 1));
                                        int updateIndex = 0;

                                        while (true) {
                                            try {
                                                System.out.print("Enter the Session Number to Update (Back:0): ");
                                                updateIndex = input.nextInt();
                                                input.nextLine(); // Consume newline
                                                if (updateIndex == 0)
                                                    break;
                                                if (updateIndex < 1 || updateIndex > sessions.size()) {
                                                    System.out.println("\u001B[91m╭────────────────────────────╮\n" +
                                                            "│Invalid session number│\n" +
                                                            "╰────────────────────────────╯\u001B[0m\n");
                                                    continue;
                                                }
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out
                                                        .println("\u001B[91m╭────────────────────────────────────╮\n" +
                                                                "│ Invalid input. Please enter a number │\n" +
                                                                "╰────────────────────────────────────╯\u001B[0m\n");
                                                input.nextLine();
                                            }
                                        }

                                        if (updateIndex == 0)
                                            continue;

                                        Session selectedSession = sessions.get(updateIndex - 1);

                                        int updateChoice = 0;
                                        while (true) {
                                            try {
                                                System.out.println("\nWhat do you want to update?");
                                                System.out.println("1. Topic");
                                                System.out.println("2. Time Range");
                                                System.out.println("3. Both");
                                                System.out.print("Enter your choice: ");
                                                updateChoice = input.nextInt();
                                                input.nextLine(); // Consume newline
                                                if (updateChoice < 1 || updateChoice > 3) {
                                                    System.out.println(
                                                            "\u001B[91mInvalid choice. Enter 1, 2, or 3.\u001B[0m");
                                                    continue;
                                                }
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println(
                                                        "\u001B[91mInvalid input. Please enter a number.\u001B[0m");
                                                input.nextLine();
                                            }
                                        }

                                        if (updateChoice == 1 || updateChoice == 3) {
                                            System.out.print("Enter new topic name: ");
                                            String newTopic = input.nextLine().trim();
                                            if (!newTopic.isEmpty()) {
                                                selectedSession.topic = newTopic;
                                            } else {
                                                System.out.println("Topic name not changed.");
                                            }
                                        }

                                        if (updateChoice == 2 || updateChoice == 3) {
                                            double eventStart = 9.0;
                                            double eventEnd = 18.0;
                                            while (true) {
                                                try {
                                                    System.out
                                                            .print("Enter new start time (e.g., 9:00 AM or 1:00 PM): ");
                                                    String startTimeStr = input.nextLine();
                                                    System.out
                                                            .print("Enter new end time (e.g., 11:00 AM or 3:00 PM): ");
                                                    String endTimeStr = input.nextLine();

                                                    double start = parseTime(startTimeStr);
                                                    double end = parseTime(endTimeStr);

                                                    if (start < eventStart || end > eventEnd) {
                                                        System.out.println(
                                                                "\u001B[91m╭────────────────────────────────────────────╮\n"
                                                                        +
                                                                        "│ Time must be within event hours (9 AM - 6 PM) │\n"
                                                                        +
                                                                        "╰────────────────────────────────────────────╯\u001B[0m");
                                                        continue;
                                                    }

                                                    if (end <= start) {
                                                        System.out.println(
                                                                "\u001B[91m╭────────────────────────────────────────────╮\n"
                                                                        +
                                                                        "│ End time must be after start time!│\n"
                                                                        +
                                                                        "╰────────────────────────────────────────────╯\u001B[0m");

                                                    }

                                                    boolean conflict = false;
                                                    for (Session s : sessions) {
                                                        if (s != selectedSession) {
                                                            String[] times = s.timeRange.split(" to ");
                                                            double existingStart = parseTime(times[0]);
                                                            double existingEnd = parseTime(times[1]);

                                                            if (start < existingEnd && end > existingStart) {
                                                                conflict = true;
                                                                break;
                                                            }
                                                        }
                                                    }

                                                    if (conflict) {
                                                        System.out.println(
                                                                "\u001B[91m╭────────────────────────────────────────────╮\n"
                                                                        +
                                                                        "│ Time overlaps with another session. Try again! │\n"
                                                                        +
                                                                        "╰────────────────────────────────────────────╯\u001B[0m");
                                                        continue;
                                                    }

                                                    String startFormatted = formatTime(start);

                                                    String endFormatted = formatTime(end);
                                                    selectedSession.timeRange = startFormatted + " to " + endFormatted;

                                                    System.out.println(
                                                            "\u001B[92m╭────────────────────────────────────────╮\n" +
                                                                    "│   Session Updated Successfully!s   │\n" +
                                                                    "╰────────────────────────────────────────╯\u001B[0m");
                                                    break;

                                                } catch (Exception e) {
                                                    System.out.println(
                                                            "\u001B[91m╭────────────────────────────────────╮\n" +
                                                                    "│ Invalid input. Please enter a valid time │\n" +
                                                                    "╰────────────────────────────────────╯\u001B[0m");

                                                }
                                            }
                                        }
                                    } else if (sessionChoice == 4) {

                                        ArrayList<Session> sessions = controller.organizer
                                                .get(controller.orgIndex).events.get(eventNumber - 1).schedule;

                                        if (sessions == null || sessions.isEmpty()) {
                                            System.out.println("\u001B[91mNo sessions available to delete!\u001B[0m");
                                            continue;
                                        }

                                        System.out.println(controller.displayAllSession(eventNumber - 1));

                                        int deleteIndex = -1;

                                        while (true) {
                                            try {
                                                System.out.print("Enter S.No of the session to delete: ");
                                                deleteIndex = input.nextInt() - 1;
                                                input.nextLine();

                                                if (deleteIndex < 0 || deleteIndex >= sessions.size()) {
                                                    System.out.println(
                                                            "\u001B[91mInvalid S.No. Please enter a valid number.\u001B[0m");
                                                    continue;
                                                }

                                                Session selected = sessions.get(deleteIndex);

                                                String confirm = "";
                                                while (true) {
                                                    System.out.print("Are you sure you want to delete \""
                                                            + selected.topic + "\"? (y/n): ");
                                                    confirm = input.nextLine().trim().toLowerCase();

                                                    if (confirm.equals("y") || confirm.equals("n"))
                                                        break;

                                                    System.out
                                                            .println("\u001B[91mInvalid input. Enter y or n.\u001B[0m");
                                                }

                                                if (confirm.equals("n")) {
                                                    System.out.println("\u001B[93mDeletion canceled.\u001B[0m");
                                                    break;
                                                }

                                                Session removed = sessions.remove(deleteIndex);

                                                System.out.println(
                                                        "\u001B[92m\u001B[1m" +
                                                                "╭──────────────────────────────────────────────╮\n" +
                                                                "│  Session \"" + removed.topic
                                                                + "\" deleted successfully!  \n" +
                                                                "╰──────────────────────────────────────────────╯" +
                                                                "\u001B[0m");

                                                double nextTime = 9;
                                                String period = "AM";

                                                for (Session s : sessions) {
                                                    String[] times = s.timeRange.split(" to ");
                                                    double startHour = Double
                                                            .parseDouble(times[0].split(":")[0].trim());
                                                    double endHour = Double.parseDouble(times[1].split(":")[0].trim());
                                                    double duration = endHour - startHour;

                                                    if (duration <= 0)
                                                        duration += 12;

                                                    double endTime = nextTime + duration;
                                                    String endPeriod = period;

                                                    if (endTime >= 12) {
                                                        if (endTime > 12)
                                                            endTime -= 12;
                                                        endPeriod = period.equals("AM") ? "PM" : "AM";
                                                    }

                                                    s.timeRange = String.format("%.0f:00 %s to %.0f:00 %s", nextTime,
                                                            period, endTime, endPeriod);

                                                    nextTime = endTime;
                                                    period = endPeriod;
                                                }

                                                break;

                                            } catch (InputMismatchException e) {
                                                System.out.println(
                                                        "\u001B[91mInvalid input. Please enter a number.\u001B[0m");
                                                input.nextLine();
                                            }
                                        }
                                    } else if (sessionChoice == 5) {
                                        break;
                                    } else {
                                        System.out.println(
                                                "\u001B[91mInvalid input \u001B[0m");
                                    }

                                }
                            } else if (eventChoice == 3) {
                                while (true) {
                                    System.out.println(
                                            "\u001B[33m\u001B[1m                                               ╭──────────────────────────╮");
                                    System.out.println(
                                            "                                               │                          │");
                                    System.out.println(
                                            "                                               │      \u001B[38m1. View Speaker     \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[32m2. Add Speaker      \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[34m3. Update Speaker   \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[35m4. Delete Speaker   \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[31m5. Back             \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │                          │");
                                    System.out.println(
                                            "                                               ╰──────────────────────────╯\u001B[0m");
                                    int speakerChoice = 0;
                                    while (true) {
                                        try {
                                            System.out.print("Enter Your Choice: ");
                                            speakerChoice = input.nextInt();
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out
                                                    .println(
                                                            "\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                            input.nextLine();
                                        }
                                    }
                                    ArrayList<Session> sess = controller.organizer
                                            .get(controller.orgIndex).events
                                            .get(eventNumber - 1).schedule;
                                    if (speakerChoice == 1) {
                                        System.out.println(controller.displayAllSpeakers(eventNumber - 1));
                                    } else if (speakerChoice == 2) {

                                        if (sess == null || sess.isEmpty()) {
                                            System.out.println(
                                                    "\u001B[91mNo sessions available to Add Speakers!\u001B[0m");
                                            continue;
                                        }

                                        input.nextLine();
                                        int secNumber = 0;
                                        System.out.println(controller.displayAllSession(eventNumber - 1));

                                        while (true) {
                                            try {
                                                System.out.print("Enter the Session Number To Add Speaker: ");
                                                secNumber = input.nextInt();

                                                int sessionCount = controller.organizer.get(controller.orgIndex).events
                                                        .get(eventNumber - 1).schedule.size();

                                                if (secNumber >= 1 && secNumber <= sessionCount) {
                                                    break;
                                                } else {
                                                    System.out.println("\u001B[91mInvalid input.\u001B[0m\n");
                                                }
                                            } catch (InputMismatchException e) {
                                                System.out.println(
                                                        "\u001B[91mInvalid input. Please enter a number.\u001B[0m\n");
                                                input.nextLine();
                                            }
                                        }

                                        while (true) {

                                            input.nextLine();
                                            System.out.print("Enter The Speaker Name: ");
                                            String speakerName = input.nextLine();
                                            String gender = "";

                                            while (true) {
                                                try {
                                                    System.out.print("Gender(Male(1) or Female(2)): ");
                                                    int genderChoice = input.nextInt();

                                                    if (genderChoice == 1) {
                                                        gender = "Male";
                                                        break;
                                                    } else if (genderChoice == 2) {
                                                        gender = "Female";
                                                        break;
                                                    } else {
                                                        System.out.println("\u001B[91mInvalid input.\u001B[0m\n");
                                                        input.nextLine();
                                                        continue;
                                                    }
                                                } catch (Exception e) {
                                                    System.out.println(
                                                            "\u001B[91mInvalid input. Please enter a number.\u001B[0m\n");
                                                    input.nextLine();
                                                    continue;
                                                }
                                            }

                                            Speaker speaker = new Speaker(speakerName, gender);

                                            controller.organizer.get(controller.orgIndex).events
                                                    .get(eventNumber - 1).schedule
                                                    .get(secNumber - 1)
                                                    .assignSpeaker(speaker);

                                            if (!controller.organizer.get(controller.orgIndex).events
                                                    .get(eventNumber - 1).speakers.contains(speaker)) {

                                                controller.organizer.get(controller.orgIndex).events
                                                        .get(eventNumber - 1).speakers.add(speaker);

                                                System.out.println(
                                                        "\u001B[92m\u001B[1m" +
                                                                "╭────────────────────────────────────╮\n" +
                                                                "│      Speaker Added Successfully    │\n" +
                                                                "╰────────────────────────────────────╯" +
                                                                "\u001B[0m");
                                            }

                                            break;

                                        }
                                    }

                                    else if (speakerChoice == 3) {

                                        while (true) {

                                            if (sess == null || sess.isEmpty()) {
                                                System.out.println(
                                                        "\u001B[91mNo sessions available to Update Speakers!\u001B[0m");
                                                break;
                                            }

                                            input.nextLine();
                                            System.out.println(controller.displayAllSession(eventNumber - 1));

                                            int secNumber = 0;
                                            int sessionCount = controller.organizer.get(controller.orgIndex).events
                                                    .get(eventNumber - 1).schedule.size();

                                            while (true) {
                                                try {
                                                    System.out.print("Enter the Session Number To Update Speaker: ");
                                                    secNumber = input.nextInt();

                                                    if (secNumber >= 1 && secNumber <= sessionCount) {
                                                        break;
                                                    } else {
                                                        System.out
                                                                .println("\u001B[91mInvalid session number.\u001B[0m");
                                                    }

                                                } catch (InputMismatchException e) {
                                                    System.out.println(
                                                            "\u001B[91mInvalid input. Please enter a number.\u001B[0m");
                                                    input.nextLine();
                                                }
                                            }

                                            Session session = controller.organizer.get(controller.orgIndex).events
                                                    .get(eventNumber - 1).schedule.get(secNumber - 1);

                                            if (session.speaker != null) {
                                                System.out.println("Current Speaker: " + session.speaker.name + " ("
                                                        + session.speaker.gender + ")");
                                            } else {
                                                System.out.println("No speaker assigned yet.");
                                            }

                                            input.nextLine();
                                            System.out.print("Enter New Speaker Name: ");
                                            String speakerName = input.nextLine();

                                            String gender = "";
                                            while (true) {
                                                try {
                                                    System.out.print("Enter New Gender (Male=1, Female=2): ");
                                                    int genderChoice = input.nextInt();

                                                    if (genderChoice == 1) {
                                                        gender = "Male";
                                                        break;
                                                    } else if (genderChoice == 2) {
                                                        gender = "Female";
                                                        break;
                                                    } else {
                                                        System.out.println("\u001B[91mInvalid input.\u001B[0m");
                                                    }

                                                } catch (InputMismatchException e) {
                                                    System.out.println(
                                                            "\u001B[91mInvalid input. Please enter a number.\u001B[0m");
                                                    input.nextLine();
                                                }
                                            }

                                            Speaker newSpeaker = new Speaker(speakerName, gender);
                                            session.assignSpeaker(newSpeaker);

                                            ArrayList<Speaker> eventSpeakers = controller.organizer
                                                    .get(controller.orgIndex).events.get(eventNumber - 1).speakers;

                                            if (!eventSpeakers.contains(newSpeaker)) {
                                                eventSpeakers.add(newSpeaker);
                                            }

                                            System.out.println(
                                                    "\u001B[92m\u001B[1m" +
                                                            "╭────────────────────────────────────╮\n" +
                                                            "│    Speaker Updated Successfully!   │\n" +
                                                            "╰────────────────────────────────────╯" +
                                                            "\u001B[0m");

                                            break;
                                        }
                                    } else if (speakerChoice == 4) {
                                        if (sess == null || sess.isEmpty()) {
                                            System.out.println(
                                                    "\u001B[91mNo sessions available to Add Speakers!\u001B[0m");
                                            continue;
                                        }
                                        input.nextLine();
                                        System.out.println(controller.displayAllSession(eventNumber - 1));
                                        System.out.print("Enter the Session Number To Delete Speaker: ");
                                        try {
                                            int secNumber = input.nextInt();
                                            Session session = controller.organizer.get(controller.orgIndex).events
                                                    .get(eventNumber - 1).schedule.get(secNumber - 1);
                                            System.out.print(
                                                    "Are you sure you want to delete '" + session.speaker.name
                                                            + "'? (y/n): ");
                                            input.nextLine();
                                            String confirm = input.nextLine().trim().toLowerCase();

                                            if (confirm.equals("y") || confirm.equals("yes")) {

                                                if (session.speaker != null) {
                                                    System.out
                                                            .println("Removing Speaker: " + session.speaker.name + " ("
                                                                    + session.speaker.gender + ")");

                                                    Speaker removedSpeaker = session.speaker;
                                                    session.assignSpeaker(null);

                                                    ArrayList<Speaker> eventSpeakers = controller.organizer
                                                            .get(controller.orgIndex).events
                                                            .get(eventNumber - 1).speakers;
                                                    eventSpeakers.remove(removedSpeaker);

                                                    System.out.println(
                                                            "\u001B[91m\u001B[1m" +
                                                                    "╭────────────────────────────────────╮\n" +
                                                                    "│   Speaker Deleted Successfully!    │\n" +
                                                                    "╰────────────────────────────────────╯" +
                                                                    "\u001B[0m");
                                                } else {
                                                    System.out.println(
                                                            "\n\u001B[93mNo speaker assigned to this session.\u001B[0m\n");
                                                }
                                            } else {
                                                System.out.println(
                                                        "\u001B[93m\u001B[1m" +
                                                                "╭────────────────────────────╮\n" +
                                                                "│       Deletion Cancelled    │\n" +
                                                                "╰────────────────────────────╯" +
                                                                "\u001B[0m");
                                            }

                                        } catch (Exception e) {
                                            System.out
                                                    .println("\n\u001B[91mInvalid input. Please try again.\u001B[0m\n");
                                            input.nextLine();
                                        }
                                    } else if (speakerChoice == 5) {
                                        break;
                                    } else {
                                        System.out.println("\n\u001B[91mInvalid input\u001B[0m\n");
                                    }
                                }
                            } else if (eventChoice == 4) {
                                while (true) {
                                    System.out.println(
                                            "\u001B[33m\u001B[1m                                               ╭──────────────────────────╮");
                                    System.out.println(
                                            "                                               │                          │");
                                    System.out.println(
                                            "                                               │      \u001B[38m1. View Sponsor     \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[32m2. Add Sponsor      \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[34m3. Update Sponsor   \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[35m4. Delete Sponsor   \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[31m5. Back             \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │                          │");
                                    System.out.println(
                                            "                                               ╰──────────────────────────╯\u001B[0m");
                                    int sponsorChoice = 0;
                                    while (true) {
                                        try {
                                            System.out.print("Enter Your Choice: ");
                                            sponsorChoice = input.nextInt();
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out
                                                    .println(
                                                            "\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                            input.nextLine();
                                        }
                                    }
                                    if (sponsorChoice == 1) {
                                        System.out.println(controller.displayAllSponsor(eventNumber - 1));
                                    } else if (sponsorChoice == 2) {
                                        input.nextLine();
                                        System.out.print("Enter The Company Name: ");
                                        String companyName = input.nextLine();
                                        double amountSponsor = 0;
                                        while (true) {
                                            try {
                                                System.out.print("Enter The Amount To Sponsor: ");
                                                amountSponsor = input.nextDouble();
                                                if (amountSponsor < 1) {
                                                    System.out.println("\u001B[91mInvalid input.\u001B[0m");
                                                    continue;
                                                }
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out
                                                        .println(
                                                                "\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                                input.nextLine();
                                                continue;
                                            }
                                        }
                                        controller.organizer.get(controller.orgIndex).events
                                                .get(eventNumber - 1).sponsors
                                                .add(new Sponsor(companyName, amountSponsor));
                                        System.out.println(
                                                "\u001B[92m\u001B[1m" +
                                                        "╭────────────────────────────────────╮\n" +
                                                        "│      Sponsor Added Successfully    │\n" +
                                                        "╰────────────────────────────────────╯" +
                                                        "\u001B[0m");

                                    } else if (sponsorChoice == 3) {
                                        ArrayList<Sponsor> sponsors = controller.organizer
                                                .get(controller.orgIndex).events
                                                .get(eventNumber - 1).sponsors;

                                        if (sponsors == null || sponsors.isEmpty()) {
                                            System.out.println("\u001B[91mNo sponsors available to update!\u001B[0m");
                                        } else {
                                            System.out.println(controller.displayAllSponsor(eventNumber - 1));

                                            System.out.print("Enter the Sponsor Number to Update: ");
                                            int sponsorNumber = -1;
                                            try {
                                                sponsorNumber = input.nextInt();
                                                input.nextLine();
                                                if (sponsorNumber < 1 || sponsorNumber > sponsors.size()) {
                                                    System.out.println("\u001B[91mInvalid Sponsor Number!\u001B[0m");
                                                } else {
                                                    Sponsor selectedSponsor = sponsors.get(sponsorNumber - 1);

                                                    System.out.print("Enter New Company Name (leave blank to keep '"
                                                            + selectedSponsor.companyName + "'): ");
                                                    String companyName = input.nextLine();
                                                    if (!companyName.trim().isEmpty()) {
                                                        selectedSponsor.companyName = companyName;
                                                    }

                                                    while (true) {
                                                        try {
                                                            System.out.print("Enter New Sponsorship Amount (current: "
                                                                    + selectedSponsor.amountSponsor + "): ");
                                                            String amountInput = input.nextLine();
                                                            if (!amountInput.trim().isEmpty()) {
                                                                double amountSponsor = Double.parseDouble(amountInput);
                                                                if (amountSponsor < 1) {
                                                                    System.out.println(
                                                                            "\u001B[91mInvalid input.\u001B[0m");
                                                                    continue;
                                                                }
                                                                selectedSponsor.amountSponsor = amountSponsor;
                                                            }
                                                            break;
                                                        } catch (NumberFormatException e) {
                                                            System.out.println(
                                                                    "\u001B[91mInvalid input. Please enter a number.\u001B[0m");
                                                        }
                                                    }

                                                    System.out.println(
                                                            "\u001B[92m\u001B[1m" +
                                                                    "╭────────────────────────────────────╮\n" +
                                                                    "│     Sponsor Updated Successfully   │\n" +
                                                                    "╰────────────────────────────────────╯" +
                                                                    "\u001B[0m");
                                                }
                                            } catch (InputMismatchException e) {
                                                System.out.println(
                                                        "\u001B[91mInvalid input. Please enter a valid number.\u001B[0m");
                                                input.nextLine();
                                            }
                                        }

                                    } else if (sponsorChoice == 4) {

                                        ArrayList<Sponsor> sponsors = controller.organizer
                                                .get(controller.orgIndex).events
                                                .get(eventNumber - 1).sponsors;

                                        if (sponsors == null || sponsors.isEmpty()) {
                                            System.out.println("\u001B[91mNo sponsors available to delete!\u001B[0m");
                                        } else {

                                            System.out.println(controller.displayAllSponsor(eventNumber - 1));

                                            while (true) {
                                                try {
                                                    System.out.print("Enter the Sponsor Number to Delete: ");
                                                    int sponsorNumber = input.nextInt();
                                                    input.nextLine();

                                                    if (sponsorNumber < 1 || sponsorNumber > sponsors.size()) {
                                                        System.out
                                                                .println("\u001B[91mInvalid Sponsor Number!\u001B[0m");
                                                        continue;
                                                    }

                                                    Sponsor selected = sponsors.get(sponsorNumber - 1);

                                                    String confirm = "";
                                                    while (true) {
                                                        System.out.print("Are you sure you want to delete '"
                                                                + selected.companyName + "'? (y/n): ");
                                                        confirm = input.nextLine().trim().toLowerCase();

                                                        if (confirm.equals("y") || confirm.equals("n"))
                                                            break;

                                                        System.out.println(
                                                                "\u001B[91mInvalid input. Enter y or n.\u001B[0m");
                                                    }

                                                    if (confirm.equals("n")) {
                                                        System.out.println("\u001B[93mDeletion canceled.\u001B[0m");
                                                        break;
                                                    }

                                                    Sponsor removedSponsor = sponsors.remove(sponsorNumber - 1);

                                                    System.out.println(
                                                            "\u001B[92m\u001B[1m" +
                                                                    "╭──────────────────────────────────────────────╮\n"
                                                                    +
                                                                    "│  Sponsor '" + removedSponsor.companyName
                                                                    + "' deleted successfully!  │\n" +
                                                                    "╰──────────────────────────────────────────────╯" +
                                                                    "\u001B[0m");

                                                    break;

                                                } catch (InputMismatchException e) {
                                                    System.out.println(
                                                            "\u001B[91mInvalid input. Please enter a valid number.\u001B[0m");
                                                    input.nextLine();
                                                }
                                            }
                                        }
                                    } else if (sponsorChoice == 5) {
                                        break;
                                    } else {
                                        System.out.println(
                                                "\u001B[91mInvalid input\u001B[0m");
                                    }
                                }
                            } else if (eventChoice == 5) {
                                while (true) {
                                    System.out.println(
                                            "\u001B[33m\u001B[1m                                               ╭──────────────────────────╮");
                                    System.out.println(
                                            "                                               │                          │");
                                    System.out.println(
                                            "                                               │      \u001B[38m1. View Expense     \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[32m2. Add Expense      \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[34m3. Update Expense   \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[35m4. Delete Expense   \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │      \u001B[31m5. Back             \u001B[33m\u001B[1m│");
                                    System.out.println(
                                            "                                               │                          │");
                                    System.out.println(
                                            "                                               ╰──────────────────────────╯\u001B[0m");
                                    int expenseChoice = 0;
                                    while (true) {
                                        try {
                                            System.out.print("Enter Your Choice: ");
                                            expenseChoice = input.nextInt();
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out
                                                    .println(
                                                            "\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                            input.nextLine();
                                        }
                                    }
                                    double amount = 0;
                                    if (expenseChoice == 1) {
                                        System.out.println(controller.displayAllExpenses(eventNumber - 1));
                                    } else if (expenseChoice == 2) {
                                        input.nextLine();
                                        System.out.print("Enter The Description: ");
                                        String description = input.nextLine();
                                        while (true) {
                                            try {
                                                System.out.print("Enter The Expenses : ");
                                                amount = input.nextInt();
                                                if (amount < 1) {
                                                    System.out.println("\u001B[91mInvalid input.\u001B[0m");
                                                    continue;
                                                }
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out
                                                        .println(
                                                                "\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                                input.nextLine();
                                                continue;
                                            }
                                        }
                                        controller.organizer.get(controller.orgIndex).events
                                                .get(eventNumber - 1).expenses
                                                .add(new Expense(description, amount));
                                        System.out.println(
                                                "\u001B[92m\u001B[1m" +
                                                        "╭────────────────────────────────────╮\n" +
                                                        "│      Expenses Added Successfully   │\n" +
                                                        "╰────────────────────────────────────╯" +
                                                        "\u001B[0m");
                                    } else if (expenseChoice == 3) {
                                        ArrayList<Expense> expenses = controller.organizer
                                                .get(controller.orgIndex).events
                                                .get(eventNumber - 1).expenses;

                                        if (expenses == null || expenses.isEmpty()) {
                                            System.out.println("\u001B[91mNo expenses available to update!\u001B[0m\n");
                                            continue;
                                        }

                                        System.out.println(controller.displayAllExpenses(eventNumber - 1));
                                        System.out.print("Enter the Expense Number to Update: ");
                                        try {
                                            int expNumber = input.nextInt();
                                            if (expNumber < 1 || expNumber > expenses.size()) {
                                                System.out.println("\u001B[91mInvalid Expense Number!\u001B[0m\n");
                                                continue;
                                            }
                                            input.nextLine();

                                            Expense expenseToUpdate = expenses.get(expNumber - 1);

                                            System.out.print("Enter New Description (Current: "
                                                    + expenseToUpdate.description + "): ");
                                            String newDescription = input.nextLine();
                                            if (!newDescription.trim().isEmpty()) {
                                                expenseToUpdate.description = newDescription;
                                            }

                                            double newAmount = -1;
                                            while (true) {
                                                try {
                                                    System.out.print("Enter New Amount (Current: "
                                                            + expenseToUpdate.amount + "): ");
                                                    newAmount = input.nextDouble();
                                                    if (newAmount >= 0) {
                                                        expenseToUpdate.amount = newAmount;
                                                        break;
                                                    } else {
                                                        System.out.println(
                                                                "\u001B[91mAmount cannot be negative!\u001B[0m");
                                                    }
                                                } catch (InputMismatchException e) {
                                                    System.out.println(
                                                            "\u001B[91mInvalid input. Please enter a number.\u001B[0m");
                                                    input.nextLine();
                                                }
                                            }

                                            System.out.println(
                                                    "\u001B[92m\u001B[1m" +
                                                            "╭────────────────────────────────────╮\n" +
                                                            "│     Expense Updated Successfully!  │\n" +
                                                            "╰────────────────────────────────────╯" +
                                                            "\u001B[0m");

                                        } catch (InputMismatchException e) {
                                            System.out.println("\u001B[91mInvalid input!\u001B[0m\n");
                                            input.nextLine();
                                        }
                                    } else if (expenseChoice == 4) {

                                        ArrayList<Expense> expenses = controller.organizer
                                                .get(controller.orgIndex).events
                                                .get(eventNumber - 1).expenses;

                                        if (expenses == null || expenses.isEmpty()) {
                                            System.out.println("\u001B[91mNo expenses available to delete!\u001B[0m\n");
                                            continue;
                                        }

                                        System.out.println(controller.displayAllExpenses(eventNumber - 1));

                                        while (true) {
                                            try {
                                                System.out.print("Enter the Expense Number to Delete: ");
                                                int expNumber = input.nextInt();
                                                input.nextLine();

                                                if (expNumber < 1 || expNumber > expenses.size()) {
                                                    System.out.println("\u001B[91mInvalid Expense Number!\u001B[0m\n");
                                                    continue;
                                                }

                                                Expense selected = expenses.get(expNumber - 1);

                                                String confirm = "";
                                                while (true) {
                                                    System.out.print("Are you sure you want to delete \""
                                                            + selected.description + "\"? (y/n): ");
                                                    confirm = input.nextLine().trim().toLowerCase();

                                                    if (confirm.equals("y") || confirm.equals("n"))
                                                        break;

                                                    System.out
                                                            .println("\u001B[91mInvalid input. Enter Y or N.\u001B[0m");
                                                }

                                                if (confirm.equals("n")) {
                                                    System.out.println("\u001B[93mDeletion canceled.\u001B[0m\n");
                                                    break;
                                                }

                                                expenses.remove(expNumber - 1);

                                                System.out.println(
                                                        "\u001B[92m\u001B[1m" +
                                                                "╭────────────────────────────────────╮\n" +
                                                                "│     Expense Deleted Successfully!  │\n" +
                                                                "╰────────────────────────────────────╯" +
                                                                "\u001B[0m");

                                                break;

                                            } catch (InputMismatchException e) {
                                                System.out.println("\u001B[91mInvalid input!\u001B[0m\n");
                                                input.nextLine();
                                            }
                                        }
                                    } else if (expenseChoice == 5) {
                                        break;
                                    } else {
                                        System.out
                                                .println("\u001B[91mInvalid input\u001B[0m");
                                    }

                                }
                            } else if (eventChoice == 6) {

                                Event currentEvent = controller.organizer.get(controller.orgIndex).events
                                        .get(eventNumber - 1);
                                boolean ticketsInitialized = false;
                                if (!ticketsInitialized) {
                                    for (int i = 0; i < currentEvent.venue.capacity; i++) {
                                        currentEvent.ticketsSold.add(new Ticket(
                                                currentEvent.name,
                                                currentEvent.category,
                                                0));
                                    }
                                    ticketsInitialized = true;
                                }

                                while (true) {
                                    System.out.println(
                                            "\u001B[33m\u001B[1m                                               ╭──────────────────────────╮");
                                    System.out.println(
                                            "                                               │                          │");
                                    System.out.println(
                                            "                                               │   \u001B[32m1. Available Tickets\u001B[33m\u001B[1m   │");
                                    System.out.println(
                                            "                                               │   \u001B[34m2. Update Price     \u001B[33m\u001B[1m   │");
                                    System.out.println(
                                            "                                               │   \u001B[35m3. Update Count     \u001B[33m\u001B[1m   │");
                                    System.out.println(
                                            "                                               │   \u001B[30m4. View Price       \u001B[33m\u001B[1m   │");
                                    System.out.println(
                                            "                                               │   \u001B[31m5. Back             \u001B[33m\u001B[1m   │");
                                    System.out.println(
                                            "                                               │                          │");
                                    System.out.println(
                                            "                                               ╰──────────────────────────╯\u001B[0m");
                                    int ticketChoice = 0;
                                    while (true) {
                                        try {
                                            System.out.print("Enter Your Choice: ");
                                            ticketChoice = input.nextInt();
                                            break;
                                        } catch (InputMismatchException e) {
                                            System.out
                                                    .println(
                                                            "\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                            input.nextLine();
                                        }
                                    }
                                    double rate = 0;
                                    boolean modified = false;
                                    int updateCount = 0;

                                    if (ticketChoice == 1) {
                                        System.out.println(
                                                "                                               \u001B[33m\u001B[1m╭─────────────────────────────────╮");
                                        System.out.println(
                                                "                                               │                                 │");
                                        System.out.println(
                                                "                                               │ \u001B[36mAvailable Tickets: "
                                                        + currentEvent.ticketsSold.size()
                                                        + " \u001B[33m\u001B[1m         │");
                                        System.out.println(
                                                "                                               │                                 │");
                                        System.out.println(
                                                "                                               ╰─────────────────────────────────╯\u001B[0m");
                                    } else if (ticketChoice == 2) {
                                        if (currentEvent.ticketsSold.isEmpty()
                                                || currentEvent.ticketsSold.get(0) == null) {
                                            System.out.println("No Tickets Avaliable");
                                            continue;
                                        }
                                        try {
                                            while (true) {
                                                System.out.print("Enter the new ticket price: ");
                                                rate = input.nextDouble();
                                                if (rate < 1) {
                                                    System.out.println("\u001B[91mInvalid input.\u001B[0m");
                                                    continue;
                                                }
                                                break;
                                            }
                                            for (Ticket t : currentEvent.ticketsSold) {
                                                t.price = rate;
                                            }
                                            System.out.println(
                                                    "\u001B[92m\u001B[1m" +
                                                            "╭────────────────────────────────────────╮\n" +
                                                            "│   Ticket Price Updated Successfully    │\n" +
                                                            "╰────────────────────────────────────────╯" +
                                                            "\u001B[0m");
                                        } catch (InputMismatchException e) {
                                            System.out.println(
                                                    "\u001B[91mInvalid input. Please enter a number.\u001B[0m");
                                            input.nextLine();
                                        }
                                    } else if (ticketChoice == 3) {
                                        while (true) {
                                            System.out.print("Enter the new ticket count: ");
                                            updateCount = input.nextInt();
                                            if (updateCount < 1) {
                                                System.out.println("\u001B[91mInvalid input.\u001B[0m");
                                                continue;
                                            }
                                            break;
                                        }
                                        controller.organizer.get(controller.orgIndex).events
                                                .get(eventNumber - 1).venue.capacity = updateCount;
                                        currentEvent.ticketsSold.clear();
                                        for (int i = 0; i < updateCount; i++) {
                                            currentEvent.ticketsSold
                                                    .add(new Ticket(currentEvent.name, currentEvent.category, rate));
                                        }
                                        System.out.println(
                                                "\u001B[92m\u001B[1m" +
                                                        "╭────────────────────────────────────────╮\n" +
                                                        "│   Ticket Count Updated Successfully    │\n" +
                                                        "╰────────────────────────────────────────╯" +
                                                        "\u001B[0m");
                                    } else if (ticketChoice == 4) {
                                        if (currentEvent.ticketsSold.isEmpty()
                                                || currentEvent.ticketsSold.get(0) == null) {
                                            System.out.println("No Tickets Avaliable");
                                            continue;
                                        }
                                        System.out.println("\u001B[33m\u001B[1m╭─────────────────────────────────╮");
                                        System.out.println("│                                 │");
                                        System.out.println(
                                                "│ \u001B[36mTickets Price : " + currentEvent.ticketsSold.get(0).price
                                                        + " \u001B[33m\u001B[1m             │");
                                        System.out.println("│                                 │");
                                        System.out.println("╰─────────────────────────────────╯\u001B[0m");
                                    } else if (ticketChoice == 5) {
                                        break;
                                    } else {
                                        System.out.println("\u001B[91mInvalid input.\u001B[0m\n");
                                        input.nextLine();
                                    }
                                }

                            } else {
                                break;
                            }
                        }
                    } else if (choice == 4) {
                        Organizer org = controller.organizer.get(controller.orgIndex);
                        System.out.println(controller.displayAllEvents());
                        if (org.events.isEmpty()) {
                            continue;
                        }
                        int deleteEventIndex = 0;
                        while (true) {
                            try {
                                System.out.print("Enter The Event Number To Delete(Back: 0): ");
                                deleteEventIndex = input.nextInt();
                                if (deleteEventIndex < 0 || deleteEventIndex > org.events.size()) {
                                    System.out.println("\u001B[91mInvalid input.\u001B[0m\n");
                                    input.nextLine();
                                    continue;
                                }
                                break;
                            } catch (Exception e) {
                                System.out.println("\u001B[91mInvalid input.\u001B[0m\n");
                                input.nextLine();
                            }
                        }
                        if (deleteEventIndex == 0) {
                            continue;
                        }
                        if (org.events.isEmpty()) {
                            System.out.println("\u001B[91mInvalid input.\u001B[0m\n");
                        } else {
                            while (true) {
                                input.nextLine();
                                System.out.print(
                                        "Are you sure you want to delete '" + org.events.get(deleteEventIndex - 1).name
                                                + "'? (y/n): ");
                                String confirm = input.nextLine().trim().toLowerCase();

                                if (confirm.equals("y") || confirm.equals("yes")) {
                                    System.out.println(
                                            "\u001B[91m\u001B[1m" +
                                                    "╭────────────────────────────────────────────────────────╮\n" +
                                                    "│  Event '" + org.events.get(deleteEventIndex - 1).name
                                                    + "' deleted successfully!  \n" +
                                                    "╰────────────────────────────────────────────────────────╯" +
                                                    "\u001B[0m");

                                    org.events.remove(deleteEventIndex - 1);
                                    break;
                                } else {
                                    System.out.println(
                                            "\u001B[93m\u001B[1m" +
                                                    "╭────────────────────────────╮\n" +
                                                    "│       Deletion Cancelled    │\n" +
                                                    "╰────────────────────────────╯" +
                                                    "\u001B[0m");
                                    break;
                                }
                            }
                        }

                    } else if (choice == 5) {
                        controller.saveEvents(controller.organizer);
                        controller.readEvents();
                        break;
                    } else {
                        System.out.println("\u001B[91mInvalid input.\u001B[0m\n");
                    }
                }
            } else if (roleChoice == 2) {
                organ: while (true) {
                    int userOrgChoice = 0;
                    System.out.println(controller.displayAllOrganizer());
                    try {
                        System.out.print("Enter Your Choice(Logout: 0): ");
                        userOrgChoice = input.nextInt();
                        if (userOrgChoice < 0 || controller.organizer.size() < userOrgChoice) {
                            System.out.println("\u001B[91mInvalid input.\u001B[0m\n");
                            continue;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("\u001B[91mInvalid input. Enter The Number\u001B[0m\n");
                        input.nextLine();
                        continue;
                    }
                    if (userOrgChoice == 0)
                        break;
                    Organizer org = controller.organizer.get(userOrgChoice - 1);
                    controller.orgIndex = userOrgChoice - 1;
                    System.out.println("\n" + controller.displayAllEvents() + "\n");
                    int userEventNumber = 0;
                    while (true) {
                        try {
                            System.out.print("Enter The Event Number You want to Get In(Back:0): ");
                            userEventNumber = input.nextInt();
                            if (userEventNumber == 0)
                                continue start;
                            controller.organizer.get(controller.orgIndex).events.get(userEventNumber - 1);
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
                        System.out.println(
                                "                                                 \u001B[33m\u001B[1m╭──────────────────────────────╮");
                        System.out.println(
                                "                                                 │                              │");
                        System.out.println(
                                "                                                 │      \u001B[36m1. View Session         \u001B[33m\u001B[1m│");
                        System.out.println(
                                "                                                 │      \u001B[32m2. View Speaker         \u001B[33m\u001B[1m│");
                        System.out.println(
                                "                                                 │      \u001B[34m3. View Sponsor         \u001B[33m\u001B[1m│");
                        System.out.println(
                                "                                                 │      \u001B[35m4. View Booth           \u001B[33m\u001B[1m│");
                        System.out.println(
                                "                                                 │      \u001B[35m5. Purchase Ticket      \u001B[33m\u001B[1m│");
                        // System.out.println(
                        //         "                                                 │      \u001B[31m6. My Tickets           \u001B[33m\u001B[1m│");
                        System.out.println(
                                "                                                 │      \u001B[31m6. Back                 \u001B[33m\u001B[1m│");
                        System.out.println(
                                "                                                 │                              │");
                        System.out.println(
                                "                                                 ╰──────────────────────────────╯\u001B[0m"); // want
                                                                                                                               // to
                                                                                                                               // add
                                                                                                                               // booths
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
                            System.out.println(controller.displayAllBooths(userEventNumber - 1));
                        } else if (userChoice == 5) {
                            Event selectedEvent = controller.organizer.get(controller.orgIndex).events
                                    .get(userEventNumber - 1);

                            if (selectedEvent.ticketsSold.isEmpty()) {
                                System.out.println("\u001B[91mSorry! No Tickets Available\u001B[0m");
                            } else {
                                System.out.println(
                                        "                                               \u001B[33m\u001B[1m╭─────────────────────────────────╮");
                                System.out.println(
                                        "                                               │                                 │");
                                System.out.println(
                                        "                                               │ \u001B[36mAvailable Tickets: "
                                                + controller.organizer.get(controller.orgIndex).events
                                                        .get(0).ticketsSold
                                                        .size()
                                                + " \u001B[33m\u001B[1m          │");
                                System.out.println(
                                        "                                               │                                 │");
                                System.out.println(
                                        "                                               ╰─────────────────────────────────╯\u001B[0m");
                                Payment payment = new Payment(
                                        controller.organizer.get(controller.orgIndex).events
                                                .get(userEventNumber - 1).ticketsSold
                                                .get(0).price);
                                int noOfTicket = 0;
                                Ticket purchasedTicket = null;
                                max: while (true) {
                                    try {
                                        System.out
                                                .print("Enter The Number Of Ticket You Are Going To Purchase (Max : 10): ");
                                        noOfTicket = input.nextInt();
                                        if (noOfTicket < 1 || noOfTicket > 10
                                                || noOfTicket > controller.organizer.get(controller.orgIndex).events
                                                        .get(userEventNumber - 1).ticketsSold.size()) {
                                            System.out
                                                    .println("\u001B[91mInvalid Ticket Count\u001B[0m\n");
                                            input.nextLine();
                                            continue;
                                        }

                                        break;
                                    } catch (InputMismatchException e) {
                                        System.out
                                                .println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                        input.nextLine();
                                    }
                                }

                                while (true) {
                                    try {
                                        System.out
                                                .print("Enter The Payment Amount " + (payment.amount * noOfTicket)
                                                        + " : ");
                                        int amount = input.nextInt();
                                        if (amount < (payment.amount * noOfTicket)) {
                                            System.out
                                                    .println("\u001B[91mInvalid Amount\u001B[0m\n");
                                            input.nextLine();
                                            continue;
                                        }
                                        if (amount > (payment.amount * noOfTicket)) {
                                            System.out.println(
                                                    "Here is Your Balance " + (amount - (payment.amount * noOfTicket)));
                                        }
                                        for (int i = 0; i < noOfTicket; i++) {
                                            purchasedTicket = selectedEvent.ticketsSold
                                                    .remove(selectedEvent.ticketsSold.size() - 1);
                                        }
                                        payment.processPayment(noOfTicket);
                                        break;
                                    } catch (InputMismatchException e) {
                                        System.out
                                                .println("\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                        input.nextLine();
                                    }
                                }
                                controller.organizer.get(controller.orgIndex).events
                                        .get(userEventNumber
                                                - 1).venue.capacity = controller.organizer
                                                        .get(controller.orgIndex).events
                                                        .get(0).ticketsSold.size();
                                // System.out.println(
                                // "Ticket Count : "
                                // + );
                                controller.users.get(controller.userIndex).addTicket(purchasedTicket);
                                System.out.println(
                                        "\u001B[92m\u001B[1m" +
                                                "╭────────────────────────────────────────╮\n" +
                                                "│   Ticket Purchased Successfully!       │\n" +
                                                "╰────────────────────────────────────────╯" +
                                                "\u001B[0m");
                            }
                        }
                        // else if (userChoice == 6) {
                        // System.out.println(controller.displayAllTickets(controller.users.get(controller.userIndex)));
                        // }
                        else if (userChoice == 6) {
                            continue organ;
                        }
                    }

                }
            } else {
                System.out.println("\u001B[91mInvalid Choice\u001B[0m\n");
            }
        }

    }

    String displayAllSpeakers(int eventNumber) {
        System.out.println(
                "                                 \u001B[32m░██████                                    ░██                           \n                                ░██   ░██                                   ░██                           \n                               ░██         ░████████   ░███████   ░██████   ░██    ░██ ░███████  ░██░████ \n                                ░████████  ░██    ░██ ░██    ░██       ░██  ░██   ░██ ░██    ░██ ░███     \n                                       ░██ ░██    ░██ ░█████████  ░███████  ░███████  ░█████████ ░██      \n                                ░██   ░██  ░███   ░██ ░██        ░██   ░██  ░██   ░██ ░██        ░██      \n                                 ░██████   ░██░█████   ░███████   ░█████░██ ░██    ░██ ░███████  ░██      \n                                           ░██                                                            \n                                           ░██\u001B[0m");
        StringBuilder result = new StringBuilder();

        result.append(
                "                                            ╭──────┬────────────────────────────────┬────────────╮\n");
        result.append(
                "                                            │ S.No │ Name                           │ Gender     │\n");
        result.append(
                "                                            ├──────┼────────────────────────────────┼────────────┤\n");

        ArrayList<Speaker> speakers = organizer.get(orgIndex).events.get(eventNumber).speakers;
        if (speakers == null || speakers.isEmpty()) {
            result.append(
                    "                                            │  --  │ No speakers available          │    --      │\n");
        }
        for (int i = 0; i < speakers.size(); i++) {
            Speaker s = speakers.get(i);

            String name = s.name;
            String gender = s.gender;

            result.append(String.format("                                            │ %-5d│ %-30s │ %-10s │\n", i + 1,
                    name, gender));

            if (i != speakers.size() - 1) {
                result.append(
                        "                                            ├──────┼────────────────────────────────┼────────────┤\n");
            }
        }

        result.append(
                "                                            ╰──────┴────────────────────────────────┴────────────╯\n");

        return result.toString();
    }

    String displayAllSession(int eventNumber) {

        System.out.println(
                "\u001B[35m                                    ███████╗ ███████╗ ███████╗ ███████╗ ██╗  ██████╗  ███╗   ██╗\n                                    ██╔════╝ ██╔════╝ ██╔════╝ ██╔════╝ ██║ ██╔═══██╗ ████╗  ██║\n                                    ███████╗ █████╗   ███████╗ ███████╗ ██║ ██║   ██║ ██╔██╗ ██║\n                                    ╚════██║ ██╔══╝   ╚════██║ ╚════██║ ██║ ██║   ██║ ██║╚██╗██║\n                                    ███████║ ███████╗ ███████║ ███████║ ██║ ╚██████╔╝ ██║ ╚████║\n                                    ╚══════╝ ╚══════╝ ╚══════╝ ╚══════╝ ╚═╝  ╚═════╝  ╚═╝  ╚═══╝\n                                    \u001B[0m");

        StringBuilder result = new StringBuilder();

        result.append(
                "\u001B[97m     ╭──────┬──────────────────────┬───────────────────────────────────────────────────────────┬──────────────────────────────╮\n");
        result.append(
                "     │ S.No │ Time                 │ Sessions scheduled                                        │ Speaker                      │\n");
        result.append(
                "     ├──────┼──────────────────────┼───────────────────────────────────────────────────────────┼──────────────────────────────┤\n");

        ArrayList<Session> sessions = organizer.get(orgIndex).events.get(eventNumber).schedule;

        if (sessions == null || sessions.isEmpty()) {
            result.append(
                    "     │  --  │   --                 │ No sessions available                                     │        --                    │\n");
        } else {
            int i = 0;
            for (Session s : sessions) {
                String time = s.timeRange;
                String action = s.topic;
                String owner = (s.speaker != null)
                        ? s.speaker.name + " (" + s.speaker.gender + ")"
                        : "Not Added Yet";

                result.append(String.format("     │ %-5d│ %-20s │ %-57s │ %-28s │\n", i + 1, time, action, owner));
                if (i++ != sessions.size() - 1) {
                    result.append(
                            "     ├──────┼──────────────────────┼───────────────────────────────────────────────────────────┼──────────────────────────────┤\n");
                }
            }
        }

        result.append(
                "     ╰──────┴──────────────────────┴───────────────────────────────────────────────────────────┴──────────────────────────────╯\u001B[0m");
        return result.toString();
    }

    String displayAllSponsor(int eventNumber) {
        StringBuilder result = new StringBuilder();
        System.out.println("                           \u001B[34m$$$$$$\\ ");
        System.out.println("                          $$  __$$\\ ");
        System.out.println(
                "                          $$ /  \\__| $$$$$$\\   $$$$$$\\  $$$$$$$\\   $$$$$$$\\  $$$$$$\\   $$$$$$\\ ");
        System.out.println(
                "                          \\$$$$$$\\  $$  __$$\\ $$  __$$\\ $$  __$$\\ $$  _____|$$  __$$\\ $$  __$$\\ ");
        System.out.println(
                "                           \\____$$\\ $$ /  $$ |$$ /  $$ |$$ |  $$ |\\$$$$$$\\  $$ /  $$ |$$ |  \\__|");
        System.out.println(
                "                          $$\\   $$ |$$ |  $$ |$$ |  $$ |$$ |  $$ | \\____$$\\ $$ |  $$ |$$ |      ");
        System.out.println(
                "                          \\$$$$$$  |$$$$$$$  |\\$$$$$$  |$$ |  $$ |$$$$$$$  |\\$$$$$$  |$$ |      ");
        System.out.println(
                "                           \\______/ $$  ____/  \\______/ \\__|  \\__|\\_______/  \\______/ \\__|      ");
        System.out.println("                                    $$ |");
        System.out.println("                                    $$ |");
        System.out.println("                                    \\__|\u001B[0m");

        result.append("                                     ╭───────┬───────────────────────────┬─────────────╮\n");
        result.append("                                     │ S.No  │ Company Name              │ Amount      │\n");
        result.append("                                     ├───────┼───────────────────────────┼─────────────┤\n");

        ArrayList<Sponsor> sponsors = organizer.get(orgIndex).events.get(eventNumber).sponsors;
        if (sponsors == null || sponsors.isEmpty()) {
            result.append("                                     │  --   │ No sponsors available     │     --      │\n");
        }
        for (int i = 0; i < sponsors.size(); i++) {
            Sponsor s = sponsors.get(i);

            String company = s.companyName;
            String amount = String.format("%.2f", s.amountSponsor);

            result.append(String.format("                                     │ %-6d│ %-25s │ %-11s │\n", i + 1,
                    company, amount));

            if (i != sponsors.size() - 1) {
                result.append(
                        "                                     ├───────┼───────────────────────────┼─────────────┤\n");
            }
        }

        result.append("                                     ╰───────┴───────────────────────────┴─────────────╯\n");

        return result.toString();
    }

    void addEvents(Event e) {
        organizer.get(orgIndex).events.add(e);
    }

    void saveEvents(ArrayList<Organizer> events2) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(events2);
        } catch (Exception i) {

        }
    }

    @SuppressWarnings("unchecked")
    ArrayList<Organizer> readEvents() {
        ArrayList<Organizer> list = new ArrayList<>();
        if (!path.exists()) {
            return list;
        }

        try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(path))) {
            list = (ArrayList<Organizer>) oos.readObject();
        } catch (Exception e) {
        }
        return list;
    }

    String displayAllEvents() {
        if (!organizer.get(orgIndex).events.isEmpty()) {
            String result = "                                  \033[33m███████╗ ██╗ ██╗ ███████╗ ███╗   ██╗ ████████╗ ███████╗\n                                  ██╔════╝ ██║ ██║ ██╔════╝ ████╗  ██║ ╚══██╔══╝ ██╔════╝\n                                  █████╗   ██║ ██║ █████╗   ██╔██╗ ██║    ██║    ███████╗\n                                  ██╔══╝   ██║ ██║ ██╔══╝   ██║╚██╗██║    ██║    ╚════██║\n                                  ███████╗ ╚████╔╝ ███████╗ ██║ ╚████║    ██║    ███████║\n                                  ╚══════╝  ╚═══╝  ╚══════╝ ╚═╝ ╚═══╝     ╚═╝    ╚══════╝\n                                  \n";
            for (Event e : organizer.get(orgIndex).events) {
                e.num = organizer.get(orgIndex).events.indexOf(e) + 1;
                result += e.displayEvent() + "\n\n";
            }
            return result;
        } else {
            return "\u001B[32m\u001B[1m" +
                    "╭───────────────────────────────╮\n" +
                    "│       No Events Available     │\n" +
                    "╰───────────────────────────────╯" +
                    "\u001B[0m";
        }
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

    String displayAllBooths(int eventNumber) {
        StringBuilder result = new StringBuilder();
        System.out.println(
                "                                            \u001B[33m$$$$$$$\\                       $$\\     $$\\       ");
        System.out.println(
                "                                            $$  __$$\\                      $$ |    $$ |      ");
        System.out.println(
                "                                            $$ |  $$ | $$$$$$\\   $$$$$$\\ $$$$$$\\   $$$$$$$\\  ");
        System.out.println(
                "                                            $$$$$$$\\ |$$  __$$\\ $$  __$$\\\\_$$  _|  $$  __$$\\ ");
        System.out.println(
                "                                            $$  __$$\\ $$ /  $$ |$$ /  $$ | $$ |    $$ |  $$ |");
        System.out.println(
                "                                            $$ |  $$ |$$ |  $$ |$$ |  $$ | $$ |$$\\ $$ |  $$ |");
        System.out.println(
                "                                            $$$$$$$  |\\$$$$$$  |\\$$$$$$  | \\$$$$  |$$ |  $$ |");
        System.out.println(
                "                                            \\_______/  \\______/  \\______/   \\____/ \\__|  \\__|\u001B[0m");

        result.append(
                "                                  ╭──────┬─────────────────────────────┬──────────────┬──────────────╮\n");
        result.append(
                "                                  │ S.No │ Booth Name                  │ Size (sq.ft) │ Cost (₹)     │\n");
        result.append(
                "                                  ├──────┼─────────────────────────────┼──────────────┼──────────────┤\n");

        ArrayList<Booth> booths = organizer.get(orgIndex).events.get(eventNumber).booths;

        if (booths == null || booths.isEmpty()) {
            result.append(
                    "                                  │  --  │ No booths available         │      --      │      --      │\n");
        } else {
            for (int i = 0; i < booths.size(); i++) {
                Booth b = booths.get(i);
                String name = b.name;
                String size = String.format("%.2f", b.size);
                String cost = String.format("%.2f", b.price);

                result.append(String.format("                                  │ %-5d│ %-27s │ %-12s │ %-12s │\n",
                        i + 1, name, size, cost));

                if (i != booths.size() - 1) {
                    result.append(
                            "                                  ├──────┼─────────────────────────────┼──────────────┼──────────────┤\n");
                }
            }
        }

        result.append(
                "                                  ╰──────┴─────────────────────────────┴──────────────┴──────────────╯\n");

        return result.toString();
    }

    String displayAllExpenses(int eventNumber) {
        StringBuilder result = new StringBuilder();

        result.append("╭──────┬───────────────────────────┬─────────────╮\n");
        result.append("│ S.No │ Description               │ Amount      │\n");
        result.append("├──────┼───────────────────────────┼─────────────┤\n");

        ArrayList<Expense> expenses = organizer.get(orgIndex).events
                .get(eventNumber).expenses;

        if (expenses == null || expenses.isEmpty()) {
            result.append("│  --  │ No expenses available     │      --     │\n");
        } else {
            for (int i = 0; i < expenses.size(); i++) {
                Expense e = expenses.get(i);
                String description = e.description;
                String amount = String.format("%.2f", e.amount);

                result.append(String.format("│ %-5d│ %-25s │ %-11s │\n", i + 1, description, amount));

                if (i != expenses.size() - 1) {
                    result.append("├──────┼───────────────────────────┼─────────────┤\n");
                }
            }
        }

        result.append("╰──────┴───────────────────────────┴─────────────╯\n");

        return result.toString();
    }

    String displayAllOrganizer() {
        StringBuilder result = new StringBuilder();

        result.append("                                         ╭──────┬───────────────────────────┬─────────────╮\n");
        result.append("                                         │ S.No │ Organizer Name            │ Event Count │\n");
        result.append("                                         ├──────┼───────────────────────────┼─────────────┤\n");

        if (organizer == null || organizer.isEmpty()) {
            result.append(
                    "                                         │  --  │ No organizers available   │     --      │\n");
        } else {
            for (int i = 0; i < organizer.size(); i++) {
                Organizer o = organizer.get(i);
                String name = o.name;
                int eventCount = (o.events != null) ? o.events.size() : 0;

                result.append(String.format("                                         │ %-5d│ %-25s │ %-11d │\n", i + 1,
                        name, eventCount));

                if (i != organizer.size() - 1) {
                    result.append(
                            "                                         ├──────┼───────────────────────────┼─────────────┤\n");
                }
            }
        }

        result.append("                                         ╰──────┴───────────────────────────┴─────────────╯\n");

        return result.toString();
    }

    private static double parseTime(String timeStr) {
        timeStr = timeStr.toUpperCase().trim();
        boolean isPM = timeStr.contains("PM");
        timeStr = timeStr.replace("AM", "").replace("PM", "").trim();
        double hour = Double.parseDouble(timeStr.split(":")[0]);
        if (isPM && hour != 12)
            hour += 12;
        return hour;
    }

    // Validations
    public boolean isValidName(String name) {
        if (!(name.length() > 2)) {
            System.out.println(
                    "\u001B[91m╭────────────────────────────────────────────╮\n" +
                            "│      Name must be greater than 2 letters   │\n" +
                            "╰────────────────────────────────────────────╯\u001B[0m");
            return false;
        }
        return true;
    }

    public boolean isValidEmail(String email) {
        if (!(email.endsWith("@gmail.com") || email.endsWith("@zoho.com") || email.endsWith("@zohocorp.com"))) {
            System.out.println(
                    "\u001B[91m╭───────────────────────────────────────╮\n" +
                            "│         Invalid Email Format          │\n" +
                            "╰───────────────────────────────────────╯\u001B[0m");
            return false;
        } else if (email.length() < 13) {
            System.out.println(
                    "\u001B[91m╭────────────────────────────────────────────╮\n" +
                            "│        Email must be at least 13 letters   │\n" +
                            "╰────────────────────────────────────────────╯\u001B[0m");
            return false;
        }

        return true;
    }

    public boolean isValidPassword(String password) {

        if (password.length() < 6) {
            System.out.println(
                    "\u001B[91m╭────────────────────────────────────────────╮\n" +
                            "│     Password must be at least 6 characters │\n" +
                            "╰────────────────────────────────────────────╯\u001B[0m");
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            System.out.println(
                    "\u001B[91m╭────────────────────────────────────────────╮\n" +
                            "│    Password must contain an uppercase letter │\n" +
                            "╰────────────────────────────────────────────╯\u001B[0m");
            return false;
        }

        if (!password.matches(".*[0-9].*")) {
            System.out.println(
                    "\u001B[91m╭────────────────────────────────────────────╮\n" +
                            "│        Password must contain a number       │\n" +
                            "╰────────────────────────────────────────────╯\u001B[0m");
            return false;
        }

        if (!password.matches(".*[!@#$%^&()_+\\-={}\\[\\]|:;\"'<>,.?/].*")) {
            System.out.println(
                    "\u001B[91m╭────────────────────────────────────────────╮\n" +
                            "│    Password must contain a special symbol   │\n" +
                            "╰────────────────────────────────────────────╯\u001B[0m");
            return false;
        }

        return true;
    }

    public boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() == 10;
    }

    public boolean isValidLocation(String location) {
        return location.length() > 5;
    }

    public boolean isValidAddress(String address) {
        return address.length() > 10;
    }

    static String formatTime(double hour) {
        String period = "AM";
        if (hour == 0) {
            hour = 12;
        } else if (hour >= 12) {
            period = "PM";
            if (hour > 12) {
                hour -= 12;
            }
        }
        return String.format("%.0f:00 %s", hour, period);
    }

    // String displayAllTickets(User user) {
    // String result = "";
    // for(Ticket t : user.ticket){
    // result += t.displayTicket()+"\n";
    // }
    // return result;
    // }
}
