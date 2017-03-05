package board.service;

import jdbc.connection.ConnectionProvider;
import jdbc.loader.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IdGenerator {
    private static IdGenerator instance = new IdGenerator();

    public static IdGenerator getInstance() {
        return instance;
    }

    private IdGenerator() {
    }

    public int generateNextId(String sequenceName) throws IdGenerationFailedException {
        Connection conn = null;
        PreparedStatement pstmtSelect = null;
        ResultSet rs = null;
        PreparedStatement pstmtUpdate = null;
        try {
            conn = ConnectionProvider.getConnection();
            conn.setAutoCommit(false);
            pstmtSelect = conn.prepareStatement("SELECT next_value FROM id_sequence" +
                    " WHERE sequence_name = ? FOR UPDATE ");
            pstmtSelect.setString(1, sequenceName);
            rs = pstmtSelect.executeQuery();
            rs.next();
            int id = rs.getInt(1);
            id++;

            pstmtUpdate = conn.prepareStatement("UPDATE id_sequence SET next_value = ?" +
                    " WHERE sequence_name = ?");
            pstmtUpdate.setInt(1, id);
            pstmtUpdate.setString(2, sequenceName);
            pstmtUpdate.executeUpdate();

            conn.commit();

            return id;
        } catch (SQLException ex) {
            JdbcUtil.rollBack(conn);
            throw new IdGenerationFailedException(ex);
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
