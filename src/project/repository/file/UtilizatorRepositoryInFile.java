package project.repository.file;

import project.model.Utilizator;
import project.model.validare.Validator;

import java.util.Arrays;
import java.util.List;

public class UtilizatorRepositoryInFile extends RepositoryInFile<Integer, Utilizator> {
    public UtilizatorRepositoryInFile(Validator<Utilizator> validator, String fileName) {
        super(validator, fileName);
    }

    /**
     * @return Utilizatorul format de aceasta lista de atribute
     */
    @Override
    protected Utilizator extractEntity(List<String> attributes) {
        int id = Integer.parseInt(attributes.get(0));
        String name = attributes.get(1);
        return new Utilizator(id,name);
    }

    /**
     * @param entity Entitatea care va fi convertita
     * @return List de String
     */
    @Override
    protected List<String> convertEntityToStringList(Utilizator entity) {
        return Arrays.asList(entity.getId().toString(),entity.getName());
    }
}
