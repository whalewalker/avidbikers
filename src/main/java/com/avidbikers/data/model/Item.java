package com.avidbikers.data.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Product product;
    private int quantity = 1;
    private BigDecimal itemTotal;

    private BigDecimal updateProductAmount(int quantity) {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public void increaseQuantity(int quantity){
        this.quantity += quantity;
        itemTotal = updateProductAmount(quantity);
    }

    public void decreaseQuantity(int quantity){
        this.quantity -= quantity;
        itemTotal = updateProductAmount(quantity);
    }

}
