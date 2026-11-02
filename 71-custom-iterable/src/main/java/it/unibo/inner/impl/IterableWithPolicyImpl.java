package it.unibo.inner.impl;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import it.unibo.inner.api.IterableWithPolicy;
import it.unibo.inner.api.Predicate;

/**
 * Implementation of {@link IterableWithPolicy} whith {@link Predicate} policy support
 * @param <T> Target type
 */
public class IterableWithPolicyImpl<T> implements IterableWithPolicy<T> {
    private final List<T> elements;
    private Predicate<T> filter;

    /**
     * Initialize the Iterable class
     * @param array list of elements
     */
    public IterableWithPolicyImpl(final T[] array) {
        this(array, new Predicate<T>() {
            @Override
            public boolean test(final Object elem) {
                return true;
            }
        });
    }

    /**
     * Initialize the Iterable class
     * @param array list of elements 
     * @param predicate the filter to be applied
     */
    public IterableWithPolicyImpl(final T[] array, final Predicate<T> predicate) {
        elements = List.of(array);
        this.filter = predicate;
    }
    
    /**
     * Returns the array iterator
     * @return The iterator
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return new WithPolicyIterator();
    }

    /**
     * @param filter Set the iterator's filter
     * @see it.unibo.inner.api.IterableWithPolicy#setIterationPolicy(it.unibo.inner.api.Predicate)
     */
    @Override
    public void setIterationPolicy(final Predicate<T> filter) {
        this.filter = filter;
    }

    /**
     * Local implementation of the {@link Iterator}
     */
    private class WithPolicyIterator implements Iterator<T> {
        private int currentIndex = 0;
        private final int size;

        WithPolicyIterator() {
            size = elements.size();
        }

        /**
         * Check if the iterator have another element
         * @see java.util.Iterator#hasNext()
         */
        @Override
        public boolean hasNext() {
            while (currentIndex < size) {
                if (filter.test(elements.get(currentIndex))) {
                    return true;
                }
                currentIndex++;
            }
            return false;
        }

        /** 
         * @return Returns {@link T} - Item of rhe current index
         * @see java.util.Iterator#next()
         */
        @Override
        public T next() {
            if (this.hasNext()) {
                return elements.get(currentIndex++);
            }
            throw new NoSuchElementException("Size of array: " + size + ". Current index: " + currentIndex);
        }
    }

}
