package com.iablonski.mynetwork.validation;

import com.iablonski.mynetwork.payload.request.SignUpRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchingPasswordsValidator implements ConstraintValidator<MatchingPasswords, SignUpRequest> {


    @Override
    public boolean isValid(SignUpRequest signUpRequest, ConstraintValidatorContext constraintValidatorContext) {
        return signUpRequest.getPassword().equals(signUpRequest.getPasswordConfirmation());
    }
}