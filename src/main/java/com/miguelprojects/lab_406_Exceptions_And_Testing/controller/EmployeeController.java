package com.miguelprojects.lab_406_Exceptions_And_Testing.controller;

import com.miguelprojects.lab_406_Exceptions_And_Testing.DTOs.EmployeeDTO;
import com.miguelprojects.lab_406_Exceptions_And_Testing.Enums.Status;
import com.miguelprojects.lab_406_Exceptions_And_Testing.Service.EmployeeService;
import com.miguelprojects.lab_406_Exceptions_And_Testing.model.Employee;
import com.miguelprojects.lab_406_Exceptions_And_Testing.repository.EmployeeRepository;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;


//    Obtener todos los médicos (doctors): Crear una ruta para obtener todos los médicos.
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

//    Obtener médico por ID: Crear una ruta para obtener un médico por employee_id.
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id).orElseThrow
                (() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

//    Obtener médicos por estado (status): Crear una ruta para obtener médicos por status.
    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getEmployeeByStatus(@RequestParam Status status) {
        return employeeRepository.findByStatus(status);
    }

//    Obtener médicos por departamento: Crear una ruta para obtener médicos por department.
    @GetMapping("/department")
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getEmployeeByDepartment(@RequestParam String department) {
        return employeeRepository.findByDepartment(department);
    }

    // Añadir nuevo doctor
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    // Actualizar la información del doctor:
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEmployee(@PathVariable(name = "id") Long employeeId, @RequestBody EmployeeDTO employeeDTO) {
        employeeService.updateEmployee(employeeId, employeeDTO);
    }

    // Cambiar el estado del doctor
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeStatus(@PathVariable(name = "id") Long idEmployee, @RequestParam Status status) {
        employeeService.changeStatus(idEmployee, status);
    }

    // Cambiar el departamento del doctor
    @PatchMapping("/{id}/department")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeDepartment(@PathVariable(name = "id") Long idEmployee, @RequestParam String department) {
        employeeService.changeDepartment(idEmployee, department);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

}
