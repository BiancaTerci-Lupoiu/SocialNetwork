package project.lab6.repository.paging;

import java.util.List;

public class PageImplementation<T> implements Page<T> {
    private Pageable pageable;
    private List<T> content;

    public PageImplementation(Pageable pageable, List<T> content) {
        this.pageable = pageable;
        this.content = content;
    }

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

    @Override
    public List<T> getContent() {
        return this.content;
    }
}
