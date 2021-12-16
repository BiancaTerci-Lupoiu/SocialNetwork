package project.lab6.repository.memory;

import project.lab6.domain.Entity;
import project.lab6.domain.validators.Validator;
import project.lab6.domain.validators.ValidationException;
import project.lab6.repository.repointerface.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CRUD operations repository in memory
 *
 * @param <ID> -type E must have an attribute of type ID
 * @param <E>  - type of entities saved in repository
 */
public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {

    private final Validator<E> validator;
    Map<ID, E> entities;

    /**
     * constructor
     *
     * @param validator to validate the entity E
     */
    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID, E>();
    }

    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    @Override
    public E findOne(ID id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null!");
        return entities.get(id);
    }

    /**
     * @return all entities
     */
    @Override
    public List<E> findAll() {
        return entities.values().stream().toList();
    }

    /**
     * @param entity entity must be not null
     * @return true- if the given entity is saved
     * otherwise returns false (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    @Override
    public E save(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);
        if (entities.get(entity.getId()) != null)
            return null;
        else
            entities.put(entity.getId(), entity);
        return entity;
    }

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return true if the entity is deleted or false if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    @Override
    public boolean delete(ID id) {
        if (id == null)
            throw new IllegalArgumentException("id must be not null!");
        E entityToDelete = entities.get(id);
        if (entityToDelete != null) {
            entities.remove(id);
            return true;
        }
        return false;
    }

    /**
     * @param entity entity must not be null
     * @return true - if the entity is updated,
     * otherwise  returns false  - (e.g id does not exist).
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    @Override
    public boolean update(E entity) {
        if (entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);
        if (entities.get(entity.getId()) == null)
            return false;
        entities.put(entity.getId(), entity);
        return true;
    }
}
