package com.example.service;

import com.example.addressBook.AddressBook;
import com.example.addressBook.AddressBookFactory;
import com.example.dto.AddContactRequest;
import com.example.dto.ContactCard;
import com.example.dto.DeletedResponse;
import com.example.dto.FetchRequest;
import com.example.enums.AddressBookImplType;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;


@ApplicationScoped
public class AddressBookAdapter {
    public AddressBook getAddressBook() {
        AddressBookImplType config = AddressBookImplType.FULL_TOKENIZATION;
        return AddressBookFactory.getAddressBookImpl(config);
    }

    public List<ContactCard> insert(List<AddContactRequest> requests) {
        return getAddressBook().insert(requests);
    }

    public List<ContactCard> find(FetchRequest fetchRequest) {
        return getAddressBook().find(fetchRequest);
    }

    public List<ContactCard> update(List<ContactCard> requests) {
        return getAddressBook().update(requests);
    }

    public DeletedResponse delete(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("List of IDs to delete cannot be null or empty");
        }
        return getAddressBook().delete(ids);
    }
}
