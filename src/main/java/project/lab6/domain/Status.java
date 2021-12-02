package project.lab6.domain;

public enum Status {
    APPROVED,
    PENDING,
    REJECTED;

    /**
     * converts this Status into a code (int) 0-APPROVED
     *                                       1-PENDING
     *                                       2-REJECTED
     * @return the corespondent code for the status
     */
    public int toInt()
    {
        return switch (this)
                {
                    case APPROVED -> 0;
                    case PENDING -> 1;
                    case REJECTED -> 2;
                };
    }

    /**
     * converts a code(int) into a Status
     * @param statusCode the code to be converted
     * @return the Status corespondent to the statusCode
     */
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

    /**
     * converts this Status to a DirectedStatus based on the boolean send
     * @param send -if it is true, the DirectedStatus will contain SEND in the name
     *             else will contain RECEIVED
     * @return the corespondent DirectedStatus
     */
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
