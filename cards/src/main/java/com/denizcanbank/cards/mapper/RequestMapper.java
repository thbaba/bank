package com.denizcanbank.cards.mapper;

public interface RequestMapper<E, D> {
    E toEntity(D dto);
}
