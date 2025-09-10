package com.denizcanbank.accounts.application.domain.valueObject;

import com.denizcanbank.accounts.application.domain.exception.InvalidIDException;

import java.util.Objects;
import java.util.UUID;

public class ID {

    private final UUID id;

    private ID(UUID id) {
        this.id = id;
    }

    public static ID of(UUID id) throws InvalidIDException {
        if(Objects.isNull(id))
            throw new InvalidIDException("Invalid ID: ID can not be null");
        return new ID(id);
    }

    public static ID  of(String id) throws InvalidIDException {
        try {
            UUID uuid = UUID.fromString(id);
            return new ID(uuid);
        } catch (IllegalArgumentException e) {
            StringBuilder messageBuilder = new StringBuilder();
            String message = messageBuilder.append(e.getMessage())
                    .append(System.lineSeparator())
                    .append("Invalid ID: ")
                    .append(id)
                    .toString();
            throw new InvalidIDException(message);
        }
    }

    public ID generate() {
        return new ID(UUID.randomUUID());
    }

    public UUID asUUID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return id.equals(((ID) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    @Override
    public String toString() {
        return this.id.toString();
    }

}
