package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = ShareItServer.class)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    private static final Long USER_ID = 1L;
    private static final Long NON_EXISTING_USER_ID = 999L;
    private static final UserDto userDto = new UserDto(1L, "Name", "email@example.com");

    @Test
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        List<UserDto> users = Collections.singletonList(userDto);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Name"))
                .andExpect(jsonPath("$[0].email").value("email@example.com"));
    }

    @Test
    void getUserById_shouldReturnUser() throws Exception {
        when(userService.getUserById(USER_ID)).thenReturn(userDto);

        mockMvc.perform(get("/users/{userId}", USER_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.email").value("email@example.com"));
    }

    @Test
    void getUserById_whenUserNotFound_thenNotFoundException() throws Exception {
        when(userService.getUserById(NON_EXISTING_USER_ID))
                .thenThrow(new NotFoundException(NON_EXISTING_USER_ID, "User"));

        mockMvc.perform(get("/users/{userId}", NON_EXISTING_USER_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Ошибка с введенным id"))
                .andExpect(jsonPath("$.description").value("id 999 сущности User не находится в базе"));
    }

    @Test
    void saveUser_shouldCreateUser() throws Exception {
        when(userService.saveUser(userDto)).thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.email").value("email@example.com"));
    }

    @Test
    void updateUser_shouldUpdateUser() throws Exception {
        UserDto updatedUserDto = new UserDto(1L, "Updated Name", "updated_email@example.com");
        when(userService.updateUser(USER_ID, updatedUserDto)).thenReturn(updatedUserDto);

        mockMvc.perform(patch("/users/{userId}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedUserDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated_email@example.com"));
    }

    @Test
    void deleteUser_shouldDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(USER_ID);

        mockMvc.perform(delete("/users/{userId}", USER_ID))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_whenUserNotFound_thenNotFoundException() throws Exception {
        doThrow(new NotFoundException(NON_EXISTING_USER_ID, "User")).when(userService).deleteUser(NON_EXISTING_USER_ID);

        mockMvc.perform(delete("/users/{userId}", NON_EXISTING_USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Ошибка с введенным id"))
                .andExpect(jsonPath("$.description").value("id 999 сущности User не находится в базе"));
    }
}
