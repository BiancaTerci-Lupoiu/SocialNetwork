package project.factory;

import project.model.Prietenie;
import project.model.Tuple;
import project.model.Utilizator;
import project.model.validare.Validator;
import project.model.validare.ValidatorPrieteni;
import project.model.validare.ValidatorUtilizator;
import project.repository.Repository;
import project.repository.db.PrietenieDbRepository;
import project.repository.db.UtilizatorDbRepository;
import project.repository.file.PrietenieRepositoryInFile;
import project.repository.memory.RepositoryInMemory;
import project.repository.RepositoryTypes;
import project.repository.file.UtilizatorRepositoryInFile;
import project.service.Service;
import project.ui.UI;

/**
 * Factory care va fi responsabil cu instantiarea corecta a claselor
 */
public class Factory {
    private Validator<Utilizator> validatorUtilizatori = null;
    private Validator<Prietenie> validatorPrieteni = null;
    private Repository<Integer, Utilizator> repositoryUtilizator = null;
    private Repository<Tuple<Integer,Integer>, Prietenie> repositoryPrieteni = null;
    private Service service = null;
    private UI ui = null;

    private final RepositoryTypes repositoryType;

    /**
     * Constructor
     * @param repositoryType Tipul de repository care va fi folosit pentru aplicatie
     */
    public Factory(RepositoryTypes repositoryType) {
        this.repositoryType=repositoryType;
    }

    /**
     * Validatorul va fi unic pe obiect Factory
     * @return Un validator de utilizator
     */
    public Validator<Utilizator> getValidatorUtilizatori() {
        if (validatorUtilizatori == null)
            validatorUtilizatori = new ValidatorUtilizator();
        return validatorUtilizatori;
    }

    /**
     * Validatorul va fi unic pe obiect Factory
     * @return Un validator de relatii prieten
     */
    public Validator<Prietenie> getValidatorPrieteni() {
        if (validatorPrieteni == null)
            validatorPrieteni = new ValidatorPrieteni(getRepositoryUtilizator());
        return validatorPrieteni;
    }

    /**
     * Repository-ul va fi unic pe obiect Factory
     * @return Un repository de utilizatori
     */
    public Repository<Integer, Utilizator> getRepositoryUtilizator() {
        if (repositoryUtilizator == null)
            repositoryUtilizator = switch (repositoryType)
                    {
                        case InMemory ->  new RepositoryInMemory<>(getValidatorUtilizatori());
                        case File -> new UtilizatorRepositoryInFile(getValidatorUtilizatori(), "data/utilizatori.txt");
                        case Database -> new UtilizatorDbRepository("jdbc:postgresql://localhost:1234/network",
                                "postgres",
                                "cartoon");
                    };
        return repositoryUtilizator;
    }

    /**
     * Repository-ul va fi unic pe obiect Factory
     * @return Un repository de prietenii
     */
    public Repository<Tuple<Integer,Integer>, Prietenie> getRepositoryPrieteni() {
        if (repositoryPrieteni == null)
            repositoryPrieteni = switch (repositoryType)
                    {
                        case InMemory ->  new RepositoryInMemory<>(getValidatorPrieteni());
                        case File -> new PrietenieRepositoryInFile(getValidatorPrieteni(), "data/prieteni.txt");
                        case Database -> new PrietenieDbRepository("jdbc:postgresql://localhost:1234/network",
                                "postgres",
                                "cartoon", getValidatorPrieteni());
                    };
        return repositoryPrieteni;
    }

    /**
     * Service-ul va fi unic pe obiect Factory
     * @return Service-ul de utilizatori si prietenii
     */
    public Service getService() {
        if (service == null)
            service = new Service(getRepositoryUtilizator(), getRepositoryPrieteni(),getValidatorUtilizatori(), getValidatorPrieteni());
        return service;
    }

    /**
     * UI va fi unic pe obiect Factory
     * @return UI-ul acestei aplicatii
     */
    public UI getUI() {
        if (ui == null)
            ui = new UI(getService(), System.in, System.out);
        return ui;
    }
}
