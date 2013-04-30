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
		while(true){
			int temp = generator.nextInt(columns);
			if(!isFull(temp)) {
				return temp;
			}
		}
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
			if(j >= columns) break;
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
			if(j >= columns) break;
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
	
	public int other(int player){
		if(player == 1)
			return 2;
		else if(player == 2)
			return 1;
		return -1;
	}
	
	public boolean isWin(int player) {
		for(int row = 0; row < rows; row++) {
			int count = 0;
			for(int col = 0; col < columns-1; col++) {
				if(board[row][col] == player && board[row][col+1] == player)
					count++;
				else
					count = 0;
				if(count == connect-1) {
					return true;
				}
			}
		}
		for(int col = 0; col < columns; col++) {
			int count = 0;
			for(int row = 0; row < rows-1; row++) {
				if(board[row][col] == player && board[row+1][col] == player)
					count++;
				else
					count = 0;
				if(count == connect-1) {
					return true;
				}
			}
		}
		for(int r = 0; r < rows; r++) {
			int row;
			int col;
			int count = 0;
			for(row = r, col = 0; row > 0 && col < columns-1; row--, col++) {
				if(board[row][col] == player && board[row-1][col+1] == player)
					count++;
				else
					count = 0;
				if(count == connect-1)
					return true;
			}
		}
		for(int c = 0; c < columns; c++) {
			int row;
			int col;
			int count = 0;
			for(col = c, row = rows-1; row > 0 && col < columns - 1; row--, col++) {
				if(board[row][col] == player && board[row-1][col+1] == player)
					count++;
				else
					count = 0;
				if(count == connect-1)
					return true;
			}
		}
		for(int c = 0; c < columns; c++) {
			int row;
			int col;
			int count = 0;
			for(col = c, row = rows-1; row > 0 && col > 0; row--, col--) {
				if(board[row][col] == player && board[row-1][col-1] == player)
					count++;
				else
					count = 0;
				if(count == connect-1)
					return true;
			}
		}
		for(int r = 0; r < rows; r++) {
			int row;
			int col;
			int count = 0;
			for(row = r, col = columns-1; row > 0 && col > 0; row--, col--) {
				if(board[row][col] == player && board[row-1][col-1] == player)
					count++;
				else
					count = 0;
				if(count == connect-1)
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
}
