package project.lab6.repository.paging;

import java.util.List;

public class PagedItemsImplementation<T> implements PagedItems<T> {
    Pageable curentPage;
    private final PageSupplier<T> supplier;

    public PagedItemsImplementation(PageSupplier<T> supplier, int size) {
        this.supplier = supplier;
        curentPage = new PageableImplementation(0, size);
    }

    @Override
    public List<T> getNextItems() {
        var result = supplier.getPage(curentPage);
        curentPage = result.nextPageable();
        return result.getContent();
    }
}
