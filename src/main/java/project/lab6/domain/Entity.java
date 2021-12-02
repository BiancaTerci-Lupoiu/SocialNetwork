package project.lab6.domain;

import java.io.Serializable;

// nu e abstract pt ca n am dc sa o fac (n am metode abstracte)

/**
 * Define an Entity with a generic id
 *
 * @param <ID> the id of the entity
 */
public class Entity<ID> implements Serializable {
    private static final long serialVersionUID = 7331115341259248461L;
    private ID id;

    /**
     * @return the id of the entity
     */
    public ID getId() {
        return id;
    }

    /**
     * @param id the id that must be set
     */
    public void setId(ID id) {
        this.id = id;
    }
}
