package com.miguelprojects.lab_406_Exceptions_And_Testing.Service;

import com.miguelprojects.lab_406_Exceptions_And_Testing.DTOs.EmployeeDTO;
import com.miguelprojects.lab_406_Exceptions_And_Testing.Enums.Status;
import com.miguelprojects.lab_406_Exceptions_And_Testing.model.Employee;
import com.miguelprojects.lab_406_Exceptions_And_Testing.model.Patient;
import com.miguelprojects.lab_406_Exceptions_And_Testing.repository.EmployeeRepository;
import com.miguelprojects.lab_406_Exceptions_And_Testing.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PatientRepository patientRepository;

    public Employee createEmployee (Employee employee){

        return employeeRepository.save(employee);
    }

    public void updateEmployee (Long idEmployee, EmployeeDTO employeeDTO){
        Employee employee = employeeRepository.findById(idEmployee).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));

//        if (employeeDTO.getEmployeeId() != null) {
//            employee.setEmployeeId(employeeDTO.getEmployeeId());
//        }
        if (employeeDTO.getDepartment() != null) {
            employee.setDepartment(employeeDTO.getDepartment());
        }
        if (employeeDTO.getName() != null) {
            employee.setName(employeeDTO.getName());
        }

        if (employeeDTO.getStatus() != null) {
            employee.setStatus(employeeDTO.getStatus());
        }

        employeeRepository.save(employee);
    }

    public void deleteEmployee (Long idEmployee){
        Employee employee = employeeRepository.findById(idEmployee).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee with id: "+  idEmployee + "not found"));

        for (Patient patient : patientRepository.findAll()) {
            if (patient.getDoctor().equals(employee)) {
                patient.setDoctor(null);
                System.out.println("No se puede borrar porque hay un paciente con este doctor");
                employeeRepository.deleteById(idEmployee);
                break;
            } else {
                employeeRepository.deleteById(idEmployee);
            }
        }
    }

    // Cambiar el estado del doctor
    public void changeStatus (Long idEmployee, Status status) {
        Employee doctor = employeeRepository.findById(idEmployee).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee " + idEmployee +" not found"));

        doctor.setStatus(status);
        employeeRepository.save(doctor);
    }

    // Actualizar el departamento del doctor
    public void changeDepartment (Long idEmployee, String department) {
        Employee doctor = employeeRepository.findById(idEmployee).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee " + idEmployee +" not found"));

        doctor.setDepartment(department);
        employeeRepository.save(doctor);
    }
}
