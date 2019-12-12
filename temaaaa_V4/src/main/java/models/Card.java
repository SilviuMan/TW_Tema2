package models;

import Enums.Rank;
import Enums.Suit;

import java.io.Serializable;

public class Card implements Serializable {
    private static final long serialVersionUID = 1L;
    private Suit suit;
    private Rank rank;

    @Override
    public String toString() {
        return  suit +
                "," + rank
                ;
    }
    public Suit getSuit(String suit){
        switch (suit){
            case "Diamonds":{return Suit.Diamonds;
            }
            case "Black":{return Suit.Black;
            }
            case "Red":{return Suit.Red;
            }
            case "Clubs":{return Suit.Clubs;
            }
            case "Hearts":{return Suit.Hearts;
            }
            case "Spades":{return Suit.Spades;
            }

        }
        return null;
    }
    public Rank getRank(String rank){
        switch (rank){
            case "Joker":{return Rank.Joker;
            }
            case "King":{return Rank.King;}
            case "Queen":{return Rank.Queen;}
            case "Jack":{return Rank.Jack;}
            case "Ten":{return Rank.Ten;}
            case "Nine":{return Rank.Nine;}
            case "Eight":{return Rank.Eight;}
            case "Seven":{return Rank.Seven;}
            case "Six":{return Rank.Six;}
            case "Five":{return Rank.Five;}
            case "Four":{return Rank.Four;}
            case "Three":{return Rank.Three;}
            case "Two":{return Rank.Two;}
            case "Ace":{return Rank.Ace;}

        }
        return null;
    }
    public Card(String card){
        String[] arrSplit = card.split(",");
        this.suit=getSuit(arrSplit[0]);
        this.rank=getRank(arrSplit[1]);
    }
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }
}