import Enums.Rank;
import Enums.Suit;
import db.ConnectionDB;
import models.Card;
import models.Room;
import models.User;
import services.Deck;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;

@WebServlet(name = "enterInRoom",
        urlPatterns = {"/enterInRoom"},
        asyncSupported=true)
public class EnterInRoom  extends HttpServlet {
    private List<AsyncContext> contexts = new LinkedList<>();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.setIntHeader("Refresh", 3);

        ConnectionDB connectionDB = new ConnectionDB();
        if( request.getSession().getAttribute("nrcamera")!=null) {
            ArrayList<String> users;
            List players = new ArrayList();
            //ConnectionDB connectionDB =new ConnectionDB();
            try {
                int nrOfPlayers = connectionDB.getRoomNrOfPlayers(Integer.parseInt((String) request.getSession().getAttribute("nrcamera")));
                request.getServletContext().setAttribute("nrOfPlayers"+ request.getSession().getAttribute("nrcamera"),nrOfPlayers);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            users = connectionDB.getPlyaersFromRoom(Integer.parseInt((String) request.getSession().getAttribute("nrcamera")));
            if (!users.isEmpty()) {
                for (String s : users
                ) {
                    if (!s.equals((String) request.getSession().getAttribute("userName2"))) {
                        request.getSession().setAttribute("playersList", null);
                        if (s.equals(request.getServletContext().getAttribute("tura" + request.getSession().getAttribute("nrcamera")))) {
                            players.add("66ff99");
                            players.add(s);
                        } else {
                            players.add("ffffff");
                            players.add(s);
                        }
                    }
                }
                request.getSession().setAttribute("playersList", players);

            }
        }
//        ArrayList<String> users;
//        List players=new ArrayList();
//        if(request.getSession().getAttribute("nrcamera")!=null) {
//            users = connectionDB.getPlyaersFromRoom(Integer.parseInt((String) request.getSession().getAttribute("nrcamera")));
//            if(!users.isEmpty())
//            {
//                for (String s:users
//                     ) {
//                    if(!s.equals((String)request.getSession().getAttribute("userName2")))
//                    {
//                        if(s.equals(request.getServletContext().getAttribute("tura"+ request.getSession().getAttribute("nrcamera")))) {
//                            players.add("66ff99");
//                            players.add(s);
//                        }
//                        else{
//                            players.add("ffffff");
//                            players.add(s);
//                        }
//                    }
//                }
//                request.getSession().setAttribute("playersList",players);
//            }
//        }


        if (request.getParameter("enter") != null && request.getParameter("nrOfRoom")!=null) {
            String nr=request.getParameter("nrOfRoom");
            // System.out.println(request.getParameter("nrOfRoom"));
            request.getSession().setAttribute("nrcamera",nr);
            Queue queue = (Queue) request.getServletContext().getAttribute("coadaTura"+ request.getSession().getAttribute("nrcamera"));

            try {
                request.getSession().setAttribute("autorCamera"+(String) request.getSession().getAttribute("nrcamera"),connectionDB.getAutorRoom(Integer.parseInt((String) request.getSession().getAttribute("nrcamera"))));
                //System.out.println(connectionDB.getAutorRoom(Integer.parseInt(nr)));
                //System.out.println(request.getSession().getAttribute("autorCamera"+(String) request.getSession().getAttribute("nrcamera")));
                connectionDB.enterRoom((String)request.getSession().getAttribute("userName2"),Integer.parseInt(nr));
                int nrOfPlayers = connectionDB.getRoomNrOfPlayers(Integer.parseInt((String) request.getSession().getAttribute("nrcamera")));
                request.getServletContext().setAttribute("nrOfPlayers"+ request.getSession().getAttribute("nrcamera"),nrOfPlayers);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ArrayList<String> users;
            List players=new ArrayList();
            users = connectionDB.getPlyaersFromRoom(Integer.parseInt((String) request.getSession().getAttribute("nrcamera")));
            if(!users.isEmpty())
            {
                for (String s:users
                ) {
                    if(!s.equals((String)request.getSession().getAttribute("userName2")))
                    {
                        if(s.equals(request.getServletContext().getAttribute("tura"+ request.getSession().getAttribute("nrcamera")))) {
                            players.add("66ff99");
                            players.add(s);
                        }
                        else{
                            players.add("ffffff");
                            players.add(s);
                        }
                    }
                }
                request.getSession().setAttribute("playersList",players);
            }
            Deck deck=new Deck();
            List<Card> cards= (List<Card>) request.getServletContext().getAttribute("packOfCards"+request.getSession().getAttribute("nrcamera"));
            deck.setCards(cards);
            List<Card> myCard=deck.dealCards(5);
            request.getSession().setAttribute("cardsInHeand",myCard);
            request.getSession().setAttribute("isSeven",false);
            request.getServletContext().setAttribute("packOfCards"+request.getSession().getAttribute("nrcamera"),deck.getCards());

            queue.add(request.getSession().getAttribute("userName2"));
            request.getServletContext().setAttribute("coadaTura"+ request.getSession().getAttribute("nrcamera"),queue);
            request.getRequestDispatcher("room.jsp").forward(request, response);


            // System.out.println(request.getSession().getAttribute("autorCamera"+(String) request.getSession().getAttribute("nrcamera")));
        }
        if (request.getParameter("createRoom") != null ) {
            Queue queue =new LinkedList<>();
            Deck cards=new Deck();
            cards.fill();
            cards.shuffle();
            List<Card> myCard=cards.dealCards(5);
            List<Suit> suitSeven=new ArrayList<>();
            for (Suit suit : Suit.values())
            {
                if(suit!=Suit.Red && suit!=Suit.Black)
                    suitSeven.add(suit);
            }
            Card downCard=cards.dealCard();

            request.getServletContext().setAttribute("suitsSeven",suitSeven);
            request.getSession().setAttribute("cardsInHeand",myCard);
            request.getSession().setAttribute("isSeven",false);

            //ServletContext sc = request.getServletContext();
            // List<AsyncContext> asyncContexts = new ArrayList<>(this.contexts);
            // this.contexts.clear();
            String nr="";
            String username=(String)request.getSession().getAttribute("userName2");
            // System.out.println(username);
            try {

                connectionDB.createRoom(username);
                request.getSession().setAttribute("nrcamera",Integer.toString(connectionDB.getIdRoom(username)));
                nr=(String) request.getSession().getAttribute("nrcamera");
                request.getServletContext().setAttribute("start"+request.getSession().getAttribute("nrcamera"),null);
                request.getSession().setAttribute("autorCamera"+(String) request.getSession().getAttribute("nrcamera"),connectionDB.getAutorRoom(Integer.parseInt((String) request.getSession().getAttribute("nrcamera"))));
                int nrOfPlayers = connectionDB.getRoomNrOfPlayers(Integer.parseInt((String) request.getSession().getAttribute("nrcamera")));
                request.getServletContext().setAttribute("nrOfPlayers"+ request.getSession().getAttribute("nrcamera"),nrOfPlayers);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            request.getServletContext().setAttribute("packOfCards"+request.getSession().getAttribute("nrcamera"),cards.getCards());
            request.getServletContext().setAttribute("downCard"+request.getSession().getAttribute("nrcamera"),downCard);
            String htmlMessage = "<p><b>" + username + "</b>" + " turn" + "</p>";
            //  sc.setAttribute("messages"+request.getSession().getAttribute("nrcamera"), htmlMessage);
            //System.out.println(sc.getAttribute("messages"+request.getSession().getAttribute("nrcamera")));
            /*for (AsyncContext asyncContext : asyncContexts) {
                try (PrintWriter writer = asyncContext.getResponse().getWriter()) {
                    writer.println(htmlMessage);
                    writer.flush();
                    asyncContext.complete();
                } catch (Exception ex) {
                }
            }*/
            queue.add(username);
            request.getServletContext().setAttribute("coadaTura"+ request.getSession().getAttribute("nrcamera"),queue);
            request.getServletContext().setAttribute("tura"+ request.getSession().getAttribute("nrcamera"),username);
            ArrayList<String> users;
            List players=new ArrayList();
            users = connectionDB.getPlyaersFromRoom(Integer.parseInt((String) request.getSession().getAttribute("nrcamera")));
            if(!users.isEmpty())
            {
                for (String s:users
                ) {
                    if(!s.equals((String)request.getSession().getAttribute("userName2")))
                    {
                        if(s.equals(request.getServletContext().getAttribute("tura"+ request.getSession().getAttribute("nrcamera")))) {
                            players.add("66ff99");
                            players.add(s);
                        }
                        else{
                            players.add("ffffff");
                            players.add(s);
                        }
                    }
                }
                request.getSession().setAttribute("playersList",players);
            }
            request.getSession().setAttribute("autorCamera",username);
            int nrOfPlayers = 0;
            try {
                nrOfPlayers = connectionDB.getRoomNrOfPlayers(Integer.parseInt((String) request.getSession().getAttribute("nrcamera")));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            request.getServletContext().setAttribute("nrOfPlayers"+ request.getSession().getAttribute("nrcamera"),nrOfPlayers);
            request.getRequestDispatcher("room.jsp").forward(request, response);
        }
        if (request.getParameter("nextTurn") != null ) {
            if(checkInHeandsCards(request)==false) {
                tackeCard(request);
                nextTurn(request);
            }
            else
                request.getSession().setAttribute("messages", "Ai carti in mana care pot fi date jos");
            request.getRequestDispatcher("room.jsp").forward(request, response);

        }
        if (request.getParameter("RefreshRooms") != null ){
            List<Room> rooms = connectionDB.getRooms();
            List roos = new ArrayList<>();
            List idRooms = new ArrayList<>();
            for (Room room : rooms
            ) {
                idRooms.add(room.getId());
                roos.add(room.getId());
                roos.add(room.getNrJucatori());
                roos.add(room.getNumeCreator());
            }

            request.getServletContext().setAttribute("slist", idRooms);
            request.getServletContext().setAttribute("roomslist", roos);
            request.getRequestDispatcher("success.jsp").forward(request, response);
        }
        if (request.getParameter("Start") != null ){
            // List<AsyncContext> asyncContexts = new ArrayList<>(this.contexts);
            //this.contexts.clear();
            // ServletContext sc = request.getServletContext();
            //System.out.println(request.getParameter("tura" +request.getSession().getAttribute("nrcamera")));
            request.getServletContext().setAttribute("start"+request.getSession().getAttribute("nrcamera"),"asss");
            Queue queue = (Queue) request.getServletContext().getAttribute("coadaTura"+ request.getSession().getAttribute("nrcamera"));

            String username=(String)request.getSession().getAttribute("userName2");
            String turn= (String) queue.poll();
            queue.add(turn);
            request.getServletContext().setAttribute("coadaTura"+ request.getSession().getAttribute("nrcamera"),queue);
            request.getServletContext().setAttribute("tura" +request.getSession().getAttribute("nrcamera"),turn);
            ArrayList<String> users;
            List players=new ArrayList();
            users = connectionDB.getPlyaersFromRoom(Integer.parseInt((String) request.getSession().getAttribute("nrcamera")));
            if(!users.isEmpty())
            {
                for (String s:users
                ) {
                    if(!s.equals((String)request.getSession().getAttribute("userName2")))
                    {
                        if(s.equals(request.getServletContext().getAttribute("tura"+ request.getSession().getAttribute("nrcamera")))) {
                            players.add("66ff99");
                            players.add(s);
                        }
                        else{
                            players.add("ffffff");
                            players.add(s);
                        }
                    }
                }
                request.getSession().setAttribute("playersList",players);
            }
            String htmlMessage = "<p><b>" + username + "</b>" + " turn" + "</p>";
            //sc.setAttribute("messages"+request.getSession().getAttribute("nrcamera"), htmlMessage);
            //sc.setAttribute("tura", username);
            //  System.out.println(request.getServletContext().getAttribute("tura" +request.getSession().getAttribute("nrcamera")));

//            for (AsyncContext asyncContext : asyncContexts) {
//                try (PrintWriter writer = asyncContext.getResponse().getWriter()) {
//                    writer.println(htmlMessage);
//                    writer.flush();
//                    asyncContext.complete();
//                } catch (Exception ex) {
//                }
//            }
            request.getRequestDispatcher("room.jsp").forward(request, response);
        }
        if (request.getParameter("checkSelectedCard") != null ){
            String card=request.getParameter("nrOfCard");
            Card selectedCard=new Card(card);
            String mesaj=checkCard(selectedCard,request);
            request.getSession().setAttribute("messages",mesaj);

            request.getRequestDispatcher("room.jsp").forward(request, response);
        }
        if (request.getParameter("dropCard") != null ){
            String card=request.getParameter("nrOfCard");
            Card selectedCard=new Card(card);
            int check=checkDropDown(selectedCard,request);
            if(check==1)
            {
                dropCard(selectedCard,request);
                nextTurn(request);
            }
            else if(check==0) {
                request.getSession().setAttribute("messages", "Cartea " + card + " NU este buna");
            }
            else
                if(check==7)
                {
                    if((boolean)request.getSession().getAttribute("isSeven")==true) {
                        selectedCard.setSuit(selectedCard.getSuit(request.getParameter("nrOfSeven")));
                        dropCard(selectedCard, request);
                        request.getSession().setAttribute("isSeven", false);
                        nextTurn(request);
                    }
                    else {
                        request.getSession().setAttribute("messages", "Alege ce tip de carte 7 vrei sa dai");
                        request.getSession().setAttribute("isSeven", true);
                    }
                }
            request.getRequestDispatcher("room.jsp").forward(request, response);
        }
        //response.sendRedirect("room.jsp");
        //request.getRequestDispatcher("room.jsp").include(request, response);
       // doPost(request,response);
        response.sendRedirect("room.jsp");
    }
    private String checkCard(Card card,HttpServletRequest request)
    {

        request.getSession().setAttribute("isSeven",false);
        String mesaj="";
        Card downCard= (Card) request.getServletContext().getAttribute("downCard"+request.getSession().getAttribute("nrcamera"));
        if(card.getRank()!= Rank.Seven) {
            if (downCard.getSuit() == card.getSuit() || downCard.getRank() == card.getRank() ||
                    card.getRank()==Rank.Two ||
                    card.getRank()==Rank.Three ||card.getRank()==Rank.Four ||
                    (card.getSuit()==Suit.Black && downCard.getSuit()==Suit.Spades) ||
                    (card.getSuit()==Suit.Black && downCard.getSuit()==Suit.Clubs) ||
                    (card.getSuit()==Suit.Red && downCard.getSuit()==Suit.Hearts) ||
                    (card.getSuit()==Suit.Red && downCard.getSuit()==Suit.Diamonds) ||
                    (card.getSuit()==Suit.Spades && downCard.getSuit()==Suit.Black) ||
                    (card.getSuit()==Suit.Clubs && downCard.getSuit()==Suit.Black) ||
                    (card.getSuit()==Suit.Hearts && downCard.getSuit()==Suit.Red) ||
                    (card.getSuit()==Suit.Diamonds && downCard.getSuit()==Suit.Red))
                mesaj = "Cartea "+ card +" este buna";
            else
                mesaj = "Cartea "+ card +" NU este buna";
        }
        else {
            request.getSession().setAttribute("isSeven",true);
            mesaj = "Alege ce tip de carte 7 vrei sa dai";
        }

        return mesaj;
    }
    private void nextTurn(HttpServletRequest request)
    {
        ConnectionDB connectionDB = new ConnectionDB();
        Queue queue = (Queue) request.getServletContext().getAttribute("coadaTura"+ request.getSession().getAttribute("nrcamera"));

        String username=(String)request.getSession().getAttribute("userName2");

        String turn= (String) queue.poll();
        queue.add(turn);

        request.getServletContext().setAttribute("coadaTura"+ request.getSession().getAttribute("nrcamera"),queue);
        request.getServletContext().setAttribute("tura" +request.getSession().getAttribute("nrcamera"),turn);
        ArrayList<String> users;
        List players=new ArrayList();
        users = connectionDB.getPlyaersFromRoom(Integer.parseInt((String) request.getSession().getAttribute("nrcamera")));
        if(!users.isEmpty())
        {
            for (String s:users
            ) {
                if(!s.equals((String)request.getSession().getAttribute("userName2")))
                {
                    if(s.equals(request.getServletContext().getAttribute("tura"+ request.getSession().getAttribute("nrcamera")))) {
                        players.add("66ff99");
                        players.add(s);
                    }
                    else{
                        players.add("ffffff");
                        players.add(s);
                    }
                }
            }
            int nrOfPlayers = 0;
            try {
                nrOfPlayers = connectionDB.getRoomNrOfPlayers(Integer.parseInt((String) request.getSession().getAttribute("nrcamera")));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            request.getServletContext().setAttribute("nrOfPlayers"+ request.getSession().getAttribute("nrcamera"),nrOfPlayers);
            request.getSession().setAttribute("playersList",players);
        }
    }
    private boolean checkInHeandsCards(HttpServletRequest request)
    {
        // request.getSession().setAttribute("isSeven",false);
        boolean mesaj=false;
        List<Card> inHeand= (List<Card>) request.getSession().getAttribute("cardsInHeand");
        Card downCard= (Card) request.getServletContext().getAttribute("downCard"+request.getSession().getAttribute("nrcamera"));
        for (Card cardHeand:inHeand
        ) {
            if(cardHeand.getRank()==downCard.getRank() || cardHeand.getSuit()==downCard.getSuit() ||
                    cardHeand.getRank()==Rank.Seven || cardHeand.getRank()==Rank.Two ||
                    cardHeand.getRank()==Rank.Three ||cardHeand.getRank()==Rank.Four ||
                    (cardHeand.getSuit()==Suit.Black && downCard.getSuit()==Suit.Spades) ||
                    (cardHeand.getSuit()==Suit.Black && downCard.getSuit()==Suit.Clubs) ||
                    (cardHeand.getSuit()==Suit.Red && downCard.getSuit()==Suit.Hearts) ||
                    (cardHeand.getSuit()==Suit.Red && downCard.getSuit()==Suit.Diamonds) ||
                    (cardHeand.getSuit()==Suit.Spades && downCard.getSuit()==Suit.Black) ||
                    (cardHeand.getSuit()==Suit.Clubs && downCard.getSuit()==Suit.Black) ||
                    (cardHeand.getSuit()==Suit.Hearts && downCard.getSuit()==Suit.Red) ||
                    (cardHeand.getSuit()==Suit.Diamonds && downCard.getSuit()==Suit.Red))
               mesaj=true;
        }

        return mesaj;
    }
    private void tackeCard(HttpServletRequest request)
    {
        Deck deck=new Deck();
        List<Card> cards= (List<Card>) request.getServletContext().getAttribute("packOfCards"+request.getSession().getAttribute("nrcamera"));
        deck.setCards(cards);
        Card myCard=deck.dealCard();
        List<Card> inHeand= (List<Card>) request.getSession().getAttribute("cardsInHeand");
        inHeand.add(myCard);
        request.getSession().setAttribute("cardsInHeand",inHeand);
        request.getServletContext().setAttribute("packOfCards"+request.getSession().getAttribute("nrcamera"),deck.getCards());
    }
    private int checkDropDown(Card card,HttpServletRequest request)
    {
       // request.getSession().setAttribute("isSeven",false);
        int mesaj=0;
        Card downCard= (Card) request.getServletContext().getAttribute("downCard"+request.getSession().getAttribute("nrcamera"));
        if(card.getRank()!= Rank.Seven) {
            if (downCard.getSuit() == card.getSuit() || downCard.getRank() == card.getRank() ||
                     card.getRank()==Rank.Two ||
                    card.getRank()==Rank.Three ||card.getRank()==Rank.Four ||
                    (card.getSuit()==Suit.Black && downCard.getSuit()==Suit.Spades) ||
                    (card.getSuit()==Suit.Black && downCard.getSuit()==Suit.Clubs) ||
                    (card.getSuit()==Suit.Red && downCard.getSuit()==Suit.Hearts) ||
                    (card.getSuit()==Suit.Red && downCard.getSuit()==Suit.Diamonds) ||
                    (card.getSuit()==Suit.Spades && downCard.getSuit()==Suit.Black) ||
                    (card.getSuit()==Suit.Clubs && downCard.getSuit()==Suit.Black) ||
                    (card.getSuit()==Suit.Hearts && downCard.getSuit()==Suit.Red) ||
                    (card.getSuit()==Suit.Diamonds && downCard.getSuit()==Suit.Red))
                mesaj=1;
            else
                mesaj=0;
        }
        else {
            //request.getSession().setAttribute("isSeven",true);
            mesaj = 7;
        }

        return mesaj;
    }
    private void dropCard(Card card,HttpServletRequest request)
    {
        List<Card> inHeand= (List<Card>) request.getSession().getAttribute("cardsInHeand");
        List<Card> newHeand=new ArrayList<>();
        for (Card cardHeand:inHeand
             ) {
            if(cardHeand.getRank()!=card.getRank() || cardHeand.getSuit()!=card.getSuit())
                newHeand.add(cardHeand);
        }
        request.getSession().setAttribute("cardsInHeand",newHeand);
        if((boolean)request.getSession().getAttribute("isSeven")==true)
                 card.setSuit(card.getSuit(request.getParameter("nrOfSeven")));
        request.getSession().setAttribute("isSeven",false);
        Card downCard= (Card) request.getServletContext().getAttribute("downCard"+request.getSession().getAttribute("nrcamera"));
        Deck deck=new Deck();
        deck.setCards((List<Card>) request.getServletContext().getAttribute("packOfCards"+request.getSession().getAttribute("nrcamera")));
        deck.addCard(downCard);
        request.getServletContext().setAttribute("downCard"+request.getSession().getAttribute("nrcamera"),card);
        request.getServletContext().setAttribute("packOfCards"+request.getSession().getAttribute("nrcamera"),deck.getCards());
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            doGet(request,response);

    }
}
