package project.lab6.domain.validators;

/**
 * Validator interface
 *
 * @param <T> T - the entity to be validated
 */
public interface Validator<T> {
    /**
     * @param entity - the entity to be validated
     * @throws ValidationException
     */
    void validate(T entity) throws ValidationException;
}
