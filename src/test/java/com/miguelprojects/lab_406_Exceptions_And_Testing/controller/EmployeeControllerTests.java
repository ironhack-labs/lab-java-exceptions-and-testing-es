package com.miguelprojects.lab_406_Exceptions_And_Testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miguelprojects.lab_406_Exceptions_And_Testing.DTOs.EmployeeDTO;
import com.miguelprojects.lab_406_Exceptions_And_Testing.Enums.Status;
import com.miguelprojects.lab_406_Exceptions_And_Testing.model.Employee;
import com.miguelprojects.lab_406_Exceptions_And_Testing.model.Patient;
import com.miguelprojects.lab_406_Exceptions_And_Testing.repository.EmployeeRepository;
import com.miguelprojects.lab_406_Exceptions_And_Testing.repository.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;

import static com.miguelprojects.lab_406_Exceptions_And_Testing.Enums.Status.*;
import static com.miguelprojects.lab_406_Exceptions_And_Testing.Enums.Status.OFF;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class EmployeeControllerTests {

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
        //objectMapper.registerModule(new JavaTimeModule());

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
    @DisplayName("getAllEmployees Method---Ok")
    void getAllEmployees_Ok() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/employees"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Alonso"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("psychiatric"));
    }

    @Test
    @DisplayName("getAllEmployees Method--wrongPath--notFound")
    void getAllEmployees_wrongPath_notFound() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/wrongPath"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("getAllEmployees Method--wrongPath--badRequest")
    void getAllEmployees_wrongPath_badRequest() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/employees/wrongPath"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("getEmployeeById Method---Ok")
    void getEmployeeById_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/{id}", employees.getLast().getEmployeeId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("John Paul Armes"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("psychiatric"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("OFF"));
    }

    @Test
    @DisplayName("getEmployeeById Method--wrongEmployeeId--notFound")
    void getEmployeeById_wrongEmployeeId_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/{id}", 0))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException);
    }

    @Test
    @DisplayName("getEmployeeById Method--wrongData--badRequest")
    void getEmployeeById_wrongData_badRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/id"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentTypeMismatchException);
    }

    @Test
    @DisplayName("getEmployeeByStatus Method---Ok")
    void getEmployeeByStatus_Ok() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/employees/status?status=ON"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Maria Lin"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("immunology"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("ON"));
    }

    @Test
    @DisplayName("getEmployeeByStatus Method---badRequest")
    void getEmployeeByStatus_badRequest() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/employees/status?status=ALL"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentTypeMismatchException);
    }

    @Test
    @DisplayName("getEmployeeByDepartment Method---Ok")
    void getEmployeeByDepartment_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/employees/department?department=cardiology"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Alonso Flores"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("German Ruiz"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("ON_CALL"));
    }

    @Test
    @DisplayName("getEmployeeByDepartment Method---badRequest")
    void getEmployeeByDepartment_badRequest() throws Exception{
        MvcResult mvcResult = mockMvc.perform(get("/employees/status?status=fisioteraphy"))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentTypeMismatchException);
    }

    @Test
    @DisplayName("getEmployeeByDepartment Method---Ok")
    void createEmployee_Ok() throws Exception {
        Employee employee = new Employee(356222L, "fisioteraphy", "Juan Ramon Lara", ON, null);
        String body = objectMapper.writeValueAsString(employee);

        System.out.println("-----");
        System.out.println(body);
        System.out.println("-----");

        MvcResult mvcResult = mockMvc.perform(post("/employees")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Juan"));
    }

    @Test
    @DisplayName("getEmployeeByDepartment Method--noNameNoDepartment--badRequest")
    void createEmployee_noNameNoDepartment_badRequest() throws Exception {
        Employee employee = new Employee(356222L, "", "", ON, null);
        String body = objectMapper.writeValueAsString(employee);

        System.out.println("-----");
        System.out.println(body);
        System.out.println("-----");

        MvcResult mvcResult = mockMvc.perform(post("/employees")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException);
    }

    @Test
    @DisplayName("getEmployeeByDepartment Method--noStatus--badRequest")
    void createEmployee_noStatus_badRequest() throws Exception {
        Employee employee = new Employee(356222L, "fisioteraphy", "Juan Ramon Lara",null,null);
        String body = objectMapper.writeValueAsString(employee);

        System.out.println("-----");
        System.out.println(body);
        System.out.println("-----");

        MvcResult mvcResult = mockMvc.perform(post("/employees")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentNotValidException);
    }

    @Test
    @DisplayName("updateEmployee Method--AllData--Ok")
    void updateEmployee_AllData_Ok() throws Exception {
        EmployeeDTO employeeRequest = new EmployeeDTO(356222L, "fisioteraphy", "Juan Ramon Lara", ON);
        String employeeJson = objectMapper.writeValueAsString(employeeRequest);

        System.out.println("EMPLOYEE JSON: " + employeeJson);

        MvcResult mvcResult = mockMvc.perform(put("/employees/{id}", employees.getLast().getEmployeeId())
                        .contentType("application/json")
                        .content(employeeJson))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();

        assertEquals("Juan Ramon Lara", employeeRepository.findById(employees.getLast().getEmployeeId()).get().getName());
    }

    @Test
    @DisplayName("updateEmployee Method--SomeData--Ok")
    void updateEmployee_SomeData_Ok() throws Exception {
        EmployeeDTO employeeRequest = new EmployeeDTO(null, null, "Juan Ramon Lara", ON);
        String employeeJson = objectMapper.writeValueAsString(employeeRequest);

        System.out.println("EMPLOYEE JSON: " + employeeJson);

        MvcResult mvcResult = mockMvc.perform(put("/employees/{id}", employees.getLast().getEmployeeId())
                        .contentType("application/json")
                        .content(employeeJson))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();

        assertEquals("Juan Ramon Lara", employeeRepository.findById(employees.getLast().getEmployeeId()).get().getName());
    }

    @Test
    @DisplayName("updateEmployee Method--invalidEmployeeId--notFound")
    void updateEmployee_invalidEmployeeId_notFound() throws Exception {
        EmployeeDTO employeeRequest = new EmployeeDTO(356222L, "fisioteraphy", "Juan Ramon Lara", ON);
        String employeeJson = objectMapper.writeValueAsString(employeeRequest);

        System.out.println("EMPLOYEE JSON: " + employeeJson);

        MvcResult mvcResult = mockMvc.perform(put("/employees/{id}", 0)
                        .contentType("application/json")
                        .content(employeeJson))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException);
    }

    @Test
    @DisplayName("changeStatus Method--Ok")
    void changeStatus() throws Exception {
        MvcResult mvcResult = mockMvc.perform(patch
                        ("/employees/{id}/status?status=ON_CALL", employees.getLast().getEmployeeId()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();

        assertEquals(Status.valueOf("ON_CALL"), employeeRepository.findById(employees.getLast().getEmployeeId()).get().getStatus());
    }

    @Test
    @DisplayName("changeStatus Method--invalidEmployeeId--notFound")
    void changeStatus_invalidEmployeeId_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(patch
                        ("/employees/{id}/status?status=ON_CALL", 0))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException);
    }

    @Test
    @DisplayName("changeStatus Method--wrongStatus--badRequest")
    void changeStatus_wrongStatus_badRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(patch
                        ("/employees/{id}/status?status=ALL", employees.getLast().getEmployeeId()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof MethodArgumentTypeMismatchException);
    }

    @Test
    @DisplayName("changeStatus Method--noStatus--badRequest")
    void changeStatus_noStatus_badRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(patch
                        ("/employees/{id}/status?status=", employees.getLast().getEmployeeId()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof MissingServletRequestParameterException);
    }

    @Test
    @DisplayName("changeDepartment Method--Ok")
    void changeDepartment() throws Exception {
        MvcResult mvcResult = mockMvc.perform(patch
                        ("/employees/{id}/department?department=fisioteraphy", employees.getLast().getEmployeeId()))
                .andExpect(status().isNoContent())
                .andDo(print())
                .andReturn();

        assertEquals("fisioteraphy", employeeRepository.findById(employees.getLast().getEmployeeId()).get().getDepartment());
    }

    @Test
    @DisplayName("changeDepartment Method--invalidEmployeeId--notFound")
    void changeDepartment_invalidEmployeeId_notFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(patch
                        ("/employees/{id}/department?department=fisioteraphy", 0))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof ResponseStatusException);
    }

    @Test
    @DisplayName("changeDepartment Method--noDepartment--badRequest")
    void changeDepartment_noDepartment_badRequest() throws Exception {
        MvcResult mvcResult = mockMvc.perform(patch
                        ("/employees/{id}/department?", employees.getLast().getEmployeeId()))
                .andExpect(status().isBadRequest())
                .andDo(print())
                .andReturn();

        assertTrue(mvcResult.getResolvedException() instanceof MissingServletRequestParameterException);
    }

    @Test
    @DisplayName("deleteEmployee Method--withoutPatient--Ok")
    void deleteEmployee_withoutPatient_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/employees/{id}", 166552L))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        assertFalse(mvcResult.getResponse().getContentAsString().contains("Maria Lin"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("pulmonary"));
        assertFalse(employeeRepository.existsById(166552L));
    }
    @Test
    @DisplayName("deleteEmployee Method--withPatient--Ok")
    void deleteEmployee_withPatient_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/employees/{id}", 172456L))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        assertFalse(mvcResult.getResponse().getContentAsString().contains("John Paul Armes"));
        assertFalse(mvcResult.getResponse().getContentAsString().contains("psychiatric"));
        assertFalse(employeeRepository.existsById(172456L));

    }


}
