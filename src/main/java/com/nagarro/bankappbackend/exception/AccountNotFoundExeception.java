package com.nagarro.bankappbackend.exception;

public class AccountNotFoundExeception extends RuntimeException {

    public AccountNotFoundExeception() {
        super();
    }

    public  AccountNotFoundExeception (String message) {
        super(message);
    }
}
