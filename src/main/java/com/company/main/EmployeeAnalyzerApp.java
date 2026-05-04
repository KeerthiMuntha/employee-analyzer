package com.company.main;


import com.company.model.Employee;
import com.company.service.Analyzer;
import com.company.util.CsvReader;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
public class EmployeeAnalyzerApp {

    public static void main(String[] args) throws Exception {

        String filePath = args.length > 0 ? args[0] : "employees.csv";

        CsvReader reader = new CsvReader();
        Map<Integer, Employee> employees = reader.read(filePath);

        Analyzer analyzer = new Analyzer(employees);

        System.out.println("=== Salary Analysis ===");
        List<String> salaryIssues = analyzer.analyzeSalaries();
        salaryIssues.forEach(System.out::println);

        System.out.println("\n=== Reporting Line Analysis ===");
        List<String> reportingIssues = analyzer.analyzeReportingLines();
        reportingIssues.forEach(System.out::println);
    }
}