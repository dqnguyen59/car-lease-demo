package org.smartblackbox.leasedemo;

//import static org.hamcrest.CoreMatchers.is;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.smartblackbox.carleasedemo.UserApplication;
import org.smartblackbox.carleasedemo.config.ServerConfig;
import org.smartblackbox.carleasedemo.datamodel.dto.SignInDTO;
import org.smartblackbox.carleasedemo.datamodel.dto.SignUpDTO;
import org.smartblackbox.carleasedemo.datamodel.dto.UpdateUserDTO;
import org.smartblackbox.carleasedemo.datamodel.entity.Role;
import org.smartblackbox.carleasedemo.datamodel.entity.User;
import org.smartblackbox.carleasedemo.datamodel.entity.enums.RoleType;
import org.smartblackbox.carleasedemo.datamodel.response.SignInResponse;
import org.smartblackbox.carleasedemo.datamodel.response.UserDTO;
import org.smartblackbox.carleasedemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Copyright (C) 2024  Duy Quoc Nguyen d.q.nguyen@smartblackbox.org
 *
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
class UserControllerTests {

  @Autowired
  private ServerConfig serverConfig;

  private static String rolesString;

  @Autowired
  private MockMvc api;

  @Autowired
  private UserService userService;

  @Autowired
  private ObjectMapper objectMapper;

  private String token;

  private Optional<User> user;

  private String username;

  private String password;

  private int userId;

  @BeforeAll
  void setup() {
    log.info("Setup");

    // Run SpringbootDemoApplication
    SpringApplication.run(UserApplication.class, new String[0]);

    user = userService.findByUsername(serverConfig.rootUsername);

    rolesString = userService.getAllRolesString(user.get().getId());

  }

  @Test
  @Order(1)
  public void givenSysRootUserAndWrongPassword_whenSignIn_thenReturnToken() throws Exception {
    log.info("givenSysRoot_whenSignIn_thenReturnToken()");

    username = serverConfig.rootUsername;
    password = "wrongpassword";

    // given - precondition or setup
    SignInDTO userSignIn = new SignInDTO();
    userSignIn.setUsername(username);
    userSignIn.setPassword(password);

    // when - action or behavior that we are going test
    ResultActions response = api.perform(post("/api/v1/auth/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userSignIn)));


    // then - verify the result or output using assert statements
    response
    .andDo(print())
    .andExpect(status().isUnauthorized())
    .andExpect(jsonPath("$.token").doesNotExist());
  }

  @Test
  @Order(2)
  public void givenSysRootUser_whenSignIn_thenReturnToken() throws Exception {
    log.info("givenSysRoot_whenSignIn_thenReturnToken()");

    username = serverConfig.rootUsername;
    password = serverConfig.rootPassword;

    // given - precondition or setup
    SignInDTO userSignIn = new SignInDTO();
    userSignIn.setUsername(username);
    userSignIn.setPassword(password);

    // when - action or behavior that we are going test
    ResultActions response = api.perform(post("/api/v1/auth/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userSignIn)));


    // then - verify the result or output using assert statements
    MvcResult result = response
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.resetPasswordRequired", is(true)))
        .andReturn();

    SignInResponse loginResponse = new Gson().fromJson(result.getResponse().getContentAsString(), SignInResponse.class);

    token = loginResponse.getToken();
  }

  @Test
  @Order(3)
  public void givenNoToken_whenWelcomeUser_thenReturnUnauthorized() throws Exception {
    log.info("givenNoToken_whenWelcomeUser_thenReturnUnauthorized()");

    // given - precondition or setup
    // None

    // when - action or behavior that we are going test
    ResultActions response = api.perform(get("/api/v1/welcome/role/user")
        .contentType(MediaType.APPLICATION_JSON));

    // then - verify the result or output using assert statements
    response
    .andDo(print())
    .andExpect(status().isForbidden());

  }

  @Test
  @Order(4)
  public void givenNoToken_whenWelcomeBroker_thenReturnUnauthorized() throws Exception {
    log.info("givenNoToken_whenWelcomeBroker_thenReturnUnauthorized()");

    // given - precondition or setup
    // None

    // when - action or behavior that we are going test
    ResultActions response = api.perform(get("/api/v1/welcome/role/broker")
        .contentType(MediaType.APPLICATION_JSON));

    // then - verify the result or output using assert statements
    response
    .andDo(print())
    .andExpect(status().isForbidden());

  }

  @Test
  @Order(5)
  public void givenToken_whenWelcomeUser_thenReturnWelcomeMessage() throws Exception {
    log.info("givenToken_whenWelcomeUser_thenReturnWelcomeMessage()");

    // given - precondition or setup
    // Token from signed in as user root, see order(1)

    // when - action or behavior that we are going test
    ResultActions response = api.perform(get("/api/v1/welcome/role/user")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON));

    // then - verify the result or output using assert statements
    response
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.message", is("Welcome user " + username + ", your roles are: [" + rolesString + "]!")));

  }

  @Test
  @Order(6)
  public void givenToken_whenWelcomeBroker_thenReturnWelcomeMessage() throws Exception {
    log.info("givenToken_whenWelcomeBroker_thenReturnWelcomeMessage()");

    // given - precondition or setup
    // Token from signed in as user with broker role, see order(1)

    // when - action or behavior that we are going test
    ResultActions response = api.perform(get("/api/v1/welcome/role/broker")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON));

    // then - verify the result or output using assert statements
    response
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.message", is("Welcome broker " + username + ", your roles are: [" + rolesString + "]!")));

  }

  @Test
  @Order(7)
  public void givenToken_whenChangePassword_thenReturnOk() throws Exception {
    log.info("givenToken_whenChangePassword_thenReturnOk()");

    // given - precondition or setup
    int id = 1; // Id of root user
    String oldPassword = serverConfig.rootPassword;
    String newPassword = "mypassword";

    UpdateUserDTO updateDTO = new UpdateUserDTO();
    updateDTO.setOldPassword(oldPassword);
    updateDTO.setNewPassword(newPassword);

    // when -  action or the behavior that we are going test
    ResultActions response = api.perform(put("/api/v1/users/{id}", id)
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateDTO)));


    // then - verify the output
    response
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.id", is(id)));
  }

  @Test
  @Order(8)
  public void givenToken_whenSignOut_thenReturnOk() throws Exception {
    log.info("givenToken_whenSignOut_thenReturnOk()");

    // when - action or behavior that we are going test
    ResultActions response = api.perform(post("/api/v1/auth/signout")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON));

    // then - verify the result or output using assert statements
    response
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.message", is("Signout successfully!")));
  }

  @Test
  @Order(9)
  public void givenTokenButSignedOut_whenWelcomeBroker_thenReturnWelcomeMessage() throws Exception {
    log.info("givenTokenButSignedOut_whenWelcomeBroker_thenReturnWelcomeMessage()");

    // given - precondition or setup
    // Token from signed in as root user, see order(1)

    // when - action or behavior that we are going test
    ResultActions response = api.perform(get("/api/v1/welcome/role/broker")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON));

    // then - verify the result or output using assert statements
    response
    .andDo(print())
    .andExpect(status().isForbidden());

  }

  @Test
  @Order(20)
  public void givenNewSysRootPassword_whenSignin_thenReturnOk() throws Exception {
    log.info("givenNewSysRootPassword_whenSignin_thenReturnOk()");

    username = serverConfig.rootUsername;
    password = "mypassword";

    // given - precondition or setup
    SignInDTO userSignIn = new SignInDTO();
    userSignIn.setUsername(username);
    userSignIn.setPassword(password);

    // when - action or behavior that we are going test
    ResultActions response = api.perform(post("/api/v1/auth/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userSignIn)));


    // then - verify the result or output using assert statements
    MvcResult result = response.andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.resetPasswordRequired", is(true)))
        .andReturn();

    SignInResponse loginResponse = new Gson().fromJson(result.getResponse().getContentAsString(), SignInResponse.class);

    token = loginResponse.getToken();
  }

  @Test
  @Order(21)
  public void givenMissingRootRole_whenChangeRole_thenReturnForbidden() throws Exception {
    log.info("givenMissingRootRole_whenChangeRole_thenReturnForbidden()");

    // given - precondition or setup
    int id = 1; // Id of root user
    UpdateUserDTO updateDTO = new UpdateUserDTO();

    Role roleUser = new Role();
    roleUser.setId(2);
    roleUser.setType(RoleType.ROLE_USER);

    List<Role> roles = Arrays.asList(roleUser);

    updateDTO.setRoles(roles);

    // when -  action or the behavior that we are going test
    ResultActions response = api.perform(put("/api/v1/users/{id}", id)
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateDTO)));


    // then - verify the output
    response
    .andDo(print())
    .andExpect(status().is(403))
    .andExpect(jsonPath("$.detail", is("You are not allowed to modify roles of your own!")));
  }

  @Test
  @Order(24)
  public void givenUserObject_whenSignUpUser_thenReturnSavedUser() throws Exception {

    // given - precondition or setup

    username = "TestUser1";
    password = "TestPassword1";

    SignUpDTO signupDTO = new SignUpDTO();
    signupDTO.setUsername(username);
    signupDTO.setPassword(password);

    // when - action or behavior that we are going test
    ResultActions response = api.perform(post("/api/v1/users/add")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signupDTO)));

    // then - verify the result or output using assert statements
    MvcResult result = response.andDo(print()).
        andExpect(status().isOk())
        .andExpect(jsonPath("$.username", is(signupDTO.getUsername())))
        .andExpect(jsonPath("$.resetPasswordRequired", is(true)))
        .andExpect(jsonPath("$.active", is(true)))
        .andReturn();

    UserDTO userResponse = new Gson().fromJson(result.getResponse().getContentAsString(), UserDTO.class);

    userId = userResponse.getId();
    user = userService.findById(userResponse.getId());

    assert user.get().roleMatchedDTO(userResponse.getRoles()) : "Error: roles does not match!";
  }

  @Test
  @Order(25)
  public void givenUserObject_whenSignUpSameUser_thenReturnNotOk() throws Exception {

    // given - precondition or setup

    username = "TestUser1";
    password = "TestPassword1";

    SignUpDTO signupDTO = new SignUpDTO();
    signupDTO.setUsername(username);
    signupDTO.setPassword(password);

    // when - action or behavior that we are going test
    ResultActions response = api.perform(post("/api/v1/users/add")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(signupDTO)));

    // then - verify the result or output using assert statements
    response.andDo(print()).
    andExpect(status().isBadRequest());

  }

  @Test
  @Order(26)
  public void givenToken_whenSignOut_thenReturnOk2() throws Exception {
    log.info("givenToken_whenSignOut_thenReturnOk2()");

    // when - action or behavior that we are going test
    ResultActions response = api.perform(post("/api/v1/auth/signout")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON));

    // then - verify the result or output using assert statements
    response
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.message", is("Signout successfully!")));
  }

  @Test
  @Order(27)
  public void givenTestUser1AndWrongPassword_whenSignIn_thenReturnToken() throws Exception {
    log.info("givenTestUser1AndWrongPassword_whenSignIn_thenReturnToken()");

    username = "TestUser1";
    password = "wrongpassword";

    // given - precondition or setup
    SignInDTO userSignIn = new SignInDTO();
    userSignIn.setUsername(username);
    userSignIn.setPassword(password);

    // when - action or behavior that we are going test
    ResultActions response = api.perform(post("/api/v1/auth/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userSignIn)));


    // then - verify the result or output using assert statements
    response
    .andDo(print())
    .andExpect(status().isUnauthorized())
    .andExpect(jsonPath("$.token").doesNotExist());
  }

  @Test
  @Order(28)
  public void givenTestUser1_whenSignIn_thenReturnToken() throws Exception {
    log.info("givenTestUser1_whenSignIn_thenReturnToken()");

    username = "TestUser1";
    password = "TestPassword1";

    // given - precondition or setup
    SignInDTO userSignIn = new SignInDTO();
    userSignIn.setUsername(username);
    userSignIn.setPassword(password);

    // when - action or behavior that we are going test
    ResultActions response = api.perform(post("/api/v1/auth/signin")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userSignIn)));


    // then - verify the result or output using assert statements
    MvcResult result = response
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").exists())
        .andExpect(jsonPath("$.resetPasswordRequired", is(true)))
        .andReturn();

    SignInResponse loginResponse = new Gson().fromJson(result.getResponse().getContentAsString(), SignInResponse.class);

    token = loginResponse.getToken();
  }

  @Test
  @Order(29)
  public void givenToken_whenChangePassword_thenReturnOk2() throws Exception {
    log.info("givenToken_whenChangePassword_thenReturnOk2()");

    // given - precondition or setup
    int id = userId; // Id of TestUser1
    String oldPassword = "TestPassword1";
    String newPassword = "mypassword";

    UpdateUserDTO updateDTO = new UpdateUserDTO();
    updateDTO.setOldPassword(oldPassword);
    updateDTO.setNewPassword(newPassword);

    // when -  action or the behavior that we are going test
    ResultActions response = api.perform(put("/api/v1/users/{id}", id)
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateDTO)));


    // then - verify the output
    response
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.id", is(id)));
  }

}

