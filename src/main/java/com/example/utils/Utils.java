package com.example.utils;

import com.example.dto.ContactCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Utils {
    public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public static List<String> getAllSubstrings(String input) {
        List<String> substrings = new ArrayList<>();
        int n = input.length();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                substrings.add(input.substring(i, j));
            }
        }
        return substrings;
    }

    public static void updateContactDetails(ContactCard contactCard, ContactCard request) {
        if(Objects.nonNull(request.getName()))
            contactCard.setName(request.getName());
        if(Objects.nonNull(request.getEmail()))
            contactCard.setEmail(request.getEmail());
        if(Objects.nonNull(request.getPhone()))
            contactCard.setPhone(request.getPhone());
    }
}
