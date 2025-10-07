package com.bank.accounts.registeraccount;

public record AccountPayload(
        String securityNumber,
        Integer id
) {

    public String toJson(IJsonSerializer<AccountPayload> serializer) throws JsonSerializerException {
        return serializer.serialize(this);
    }

}
