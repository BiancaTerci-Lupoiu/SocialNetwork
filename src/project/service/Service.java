package project.service;

import project.model.Comunitate;
import project.model.Prietenie;
import project.model.Tuple;
import project.model.Utilizator;
import project.model.validare.Validator;
import project.repository.Repository;

import java.util.*;

public class Service {
    private final Repository<Integer, Utilizator> repositoryUtilizatori;
    private final Repository<Tuple<Integer,Integer>, Prietenie> repositoryPrieteni;
    private final Validator<Utilizator> validatorUtilizatori;
    private final Validator<Prietenie> validatorPrieteni;

    private Integer maxId;

    public Service(Repository<Integer, Utilizator> repositoryUtilizatori, Repository<Tuple<Integer, Integer>, Prietenie> repositoryPrieteni,
                   Validator<Utilizator> validatorUtilizatori, Validator<Prietenie> validatorPrieteni) {
        this.repositoryUtilizatori = repositoryUtilizatori;
        this.repositoryPrieteni = repositoryPrieteni;
        this.validatorUtilizatori = validatorUtilizatori;
        this.validatorPrieteni = validatorPrieteni;

        maxId=0;
        repositoryUtilizatori.findAll().forEach(utilizator -> maxId= Math.max(maxId,utilizator.getId()));
    }

    /**
     * Adauga un utilizator
     * @param name Numele utilizatorului
     */
    public void addUtilizator(String name)
    {
        Utilizator utilizator = new Utilizator(maxId+1, name);
        validatorUtilizatori.valideaza(utilizator);
        if(!repositoryUtilizatori.save(utilizator))
            throw new ServiceException("Este deja un utilizator cu acest id adaugat!");
        maxId++;
    }

    /**
     * Sterge un utilizator
     * @param id - Integer id-ul utilizatorului
     */
    public void removeUtilizator(Integer id)
    {
        List<Prietenie> prietenii = new ArrayList<>();
        repositoryPrieteni.findAll().forEach(prietenii::add); //facem o copie pentru a functiona iterarea cand stergem o prietenie
        for(var prietenie: prietenii)
        {
            if(prietenie.getId().getLeft().equals(id) || prietenie.getId().getRight().equals(id))
                repositoryPrieteni.delete(prietenie.getId());
        }

        if(!repositoryUtilizatori.delete(id))
            throw new ServiceException("Utilizator inexistent!");
    }

    /**
     * Adauga o relatie de prietenie intre cei doi utilizatori cu aceste id-uri
     * @param idPrieten1 Integer
     * @param idPrieten2 Integer
     */
    public void addPrieten(Integer idPrieten1, Integer idPrieten2)
    {
        Prietenie prietenie = new Prietenie(idPrieten1,idPrieten2);
        validatorPrieteni.valideaza(prietenie);
        if(!repositoryPrieteni.save(prietenie))
            throw new ServiceException("Nu s-a putut salva prietenia!");
    }

    /**
     * Sterge relatia de prietenie dintre cei doi prieteni
     * @param idPrieten1 Integer
     * @param idPrieten2 Integer
     */
    public void removePrieten(Integer idPrieten1, Integer idPrieten2)
    {
        Tuple<Integer, Integer> id = new Tuple<>(Math.min(idPrieten1,idPrieten2),
                Math.max(idPrieten1,idPrieten2));
        if(!repositoryPrieteni.delete(id))
        {
            throw new ServiceException("Nu s-a putut sterge prietenia!");
        }
    }

    /**
     * @return Toti utilizatorii cu lista de prieteni completata
     */
    public Iterable<Utilizator> findAll()
    {
        Map<Integer, Utilizator> utilizatori = new HashMap<>();
        repositoryUtilizatori.findAll().forEach(utilizator ->
                utilizatori.put(utilizator.getId(),new Utilizator(utilizator.getId(),utilizator.getName())));

        repositoryPrieteni.findAll().forEach(prietenie ->
        {
            var firstUtilizator= utilizatori.get(prietenie.getId().getLeft());
            var secondUtilizator= utilizatori.get(prietenie.getId().getRight());
            firstUtilizator.addPrieten(secondUtilizator);
            secondUtilizator.addPrieten(firstUtilizator);
        });

        return utilizatori.values();
    }

    /**
     * Adauga in comunitate toti membri acesteia
     * @param comunitate Comunitatea in care se vor adauga membri
     * @param vizitati Set<Integer> -> Contine toate id-urile utilizatorilor care au fost deja acoperiti de aceasta functie
     * @param utilizator Utilizator -> utilizatorul pentru care ii se va descoperi comunitatea
     */
    private void DescoperaComunitate(Comunitate comunitate, Set<Integer> vizitati, Utilizator utilizator)
    {
        vizitati.add(utilizator.getId());
        comunitate.add(utilizator);
        for(var prieten: utilizator.getPrieteni())
            if(!vizitati.contains(prieten.getId()))
                DescoperaComunitate(comunitate,vizitati, prieten);
    }

    /**
     * @return List de Comunitate -> O lista cu toate comunitatile
     */
    public List<Comunitate> Comunitati()
    {
        Set<Integer> vizitat = new HashSet<>();
        var utilizatori = findAll();
        List<Comunitate> comunitati = new ArrayList<>();

        for(var utilizator: utilizatori)
        {
            if(!vizitat.contains(utilizator.getId()))
            {
                Comunitate comunitate = new Comunitate();
                DescoperaComunitate(comunitate, vizitat, utilizator);
                comunitati.add(comunitate);
            }
        }
        return comunitati;
    }

    /**
     * @return Numarul de comunitati
     */
    public int NumarComunitati()
    {
        return Comunitati().size();
    }

    /**
     * @return Cea mai sociabila comunitate
     */
    public Comunitate CeaMaiSociabila()
    {
        var comunitate= Comunitati().stream().max(Comparator.comparingInt(Comunitate::celMaiLungDrum));
        return comunitate.orElse(null);
    }

    public void updateUtilizator(Integer id, String nume) {
        Utilizator utilizator = new Utilizator(id, nume);
        validatorUtilizatori.valideaza(utilizator);
        repositoryUtilizatori.update(utilizator);
    }
}
