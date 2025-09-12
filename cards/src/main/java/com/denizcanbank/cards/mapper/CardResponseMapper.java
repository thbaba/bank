package com.denizcanbank.cards.mapper;

import com.denizcanbank.cards.application.domain.entity.Card;
import com.denizcanbank.cards.dto.CardResponseDto;

public class CardResponseMapper implements ResponseMapper<Card, CardResponseDto> {
    @Override
    public CardResponseDto toDto(Card entity) {
        return new CardResponseDto(
                entity.cardID().toString(),
                entity.accountID().toString(),
                entity.totalLimit().amount(),
                entity.amountUsed().amount(),
                entity.availableAmount().amount()
        );
    }
}
