package board.service;

import board.dao.ArticleDao;
import jdbc.connection.ConnectionProvider;
import jdbc.loader.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class DeleteArticleService {

    private static DeleteArticleService instance = new DeleteArticleService();
    public static DeleteArticleService getInstance() {
        return instance;
    }

    private DeleteArticleService() {
    }

    public void deleteArticle(DeleteRequest deleteRequest) throws ArticleNotFoundException, InvalidPasswordException{
        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);
            ArticleCheckHelper checkHelper = new ArticleCheckHelper();
            checkHelper.checkExistsAndPassword(conn, deleteRequest.getArticleId(), deleteRequest.getPassword());

            ArticleDao articleDao = ArticleDao.getInstance();
            articleDao.delete(conn, deleteRequest.getArticleId());

            conn.commit();
        } catch (SQLException e) {
            JdbcUtil.rollBack(conn);
            throw new RuntimeException(e);
        } catch (ArticleNotFoundException e) {
            JdbcUtil.rollBack(conn);
            throw e;
        } catch (InvalidPasswordException e) {
            JdbcUtil.rollBack(conn);
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                }
            }
            JdbcUtil.close(conn);
        }
    }
}
