package com.avidbikers.data.model;

import com.avidbikers.web.exceptions.CartException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    private String id;
    private Map<String, Item> items = new HashMap<>();
    private BigDecimal total;

    public void addItem(Product product, int quantity){
        String productId = product.getId();
        Item item = new Item();
        item.setProduct(product);

        if (items.containsKey(productId))
            items.get(productId).increaseQuantity(quantity);

        else items.put(productId, item);
        total = calculateTotal();
    }

    public void removeItem(String productId){
        items.remove(productId);
        total = calculateTotal();
    }

    public void removeItem(String productId, int quantity) throws CartException {
        if (items.containsKey(productId)){
            items.get(productId).decreaseQuantity(quantity);
        }else {
            throw new CartException("item is not in cart");
        }
        total = calculateTotal();
    }

    private BigDecimal calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Item cartItem: items.values()) {
            total = total.add(cartItem.getItemTotal());
        }
        return total;
    }

}
