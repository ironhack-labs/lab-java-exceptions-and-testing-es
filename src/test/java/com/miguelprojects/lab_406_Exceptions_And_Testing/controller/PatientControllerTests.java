package com.miguelprojects.lab_406_Exceptions_And_Testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miguelprojects.lab_406_Exceptions_And_Testing.DTOs.EmployeeDTO;
import com.miguelprojects.lab_406_Exceptions_And_Testing.DTOs.PatientDTO;
import com.miguelprojects.lab_406_Exceptions_And_Testing.model.Employee;
import com.miguelprojects.lab_406_Exceptions_And_Testing.model.Patient;
import com.miguelprojects.lab_406_Exceptions_And_Testing.repository.EmployeeRepository;
import com.miguelprojects.lab_406_Exceptions_And_Testing.repository.PatientRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.net.BindException;
import java.time.LocalDate;
import java.util.List;

import static com.miguelprojects.lab_406_Exceptions_And_Testing.Enums.Status.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class PatientControllerTests {


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private List<Patient> patients;
    private List<Employee> employees;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper.registerModule(new JavaTimeModule());

//        ObjectMapper om = new ObjectMapper();
//        // support Java 8 date time apis
//        om.registerModule(new JavaTimeModule());
//        //om.findAndRegisterModules();

        employees = employeeRepository.saveAll(List.of(
                new Employee(356712L, "cardiology", "Alonso Flores", ON_CALL, null),
                new Employee(564134L, "immunology", "Sam Ortega", ON, null),
                new Employee(761527L, "cardiology", "German Ruiz", OFF, null),
                new Employee(166552L, "pulmonary", "Maria Lin", ON, null),
                new Employee(156545L, "orthopaedic", "Paolo Rodriguez", ON_CALL, null),
                new Employee(172456L, "psychiatric", "John Paul Armes", OFF, null)
        ));

        patients = patientRepository.saveAll(List.of(
                new Patient("Jaime Jordan", LocalDate.parse("1984-03-02"), employees.get(1)),
                new Patient("Marian Garcia", LocalDate.parse("1972-01-12"), employeeRepository.findById(564134L).get()),
                new Patient("Julia Dusterdieck", LocalDate.parse("1954-06-11"), employeeRepository.findById(356712L).get()),
                new Patient("Steve McDuck", LocalDate.parse("1931-11-10"), employeeRepository.findById(761527L).get()),
                new Patient("Marian Garcia", LocalDate.parse("1999-02-15"), employeeRepository.findById(172456L).get())
        ));

    }

    @AfterEach
    void tearDown() {
        patientRepository.deleteAll();
        employeeRepository.deleteAll();
    }


    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("getAllPatients Method---Ok")
    void getAllPatients_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jordan"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("cardiology"));
    }

    @Test
    @DisplayName("getAllPatients Method--wrongPath--NotFound")
    void getAllPatients_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/wrongPath"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("getPatientById Method---Ok")
    void getPatientById_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patients/{id}", patients.getLast().getPatientId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Marian"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("psychiatric"));
    }

    @Test
    @DisplayName("getPatientById Method--wrongId--notFound")
    void getPatientById_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patients/{id}", 0))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException);
    }

    @Test
    @DisplayName("getPatientById Method--invalidData--badRequest")
    void getPatientById_badRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patients/id"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("getPatientByDateOfBirth Method---Ok")
    void getPatientByDateOfBirth_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(
                "/patients/dateOfBirth?dateOfBirthFrom=1930-03-01&dateOfBirthTo=1980-03-02"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Marian"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Julia Dusterdieck"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Jaime"));
    }

    @Test
    @DisplayName("getPatientByDateOfBirth Method---badRequest")
    void getPatientByDateOfBirth_invalidData_badRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(
                        "/patients/dateOfBirth?dateOfBirthFrom=2030-01-01&dateOfBirthTo=2100-01-01"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("getPatientByDepartment Method---Ok")
    void getPatientByDepartment_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(
                        "/patients/department/{department}", "cardiology"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Julia Dusterdieck"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Jaime"));
    }

    @Test
    @DisplayName("getPatientByDepartment Method---notFound")
    void getPatientByDepartment_wrongData_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(
                        "/patients/department/{department}", ""))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("getPatientByOffStatus Method---Ok")
    void getPatientByOffStatus_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patients/offStatus"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Steve"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Marian"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("Jaime"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("ON"));
    }

    @Test
    @DisplayName("createPatient Method---Ok")
    void createPatient_Ok() throws Exception {
        Patient patient = new Patient("Susana Rojas", LocalDate.parse("1990-03-02"), employeeRepository.findById(761527L).get());
        String body = objectMapper.writeValueAsString(patient);

        System.out.println("-----");
        System.out.println(body);
        System.out.println("-----");

        MvcResult mvcResult = mockMvc.perform(post("/patients")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Rojas"));
    }

    @Test
    @DisplayName("createPatient Method--wrongName--badRequest")
    void createPatient_wrongName_badRequest() throws Exception {
        Patient patient = new Patient("", LocalDate.parse("1990-03-02"), employeeRepository.findById(761527L).get());
        String body = objectMapper.writeValueAsString(patient);

        MvcResult mvcResult = mockMvc.perform(post("/patients")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("createPatient Method--wrongDate--badRequest")
    void createPatient_wrongDate_badRequest() throws Exception {
        Patient patient = new Patient("Lucia", null, employeeRepository.findById(761527L).get());
        String body = objectMapper.writeValueAsString(patient);

        MvcResult mvcResult = mockMvc.perform(post("/patients")
                        .contentType("application/json")
                        .content(body)                        )
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("updatePatient Method-fullData-Ok")
    void updatePatient_fullData_Ok() throws Exception{
        PatientDTO patientRequest = new PatientDTO("Antonio Rodriguez", LocalDate.parse("1999-03-02"), 761527L);
        String body = objectMapper.writeValueAsString(patientRequest);

        System.out.println("PATIENT JSON: " + body);

        MvcResult mvcResult = mockMvc.perform(put("/patients/{id}", patients.getLast().getPatientId())
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();

        assertEquals("Antonio Rodriguez", patientRepository.findById(patients.getLast().getPatientId()).get().getName());
    }

    @Test
    @DisplayName("updatePatient Method-noDate-Ok")
    void updatePatient_noDate_Ok() throws Exception{
        PatientDTO patientRequest = new PatientDTO("Antonio Rodriguez", null, 761527L);
        String body = objectMapper.writeValueAsString(patientRequest);

        System.out.println("PATIENT JSON: " + body);

        MvcResult mvcResult = mockMvc.perform(put("/patients/{id}", patients.getLast().getPatientId())
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();

        assertEquals("Antonio Rodriguez", patientRepository.findById(patients.getLast().getPatientId()).get().getName());
    }

    @Test
    @DisplayName("updatePatient Method-noName-Ok")
    void updatePatient_noName_Ok() throws Exception{
        PatientDTO patientRequest = new PatientDTO(null, LocalDate.parse("1999-03-02"), 761527L);
        String body = objectMapper.writeValueAsString(patientRequest);

        System.out.println("PATIENT JSON: " + body);

        MvcResult mvcResult = mockMvc.perform(put("/patients/{id}", patients.getLast().getPatientId())
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();

        assertEquals(LocalDate.parse("1999-03-02"), patientRepository.findById(patients.getLast().getPatientId()).get().getDateOfBirth());
    }

    @Test
    @DisplayName("updatePatient Method--wrongPatientId--notFound")
    void updatePatient_wrongPatientId_notFound() throws Exception{
        PatientDTO patientRequest = new PatientDTO("Antonio Rodriguez", LocalDate.parse("1999-03-02"), 761527L);
        String body = objectMapper.writeValueAsString(patientRequest);

        System.out.println("PATIENT JSON: " + body);

        MvcResult mvcResult =  mockMvc.perform(put("/patients/{id}", 0)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException);
    }

    @Test
    @DisplayName("updatePatient Method--wrongDate--badRequest")
    void updatePatient_wrongDate_badRequest() throws Exception{
        PatientDTO patientRequest = new PatientDTO("Antonio Rodriguez", LocalDate.parse("2050-03-02"), 761527L);
        String body = objectMapper.writeValueAsString(patientRequest);

        System.out.println("PATIENT JSON: " + body);

        MvcResult mvcResult =  mockMvc.perform(put("/patients/{id}", 0)
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException);
    }

    @Test
    @DisplayName("deletePatient Method---Ok")
    void deletePatient_Ok() throws Exception{
        MvcResult mvcResult = mockMvc.perform(delete("/patients/{id}", patients.getLast().getPatientId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        patients = patientRepository.findAll();

        assertEquals("Steve McDuck", patientRepository.findById(patients.getLast().getPatientId()).get().getName());
    }

    @Test
    @DisplayName("deletePatient Method--wrongPatientId--notFound")
    void deletePatient_wrongPatientId_notFound() throws Exception{
        MvcResult mvcResult = mockMvc.perform(delete("/patients/{id}", 0))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException);
    }


}
