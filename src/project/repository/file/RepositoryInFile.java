package project.repository.file;

import project.model.Entity;
import project.model.validare.ValidationException;
import project.model.validare.Validator;
import project.repository.memory.RepositoryInMemory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public abstract class RepositoryInFile<ID, E extends Entity<ID>> extends RepositoryInMemory<ID,E> {

    protected final String fileName;

    public RepositoryInFile(Validator<E> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    /**
     * Aduce in memorie entitatile din fisier
     */
    private void loadData()
    {
        try {
            Files.readAllLines(Paths.get(fileName)).forEach(line ->
            {
                String[] args=line.split(";");
                E entity=extractEntity(Arrays.asList(args));
                super.save(entity);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param entity entity must be not null
     * @return True- if the given entity is saved
     * otherwise returns False
     * @throws ValidationException if the entity is not valid
     */
    @Override
    public boolean save(E entity) {
        var result = super.save(entity);
        if(result)
        {
            writeToFile(entity);
        }
        return result;
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
        boolean result = super.update(entity);
        if(result)
            writeAll();
        return result;
    }

    /**
     * Sterge un utilizator din repository
     *
     * @param id - the id of the entity to be removed
     * @return True if the entity has been removed and false otherwise
     */
    @Override
    public boolean delete(ID id) {
        boolean result = super.delete(id);
        if(result)
            writeAll();
        return result;
    }

    /**
     * Scrie in fisier entitatea data
     * @param entity Entitatea care va fi adaugata in fisier
     */
    protected void writeToFile(E entity){
        List<String> entityToStringList = convertEntityToStringList(entity);
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true)))
        {
            writer.write(String.join(";",entityToStringList));
            writer.newLine();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     * Scrie toate entitatile in fisier
     */
    protected void writeAll()
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)))
        {
            for(var entity : super.findAll())
            {
                var attributesAsString=convertEntityToStringList(entity);
                writer.write(String.join(";",attributesAsString));
                writer.newLine();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes lista de atribute a entitatii
     * @return an entity of type E
     */
    protected abstract E extractEntity(List<String> attributes);

    /**
     * Converteste atributele unei entitati intr-o lista de stringuri
     * Aceasta metoda trebuie sa permita construirea entitatii originale din aceasta lista de stringuri
     * @param entity Entitatea
     * @return List de String
     */
    protected abstract List<String> convertEntityToStringList(E entity);

}
