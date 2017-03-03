package board.service;

import board.dao.ArticleDao;
import board.mode.Article;
import jdbc.connection.ConnectionProvider;
import jdbc.loader.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class ReadArticleService {
    private static ReadArticleService instance = new ReadArticleService();
    public static ReadArticleService getInstance() {
        return instance;
    }

    public ReadArticleService() {
    }

    public Article readArticle(int articleId) throws ArticleNotFondException {
        return selectArticle(articleId, true);
    }

    private Article selectArticle(int articleId, boolean increaseCount) throws ArticleNotFondException{
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            ArticleDao articleDao = ArticleDao.getInstance();
            Article article = articleDao.selectedById(conn, articleId);

            if (article == null) {
                throw new ArticleNotFondException("게시글이 존재하지 않음: " + articleId);
            }
            if (increaseCount) {
                articleDao.increaseReadCount(conn, articleId);
                article.setReadCount(article.getReadCount() + 1);
            }
            return article;
        } catch (SQLException e) {
            throw new RuntimeException("DB 에러: " + e.getMessage(), e);
        } finally {
            JdbcUtil.close(conn);
        }
    }

    public Article getArticle(int articleId) throws ArticleNotFondException {
        return selectArticle(articleId, false);
    }
}
