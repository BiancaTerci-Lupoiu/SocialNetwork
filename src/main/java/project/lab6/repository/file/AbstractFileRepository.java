package project.lab6.repository.file;

import project.lab6.domain.Entity;
import project.lab6.domain.validators.ValidationException;
import project.lab6.domain.validators.Validator;
import project.lab6.repository.memory.InMemoryRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class to store E entities to file
 *
 * @param <ID> -type E must have an attribute of type ID
 * @param <E>  - type of entities saved in repository
 */
public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    String fileName;
    /**
     * @param fileName  the name of the file
     * @param validator to validate the entities
     */
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName = fileName;
        loadData();

    }

    /**
     * loads data from file
     */
    private void loadData() {
        Path path = Paths.get(fileName);
        try {
            List<String> allData = Files.readAllLines(path);
            allData.forEach((line) -> {
                String[] args = line.split(";");
                E entity = extractEntity(Arrays.asList(args));
                super.save(entity);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * appends entity to file
     *
     * @param entity the entity to be stored
     */
    protected void writeToFile(E entity) {
        String entityAsString = createEntityAsString(entity);
        // se inchide bufferedWriter singur asa
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true))) {
            // daca fac asa trebuie sa inchid eu bufferedreaderul
            //BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(fileName,true));
            bufferedWriter.write(entityAsString);
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * rewrites the file with the current data
     */
    protected void storeData() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, false))) {
            // daca fac asa trebuie sa inchid eu bufferedreaderul
            //BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(fileName,true));
            for (E entity : super.findAll()) {
                bufferedWriter.write(createEntityAsString(entity));
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @param attributes list with entity attributes
     * @return an entity of type E
     */
    protected abstract E extractEntity(List<String> attributes);

    /**
     * creates a string from entity
     *
     * @param entity
     * @return entity as string
     */
    protected abstract String createEntityAsString(E entity);

    /**
     * @param entity entity must be not null
     * @return true- if the given entity is saved
     * otherwise returns false (id already exists)
     * @throws ValidationException      if the entity is not valid
     * @throws IllegalArgumentException if the given entity is null.     *
     */
    @Override
    public E save(E entity) {
        E result = super.save(entity);
        if (result != null)
            writeToFile(result);
        return result;
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
        boolean result = super.delete(id);
        if (result)
            storeData();
        return result;
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
        boolean result = super.update(entity);
        if (result)
            storeData();
        return result;

    }
}
