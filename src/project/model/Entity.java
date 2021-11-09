package project.model;

/**
 * Reprezinta o entitate
 * @param <ID>
 */
public class Entity<ID> {
    private ID id;

    /**
     * @return ID-ul entitatii
     */
    public ID getId() {
        return id;
    }

    /**
     * Seteaza id-ul entitatii
     * @param id ID -> noua valoarea la care va fi setat
     */
    public void setId(ID id) {
        this.id = id;
    }
}
