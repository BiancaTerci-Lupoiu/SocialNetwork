package project.lab6.repository.paging;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FilteredPagedItems<T> implements PagedItems<T> {
    private Pageable curentPageable;
    private final PageSupplier<T> supplier;
    private final Predicate<T> filter;
    private final int pageSize;

    public FilteredPagedItems(int size, PageSupplier<T> supplier, Predicate<T> filter) {
        this.supplier = supplier;
        this.filter = filter;
        this.pageSize = size;
        curentPageable = new PageableImplementation(0, size);
    }

    @Override
    public List<T> getNextItems() {
        List<T> elementsInResult = new ArrayList<>();
        while (elementsInResult.size() < pageSize) {
            var page = supplier.getPage(curentPageable);
            curentPageable = page.nextPageable();
            List<T> content = page.getContent();
            elementsInResult.addAll(content);
            if (content.size() == 0)
                break;
        }
        return elementsInResult;
    }
}
