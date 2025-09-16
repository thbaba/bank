package com.denizcanbank.accounts.mapper;

import com.denizcanbank.accounts.application.domain.entity.Card;
import com.denizcanbank.accounts.application.domain.valueObject.ID;
import com.denizcanbank.accounts.dto.CardClientDto;
import org.springframework.stereotype.Component;

@Component
public class CardClientMapper implements EntityMapper<Card, CardClientDto>, DtoMapper<Card, CardClientDto> {
    @Override
    public Card toEntity(CardClientDto dto) {
        return Card.builder()
                .cardID(ID.of(dto.cardID()))
                .accountID(ID.of(dto.accountID()))
                .totalLimit(dto.limit())
                .amountUsed(dto.used())
                .availableAmount(dto.available())
                .build();
    }

    @Override
    public CardClientDto toDto(Card entity) {
        return new CardClientDto(
                entity.getCardID().toString(),
                entity.getAccountID().toString(),
                entity.getTotalLimit(),
                entity.getAmountUsed(),
                entity.getAvailableAmount()
        );
    }
}
