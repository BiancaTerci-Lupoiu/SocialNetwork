package project.model.validare;

import project.model.Utilizator;

/**
 * Clasa care va valida un utilizator
 */
public class ValidatorUtilizator implements Validator<Utilizator> {
    /**
     * Valideaza un utilizator
     * @param entity Utilizatorul care vi validata
     * @throws ValidationException cand utilizatorul nu este valid: id-ul nu este nenegativ sau numele este gol
     */
    @Override
    public void valideaza(Utilizator entity) throws ValidationException {
        String erori = "";

        if (entity.getName() == null || entity.getName().trim().equals(""))
            erori += "Nume invalid!\n";

        if (!erori.equals(""))
            throw new ValidationException(erori);
    }
}
