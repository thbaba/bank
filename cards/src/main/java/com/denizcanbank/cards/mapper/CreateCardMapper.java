package com.denizcanbank.cards.mapper;

import com.denizcanbank.cards.application.domain.entity.Card;
import com.denizcanbank.cards.application.domain.valueObject.ID;
import com.denizcanbank.cards.application.domain.valueObject.Money;
import com.denizcanbank.cards.dto.CardResponseDto;
import com.denizcanbank.cards.dto.CreateCardRequestDto;
import org.springframework.stereotype.Component;

@Component
public class CreateCardMapper extends CardResponseMapper implements FullMapper<Card, CreateCardRequestDto, CardResponseDto>{
    @Override
    public Card toEntity(CreateCardRequestDto dto) {
        return Card.builder()
                .accountID(ID.of(dto.accountID()))
                .totalLimit(Money.of(dto.limit()))
                .amountUsed(Money.of(dto.used()))
                .build();
    }
}
