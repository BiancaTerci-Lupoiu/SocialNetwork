package project.repository.memory;

import project.model.Entity;
import project.model.Utilizator;
import project.model.validare.ValidationException;
import project.model.validare.Validator;
import project.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class RepositoryInMemory<ID, E extends Entity<ID>> implements Repository<ID, E> {

    protected final Map<ID, E> entities;
    protected final Validator<E> validator;

    public RepositoryInMemory(Validator<E> validator) {
        entities = new HashMap<>();
        this.validator = validator;
    }

    /**
     *
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     */
    @Override
    public E findOne(ID id) {
        return entities.get(id);
    }

    /**
     * @return all entities
     */
    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     * @param entity entity must be not null
     * @return True- if the given entity is saved
     * otherwise returns False
     * @throws ValidationException if the entity is not valid
     */
    @Override
    public boolean save(E entity) {
        validator.valideaza(entity);
        var utilizator = entities.putIfAbsent(entity.getId(),entity);
        return utilizator==null;
    }

    /**
     * Sterge un utilizator din repository
     *
     * @param id - the id of the entity to be removed
     * @return True if the entity has been removed and false otherwise
     */
    @Override
    public boolean delete(ID id) {
        return entities.remove(id) != null;
    }

    /**
     * Updateaza entitatea cu noile valori
     *
     * @param entity nu poate fi null
     * @return true daca s-a putut efectua acest update
     * si false in caz contrar
     */
    @Override
    public boolean update(E entity) {
        var oldUtilizator = entities.get(entity.getId());
        if(oldUtilizator == null)
            return false;
        entities.put(entity.getId(),entity);
        return true;
    }
}
