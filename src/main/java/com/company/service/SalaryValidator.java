package com.company.service;
import com.company.model.Employee;

import java.util.ArrayList;
import java.util.List;

public class SalaryValidator {

    private static final double MIN_FACTOR = 1.2;
    private static final double MAX_FACTOR = 1.5;

    public List<String> validate(Employee manager) {
        List<String> result = new ArrayList<>();

        double avg = manager.getSubordinates()
                .stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0);

        double min = avg * MIN_FACTOR;
        double max = avg * MAX_FACTOR;

        if (manager.getSalary() < min) {
            result.add(String.format("Manager %s earns LESS than expected by %.2f",
                    manager.getFullName(), (min - manager.getSalary())));
        } else if (manager.getSalary() > max) {
            result.add(String.format("Manager %s earns MORE than expected by %.2f",
                    manager.getFullName(), (manager.getSalary() - max)));
        }

        return result;
    }
}