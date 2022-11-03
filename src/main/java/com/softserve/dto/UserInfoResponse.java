package com.softserve.dto;

import com.softserve.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {

    private Integer id;

    private String userName;

    private String email;

    private List<String> roles;

    private String token;
}
