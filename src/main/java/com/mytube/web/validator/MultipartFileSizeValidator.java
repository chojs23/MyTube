package com.mytube.web.validator;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Component
public class MultipartFileSizeValidator implements ConstraintValidator<MultipartFileSizeValid, MultipartFile> {

    private static final String ERROR_MESSAGE = "File too Large. 30MB Limit";

    private static final long FILE_SIZE = 50000000L;

    @Override
    public boolean isValid(final MultipartFile file, final ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate(ERROR_MESSAGE).addConstraintViolation();
        return file.getSize() < FILE_SIZE;
    }
}