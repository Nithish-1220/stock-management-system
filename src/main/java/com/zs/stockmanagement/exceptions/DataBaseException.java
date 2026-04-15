package com.zs.stockmanagement.exceptions;

public class DataBaseException extends AppException {
    public DataBaseException(String message){
        super(message,500);
    }
}
