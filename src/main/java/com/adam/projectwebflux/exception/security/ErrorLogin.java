package com.adam.projectwebflux.exception.security;

import lombok.*;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorLogin {
    private String message;
    private Date timestamp;
}
