import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ChessGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGUI().createAndShowGUI());
    }
}

class ChessGUI {
    private JFrame frame;
    private JPanel boardPanel;
    private JButton[][] squares = new JButton[8][8];
    private ChessGameLogic gameLogic = new ChessGameLogic();
    private Point selectedPiece = null;

    public void createAndShowGUI() {
        frame = new JFrame("Java Chess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        boardPanel = new JPanel(new GridLayout(8, 8));
        initializeBoard();

        frame.add(boardPanel);
        frame.setVisible(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setBackground((row + col) % 2 == 0 ? Color.WHITE : Color.GRAY);
                int r = row, c = col;
                square.addActionListener(e -> handleClick(r, c));
                squares[row][col] = square;
                boardPanel.add(square);
            }
        }
        updateBoard();
    }

    private void handleClick(int row, int col) {
        if (selectedPiece == null) {
            if (gameLogic.hasPiece(row, col)) {
                selectedPiece = new Point(row, col);
            }
        } else {
            if (gameLogic.movePiece(selectedPiece.x, selectedPiece.y, row, col)) {
                updateBoard();
            }
            selectedPiece = null;
        }
    }

    private void updateBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setText(gameLogic.getPieceSymbol(row, col));
            }
        }
    }
}

class ChessGameLogic {
    private String[][] board = new String[8][8];
    private boolean whiteTurn = true;

    public ChessGameLogic() {
        setupBoard();
    }

    private void setupBoard() {
        // Setup pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = "♟"; // Black pawns
            board[6][i] = "♙"; // White pawns
        }
        // Setup Rooks
        board[0][0] = board[0][7] = "♜";
        board[7][0] = board[7][7] = "♖";
        // Setup Knights
        board[0][1] = board[0][6] = "♞";
        board[7][1] = board[7][6] = "♘";
        // Setup Bishops
        board[0][2] = board[0][5] = "♝";
        board[7][2] = board[7][5] = "♗";
        // Setup Queens
        board[0][3] = "♛"; board[7][3] = "♕";
        // Setup Kings
        board[0][4] = "♚"; board[7][4] = "♔";
    }

    public boolean hasPiece(int row, int col) {
        return board[row][col] != null;
    }

    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        if (!hasPiece(fromRow, fromCol)) return false;
        if (!isLegalMove(fromRow, fromCol, toRow, toCol)) return false;

        board[toRow][toCol] = board[fromRow][fromCol];
        board[fromRow][fromCol] = null;
        whiteTurn = !whiteTurn;
        return true;
    }

    private boolean isLegalMove(int fromRow, int fromCol, int toRow, int toCol) {
        // Basic rule: can't move to same spot or on top of same team (simplified)
        if (fromRow == toRow && fromCol == toCol) return false;
        if (board[toRow][toCol] != null && isSameTeam(board[fromRow][fromCol], board[toRow][toCol])) return false;
        // Add real piece rules here later...
        return true;
    }

    private boolean isSameTeam(String p1, String p2) {
        return (Character.isUpperCase(p1.charAt(0)) && Character.isUpperCase(p2.charAt(0))) ||
               (Character.isLowerCase(p1.charAt(0)) && Character.isLowerCase(p2.charAt(0)));
    }

    public String getPieceSymbol(int row, int col) {
        return board[row][col] == null ? "" : board[row][col];
    }
}
