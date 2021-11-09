package project.model.validare;

import project.model.Prietenie;
import project.model.Utilizator;
import project.repository.Repository;

/**
 * Clasa care va valida o relatie de prietenie
 */
public class ValidatorPrieteni implements Validator<Prietenie> {
    private final Repository<Integer, Utilizator> repositoryUtilizatori;

    public ValidatorPrieteni(Repository<Integer, Utilizator> repositoryUtilizatori) {
        this.repositoryUtilizatori = repositoryUtilizatori;
    }

    /**
     * @param entity Entitatea care vi validata
     * @throws ValidationException cand obiectul nu este valid: id-urile utilizatorilor sunt identice
     *      sau nu exista utilizatori cu aceste id-uri
     */
    @Override
    public void valideaza(Prietenie entity) throws ValidationException {
        Integer first = entity.getId().getLeft();
        Integer second = entity.getId().getRight();
        if(first.equals(second))
            throw new ValidationException("Nu poti fi prieten cu tine insuti!");
        if(repositoryUtilizatori.findOne(first) == null || repositoryUtilizatori.findOne(second) == null)
            throw new ValidationException("Utilizatori inexistenti!");
    }
}
