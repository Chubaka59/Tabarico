package com.gtarp.tabarico.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gtarp.tabarico.dto.CheckboxUpdateRequestDto;
import com.gtarp.tabarico.dto.HolidayModificationDto;
import com.gtarp.tabarico.entities.Role;
import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.repositories.RoleRepository;
import com.gtarp.tabarico.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @WithUserDetails("testUser")
    public void getUserListPageTest() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("userList"))
                .andExpect(model().attributeExists("users"));
    }

    @Test
    @WithUserDetails("testUser")
    public void showAddUserPageTest() throws Exception {
        mockMvc.perform(get("/addUser"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addUser"))
                .andExpect(model().attributeExists("roles"));
    }

    @Test
    @WithUserDetails("testUser")
    public void addUserTest() throws Exception {
        int initialCount = userRepository.findAll().size();
        Role role = roleRepository.findAll().get(0);

        mockMvc.perform(post("/users")
                        .param("username", "testUsername")
                        .param("password", "testPassword")
                        .param("lastName", "testLastName")
                        .param("firstName", "testFirstName")
                        .param("phone", "testPhone")
                        .param("role.id", role.getId().toString())
                        .param("role.name", role.getName())
                        .param("role.redistributionRate", role.getRedistributionRate().toString())
                        .param("role.salary", role.getSalary().toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("userList"))
                .andExpect(model().attributeExists("users"));

        assertEquals(initialCount+1, userRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void addUserWhenErrorInTheFormTest() throws Exception {
        int initialCount = userRepository.findAll().size();

        mockMvc.perform(post("/users")
                        .param("username", "testUsername")
                        .param("password", "testPassword")
                        .param("lastName", "testLastName")
                        .param("firstName", "testFirstName")
                        .param("phone", "testPhone")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addUser"))
                .andExpect(model().attributeExists("roles"));

        assertEquals(initialCount, userRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void addUserWhenUserAlreadyExistTest() throws Exception {
        int initialCount = userRepository.findAll().size();
        Role role = roleRepository.findAll().get(0);

        mockMvc.perform(post("/users")
                        .param("username", "testUser")
                        .param("password", "testPassword")
                        .param("lastName", "testLastName")
                        .param("firstName", "testFirstName")
                        .param("phone", "testPhone")
                        .param("role.id", role.getId().toString())
                        .param("role.name", role.getName())
                        .param("role.redistributionRate", role.getRedistributionRate().toString())
                        .param("role.salary", role.getSalary().toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("addUser"))
                .andExpect(model().attributeExists("roles"));

        assertEquals(initialCount, userRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void showUpdateUserPageTest() throws Exception {
        mockMvc.perform(get("/users/{id}", userRepository.findAll().getFirst().getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateUser"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("userDto"));
    }

    @Test
    @WithUserDetails("testUser")
    public void updateUserTest() throws Exception {
        User updatedInformationUser = new User(2, "testUpdateUsername", "testUpdatePassword", "testUpdateLastname", "testUpdateFirstname", "testUpdatePhone", false, roleRepository.findAll().getFirst(), false, null, false, false, null, null, false, false, null);

        mockMvc.perform(post("/users/{id}", updatedInformationUser.getId())
                        .param("username", updatedInformationUser.getUsername())
                        .param("password", updatedInformationUser.getPassword())
                        .param("lastName", updatedInformationUser.getLastName())
                        .param("firstName", updatedInformationUser.getFirstName())
                        .param("phone", updatedInformationUser.getPhone())
                        .param("role.id", updatedInformationUser.getRole().getId().toString())
                        .param("role.name", updatedInformationUser.getRole().getName())
                        .param("role.redistributionRate", updatedInformationUser.getRole().getRedistributionRate().toString())
                        .param("role.salary", updatedInformationUser.getRole().getSalary().toString())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("userList"))
                .andExpect(model().attributeExists("users"));

        User updatedUser = userRepository.findById(updatedInformationUser.getId()).get();
        assertEquals(updatedInformationUser.getUsername(), updatedUser.getUsername());
        assertEquals(updatedInformationUser.getLastName(), updatedUser.getLastName());
        assertEquals(updatedInformationUser.getFirstName(), updatedUser.getFirstName());
        assertEquals(updatedInformationUser.getPhone(), updatedUser.getPhone());
        assertEquals(updatedInformationUser.getRole(), updatedUser.getRole());
    }

    @Test
    @WithUserDetails("testUser")
    public void updateUserWhenErrorInTheFormTest() throws Exception {
        User updatedInformationUser = new User(2, "testNotUpdateUsername", "testNotUpdatePassword", "testNotUpdateLastname", "testNotUpdateFirstname", "testNotUpdatePhone", false, roleRepository.findAll().getFirst(), false, null, false, false, null, null, false, false, null);

        mockMvc.perform(post("/users/{id}", updatedInformationUser.getId())
                        .param("username", updatedInformationUser.getUsername())
                        .param("password", updatedInformationUser.getPassword())
                        .param("lastName", updatedInformationUser.getLastName())
                        .param("firstName", updatedInformationUser.getFirstName())
                        .param("phone", updatedInformationUser.getPhone())
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateUser"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("userDto"));

        User updatedUser = userRepository.findById(updatedInformationUser.getId()).get();
        assertNotEquals(updatedInformationUser.getUsername(), updatedUser.getUsername());
    }

    @Test
    @WithUserDetails("testUser")
    public void updateUserWhenUserIsNotFoundTest() throws Exception {
        mockMvc.perform(post("/users/{id}",99)
                        .param("username", "test")
                        .param("password", "test")
                        .param("lastName", "test")
                        .param("firstName", "test")
                        .param("phone", "test")
                        .param("role.id", "1")
                        .param("role.name", "test")
                        .param("role.redistributionRate", "20")
                        .param("role.salary", "30000")
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("updateUser"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attributeExists("userDto"));
    }

    @Test
    @WithUserDetails("testUser")
    public void deleteUserTest() throws Exception {
        int initialCount = userRepository.findAll().size();

        mockMvc.perform(get("/users/3/delete"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("userList"))
                .andExpect(model().attributeExists("users"));

        assertEquals(initialCount - 1, userRepository.findAll().size());
    }

    @Test
    @WithUserDetails("testUser")
    public void updateBooleanValueTest() throws Exception {
        CheckboxUpdateRequestDto checkboxUpdateRequestDto = new CheckboxUpdateRequestDto(2, "quota", true);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestBody = objectWriter.writeValueAsString(checkboxUpdateRequestDto);

        mockMvc.perform(post("/updateBoolean").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        User updateduser = userRepository.findById(2).get();
        assertTrue(updateduser.isQuota());
    }

    @Test
    @WithUserDetails("testUser")
    public void updateBooleanValueWhenUserIsNotFoundTest() throws Exception {
        CheckboxUpdateRequestDto checkboxUpdateRequestDto = new CheckboxUpdateRequestDto(99, "quota", true);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestBody = objectWriter.writeValueAsString(checkboxUpdateRequestDto);

        mockMvc.perform(post("/updateBoolean").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("testUser")
    public void updateHolidayDataTest() throws Exception {
        LocalDate endOfHoliday = LocalDate.now().plusDays(1);
        HolidayModificationDto holidayModificationDto = new HolidayModificationDto(2, true, endOfHoliday);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestBody = objectWriter.writeValueAsString(holidayModificationDto);

        mockMvc.perform(post("/updateHolidayData").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        User updateduser = userRepository.findById(holidayModificationDto.getUserId()).get();
        assertTrue(updateduser.isHoliday());
        assertEquals(endOfHoliday, updateduser.getEndOfHoliday());
    }

    @Test
    @WithUserDetails("testUser")
    public void updateHolidayDataWhenUserIsNotFoundTest() throws Exception {
        LocalDate endOfHoliday = LocalDate.now().plusDays(1);
        HolidayModificationDto holidayModificationDto = new HolidayModificationDto(99, true, endOfHoliday);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestBody = objectWriter.writeValueAsString(holidayModificationDto);

        mockMvc.perform(post("/updateHolidayData").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("testUser")
    public void updateHolidayDataWhenEndOfHolidayIsBeforeNowTest() throws Exception {
        LocalDate endOfHoliday = LocalDate.now().minusDays(1);
        HolidayModificationDto holidayModificationDto = new HolidayModificationDto(2, true, endOfHoliday);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestBody = objectWriter.writeValueAsString(holidayModificationDto);

        mockMvc.perform(post("/updateHolidayData").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        User updateduser = userRepository.findById(holidayModificationDto.getUserId()).get();
        assertFalse(updateduser.isHoliday());
        assertNull(updateduser.getEndOfHoliday());
    }

    @Test
    @WithUserDetails("testUser")
    public void updateHolidayDataWhenDateIsNullTest() throws Exception {
        HolidayModificationDto holidayModificationDto = new HolidayModificationDto(2, true, null);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
        String requestBody = objectWriter.writeValueAsString(holidayModificationDto);

        mockMvc.perform(post("/updateHolidayData").contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

        User updateduser = userRepository.findById(holidayModificationDto.getUserId()).get();
        assertFalse(updateduser.isHoliday());
        assertNull(updateduser.getEndOfHoliday());
    }
}
