package com.company.service;

import com.company.model.Employee;

import java.util.*;

public class Analyzer {

    private Map<Integer, Employee> employees;

    public Analyzer(Map<Integer, Employee> employees) {
        this.employees = employees;
        buildHierarchy();
    }

    private void buildHierarchy() {
        for (Employee emp : employees.values()) {
            if (emp.getManagerId() != null) {
                Employee manager = employees.get(emp.getManagerId());
                if (manager != null) {
                    manager.getSubordinates().add(emp);
                }
            }
        }
    }

    public List<String> analyzeSalaries() {
        List<String> result = new ArrayList<>();
        SalaryValidator validator = new SalaryValidator();

        for (Employee emp : employees.values()) {
            if (emp.isManager()) {
                result.addAll(validator.validate(emp));
            }
        }

        return result;
    }

    public List<String> analyzeReportingLines() {
        List<String> result = new ArrayList<>();

        for (Employee emp : employees.values()) {
            int depth = getDepth(emp);

            if (depth > 4) {
                result.add(String.format("Employee %s has too long reporting line by %d levels",
                        emp.getFullName(), depth - 4));
            }
        }

        return result;
    }

    private int getDepth(Employee emp) {
        int depth = 0;
        Integer managerId = emp.getManagerId();

        while (managerId != null) {
            depth++;
            Employee manager = employees.get(managerId);
            managerId = manager != null ? manager.getManagerId() : null;
        }

        return depth;
    }
}