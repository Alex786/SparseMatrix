public class SparseMatrix implements BooleanMatrix {
    // Since the length of each row i in the array may be longer than the number of true values in row i of the matrix, the unused array elements are all at the end of each row and filled with this sentinel value. A value of -1 is used for the sentinel since matrix indices are non-negative.
    private static final int SENTINEL = -1;
    
    // The array used for the sparse matrix representation.
    private int[][] array;

    // The number of rows in the Boolean Matrix.
    private int numRows;

    // The number of columns in the Boolean Matrix.
    private int numCols;

    public SparseMatrix() {
		// Creates a 1 x 1 matrix with a single false value. This constructor calls the reset() method.
        reset();
    }

    public void reset() {
		// Resets the Boolean matrix to a 1 x 1 matrix that contains a single false value.
		numRows = 1;
		numCols = 1;
		array = new int[1][];
		array[0] = new int[1];
		array[0][0] = SENTINEL;
    }

    public int getNumberRows() {
		// Returns the number of rows.
        return numRows;
    }

    public int getNumberCols() {
		// Returns the number of columns.
        return numCols;
    }

    public void set(int row, int col) throws IndexOutOfBoundsException {
		// Sets the element at the specified position to true. Adds new rows and columns if needed, filled with false. This method should call the BooleanMatrix.put(int row, int col, boolean value) method.
		put(row, col, true);
    }

	public void clear(int row, int col) throws IndexOutOfBoundsException {
		// Sets the element at the specified position to false. Adds new rows and columns if needed, filled with false. This method should call the BooleanMatrix.put(int row, int col, boolean value) method.
		put(row, col, false);
	}

	public void put(int row, int col, boolean value) throws IndexOutOfBoundsException {
		// Stores the element at the specified position to the specified value. Adds new rows and columns if needed, filled with false.
		if (row < 0 || col < 0) {
			throw new IndexOutOfBoundsException();
		}
		
		// Expand array if needed
		while (row >= array.length) {
			doubleNumArrayRows();
		}
		
		// Update actual dimensions
		numRows = Math.max(numRows, row + 1);
		numCols = Math.max(numCols, col + 1);
		
		// Initialize new rows if needed
		if (array[row] == null) {
			array[row] = new int[1];
			array[row][0] = SENTINEL;
		}
		
		if (value == true) {
			insert(row, col);
		} else {
			remove(row, col);
		}
	}

	private void doubleNumArrayRows() {
		// Doubles the number of rows in the matrix representing the sparse matrix.
		int[][] newArray = new int[array.length * 2][];
		System.arraycopy(array, 0, newArray, 0, array.length);
		array = newArray;
	}

	private void insert(int row, int value) {
		// Inserts the given value into the specified row, keeping row sorted. If value is already in row, this method returns without changing the row. If value is not in row, it is added to row in the correct position. This method assumes the row is either null or it is in sorted order.
		if (array[row] == null) {
			array[row] = new int[1];
			array[row][0] = value;
			return;
		}
		
		// Check if value already exists
		for (int i = 0; array[row][i] != SENTINEL && i < array[row].length; i++) {
			if (array[row][i] == value) return;
		}
		
		// Find insertion point and check if we need more space
		int i = 0;
		while (i < array[row].length && array[row][i] != SENTINEL && array[row][i] < value) {
			i++;
		}
		
		if (i >= array[row].length || array[row][array[row].length - 1] != SENTINEL) {
			doubleArrayRow(row);
		}
		
		// Shift elements to make room
		for (int j = array[row].length - 2; j >= i; j--) {
			array[row][j + 1] = array[row][j];
		}
		array[row][i] = value;
	}

	private void remove(int row, int value) {
		// Removes the given value from the specified row. If value is not in the row, this method returns without changing the row. If value is in the row, it is removed and the other values are shifted left to fill the hole created.
		if (array[row] == null) return;
    
		int i = 0;
		boolean found = false;
		while (i < array[row].length && array[row][i] != SENTINEL) {
			if (array[row][i] == value) {
				found = true;
				break;
			}
			i++;
		}
		
		if (found) {
			// Shift elements left
			while (i < array[row].length - 1 && array[row][i] != SENTINEL) {
				array[row][i] = array[row][i + 1];
				i++;
			}
			array[row][i] = SENTINEL;
			
			// Check if we can reduce array size
			if (i < array[row].length / 4) {
				halveArrayRow(row);
			}
		}
	}

	private void doubleArrayRow(int row) {
		// Doubles the length of the specified row of the sparse matrix array.
		int[] newRow = new int[array[row].length * 2];
		System.arraycopy(array[row], 0, newRow, 0, array[row].length);
		for (int i = array[row].length; i < newRow.length; i++) {
			newRow[i] = SENTINEL;
		}
		array[row] = newRow;
	}

	private void halveArrayRow(int row) {
		// Reduces by half the length of the specified row of the sparse matrix array.
		int[] newRow = new int[array[row].length / 2];
		System.arraycopy(array[row], 0, newRow, 0, newRow.length);
		array[row] = newRow;
	}

	public void toggle(int row, int col) {
		// Changes the element at the specified position. If the current value is true it is changed to false. If the current value is false it is changed to true.
		if (get(row, col) == true) {
			put(row, col, false);
		} else {
			put(row, col, true);
		}
	}

	public void setAll() {
		// Sets all elements to true. This method should call the BooleanMatrix.putAll(boolean value) method.
		putAll(true);
	}

	public void clearAll() {
		// Clears all elements to false. This method should call the BooleanMatrix.putAll(boolean value) method.
		putAll(false);
	}

	public void putAll(boolean  value) {
		// Sets all elements to the specified value.
		if (value) {
			for (int i = 0; i < numRows; i++) {
				array[i] = new int[numCols];
				for (int j = 0; j < numCols; j++) {
					array[i][j] = j;
				}
			}
		} else {
			reset();
		}
	}

	public boolean get(int row, int col) throws IndexOutOfBoundsException {
		// Returns the element at the specified position.
		if (row < 0 || col < 0) {
			throw new IndexOutOfBoundsException();
		}
		
		// Return false for positions beyond current matrix dimensions
		if (row >= array.length || col >= numCols) {
			return false;
		}
		
		// Return false if row is not initialized
		if (array[row] == null) {
			return false;
		}
		
		// Search for column index in the row
		for (int i = 0; i < array[row].length && array[row][i] != SENTINEL; i++) {
			if (array[row][i] == col) {
				return true;
			}
		}
		return false;
	}

	public int[][] getTruePositions() {
		// Returns the indices of the elements whose value is true. The indices are returned in a P x 2 array of integers, where P is the number of elements with a value of true. Each row in the returned array contains the row and column index of an element with a value of true. The returned array is sorted in increasing order by row and column. This method should call the BooleanMatrix.getPositions(boolean value) method.
		return getPositions(true);
	}

	public int[][] getFalsePositions() {
		// Returns the indices of the elements whose value is false. The indices are returned in a P x 2 array of integers, where P is the number of elements with a value of false. Each row in the returned array contains the row and column index of an element with a value of false. The returned array is sorted in increasing order by row and column. This method should call the BooleanMatrix.getPositions(boolean value) method.
		return getPositions(false);
	}

	public int[][] getPositions(boolean value) {
		// Returns the indices of the elements whose value matches the parameter. The indices are returned in a P x 2 array of integers, where P is the number of elements whose value matches the parameter. Each row in the returned array contains the row and column index of an element whose value matches the parameter. The returned array is sorted in increasing order by row and column. This method should call the BooleanMatrix.getPositions(boolean value) method.
		int count = getNumberValues(value);
		int[][] positions = new int[count][2];
		int posIndex = 0;
		
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				if (get(i, j) == value && posIndex < count) {
					positions[posIndex][0] = i;
					positions[posIndex][1] = j;
					posIndex++;
				}
			}
		}
		return positions;
	}

	public int getNumberTrueValues() {
		// Returns the number of elements whose value is true.
		return getNumberValues(true);
	}

	public int getNumberFalseValues() {
		// Returns the number of elements whose value is false.
		return getNumberValues(false);
	}

	public int getNumberValues(boolean value) {
		// Returns the number of elements with the specified value.
		int count = 0;
		if (value == true) {
			for (int i = 0; i < numRows; i++) {
				if (array[i] != null) {
					for (int j = 0; j < array[i].length && array[i][j] != SENTINEL; j++) {
						count++;
					}
				}
			}
		} else {
			count = numRows * numCols - getNumberValues(true);
		}
		return count;
	}

	public String toString() {
		// Returns a string showing: 1) the number of rows, 2) the number of columns, 3) the number of true values, and 4) the number of false values.
		return "Number of rows: " + getNumberRows() + "\nNumber of columns: " + getNumberCols() + "\nNumber of true values: " + getNumberTrueValues() + "\nNumber of false values: " + getNumberFalseValues();
	}
}
