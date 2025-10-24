package br.car.registration.domain.attributes;

public interface Attribute<T> {
    String getName();
    T getValue();
}
