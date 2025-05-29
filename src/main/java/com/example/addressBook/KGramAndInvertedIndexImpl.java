package com.example.addressBook;

import com.example.dto.AddContactRequest;
import com.example.dto.ContactCard;
import com.example.dto.DeletedResponse;
import com.example.dto.FetchRequest;
import com.example.utils.Utils;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class KGramAndInvertedIndexImpl implements AddressBook {
    /**

     >> KGramAndInvertedIndexImpl uses inverted indexing on substring of maximum length upto 10
        with reverse indexing.

        | Operation | Time Complexity                                          |
        | --------- | -------------------------------------------------------- |
        | Insert    | O(len(query)^2)     Acceptable for contact-sized strings |
        | Search    | O(1)                HashMap-backed                       |
        | Update    | O(len(query)^2)     Due to un-index + re-index           |

     **/

    private final Map<String, ContactCard> contactMap = new ConcurrentHashMap<>() {};
    private final Map<String, Set<ContactCard>> substringIndex = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> reverseIndex = new ConcurrentHashMap<>();
    private static final int MAX_SUBSTRING_LENGTH = 10;

    @Override
    public List<ContactCard> insert(List<AddContactRequest> requests) {
        List<ContactCard> inserted = new ArrayList<>();

        for (AddContactRequest req : requests) {
            String id = Utils.generateId();
            ContactCard card = ContactCard.builder()
                    .id(id)
                    .name(req.getName())
                    .email(req.getEmail())
                    .phone(req.getPhone())
                    .build();

            contactMap.put(id, card);
            Set<String> substrings = indexCard(card);
            reverseIndex.put(id, substrings);

            inserted.add(card);
        }

        return inserted;
    }

    @Override
    public List<ContactCard> find(FetchRequest request) {
        String query = request.getQuery();
        if (query == null || query.isEmpty()) return Collections.emptyList();

        return new ArrayList<>(substringIndex.getOrDefault(query.toLowerCase(), Collections.emptySet()));
    }

    @Override
    public DeletedResponse delete(List<String> ids) {
        int deleted = 0;
        for (String id : ids) {
            if(!contactMap.containsKey(id))
                continue;
            ContactCard card = contactMap.remove(id);
            if (card != null) {
                Set<String> substrings = reverseIndex.remove(id);
                if (substrings != null) {
                    for (String substr : substrings) {
                        Set<ContactCard> set = substringIndex.get(substr);
                        if (set != null) {
                            set.remove(card);
                            if (set.isEmpty()) substringIndex.remove(substr);
                        }
                    }
                }
                deleted++;
            }
        }
        return DeletedResponse.builder().deleted(deleted).build();
    }

    @Override
    public List<ContactCard> update(List<ContactCard> requests) {
        List<ContactCard> updated = new ArrayList<>();
        for (ContactCard newCard : requests) {
            if(!contactMap.containsKey(newCard.getId()))
                continue;
            ContactCard oldCard = contactMap.get(newCard.getId());
            Utils.updateContactDetails(oldCard, newCard);
            newCard = oldCard;
            if (oldCard == null) continue;
            // Un-index old
            Set<String> oldSubstrings = reverseIndex.remove(newCard.getId());
            if (oldSubstrings != null) {
                for (String substr : oldSubstrings) {
                    Set<ContactCard> set = substringIndex.get(substr);
                    if (set != null) {
                        set.remove(oldCard);
                        if (set.isEmpty()) substringIndex.remove(substr);
                    }
                }
            }
            // Index new
            contactMap.put(newCard.getId(), newCard);
            Set<String> newSubstrings = indexCard(newCard);
            reverseIndex.put(newCard.getId(), newSubstrings);
            updated.add(newCard);
        }

        return updated;
    }

    private Set<String> indexCard(ContactCard card) {
        Set<String> substrings = new HashSet<>();
        indexField(substrings, card.getName(), card);
        indexField(substrings, card.getEmail(), card);
        indexField(substrings, card.getPhone(), card);
        return substrings;
    }

    private void indexField(Set<String> substrings, String field, ContactCard card) {
        if (field == null) return;
        String value = field.toLowerCase();
        int len = value.length();

        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j <= Math.min(len, i + MAX_SUBSTRING_LENGTH); j++) {
                String substr = value.substring(i, j);
                substrings.add(substr);
                substringIndex.computeIfAbsent(substr, k -> new HashSet<>()).add(card);
            }
        }
    }
}
