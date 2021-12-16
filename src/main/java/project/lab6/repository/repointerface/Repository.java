package project.lab6.repository.repointerface;

import project.lab6.domain.Entity;
import project.lab6.domain.validators.ValidationException;

import java.util.List;

/**
 * CRUD operations repository interface
 *
 * @param <ID> -type E must have an attribute of type ID
 * @param <E>  - type of entities saved in repository
 */
public interface Repository<ID, E extends Entity<ID>> {
    /**
     * @param id -the id of the entity to be returned
     *           id must not be null
     * @return the entity with the specified id
     * or null - if there is no entity with the given id
     * @throws IllegalArgumentException if id is null.
     */
    E findOne(ID id);

    /**
     * @return all entities
     */
    List<E> findAll();

    /**
     * @param entity entity must be not null
     * @return the entity with the id completed or null if the operation failed
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    E save(E entity);

    /**
     * removes the entity with the specified id
     *
     * @param id id must be not null
     * @return true if the entity is deleted or false if there is no entity with the given id
     * @throws IllegalArgumentException if the given id is null.
     */
    boolean delete(ID id);

    /**
     * @param entity entity must not be null
     * @return true - if the entity is updated,
     * otherwise  returns false  - (e.g id does not exist).
     * @throws IllegalArgumentException if the given entity is null.
     * @throws ValidationException      if the entity is not valid.
     */
    boolean update(E entity);
}
