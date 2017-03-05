package board.service;

import board.dao.ArticleDao;
import board.model.Article;
import jdbc.connection.ConnectionProvider;
import jdbc.loader.JdbcUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;

public class WriteArticleService {
    private static WriteArticleService instance = new WriteArticleService();

    public static WriteArticleService getInstance() {
        return instance;
    }

    public WriteArticleService() {
    }

    public Article write(WritingRequest writingRequest) throws IdGenerationFailedException {
        int groupId = IdGenerator.getInstance().generateNextId("article");
        Article article = writingRequest.toArticle();

        article.setGroupId(groupId);
        article.setPostingDate(new Date());
        DecimalFormat decimalFormat = new DecimalFormat("0000000000");
        article.setSequenceNumber(decimalFormat.format(groupId) + "999999");

        Connection conn = null;
        try {
            conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);

            int articleId = ArticleDao.getInstance().insert(conn, article);
            if (articleId == -1) {
                JdbcUtil.rollBack(conn);
                throw new RuntimeException("DB 삽입 실패: " + articleId);
            }
            conn.commit();

            article.setId(articleId);
            return article;
        } catch (SQLException e) {
            JdbcUtil.rollBack(conn);
            throw new RuntimeException("DB 에러: " + e.getMessage(), e);
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
