package com.avidbikers.data.dto;

import com.avidbikers.data.model.Address;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class OrderDto {
    @NotBlank
    private String orderID;
    @NotBlank(message = "Address field cannot be blank")
    private Address deliveryAddress;
    @NotBlank
    private BigDecimal orderTotal;
}
