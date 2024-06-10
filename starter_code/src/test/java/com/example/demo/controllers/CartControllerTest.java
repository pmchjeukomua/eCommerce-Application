package com.example.demo.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
@Transactional
public class CartControllerTest {

	@Autowired
    private UserRepository userRepo;
	
	@Autowired
    private ItemRepository itemRepo;
	
	@Autowired
    private CartRepository cartRepo;
	
	@Autowired
    private CartController cartController;
	
    @Test
    public void addToCartSuccessTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        user = userRepo.save(user);
        item = itemRepo.save(item);

        ModifyCartRequest modifyCartRequest = newModifyCartRequest("hoannt12", 1, 1);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());

        Cart responseCart = responseEntity.getBody();

        assertNotNull(responseCart);

        List<Item> items = responseCart.getItems();
        assertNotNull(items);
    }

    @Test
    public void addToCartFailedByInvalidUserNameTest(){

        User user = createUser();
        Item item = createItem();
        
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        
        user.setCart(cart);

        user = userRepo.save(user);
        item = itemRepo.save(item);

        ModifyCartRequest modifyCartRequest = newModifyCartRequest(user.getUsername() + "xxx", 1, 1);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void addToCartFailedByInvalidItemIDTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        user = userRepo.save(user);
//        item = itemRepo.save(item);

        ModifyCartRequest modifyCartRequest = newModifyCartRequest("hoannt12", 999999999, 2);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void removeCartSuccessTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        user = userRepo.save(user);
        item = itemRepo.save(item);

        ModifyCartRequest modifyCartRequest = newModifyCartRequest(user.getUsername(), item.getId(), 2);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    public void removeCartFailedByUserNameTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        user = userRepo.save(user);
        item = itemRepo.save(item);

        ModifyCartRequest modifyCartRequest = newModifyCartRequest(user.getUsername() + "xxx", item.getId(), 2);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }

    @Test
    public void removeCartFailedByItemIdTest(){

        User user = createUser();
        Item item = createItem();
        Cart cart = user.getCart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        user = userRepo.save(user);
//        item = itemRepo.save(item);

        ModifyCartRequest modifyCartRequest = newModifyCartRequest(user.getUsername(), 999999999, 2);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());

    }
    public User createUser(){
        User user = new User();
        user.setUsername("hoannt12");
        user.setPassword("Password");

        Cart cart = new Cart();
        cart.setUser(null);
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.valueOf(0.0));
        
        cart = cartRepo.save(cart);
        
        user.setCart(cart);

        return user;
    }

    public static Item createItem(){
        Item item = new Item();
        item.setName("New Item");
        item.setDescription("Decription item");
        item.setPrice(BigDecimal.valueOf(20.0));
        return item;
    }

    public static ModifyCartRequest newModifyCartRequest(String username, long itemId, int quantity){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(username);
        modifyCartRequest.setItemId(itemId);
        modifyCartRequest.setQuantity(quantity);
        return modifyCartRequest;
    }
}
