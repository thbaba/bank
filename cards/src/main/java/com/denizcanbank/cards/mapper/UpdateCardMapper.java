package com.denizcanbank.cards.mapper;

import com.denizcanbank.cards.application.domain.entity.Card;
import com.denizcanbank.cards.application.domain.valueObject.ID;
import com.denizcanbank.cards.application.domain.valueObject.Money;
import com.denizcanbank.cards.dto.CardResponseDto;
import com.denizcanbank.cards.dto.UpdateCardRequestDto;
import org.springframework.stereotype.Component;

@Component
public class UpdateCardMapper extends CardResponseMapper implements FullMapper<Card, UpdateCardRequestDto, CardResponseDto> {
    @Override
    public Card toEntity(UpdateCardRequestDto dto) {
        return Card.builder()
                .cardID(ID.of(dto.cardID()))
                .totalLimit(Money.of(dto.limit()))
                .amountUsed(Money.of(dto.used()))
                .build();
    }
}
