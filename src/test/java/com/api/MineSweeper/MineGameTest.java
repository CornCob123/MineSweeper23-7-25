package com.api.MineSweeper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MineGameTest {
    @Autowired
    private MineGame game;

    @BeforeEach
    void setUp() {
        game = new MineGame(4, 0); // 4x4 grid with 0 mines to test logic
    }

    @Test
    void testRevealCellPrivateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = MineGame.class.getDeclaredMethod("revealCell", int.class, int.class,Set.class);
        method.setAccessible(true);

        game.getAnswerBoard()[0][0] = 1;
        method.invoke(game, 0, 0, new HashSet<VisitedCoordinates>());

        assertEquals('1', game.getPlayerBoard()[0][0]);
    }

    // Additional basic tests remain unchanged
    @Test
    void testDeployMinesRespectsMineCount() {
        MineGame g = new MineGame(5, 5);
        int count = 0;
        for (int[] row : g.getAnswerBoard()) {
            for (int cell : row) {
                if (cell == -1) count++;
            }
        }
        assertEquals(5, count);
    }

    @Test
    void testIsVictoryTrueWhenAllSafeRevealed() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        MineGame g = new MineGame(2, 1);
        g.getAnswerBoard()[0][0] = -1; // set a mine
        g.getAnswerBoard()[0][1] = 1;
        g.getAnswerBoard()[1][0] = 1;
        g.getAnswerBoard()[1][1] = 1;
        g.getPlayerBoard()[0][0] = '_'; // set a mine
        g.getPlayerBoard()[0][1] = 1;
        g.getPlayerBoard()[1][0] = 1;
        g.getPlayerBoard()[1][1] = 1;

        Field gridSize = MineGame.class.getDeclaredField("gridSize");
        gridSize.setAccessible(true);
        gridSize.set(g, 2);
        Field mineTotal = MineGame.class.getDeclaredField("mineTotal");
        mineTotal.setAccessible(true);
        mineTotal.set(g,1);
        Field setVisited = MineGame.class.getDeclaredField("visited");
        setVisited.setAccessible(true);
        Set tempVisit = new HashSet<VisitedCoordinates>();
        tempVisit.add(new VisitedCoordinates(0,1));
        tempVisit.add(new VisitedCoordinates(1,0));
        tempVisit.add(new VisitedCoordinates(1,1));
        setVisited.set(g, tempVisit);
        Method method = MineGame.class.getDeclaredMethod("isVictory");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(g);
        assertTrue(result);
    }

    @Test
    void testIsVictoryFalseWhenUnrevealedSafeCells() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MineGame g = new MineGame(2, 1);
        g.getAnswerBoard()[0][0] = -1;
        g.getAnswerBoard()[0][1] = 0;
        g.getAnswerBoard()[1][0] = 1;
        g.getAnswerBoard()[1][1] = 1;

        Method method = MineGame.class.getDeclaredMethod("isVictory" );
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(g);
        assertFalse(result);
    }

    @Test
    void testDeployMinesCorrectCount() {
        MineGame g = new MineGame(5, 5);
        int count = 0;
        for (int[] row : g.getAnswerBoard()) {
            for (int cell : row) {
                if (cell == -1) count++;
            }
        }
        assertEquals(5, count);
    }

    @Test
    void testVictoryTrueWhenAllSafeRevealed() throws Exception {
        MineGame g = new MineGame(2, 1);

        g.getAnswerBoard()[0][0] = -1;
        g.getAnswerBoard()[0][1] = 1;
        g.getAnswerBoard()[1][0] = 1;
        g.getAnswerBoard()[1][1] = 1;

        g.getPlayerBoard()[0][0] = '_';
        g.getPlayerBoard()[0][1] = 1;
        g.getPlayerBoard()[1][0] = 1;
        g.getPlayerBoard()[1][1] = 1;

        Field gridSize = MineGame.class.getDeclaredField("gridSize");
        gridSize.setAccessible(true);
        gridSize.set(g, 2);
        Field mineTotal = MineGame.class.getDeclaredField("mineTotal");
        mineTotal.setAccessible(true);
        mineTotal.set(g,1);

        Field setVisited = MineGame.class.getDeclaredField("visited");
        setVisited.setAccessible(true);
        Set tempVisit = new HashSet<VisitedCoordinates>();
        tempVisit.add(new VisitedCoordinates(0,1));
        tempVisit.add(new VisitedCoordinates(1,0));
        tempVisit.add(new VisitedCoordinates(1,1));
        setVisited.set(g, tempVisit);

        Method isVictory = MineGame.class.getDeclaredMethod("isVictory");
        isVictory.setAccessible(true);
        assertTrue((Boolean) isVictory.invoke(g));
    }

    @Test
    void testVictoryFalseWhenSafeCellUnrevealed() throws Exception {
        MineGame g = new MineGame(2, 1);
        g.getAnswerBoard()[0][0] = -1;
        g.getAnswerBoard()[0][1] = 0;
        g.getAnswerBoard()[1][0] = 1;
        g.getAnswerBoard()[1][1] = 1;

        Method isVictory = MineGame.class.getDeclaredMethod("isVictory");
        isVictory.setAccessible(true);
        assertFalse((Boolean) isVictory.invoke(g));
    }

    @Test
    void testVisibleBoardInitialized() {
        for (char[] row : game.getPlayerBoard()) {
            for (char c : row) {
                assertEquals('_', c);
            }
        }
    }

    @Test
    void testInternalBoardNotNull() {
        assertNotNull(game.getAnswerBoard());
    }


    @Test
    void testCalculateHintsNonMine() throws Exception {
        game.getAnswerBoard()[0][0] = -1;
        Method calc = MineGame.class.getDeclaredMethod("computeHints");
        calc.setAccessible(true);
        calc.invoke(game);

        assertTrue(game.getAnswerBoard()[0][1] > 0);
    }

    @Test
    void testNoExceptionOnRepeatedReveal() throws Exception {
        Method reveal = MineGame.class.getDeclaredMethod("revealCell", int.class, int.class, Set.class);
        reveal.setAccessible(true);
        reveal.invoke(game, 0, 0, new HashSet<VisitedCoordinates>());
        reveal.invoke(game, 0, 0, new HashSet<VisitedCoordinates>());
        reveal.invoke(game, 0, 0, new HashSet<VisitedCoordinates>()); // Should not throw
    }



    @Test
    void testMineFieldNoNegativeIndexes() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            int test = game.getAnswerBoard()[-1][-1];
        });
    }

    @Test
    void testComputeHintsNoCrashOnAllZero() throws Exception {
        Method calc = MineGame.class.getDeclaredMethod("computeHints");
        calc.setAccessible(true);
        calc.invoke(game);
    }

    @Test
    void testMineGameConstructorNotNullFields() {
        assertNotNull(game);
        assertNotNull(game.getPlayerBoard());
        assertNotNull(game.getAnswerBoard());
    }

    @Test
    void testMineBoardHintValuesBounded() throws Exception {
        game.getAnswerBoard()[1][1] = -1;
        Method calc = MineGame.class.getDeclaredMethod("computeHints");
        calc.setAccessible(true);
        calc.invoke(game);

        for (int[] row : game.getAnswerBoard()) {
            for (int val : row) {
                assertTrue(val >= -1 && val <= 8);
            }
        }
    }
   public int[][] initAnswerBoard(int size){
       int[][] ans = new int[size][size];
       for(int i = 0; i< size; i++){
           for(int j =0; j < size; j++){
               ans[i][j] = 0;
           }
       }
        return ans;
   }
    @Test
    public void test2By2Grid_MineBottomRight_ClearOthersFirst() throws Exception {
        MineGame game = new MineGame();
        int[][] ans = initAnswerBoard(2);
        char[][] playerBoard = new char[2][2];
        ans[1][1] = -1; // Mine at bottom-right

        Field gridField = MineGame.class.getDeclaredField("gridSize");
        gridField.setAccessible(true);
        gridField.set(game, 2);

        Field totalMineField = MineGame.class.getDeclaredField("mineTotal");
        totalMineField.setAccessible(true);
        totalMineField.set(game, 1);

        Field field = MineGame.class.getDeclaredField("answerBoard");
        field.setAccessible(true);
        field.set(game, ans);

        for (char[] row : playerBoard) {
            Arrays.fill(row, '_');
        }

        Field fieldPlayer = MineGame.class.getDeclaredField("playerBoard");
        fieldPlayer.setAccessible(true);
        fieldPlayer.set(game, playerBoard);

        Field setVisited = MineGame.class.getDeclaredField("visited");
        setVisited.setAccessible(true);
        setVisited.set(game, new HashSet<VisitedCoordinates>());

        Method computeHints = MineGame.class.getDeclaredMethod("computeHints");
        computeHints.setAccessible(true);
        computeHints.invoke(game);

        Method start = MineGame.class.getDeclaredMethod("gameStep", int.class, int.class, int[][].class, char[][].class);
        start.setAccessible(true);

        // Safely reveal all non-mine cells
        start.invoke(game, 0, 0, ans, playerBoard);
        assertFalse(game.isGameOver());
        start.invoke(game, 0, 1, ans, playerBoard);
        assertFalse(game.isGameOver());
        start.invoke(game, 1, 0, ans, playerBoard);
        assertTrue(game.isGameOver()); // All safe cells revealed
    }
    @Test
    public void test3By3GridNoChosenMines() throws Exception{
        MineGame game3 = new MineGame();
        int[][] ans = initAnswerBoard(3);
        char[][] playerBoard = new char[3][3];
        ans[2][2] = -1;
        ans[0][2] = -1;

        Field gridField = MineGame.class.getDeclaredField("gridSize");
        gridField.setAccessible(true);
        gridField.set(game3, 3);
        Field totalMineField = MineGame.class.getDeclaredField("mineTotal");
        totalMineField.setAccessible(true);
        totalMineField.set(game3, 2);
        Field field = MineGame.class.getDeclaredField("answerBoard");
        field.setAccessible(true);
        field.set(game3, ans);
        for (char[] row : playerBoard)
        {
            Arrays.fill(row, '_');
        }
        Field fieldPlayer = MineGame.class.getDeclaredField("playerBoard");
        fieldPlayer.setAccessible(true);
        fieldPlayer.set(game3, playerBoard);

        Field setVisited = MineGame.class.getDeclaredField("visited");
        setVisited.setAccessible(true);
        setVisited.set(game3, new HashSet<VisitedCoordinates>());

        Method computeHints = MineGame.class.getDeclaredMethod("computeHints");
        computeHints.setAccessible(true);
        computeHints.invoke(game3);

        Method start = MineGame.class.getDeclaredMethod("gameStep",int.class, int.class, int[][].class, char[][].class);
        start.setAccessible(true);
        start.invoke(game3, 0,0,ans,playerBoard);
        assertFalse(game3.isGameOver());
        start.invoke(game3, 0,1,ans,playerBoard);
        assertFalse(game3.isGameOver());

        start.invoke(game3, 1,0,ans,playerBoard);
        assertFalse(game3.isGameOver());
        start.invoke(game3, 1,1,ans,playerBoard);
        assertFalse(game3.isGameOver());
        start.invoke(game3, 1,2,ans,playerBoard);
        assertTrue(game3.isGameOver());

    }
    @Test
    public void test3By3Grid_MiddleMineLoss() throws Exception {
        MineGame game = new MineGame();
        int[][] ans = initAnswerBoard(3);
        char[][] playerBoard = new char[3][3];
        ans[1][1] = -1;
        ans[0][0] = -1;

        Field gridField = MineGame.class.getDeclaredField("gridSize");
        gridField.setAccessible(true);
        gridField.set(game, 3);

        Field totalMineField = MineGame.class.getDeclaredField("mineTotal");
        totalMineField.setAccessible(true);
        totalMineField.set(game, 2);

        Field field = MineGame.class.getDeclaredField("answerBoard");
        field.setAccessible(true);
        field.set(game, ans);

        for (char[] row : playerBoard) {
            Arrays.fill(row, '_');
        }

        Field fieldPlayer = MineGame.class.getDeclaredField("playerBoard");
        fieldPlayer.setAccessible(true);
        fieldPlayer.set(game, playerBoard);

        Field setVisited = MineGame.class.getDeclaredField("visited");
        setVisited.setAccessible(true);
        setVisited.set(game, new HashSet<VisitedCoordinates>());

        Method computeHints = MineGame.class.getDeclaredMethod("computeHints");
        computeHints.setAccessible(true);
        computeHints.invoke(game);

        Method start = MineGame.class.getDeclaredMethod("gameStep", int.class, int.class, int[][].class, char[][].class);
        start.setAccessible(true);

        start.invoke(game, 0,1,ans,playerBoard);
        assertFalse(game.isGameOver());
        start.invoke(game, 1,0,ans,playerBoard);
        assertFalse(game.isGameOver());
        start.invoke(game, 1,1,ans,playerBoard); // Hit a mine
        assertTrue(game.isGameOver());
    }

    @Test
    public void test3By3Grid_EdgeMineSafeFinish() throws Exception {
        MineGame game = new MineGame();
        int[][] ans = initAnswerBoard(3);
        char[][] playerBoard = new char[3][3];
        ans[2][0] = -1;
        ans[0][0] = -1;

        Field gridField = MineGame.class.getDeclaredField("gridSize");
        gridField.setAccessible(true);
        gridField.set(game, 3);

        Field totalMineField = MineGame.class.getDeclaredField("mineTotal");
        totalMineField.setAccessible(true);
        totalMineField.set(game, 2);

        Field field = MineGame.class.getDeclaredField("answerBoard");
        field.setAccessible(true);
        field.set(game, ans);

        for (char[] row : playerBoard) {
            Arrays.fill(row, '_');
        }

        Field fieldPlayer = MineGame.class.getDeclaredField("playerBoard");
        fieldPlayer.setAccessible(true);
        fieldPlayer.set(game, playerBoard);

        Field setVisited = MineGame.class.getDeclaredField("visited");
        setVisited.setAccessible(true);
        setVisited.set(game, new HashSet<VisitedCoordinates>());

        Method computeHints = MineGame.class.getDeclaredMethod("computeHints");
        computeHints.setAccessible(true);
        computeHints.invoke(game);

        Method start = MineGame.class.getDeclaredMethod("gameStep", int.class, int.class, int[][].class, char[][].class);
        start.setAccessible(true);

        start.invoke(game, 0,1,ans,playerBoard);
        assertFalse(game.isGameOver());
        start.invoke(game, 1,1,ans,playerBoard);
        assertFalse(game.isGameOver());
        start.invoke(game, 1,2,ans,playerBoard);
        assertFalse(game.isGameOver());
        start.invoke(game, 2,2,ans,playerBoard);
        assertFalse(game.isGameOver()); // All safe cells revealed
        start.invoke(game, 1,0,ans,playerBoard);
        assertTrue(game.isGameOver()); // All safe cells revealed
    }

    @Test
    public void test3By3GridHitMines() throws Exception{
        MineGame game3 = new MineGame();
        int[][] ans = initAnswerBoard(3);
        char[][] playerBoard = new char[3][3];
        ans[2][2] = -1;
        ans[0][2] = -1;
        Field gridField = MineGame.class.getDeclaredField("gridSize");
        gridField.setAccessible(true);
        gridField.set(game3, 3);
        Field totalMineField = MineGame.class.getDeclaredField("mineTotal");
        totalMineField.setAccessible(true);
        totalMineField.set(game3, 2);
        Field field = MineGame.class.getDeclaredField("answerBoard");
        field.setAccessible(true);
        field.set(game3, ans);
        for (char[] row : playerBoard)
        {
            Arrays.fill(row, '_');
        }
        Field setVisited = MineGame.class.getDeclaredField("visited");
        setVisited.setAccessible(true);
        setVisited.set(game3, new HashSet<VisitedCoordinates>());

        Field fieldPlayer = MineGame.class.getDeclaredField("playerBoard");
        fieldPlayer.setAccessible(true);
        fieldPlayer.set(game3, playerBoard);

        Method computeHints = MineGame.class.getDeclaredMethod("computeHints");
        computeHints.setAccessible(true);
        computeHints.invoke(game3);

        Method start = MineGame.class.getDeclaredMethod("gameStep",int.class, int.class, int[][].class, char[][].class);
        start.setAccessible(true);
        start.invoke(game3, 0,2,ans,playerBoard);
        assertTrue(game3.isGameOver());
        start.invoke(game3, 2,2,ans,playerBoard);
        assertTrue(game3.isGameOver());


    }

    @Test
    public void test4By4Grid_CenterClearPath_WinGame() throws Exception {
        MineGame game = new MineGame();
        int[][] ans = initAnswerBoard(4);
        char[][] playerBoard = new char[4][4];

        // Place mines on the border
        for (int i = 0; i < 4; i++) {
            ans[0][i] = -1;
            ans[3][i] = -1;
            ans[i][0] = -1;
            ans[i][3] = -1;
        }

        Field gridField = MineGame.class.getDeclaredField("gridSize");
        gridField.setAccessible(true);
        gridField.set(game, 4);

        Field totalMineField = MineGame.class.getDeclaredField("mineTotal");
        totalMineField.setAccessible(true);
        totalMineField.set(game, 12);  // 12 border cells

        Field field = MineGame.class.getDeclaredField("answerBoard");
        field.setAccessible(true);
        field.set(game, ans);

        for (char[] row : playerBoard) {
            Arrays.fill(row, '_');
        }

        Field fieldPlayer = MineGame.class.getDeclaredField("playerBoard");
        fieldPlayer.setAccessible(true);
        fieldPlayer.set(game, playerBoard);

        Field setVisited = MineGame.class.getDeclaredField("visited");
        setVisited.setAccessible(true);
        setVisited.set(game, new HashSet<VisitedCoordinates>());

        Method computeHints = MineGame.class.getDeclaredMethod("computeHints");
        computeHints.setAccessible(true);
        computeHints.invoke(game);

        Method start = MineGame.class.getDeclaredMethod("gameStep", int.class, int.class, int[][].class, char[][].class);
        start.setAccessible(true);

        // Reveal all safe center cells
        start.invoke(game, 1, 1, ans, playerBoard);
        assertFalse(game.isGameOver());
        start.invoke(game, 1, 2, ans, playerBoard);
        assertFalse(game.isGameOver());
        start.invoke(game, 2, 1, ans, playerBoard);
        assertFalse(game.isGameOver());
        start.invoke(game, 2, 2, ans, playerBoard);
        assertTrue(game.isGameOver()); // All safe cells revealed
    }
}
