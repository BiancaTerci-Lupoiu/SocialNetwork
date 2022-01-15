package project.lab6.repository.paging;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilteredPagedItems<T> implements PagedItems<T> {
    private final PageSupplier<T> supplier;
    private final Predicate<T> filter;
    private final int pageSize;
    //Saves all the previous location seen for the getPreviousItems functionality
    private final Stack<Location> locations = new Stack<>();
    private Location startLocation;
    private Location endLocation;
    public FilteredPagedItems(int size, PageSupplier<T> supplier, Predicate<T> filter) {
        this.supplier = supplier;
        this.filter = filter;
        this.pageSize = size;
        var currentPageable = new PageableImplementation(0, size);
        endLocation = new Location(currentPageable, 0);
        startLocation = null;
    }

    private ItemsGet<T> getItems(Location location) {
        Pageable pageable = location.pageable();
        int leftOver = location.elementsNeeded();
        List<T> elementsInResult =
                supplier.getPage(pageable).getContent().stream()
                        .filter(filter)
                        .skip(leftOver)
                        .collect(Collectors.toList());
        pageable = pageable.nextPageable();
        while (elementsInResult.size() < pageSize) {
            var page = supplier.getPage(pageable);
            pageable = pageable.nextPageable();
            List<T> content = page.getContent();
            elementsInResult.addAll(content.stream().filter(filter).toList());
            if (content.size() == 0)
                break;
        }
        int size = elementsInResult.size();
        if (size > pageSize)
            leftOver = size - pageSize;
        else
            leftOver = 0;
        if (size > pageSize) {
            pageable = pageable.previousPageable();
            elementsInResult = elementsInResult.subList(0, pageSize);
        }
        return new ItemsGet<>(elementsInResult, new Location(pageable, leftOver));
    }

    @Override
    public List<T> getNextItems() {
        locations.push(startLocation);
        startLocation = endLocation;
        var items = getItems(startLocation);

        if (items.items().size() == 0) {
            startLocation = locations.pop();
        } else {
            startLocation = endLocation;
            endLocation = items.endLocation();
        }
        return items.items();
    }

    @Override
    public List<T> getPreviousItems() {
        if (locations.size() < 2)
            return new ArrayList<>();
        startLocation = locations.pop();
        var items = getItems(startLocation);
        endLocation = items.endLocation();
        return items.items();
    }

    private record Location(Pageable pageable, int elementsNeeded) {
    }

    private record ItemsGet<T>(List<T> items, Location endLocation) {
    }
}
