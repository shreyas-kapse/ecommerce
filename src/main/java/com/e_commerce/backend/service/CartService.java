package com.e_commerce.backend.service;

import com.e_commerce.backend.dtos.CartDTO;
import com.e_commerce.backend.dtos.CartDTOResponse;
import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.Repository.CartItemRepository;
import com.e_commerce.backend.Repository.CartRepository;
import com.e_commerce.backend.Repository.ProductRepository;
import com.e_commerce.backend.Repository.UserRepository;
import com.e_commerce.backend.enity.CartEntity;
import com.e_commerce.backend.enity.CartItem;
import com.e_commerce.backend.enity.ProductEntity;
import com.e_commerce.backend.enity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
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

            log.info("Processing add product to cart request with product id {} for user with id {}", productId, userIdObject.toString());

            Optional<CartEntity> cartEntityOptional = cartRepository.findByUserId(Long.valueOf(userIdObject.toString()));
            CartEntity cart;

            cart = cartEntityOptional.orElseGet(() -> createNewCart(Long.valueOf(userIdObject.toString())));
            if (cart == null) {
                DefaultResponse.builder()
                        .success(false)
                        .message("Error occurred while removing product")
                        .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                        .build();
            }
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

                log.info("Request is already fulfilled to add product to cart with product id {} for user with id {}", productId, userIdObject.toString());
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

            log.info("Successfully processed request to add product to cart with product id {} for user with id {}", productId, userIdObject.toString());
            return DefaultResponse.builder()
                    .success(true)
                    .message("Product added successfully")
                    .httpStatus(Optional.of(HttpStatus.CREATED))
                    .build();
        } catch (Exception e) {
            log.error("Error occurred while processing request to add product to cart with error {} ", e.getMessage());
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

            log.info("Processing remove product from cart request with product id {} for user with id {}", productId, userIdObject.toString());
            Optional<CartEntity> cartEntityOptional = cartRepository.findByUserId(Long.valueOf(userIdObject.toString()));
            CartEntity cart;

            cart = cartEntityOptional.orElseGet(() -> createNewCart(Long.valueOf(userIdObject.toString())));
            if (cart == null) {
                DefaultResponse.builder()
                        .success(false)
                        .message("Error occurred while removing product")
                        .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                        .build();
            }
            cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));

            cartRepository.save(cart);

            log.info("Successfully processed request to remove product from cart with product id {} for user with id {}", productId, userIdObject.toString());
            return DefaultResponse.builder()
                    .success(true)
                    .message("Product removed successfully")
                    .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                    .build();
        } catch (Exception e) {
            log.error("Error occurred while processing request to remove product from cart with error {} ", e.getMessage());
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
            log.info("Processing clear cart request for user with id {}", userIdObject.toString());

            Optional<CartEntity> cartEntityOptional = cartRepository.findByUserId(Long.valueOf(userIdObject.toString()));
            CartEntity cart;

            cart = cartEntityOptional.orElseGet(() -> createNewCart(Long.valueOf(userIdObject.toString())));
            if (cart == null) {
                return DefaultResponse.builder()
                        .success(false)
                        .message("Error occurred while clearing the cart")
                        .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                        .build();
            }
            cart.getCartItems().clear();

            cartRepository.save(cart);

            log.info("Successfully processed clear cart request for user with id {}", userIdObject.toString());
            return DefaultResponse.builder()
                    .success(true)
                    .message("Cart cleared successfully")
                    .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                    .build();

        } catch (Exception e) {
            log.error("Error occurred while processing clear cart request with error {} ", e.getMessage());
            return DefaultResponse.builder()
                    .success(false)
                    .message("Error occurred while clearing the cart")
                    .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                    .build();
        }
    }

    @Override
    public CartDTOResponse getCart(String token) {
        try {
            Object userIdObject = jwtService.extractId(token);
            log.info("Processing get cart request for user with id {}", userIdObject.toString());

            Optional<CartEntity> cartEntityOptional = cartRepository.findByUserId(Long.valueOf(userIdObject.toString()));
            CartEntity cart;
            if (cartEntityOptional.isEmpty()) {
                cart = createNewCart(Long.valueOf(userIdObject.toString()));
                if (cart == null) {
                    return CartDTOResponse.builder()
                            .response(DefaultResponse.builder()
                                    .success(false)
                                    .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                                    .build())
                            .build();
                }
                cartRepository.save(cart);

                return CartDTOResponse.builder()
                        .response(DefaultResponse.builder()
                                .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                                .build())
                        .build();
            } else {
                cart = cartEntityOptional.get();
                CartDTO cartDTO = CartDTO.builder()
                        .cartId(cart.getId())
                        .cartItems(cart.getCartItems())
                        .createdAt(cart.getCreatedAt())
                        .updatedAt(cart.getUpdatedAt())
                        .build();

                log.info("Successfully processed get cart request for user with id {}", userIdObject.toString());
                return CartDTOResponse.builder()
                        .response(DefaultResponse.builder()
                                .success(true)
                                .build())
                        .cart(Optional.of(cartDTO))
                        .build();
            }
        } catch (Exception exception) {
            log.error("Error occurred while processing get cart request with error {} ", exception.getMessage());
            return CartDTOResponse.builder()
                    .response(DefaultResponse.builder()
                            .success(false)
                            .message("Error occurred while processing get cart request")
                            .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                            .build())
                    .build();
        }
    }

    public CartEntity createNewCart(Long userId) {
        try {
            log.info("Processing create new cart request for user with id {}", userId);
            CartEntity cart = new CartEntity();
            Optional<UserEntity> user = userRepository.findById(userId);
            cart.setUser(user.get());

            log.info("Successfully processed create cart request for user with id {}", userId);
            return cartRepository.save(cart);
        } catch (Exception exception) {
            log.error("Error occurred while processing create cart request for user id {}, error {} ", userId, exception.getMessage());
        }
        return null;
    }
}