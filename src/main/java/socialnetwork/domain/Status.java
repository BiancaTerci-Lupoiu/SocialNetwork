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

    public DirectedStatus toDirectedStatus(boolean send)
    {
        if(send)
            return switch (this)
                    {
                        case PENDING -> DirectedStatus.PENDING_SEND;
                        case REJECTED -> DirectedStatus.REJECTED_SEND;
                        case APPROVED -> DirectedStatus.APPROVED;
                    };
        else
            return switch (this)
                    {
                        case PENDING -> DirectedStatus.PENDING_RECEIVED;
                        case REJECTED -> DirectedStatus.REJECTED_RECEIVED;
                        case APPROVED -> DirectedStatus.APPROVED;
                    };
    }
}
