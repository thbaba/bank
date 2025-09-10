package com.denizcanbank.accounts.mapper;

public interface DtoMapper<E, D> {
    D toDto(E entity);
}
