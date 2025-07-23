package com.api.MineSweeper;
import java.util.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MineGame
{
    private int gridSize;
    private int mineTotal;
    private char[][] playerBoard;
    private int[][] answerBoard;
    private boolean firstBoard = true;
    private boolean isGameOver = false;
    private Set<VisitedCoordinates> visited;

    public MineGame(int gridSize, int mineTotal)
    {
        this.gridSize = gridSize;
        this.mineTotal = mineTotal;
        this.playerBoard = new char[gridSize][gridSize];
        this.answerBoard = new int[gridSize][gridSize];
        this.visited = new HashSet<>();
        initializeBoard();
    }

    private void initializeBoard()
    {
        for (char[] row : playerBoard)
        {
            Arrays.fill(row, '_');
        }
        deployMines();
        computeHints();
    }

    private void deployMines()
    {
        Random rng = new Random();
        int count = 0;
        while (count < mineTotal)
        {
            int row = rng.nextInt(gridSize);
            int col = rng.nextInt(gridSize);
            if (answerBoard[row][col] != -1)
            {
                answerBoard[row][col] = -1;
                count++;
            }
        }
    }

    private void computeHints() {
        /*  c is current cell (dx, dy)-> goes to (dx + x , dy + y) check all neighbouring cells
        *    -1 0 1
        *  -1 x x x
        *   0 x c x
        *   1 x x x
        * */
        for (int currentX = 0; currentX < gridSize; currentX++)
        {
            for (int currentY = 0; currentY < gridSize; currentY++)
            {
                if (answerBoard[currentX][currentY] == -1) continue;
                int nearbyMines = 0;
                for (int dx = -1; dx <= 1; dx++)
                {
                    for (int dy = -1; dy <= 1; dy++)
                    {
                        int nextX = currentX + dx, nextY = currentY + dy;
                        if(currentX == nextX && currentY == nextY) continue;
                        if(nextX < 0 || nextX >= gridSize  || nextY < 0 || nextY >= gridSize) continue;
                        if(answerBoard[nextX][nextY] == -1)
                        {
                            nearbyMines++;
                        }
                    }
                }
                answerBoard[currentX][currentY] = nearbyMines;
            }
        }
    }
    public void gameStep(int row, int col, int[][] answerBoard, char[][]playerBoard)
    {

        revealCell(row, col, visited);
        if (answerBoard[row][col] == -1)
        {
            System.out.println("Oh no, you detonated a mine! Game over.");
            isGameOver = true;
            return;
        }
        else
        {
            System.out.printf("This square contains %d adjacent mines.", answerBoard[row][col]);
            System.out.println(" ");
        }

        if (isVictory())
        {
            displayBoard(playerBoard);
            System.out.println("Congratulations, you have won the game!");
            isGameOver = true;
        }
    }
    public void start(Scanner scanner)
    {
        while (!isGameOver)
        {
            displayBoard(playerBoard);
            System.out.println(" ");
            System.out.print("Select a square to reveal (e.g. A1): ");
            String command = scanner.nextLine().toUpperCase();
            if (command.length() < 2) continue;
            int row = command.charAt(0) - 'A';
            int col = Integer.parseInt(command.substring(1)) - 1;
            if (row < 0 || row >= gridSize || col < 0 || col >= gridSize || playerBoard[row][col] != '_')
            {
                System.out.println("Invalid or already revealed spot. Try again.");
                continue;
            }
            gameStep(row, col, answerBoard, playerBoard);
        }
    }

    private void revealCell(int row, int col, Set<VisitedCoordinates> visited)
    {
        //base case if cell is opened or if out of bounds return
        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize)
        {
            return;
        }
        VisitedCoordinates check = new VisitedCoordinates(row, col);
        if (visited.contains(check))
        {
            return;
        }
        visited.add(new VisitedCoordinates(row, col));
        if (answerBoard[row][col] == 0)
        {
            // reveal neighbouring cells via recursive call if current cell has 0 adjacent mines
            playerBoard[row][col] = '0';
            for (int dx = -1; dx <= 1; dx++)
            {
                for (int dy = -1; dy <= 1; dy++)
                {
                    revealCell(row + dx, col + dy, visited);
                }
            }
        }
        else
        {
            //change visible board for current cell and don't recursive call
            //This line converts an integer (e.g., number of adjacent mines) from your internalBoard into the corresponding character, and stores it in visibleBoard.
            //'0' + integer  converts to numeric ASCII
            playerBoard[row][col] = (char) ('0' + answerBoard[row][col]);
        }
    }
    private void DisplayBoardMessage()
    {
        if(isFirstBoard())
        {
            System.out.println("\nHere is your minefield:");
            setFirstBoard(false);
        }else
        {
            System.out.println("\nHere is your updated minefield:");
        }
    }

    private void displayBoard(char[][] board)
    {
        DisplayBoardMessage();
        System.out.print("  ");
        for (int i = 1; i <= gridSize; i++) System.out.print(i + " ");
        System.out.println();
        for (int r = 0; r < gridSize; r++)
        {
            System.out.print((char) ('A' + r) + " ");
            for (int c = 0; c < gridSize; c++)
            {
                System.out.print(board[r][c] + " ");
            }
            System.out.println();
        }
    }
    private boolean isVictory()
    {
        if(visited.size() == gridSize * gridSize - mineTotal)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
