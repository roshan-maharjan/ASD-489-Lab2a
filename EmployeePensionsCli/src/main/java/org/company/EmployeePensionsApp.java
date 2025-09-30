package org.company;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeePensionsApp {

    private final List<Employee> employees = new ArrayList<>();
    private final ObjectMapper objectMapper;

    public EmployeePensionsApp() {
        // Configure Jackson ObjectMapper for pretty printing and LocalDate support
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        loadInitialData();
    }

    /**
     * Loads the initial Employee and PensionPlan data into the in-memory list.
     */
    private void loadInitialData() {
        // Data from the problem statement
        PensionPlan pp1 = new PensionPlan("EX1089", LocalDate.of(2023, 1, 17), 100.00);
        PensionPlan pp3 = new PensionPlan("SM2307", LocalDate.of(2017, 5, 17), 1555.50);

        employees.add(new Employee(1, "Daniel", "Agar", LocalDate.of(2023, 1, 17), 105945.50, pp1));

        // Employee 2: Bernard Shaw - NOT ENROLLED (pensionPlan is null)
        employees.add(new Employee(2, "Bernard", "Shaw", LocalDate.of(2022, 9, 3), 197750.00));

        employees.add(new Employee(3, "Carly", "Agar", LocalDate.of(2014, 5, 16), 842000.75, pp3));

        // Employee 4: Wesley Schneider - NOT ENROLLED
        employees.add(new Employee(4, "Wesley", "Schneider", LocalDate.of(2023, 7, 21), 74500.00));

        // Employee 5: Anna Wiltord - NOT ENROLLED
        employees.add(new Employee(5, "Anna", "Wiltord", LocalDate.of(2023, 3, 15), 85750.00));

        // Employee 6: Yosef Tesfalem - NOT ENROLLED
        employees.add(new Employee(6, "Yosef", "Tesfalem", LocalDate.of(2024, 10, 31), 100000.00));
    }

    // --- Task 1: Print All Employees Report ---

    /**
     * Prints the list of all Employees, sorted and formatted as JSON.
     * Sort: Descending by Yearly Salary, then Ascending by Last Name.
     */
    public void printAllEmployeesReport() {
        System.out.println("=======================================================================");
        System.out.println("FEATURE 1: All Employees Report (Sorted and in JSON)");
        System.out.println("=======================================================================");

        // 1. Define the Comparator for sorting
        Comparator<Employee> employeeComparator = Comparator
                // Primary Sort: Descending by Yearly Salary
                .comparing(Employee::getYearlySalary, Comparator.reverseOrder())
                // Secondary Sort: Ascending by Last Name
                .thenComparing(Employee::getLastName);

        // 2. Apply the sort
        List<Employee> sortedEmployees = employees.stream()
                .sorted(employeeComparator)
                .collect(Collectors.toList());

        // 3. Print the list as JSON
        try {
            String jsonOutput = objectMapper.writeValueAsString(sortedEmployees);
            System.out.println(jsonOutput);
        } catch (Exception e) {
            System.err.println("Error generating JSON report: " + e.getMessage());
        }
    }

    // --- Task 2: Quarterly Upcoming Enrollees Report ---

    /**
     * Calculates the start date (first day) and end date (last day) of the next calendar quarter.
     * @param currentDate The current date to base the calculation on.
     * @return A list containing [Quarter Start Date, Quarter End Date].
     */
    private List<LocalDate> getNextQuarterRange(LocalDate currentDate) {
        // Determine the current quarter
        int currentQuarter = currentDate.get(IsoFields.QUARTER_OF_YEAR);

        // Determine the start month of the next quarter
        int nextQuarterStartMonth = 1; // Default to Q1 (Jan)
        int nextQuarterYear = currentDate.getYear();

        switch (currentQuarter) {
            case 1: // Current is Q1 (Jan-Mar), Next is Q2 (Apr-Jun)
                nextQuarterStartMonth = 4;
                break;
            case 2: // Current is Q2 (Apr-Jun), Next is Q3 (Jul-Sep)
                nextQuarterStartMonth = 7;
                break;
            case 3: // Current is Q3 (Jul-Sep), Next is Q4 (Oct-Dec)
                nextQuarterStartMonth = 10;
                break;
            case 4: // Current is Q4 (Oct-Dec), Next is Q1 (Jan-Mar) of the next year
                nextQuarterStartMonth = 1;
                nextQuarterYear++;
                break;
        }

        LocalDate quarterStart = LocalDate.of(nextQuarterYear, nextQuarterStartMonth, 1);
        // The end date is the day before the start of the quarter after the next one.
        LocalDate quarterEnd = quarterStart.plus(3, ChronoUnit.MONTHS).minusDays(1);

        return List.of(quarterStart, quarterEnd);
    }

    /**
     * Prints the list of Quarterly Upcoming Enrollees, sorted and formatted as JSON.
     * Criteria: Not enrolled AND will reach 3 years of employment in the next quarter.
     * Sort: Descending by Employment Date.
     */
    public void printQuarterlyUpcomingEnrolleesReport() {
        System.out.println("\n=======================================================================");
        System.out.println("FEATURE 2: Quarterly Upcoming Enrollees Report (Sorted and in JSON)");
        System.out.println("=======================================================================");

        // For this exercise, we'll use a fixed 'current date' of 2025-09-30 (The provided
        // date in the problem context which has an effect on the next quarter calculation).
        // In a real system, you would use LocalDate.now().
        LocalDate fixedCurrentDate = LocalDate.of(2025, 9, 30);

        // The problem statement was dated October 2025, implying a potential 'next quarter'
        // calculation might start from Q4 2025 to Q1 2026.
        // If current date is 2025-09-30 (Q3), next quarter is Q4 (Oct 1 to Dec 31, 2025).
        List<LocalDate> quarterRange = getNextQuarterRange(fixedCurrentDate);
        LocalDate quarterStart = quarterRange.get(0);
        LocalDate quarterEnd = quarterRange.get(1);

        System.out.printf("Current Date Used: %s%n", fixedCurrentDate);
        System.out.printf("Next Quarter Range: %s to %s%n", quarterStart, quarterEnd);
        System.out.println("---");


        // The qualification date is 3 years of service.
        // Qualification for *enrollment* must happen *on or between* quarterStart and quarterEnd.
        List<Employee> upcomingEnrollees = employees.stream()
                .filter(e -> !e.isEnrolled()) // 1. Must NOT yet be enrolled
                .filter(e -> {
                    LocalDate qualificationDate = e.getEmploymentDate().plus(3, ChronoUnit.YEARS);
                    // 2. Qualification date must be ON or AFTER the quarter start date
                    boolean isAfterStart = !qualificationDate.isBefore(quarterStart);
                    // 3. Qualification date must be ON or BEFORE the quarter end date
                    boolean isBeforeEnd = !qualificationDate.isAfter(quarterEnd);

                    return isAfterStart && isBeforeEnd;
                })
                // 4. Sort: Descending by Employment Date
                .sorted(Comparator.comparing(Employee::getEmploymentDate, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // Print the list as JSON
        try {
            String jsonOutput = objectMapper.writeValueAsString(upcomingEnrollees);
            System.out.println(jsonOutput);
        } catch (Exception e) {
            System.err.println("Error generating JSON report: " + e.getMessage());
        }
    }
}