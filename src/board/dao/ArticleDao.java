package board.dao;

import board.mode.Article;
import jdbc.loader.JdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArticleDao {
    private static ArticleDao instance = new ArticleDao();

    public static ArticleDao getInstance() {
        return instance;
    }

    private ArticleDao() {
    }

    public int selectCount(Connection conn) throws SQLException {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT count(*) FROM article");
            rs.next();
            return rs.getInt(1);
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(stmt);
        }
    }

    public List<Article> select(Connection conn, int firstRow, int endRow)
            throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT article_id, " +
                    "group_id, sequence_no, posting_date, " +
                    "read_count, writer_name, password, title " +
                    "FROM article ORDER BY sequence_no DESC LIMIT ?, ?");
            pstmt.setInt(1, firstRow - 1);
            pstmt.setInt(2, endRow - firstRow + 1);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                return Collections.emptyList();
            }
            List<Article> articleList = new ArrayList<Article>();
            do {
                Article article = makeArticleFromResultSet(rs, false);
                articleList.add(article);
            } while (rs.next());
            return articleList;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    private Article makeArticleFromResultSet(ResultSet rs, boolean readContent) throws SQLException {
        Article article = new Article();
        article.setId(rs.getInt("article_id"));
        article.setGroupId(rs.getInt("group_id"));
        article.setSequenceNumber(rs.getString("sequence_no"));
        article.setPostingDate(rs.getTimestamp("posting_date"));
        article.setReadCount(rs.getInt("read_count"));
        article.setWriteName(rs.getString("writer_name"));
        article.setPassword(rs.getString("password"));
        article.setTitle(rs.getString("title"));
        if (readContent) {
            article.setContent(rs.getString("content"));
        }
        return article;
    }

    public int insert(Connection conn, Article article) throws SQLException {
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO article" +
                    " (group_id, sequence_no, posting_date, read_count," +
                    " writer_name, password, title, content)" +
                    " VALUES (?, ?, ?, 0, ?, ?, ?, ?)");
            pstmt.setInt(1, article.getGroupId());
            pstmt.setString(2, article.getSequenceNumber());
            pstmt.setTimestamp(3, new Timestamp(article.getPostingDate().getTime()));
            pstmt.setString(4, article.getWriteName());
            pstmt.setString(5, article.getWriteName());
            pstmt.setString(6, article.getTitle());
            pstmt.setString(7, article.getContent());

            int insertedCount = pstmt.executeUpdate();

            if (insertedCount > 0) {
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT last_insert_id() FROM article");
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        } finally {
            JdbcUtil.close(conn);
            JdbcUtil.close(stmt);
            JdbcUtil.close(pstmt);
        }
    }

    public Article selectedById(Connection conn, int articleId) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement("SELECT * FROM article" +
                    " WHERE article_id = ?");
            pstmt.setInt(1, articleId);
            rs = pstmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            Article article = makeArticleFromResultSet(rs, true);
            return article;
        } finally {
            JdbcUtil.close(rs);
            JdbcUtil.close(pstmt);
        }
    }

    public void increaseReadCount(Connection conn, int articleId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("UPDATE article" +
                    " SET read_count = read_count + 1" +
                    " WHERE article_id = ?");
            pstmt.setInt(1, articleId);
            pstmt.executeUpdate();
        } finally {
            JdbcUtil.close(pstmt);
        }
    }
}
