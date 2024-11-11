package io.effi.rpc.common.extension;

/**
 * Holder that wraps a value of type {@link T}.
 * This class is used to encapsulate a value and modify it, offering basic getter and setter functionality.
 *
 * @param <T> the type of the value being held
 */
@SuppressWarnings("unchecked")
public class Holder<T> {

    private Object value;

    public Holder() {

    }

    public Holder(T value) {
        this.value = value;
    }

    /**
     * Sets a new value of type {@link NEW} for this holder and returns the updated holder.
     * This method allows changing the type of the held value.
     *
     * @param value the new value to be set
     * @param <NEW> the type of the new value
     * @return the updated {@link Holder} containing the new value
     */
    public <NEW> Holder<NEW> set(NEW value) {
        this.value = value;
        return (Holder<NEW>) this;
    }

    /**
     * Returns the value held by this {@link Holder}.
     *
     * @return the value of type {@link T} held by this {@link Holder}
     */
    public T get() {
        return (T) value;
    }
}

