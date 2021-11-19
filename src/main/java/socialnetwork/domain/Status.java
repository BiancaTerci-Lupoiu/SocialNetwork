package socialnetwork.domain;

public enum Status {
    APPROVED,
    PENDING,
    REJECTED;

    public int toInt()
    {
        return switch (this)
                {
                    case APPROVED -> 0;
                    case PENDING -> 1;
                    case REJECTED -> 2;
                };
    }

    public static Status getStatus(int statusCode)
    {
        return switch (statusCode)
                {
                    case 0 -> APPROVED;
                    case 1 -> PENDING;
                    case 2 -> REJECTED;
                    default -> throw new IllegalStateException("Unexpected value: " + statusCode);
                };
    }
}
