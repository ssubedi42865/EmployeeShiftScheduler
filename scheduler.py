import random

DAYS = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]
SHIFTS = ["Morning", "Afternoon", "Evening"]

employees = {}
schedule = {day: {shift: [] for shift in SHIFTS} for day in DAYS}

# -------------------------
# INPUT SECTION
# -------------------------
def add_employee(name, preferences):
    employees[name] = {
        "preferences": preferences,
        "days_worked": 0,
        "schedule": {}
    }

# Example input data
add_employee("Alice", {"Mon": "Morning", "Tue": "Afternoon", "Wed": "Evening"})
add_employee("Bob", {"Mon": "Morning", "Wed": "Evening", "Fri": "Morning"})
add_employee("Charlie", {"Mon": "Evening", "Thu": "Morning", "Sat": "Afternoon"})
add_employee("David", {"Tue": "Morning", "Fri": "Afternoon", "Sun": "Evening"})
add_employee("Eve", {"Sun": "Morning", "Wed": "Afternoon", "Thu": "Evening"})
add_employee("Frank", {"Mon": "Afternoon", "Tue": "Evening", "Sat": "Morning"})
add_employee("Grace", {"Wed": "Morning", "Thu": "Afternoon", "Sun": "Afternoon"})
add_employee("Henry", {"Tue": "Afternoon", "Fri": "Evening", "Sat": "Evening"})
add_employee("Ivy", {"Mon": "Evening", "Thu": "Morning", "Sun": "Morning"})

# -------------------------
# SCHEDULING LOGIC
# -------------------------
def assign_shifts():
    for day in DAYS:
        for employee, data in employees.items():
            if data["days_worked"] >= 5:
                continue

            if day in data["schedule"]:
                continue

            preferred = data["preferences"].get(day, None)
            assigned = False

            # Try preferred shift first
            if preferred and len(schedule[day][preferred]) < 2:
                schedule[day][preferred].append(employee)
                data["schedule"][day] = preferred
                data["days_worked"] += 1
                assigned = True

            # Try other shifts on the same day if preferred shift is full
            if not assigned and preferred:
                for shift in SHIFTS:
                    if len(schedule[day][shift]) < 2:
                        schedule[day][shift].append(employee)
                        data["schedule"][day] = shift
                        data["days_worked"] += 1
                        assigned = True
                        break

            # Try next day if conflict cannot be solved on the same day
            if not assigned and preferred:
                current_day_index = DAYS.index(day)

                if current_day_index < len(DAYS) - 1:
                    next_day = DAYS[current_day_index + 1]

                    if next_day not in data["schedule"] and data["days_worked"] < 5:
                        for shift in SHIFTS:
                            if len(schedule[next_day][shift]) < 2:
                                schedule[next_day][shift].append(employee)
                                data["schedule"][next_day] = shift
                                data["days_worked"] += 1
                                assigned = True
                                break

    # Fill missing slots
    fill_shortages()

# -------------------------
# FILL SHORTAGES
# -------------------------
def fill_shortages():
    for day in DAYS:
        for shift in SHIFTS:
            while len(schedule[day][shift]) < 2:
                candidates = [
                    e for e in employees
                    if employees[e]["days_worked"] < 5
                    and day not in employees[e]["schedule"]
                ]

                if not candidates:
                    break

                chosen = random.choice(candidates)
                schedule[day][shift].append(chosen)
                employees[chosen]["days_worked"] += 1
                employees[chosen]["schedule"][day] = shift

# -------------------------
# OUTPUT
# -------------------------
def print_schedule():
    for day in DAYS:
        print(f"\n{day}")
        for shift in SHIFTS:
            print(f" {shift}: {schedule[day][shift]}")

assign_shifts()
print_schedule()