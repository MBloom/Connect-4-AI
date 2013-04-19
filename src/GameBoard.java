import java.util.Random;

public class GameBoard {
	private int rows;
	private int columns;
	private int[][] board;
	private int connect;

	public GameBoard(int r, int c, int num) {
		rows = r;
		columns = c;
		connect = num;
		board = new int[r][c];
		for (int i = 0; i < r; i++) {
			for (int k = 0; k < c; k++) {
				board[i][k] = 0;
			}
		}
	}

	public GameBoard(int r, int c, int num, int[][] b) {
		rows = r;
		columns = c;
		connect = num;
		board = new int[r][c];
		for (int i = 0; i < r; i++) {
			for (int k = 0; k < c; k++) {
				board[i][k] = b[i][k];
			}
		}
	}

	public int dropUser(int col) {
		for (int k = rows - 1; k >= 0; k--) {
			if (board[k][col] == 0) {
				board[k][col] = 1;
				return k;
			}
		}
		return -1;
	}

	public int dropCompPiece(int col) {
		for (int k = rows - 1; k >= 0; k--) {
			if (board[k][col] == 0) {
				board[k][col] = 2;
				return k;
			}
		}
		return -1;
	}

	public int dropComp(int difficulty) {
		int[] choices = new int[columns];
		for (int i = 0; i < columns; i++) {
			GameBoard tempBoard = new GameBoard(rows, columns, connect, board);
			int compRowLoc = tempBoard.dropCompPiece(i);
			if (compRowLoc == -1) {
				choices[i] = -100;
				continue;
			} else {
				tempBoard.board[compRowLoc][i] = 2;
			}
			if (tempBoard.checkCompWin(compRowLoc, i)) {
				choices[i] = 100;
				continue;
			}
			for (int k = 0; k < columns; k++) {
				int userRow = tempBoard.dropUser(k);
				if (userRow != -1) {
					if (tempBoard.checkCompWin(userRow, k)) {
						choices[i] = -100;
						break;
					}
					tempBoard.board[userRow][k] = 0;
				}
			}
			if (choices[i] == -100)
				continue;
			else
				choices[i] = 50;
		}
		int fity = -1;
		int lose = 0;
		for (int i = 0; i < columns; i++) {
			if (choices[i] == 100)
				return i;
			else if (choices[i] == 50)
				fity = i;
		}
		if (fity != -1)
			return fity;
		else
			return lose;
	}

	public int randomDrop() {
		for (int i = 0; i < columns; i++) {
			int rowLoc = dropCompPiece(i);
			if (rowLoc != -1) {
				board[rowLoc][i] = 2;
				if (checkCompWin(rowLoc, i)) {
					board[rowLoc][i] = 0;
					return i;
				}
				board[rowLoc][i] = 0;
			}
		}
		Random generator = new Random();
		return generator.nextInt(columns);
	}

	public int randomWithDefenseDrop() {
		for (int i = 0; i < columns; i++) {
			int rowLoc = dropCompPiece(i);
			if (rowLoc != -1) {
				board[rowLoc][i] = 2;
				if (checkCompWin(rowLoc, i)) {
					board[rowLoc][i] = 0;
					return i;
				}
				board[rowLoc][i] = 0;
			}
		}
		for (int i = 0; i < columns; i++) {
			int rowLoc = dropUser(i);
			if (rowLoc != -1) {
				board[rowLoc][i] = 1;
				if (checkUserWin(rowLoc, i)) {
					board[rowLoc][i] = 0;
					return i;
				}
				board[rowLoc][i] = 0;
			}
		}
		Random generator = new Random();
		return generator.nextInt(columns);
	}

	public boolean checkUserWin(int r, int c) {

		int count = 0;
		// Check Horizontal for Win
		for (int i = 0; i < columns - 1; i++) {
			if (board[r][i] == 1 && board[r][i + 1] == 1) {
				count++;
			} else
				count = 0;
			if (count == connect - 1)
				return true;
		}

		// Check verticle for Win
		for (int i = 0; i < rows - 1; i++) {
			if (board[i][c] == 1 && board[i + 1][c] == 1) {
				count++;
			} else
				count = 0;
			if (count == connect - 1)
				return true;
		}

		// Check right to bottom diagonal.
		count = 0;
		for (int i = -connect + 1; i < connect; i++) {
			if (r + i >= 0 && r + i + 1 < rows && c + i >= 0
					&& c + i + 1 < columns) {
				if (board[r + i][c + i] == 1
						&& board[r + i + 1][c + i + 1] == 1) {
					count++;
				} else
					count = 0;
				if (count == connect - 1)
					return true;
			}
		}

		// Check right to top diagonal
		count = 0;
		int i;
		int k;
		for (i = connect - 1, k = -connect + 1; i > -connect && k < connect; i--, k++) {
			if (r + i - 1 >= 0 && r + i < rows && k + c + 1 < columns
					&& k + c >= 0) {
				if (board[r + i][c + k] == 1
						&& board[r + i - 1][c + k + 1] == 1) {
					count++;
				} else
					count = 0;
				if (count == connect - 1)
					return true;
			}
		}
		return false;
	}

	public void print() {
		for (int i = 0; i < rows; i++) {
			for (int k = 0; k < columns; k++) {
				System.out.print(board[i][k]);
			}
			System.out.println();
		}
	}

	public boolean checkCompWin(int r, int c) {
		int count = 0;
		// Check Horizontal for Win
		for (int i = 0; i < columns - 1; i++) {
			if (board[r][i] == 2 && board[r][i + 1] == 2) {
				count++;
			} else
				count = 0;
			if (count == connect - 1)
				return true;
		}

		// Check verticle for Win
		for (int i = 0; i < rows - 1; i++) {
			if (board[i][c] == 2 && board[i + 1][c] == 2) {
				count++;
			} else
				count = 0;
			if (count == connect - 1)
				return true;
		}

		// Check right to bottom diagonal.
		count = 0;
		for (int i = -connect + 1; i < connect; i++) {
			if (r + i >= 0 && r + i + 1 < rows && c + i >= 0
					&& c + i + 1 < columns) {
				if (board[r + i][c + i] == 2
						&& board[r + i + 1][c + i + 1] == 2) {
					count++;
				} else
					count = 0;
				if (count == connect - 1)
					return true;
			}
		}

		// Check right to top diagonal
		count = 0;
		int i;
		int k;
		for (i = connect - 1, k = -connect + 1; i > -connect && k < connect; i--, k++) {
			if (r + i - 1 >= 0 && r + i < rows && k + c + 1 < columns
					&& k + c >= 0) {
				if (board[r + i][c + k] == 2
						&& board[r + i - 1][c + k + 1] == 2) {
					count++;
				} else
					count = 0;
				if (count == connect - 1)
					return true;
			}
		}
		return false;

	}
}
