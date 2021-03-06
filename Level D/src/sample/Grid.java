package sample;

import java.util.Random;

class Grid {
    private int rows;
    private int columns;
    private int numberOfBombs;
    private boolean isFinished;
    private Random rand;
    private int level;
    Position[][] board;

    private String playerA;
    private String playerB;
    private int scoreA;
    private int scoreB;
    private int leftBombs;
    private boolean turn;
    private int moves;

    Grid(int rows, int columns, String name, int l) {
        this.rows=rows;
        this.columns=columns;
        this.isFinished = false;
        this.scoreA=0;
        this.scoreB=0;
        this.turn=false;
        this.playerA=name;
        this.level=l;
        this.moves=0;

        int min, max;
        if (l == 1) {
            //easy game = 10%-20% of board are bombs
            min = (int)(rows*columns*0.1);
            max = (int)(rows*columns*0.2);
        } else if (l == 2) {
            //moderate game = 20%-30% of board are bombs
            min = (int)(rows*columns*0.2);
            max = (int)(rows*columns*0.3);
        } else {
            //hard game = 30%-40% of board are bombs
            min = (int)(rows*columns*0.3);
            max = (int)(rows*columns*0.4);
        }

        rand = new Random();
        this.numberOfBombs=rand.nextInt(max-min) + min + 1;
        this.leftBombs=numberOfBombs;

        board = new Position[rows][columns];
        for(int i=0;i<rows;i++) {
            for(int j=0;j<columns;j++) {
                try {
                    board[i][j]=new Position();
                } catch (Exception ImageNotFound) {
                    System.out.println("One of the image was not found in the directory.");
                    return;
                }
            }
        }
        plantMines();
    }
    Grid(int rows, int columns, String a, String b, int l) {
        this.rows=rows;
        this.columns=columns;
        this.isFinished = false;
        this.scoreA=0;
        this.scoreB=0;
        this.turn=false;
        this.playerA = a;
        this.playerB = b;
        this.level=l;

        int min, max;
        if (l == 1) {
            //easy game = 10%-20% of board are bombs
            min = (int)(rows*columns*0.1);
            max = (int)(rows*columns*0.2);
        } else if (l == 2) {
            //moderate game = 20%-30% of board are bombs
            min = (int)(rows*columns*0.2);
            max = (int)(rows*columns*0.3);
        } else {
            //hard game = 30%-40% of board are bombs
            min = (int)(rows*columns*0.3);
            max = (int)(rows*columns*0.4);
        }

        rand = new Random();
        this.numberOfBombs=rand.nextInt(max-min) + min + 1;
        this.leftBombs=numberOfBombs;

        board = new Position[rows][columns];
        for(int i=0;i<rows;i++) {
            for(int j=0;j<columns;j++) {
                try {
                    board[i][j]=new Position();
                } catch (Exception ImageNotFound) {
                    System.out.println("One of the image was not found in the directory.");
                    return;
                }
            }
        }
        plantMines();
    }

    private void plantMines() {
        for(int i=0;i<numberOfBombs;i++) {
            int k = rand.nextInt(rows);
            int m = rand.nextInt(columns);
            if(board[k][m].isBomb())
                i--;
            board[k][m].setBomb(true);
        }
    }
    boolean isWon() {
        int uncoveredCells=0;
        for(int i=0; i<rows;i++) {
            for(int j=0; j<columns;j++) {
                if(!board[i][j].isShown())
                    uncoveredCells++;
            }
        }
        return uncoveredCells <= numberOfBombs;
    }
    void reveal() {
        for(int i=0; i<rows;i++) {
            for(int j=0; j<columns;j++) {
                if (board[i][j].isBomb() && board[i][j].isFlagged())
                    //just leave background as flagged
                    board[i][j].setShown(true);
                else if (board[i][j].isBomb() && !board[i][j].isFlagged())
                    //set normal bomb
                    board[i][j].setBackImage(9);
                else if (!board[i][j].isBomb() && board[i][j].isFlagged())
                    //set bot bomb
                    board[i][j].setBackImage(11);
            }
        }
        this.setFinished(true);
    }
    void check(int r, int c)  {
        int sum=0;
        for(int i = r-1;i<=r+1;i++) {
            for(int j=c-1;j<=c+1;j++) {
                try {
                    if(board[i][j].isBomb())
                        sum++;
                } catch(Exception ignored) { }
            }
        }
        if(sum!=0)
            board[r][c].setNeighborBombs(sum);
        else {
            board[r][c].setNeighborBombs(0);
            for(int i = r-1;i<=r+1;i++) {
                for(int j=c-1;j<=c+1;j++) {
                    try {
                        if(!board[i][j].isShown() && !board[i][j].isFlagged()) {
                            board[i][j].setShown(true);
                            check(i,j);
                        }
                    } catch(Exception ignored) { }
                }
            }
        }
        if(!turn)
            scoreA++;
        else
            scoreB++;
    }
    void show() {
        for(int i=0;i<rows;i++) {
            for(int j=0;j<columns;j++) {
                if(board[i][j].isShown() && !board[i][j].isBomb() && !board[i][j].isFlagged())
                    board[i][j].setBackImage(board[i][j].getNeighborBombs());
            }
        }
    }

    boolean isFinished() {
        return isFinished;
    }
    void setFinished(boolean revealed) {
        isFinished = revealed;
    }
    int getNumberOfBombs() {
        return numberOfBombs;
    }
    void setNumberOfBombs(int numberOfBombs) {
        this.numberOfBombs = numberOfBombs;
    }
    int getLevel() {
        return level;
    }
    void setLevel(int level) {
        this.level = level;
    }
    String getPlayerA() {
        return playerA;
    }
    void setPlayerA(String playerA) {
        this.playerA = playerA;
    }
    String getPlayerB() {
        return playerB;
    }
    void setPlayerB(String playerB) {
        this.playerB = playerB;
    }
    int getScoreA() {
        return scoreA;
    }
    void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }
    int getScoreB() {
        return scoreB;
    }
    void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }
    int getLeftBombs() {
        return leftBombs;
    }
    void setLeftBombs(int leftBombs) {
        this.leftBombs = leftBombs;
    }
    boolean isTurn() {
        return turn;
    }
    void setTurn(boolean turn) {
        this.turn = turn;
    }
    int getMoves() {
        return moves;
    }
    void setMoves(int moves) {
        this.moves = moves;
    }
}
