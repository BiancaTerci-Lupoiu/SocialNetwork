package project.lab6.repository.paging;

import java.util.ArrayList;
import java.util.List;

public class PagedItemsImplementation<T> implements PagedItems<T> {
    //curentPage represents the start of the nextItems call
    Pageable curentPage;
    private final PageSupplier<T> supplier;

    public PagedItemsImplementation(PageSupplier<T> supplier, int size) {
        this.supplier = supplier;
        curentPage = new PageableImplementation(0, size);
    }

    private List<T> getItems(Pageable pageable) {
        var result = supplier.getPage(pageable);
        return result.getContent();
    }

    @Override
    public List<T> getNextItems() {
        var content = getItems(curentPage);
        int size = content.size();
        if (size != 0)
            curentPage = curentPage.nextPageable();
        return content;
    }

    @Override
    public List<T> getPreviousItems() {
        if (curentPage.getPageNumber() <= 1)
            return new ArrayList<>();
        curentPage = curentPage.previousPageable();
        return getItems(curentPage.previousPageable());
    }
}
