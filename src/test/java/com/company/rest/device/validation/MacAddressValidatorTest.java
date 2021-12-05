package com.company.rest.device.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MacAddressValidatorTest {
    private final MacAddressValidator validator = new MacAddressValidator();

    @Test
    void invalid() throws Exception {
        assertFalse(validator.isValid("123", null));
        assertFalse(validator.isValid("10:10:10:10:10", null));
        assertFalse(validator.isValid("x1‑xx‑xx‑xx‑xx‑xx", null));
        assertFalse(validator.isValid("-A-AA-BB-AA-10-12", null));
    }

    @Test
    void valid() throws Exception {
        assertTrue(validator.isValid("AA:AA:BB:AA:10:12", null));
        assertTrue(validator.isValid("AA-AA-BB-AA-10-12", null));
        assertTrue(validator.isValid("0-1-BB-AA-10-13", null));
        assertTrue(validator.isValid("1:2:3:4:5:6", null));
    }

}
