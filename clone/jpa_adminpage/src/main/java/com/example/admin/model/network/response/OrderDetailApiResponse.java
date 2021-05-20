package com.example.admin.model.network.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailApiResponse {

    private Long id;

    private String status;

    private LocalDateTime arrivalDate;

    private BigDecimal totalPrice;

    private Integer quantity;

    private LocalDateTime createdAt;

    private String createdBy;

    private Long itemId;

    private Long orderGroupId;
}
