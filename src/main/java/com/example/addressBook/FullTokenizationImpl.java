package com.example.addressBook;

import com.example.utils.Utils;
import com.example.dto.AddContactRequest;
import com.example.dto.ContactCard;
import com.example.dto.DeletedResponse;
import com.example.dto.FetchRequest;
import java.util.*;
import java.util.stream.Collectors;


public class FullTokenizationImpl implements AddressBook{
    /**
        FullTokenizationImpl uses inverted indexing with all possible tokens
        Takes O(n^2) time & ~O(n^3) space to generate all possible tokens from the name, email and phone number
        and O(1) time to retrieve them.
    **/
    private static final Map<String, ContactCard> contactCardMap = new HashMap<>();     //  mapping of _id to ContactCard
    private static final Map<String, Set<String>> tokenToContactCardIds = new HashMap<>(); // mapping of token to set of ContactCard _ids

    @Override
    public List<ContactCard> insert(List<AddContactRequest> requests) {
        return requests.stream()
                .map(request -> {
                    //  create a ContactCard & save it to AddressBook
                    ContactCard contactCard = ContactCard.builder()
                            .id(Utils.generateId())
                            .name(request.getName())
                            .email(request.getEmail())
                            .phone(request.getPhone())
                            .build();
                    contactCardMap.put(contactCard.getId(), contactCard);
                    //  generate all possible tokens from the name, email and phone number & inverted index them
                    Utils.getAllSubstrings(contactCard.getName()).forEach(token -> {
                        tokenToContactCardIds.computeIfAbsent(token, k -> new HashSet<>()).add(contactCard.getId());
                    });
                    Utils.getAllSubstrings(contactCard.getEmail()).forEach(token -> {
                        tokenToContactCardIds.computeIfAbsent(token, k -> new HashSet<>()).add(contactCard.getId());
                    });
                    Utils.getAllSubstrings(contactCard.getPhone()).forEach(token -> {
                        tokenToContactCardIds.computeIfAbsent(token, k -> new HashSet<>()).add(contactCard.getId());
                    });
                    return contactCard;
                }).collect(Collectors.toList());
    }

    @Override
    public List<ContactCard> find(FetchRequest request) {
        return tokenToContactCardIds.getOrDefault(request.getQuery(), new HashSet<>())
                .stream()
                .map(contactCardMap::get)
                .collect(Collectors.toList());
    }

    @Override
    public DeletedResponse delete(List<String> requests) {
        long count =
                requests.stream()
                        .map(request -> {
                            if(contactCardMap.containsKey(request)) {
                                ContactCard contactCard = contactCardMap.get(request);
                                //  remove from address book
                                contactCardMap.remove(request);
                                //  remove inverted indexing
                                Utils.getAllSubstrings(contactCard.getName())
                                        .forEach(token -> tokenToContactCardIds.get(token).remove(request));
                                Utils.getAllSubstrings(contactCard.getEmail())
                                        .forEach(token -> tokenToContactCardIds.get(token).remove(request));
                                Utils.getAllSubstrings(contactCard.getPhone())
                                        .forEach(token -> tokenToContactCardIds.get(token).remove(request));
                                return contactCard;
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .count();
        return DeletedResponse.builder()
                .deleted((int) count)
                .build();
    }

    @Override
    public List<ContactCard> update(List<ContactCard> requests) {
        return requests.stream()
                .map(request -> {
                    //  update the contact card and save it to db
                    ContactCard contactCard = contactCardMap.get(request.getId());
                    Utils.updateContactDetails(contactCard, request);
                    contactCardMap.put(contactCard.getId(), contactCard);
                    //  generate all possible tokens from the name, email and phone number & inverted index them
                    Utils.getAllSubstrings(contactCard.getName()).forEach(token -> {
                        tokenToContactCardIds.computeIfAbsent(token, k -> new HashSet<>()).add(contactCard.getId());
                    });
                    Utils.getAllSubstrings(contactCard.getEmail()).forEach(token -> {
                        tokenToContactCardIds.computeIfAbsent(token, k -> new HashSet<>()).add(contactCard.getId());
                    });
                    Utils.getAllSubstrings(contactCard.getPhone()).forEach(token -> {
                        tokenToContactCardIds.computeIfAbsent(token, k -> new HashSet<>()).add(contactCard.getId());
                    });
                    return contactCard;
                }).collect(Collectors.toList());
    }
}
