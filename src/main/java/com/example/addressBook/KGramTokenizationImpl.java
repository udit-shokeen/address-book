package com.example.addressBook;

import com.example.dto.AddContactRequest;
import com.example.dto.ContactCard;
import com.example.dto.DeletedResponse;
import com.example.dto.FetchRequest;
import com.example.utils.Utils;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class KGramTokenizationImpl implements AddressBook {
    /**
     * KGramTokenizationImpl uses inverted indexing with k-grams and tokens
     * It supports efficient search by breaking down contact details into n-grams and tokens.
     */

    private static final Map<String, ContactCard> contactMap = new ConcurrentHashMap<>();
    private static final Map<String, Set<String>> ngramIndex = new ConcurrentHashMap<>();
    private static final Map<String, Set<String>> tokenIndex = new ConcurrentHashMap<>();
    private static final int[] NGRAM_SIZES = {9, 7, 5, 3, 1};

    @Override
    public List<ContactCard> insert(List<AddContactRequest> requests) {
        return requests.stream().map(req -> {
            String id = Utils.generateId();
            ContactCard card = ContactCard.builder()
                    .id(id)
                    .name(req.getName())
                    .email(req.getEmail())
                    .phone(req.getPhone())
                    .build();

            contactMap.put(id, card);
            indexCard(card);
            return card;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ContactCard> find(FetchRequest request) {
        String query = request.getQuery();

        Set<String> ngramMatches = null;
        for (int k : NGRAM_SIZES) {
            for (String gram : getNGrams(query, k)) {
                Set<String> ids = ngramIndex.get(gram);
                if (ids != null && !ids.isEmpty()) {
                    ngramMatches = new HashSet<>(ids);
                    break;
                }
            }
            if (ngramMatches != null) break;
        }

        Set<String> tokenMatches = new HashSet<>();
        for (String token : tokenize(query)) {
            Set<String> ids = tokenIndex.get(token);
            if (ids != null) tokenMatches.addAll(ids);
        }

        Set<String> finalIds;
        if (tokenMatches.isEmpty()) {
            finalIds = (ngramMatches != null) ? ngramMatches : Set.of();
        } else if (ngramMatches == null) {
            finalIds = tokenMatches;
        } else {
            finalIds = ngramMatches.stream()
                    .filter(tokenMatches::contains)
                    .collect(Collectors.toSet());
        }

        return finalIds.stream()
                .map(contactMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public DeletedResponse delete(List<String> ids) {
        int deleted = 0;
        for (String id : ids) {
            ContactCard card = contactMap.remove(id);
            if (card != null) {
                unindexCard(card);
                deleted++;
            }
        }
        return DeletedResponse.builder().deleted(deleted).build();
    }

    @Override
    public List<ContactCard> update(List<ContactCard> requests) {
        return requests.stream().map(req -> {
            ContactCard old = contactMap.get(req.getId());
            if (old == null) return null;
            unindexCard(old);
            old.setName(req.getName());
            old.setEmail(req.getEmail());
            old.setPhone(req.getPhone());
            contactMap.put(old.getId(), old);
            indexCard(old);
            return old;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    // ---------- Indexing Logic ----------

    private void indexCard(ContactCard card) {
        indexNGrams(card.getId(), card.getName());
        indexNGrams(card.getId(), card.getEmail());
        indexNGrams(card.getId(), card.getPhone());

        indexTokens(card.getId(), card.getName());
        indexTokens(card.getId(), card.getEmail());
        indexTokens(card.getId(), card.getPhone());
    }

    private void unindexCard(ContactCard card) {
        unindexNGrams(card.getId(), card.getName());
        unindexNGrams(card.getId(), card.getEmail());
        unindexNGrams(card.getId(), card.getPhone());

        unindexTokens(card.getId(), card.getName());
        unindexTokens(card.getId(), card.getEmail());
        unindexTokens(card.getId(), card.getPhone());
    }

    private void indexNGrams(String id, String value) {
        if (value == null) return;
        for (int k : NGRAM_SIZES) {
            for (String gram : getNGrams(value, k)) {
                ngramIndex.computeIfAbsent(gram, key -> ConcurrentHashMap.newKeySet()).add(id);
            }
        }
    }

    private void unindexNGrams(String id, String value) {
        if (value == null) return;
        for (int k : NGRAM_SIZES) {
            for (String gram : getNGrams(value, k)) {
                Set<String> ids = ngramIndex.get(gram);
                if (ids != null) {
                    ids.remove(id);
                    if (ids.isEmpty()) ngramIndex.remove(gram);
                }
            }
        }
    }

    private void indexTokens(String id, String value) {
        if (value == null) return;
        for (String token : tokenize(value)) {
            tokenIndex.computeIfAbsent(token, key -> ConcurrentHashMap.newKeySet()).add(id);
        }
    }

    private void unindexTokens(String id, String value) {
        if (value == null) return;
        for (String token : tokenize(value)) {
            Set<String> ids = tokenIndex.get(token);
            if (ids != null) {
                ids.remove(id);
                if (ids.isEmpty()) tokenIndex.remove(token);
            }
        }
    }

    // ---------- Utility ----------

    private List<String> getNGrams(String input, int k) {
        if (input == null || input.length() < k) return List.of();
        List<String> ngrams = new ArrayList<>();
        for (int i = 0; i <= input.length() - k; i++) {
            ngrams.add(input.substring(i, i + k));
        }
        return ngrams;
    }

    private List<String> tokenize(String input) {
        if (input == null) return List.of();
        return Arrays.stream(input.split("[@._\\-\\s,]+"))
                .filter(s -> !s.isBlank())
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}