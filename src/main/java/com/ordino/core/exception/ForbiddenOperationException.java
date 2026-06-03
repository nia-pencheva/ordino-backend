package com.ordino.core.exception;

import java.util.List;

import lombok.Getter;

@Getter
public class ForbiddenOperationException extends RuntimeException {
    private List<String> reasons;

    public ForbiddenOperationException(List<String> reasons) {
        this.reasons = reasons;
    }
}
