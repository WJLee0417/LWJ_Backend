package com.test.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.test.dto.Board;
import com.test.util.DBUtil;

class BoardDAOIntegrationTest {

    private final BoardDAO boardDAO = new BoardDAO();

    @BeforeAll
    static void requireDatabaseEnvironment() {
        Assumptions.assumeTrue(
                hasText("DB_URL") && hasText("DB_USERNAME") && hasText("DB_PASSWORD"),
                "DB integration tests require DB_URL, DB_USERNAME, and DB_PASSWORD.");
    }

    @BeforeEach
    void resetDatabase() throws IOException, SQLException {
        String sql = readInitSql();
        try (Connection connection = DBUtil.getConnection(); Statement statement = connection.createStatement()) {
            for (String command : sql.split(";")) {
                if (!command.isBlank()) {
                    statement.execute(command);
                }
            }
        }
    }

    @Test
    void createsListsFindsAndCountsBoardViews() {
        assertEquals(2, boardDAO.getNoticeList().size());

        Board newBoard = new Board(0, "free", "integration test", "board dao integration test", null, 0, null);
        assertTrue(boardDAO.insertBoard(newBoard));

        List<Board> boards = boardDAO.getBoardList("전체", "title", "integration test", 0, 10);
        assertEquals(1, boards.size());
        assertEquals(1, boardDAO.getTotalBoardCount("전체", "title", "integration test"));

        Board insertedBoard = boardDAO.getBoardById(boards.get(0).getId());
        assertNotNull(insertedBoard);
        assertEquals(0, insertedBoard.getViews());

        boardDAO.incrementViewCount(insertedBoard.getId());
        assertEquals(1, boardDAO.getBoardById(insertedBoard.getId()).getViews());
    }

    private static boolean hasText(String name) {
        String value = System.getenv(name);
        return value != null && !value.isBlank();
    }

    private static String readInitSql() throws IOException {
        try (InputStream inputStream = BoardDAOIntegrationTest.class.getResourceAsStream("/sql/init.sql")) {
            if (inputStream == null) {
                throw new IllegalStateException("init.sql resource was not found.");
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
