package com.e_commerce.backend.service;

import com.e_commerce.backend.CartDTOResponse;
import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.Repository.CartItemRepository;
import com.e_commerce.backend.Repository.CartRepository;
import com.e_commerce.backend.Repository.ProductRepository;
import com.e_commerce.backend.Repository.UserRepository;
import com.e_commerce.backend.enity.CartEntity;
import com.e_commerce.backend.enity.CartItem;
import com.e_commerce.backend.enity.ProductEntity;
import com.e_commerce.backend.enity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public DefaultResponse addProductToCart(Long productId, int quantity, String token) {
        try {
            Object userIdObject = jwtService.extractId(token);

            Optional<CartEntity> cartEntityOptional = cartRepository.findByUserId(Long.valueOf(userIdObject.toString()));
            CartEntity cart;

            cart = cartEntityOptional.orElseGet(() -> createNewCart(Long.valueOf(userIdObject.toString())));

            Optional<ProductEntity> product = productRepository.findById(productId);

            if (product.isEmpty()) {
                return DefaultResponse.builder()
                        .success(false)
                        .message("Product not found")
                        .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                        .build();
            }
            Optional<CartItem> existingItem = cart.getCartItems()
                    .stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst();

            if (existingItem.isPresent()) {
                CartItem cartItem = existingItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemRepository.save(cartItem);
                return DefaultResponse.builder()
                        .success(true)
                        .message("Product added successfully")
                        .httpStatus(Optional.of(HttpStatus.CREATED))
                        .build();
            }

            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product.get());
            newItem.setQuantity(quantity);
            newItem.setPrice(BigDecimal.valueOf(product.get().getPrice()));
            cart.getCartItems().add(newItem);

            cartRepository.save(cart);
            return DefaultResponse.builder()
                    .success(true)
                    .message("Product added successfully")
                    .httpStatus(Optional.of(HttpStatus.CREATED))
                    .build();
        } catch (Exception e) {
            return DefaultResponse.builder()
                    .success(false)
                    .message("Error occurred while adding product")
                    .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                    .build();
        }
    }

    @Override
    public DefaultResponse removeProductFromCart(Long productId, String token) {
        try {
            Object userIdObject = jwtService.extractId(token);

            Optional<CartEntity> cartEntityOptional = cartRepository.findByUserId(Long.valueOf(userIdObject.toString()));
            CartEntity cart;

            cart = cartEntityOptional.orElseGet(() -> createNewCart(Long.valueOf(userIdObject.toString())));
            cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));
            cartRepository.save(cart);
            return DefaultResponse.builder()
                    .success(true)
                    .message("Product removed successfully")
                    .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                    .build();
        } catch (Exception e) {
            return DefaultResponse.builder()
                    .success(false)
                    .message("Error occurred while removing product")
                    .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                    .build();
        }
    }

    @Override
    public DefaultResponse clearCart(String token) {
        try {
            Object userIdObject = jwtService.extractId(token);

            Optional<CartEntity> cartEntityOptional = cartRepository.findByUserId(Long.valueOf(userIdObject.toString()));
            CartEntity cart;

            cart = cartEntityOptional.orElseGet(() -> createNewCart(Long.valueOf(userIdObject.toString())));
            cart.getCartItems().clear();
            cartRepository.save(cart);

            return DefaultResponse.builder()
                    .success(true)
                    .message("Cart cleared successfully")
                    .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                    .build();

        } catch (Exception e) {
            return DefaultResponse.builder()
                    .success(false)
                    .message("Error occurred while clearing the cart")
                    .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                    .build();
        }
    }

    @Override
    public CartDTOResponse getCart(String token) {
        Object userIdObject = jwtService.extractId(token);

        Optional<CartEntity> cartEntityOptional = cartRepository.findByUserId(Long.valueOf(userIdObject.toString()));
        CartEntity cart;
        if (cartEntityOptional.isEmpty()) {
            cart = createNewCart(Long.valueOf(userIdObject.toString()));
            cartRepository.save(cart);
            return CartDTOResponse.builder()
                    .response(DefaultResponse.builder()
                            .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                            .build())
                    .build();
        } else {
            cart = cartEntityOptional.get();
            return CartDTOResponse.builder()
                    .response(DefaultResponse.builder()
                            .success(true)
                            .build())
                    .cart(Optional.of(cart))
                    .build();
        }
    }

    private CartEntity createNewCart(Long userId) {
        CartEntity cart = new CartEntity();
        Optional<UserEntity> user = userRepository.findById(userId);
        cart.setUser(user.get());
        return cartRepository.save(cart);
    }
}