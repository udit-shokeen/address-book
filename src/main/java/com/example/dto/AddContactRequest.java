package com.example.dto;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddContactRequest {
    private String name;
    private String phone;
    private String email;
}
