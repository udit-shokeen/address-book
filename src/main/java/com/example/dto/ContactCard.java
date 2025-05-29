package com.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ContactCard {
    String id;
    String name;
    String email;
    String phone;
}
