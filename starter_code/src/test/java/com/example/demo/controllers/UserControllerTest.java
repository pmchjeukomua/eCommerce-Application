package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Transactional
public class UserControllerTest {
	
	@Autowired
    private UserRepository userRepo;
	
	@Autowired
    private CartRepository cartRepo;
	
	@Autowired
    private UserController userController;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void creatUserSuccess(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("hoannt12");
        createUserRequest.setPassword("password1@");
        createUserRequest.setConfirmPassword("password1@");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        User u = response.getBody();
        assertEquals(createUserRequest.getUsername(), u.getUsername());
    }

    @Test
    public void creatUserFailed(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("hoannt12");
        createUserRequest.setPassword("1");
        createUserRequest.setConfirmPassword("1");
        ResponseEntity<User> response = userController.createUser(createUserRequest);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void findByIdTest(){
        User user = createUser();
        user = userRepo.save(user);
        ResponseEntity<User> response = userController.findById(user.getId());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void findByUsernameTest(){
        User user = createUser();
        user = userRepo.save(user);
        ResponseEntity<User> response = userController.findByUserName(user.getUsername());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        assertEquals(user.getId(), response.getBody().getId());
        assertEquals(user.getUsername(), response.getBody().getUsername());
        assertEquals(user.getPassword(), user.getPassword());
    }


    private User createUser(){
        User user = new User();
        user.setUsername("hoannt12");
        user.setPassword(bCryptPasswordEncoder.encode("password1@"));
        return user;
    }

}