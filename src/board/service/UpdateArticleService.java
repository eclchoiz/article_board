package board.service;

import board.dao.ArticleDao;
import board.model.Article;
import jdbc.connection.ConnectionProvider;
import jdbc.loader.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class UpdateArticleService {
    private static UpdateArticleService instance = new UpdateArticleService();
    public static UpdateArticleService getInstance() {
        return instance;
    }

    public UpdateArticleService() {
    }

    public Article update(UpdateRequest updateRequest)
            throws ArticleNotFoundException, InvalidPasswordException {
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);

            ArticleCheckHelper checkHelper = new ArticleCheckHelper();
            checkHelper.checkExistsAndPassword(conn,
                    updateRequest.getArticleId(), updateRequest.getPassword());

            Article updatedArticle = new Article();
            updatedArticle.setId(updateRequest.getArticleId());
            updatedArticle.setTitle(updateRequest.getTitle());
            updatedArticle.setContent(updateRequest.getContent());

            ArticleDao articleDao = ArticleDao.getInstance();
            int updateCount = articleDao.update(conn, updatedArticle);

            if (updateCount == 0) {
                throw new ArticleNotFoundException("게시글이 존재하지 않음: " + updateRequest.getArticleId());
            }
            Article article = articleDao.selectById(conn, updateRequest.getArticleId());

            conn.commit();

            return article;
        } catch (SQLException e) {
            JdbcUtil.rollBack(conn);
            throw new RuntimeException("DB 에러: " + e.getMessage(), e);
        } catch (ArticleNotFoundException e) {
            JdbcUtil.rollBack(conn);
            throw e;
        } catch (InvalidPasswordException e) {
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                }
                JdbcUtil.close(conn);
            }
        }
    }
}
