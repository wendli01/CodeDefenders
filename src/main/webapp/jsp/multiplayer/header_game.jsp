<% pageTitle = "Game " + mg.getId();
%>
<%@ include file="/jsp/header.jsp" %>
</div></div></div></div></div>
<%@ page import="java.util.*" %>
<%@ page import="org.codedefenders.Test" %>
<%@ page import="org.codedefenders.Mutant" %>
<%@ page import="org.codedefenders.duel.DuelGame" %>
<%@ page import="org.codedefenders.Constants" %>
<%@ page import="org.codedefenders.util.DatabaseAccess" %>
<%@ page import="org.codedefenders.GameClass" %>
<%@ page import="static org.codedefenders.GameState.ACTIVE" %>
<div class="game-container">
<nav class="nest" style="width: 80%; margin-left: auto; margin-right: auto;">
    <div class="crow fly">
        <div style="text-align: left">
            <h3><%= role %>::<%= mg.getState().toString() %></h3>
        </div>
        <div style="text-align: center"><h1><%= mg.getCUT().getName() %></h1></div>
        <div>
            <a href="#" class="btn btn-default btn-diff" id="btnScoreboard" data-toggle="modal" data-target="#scoreboard">Show Scoreboard</a>
        </div>
    </div>
</nav>
<div class="clear"></div>
