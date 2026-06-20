import java.util.*;

public class ShiftScheduler {

    static String[] DAYS = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
    static String[] SHIFTS = { "Morning", "Afternoon", "Evening" };

    static class Employee {
        String name;
        Map<String, String> preferences = new HashMap<>();
        Map<String, String> schedule = new HashMap<>();
        int daysWorked = 0;

        Employee(String name) {
            this.name = name;
        }
    }

    static Map<String, Employee> employees = new HashMap<>();
    static Map<String, Map<String, List<String>>> schedule = new HashMap<>();

    public static void main(String[] args) {

        initSchedule();

        // Example input data
        addEmployee("Alice", Map.of("Mon", "Morning", "Tue", "Afternoon", "Wed", "Evening"));
        addEmployee("Bob", Map.of("Mon", "Morning", "Wed", "Evening", "Fri", "Morning"));
        addEmployee("Charlie", Map.of("Mon", "Evening", "Thu", "Morning", "Sat", "Afternoon"));
        addEmployee("David", Map.of("Tue", "Morning", "Fri", "Afternoon", "Sun", "Evening"));
        addEmployee("Eve", Map.of("Sun", "Morning", "Wed", "Afternoon", "Thu", "Evening"));
        addEmployee("Frank", Map.of("Mon", "Afternoon", "Tue", "Evening", "Sat", "Morning"));
        addEmployee("Grace", Map.of("Wed", "Morning", "Thu", "Afternoon", "Sun", "Afternoon"));
        addEmployee("Henry", Map.of("Tue", "Afternoon", "Fri", "Evening", "Sat", "Evening"));
        addEmployee("Ivy", Map.of("Mon", "Evening", "Thu", "Morning", "Sun", "Morning"));

        assignShifts();
        printSchedule();
    }

    static void initSchedule() {
        for (String d : DAYS) {
            schedule.put(d, new HashMap<>());

            for (String s : SHIFTS) {
                schedule.get(d).put(s, new ArrayList<>());
            }
        }
    }

    static void addEmployee(String name, Map<String, String> prefs) {
        Employee e = new Employee(name);
        e.preferences.putAll(prefs);
        employees.put(name, e);
    }

    // -------------------------
    // SCHEDULING LOGIC
    // -------------------------
    static void assignShifts() {

        for (String day : DAYS) {

            for (Employee e : employees.values()) {

                if (e.daysWorked >= 5)
                    continue;

                if (e.schedule.containsKey(day))
                    continue;

                String preferred = e.preferences.get(day);
                boolean assigned = false;

                // Try preferred shift first
                if (preferred != null &&
                        schedule.get(day).get(preferred).size() < 2) {

                    schedule.get(day).get(preferred).add(e.name);
                    e.schedule.put(day, preferred);
                    e.daysWorked++;
                    assigned = true;
                }

                // Try other shifts if preferred shift is full
                if (!assigned && preferred != null) {

                    for (String shift : SHIFTS) {

                        if (schedule.get(day).get(shift).size() < 2) {

                            schedule.get(day).get(shift).add(e.name);
                            e.schedule.put(day, shift);
                            e.daysWorked++;
                            assigned = true;
                            break;
                        }
                    }
                }

                // Try next day if no shift is available
                if (!assigned && preferred != null) {

                    int currentDay = Arrays.asList(DAYS).indexOf(day);

                    if (currentDay < DAYS.length - 1) {

                        String nextDay = DAYS[currentDay + 1];

                        if (!e.schedule.containsKey(nextDay)
                                && e.daysWorked < 5) {

                            for (String shift : SHIFTS) {

                                if (schedule.get(nextDay).get(shift).size() < 2) {

                                    schedule.get(nextDay).get(shift).add(e.name);
                                    e.schedule.put(nextDay, shift);
                                    e.daysWorked++;
                                    assigned = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        // Fill missing slots
        fillShortages();
    }

    // -------------------------
    // FILL SHORTAGES
    // -------------------------
    static void fillShortages() {

        Random rand = new Random();

        for (String day : DAYS) {

            for (String shift : SHIFTS) {

                while (schedule.get(day).get(shift).size() < 2) {

                    List<String> candidates = new ArrayList<>();

                    for (Employee e : employees.values()) {

                        if (e.daysWorked < 5
                                && !e.schedule.containsKey(day)) {

                            candidates.add(e.name);
                        }
                    }

                    if (candidates.isEmpty())
                        break;

                    String chosen = candidates.get(rand.nextInt(candidates.size()));

                    schedule.get(day).get(shift).add(chosen);
                    employees.get(chosen).daysWorked++;
                    employees.get(chosen).schedule.put(day, shift);
                }
            }
        }
    }

    // -------------------------
    // OUTPUT
    // -------------------------
    static void printSchedule() {

        for (String day : DAYS) {

            System.out.println("\n" + day);

            for (String shift : SHIFTS) {

                System.out.println(
                        " " + shift + ": " +
                                schedule.get(day).get(shift));
            }
        }

        System.out.println("\nEmployee Work Summary");
        System.out.println("=====================");

        for (Employee e : employees.values()) {

            System.out.println(
                    e.name + ": " +
                            e.daysWorked + " days");
        }
    }
}