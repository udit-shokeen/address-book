package com.example.addressBook;

import com.example.dto.AddContactRequest;
import com.example.dto.ContactCard;
import com.example.dto.DeletedResponse;
import com.example.dto.FetchRequest;

import java.util.List;

public class KGramTokenizationImpl implements AddressBook{
    @Override
    public List<ContactCard> insert(List<AddContactRequest> requests) {
        return null;
    }

    @Override
    public List<ContactCard> find(FetchRequest request) {
        return null;
    }

    @Override
    public DeletedResponse delete(List<String> requests) {
        return null;
    }

    @Override
    public List<ContactCard> update(List<ContactCard> requests) {
        return null;
    }
}
