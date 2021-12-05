package com.company.rest.device.validation;

import inet.ipaddr.MACAddressString;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MacAddressValidator implements ConstraintValidator<MacAddress, String> {

    @Override
    public void initialize(MacAddress contactNumber) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext cxt) {
        if (value == null) {
            return true;
        }
        try {
            new MACAddressString(value).toAddress();
            return true;
        } catch(Exception e) {
            return false;
        }
    }

}