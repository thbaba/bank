package com.denizcanbank.accounts.mapper;

public interface EntityMapper<E, D> {
    E toEntity(D dto);
}
