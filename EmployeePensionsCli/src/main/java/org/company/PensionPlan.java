package org.company;

import java.time.LocalDate;

public class PensionPlan {
    private String planReferenceNumber;
    private LocalDate enrollmentDate;
    private Double monthlyContribution; // Using Double for currency

    // All-args constructor
    public PensionPlan(String planReferenceNumber, LocalDate enrollmentDate, Double monthlyContribution) {
        if (planReferenceNumber == null || planReferenceNumber.isEmpty()) {
            throw new IllegalArgumentException("Pension Plan must have a reference number.");
        }
        this.planReferenceNumber = planReferenceNumber;
        this.enrollmentDate = enrollmentDate;
        this.monthlyContribution = monthlyContribution;
    }

    // Default constructor (Needed by Jackson)
    public PensionPlan() {
    }

    // Getters and Setters
    public String getPlanReferenceNumber() {
        return planReferenceNumber;
    }

    public void setPlanReferenceNumber(String planReferenceNumber) {
        this.planReferenceNumber = planReferenceNumber;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public Double getMonthlyContribution() {
        return monthlyContribution;
    }

    public void setMonthlyContribution(Double monthlyContribution) {
        this.monthlyContribution = monthlyContribution;
    }

    @Override
    public String toString() {
        return "PensionPlan{" +
                "planReferenceNumber='" + planReferenceNumber + '\'' +
                ", enrollmentDate=" + enrollmentDate +
                ", monthlyContribution=" + monthlyContribution +
                '}';
    }
}