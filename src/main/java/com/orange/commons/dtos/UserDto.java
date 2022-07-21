package com.orange.commons.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonSerialize
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String email;
    private String pwd;

}
