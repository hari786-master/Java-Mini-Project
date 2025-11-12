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
                controller.organizer = controller.readEvents();
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

                        // controller.fileWriter("events",
                        // controller.organizer.get(controller.orgIndex).events);

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
                                System.out.println("\u001B[33m\u001B[1m┌──────────────────────────┐");
                                System.out.println("│                          │");
                                System.out.println("│      \u001B[38m1. Booth            \u001B[33m\u001B[1m│");
                                System.out.println("│      \u001B[32m2. Session          \u001B[33m\u001B[1m│");
                                System.out.println("│      \u001B[34m3. Speaker          \u001B[33m\u001B[1m│");
                                System.out.println("│      \u001B[35m4. Sponsor          \u001B[33m\u001B[1m│");
                                System.out.println("│      \u001B[36m5. Expense          \u001B[33m\u001B[1m│");
                                System.out.println("│      \u001B[37m6. Tickets          \u001B[33m\u001B[1m│");
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
                                    System.out.println("\u001B[33m\u001B[1m┌──────────────────────────┐");
                                    System.out.println("│                          │");
                                    System.out.println("│      \u001B[38m1. View Booth       \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[32m2. Add Booth        \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[34m3. Update Booth     \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[35m4. Delete Booth     \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[31m5. Exit             \u001B[33m\u001B[1m│");
                                    System.out.println("│                          │");
                                    System.out.println("└──────────────────────────┘\u001B[0m");
                                    System.out.print("Enter Your Choice: ");
                                    int boothChoice = input.nextInt();
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
                                            controller.organizer.get(controller.orgIndex).events.get(eventNumber - 1)
                                                    .addBooth(booth);
                                            System.out.println("Booth Added Sucessfully :) ");
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
                                        System.out.print("Enter The Booth Number To Update(Exit: 0): ");
                                        int boothNum = 0;
                                        while (true) {
                                            try {
                                                System.out.print("\nEnter the booth number to update (Exit: 0): ");
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
                                                boothToUpdate.price = newCost;
                                                break;
                                            } catch (NumberFormatException e) {
                                                System.out.println(
                                                        "\u001B[91mInvalid input. Please enter a number.\u001B[0m\n");
                                            }
                                        }

                                        System.out.println(" Booth updated successfully!");
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
                                                System.out.print("Enter the booth number to delete (Exit: 0): ");
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
                                                    " Booth '" + removedBooth.name + "' deleted successfully!");
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
                                    System.out.println("\u001B[33m\u001B[1m┌──────────────────────────┐");
                                    System.out.println("│                          │");
                                    System.out.println("│      \u001B[38m1. View Session     \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[32m2. Add Session      \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[34m3. Update Session   \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[35m4. Delete Session   \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[31m5. Exit             \u001B[33m\u001B[1m│");
                                    System.out.println("│                          │");
                                    System.out.println("└──────────────────────────┘\u001B[0m");
                                    System.out.print("Enter Your Choice: ");
                                    int sessionChoice = input.nextInt();
                                    if (sessionChoice == 1) {
                                        System.out.println(controller.displayAllSession(eventNumber - 1));
                                    } else if (sessionChoice == 2) {
                                        input.nextLine();
                                        double nextTime = 9;
                                        String period = "AM";
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
                                                else if (yesOrNo == 'n')
                                                    break par;
                                                else
                                                    System.out.println("\u001B[91mInvalid input\u001B[0m");
                                            }
                                        }
                                    } else if (sessionChoice == 3) {
                                        ArrayList<Session> sessions = controller.organizer
                                                .get(controller.orgIndex).events.get(eventNumber - 1).schedule;

                                        if (sessions.isEmpty()) {
                                            System.out.println("\u001B[91mNo sessions available to update.\u001B[0m\n");
                                            continue;
                                        }

                                        System.out.println(controller.displayAllSession(eventNumber - 1));
                                        int updateIndex = 0;

                                        while (true) {
                                            try {
                                                System.out.print("Enter the Session Number to Update (Exit:0): ");
                                                updateIndex = input.nextInt();
                                                input.nextLine();
                                                if (updateIndex == 0)
                                                    break;
                                                if (updateIndex < 1 || updateIndex > sessions.size()) {
                                                    System.out.println("\u001B[91mInvalid session number.\u001B[0m\n");
                                                    continue;
                                                }
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out.println(
                                                        "\u001B[91mInvalid input. Please enter a number.\u001B[0m\n");
                                                input.nextLine();
                                            }
                                        }

                                        if (updateIndex == 0)
                                            continue;

                                        Session selectedSession = sessions.get(updateIndex - 1);

                                        System.out.println("\nWhat do you want to update?");
                                        System.out.println("1. Topic");
                                        System.out.println("2. Time Range");
                                        System.out.println("3. Both");
                                        System.out.print("Enter your choice: ");
                                        int updateChoice = input.nextInt();
                                        input.nextLine();

                                        if (updateChoice == 1 || updateChoice == 3) {
                                            System.out.print("Enter new topic name: ");
                                            String newTopic = input.nextLine().trim();
                                            selectedSession.topic = newTopic;
                                        }

                                        if (updateChoice == 2 || updateChoice == 3) {
                                            while (true) {
                                                try {
                                                    System.out.print("Enter new start time (e.g., 9): ");
                                                    double start = input.nextDouble();
                                                    System.out.print("Enter new end time (e.g., 12): ");
                                                    double end = input.nextDouble();
                                                    input.nextLine();

                                                    String startFormatted = String.format("%.0f:00", start);
                                                    String endFormatted = String.format("%.0f:00", end);
                                                    selectedSession.timeRange = startFormatted + " to " + endFormatted;
                                                    break;
                                                } catch (InputMismatchException e) {
                                                    System.out.println(
                                                            "\u001B[91mInvalid input. Please enter a valid time.\u001B[0m");
                                                    input.nextLine();
                                                }
                                            }
                                        }

                                        System.out.println("\u001B[32mSession updated successfully!\u001B[0m\n");
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

                                                Session removed = sessions.remove(deleteIndex);
                                                System.out.println("\u001B[32mSession \"" + removed.topic
                                                        + "\" deleted successfully!\u001B[0m");

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

                                                    String startFormatted = String.format("%.0f:00 %s", nextTime,
                                                            period);
                                                    String endFormatted = String.format("%.0f:00 %s", endTime,
                                                            endPeriod);

                                                    s.timeRange = startFormatted + " to " + endFormatted;

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
                                    System.out.println("\u001B[33m\u001B[1m┌──────────────────────────┐");
                                    System.out.println("│                          │");
                                    System.out.println("│      \u001B[38m1. View Speaker     \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[32m2. Add Speaker      \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[34m3. Update Speaker   \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[35m4. Delete Speaker   \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[31m5. Exit             \u001B[33m\u001B[1m│");
                                    System.out.println("│                          │");
                                    System.out.println("└──────────────────────────┘\u001B[0m");
                                    System.out.print("Enter Your Choice: ");
                                    int speakerChoice = input.nextInt();
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
                                            controller.organizer.get(controller.orgIndex).events
                                                    .get(eventNumber - 1).schedule
                                                    .get(secNumber - 1)
                                                    .assignSpeaker(speaker);
                                            if (!controller.organizer.get(controller.orgIndex).events
                                                    .get(eventNumber - 1).speakers.contains(speaker)) {
                                                controller.organizer.get(controller.orgIndex).events
                                                        .get(eventNumber - 1).speakers.add(speaker);
                                            }
                                        } catch (Exception e) {
                                            System.out.println("\n\u001B[91mInvalid input\u001B[0m\n");
                                        }
                                    } else if (speakerChoice == 3) {
                                        if (sess == null || sess.isEmpty()) {
                                            System.out.println(
                                                    "\u001B[91mNo sessions available to Add Speakers!\u001B[0m");
                                            continue;
                                        }
                                        input.nextLine();
                                        System.out.println(controller.displayAllSession(eventNumber - 1));
                                        System.out.print("Enter the Session Number To Update Speaker: ");
                                        try {
                                            int secNumber = input.nextInt();
                                            input.nextLine();
                                            Session session = controller.organizer.get(controller.orgIndex).events
                                                    .get(eventNumber - 1).schedule.get(secNumber - 1);

                                            if (session.speaker != null) {
                                                System.out.println("Current Speaker: " + session.speaker.name + " ("
                                                        + session.speaker.gender + ")");
                                            } else {
                                                System.out.println("No speaker assigned yet.");
                                            }

                                            System.out.print("Enter New Speaker Name: ");
                                            String speakerName = input.nextLine();
                                            System.out.print("Enter New Gender: ");
                                            String gender = input.nextLine();

                                            Speaker newSpeaker = new Speaker(speakerName, gender);
                                            session.assignSpeaker(newSpeaker);

                                            ArrayList<Speaker> eventSpeakers = controller.organizer
                                                    .get(controller.orgIndex).events.get(eventNumber - 1).speakers;
                                            if (!eventSpeakers.contains(newSpeaker)) {
                                                eventSpeakers.add(newSpeaker);
                                            }

                                            System.out.println("\n\u001B[32mSpeaker updated successfully!\u001B[0m\n");

                                        } catch (Exception e) {
                                            System.out
                                                    .println("\n\u001B[91mInvalid input. Please try again.\u001B[0m\n");
                                            input.nextLine();
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
                                            input.nextLine();
                                            Session session = controller.organizer.get(controller.orgIndex).events
                                                    .get(eventNumber - 1).schedule.get(secNumber - 1);

                                            if (session.speaker != null) {
                                                System.out.println("Removing Speaker: " + session.speaker.name + " ("
                                                        + session.speaker.gender + ")");

                                                Speaker removedSpeaker = session.speaker;
                                                session.assignSpeaker(null);

                                                ArrayList<Speaker> eventSpeakers = controller.organizer
                                                        .get(controller.orgIndex).events.get(eventNumber - 1).speakers;
                                                eventSpeakers.remove(removedSpeaker);

                                                System.out.println(
                                                        "\n\u001B[32mSpeaker deleted successfully!\u001B[0m\n");
                                            } else {
                                                System.out.println(
                                                        "\n\u001B[93mNo speaker assigned to this session.\u001B[0m\n");
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
                                    System.out.println("\u001B[33m\u001B[1m┌──────────────────────────┐");
                                    System.out.println("│                          │");
                                    System.out.println("│      \u001B[38m1. View Sponsor     \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[32m2. Add Sponsor      \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[34m3. Update Sponsor   \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[35m4. Delete Sponsor   \u001B[33m\u001B[1m│");
                                    System.out.println("│      \u001B[31m5. Exit             \u001B[33m\u001B[1m│");
                                    System.out.println("│                          │");
                                    System.out.println("└──────────────────────────┘\u001B[0m");
                                    System.out.print("Enter Your Choice: ");
                                    int sponsorChoice = input.nextInt();
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
                                                amountSponsor = input.nextInt();
                                                break;
                                            } catch (InputMismatchException e) {
                                                System.out
                                                        .println(
                                                                "\u001B[91mInvalid input. Please enter a number \u001B[0m\n");
                                                continue;
                                            }
                                        }
                                        controller.organizer.get(controller.orgIndex).events
                                                .get(eventNumber - 1).sponsors
                                                .add(new Sponsor(companyName, amountSponsor));
                                        System.out.println("Sponsor Added Sucessfully");
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
                                                                selectedSponsor.amountSponsor = amountSponsor;
                                                            }
                                                            break;
                                                        } catch (NumberFormatException e) {
                                                            System.out.println(
                                                                    "\u001B[91mInvalid input. Please enter a number.\u001B[0m");
                                                        }
                                                    }

                                                    System.out.println("Sponsor updated successfully!");
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

                                            System.out.print("Enter the Sponsor Number to Delete: ");
                                            try {
                                                int sponsorNumber = input.nextInt();
                                                input.nextLine();

                                                if (sponsorNumber < 1 || sponsorNumber > sponsors.size()) {
                                                    System.out.println("\u001B[91mInvalid Sponsor Number!\u001B[0m");
                                                } else {
                                                    Sponsor removedSponsor = sponsors.remove(sponsorNumber - 1);
                                                    System.out
                                                            .println("\u001B[32mSponsor '" + removedSponsor.companyName
                                                                    + "' deleted successfully!\u001B[0m");
                                                }
                                            } catch (InputMismatchException e) {
                                                System.out.println(
                                                        "\u001B[91mInvalid input. Please enter a valid number.\u001B[0m");
                                                input.nextLine();
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
                                controller.organizer.get(controller.orgIndex).events.get(eventNumber - 1).expenses
                                        .add(new Expense(description, amount));
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
                                for (int i = 0; i < controller.organizer.get(controller.orgIndex).events
                                        .get(eventNumber - 1).venue.capacity; i++) {
                                    controller.organizer.get(controller.orgIndex).events
                                            .get(eventNumber - 1).ticketsSold
                                            .add(new Ticket(
                                                    controller.organizer.get(controller.orgIndex).events
                                                            .get(eventNumber - 1).name,
                                                    controller.organizer.get(controller.orgIndex).events
                                                            .get(eventNumber - 1).category,
                                                    rate));
                                }

                                System.out.println(
                                        controller.organizer.get(controller.orgIndex).events
                                                .get(eventNumber - 1).ticketsSold.size()
                                                + " Tickets Allocated Sucessfully :)");
                            } else {
                                break;
                            }
                        }
                    } else if (choice == 3) {
                        controller.saveEvents(controller.organizer);
                        controller.readEvents();
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

                        Event selectedEvent = controller.organizer.get(controller.orgIndex).events
                                .get(userEventNumber - 1);

                        if (selectedEvent.ticketsSold.isEmpty()) {
                            System.out.println("\u001B[91mSorry! No Tickets Available\u001B[0m");
                        } else {
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
                                            .print("Enter The Payment Amount " + (payment.amount * noOfTicket) + " : ");
                                    int amount = input.nextInt();
                                    if (amount != (payment.amount * noOfTicket)) {
                                        System.out
                                                .println("\u001B[91mInvalid Amount\u001B[0m\n");
                                        input.nextLine();
                                        continue;
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
                                            - 1).venue.capacity = controller.organizer.get(controller.orgIndex).events
                                                    .get(0).ticketsSold.size();
                            System.out.println(
                                    "Ticket Count : "
                                            + controller.organizer.get(controller.orgIndex).events.get(0).ticketsSold
                                                    .size());
                            controller.users.get(controller.userIndex).addTicket(purchasedTicket);
                            System.out.println("\u001B[32mTicket purchased successfully!\u001B[0m");

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

        ArrayList<Speaker> speakers = organizer.get(orgIndex).events.get(eventNumber).speakers;
        if (speakers == null || speakers.isEmpty()) {
            result.append("│  --  │ No speakers available          │    --      │\n");
        }
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
                "\u001B[97m┌──────┬──────────────────────┬───────────────────────────────────────────────────────────┬──────────────────────┐\n");
        result.append(
                "│ S.No │ Time                 │ Sessions scheduled                                        │ Speaker              │\n");
        result.append(
                "├──────┼──────────────────────┼───────────────────────────────────────────────────────────┼──────────────────────┤\n");

        ArrayList<Session> sessions = organizer.get(orgIndex).events.get(eventNumber).schedule;

        if (sessions == null || sessions.isEmpty()) {
            result.append(
                    "│  --  │   --                 │ No sessions available                                     │        --            │\n");
        } else {
            int i = 0;
            for (Session s : sessions) {
                String time = s.timeRange;
                String action = s.topic;
                String owner = (s.speaker != null)
                        ? s.speaker.name + " (" + s.speaker.gender + ")"
                        : "Not Added Yet";

                result.append(String.format("│ %-5d│ %-20s │ %-57s │ %-20s │\n", i + 1, time, action, owner));
                if (i++ != sessions.size() - 1) {
                    result.append(
                            "├──────┼──────────────────────┼───────────────────────────────────────────────────────────┼──────────────────────┤\n");
                }
            }
        }

        result.append(
                "└──────┴──────────────────────┴───────────────────────────────────────────────────────────┴──────────────────────┘\u001B[0m");
        return result.toString();
    }

    String displayAllSponsor(int eventNumber) {
        StringBuilder result = new StringBuilder();

        result.append("┌──────┬───────────────────────────┬─────────────┐\n");
        result.append("│ S.No │ Company Name              │ Amount      │\n");
        result.append("├──────┼───────────────────────────┼─────────────┤\n");

        ArrayList<Sponsor> sponsors = organizer.get(orgIndex).events.get(eventNumber).sponsors;
        if (sponsors == null || sponsors.isEmpty()) {
            result.append("│  --  │ No sponsors available      │     --      │\n");
        }
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
        e.num = organizer.get(orgIndex).events.size() + 1;
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
        String result = "\033[33m███████╗ ██╗ ██╗ ███████╗ ███╗   ██╗ ████████╗ ███████╗\n██╔════╝ ██║ ██║ ██╔════╝ ████╗  ██║ ╚══██╔══╝ ██╔════╝\n█████╗   ██║ ██║ █████╗   ██╔██╗ ██║    ██║    ███████╗\n██╔══╝   ██║ ██║ ██╔══╝   ██║╚██╗██║    ██║    ╚════██║\n███████╗ ╚████╔╝ ███████╗ ██║ ╚████║    ██║    ███████║\n╚══════╝  ╚═══╝  ╚══════╝ ╚═╝ ╚═══╝     ╚═╝    ╚══════╝\n\n";
        for (Event e : organizer.get(orgIndex).events) {
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

    String displayAllBooths(int eventNumber) {
        StringBuilder result = new StringBuilder();

        result.append("┌──────┬─────────────────────────────┬──────────────┬──────────────┐\n");
        result.append("│ S.No │ Booth Name                  │ Size (sq.ft) │ Cost (₹)     │\n");
        result.append("├──────┼─────────────────────────────┼──────────────┼──────────────┤\n");

        ArrayList<Booth> booths = organizer.get(orgIndex).events.get(eventNumber).booths;

        if (booths == null || booths.isEmpty()) {
            result.append("│  --  │ No booths available         │      --      │      --      │\n");
        } else {
            for (int i = 0; i < booths.size(); i++) {
                Booth b = booths.get(i);
                String name = b.name;
                String size = String.format("%.2f", b.size);
                String cost = String.format("%.2f", b.price);

                result.append(String.format("│ %-5d│ %-27s │ %-12s │ %-12s │\n", i + 1, name, size, cost));

                if (i != booths.size() - 1) {
                    result.append("├──────┼─────────────────────────────┼──────────────┼──────────────┤\n");
                }
            }
        }

        result.append("└──────┴─────────────────────────────┴──────────────┴──────────────┘\n");

        return result.toString();
    }
}
