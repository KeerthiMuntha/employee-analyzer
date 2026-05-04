package com.company.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import com.company.model.Employee;

public class CsvReader{
	public Map<Integer, Employee> read(String filePath) throws IOException {
	        Map<Integer, Employee> employees = new HashMap<>();

	        InputStream inputStream = getClass()
	                .getClassLoader()
	                .getResourceAsStream(filePath);

	        if (inputStream == null) {
	            throw new IllegalArgumentException("File not found: " + filePath);
	        }

	        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

	            br.readLine();
	            String line;

	            while ((line = br.readLine()) != null) {

	                if (line.trim().isEmpty()) continue;

	                String[] parts = line.split(",");

	                if (parts.length < 4) continue;

	                int id = Integer.parseInt(parts[0].trim());
	                String firstName = parts[1].trim();
	                String lastName = parts[2].trim();
	                double salary = Double.parseDouble(parts[3].trim());

	                Integer managerId = (parts.length > 4 && !parts[4].trim().isEmpty())
	                        ? Integer.parseInt(parts[4].trim())
	                        : null;

	                Employee emp = new Employee(id, firstName, lastName, salary, managerId);
	                employees.put(id, emp);
	            }
	        }

	        return employees;
	    }
	}
		
