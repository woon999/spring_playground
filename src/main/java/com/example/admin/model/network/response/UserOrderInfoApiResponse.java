package com.example.admin.model.network.response;

import com.example.admin.model.entity.OrderGroup;
import com.example.admin.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrderInfoApiResponse {

    private UserApiResponse userApiResponse;

    private List<OrderGroupApiResponse> orderGroupApiResponseList;

}
