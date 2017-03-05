<%@ page import="board.model.Article" %>
<%@ page import="board.service.ReplyArticleService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("utf8");
%>
<jsp:useBean id="replyingRequest" class="board.service.ReplyingRequest"/>
<jsp:setProperty name="replyingRequest" property="*"/>
<%
    String viewPage = null;
    try {
        Article postedArticle = ReplyArticleService.getInstance().reply(replyingRequest);
        request.setAttribute("postedArticle", postedArticle);
        viewPage = "reply_success.jsp";
    } catch (Exception e) {
        viewPage = "reply_error.jsp";
        request.setAttribute("replyException", e);
    }
%>
<jsp:forward page="<%=viewPage%>"/>