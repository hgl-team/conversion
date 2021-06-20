package org.hglteam.conversion;

public class Generic<T extends Number> {
    public final T number;

    public Generic(T number) {
        this.number = number;
    }
}
