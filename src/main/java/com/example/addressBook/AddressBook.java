package com.example.addressBook;

import com.example.dto.ContactCard;
import com.example.dto.AddContactRequest;
import com.example.dto.DeletedResponse;
import com.example.dto.FetchRequest;
import java.util.List;

public interface AddressBook {
    public List<ContactCard> insert(List<AddContactRequest> requests);
    public List<ContactCard> find(FetchRequest request);
    public DeletedResponse delete(List<String> requests);
    public List<ContactCard> update(List<ContactCard> requests);
}
