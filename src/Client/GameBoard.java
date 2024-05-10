package Client;

public class GameBoard {

    private final Choice[][] board;

    public GameBoard() {
        this.board = new Choice[3][3];
    }

    public boolean containsWinner() {
        return containsWinner(Choice.O) || containsWinner(Choice.X);
    }

    public Choice findWinner() {

        if (containsWinner(Choice.O)) {
            return Choice.O;
        }

        if (containsWinner(Choice.X)) {
            return Choice.X;
        }

        return null;
    }

    private boolean containsWinner(Choice choice) {

        for (int row = 0; row < 3; row++) {
            if (containsWinnerInLine(board[row], choice)) {
                return true;
            }
        }

        for (int col = 0; col < 3; col++) {
            Choice[] column = getColumn(col);
            if (containsWinnerInLine(column, choice)) {
                return true;
            }
        }

        final Choice[] diagonalX = new Choice[]{board[0][0], board[1][1], board[2][2]};
        final Choice[] diagonalY = new Choice[]{board[0][2], board[1][1], board[2][0]};
        return containsWinnerInLine(diagonalX, choice) || containsWinnerInLine(diagonalY, choice);
    }

    private Choice[] getColumn(int col) {

        final Choice[] column = new Choice[3];
        for (int row = 0; row < 3; row++) {
            column[row] = board[row][col];
        }

        return column;
    }

    private boolean containsWinnerInLine(Choice[] choices, Choice choice) {
        return choices[0] == choice && choices[1] == choice && choices[2] == choice;
    }

    public void setOption(Choice choice, int x, int y) {
        this.board[x][y] = choice;
    }

    public void printBoard() {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
    }
}
