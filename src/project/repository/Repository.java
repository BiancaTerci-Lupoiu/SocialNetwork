package project.repository;

import project.model.Entity;
import project.model.validare.ValidationException;

import java.util.Optional;

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
    Iterable<E> findAll();

    /**
     * @param entity entity must be not null
     * @return True- if the given entity is saved
     * otherwise returns False
     * @throws ValidationException if the entity is not valid
     */
    boolean save(E entity);

    /**
     * Sterge un utilizator din repository
     *
     * @param id - the id of the entity to be removed
     * @return True if the entity has been removed and false otherwise
     */
    boolean delete(ID id);

    /**
     * Updateaza entitatea cu noile valori
     *
     * @param entity nu poate fi null
     * @return true daca s-a putut efectua acest update
     * si false in caz contrar
     */
    boolean update(E entity);
}
