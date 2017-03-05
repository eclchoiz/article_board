<%@ page import="board.model.Article" %>
<%@ page import="board.service.UpdateArticleService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("utf8");
%>
<jsp:useBean id="updateRequest" class="board.service.UpdateRequest"/>
<jsp:setProperty name="updateRequest" property="*"/>
<%
    String viewPage = null;
    try {
        Article article = UpdateArticleService.getInstance().update(updateRequest);
        request.setAttribute("updatedArticle", article);
        viewPage = "update_success.jsp";
    } catch (Exception e) {
        request.setAttribute("updateException", e);
        viewPage = "update_error.jsp";
    }
%>
<jsp:forward page="<%=viewPage%>"/>