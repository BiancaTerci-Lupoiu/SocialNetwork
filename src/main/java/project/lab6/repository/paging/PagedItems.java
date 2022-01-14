package project.lab6.repository.paging;

import java.util.List;

public interface PagedItems<T> {
    /**
     * @return the next items in the list
     */
    List<T> getNextItems();

    /**
     * @return the previous items in the list
     */
    List<T> getPreviousItems();
}
