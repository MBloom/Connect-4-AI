import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
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

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
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
	
	private JRadioButton random = new JRadioButton("Random" , true);
	private JLabel randomLabel = new JLabel("Random");
	private JRadioButton minmax;
	private JLabel minmaxLabel = new JLabel("MinMax");
	private ButtonGroup algorithmsGroup;
	/**
	 * @wbp.nonvisual location=902,349
	 */

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
					connectLabel.setBounds(14, 201, 133, 42);
					connectLabel.setFont(new java.awt.Font("Segoe UI", 0, 12));
				}
				{
					startButton = new JButton();
					SettingsPanel.add(startButton);
					startButton.setText("Start Game");
					startButton.setBounds(7, 518, 112, 63);
					// startButton.addActionListener(this);

					startButton.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {
							int c = Integer.parseInt(columns.getText());
							int r = Integer.parseInt(rows.getText());

							getContentPane().remove(gameGrid);
							getContentPane().remove(dropButtonsPanel);

							gameGrid = new JPanel();
							getContentPane().add(gameGrid);
							GridLayout gameGridLayout = new GridLayout(r, c);
							gameGridLayout.setHgap(5);
							gameGridLayout.setVgap(5);
							gameGrid.setLayout(gameGridLayout);
							gameGrid.setBounds(0, 42, 798, 567);
							gameGrid.setBorder(new LineBorder(
									new java.awt.Color(0, 0, 0), 3, true));
							{
								createGrid();
								board = new GameBoard(r, c, Integer.parseInt(numConnect.getText()));
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
								createButtons();
							}
						}

					});

				}
				{
					restartButton = new JButton();
					SettingsPanel.add(restartButton);
					restartButton.setText("Restart Game");
					restartButton.setBounds(119, 518, 119, 63);
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
					numConnect.setBounds(147, 211, 21, 21);
				}
				{
					random.setBounds(100, 258, 20, 20);
					SettingsPanel.add(random);
					SettingsPanel.add(randomLabel);
					randomLabel.setBounds(14, 250, 105, 35);
					randomLabel.setFont(new java.awt.Font("Segoe UI", 0, 14));

				}
				{
					minmax = new JRadioButton("MinMax");
					SettingsPanel.add(minmax);
					SettingsPanel.add(minmaxLabel);
					minmaxLabel.setFont(new java.awt.Font("Segoe UI", 0, 14));
					minmaxLabel.setBounds(14, 282, 105, 35);
					minmax.setBounds(100, 290, 20, 20);
				}
				{
					algorithmsGroup = new ButtonGroup();
					algorithmsGroup.add(random);
					algorithmsGroup.add(minmax);
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
					board = new GameBoard(r, c, Integer.parseInt(numConnect.getText()));

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
					createButtons();
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
		System.out.println(dropLoc);
		int rowLoc = board.dropUser(dropLoc);
		if (rowLoc != -1) {
			grid[rowLoc][dropLoc].setBackground(new java.awt.Color(255, 100,
					100));
			if(board.checkUserWin(rowLoc, dropLoc)) {
				System.out.print("You Win!");
			}
			if(random.isSelected()) {
//				int compCol = board.randomDrop();
//				int compRow = board.dropComp(compCol);
//				grid[compRow][compCol].setBackground(new java.awt.Color(0, 0,
//						0));
				int compCol = board.minimax(6);
				int compRow = board.dropComp(compCol);
				grid[compRow][compCol].setBackground(new java.awt.Color(0, 0,
						0));
			}
			if(minmax.isSelected()) {
				int compCol = board.randomWithDefenseDrop();
				int compRow = board.dropComp(compCol);
				grid[compRow][compCol].setBackground(new java.awt.Color(0, 0,
						0));
			}
//			int compColLoc = board.dropComp(1);
//			int compRowLoc = board.dropCompPiece(compColLoc);
//			grid[compRowLoc][compColLoc].setBackground(new java.awt.Color(0, 0,
//					0));
//			if(board.checkCompWin(compRowLoc, compColLoc)) {
//				System.out.print("Computer Wins!");
//			}
		}
		board.print();
		
		// grid[r-1][dropLoc].setBackground(new java.awt.Color(255,100,100));

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

	public void createButtons() {
		int c = Integer.parseInt(columns.getText());
		buttons = new JButton[c];
		for (int i = 0; i < c; i++) {
			buttons[i] = new JButton();
			dropButtonsPanel.add(buttons[i]);
			buttons[i].setText("Drop " + (i + 1));
			buttons[i].setActionCommand(Integer.toString(i));
			buttons[i].addActionListener(this);
		}
	}
}
