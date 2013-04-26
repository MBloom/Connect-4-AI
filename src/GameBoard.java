import java.util.Random;

public class GameBoard {
	private int rows;
	private int columns;
	private int[][] board;
	private int connect;
	private int maxVal = Integer.MAX_VALUE;
	private int minVal = -(Integer.MAX_VALUE);

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

	public int drop(int col, int player) {
		for (int k = rows - 1; k >= 0; k--) {
			if (board[k][col] == 0) {
				board[k][col] = player;
				return k;
			}
		}
		return -1;
	}

	public int randomDrop() {
		for (int i = 0; i < columns; i++) {
			int rowLoc = drop(i, 2);
			if (rowLoc != -1) {
				board[rowLoc][i] = 2;
				if (isWin(2)) {
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
			int rowLoc = drop(i, 2);
			if (rowLoc != -1) {
				board[rowLoc][i] = 2;
				if (isWin(2)) {
					board[rowLoc][i] = 0;
					return i;
				}
				board[rowLoc][i] = 0;
			}
		}
		for (int i = 0; i < columns; i++) {
			int rowLoc = drop(i, 1);
			if (rowLoc != -1) {
				board[rowLoc][i] = 1;
				if (isWin(2)) {
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
	public int minimax(int diff, int player) {
		if (diff == 0)
			return -1;
		int bestDecision = minVal;
		int bestCol = 0; 
		int curValue = 0;
		for (int j = 0; j < columns; j++) {
			if (!isFull(j)) {
				int lastRow = drop(j, player);
				if(isWin(player)){
					undoMove(j);
					return j;
				}
				curValue = eval(player, diff, minVal, -bestDecision);
				if (curValue > bestDecision) {
					bestDecision = curValue;
					bestCol = j;
				}
				undoMove(j);
			}
		}
		return bestCol;
	}

	public int eval(int player, int diff, int alpha, int beta){
		if(isWin(player))
			return maxVal - diff;
		else if(isWin(other(player)))
			return -(maxVal-diff);
		else if(diff == 0)
			return heuristic(player);
		else {
			int best = minVal;
			int maxAB = alpha;
			for (int j = 0; j < columns; j++){
				if (!isFull(j)){
					int lastRow = drop(j, other(player));
					int curValue = eval(other(player), diff - 1, -beta, -maxAB);
					if (curValue > best){
						best = curValue;
						if(best > maxAB)
							maxAB = best;
					}
					undoMove(j);
					if(best > beta)
						break;
				}
			}
			return -best;
		}
	}
	/*
	//computer just dropped a piece, now evaluating user's move
	public int minRecursion(int diff, int alpha, int beta) {
		if (diff == 0)
			return heuristic(2);
		int bestDecision = minVal;
		int maxPos = alpha;
		for (int j = 0; j < columns; j++) {
			if (!isFull(j)) {
				int lastRow = drop(j, 1);
				if(isWin(1))
					return -(maxVal - diff);
				int decision = maxRecursion(diff - 1, -beta, -maxPos);
				if (bestDecision < decision) {
					bestDecision = decision;
					if(bestDecision > maxPos)
						maxPos = bestDecision;
				}
				undoMove(j);
				if(bestDecision > beta)
					break;
			}
		}
		return -bestDecision;
	}

	//user just dropped a piece, now evaluating computer's move
	public int maxRecursion(int diff, int alpha, int beta) {
		if (diff == 0)
			return heuristic(1);
		int bestDecision = minVal;
		int maxPos = alpha;
		for (int j = 0; j < columns; j++) {
			if (!isFull(j)) {
				int lastRow = drop(j, 2);
				if(isWin(2))
					return -(maxVal - diff);
				int decision = minRecursion(diff - 1, -beta, -maxPos);
				if (bestDecision < decision) {
					bestDecision = decision;
					if(bestDecision > maxPos)
						maxPos = bestDecision;
				}
				undoMove(j);
				if(bestDecision > beta)
					break;
			}
		}
		return -bestDecision;
	}
*/
	public void undoMove(int j) {
		for (int i = 0; i < rows; i++) {
			if (board[i][j] == 1 || board[i][j] == 2) {
				board[i][j] = 0;
				break;
			}
		}
	}

	public int heuristic(int player) {
		int value = 0;
		if(isWin(player))
				return Integer.MAX_VALUE;
		else{
			int threeStreak = streakCount(player, 3);
			int twoStreak = streakCount(player, 2);
			value = threeStreak*10000 + twoStreak*10;
		}
		return value;
	}
	
	public int streakCount(int player, int numInRow){
		int count = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				if(board[i][j] == player){
					count += verticalStreak(player, numInRow, i, j);
					count += horizontalStreak(player, numInRow, i, j);
					count += diagonalStreak(player, numInRow, i, j);
				}
			}
		}
		return count;
	}
	
	public int verticalStreak(int player, int numInRow, int row, int col){
		int streakCount = 0;
		for(int i = row; i < rows; i++){
			if(board[i][col] == player)
				streakCount++;
			else break;
		}
		if(streakCount >= numInRow)
			return 1;
		else return 0;
	}
	
	public int horizontalStreak(int player, int numInRow, int row, int col){
		int streakCount = 0;
		for(int j = col; j < columns; j++){
			if(board[row][j] == player)
				streakCount++;
			else break;
		}
		if(streakCount >= numInRow)
			return 1;
		else return 0;
	}
	
	public int diagonalStreak(int player, int numInRow, int row, int col){
		int total = 0;
		int streakCount = 0;
		//check first direction diagonals
		int j = col;
		
		for(int i = row; i < rows; i++){
			if(j > rows) break;
			else if(board[i][j] == player)
				streakCount++;
			else break;
			j++;
		}
		
		if(streakCount >= numInRow)
			total++;
		
		//reset streakCount and check the other direction
		streakCount = 0;
		j = col;
		for(int i = row; i >= 0; i--){
			if(j > rows) break;
			else if(board[i][j] == player)
				streakCount++;
			else break;
			j++;
		}
			
		if(streakCount >= numInRow)
			total++;
		
		return total;
	}
	
	public boolean isFull(int c) {
		if (board[0][c] != 0)
			return true;
		else
			return false;
	}

	/**************************/
	public boolean isWin(int player) {
		boolean win = false;
			//check for win horizontally
			for (int row=0; row<rows; row++) {
			    for (int col=0; col<columns-3; col++) {
					if (board[row][col] == player &&	
						board[row][col] == board[row][col+1] &&
					    board[row][col] == board[row][col+2] &&
					    board[row][col] == board[row][col+3] &&
					    board[row][col] != 0) {
					    win = true;
					}
				}
			}
			//check for win vertically
			for (int row=0; row<rows-3; row++) {
			    for (int col=0; col<columns; col++) {
					if (board[row][col] == player &&
						board[row][col] == board[row+1][col] &&
					    board[row][col] == board[row+2][col] &&
					    board[row][col] == board[row+3][col] &&
					    board[row][col] != 0) {
					    win = true;
					}
			    }
			}
			//check for win diagonally (upper left to lower right)
			for (int row=0; row<rows-3; row++) { 
			    for (int col=0; col<columns-3; col++) {
					if (board[row][col] == player &&
						board[row][col] == board[row+1][col+1] &&
					    board[row][col] == board[row+2][col+2] &&
					    board[row][col] == board[row+3][col+3] &&
					    board[row][col] != 0) {
					    win = true;
					}
			    }
			}
			//check for win diagonally (lower left to upper right)
			for (int row=3; row<rows; row++) { 
			    for (int col=0; col<columns-3; col++) { 
					if (board[row][col] == player &&
						board[row][col] == board[row-1][col+1] &&
					    board[row][col] == board[row-2][col+2] &&
					    board[row][col] == board[row-3][col+3] &&
					    board[row][col] != 0) {
					    win = true;
					}
			    }
			}

			return win;
				
	    }//end checkWinner
	
	public int other(int player){
		if(player == 1)
			return 2;
		else if(player == 2)
			return 1;
		return -1;
	}
//	public boolean checkUserWin(int r, int c) {
//
//		int count = 0;
//		// Check Horizontal for Win
//		for (int i = 0; i < columns - 1; i++) {
//			if (board[r][i] == 1 && board[r][i + 1] == 1) {
//				count++;
//			} else
//				count = 0;
//			if (count == connect - 1)
//				return true;
//		}
//
//		// Check verticle for Win
//		for (int i = 0; i < rows - 1; i++) {
//			if (board[i][c] == 1 && board[i + 1][c] == 1) {
//				count++;
//			} else
//				count = 0;
//			if (count == connect - 1)
//				return true;
//		}
//
//		// Check right to bottom diagonal.
//		count = 0;
//		for (int i = -connect + 1; i < connect; i++) {
//			if (r + i >= 0 && r + i + 1 < rows && c + i >= 0
//					&& c + i + 1 < columns) {
//				if (board[r + i][c + i] == 1
//						&& board[r + i + 1][c + i + 1] == 1) {
//					count++;
//				} else
//					count = 0;
//				if (count == connect - 1)
//					return true;
//			}
//		}
//
//		// Check right to top diagonal
//		count = 0;
//		int i;
//		int k;
//		for (i = connect - 1, k = -connect + 1; i > -connect && k < connect; i--, k++) {
//			if (r + i - 1 >= 0 && r + i < rows && k + c + 1 < columns
//					&& k + c >= 0) {
//				if (board[r + i][c + k] == 1
//						&& board[r + i - 1][c + k + 1] == 1) {
//					count++;
//				} else
//					count = 0;
//				if (count == connect - 1)
//					return true;
//			}
//		}
//		return false;
//	}

	public void print() {
		for (int i = 0; i < rows; i++) {
			for (int k = 0; k < columns; k++) {
				System.out.print(board[i][k]);
			}
			System.out.println();
		}
	}

//	public boolean checkCompWin(int r, int c) {
//		int count = 0;
//		// Check Horizontal for Win
//		for (int i = 0; i < columns - 1; i++) {
//			if (board[r][i] == 2 && board[r][i + 1] == 2) {
//				count++;
//			} else
//				count = 0;
//			if (count == connect - 1)
//				return true;
//		}
//
//		// Check verticle for Win
//		for (int i = 0; i < rows - 1; i++) {
//			if (board[i][c] == 2 && board[i + 1][c] == 2) {
//				count++;
//			} else
//				count = 0;
//			if (count == connect - 1)
//				return true;
//		}
//
//		// Check right to bottom diagonal.
//		count = 0;
//		for (int i = -connect + 1; i < connect; i++) {
//			if (r + i >= 0 && r + i + 1 < rows && c + i >= 0
//					&& c + i + 1 < columns) {
//				if (board[r + i][c + i] == 2
//						&& board[r + i + 1][c + i + 1] == 2) {
//					count++;
//				} else
//					count = 0;
//				if (count == connect - 1)
//					return true;
//			}
//		}
//
//		// Check right to top diagonal
//		count = 0;
//		int i;
//		int k;
//		for (i = connect - 1, k = -connect + 1; i > -connect && k < connect; i--, k++) {
//			if (r + i - 1 >= 0 && r + i < rows && k + c + 1 < columns
//					&& k + c >= 0) {
//				if (board[r + i][c + k] == 2
//						&& board[r + i - 1][c + k + 1] == 2) {
//					count++;
//				} else
//					count = 0;
//				if (count == connect - 1)
//					return true;
//			}
//		}
//		return false;
//
//	}
}
