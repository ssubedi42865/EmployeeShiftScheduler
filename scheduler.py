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
add_employee("Alice", {"Mon": "Morning", "Tue": "Afternoon"})
add_employee("Bob", {"Mon": "Morning", "Wed": "Evening"})
add_employee("Charlie", {"Mon": "Evening", "Thu": "Morning"})
add_employee("David", {"Fri": "Afternoon"})
add_employee("Eve", {"Sun": "Morning"})

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

            # Try other shifts
            if not assigned:
                for shift in SHIFTS:
                    if len(schedule[day][shift]) < 2:
                        schedule[day][shift].append(employee)
                        data["schedule"][day] = shift
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
                    and e not in schedule[day][shift]
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
            print(f"  {shift}: {schedule[day][shift]}")

assign_shifts()
print_schedule()
