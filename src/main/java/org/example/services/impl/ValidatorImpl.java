package org.example.services.impl;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.example.services.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ValidatorImpl implements Validator {

    @Override
    public void validateIp(String ip) {
        InetAddressValidator validator = InetAddressValidator.getInstance();
        if (!validator.isValidInet4Address(ip)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void validateCardNumber(String cardNumber) {
        if(!LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(cardNumber)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
