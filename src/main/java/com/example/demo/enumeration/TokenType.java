package com.example.demo.enumeration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public enum TokenType {
    ACCESS("access-token"),
    REFRESH("refresh-token");

    private final String value;

    TokenType(String value){
        this.value = value;
    }

    public  String getValue(){
        return this.value;
    }
}
