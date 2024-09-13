package com.e_commerce.backend.service;

import com.e_commerce.backend.DefaultResponse;
import com.e_commerce.backend.EmailService;
import com.e_commerce.backend.Repository.CartRepository;
import com.e_commerce.backend.Repository.OrderRepository;
import com.e_commerce.backend.Repository.ProductRepository;
import com.e_commerce.backend.Repository.UserRepository;
import com.e_commerce.backend.enity.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    @Override
    public DefaultResponse placeOrder(String token) {
        try {
            Object userIdObject = jwtService.extractId(token);

            Optional<CartEntity> cartEntityOptional = cartRepository.findByUserId(Long.valueOf(userIdObject.toString()));
            CartEntity cart;
            if (cartEntityOptional.isEmpty()) {
                cart = cartService.createNewCart(Long.valueOf(userIdObject.toString()));
                cartRepository.save(cart);
                return DefaultResponse.builder()
                        .httpStatus(Optional.of(HttpStatus.NO_CONTENT))
                        .build();
            } else {
                cart = cartEntityOptional.get();
                if (cart.getCartItems().isEmpty()) {
                    throw new IllegalStateException("Cart is empty.");
                }

                Optional<UserEntity> user = userRepository.findById(Long.valueOf(userIdObject.toString()));
                OrderEntity order = new OrderEntity();
                order.setUser(user.get());
                order.setStatus("PENDING");

                double totalPrice = 0.0;
                for (CartItem cartItem : cart.getCartItems()) {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setMerchantId(cartItem.getProduct().getMerchant().getId());
                    orderItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());

                    totalPrice += orderItem.getPrice();

                    order.getOrderItems().add(orderItem);

                    ProductEntity product = cartItem.getProduct();
                    if (product.getQuantity() <= 0) {
                        product.setAvailable(false);
                    }
                    product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                    productRepository.save(product);
                }

                order.setTotalPrice(totalPrice);


                UserEntity userEntity = user.get();

                order.setAddressLine1(userEntity.getAddressLine1());
                order.setAddressLine2(userEntity.getAddressLine2());
                order.setPhoneNumber(userEntity.getPhoneNumber());
                order.setCity(userEntity.getCity());
                order.setStatus(userEntity.getState());
                order.setPostalCode(userEntity.getPostalCode());
                order.setCountry(userEntity.getCountry());

                StringBuilder address = new StringBuilder();
                address.append(userEntity.getAddressLine1())
                        .append(", ")
                        .append(userEntity.getAddressLine2())
                        .append(", ")
                        .append(userEntity.getCity())
                        .append(", ")
                        .append(userEntity.getState())
                        .append(", ")
                        .append(userEntity.getPostalCode())
                        .append(", ")
                        .append(userEntity.getCountry());

                OrderEntity orderEntity = orderRepository.save(order);

                StringBuilder subject = new StringBuilder();
                subject.append("Thank You for Your Purchase! ")
                        .append(userEntity.getFirstName() + " " + userEntity.getLastName())
                        .append(" Order #" + orderEntity.getId() + " confirmed");
                emailService.orderConfirmationMail(userEntity.getUsername(), String.valueOf(subject), "admin@ecommerce.com", userEntity.getFirstName() + " " + userEntity.getLastName(), orderEntity.getId().toString(), orderEntity.getOrderDate().toString(), orderEntity.getOrderItems(), address.toString(), orderEntity.getOrderDate().plusDays(2).toLocalDate().toString());

                cartService.clearCart(token);
                return DefaultResponse.builder()
                        .success(true)
                        .message("Order placed successfully")
                        .build();
            }
        } catch (Exception e) {
            return DefaultResponse.builder()
                    .success(false)
                    .message("Error occurred while placing the order")
                    .httpStatus(Optional.of(HttpStatus.INTERNAL_SERVER_ERROR))
                    .build();
        }
    }
}
