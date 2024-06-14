package com.miguelprojects.lab_406_Exceptions_And_Testing.repository;

import com.miguelprojects.lab_406_Exceptions_And_Testing.Enums.Status;
import com.miguelprojects.lab_406_Exceptions_And_Testing.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByStatus(Status status);
    List<Employee> findByDepartment(String department);

}
