package com.ordino.domain.suppliers.model.dto.add;

import com.ordino.core.validation.ValidEmail;
import com.ordino.core.validation.ValidPhoneNumber;
import com.ordino.core.validation.address.ValidAddress;
import com.ordino.domain.suppliers.validation.email.UniqueSupplierEmail;
import com.ordino.domain.suppliers.validation.name.UniqueSupplierName;
import com.ordino.domain.suppliers.validation.name.ValidSupplierName;
import com.ordino.domain.suppliers.validation.phone_number.UniqueSupplierPhoneNumber;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddSupplierRequestDTO {
    @ValidSupplierName
    @UniqueSupplierName
    private String name;

    @ValidAddress
    private String address;

    @ValidEmail
    @UniqueSupplierEmail
    private String email;

    @ValidPhoneNumber
    @UniqueSupplierPhoneNumber
    private String phoneNumber;
}
