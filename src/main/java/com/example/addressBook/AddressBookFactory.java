package com.example.addressBook;

import com.example.enums.AddressBookImplType;
import java.util.Map;


public class AddressBookFactory {
    private static final Map<AddressBookImplType, AddressBook> addressBookMap = Map.of(
            AddressBookImplType.FULL_TOKENIZATION, new FullTokenizationImpl(),
            AddressBookImplType.K_GRAM_PLUS_INVERTED_INDEX, new KGramAndInvertedIndexImpl(),
            AddressBookImplType.K_GRAM_PLUS_PARTIAL_TOKENIZATION, new KGramTokenizationImpl()
    );

    public static AddressBook getAddressBookImpl(AddressBookImplType config) {
        return addressBookMap.get(config);
    }
}
