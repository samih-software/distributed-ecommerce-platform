package com.personal.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal.store.auth.LoginRequest;
import com.personal.store.carts.AddItemToCartRequest;
import com.personal.store.carts.CartItemDto;
import com.personal.store.products.Product;
import com.personal.store.products.ProductRepository;
import com.personal.store.payments.CheckoutRequest;
import com.personal.store.payments.CheckoutResponse;
import com.personal.store.users.Role;
import com.personal.store.users.User;
import com.personal.store.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class OrderFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String accessToken;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setup() throws Exception {
        userRepository.deleteAll();
        productRepository.deleteAll();

        User user = new User();
        user.setEmail("sa@gmail.com");
        user.setName("Samih");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole(Role.USER);
        userRepository.save(user);

        product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice(BigDecimal.valueOf(10.0));
        productRepository.save(product1);

        product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice(BigDecimal.valueOf(15.0));
        productRepository.save(product2);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("sa@gmail.com");
        loginRequest.setPassword("123456");

        String loginJson = objectMapper.writeValueAsString(loginRequest);

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn();

        accessToken = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("token").asText();
    }

    @Test
    void createCart_shouldReturnCartId() throws Exception {
        MvcResult cartResult = mockMvc.perform(post("/carts")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn();

        String responseJson = cartResult.getResponse().getContentAsString();
        UUID cartId = UUID.fromString(objectMapper.readTree(responseJson).get("id").asText());
        assertNotNull(cartId);
    }

    @Test
    void addItemsToCart_shouldReturnCartItems() throws Exception {
        MvcResult cartResult = mockMvc.perform(post("/carts")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String cartJson = cartResult.getResponse().getContentAsString();
        UUID cartId = UUID.fromString(objectMapper.readTree(cartJson).get("id").asText());

        AddItemToCartRequest addItem1 = new AddItemToCartRequest();
        addItem1.setProductId(product1.getId());
        String addItem1Json = objectMapper.writeValueAsString(addItem1);

        MvcResult item1Result = mockMvc.perform(post("/carts/" + cartId + "/items")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addItem1Json))
                .andExpect(status().isCreated())
                .andReturn();

        String item1Json = item1Result.getResponse().getContentAsString();
        CartItemDto cartItem1 = objectMapper.readValue(item1Json, CartItemDto.class);

        assertEquals(product1.getId(), cartItem1.getProduct().getId());
        assertEquals(1, cartItem1.getQuantity());

        AddItemToCartRequest addItem2 = new AddItemToCartRequest();
        addItem2.setProductId(product2.getId());
        String addItem2Json = objectMapper.writeValueAsString(addItem2);

        MvcResult item2Result = mockMvc.perform(post("/carts/" + cartId + "/items")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addItem2Json))
                .andExpect(status().isCreated())
                .andReturn();

        String item2Json = item2Result.getResponse().getContentAsString();
        CartItemDto cartItem2 = objectMapper.readValue(item2Json, CartItemDto.class);

        assertEquals(product2.getId(), cartItem2.getProduct().getId());
        assertEquals(1, cartItem2.getQuantity());
    }

    @Test
    void checkout_shouldReturnStripeUrl() throws Exception {
        MvcResult cartResult = mockMvc.perform(post("/carts")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String cartJson = cartResult.getResponse().getContentAsString();
        UUID cartId = UUID.fromString(objectMapper.readTree(cartJson).get("id").asText());

        AddItemToCartRequest addItem = new AddItemToCartRequest();
        addItem.setProductId(product1.getId());
        String addItemJson = objectMapper.writeValueAsString(addItem);

        mockMvc.perform(post("/carts/" + cartId + "/items")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addItemJson))
                .andExpect(status().isCreated());

        CheckoutRequest checkoutRequest = new CheckoutRequest();
        checkoutRequest.setCartId(cartId);
        String checkoutJson = objectMapper.writeValueAsString(checkoutRequest);

        MvcResult checkoutResult = mockMvc.perform(post("/checkout")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(checkoutJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkoutUrl").exists())
                .andReturn();

        String checkoutResponseJson = checkoutResult.getResponse().getContentAsString();
        CheckoutResponse checkoutResponse = objectMapper.readValue(checkoutResponseJson, CheckoutResponse.class);

        assertNotNull(checkoutResponse.getCheckoutUrl());
        assertTrue(checkoutResponse.getCheckoutUrl().startsWith("https://checkout.stripe.com/"));
    }
}