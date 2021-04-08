package be.vdab.luigi2.exceptions;

public class KoersClientException extends RuntimeException{
    public KoersClientException(String message) {
        super(message);
    }
    public KoersClientException(String message, Exception oorspronkelijkeFout) {
        super(message, oorspronkelijkeFout);
    }
}
