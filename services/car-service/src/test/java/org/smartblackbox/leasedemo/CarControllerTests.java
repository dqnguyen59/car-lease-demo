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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.text.SimpleDateFormat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.smartblackbox.carleasedemo.CarApplication;
import org.smartblackbox.carleasedemo.datamodel.entity.Car;
import org.smartblackbox.carleasedemo.service.CarService;
import org.smartblackbox.leasedemo.dto.SignInDTO;
import org.smartblackbox.leasedemo.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@TestMethodOrder(MethodOrderer.MethodName.class)
class CarControllerTests {

  private static String rolesString;

  @Autowired
  private MockMvc api;

  @Autowired
  private CarService carService;

  @Autowired
  private ObjectMapper objectMapper;

  private String token;

  private String username;

  private String password;

  private int userId;

  @BeforeAll
  void setup() {
    log.info("Setup");

    // Run CarApplication
    SpringApplication.run(CarApplication.class, new String[0]);


  }

  public String signInAsUser(String username, String password) {
    SignInDTO userSignIn = new SignInDTO();
    userSignIn.setUsername(username);
    userSignIn.setPassword(password);

    String url = "http://localhost:9000";

    RestClient restClient = RestClient.builder()
        .baseUrl(url)
        .build();

    UserDTO response = restClient.post()
        .uri("/api/v1/auth/signin", token)
        .accept(MediaType.APPLICATION_JSON)
        .body(userSignIn)
        .retrieve()
        .body(UserDTO.class);

    log.info("response: " + response);
    
    return response.getToken();
  }
  
  @Test
  public void t01_givenUser_whenSignIn_thenReturnToken() throws Exception {
    log.info("givenUser_whenSignIn_thenReturnToken()");

    // given - precondition or setup
    username = "root";
    password = "secret";

    // when - action or behavior that we are going test
    token = signInAsUser(username, password);
    
    // then - verify the result or output using assert statements
    assert(!token.isEmpty());
  }

  @Test
  public void t02_givenCarObject_whenAddCar_thenReturnSavedUser() throws Exception {
    log.info("givenCarObject_whenAddCar_thenReturnSavedUser()");

    // given - precondition or setup
    Car car = Car.builder()
        .make("Audi")
        .model("A6")
        .version("1")
        .mileage(35000)
        .duration(60)
        .cO2Emission(11)
        .grossPrice(75020.0)
        .nettPrice(62000.0)
        .numberOfDoors(4)
        .interestRate(4.2)
        .startDate(new SimpleDateFormat("yyyyMMddHH").parse("2024030212"))
        .build();

    // when - action or behavior that we are going test
    ResultActions response = api.perform(post("/api/v1/cars/add")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(car)));

    // then - verify the result or output using assert statements
    response.andDo(print()).
        andExpect(status().isOk())
        .andExpect(jsonPath("$.make", is(car.getMake())))
        .andReturn();
  }

  @Test
  public void t03_givenToken_whenGetCarList_thenReturnListOfCars() throws Exception {
    log.info("givenToken_whenGetCarList_thenReturnListOfCars()");

    // given - precondition or setup
    // Token from signed in as user root, see order(1)

    // when - action or behavior that we are going test
    ResultActions response = api.perform(get("/api/v1/cars/page")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON));

    // then - verify the result or output using assert statements
    response
    .andDo(print())
    .andExpect(status().isOk())
    .andExpect(jsonPath("$[0]['make']", is("BMW")))
    .andExpect(jsonPath("$[0]['model']", is("5 Serie")))
    .andExpect(jsonPath("$[0]['version']", is("25th Edition")))
    .andExpect(jsonPath("$[0]['numberOfDoors']", is(4)))
    .andExpect(jsonPath("$[0]['grossPrice']", is(76230.0)))
    .andExpect(jsonPath("$[0]['nettPrice']", is(63000.0)))
    .andExpect(jsonPath("$[0]['mileage']", is(45000)))
    .andExpect(jsonPath("$[0]['interestRate']", is(4.5)))
    .andExpect(jsonPath("$[0]['startDate']", is("2024-03-01T11:00:00.000+00:00")))
    .andExpect(jsonPath("$[0]['duration']", is(60)))
    .andExpect(jsonPath("$[0]['leaseRate']", is(239.82142857142858)))
    .andExpect(jsonPath("$[0]['co2Emission']", is(10)));
  }
  
  
  //
  //	@Test
  //	@Order(6)
  //	public void givenToken_whenWelcomeBroker_thenReturnWelcomeMessage() throws Exception {
  //		log.info("givenToken_whenWelcomeBroker_thenReturnWelcomeMessage()");
  //
  //		// given - precondition or setup
  //		// Token from signed in as user with broker role, see order(1)
  //
  //		// when - action or behavior that we are going test
  //		ResultActions response = api.perform(get("/api/v1/welcome/role/broker")
  //				.header("Authorization", "Bearer " + token)
  //				.contentType(MediaType.APPLICATION_JSON));
  //
  //		// then - verify the result or output using assert statements
  //		response
  //		.andDo(print())
  //		.andExpect(status().isOk())
  //		.andExpect(jsonPath("$.message", is("Welcome broker " + username + ", your roles are: [" + rolesString + "]!")));
  //
  //	}
  //
  //	@Test
  //	@Order(7)
  //	public void givenToken_whenChangePassword_thenReturnOk() throws Exception {
  //		log.info("givenToken_whenChangePassword_thenReturnOk()");
  //
  //		// given - precondition or setup
  //		int id = 1; // Id of root user
  //		String oldPassword = Consts.SYS_ROOT_DEFAULT_PASSWORD;
  //		String newPassword = "mypassword";
  //
  //		UpdateUserDTO updateDTO = new UpdateUserDTO();
  //		updateDTO.setOldPassword(oldPassword);
  //		updateDTO.setNewPassword(newPassword);
  //
  //		// when -  action or the behavior that we are going test
  //		ResultActions response = api.perform(put("/api/v1/users/{id}", id)
  //				.header("Authorization", "Bearer " + token)
  //				.contentType(MediaType.APPLICATION_JSON)
  //				.content(objectMapper.writeValueAsString(updateDTO)));
  //
  //
  //		// then - verify the output
  //		response
  //		.andDo(print())
  //		.andExpect(status().isOk())
  //		.andExpect(jsonPath("$.id", is(id)));
  //	}
  //
  //	@Test
  //	@Order(8)
  //	public void givenToken_whenSignOut_thenReturnOk() throws Exception {
  //		log.info("givenToken_whenSignOut_thenReturnOk()");
  //
  //		// when - action or behavior that we are going test
  //		ResultActions response = api.perform(post("/api/v1/auth/signout")
  //				.header("Authorization", "Bearer " + token)
  //				.contentType(MediaType.APPLICATION_JSON));
  //
  //		// then - verify the result or output using assert statements
  //		response
  //		.andDo(print())
  //		.andExpect(status().isOk())
  //		.andExpect(jsonPath("$.message", is("Signout successfully!")));
  //	}
  //
  //	@Test
  //	@Order(9)
  //	public void givenTokenButSignedOut_whenWelcomeBroker_thenReturnWelcomeMessage() throws Exception {
  //		log.info("givenTokenButSignedOut_whenWelcomeBroker_thenReturnWelcomeMessage()");
  //
  //		// given - precondition or setup
  //		// Token from signed in as root user, see order(1)
  //
  //		// when - action or behavior that we are going test
  //		ResultActions response = api.perform(get("/api/v1/welcome/role/broker")
  //				.header("Authorization", "Bearer " + token)
  //				.contentType(MediaType.APPLICATION_JSON));
  //
  //		// then - verify the result or output using assert statements
  //		response
  //		.andDo(print())
  //		.andExpect(status().isForbidden());
  //
  //	}
  //
  //	@Test
  //	@Order(20)
  //	public void givenNewSysRootPassword_whenSignin_thenReturnOk() throws Exception {
  //		log.info("givenNewSysRootPassword_whenSignin_thenReturnOk()");
  //
  //		username = Consts.ROOT_USER_NAME;
  //		password = "mypassword";
  //
  //		// given - precondition or setup
  //		SignInDTO userSignIn = new SignInDTO();
  //		userSignIn.setUsername(username);
  //		userSignIn.setPassword(password);
  //
  //		// when - action or behavior that we are going test
  //		ResultActions response = api.perform(post("/api/v1/auth/signin")
  //				.contentType(MediaType.APPLICATION_JSON)
  //				.content(objectMapper.writeValueAsString(userSignIn)));
  //
  //
  //		// then - verify the result or output using assert statements
  //		MvcResult result = response.andDo(print())
  //				.andExpect(status().isOk())
  //				.andExpect(jsonPath("$.token").exists())
  //				.andExpect(jsonPath("$.resetPasswordRequired", is(true)))
  //				.andReturn();
  //
  //		SignInResponse loginResponse = new Gson().fromJson(result.getResponse().getContentAsString(), SignInResponse.class);
  //
  //		token = loginResponse.getToken();
  //	}
  //
  //	@Test
  //	@Order(21)
  //	public void givenMissingRootRole_whenChangeRole_thenReturnForbidden() throws Exception {
  //		log.info("givenMissingRootRole_whenChangeRole_thenReturnForbidden()");
  //
  //		// given - precondition or setup
  //		int id = 1; // Id of root user
  //		UpdateUserDTO updateDTO = new UpdateUserDTO();
  //		
  //		Role roleUser = new Role();
  //		roleUser.setId(2);
  //		roleUser.setType(RoleType.ROLE_USER);
  //		
  //		List<Role> roles = Arrays.asList(roleUser);
  //
  //		updateDTO.setRoles(roles);
  //
  //		// when -  action or the behavior that we are going test
  //		ResultActions response = api.perform(put("/api/v1/users/{id}", id)
  //				.header("Authorization", "Bearer " + token)
  //				.contentType(MediaType.APPLICATION_JSON)
  //				.content(objectMapper.writeValueAsString(updateDTO)));
  //
  //
  //		// then - verify the output
  //		response
  //		.andDo(print())
  //		.andExpect(status().is(403))
  //		.andExpect(jsonPath("$.detail", is("You are not allowed to modify roles of your own!")));
  //	}
  //	
  //	@Test
  //	@Order(24)
  //	public void givenUserObject_whenSignUpUser_thenReturnSavedUser() throws Exception {
  //
  //		// given - precondition or setup
  //		
  //		username = "TestUser1";
  //		password = "TestPassword1";
  //
  //		SignUpDTO signupDTO = new SignUpDTO();
  //		signupDTO.setUsername(username);
  //		signupDTO.setPassword(password);
  //
  //		// when - action or behavior that we are going test
  //		ResultActions response = api.perform(post("/api/v1/users/add")
  //				.header("Authorization", "Bearer " + token)
  //				.contentType(MediaType.APPLICATION_JSON)
  //				.content(objectMapper.writeValueAsString(signupDTO)));
  //
  //		// then - verify the result or output using assert statements
  //		MvcResult result = response.andDo(print()).
  //		andExpect(status().isOk())
  //		.andExpect(jsonPath("$.username", is(signupDTO.getUsername())))
  //		.andExpect(jsonPath("$.resetPasswordRequired", is(true)))
  //		.andExpect(jsonPath("$.active", is(true)))
  //		.andReturn();
  //		
  //		UserDTO userResponse = new Gson().fromJson(result.getResponse().getContentAsString(), UserDTO.class);
  //
  //		userId = userResponse.getId();
  //		user = carService.findById(userResponse.getId());
  //		
  //		assert user.get().roleMatchedDTO(userResponse.getRoles()) : "Error: roles does not match!";
  //	}
  //	
  //	@Test
  //	@Order(25)
  //	public void givenUserObject_whenSignUpSameUser_thenReturnNotOk() throws Exception {
  //
  //		// given - precondition or setup
  //		
  //		username = "TestUser1";
  //		password = "TestPassword1";
  //
  //		SignUpDTO signupDTO = new SignUpDTO();
  //		signupDTO.setUsername(username);
  //		signupDTO.setPassword(password);
  //
  //		// when - action or behavior that we are going test
  //		ResultActions response = api.perform(post("/api/v1/users/add")
  //				.header("Authorization", "Bearer " + token)
  //				.contentType(MediaType.APPLICATION_JSON)
  //				.content(objectMapper.writeValueAsString(signupDTO)));
  //
  //		// then - verify the result or output using assert statements
  //		response.andDo(print()).
  //		andExpect(status().isBadRequest());
  //		
  //	}
  //	
  //	@Test
  //	@Order(26)
  //	public void givenToken_whenSignOut_thenReturnOk2() throws Exception {
  //		log.info("givenToken_whenSignOut_thenReturnOk2()");
  //
  //		// when - action or behavior that we are going test
  //		ResultActions response = api.perform(post("/api/v1/auth/signout")
  //				.header("Authorization", "Bearer " + token)
  //				.contentType(MediaType.APPLICATION_JSON));
  //
  //		// then - verify the result or output using assert statements
  //		response
  //		.andDo(print())
  //		.andExpect(status().isOk())
  //		.andExpect(jsonPath("$.message", is("Signout successfully!")));
  //	}
  //
  //	@Test
  //	@Order(27)
  //	public void givenTestUser1AndWrongPassword_whenSignIn_thenReturnToken() throws Exception {
  //		log.info("givenTestUser1AndWrongPassword_whenSignIn_thenReturnToken()");
  //
  //		username = "TestUser1";
  //		password = "wrongpassword";
  //
  //		// given - precondition or setup
  //		SignInDTO userSignIn = new SignInDTO();
  //		userSignIn.setUsername(username);
  //		userSignIn.setPassword(password);
  //
  //		// when - action or behavior that we are going test
  //		ResultActions response = api.perform(post("/api/v1/auth/signin")
  //				.contentType(MediaType.APPLICATION_JSON)
  //				.content(objectMapper.writeValueAsString(userSignIn)));
  //
  //
  //		// then - verify the result or output using assert statements
  //		response
  //		.andDo(print())
  //		.andExpect(status().isUnauthorized())
  //		.andExpect(jsonPath("$.token").doesNotExist());
  //	}
  //
  //	@Test
  //	@Order(28)
  //	public void givenTestUser1_whenSignIn_thenReturnToken() throws Exception {
  //		log.info("givenTestUser1_whenSignIn_thenReturnToken()");
  //
  //		username = "TestUser1";
  //		password = "TestPassword1";
  //
  //		// given - precondition or setup
  //		SignInDTO userSignIn = new SignInDTO();
  //		userSignIn.setUsername(username);
  //		userSignIn.setPassword(password);
  //
  //		// when - action or behavior that we are going test
  //		ResultActions response = api.perform(post("/api/v1/auth/signin")
  //				.contentType(MediaType.APPLICATION_JSON)
  //				.content(objectMapper.writeValueAsString(userSignIn)));
  //
  //
  //		// then - verify the result or output using assert statements
  //		MvcResult result = response
  //				.andDo(print())
  //				.andExpect(status().isOk())
  //				.andExpect(jsonPath("$.token").exists())
  //				.andExpect(jsonPath("$.resetPasswordRequired", is(true)))
  //				.andReturn();
  //
  //		SignInResponse loginResponse = new Gson().fromJson(result.getResponse().getContentAsString(), SignInResponse.class);
  //
  //		token = loginResponse.getToken();
  //	}
  //
  //	@Test
  //	@Order(29)
  //	public void givenToken_whenChangePassword_thenReturnOk2() throws Exception {
  //		log.info("givenToken_whenChangePassword_thenReturnOk2()");
  //
  //		// given - precondition or setup
  //		int id = userId; // Id of TestUser1
  //		String oldPassword = "TestPassword1";
  //		String newPassword = "mypassword";
  //
  //		UpdateUserDTO updateDTO = new UpdateUserDTO();
  //		updateDTO.setOldPassword(oldPassword);
  //		updateDTO.setNewPassword(newPassword);
  //
  //		// when -  action or the behavior that we are going test
  //		ResultActions response = api.perform(put("/api/v1/users/{id}", id)
  //				.header("Authorization", "Bearer " + token)
  //				.contentType(MediaType.APPLICATION_JSON)
  //				.content(objectMapper.writeValueAsString(updateDTO)));
  //
  //
  //		// then - verify the output
  //		response
  //		.andDo(print())
  //		.andExpect(status().isOk())
  //		.andExpect(jsonPath("$.id", is(id)));
  //	}

}

