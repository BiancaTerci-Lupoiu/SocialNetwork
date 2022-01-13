package project.lab6.repository.paging;

import java.util.List;

public interface Page<E> {
    Pageable getPageable();

    Pageable nextPageable();

    List<E> getContent();
}
