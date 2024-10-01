package com.e_commerce.backend.dtos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NamedNativeQuery;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedNativeQuery(
        name = "get_all_orders_of_merchant_by_merchant_id",
        query = "SELECT \n" +
                "\tOI.order_id,\n" +
                "\tOI.price,\n" +
                "\tOI.merchant_id,\n" +
                "\tOI.quantity,\n" +
                "\tOI.product_id,\n" +
                "\tOE.address_line1,\n" +
                "\tOE.address_line2,\n" +
                "\tOE.city,\n" +
                "\tOE.state,\n" +
                "\tOE.country,\n" +
                "\tOE.postal_code,\n" +
                "\tOE.phone_number,\n" +
                "\tUE.first_name,\n" +
                "\tUE.last_name,\n" +
                "\tPE.brand,\n" +
                "\tPE.category,\n" +
                "\tPE.product_name\n" +
                "from order_item as OI\n" +
                "FULL JOIN order_entity as OE ON\n" +
                "OI.order_id = OE.id\n" +
                "FULL JOIN user_entity as UE ON\n" +
                "OE.user_id = UE.id\n" +
                "INNER JOIN product_entity as PE ON\n" +
                "PE.id = OI.product_id WHERE OI.merchant_id=2;",
        resultClass = AllOrdersOfMerchant.class
)
public class AllOrdersOfMerchant {
    @Id
    private Long orderId;
    private Double price;
    private Long merchantId;
    private Integer quantity;
    private Long productId;

    // OrderEntity (OE) fields
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phoneNumber;

    // UserEntity (UE) fields
    private String firstName;
    private String lastName;

    // ProductEntity (PE) fields
    private String brand;
    private String category;
    private String productName;
}
