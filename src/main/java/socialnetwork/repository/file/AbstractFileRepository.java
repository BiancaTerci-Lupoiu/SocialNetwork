package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository;

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
     * @return
     */
    @Override
    public E save(E entity) {
        E result = super.save(entity);
        if (result == null)
            writeToFile(entity);
        return result;

    }

    /**
     * @param id id must be not null
     * @return
     */
    @Override
    public E delete(ID id) {
        E result = super.delete(id);
        if (result != null)
            storeData();
        return result;
    }

    /**
     * @param entity entity must not be null
     * @return
     */
    @Override
    public E update(E entity) {
        E result = super.update(entity);
        if (result == null)
            storeData();
        return result;

    }
}
