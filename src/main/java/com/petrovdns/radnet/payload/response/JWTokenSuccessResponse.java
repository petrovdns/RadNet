package com.petrovdns.radnet.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTokenSuccessResponse {
    private boolean succsess;
    private String token;
}

