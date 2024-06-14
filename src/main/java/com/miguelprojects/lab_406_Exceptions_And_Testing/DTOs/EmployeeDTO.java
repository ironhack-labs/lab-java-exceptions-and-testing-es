package com.miguelprojects.lab_406_Exceptions_And_Testing.DTOs;

import com.miguelprojects.lab_406_Exceptions_And_Testing.Enums.Status;
import jakarta.persistence.*;

public class EmployeeDTO {

    private Long employeeId;

    private String department;

    private String name;

    @Enumerated(EnumType.STRING)
    private Status status;

    public EmployeeDTO() {    }

    public EmployeeDTO(Long employeeId, String department, String name, Status status) {
        this.employeeId = employeeId;
        this.department = department;
        this.name = name;
        this.status = status;
    }

    public EmployeeDTO(String department, String name, Status status) {
        this.department = department;
        this.name = name;
        this.status = status;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
