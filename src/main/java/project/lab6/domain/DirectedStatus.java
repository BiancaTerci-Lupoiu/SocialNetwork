package project.lab6.domain;

public enum DirectedStatus {
    APPROVED,
    PENDING_SEND,
    REJECTED_SEND,
    PENDING_RECEIVED,
    REJECTED_RECEIVED;
    public Status toStatus()
    {
        return switch (this)
                {
                    case APPROVED -> Status.APPROVED;
                    case PENDING_RECEIVED, PENDING_SEND -> Status.PENDING;
                    case REJECTED_RECEIVED, REJECTED_SEND -> Status.REJECTED;
                };
    }
}
