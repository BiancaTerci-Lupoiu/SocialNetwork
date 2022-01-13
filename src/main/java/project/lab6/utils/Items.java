package project.lab6.utils;

import project.lab6.repository.paging.PagedItems;

import java.util.ArrayList;
import java.util.List;

public class Items {
    private Items() {
    }

    public static <T> List<T> getAllItems(PagedItems<T> pagedItems) {
        List<T> result = new ArrayList<>();
        List<T> page = new ArrayList<>();
        do {
            page = pagedItems.getNextItems();
            result.addAll(page);
        }while (page.size()>0);
        return result;
    }
}
