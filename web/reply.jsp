<%@ page import="board.mode.Article" %>
<%@ page import="board.service.ReadArticleService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("utf8");
%>
<jsp:useBean id="replyingRequest" class="board.service.ReplyingRequest"/>
<jsp:setProperty name="replyingRequest" property="*"/>
<%
    String viewPage = null;
    try {
        Article postedArticle = ReadArticleService.getInstance().reply(replyingRequest);
        request.setAttribute("postedArticle", postedArticle);
        viewPage = "reply_success.jsp";
    } catch (Exception e) {
        viewPage = "reply_error.jsp";
        request.setAttribute("replyException", e);
    }
%>
<jsp:forward page="<%=viewPage%>"/>