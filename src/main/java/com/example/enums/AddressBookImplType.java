package com.example.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public enum AddressBookImplType {
    FULL_TOKENIZATION,
    K_GRAM_PLUS_INVERTED_INDEX,
    K_GRAM_PLUS_PARTIAL_TOKENIZATION;

    private static final Map<String, AddressBookImplType> stringToTypeMap = new HashMap<>();

    static{
        Arrays.stream(AddressBookImplType.values())
                .forEach(type -> stringToTypeMap.put(type.name().toLowerCase(), type));
    }

    public static AddressBookImplType getType(String configString) {
        return stringToTypeMap.getOrDefault(configString.toLowerCase(), K_GRAM_PLUS_INVERTED_INDEX);
    }
}
