package com.denizcanbank.cards.mapper;

public interface ResponseMapper<E, D> {
    D toDto(E entity);
}
