package exception;

public class MemberNotFoundException extends Exception {

    private static final long serialVersionUID = 8L;

    public MemberNotFoundException(String memberId) {
        super("No member found with ID: " + memberId);
    }
}
