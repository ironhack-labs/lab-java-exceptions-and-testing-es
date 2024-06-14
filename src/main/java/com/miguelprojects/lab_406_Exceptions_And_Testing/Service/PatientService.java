package com.miguelprojects.lab_406_Exceptions_And_Testing.Service;

import com.miguelprojects.lab_406_Exceptions_And_Testing.model.Employee;
import com.miguelprojects.lab_406_Exceptions_And_Testing.model.Patient;
import com.miguelprojects.lab_406_Exceptions_And_Testing.DTOs.PatientDTO;
import com.miguelprojects.lab_406_Exceptions_And_Testing.repository.EmployeeRepository;
import com.miguelprojects.lab_406_Exceptions_And_Testing.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Patient createPatient (Patient patient){
        Employee doctor = employeeRepository.findById(patient.getDoctor().getEmployeeId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee" + patient.getDoctor().getName() +" not found"));

        Patient newPatient = new Patient();
        newPatient.setName(patient.getName());
        newPatient.setDateOfBirth(patient.getDateOfBirth());
        newPatient.setDoctor(doctor);

        return patientRepository.save(newPatient);
    }

    // Actualizar la información del paciente: Crea una ruta para actualizar
    // la información del paciente (el usuario debería poder actualizar
    // cualquier información del paciente a través de esta ruta).
    public void updatePatient (Long idPatient, PatientDTO patientDTO){
        Employee doctor = employeeRepository.findById(patientDTO.getAdmittedBy()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee " + patientDTO.getAdmittedBy() +" not found"));
        Patient patient = patientRepository.findById(idPatient).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient " + patientDTO.getName() +" not found"));

        if (patientDTO.getName() != null) {
            patient.setName(patientDTO.getName());
        }
        if (patientDTO.getDateOfBirth() != null) {
            patient.setDateOfBirth(patientDTO.getDateOfBirth());
        }
        if (patientDTO.getAdmittedBy() != null) {
            patient.setDoctor(doctor);
        }

        patientRepository.save(patient);
    }

    public void deletePatient (Long idPatient){
        Patient patient = patientRepository.findById(idPatient).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient with id: "+  idPatient + "not found"));
        patientRepository.deleteById(idPatient);
    }


}
