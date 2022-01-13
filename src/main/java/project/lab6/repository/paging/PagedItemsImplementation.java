package project.lab6.repository.paging;

import java.util.List;

public class PagedItemsImplementation<T> implements PagedItems<T> {
    Pageable curentPage;
    private final PageSupplier<T> supplier;
    private final int pageSize;

    private boolean isMoreToLoad = true;
    private T lastItem = null;

    public PagedItemsImplementation(PageSupplier<T> supplier, int size) {
        this.supplier = supplier;
        this.pageSize = size;
        curentPage = new PageableImplementation(0, size);
    }

    @Override
    public List<T> getNextItems() {
        var result = supplier.getPage(curentPage);
        curentPage = result.nextPageable();
        var content = result.getContent();
        int size = content.size();
        if (size == 0)
            isMoreToLoad = false;
        if (size > 0)
            lastItem = content.get(size - 1);
        return content;
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
