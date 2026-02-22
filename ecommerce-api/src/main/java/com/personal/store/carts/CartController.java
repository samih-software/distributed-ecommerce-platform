package com.personal.store.carts;

import com.personal.store.common.ErrorDto;
import com.personal.store.products.ProductNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
@Tag(name = "Carts") // nice naming for ui-swagger
public class CartController {

    private final CartService cartService;


    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder builder
    ) {

        var cartDt0 = cartService.createCart();

        var uri = builder.path("/carts/{id}").buildAndExpand(cartDt0.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDt0);

    }



    @PostMapping("/{cartId}/items")
    @Operation(summary = "Adds a product to the cart.")
    public ResponseEntity<CartItemDto> addToCart(
            @Parameter(description = "The id of the cart")
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRequest request) {

        var cartItemDto = cartService.addToCart(cartId, request.getProductId());

        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable UUID cartId) {
        return cartService.getCart(cartId);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public CartItemDto updateItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request){

        return cartService.updateItem(cartId, productId, request.getQuantity());
    }



    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable UUID cartId,
            @PathVariable Long productId
    ){
        cartService.removeItem(cartId, productId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(@PathVariable UUID cartId) {

        cartService.clearCart(cartId);

        return ResponseEntity.noContent().build();

    }


    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCartNotFound(){

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        new ErrorDto("Cart not found")
                );
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFound(){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        new ErrorDto("Product not found in the cart")
                );
    }
}
