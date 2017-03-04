<%@ page import="board.service.DeleteArticleService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    request.setCharacterEncoding("utf8");
%>
<jsp:useBean id="deleteRequest" class="board.service.DeleteRequest"/>
<jsp:setProperty name="deleteRequest" property="*"/>
<%
    String viewPage = null;
    try {
        DeleteArticleService.getInstance().deleteArticle(deleteRequest);
        viewPage = "delete_success.jsp";
    } catch (Exception e) {
        request.setAttribute("deleteException", e);
        viewPage = "delete_error.jsp";
    }
%>
<jsp:forward page="<%=viewPage%>"/>