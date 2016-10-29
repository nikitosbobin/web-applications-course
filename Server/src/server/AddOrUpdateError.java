package server;

public class AddOrUpdateError extends Exception {
    private String invalidInputTag;
    public AddOrUpdateError(String invalidInputTag) {
        this.invalidInputTag = invalidInputTag;
    }

    @Override
    public String getMessage() {
        return "invalid " + invalidInputTag;
    }
}
