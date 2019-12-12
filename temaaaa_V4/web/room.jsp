<%@ page import="java.util.Queue" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="db.ConnectionDB" %>
<%@ page import="models.Card" %>
<%@ page import="Enums.Suit" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <META HTTP-EQUIV="Refresh" CONTENT="10">
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Room</title>


    </head>
<body>
<% String youuuu = (String) request.getSession().getAttribute("userName2"); %>
<center><p><b>You are  <% out.println(youuuu); %></b></p>
<form action="success" method="post">
    <input type="submit" name="backToMeniu" value="Back to Main Menu">
</form> </center>
<form action="enterInRoom" method="post">

    <% String autor = (String) request.getSession().getAttribute("autorCamera"+(String) request.getSession().getAttribute("nrcamera")); %>
    You are in room of  <% out.println(autor); %>
    <table id="players" border="1"  width="100%">
        <tr>
            <td><b> Nume Jucatori</b></td>
        </tr>

        <%Iterator itr;%>
        <%
            List data= (List)request.getSession().getAttribute("playersList");
            for (itr=data.iterator(); itr.hasNext(); )
            {
        %>
        <tr>
            <td width="119"  bgcolor="#<%=itr.next()%>"><%=itr.next()%></td>
        </tr>
        <%}%>
    </table>
    <% String turn = (String) request.getServletContext().getAttribute("tura"+request.getSession().getAttribute("nrcamera")); %>
    <p>It's <% out.println(turn); %> turn
    <table align="center">
        <tr>
            <td>Select a Card :</td>
            <td><select name="nrOfCard" style="width:180px">
                <%Iterator itr2;%>
                <% List<Card> data2= ( List<Card>)request.getSession().getAttribute("cardsInHeand");
                    for (itr2=data2.iterator(); itr2.hasNext(); )
                    {
                %>
                <%Card nr= (Card) itr2.next();%>
                <option value="<%=nr%>"> <%=nr.getRank()%> <%=nr.getSuit()%></option>
                <%}%>
            </select></td>
            <% if((boolean)request.getSession().getAttribute("isSeven")==true){ %>
                <td>Select a Suit :</td>
                <td><select name="nrOfSeven" style="width:180px">
                    <%Iterator itr3;%>
                    <% List<Suit> suits= ( List<Suit>)request.getServletContext().getAttribute("suitsSeven");
                        for (itr3=suits.iterator(); itr3.hasNext(); )
                        {
                    %>
                    <%Suit nr= (Suit) itr3.next();%>
                    <option value="<%=nr%>"> <%=nr%></option>
                    <%}%>
                </select></td>

            <%}%>
        </tr>
    </table>
    <% Card downCard = (Card) request.getServletContext().getAttribute("downCard"+request.getSession().getAttribute("nrcamera")); %>
    <p>Down Card is: <% out.println(downCard.getRank()); %>  <% out.println(downCard.getSuit()); %>
    <% String start = (String) request.getServletContext().getAttribute("start"+request.getSession().getAttribute("nrcamera")); %>
    <% if(turn.equals(youuuu) && start!=null)
    {%>
        <input type="submit" name="checkSelectedCard" value="Check">
        <input type="submit" name="dropCard" value="Drop Card">
        <input type="submit" name="nextTurn" value="Skip">
     <%}%>

    <% if(turn.equals(youuuu) && start==null && (int)request.getServletContext().getAttribute("nrOfPlayers"+ request.getSession().getAttribute("nrcamera"))>=2) {%>
        <input type="submit" name="Start" value="Start">  <%}%>
</form></p>
<% if (request.getSession().getAttribute("messages") != null) {%>
<%=request.getSession().getAttribute("messages")%>
<% }%>
</body>
</html>
