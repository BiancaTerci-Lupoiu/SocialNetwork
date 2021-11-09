package project.repository.file;

import project.model.Prietenie;
import project.model.Tuple;
import project.model.validare.Validator;

import java.util.Arrays;
import java.util.List;

public class PrietenieRepositoryInFile extends RepositoryInFile<Tuple<Integer,Integer>, Prietenie> {

    public PrietenieRepositoryInFile(Validator<Prietenie> validator, String fileName) {
        super(validator, fileName);
    }

    @Override
    protected Prietenie extractEntity(List<String> attributes) {
        Integer first = Integer.parseInt(attributes.get(0));
        Integer second = Integer.parseInt(attributes.get(1));
        return new Prietenie(first,second);
    }

    @Override
    protected List<String> convertEntityToStringList(Prietenie entity) {
        return Arrays.asList(entity.getId().getLeft().toString(),entity.getId().getRight().toString());
    }
}
