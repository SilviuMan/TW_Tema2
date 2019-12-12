package services;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Enums.Rank;
import Enums.Suit;
import models.Card;


public class Deck implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Card> cards = new ArrayList<>();

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public Card dealCard() {
        if (cards.isEmpty()) {
            //throw new EmptyDeckException("There are no cards to deal");
        }
        //get the last element from cards
        Card topCard = cards.get(cards.size() - 1);
        cards.remove(cards.size() - 1);
        return topCard;
    }
    public void addCard(Card card)
    {
        cards.add(card);
        this.shuffle();
    }
    public List<Card> dealCards(int numberOfCards) {
        if (cards.size() < numberOfCards) {
            //throw new NotEnoughCardsException("There are not enough cards to deal" + numberOfCards);
        }
        // get the last numberOfCards elements from list
        List<Card> requestedCards = new ArrayList<>();
        //requestedCards = cards.subList(Math.max(cards.size() - numberOfCards, 0), cards.size());
        for (int i=0;i<numberOfCards;i++){
            Card topCard =dealCard();
            requestedCards.add(topCard);
        }
       // cards.remove(cards.size() - numberOfCards);
        return requestedCards;
    }

    public void fill() {
        int numberOfSuitsWithoutJokerSpecificOnes = 4;
        int numberOfRanksWithoutJokers = 13;
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                if(suit!=Suit.Black && suit!=Suit.Red)
                    if(rank!=Rank.Joker) {
                        Card card = new Card(suit, rank);
                        cards.add(card);
                    }
                else if(rank==Rank.Joker && (suit==Suit.Black || suit==Suit.Red)) {
                    Card card = new Card(suit, rank);
                    cards.add(card);
                }
            }
        }
        Card card = new Card(Suit.Black, Rank.Joker);
        cards.add(card);
        card=new Card(Suit.Red, Rank.Joker);
        cards.add(card);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

}
