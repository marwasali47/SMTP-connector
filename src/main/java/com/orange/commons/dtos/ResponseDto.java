package com.orange.commons.dtos;

import lombok.Data;
import lombok.ToString;

/**
 * Created by Mohamed Gaber on Oct, 2018
 */

@Data
@ToString
public class ResponseDto {
    private Integer statusCode;
    private String clientMessage;
    private String developerMessage;
}
