package com.avidbikers.data.dto;

import com.avidbikers.data.model.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    @NotBlank
    private String id;
    @NotBlank
    private BigDecimal total;
    private Map<String, Item> items;
}
