package org.company;

import java.time.LocalDate;

public class Employee {
    private long employeeId;
    private String firstName;
    private String lastName;
    private LocalDate employmentDate;
    private Double yearlySalary; // Using Double for currency
    private PensionPlan pensionPlan; // Can be null if not yet enrolled

    // All-args constructor
    public Employee(long employeeId, String firstName, String lastName,
                    LocalDate employmentDate, Double yearlySalary, PensionPlan pensionPlan) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.employmentDate = employmentDate;
        this.yearlySalary = yearlySalary;
        this.pensionPlan = pensionPlan;
    }

    // Constructor without PensionPlan
    public Employee(long employeeId, String firstName, String lastName,
                    LocalDate employmentDate, Double yearlySalary) {
        this(employeeId, firstName, lastName, employmentDate, yearlySalary, null);
    }

    // Default constructor (Needed by Jackson)
    public Employee() {
    }

    // Helper method to check if the employee is enrolled
    public boolean isEnrolled() {
        return this.pensionPlan != null;
    }

    // Getters and Setters
    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }

    public Double getYearlySalary() {
        return yearlySalary;
    }

    public void setYearlySalary(Double yearlySalary) {
        this.yearlySalary = yearlySalary;
    }

    public PensionPlan getPensionPlan() {
        return pensionPlan;
    }

    public void setPensionPlan(PensionPlan pensionPlan) {
        this.pensionPlan = pensionPlan;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", employmentDate=" + employmentDate +
                ", yearlySalary=" + yearlySalary +
                ", pensionPlan=" + (pensionPlan != null ? pensionPlan.getPlanReferenceNumber() : "None") +
                '}';
    }
}