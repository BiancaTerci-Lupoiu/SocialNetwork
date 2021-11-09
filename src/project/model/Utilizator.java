package project.model;

import java.util.ArrayList;
import java.util.List;

public class Utilizator extends Entity<Integer> {
    private String name;
    private final List<Utilizator> prieteni;

    public Utilizator(Integer id, String name) {
        this.name = name;
        setId(id);
        prieteni = new ArrayList<>();
    }

    /**
     * @return Numele utilizatorului
     */
    public String getName() {
        return name;
    }

    /**
     * Seteaza numele utilizatorului
     * @param name Noua valoarea a numelui utilizatorului
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adauga un prieten acestui utilizator
     * @param prieten Utilizator -> prietenul care va fi adaugat
     */

    public void addPrieten(Utilizator prieten) {
        prieteni.add(prieten);
    }

    /**
     * Sterge un prieten acestui utilizator
     * @param id Integer -> Id-ul prietenului care va fi sters
     * @return true daca a fost gasit si sters prietenul si fals in caz contrar
     */
    public boolean removePrieten(Integer id) {
        return prieteni.removeIf(utilizator -> utilizator.getId().equals(id));
    }

    /**
     * @return Lista de prieteni
     */
    public Iterable<Utilizator> getPrieteni() {
        return prieteni;
    }

    /**
     * Converteste la string acest utilizator si afiseaza si lista lui de prieteni
     * @return String
     */
    public String toStringWithFriends() {
        return String.format("(%s) %s\n\tFriends=%s",getId(),getName(),getPrieteni());
    }

    @Override
    public String toString() {
        return String.format("(%s) %s",getId(),getName());
    }
}
