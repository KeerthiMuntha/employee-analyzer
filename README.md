# Employee Organizational Structure Analyzer

A Java console application that analyzes a company's organizational structure
and identifies salary and reporting line issues.

## Problem Statement
- Managers must earn at least 20% more than the average salary of their direct subordinates
- Managers must not earn more than 50% more than that average
- No employee should have more than 4 managers between them and the CEO

## Project Structure
```
src/
├── main/
│   ├── java/com/company/
│   │   ├── main/EmployeeAnalyzerApp.java   → entry point
│   │   ├── model/Employee.java             → employee data model
│   │   ├── service/Analyzer.java           → core analysis logic
│   │   ├── service/SalaryValidator.java    → salary rule checker
│   │   └── util/CsvReader.java             → CSV file reader
│   └── resources/
│       └── employees.csv                   → sample data
└── test/
└── java/com/company/
├── service/AnalyzerTest.java
└── service/SalaryValidatorTest.java
```

## How to Run
mvn clean package
java -jar target/employee-analyzer.jar

## Input Format(employees.csv)
CSV with:
```
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
300,Alice,Hasacat,50000,124
305,Brett,Hardleaf,34000,300
```

## Sample Output
```
=== Salary Analysis ===
Manager Martin Chekov earns LESS than expected by 15000.00
=== Reporting Line Analysis ===
```

## How to Run Tests
```bash
mvn test
```

## Assumptions

- CEO is identified by an empty managerId field
- Salary difference is calculated from the allowed boundary, not the average
- If no issues are found, the section is printed but remains empty
- If no file path is provided, app looks for employees.csv on the classpath

## Technologies Used
- Java SE
- JUnit 5
- Maven
