package com.nisal.Queue.Management.System.response;

import com.nisal.Queue.Management.System.dto.UserDTO;
import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;
    private String message;
    private UserDTO user;

}
