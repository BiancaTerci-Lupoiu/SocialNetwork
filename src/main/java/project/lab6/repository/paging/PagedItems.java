package project.lab6.repository.paging;

import java.util.List;

public interface PagedItems<T> {
    /**
     * @return the next items
     */
    List<T> getNextItems();
    T getLastItemLoaded();
    boolean isMoreToLoad();
}
