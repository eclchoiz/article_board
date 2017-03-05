<%@ page import="board.model.Article" %>
<%@ page import="board.service.ReadArticleService" %>
<%@ page import="board.service.ArticleNotFoundException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    int articleId = Integer.parseInt(request.getParameter("articleId"));
    String viewPage = null;
    try {
        Article article = ReadArticleService.getInstance().readArticle(articleId);
        request.setAttribute("article", article);
        viewPage = "read_view.jsp";
    } catch (ArticleNotFoundException ex) {
        viewPage = "article_not_found.jsp";
    }
%>
<jsp:forward page="<%= viewPage %>"/>