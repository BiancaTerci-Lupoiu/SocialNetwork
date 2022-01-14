package project.lab6.repository.paging;

import java.util.List;

public interface Page<E> {
    Pageable getPageable();

    List<E> getContent();
}
