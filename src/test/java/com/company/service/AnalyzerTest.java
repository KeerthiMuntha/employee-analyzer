package com.company.service;

import com.company.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AnalyzerTest {

    // Helper to build employee map directly (no CSV needed)
    private Map<Integer, Employee> buildEmployees(Employee... employees) {
        Map<Integer, Employee> map = new HashMap<>();
        for (Employee e : employees) {
            map.put(e.getId(), e);
        }
        return map;
    }

    @Test
    void managerEarningLessShouldBeInSalaryReport() {
        // avg subordinate = 50000, min = 60000, Martin earns 45000
        Employee ceo     = new Employee(1, "Joe",    "Doe",     60000, null);
        Employee martin  = new Employee(2, "Martin", "Chekov",  45000, 1);
        Employee alice   = new Employee(3, "Alice",  "Hasacat", 50000, 2);

        Analyzer analyzer = new Analyzer(buildEmployees(ceo, martin, alice));
        List<String> result = analyzer.analyzeSalaries();

        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("Martin Chekov"));
        assertTrue(result.get(0).contains("LESS"));
    }

    @Test
    void managerEarningMoreShouldBeInSalaryReport() {
        // avg subordinate = 30000, max = 45000, manager earns 60000
        Employee ceo     = new Employee(1, "Joe",  "Doe",   80000, null);
        Employee manager = new Employee(2, "Rich", "Boss",  60000, 1);
        Employee sub     = new Employee(3, "Low",  "Paid",  30000, 2);

        Analyzer analyzer = new Analyzer(buildEmployees(ceo, manager, sub));
        List<String> result = analyzer.analyzeSalaries();

        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("Rich Boss"));
        assertTrue(result.get(0).contains("MORE"));
    }

    @Test
    void employeeWithinFourManagersShouldNotBeReported() {
        // CEO → M1 → M2 → M3 → Employee = 3 managers, fine
        Employee ceo  = new Employee(1, "CEO", "One",  90000, null);
        Employee m1   = new Employee(2, "M1",  "Two",  70000, 1);
        Employee m2   = new Employee(3, "M2",  "Three",60000, 2);
        Employee m3   = new Employee(4, "M3",  "Four", 50000, 3);
        Employee emp  = new Employee(5, "Emp", "Five", 40000, 4);

        Analyzer analyzer = new Analyzer(buildEmployees(ceo, m1, m2, m3, emp));
        List<String> result = analyzer.analyzeReportingLines();

        assertTrue(result.isEmpty());
    }

    @Test
    void employeeWithMoreThanFourManagersShouldBeReported() {
        // CEO → M1 → M2 → M3 → M4 → Employee = 5 managers, too long by 1
        Employee ceo  = new Employee(1, "CEO", "One",   90000, null);
        Employee m1   = new Employee(2, "M1",  "Two",   70000, 1);
        Employee m2   = new Employee(3, "M2",  "Three", 60000, 2);
        Employee m3   = new Employee(4, "M3",  "Four",  50000, 3);
        Employee m4   = new Employee(5, "M4",  "Five",  45000, 4);
        Employee emp  = new Employee(6, "Emp", "Six",   40000, 5);

        Analyzer analyzer = new Analyzer(buildEmployees(ceo, m1, m2, m3, m4, emp));
        List<String> result = analyzer.analyzeReportingLines();

        assertEquals(1, result.size());
        assertTrue(result.get(0).contains("Emp Six"));
        assertTrue(result.get(0).contains("1 levels"));
    }

    @Test
    void ceoShouldNeverBeReportedForReportingLine() {
        Employee ceo = new Employee(1, "Joe", "Doe", 90000, null);
        Analyzer analyzer = new Analyzer(buildEmployees(ceo));
        List<String> result = analyzer.analyzeReportingLines();
        assertTrue(result.isEmpty());
    }

    @Test
    void noIssuesShouldReturnEmptyReports() {
        // Their exact CSV — only Martin should be flagged for salary
        Employee ceo    = new Employee(123, "Joe",    "Doe",     60000, null);
        Employee martin = new Employee(124, "Martin", "Chekov",  45000, 123);
        Employee bob    = new Employee(125, "Bob",    "Ronstad", 47000, 123);
        Employee alice  = new Employee(300, "Alice",  "Hasacat", 50000, 124);
        Employee brett  = new Employee(305, "Brett",  "Hardleaf",34000, 300);

        Analyzer analyzer = new Analyzer(buildEmployees(ceo, martin, bob, alice, brett));

        List<String> salaries = analyzer.analyzeSalaries();
        List<String> lines    = analyzer.analyzeReportingLines();

        assertEquals(1, salaries.size()); // only Martin
        assertTrue(lines.isEmpty());      // no deep lines
    }
}