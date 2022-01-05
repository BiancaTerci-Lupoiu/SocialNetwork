package project.lab6.utils;

import java.util.function.Supplier;

/**
 * Provides support for lazy initialization.
 * @param <T> The type of object that is being lazily initialized.
 */
public class Lazy<T> {
    private final Supplier<T> valueFactory;
    private T value = null;
    private boolean initialize = false;

    /**
     * When lazy initialization occurs, the specified initialization function is used.
     * @param valueFactory
     *     The delegate that is invoked to produce the lazily initialized value when it
     *     is needed.
     */
    public Lazy(Supplier<T> valueFactory) {
        this.valueFactory = valueFactory;
    }

    /**
     * Initializes a new instance of the Lazy class that uses a preinitialized
     * specified value.
     * @param value The preinitialized value to be used.
     */
    public Lazy(T value)
    {
        valueFactory =null;
        initialize=true;
        this.value = value;
    }

    /**
     * @return The value
     */
    public T get() {
        if (!initialize) {
            initialize = true;
            value = valueFactory.get();
        }
        return value;
    }
}
