package com.example.admin.model.network.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryApiRequest {

    private Long id;

    private String type;

    private String title;

    private LocalDateTime createdAt;

    private String createdBy;

}
