package com.example.utils;

import com.example.dto.ContactCard;

import java.util.*;
import java.util.stream.Collectors;

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

    public static List<String> getNGrams(String input, int k) {
        if (input == null || input.length() < k) return List.of();
        List<String> ngrams = new ArrayList<>();
        for (int i = 0; i <= input.length() - k; i++) {
            ngrams.add(input.substring(i, i + k));
        }
        return ngrams;
    }

    public static List<String> tokenize(String input) {
        if (input == null) return List.of();
        return Arrays.stream(input.split("[@._\\-\\s,]+"))
                .filter(s -> !s.isBlank())
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
