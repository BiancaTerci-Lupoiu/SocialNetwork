package project.lab6.repository.paging;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FilteredPagedItems<T> implements PagedItems<T> {
    private Pageable curentPageable;
    private final PageSupplier<T> supplier;
    private final Predicate<T> filter;
    private final int pageSize;

    private boolean isMoreToLoad = true;
    private T lastItem = null;

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
        int size = elementsInResult.size();
        if (size < pageSize)
            isMoreToLoad = false;
        if (size > 0)
            lastItem = elementsInResult.get(size - 1);
        return elementsInResult;
    }

    @Override
    public T getLastItemLoaded() {
        return lastItem;
    }

    @Override
    public boolean isMoreToLoad() {
        return isMoreToLoad;
    }
}
