package com.company.service;

import com.company.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SalaryValidatorTest {

    private SalaryValidator validator;

    @BeforeEach
    void setUp() {
        validator = new SalaryValidator();
    }

    // Helper to build a manager with one subordinate
    private Employee buildManager(double managerSalary, double subordinateSalary) {
        Employee subordinate = new Employee(2, "Sub", "One", subordinateSalary, 1);
        Employee manager = new Employee(1, "John", "Doe", managerSalary, null);
        manager.getSubordinates().add(subordinate);
        return manager;
    }

    @Test
    void managerEarningWithinRangeShouldReturnNoIssue() {
        // avg = 50000, min = 60000, max = 75000 → 65000 is fine
        Employee manager = buildManager(65000, 50000);
        List<String> result = validator.validate(manager);
        assertTrue(result.isEmpty());
    }

    @Test
    void managerEarningLessThan20PercentAboveAvgShouldBeReported() {
        // avg = 50000, min = 60000 → manager earns 45000 (less by 15000)
        Employee manager = buildManager(45000, 50000);
        List<String> result = validator.validate(manager);

        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("LESS"));
        assertTrue(result.get(0).contains("15000.00"));
    }

    @Test
    void managerEarningMoreThan50PercentAboveAvgShouldBeReported() {
        // avg = 50000, max = 75000 → manager earns 80000 (more by 5000)
        Employee manager = buildManager(80000, 50000);
        List<String> result = validator.validate(manager);

        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("MORE"));
        assertTrue(result.get(0).contains("5000.00"));
    }

    @Test
    void managerEarningExactlyAtMinBoundaryShouldReturnNoIssue() {
        // avg = 50000, min = 60000 → manager earns exactly 60000
        Employee manager = buildManager(60000, 50000);
        List<String> result = validator.validate(manager);
        assertTrue(result.isEmpty());
    }

    @Test
    void managerEarningExactlyAtMaxBoundaryShouldReturnNoIssue() {
        // avg = 50000, max = 75000 → manager earns exactly 75000
        Employee manager = buildManager(75000, 50000);
        List<String> result = validator.validate(manager);
        assertTrue(result.isEmpty());
    }

    @Test
    void managerWithMultipleSubordinatesShouldUseAverageSalary() {
        // avg = (30000+50000) / 2 = 40000, min = 48000, max = 60000
        Employee sub1 = new Employee(2, "Sub", "One", 30000, 1);
        Employee sub2 = new Employee(3, "Sub", "Two", 50000, 1);
        Employee manager = new Employee(1, "John", "Doe", 45000, null);
        manager.getSubordinates().add(sub1);
        manager.getSubordinates().add(sub2);

        List<String> result = validator.validate(manager);

        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("LESS"));
        assertTrue(result.get(0).contains("3000.00")); // 48000 - 45000
    }
}