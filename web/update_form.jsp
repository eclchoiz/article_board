<%@ page import="board.model.Article" %>
<%@ page import="board.service.ReadArticleService" %>
<%@ page import="board.service.ArticleNotFoundException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String viewPage = null;
    try {
        int articleId = Integer.parseInt(request.getParameter("articleId"));
        Article article = ReadArticleService.getInstance().getArticle(articleId);
        viewPage = "update_form_view.jsp";
        request.setAttribute("article", article);
    } catch (ArticleNotFoundException e) {
        viewPage = "article_not_found.jsp";
    }
%>
<jsp:forward page="<%=viewPage%>"/>
