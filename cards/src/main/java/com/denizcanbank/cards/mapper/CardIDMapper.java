package com.denizcanbank.cards.mapper;

import com.denizcanbank.cards.application.domain.entity.Card;
import com.denizcanbank.cards.application.domain.valueObject.ID;
import com.denizcanbank.cards.dto.CardIDRequestDto;
import com.denizcanbank.cards.dto.CardResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CardIDMapper extends CardResponseMapper implements FullMapper<Card, CardIDRequestDto, CardResponseDto>{
    @Override
    public Card toEntity(CardIDRequestDto dto) {
        return Card.builder()
                .cardID(ID.of(dto.id()))
                .build();
    }
}
