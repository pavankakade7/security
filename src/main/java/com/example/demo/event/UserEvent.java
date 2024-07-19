package com.example.demo.event;


import com.example.demo.entity.UserEntity;
import com.example.demo.enumeration.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
@AllArgsConstructor
public class UserEvent {
    private UserEntity user;
    private EventType type;
    private Map<?,?> data;
}
