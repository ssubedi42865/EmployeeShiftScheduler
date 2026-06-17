import java.util.*;

public class ShiftScheduler {

    static String[] DAYS = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
    static String[] SHIFTS = {"Morning","Afternoon","Evening"};

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

        addEmployee("Alice", Map.of("Mon","Morning","Tue","Afternoon"));
        addEmployee("Bob", Map.of("Mon","Morning","Wed","Evening"));
        addEmployee("Charlie", Map.of("Mon","Evening","Thu","Morning"));
        addEmployee("David", Map.of("Fri","Afternoon"));
        addEmployee("Eve", Map.of("Sun","Morning"));

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

    static void addEmployee(String name, Map<String,String> prefs) {
        Employee e = new Employee(name);
        e.preferences.putAll(prefs);
        employees.put(name, e);
    }

    static void assignShifts() {

        for (String day : DAYS) {
            for (Employee e : employees.values()) {

                if (e.daysWorked >= 5) continue;
                if (e.schedule.containsKey(day)) continue;

                String preferred = e.preferences.get(day);
                boolean assigned = false;

                if (preferred != null &&
                        schedule.get(day).get(preferred).size() < 2) {

                    schedule.get(day).get(preferred).add(e.name);
                    e.schedule.put(day, preferred);
                    e.daysWorked++;
                    assigned = true;
                }

                if (!assigned) {
                    for (String shift : SHIFTS) {
                        if (schedule.get(day).get(shift).size() < 2) {
                            schedule.get(day).get(shift).add(e.name);
                            e.schedule.put(day, shift);
                            e.daysWorked++;
                            break;
                        }
                    }
                }
            }
        }

        fillShortages();
    }

    static void fillShortages() {
        Random rand = new Random();

        for (String day : DAYS) {
            for (String shift : SHIFTS) {
                while (schedule.get(day).get(shift).size() < 2) {

                    List<String> candidates = new ArrayList<>();

                    for (Employee e : employees.values()) {
                        if (e.daysWorked < 5 &&
                            !schedule.get(day).get(shift).contains(e.name)) {
                            candidates.add(e.name);
                        }
                    }

                    if (candidates.isEmpty()) break;

                    String chosen = candidates.get(rand.nextInt(candidates.size()));

                    schedule.get(day).get(shift).add(chosen);
                    employees.get(chosen).daysWorked++;
                    employees.get(chosen).schedule.put(day, shift);
                }
            }
        }
    }

    static void printSchedule() {
        for (String day : DAYS) {
            System.out.println("\n" + day);
            for (String shift : SHIFTS) {
                System.out.println("  " + shift + ": " + schedule.get(day).get(shift));
            }
        }
    }
}
