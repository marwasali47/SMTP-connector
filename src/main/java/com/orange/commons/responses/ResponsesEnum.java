package com.orange.commons.responses;

/**
 * Created by Karim on 10/11/2017.
 */
public enum ResponsesEnum {
    Success("Success"),
    Unauthorizerd("Unauthorized...please login"),
    Forbidden("You Can't access that resource"),
    NotFound("There is no such resource"),
    BadRequest("Bad request, make sure you sent all required headers"),
    Failure("Failure occurred...please try again later"),
    ServiceUnavailable("Service Unavailable...please try again later"),
    DataError("The data you entered are wrong"),
    InvalidChannelCred("Invalid channel credentials"),
    UserAlreadyConnected("This user already logged on this channel"),
    WrongChannelId("Channel id must be a number"),
    EmptyBody("Body must not be empty");

    private ResponsesEnum(String value){
        this.value = value;
    }

    private String value;

    public String getValue(){
        return value;
    }
}