package co.com.management.model.exception;

public class BusinessException extends  RuntimeException{

    public BusinessException(String message) {
        super(message);
    }
}
