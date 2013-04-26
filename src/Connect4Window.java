import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingConstants;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.SwingUtilities;

public class Connect4Window extends javax.swing.JFrame implements
		ActionListener {
	private static final LayoutManager SettingsPanelLayout = null;
	private JPanel gameGrid;
	private JLabel sizeLabel;
	private JTextField rows;
	private JTextField columns;
	private JTextField numConnect;
	private JLabel connectX;
	private JButton restartButton;
	private JButton startButton;
	private JLabel Title;
	private JPanel SettingsPanel;
	private JPanel dropButtonsPanel;
	private JButton[] buttons;
	private JPanel[][] grid;
	private GameBoard board;
	private JLabel connectLabel;

	private JRadioButton random = new JRadioButton("", true);
	private JLabel randomLabel = new JLabel("Random");
	private JRadioButton minmax;
	private JLabel minmaxLabel = new JLabel("MinMax");
	private ButtonGroup algorithmsGroup;
	private JRadioButton randomWithDefense = new JRadioButton("");
	private JLabel ranWithDefLabel = new JLabel("Random w/ Defense");

	private JRadioButton normGrid = new JRadioButton("Normal Grid", true);
	private JLabel normGridLabel = new JLabel("Normal Grid");
	private JRadioButton customGrid = new JRadioButton("Custom Grid");
	private JLabel customGridLabel = new JLabel("Custom Grid");
	private ButtonGroup typeOfGridGroup;
	private JTextField custom;

	private JOptionPane winMessage = new JOptionPane();

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Connect4Window inst = new Connect4Window();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public Connect4Window() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setFocusTraversalPolicyProvider(true);
			getContentPane().setLayout(null);
			{
				SettingsPanel = new JPanel();
				getContentPane().add(SettingsPanel);
				SettingsPanel.setLayout(SettingsPanelLayout);
				SettingsPanel.setBounds(805, 14, 245, 595);
				SettingsPanel.setBorder(new LineBorder(new java.awt.Color(0, 0,
						0), 3, true));
				SettingsPanel.setLayout(null);
				{
					Title = new JLabel();
					SettingsPanel.add(Title);
					Title.setText("Connect 4");
					Title.setBounds(63, 14, 105, 35);
					Title.setFont(new java.awt.Font("Segoe UI", 0, 22));
				}
				{
					sizeLabel = new JLabel();
					SettingsPanel.add(sizeLabel);
					sizeLabel.setText("Number of Columns:");
					sizeLabel.setBounds(14, 105, 133, 42);
					sizeLabel.setFont(new java.awt.Font("Segoe UI", 0, 12));
				}
				{
					connectLabel = new JLabel();
					SettingsPanel.add(connectLabel);
					connectLabel.setText("Number to Connect:");
					connectLabel.setBounds(14, 440, 133, 42);
					connectLabel.setFont(new java.awt.Font("Segoe UI", 0, 14));
				}
				{
					startButton = new JButton();
					SettingsPanel.add(startButton);
					startButton.setText("Start Game");
					startButton.setBounds(7, 518, 230, 63);
					// startButton.addActionListener(this);

					startButton.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							getContentPane().remove(gameGrid);
							getContentPane().remove(dropButtonsPanel);

							int c = 0;
							int r = 0;
							gameGrid = new JPanel();
							getContentPane().add(gameGrid);

							if (normGrid.isSelected()) {
								c = Integer.parseInt(columns.getText());
								r = Integer.parseInt(rows.getText());
								GridLayout gameGridLayout = new GridLayout(r, c);
								gameGridLayout.setHgap(5);
								gameGridLayout.setVgap(5);
								gameGrid.setLayout(gameGridLayout);
								gameGrid.setBounds(0, 42, 798, 567);
								gameGrid.setBorder(new LineBorder(
										new java.awt.Color(0, 0, 0), 3, true));
								{
									createGrid();
									board = new GameBoard(r, c, Integer
											.parseInt(numConnect.getText()));
								}
							} else {
								int[][] customGridTemplate = loadGrid();
								if (customGridTemplate != null) {
									r = customGridTemplate.length;
									c = customGridTemplate[0].length;
									GridLayout gameGridLayout = new GridLayout(
											r, c);
									gameGridLayout.setHgap(5);
									gameGridLayout.setVgap(5);
									gameGrid.setLayout(gameGridLayout);
									gameGrid.setBounds(0, 42, 798, 567);
									gameGrid.setBorder(new LineBorder(
											new java.awt.Color(0, 0, 0), 3,
											true));
									{
										createCustomGrid(r, c,
												customGridTemplate);
										board = new GameBoard(r, c,
												Integer.parseInt(numConnect
														.getText()),
												customGridTemplate);
									}
								}
							}
							dropButtonsPanel = new JPanel();
							GridLayout dropButtonsPanelLayout = new GridLayout(
									1, c);
							dropButtonsPanelLayout.setHgap(5);
							dropButtonsPanelLayout.setVgap(5);
							dropButtonsPanelLayout.setColumns(1);
							getContentPane().add(dropButtonsPanel);
							dropButtonsPanel.setLayout(dropButtonsPanelLayout);
							dropButtonsPanel.setBounds(3, 6, 794, 30);
							{
								createButtons(c);
							}
						}

					});

				}
				{
					connectX = new JLabel();
					SettingsPanel.add(connectX);
					connectX.setText("Number of Rows:");
					connectX.setBounds(14, 161, 154, 21);
					connectX.setFont(new java.awt.Font("Segoe UI", 0, 14));
				}
				{
					columns = new JTextField();
					SettingsPanel.add(columns);
					columns.setText("7");
					columns.setBounds(147, 112, 21, 21);
				}
				{
					rows = new JTextField();
					SettingsPanel.add(rows);
					rows.setText("6");
					rows.setBounds(147, 161, 21, 21);
				}
				{
					numConnect = new JTextField();
					SettingsPanel.add(numConnect);
					numConnect.setText("4");
					numConnect.setBounds(150, 450, 21, 21);
				}
				{
					SettingsPanel.add(random);
					SettingsPanel.add(randomLabel);
					randomLabel.setFont(new java.awt.Font("Segoe UI", 0, 14));
					randomLabel.setBounds(14, 300, 105, 35);
					random.setBounds(150, 308, 25, 25);

				}

				{
					SettingsPanel.add(randomWithDefense);
					SettingsPanel.add(ranWithDefLabel);
					ranWithDefLabel
							.setFont(new java.awt.Font("Segoe UI", 0, 14));
					ranWithDefLabel.setBounds(14, 332, 155, 35);
					randomWithDefense.setBounds(150, 340, 25, 25);
				}

				{
					minmax = new JRadioButton("");
					SettingsPanel.add(minmax);
					SettingsPanel.add(minmaxLabel);
					minmaxLabel.setFont(new java.awt.Font("Segoe UI", 0, 14));
					minmaxLabel.setBounds(14, 364, 105, 35);
					minmax.setBounds(150, 372, 25, 25);
				}

				{
					algorithmsGroup = new ButtonGroup();
					algorithmsGroup.add(random);
					algorithmsGroup.add(minmax);
					algorithmsGroup.add(randomWithDefense);
				}
				{
					custom = new JTextField();
					SettingsPanel.add(normGrid);
					SettingsPanel.add(customGrid);
					SettingsPanel.add(custom);

					normGrid.setBounds(10, 83, 180, 25);
					normGrid.setHorizontalTextPosition(SwingConstants.LEFT);

					customGrid.setBounds(10, 218, 180, 25);
					customGrid.setHorizontalTextPosition(SwingConstants.LEFT);

					custom.setText("File Name");
					custom.setBounds(50, 250, 150, 21);

				}
				{
					typeOfGridGroup = new ButtonGroup();
					typeOfGridGroup.add(customGrid);
					typeOfGridGroup.add(normGrid);
				}
			}
			{
				gameGrid = new JPanel();
				getContentPane().add(gameGrid);
				int c = Integer.parseInt(columns.getText());
				int r = Integer.parseInt(rows.getText());
				GridLayout gameGridLayout = new GridLayout(r, c);
				gameGridLayout.setHgap(5);
				gameGridLayout.setVgap(5);
				gameGrid.setLayout(gameGridLayout);
				gameGrid.setBounds(0, 42, 798, 567);
				gameGrid.setBorder(new LineBorder(new java.awt.Color(0, 0, 0),
						3, true));
				{
					createGrid();
					board = new GameBoard(r, c, Integer.parseInt(numConnect
							.getText()));

				}
			}
			{
				int c = Integer.parseInt(columns.getText());
				int r = Integer.parseInt(rows.getText());
				dropButtonsPanel = new JPanel();
				GridLayout dropButtonsPanelLayout = new GridLayout(1, c);
				dropButtonsPanelLayout.setHgap(5);
				dropButtonsPanelLayout.setVgap(5);
				dropButtonsPanelLayout.setColumns(1);
				getContentPane().add(dropButtonsPanel);
				dropButtonsPanel.setLayout(dropButtonsPanelLayout);
				dropButtonsPanel.setBounds(3, 6, 794, 30);
				{
					createButtons(c);
				}
			}
			pack();
			this.setSize(1073, 654);
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int r = Integer.parseInt(rows.getText());
		String loc = e.getActionCommand();
		int dropLoc = Integer.parseInt(loc);
		// System.out.println(dropLoc);
		int rowLoc = board.drop(dropLoc, 1);
		if (rowLoc != -1) {
			grid[rowLoc][dropLoc].setBackground(new java.awt.Color(0, 0, 255));
			if (board.isWin(1)) {
				String[] choices = { "Restart", "Quit" };
				int response = JOptionPane.showOptionDialog(null, "You Win!",
						"Game Over", JOptionPane.YES_NO_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, choices, "Restart");
				// ... Use a switch statement to check which button was clicked.
				switch (response) {
				case 0:
					startButton.doClick();
					break;
				case 1:
					System.exit(0);
					break;
				default:
					// ... If we get here, something is wrong. Defensive
					// programming.
					JOptionPane.showMessageDialog(null, "Unexpected response "
							+ response);
				}
				return;
			}
			if (random.isSelected()) {
				int compCol = board.randomDrop();
				int compRow = board.drop(compCol, 2);
				grid[compRow][compCol].setBackground(new java.awt.Color(255,
						69, 0));
			}
			if (randomWithDefense.isSelected()) {
				int compCol = board.randomWithDefenseDrop();
				int compRow = board.drop(compCol, 2);
				grid[compRow][compCol].setBackground(new java.awt.Color(255,
						69, 0));
			}
			if (minmax.isSelected()) {
				int compCol = board.minimax(3, 2);
				int compRow = board.drop(compCol, 2);
				grid[compRow][compCol].setBackground(new java.awt.Color(255,
						69, 0));
			}
			if (board.isWin(2)) {
				String[] choices = { "Restart", "Quit" };
				int response = JOptionPane.showOptionDialog(null,
						"Computer Wins!", "Game Over",
						JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
						null, choices, "Restart");
				// ... Use a switch statement to check which button was clicked.
				switch (response) {
				case 0:
					startButton.doClick();
					break;
				case 1:
					System.exit(0);
					break;
				default:
					// ... If we get here, something is wrong. Defensive
					// programming.
					JOptionPane.showMessageDialog(null, "Unexpected response "
							+ response);
				}
				return;
			}
		}
	}

	public void createGrid() {
		int c = Integer.parseInt(columns.getText());
		int r = Integer.parseInt(rows.getText());
		grid = new JPanel[r][c];

		for (int i = 0; i < r; i++) {
			for (int k = 0; k < c; k++) {
				grid[i][k] = new JPanel();
				gameGrid.add(grid[i][k]);
				grid[i][k].setBorder(new LineBorder(
						new java.awt.Color(0, 0, 0), 2, false));
			}
		}
	}

	public void createCustomGrid(int r, int c, int[][] template) {
		grid = new JPanel[r][c];
		for (int i = 0; i < r; i++) {
			for (int k = 0; k < c; k++) {
				grid[i][k] = new JPanel();
				gameGrid.add(grid[i][k]);
				grid[i][k].setBorder(new LineBorder(
						new java.awt.Color(0, 0, 0), 2, false));
				if (template[i][k] == 3) {
					grid[i][k].setBackground(new java.awt.Color(0, 0, 0));
				}
			}
		}
	}

	public void createButtons(int c) {
		buttons = new JButton[c];
		for (int i = 0; i < c; i++) {
			buttons[i] = new JButton();
			dropButtonsPanel.add(buttons[i]);
			buttons[i].setText("Drop " + (i + 1));
			buttons[i].setActionCommand(Integer.toString(i));
			buttons[i].addActionListener(this);
		}
	}

	public int[][] loadGrid() {
		BufferedReader br = null;
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(custom.getText()));
			sCurrentLine = br.readLine();
			int loc = sCurrentLine.indexOf('x');
			int numRow = Integer.parseInt(sCurrentLine.substring(0, loc));
			int numCol = Integer.parseInt(sCurrentLine.substring(loc + 1,
					sCurrentLine.length()));
			int[][] tempGrid = new int[numRow][numCol];
			int count = 0;
			while ((sCurrentLine = br.readLine()) != null) {
				for (int i = 0; i < sCurrentLine.length(); i++) {
					tempGrid[count][i] = Integer.parseInt(Character
							.toString(sCurrentLine.charAt(i)));
				}
				count++;
			}
			return tempGrid;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error: File Not Found",
					"ERROR", JOptionPane.ERROR_MESSAGE);
			return null;
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}