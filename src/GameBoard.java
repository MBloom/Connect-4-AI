import java.util.Random;

public class GameBoard {
	private int rows;
	private int columns;
	private int[][] board;
	private int connect;
	private int max = 100;
	private int min = -100;

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

	public int dropComp(int col) {
		for (int k = rows - 1; k >= 0; k--) {
			if (board[k][col] == 0) {
				board[k][col] = 2;
				return k;
			}
		}
		return -1;
	}

	public int randomDrop() {
		for (int i = 0; i < columns; i++) {
			int rowLoc = dropComp(i);
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
			int rowLoc = dropComp(i);
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

	/************************* MINIMAX CODE ************************************/
	public int minimax(int diff) {
		if (diff == 0)
			return -1;
		int bestDecision = -1;
		int bestCol = 0;
		for (int j = 0; j < columns; j++) {
			if (!isFull(j)) {
				int lastRow = dropComp(j);
				int decision = userRecursion(diff - 1, bestDecision, lastRow, j);
				if (decision > bestDecision) {
					bestDecision = decision;
					bestCol = j;
				}
				undoMove(j);
			}
		}
		return bestCol;
	}

	public int userRecursion(int diff, int prevMin, int lastRowLoc,
			int lastColLoc) {
		if (diff == 0)
			return heuristic(2, lastRowLoc, lastColLoc);
		int bestCompDecision = -1;
		for (int j = 0; j < columns; j++) {
			if (!isFull(j)) {
				int lastRow = dropUser(j);
				if (checkUserWin(lastRow, j)) {
					undoMove(j);
					return -1;
				}
				int decision = compRecursion(diff - 1, bestCompDecision,
						lastRow, j);
				if (bestCompDecision < decision) {
					bestCompDecision = decision;
				}
				undoMove(j);
			}
		}
		return bestCompDecision;
	}

	public int compRecursion(int diff, int prevMax, int lastRowLoc,
			int lastColLoc) {
		if (diff == 0)
			return heuristic(1, lastRowLoc, lastColLoc);
		int bestCompDecision = -1;
		for (int j = 0; j < columns; j++) {
			if (!isFull(j)) {
				int rowLoc = dropComp(j);
				if (checkCompWin(rowLoc, j)) {
					undoMove(j);
					return 1;
				}
				int decision = userRecursion(diff - 1, bestCompDecision,
						rowLoc, j);
				if (decision > bestCompDecision) {
					bestCompDecision = decision;
				}
				undoMove(j);
			}
		}
		return bestCompDecision;
	}

	public void undoMove(int j) {
		for (int i = 0; i < rows; i++) {
			if (board[i][j] == 1 || board[i][j] == 2) {
				board[i][j] = 0;
				break;
			}
		}
	}

	public int heuristic(int player, int lastRowLoc, int lastColLoc) {
		if (player == 1) {
			if (checkUserWin(lastRowLoc, lastColLoc)) {
				return -1;
			}
		}
		if (player == 2) {
			if (checkCompWin(lastRowLoc, lastColLoc)) {
				return 1;
			}
		}
		return 0;
	}

	public boolean isFull(int c) {
		if (board[0][c] != 0)
			return true;
		else
			return false;
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
