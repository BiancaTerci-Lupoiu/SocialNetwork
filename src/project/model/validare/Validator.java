package project.model.validare;

public interface Validator<E> {
    /**
     * Valideaza entitatea
     * @param entity Entitatea care vi validata
     * @throws ValidationException cand entity nu este valid si eroarea va contine un mesaj de eroare
     */
    void valideaza(E entity) throws ValidationException;
}
