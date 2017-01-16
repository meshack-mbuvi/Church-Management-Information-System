/**
 * 
 */
package databaseApp;

import java.awt.BorderLayout;

import java.awt.Choice;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

/**
 * This is a class for zones in a given parish
 * It contains methods to create new zones,edit ,
 * delete and show existing ones.
 * The methods are:
 * 1.insertZone()
 * 2.editZone()
 * 3.deleteZone()
 * 4.showZones()
 * 5.searchZone()
 * @author mbuvi
 *
 */
public class Zone extends VariablesDefinitions{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8274298826698761611L;
	private JDateChooser date; 
	private JTextArea comments ;
	private JPanel zones;
	JPanel panel;
	private Choice zoneName ;
	JScrollPane sp;
	
	public Zone(){
		date = new JDateChooser();
		comments = new JTextArea(5, 10);
		zones = new JPanel(new GridLayout(2, 2, 3, 3));
		panel=new JPanel();
		
		zoneName = new Choice();
		sp=new JScrollPane();
	}

	/******************************************************************************
	 * 
	 * Zone methods
	 * 
	 ******************************************************************************/
	// insert zone method
	void insertZone() {
		zones.removeAll();//its necessary
		txtZoneName.setText("zone name here");

		// panel for inputs
		
		// LabzoneName.setBounds(100, 100, 100, 20);
		zones.add(LabzoneName);
		zones.add(txtZoneName);
		zones.add(new JLabel("Date Opened:"));
		zones.add(date);
		// zones.add(new JLabel("Comments:"));

		
		comments.setAutoscrolls(true);
		comments.setLineWrap(true);
		comments.setWrapStyleWord(true);
		comments.setDocument(new FixedSizeDoc(1000));
		comments.setText("Comments here");
		comments.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				comments.setText("");
				
			}
		});
		comments.setToolTipText("short notes");
		txtZoneName.setToolTipText("Name of new zone");
		txtZoneName.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				txtZoneName.setText("");
				
			}
		});
		JScrollPane txt = new JScrollPane(comments);
		txt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		// button to execute update
		JButton save = new JButton("Save");
		save.setBackground(Color.LIGHT_GRAY);
		save.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String Newzone = txtZoneName.getText().toString();
				// Date dateSelected = date.getDate();
				String comnts = comments.getText();
				// Insertion to database
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					String sql = "INSERT INTO Zone VALUES (?,?,0,?);";
					stmt = (PreparedStatement) con.prepareStatement(sql);
					stmt.setString(1, Newzone.toUpperCase());
					stmt.setDate(2, new java.sql.Date(date.getDate().getTime()));
					stmt.setString(3, comnts);
					stmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Data successfully enter.");
					showZones();
					comments.setText("Comments here");
					txtZoneName.setText("zone name here");
					date.setDate(null);

				} catch (SQLException | ClassNotFoundException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});
		Object[] p = { zones, new JLabel("Comments of less than or equal to 1000 characters:"), txt
				};
		JOptionPane.showOptionDialog(this, p, "Enter new zone", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, new Object[] { save }, null);

	}

	/**************************************************************
	 * edit zone method
	 *************************************************************/
	void editZone() {
		zones.removeAll();//its necessary
		comments.setAutoscrolls(true);
		comments.setLineWrap(true);
		comments.setWrapStyleWord(true);
		comments.setDocument(new FixedSizeDoc(1000));
		// zones.add(comments);
		JScrollPane txt = new JScrollPane(comments);
		txt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		String sql = "select * from Zone;";
		
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			
			ResultSet rs = null;
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				zoneName.add(rs.getString("Name"));
			}
			zoneName.select(0);
			sql = "select DateOpened,  " + "`Comments` FROM `Zone` where Name=? ;";
			try {
				stmt = con.prepareStatement(sql);
				stmt.setString(1, zoneName.getSelectedItem());
				rs = stmt.executeQuery();
				rs.beforeFirst();
				while (rs.next()) {
					comments.setText(rs.getString("Comments"));
					date.setDate((rs.getDate("DateOpened")));
				}
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
				
			}

		} catch (SQLException | ClassNotFoundException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}

		zoneName.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				String sql = "select DateOpened,  " + "`Comments` FROM `Zone` where Name=? ;";
				try {
					stmt = con.prepareStatement(sql);
					stmt.setString(1, zoneName.getSelectedItem());
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						comments.setText(rs.getString("Comments"));
						date.setDate((rs.getDate("DateOpened")));
					}
				} catch (SQLException e1) {}

			}
		});

		//
		txtZoneName.setText("");
		zones.add(LabzoneName);
		zones.add(zoneName);
		zones.add(new JLabel("Date Opened:"));
		zones.add(date);

		// button to execute update
		JButton update = new JButton("Update record");
		update.setBackground(Color.LIGHT_GRAY);
		update.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		update.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Insertion to database
				try {
					String sql = "UPDATE `Zone` SET `DateOpened`=?," + "`Comments`=? WHERE `Name`=?";
					stmt = (PreparedStatement) con.prepareStatement(sql);

					stmt.setDate(1, new java.sql.Date(date.getDate().getTime()));
					stmt.setString(2, comments.getText());
					stmt.setString(3, zoneName.getSelectedItem());
					stmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record successfully updated.");
					

				} catch (SQLException e1) {

				}
			}
		});
		Object[] p = { zones, new JLabel("Comments of less than or equal to 1000 characters:"), txt

		};
		JOptionPane.showOptionDialog(this, p, "Enter new zone", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, new Object[] {update}, null);

	}

	// delete zone
	void deleteZone() {
		Choice zone = new Choice();
		String sql = "select * from Zone;";

		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD); 
			
			ResultSet rs = null;
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				zone.add(rs.getString("Name"));
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// create array of type Object for display on JOptionpane
		Object[] contribution = { LabzoneName, zone };

		int option = JOptionPane.showConfirmDialog(this, contribution, "Select Zone to delete",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

		// If ok button clicked, get the inputs from the textfields
		if (option == JOptionPane.OK_OPTION) {
			String Newzone = zone.getSelectedItem();
			// TO DO:implement appropriate sql command to edit existing zone
			// record
			String sql2 = "delete from Zone WHERE Name=?;";
			try {
				stmt = (PreparedStatement) con.prepareStatement(sql2);
				stmt.setString(1, Newzone);
				stmt.executeUpdate();
				showZones();
			} catch (SQLException e) {
				JOptionPane.showMessageDialog(null, "Record deleted successfully");
			}

		}

	}

	// show existing zones
	void showZones() {
		ResultSet rs = null;
		try {
			String sql = "select * from Zone;";
			
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			// Prepare table to fill values
			DefaultTableModel tab = new DefaultTableModel();

			// get metadata
			ResultSetMetaData rsmt = rs.getMetaData();

			// create array of column names
			int colCount = rsmt.getColumnCount();// number of columns
			String colNames[] = {
					"Date opened","Name of Zone","Number of Christians ","Short notes"
			};
			tab.setColumnIdentifiers(colNames);// set column headings

			// now populate the data
			rs.beforeFirst();// make sure to move cursor to beginning
			while (rs.next()) {
				String[] rowData = new String[colCount];
				for (int i = 1; i <= colCount; i++) {
					rowData[i - 1] = rs.getString(i);
				}
				tab.addRow(rowData);
			}
			JPanel panel = new JPanel();
			JTable t = new JTable(tab);
			t.setSize(500, 600);
			sp = new JScrollPane(t);
			panel.setSize(500, 600);
			sp.setBorder(BorderFactory.createTitledBorder("LIST SHOWING EXISTING ZONES"));
			panel.add(sp);
			parent.add(sp, BorderLayout.CENTER);
			frame.pack();
			frame.repaint();
		} catch (Exception e) {
		}
	}
	void searchZone(){
		JTextField txtName=new JTextField(5);
		Object [] obj={
				new JLabel("Name of Zone:"),
				txtName
		};
		int option=JOptionPane.showConfirmDialog(this,obj,
				"Enter name of Zone to search", JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
		if(option==JOptionPane.OK_OPTION){
			ResultSet rs = null;
			try {
				String sql = "select * from Zone where Name=?;";
				
				// Register jdbc driver
				Class.forName(DRIVER_CLASS);
				// open connection
				con = DriverManager.getConnection(URL, USER, PASSWORD);
				
				stmt = con.prepareStatement(sql);
				stmt.setString(1, txtName.getText().toString().trim().toUpperCase());
				rs = stmt.executeQuery();
				
				// Prepare table to fill values
				DefaultTableModel tab = new DefaultTableModel();

				// get metadata
				ResultSetMetaData rsmt = rs.getMetaData();

				// create array of column names
				int colCount = rsmt.getColumnCount();// number of columns
				String colNames[] = {
						"Date opened","Name of Zone","Number of Christians ","Short notes"
				};
				tab.setColumnIdentifiers(colNames);// set column headings

				// now populate the data
				rs.beforeFirst();// make sure to move cursor to beginning
				while (rs.next()) {
					String[] rowData = new String[colCount];
					for (int i = 1; i <= colCount; i++) {
						rowData[i - 1] = rs.getString(i);
					}
					tab.addRow(rowData);
				}
				JTable t = new JTable(tab);
				sp = new JScrollPane(t);
				panel.setSize(500, 600);
				sp.setBorder(BorderFactory.createTitledBorder("LIST SHOWING EXISTING ZONES"));
				
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
			}
		}

		else {
			JOptionPane.showMessageDialog(null, "We wish you well.");
		}
	}

}
