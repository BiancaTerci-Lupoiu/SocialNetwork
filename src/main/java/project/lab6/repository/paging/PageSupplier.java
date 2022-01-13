package project.lab6.repository.paging;

@FunctionalInterface
public interface PageSupplier<T> {
    Page<T> getPage(Pageable pageable);
}