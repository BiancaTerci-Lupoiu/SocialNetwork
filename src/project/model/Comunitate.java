package project.model;

import java.util.*;

/**
 * Pastreaza informatii despre o comunitatea de utilizatori
 */
public class Comunitate implements Iterable<Utilizator>{
    private final Map<Integer, Utilizator> comunitate;

    public Comunitate() {
        comunitate = new HashMap<>();
    }

    /**
     * Adauga un utilizator in comunitate
     * @param utilizator Utilizatorul care va fi adaugat
     */
    public void add(Utilizator utilizator) {
        comunitate.put(utilizator.getId(),utilizator);
    }

    /**
     * @return int -> numarul de membrii din comunitate
     */
    public int count() {
        return comunitate.size();
    }

    @Override
    public Iterator<Utilizator> iterator() {
        return comunitate.values().iterator();
    }

    @Override
    public String toString() {
        List<String> utilizatori = new ArrayList<>();
        for(var utilizator: comunitate.values())
            utilizatori.add(utilizator.toString());
        return String.join(",",utilizatori);
    }

    /**
     * Calculeaza cel mai lung drum de prietenii care incepe de la utilizatorul dat
     * @param vizitat Un hashSet care sa contina utilizatorii care au fost deja vizitati. Initial trebuie sa fie gol
     *                si la final va ramane gol
     * @param utilizator Utilizatorul de care se va incepe calcularea drumului
     * @return lungimea acestui drum
     */
    private int celMaiLungDrum(HashSet<Integer> vizitat,Utilizator utilizator)
    {
        int best=0;
        vizitat.add(utilizator.getId());
        for(var prieten: utilizator.getPrieteni())
        {
            if(!vizitat.contains(prieten.getId()))
                best=Math.max(best,1+celMaiLungDrum(vizitat,prieten));
        }
        vizitat.remove(utilizator.getId());
        return best;
    }

    /**
     * Calculeaza cel mai lung drum de prietenii din aceasta comunitate
     * @return int -> lungimea acestui drum
     */
    public int celMaiLungDrum()
    {
        int best=0;
        HashSet<Integer> vizitat = new HashSet<>();
        for(var utilizator: comunitate.values())
            best=Math.max(best,celMaiLungDrum(vizitat,utilizator));
        return best;
    }

}
