package databaseApp;

import java.awt.Choice;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

@SuppressWarnings("serial")
public class OutstationCommittee extends VariablesDefinitions {

	private JComboBox<Integer> ID;
	private String sql = null;
	private ResultSet rs = null;
	JScrollPane sp;

	public OutstationCommittee() {
		sql = null;
		rs = null;
		sp = new JScrollPane();

	}

	/**************************************************************************
	 * 
	 * Method to add new church committee to the database
	 * 
	 **************************************************************************/
	void addChrchCommitte() {
		// variables for below function
		ID = new JComboBox<Integer>();
		AutoCompleteDecorator.decorate(ID);
		JButton save = new JButton("Save");
		final Choice position = new Choice();
		final JTextField txtLevel = new JTextField(10), txtPhone = new JTextField(10);
		final Choice chcchrch = new Choice();

		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			sql = "select C_Name from Outstation;";
			ResultSet rs = null;
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				chcchrch.add(rs.getString("C_Name"));
			}
		} catch (SQLException | ClassNotFoundException e) {
		}

		Object[] outstation = { new JLabel("Outstation:"), chcchrch };
		int option = JOptionPane.showConfirmDialog(this, outstation, "Select outstation121",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

		chcchrch.setEnabled(false);
		if (option == JOptionPane.OK_OPTION) {
			try {
				sql = "select RegNo from Registration where Church=?;";
				stmt = con.prepareStatement(sql);
				stmt.setString(1, chcchrch.getSelectedItem().toUpperCase().trim());
				rs = stmt.executeQuery();
				rs.beforeFirst();
				while (rs.next()) {
					ID.addItem(Integer.parseInt(rs.getString("RegNo")));
				}
				rs = null;
				if (ID.getItemCount() > 0) {
					sql = "select BaptismalName,OtherNames from Registration where RegNo=? ;";
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString()));

					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtFname.setText(rs.getString("BaptismalName"));
						txtSname.setText(rs.getString("OtherNames"));
					}
				}

			} catch (SQLException e1) {
				e1.printStackTrace();

			}
		}
		ID.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					sql = "select BaptismalName,OtherNames from Registration where RegNo=? ;";
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString()));

					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtFname.setText(rs.getString("BaptismalName"));
						txtSname.setText(rs.getString("OtherNames"));
					}
				} catch (Exception e2) {
				}
			}
		});
		// If save button clicked, save data
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (ID.getItemCount() > 0) {
					try {
						String sql = "INSERT INTO `OutstationCommittee`(`RegNo`, `Phone`, `Level`, `Position`, `Church`)"
								+ " VALUES (?,?,?,?,?);";
						stmt = (PreparedStatement) con.prepareStatement(sql);
						stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString()));
						stmt.setInt(2, Integer.parseInt(txtPhone.getText().toString()));
						stmt.setString(3, txtLevel.getText());
						stmt.setString(4, position.getSelectedItem());
						stmt.setString(5, chcchrch.getSelectedItem());
						stmt.executeUpdate();
						txtFname.setText("");
						txtSname.setText("");
						txtLevel.setText("");
						txtPhone.setText("");
						position.select(0);
					} catch (SQLException e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage());
					}
				} else {
					JOptionPane.showMessageDialog(null, "Outstation does not have members yet.");
				}
			}
		});
		// add items to the choice box
		position.add("Chairperson");
		position.add("Vice chairperson");
		position.add("Secretary");
		position.add("Vice secretary");
		position.add("Treasurer");
		position.add("Member");
		position.select("Member");

		Object[] inputfields = { new JLabel("Outstation:"), chcchrch, labID, ID, labBaptismalName, txtFname,
				labOtherName, txtSname, labPhone, txtPhone, new JLabel("Level"), txtLevel, labPosition, position };

		JOptionPane.showOptionDialog(this, inputfields, "Enter Outstation committee member", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, new Object[] { save }, null);
	}

	/**************************************************************
	 * Edit church Committee members
	 **************************************************************/
	void EditChrchCommitte() {
		ID = new JComboBox<Integer>();
		AutoCompleteDecorator.decorate(ID);
		JButton save = new JButton("Update data");

		final Choice position = new Choice();
		final JTextField txtLevel = new JTextField(10), txtPhone = new JTextField(10);
		final Choice chcChrch = new Choice();
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			String sql = "select C_Name from Outstation;";
			ResultSet rs = null;
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				chcChrch.add(rs.getString("C_Name"));
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO: handle exception
		}

		Object[] outstation = { new JLabel("Outstation:"), chcChrch };
		int option = JOptionPane.showConfirmDialog(this, outstation, "Select outstation", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE);

		chcChrch.setEnabled(false);

		if (option == JOptionPane.OK_OPTION) {
			// add items to the choice box
			position.add("Chairperson");
			position.add("Vice chairperson");
			position.add("Secretary");
			position.add("Vice secretary");
			position.add("Treasurer");
			position.add("Member");
			position.select("Member");
			sql = "select RegNo from `OutstationCommittee` where Church=?;";

			ResultSet rs;
			try {
				stmt = con.prepareStatement(sql);
				stmt.setString(1, chcChrch.getSelectedItem().trim().toUpperCase());
				rs = stmt.executeQuery();
				rs.beforeFirst();
				while (rs.next()) {
					ID.addItem(Integer.parseInt(rs.getString("RegNo")));
				}
				sql = "select  `Registration`.BaptismalName, " + " `Registration`.OtherNames,"
						+ "`OutstationCommittee`.`Phone`, " + "`OutstationCommittee`.`Level`,"
						+ "`OutstationCommittee`.`Position`  from `OutstationCommittee` "
						+ "JOIN `Registration` USING(RegNo) where RegNo=?;";
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString()));
				rs = stmt.executeQuery();
				rs.beforeFirst();
				while (rs.next()) {
					txtBapName.setText(rs.getString("BaptismalName"));
					txtOtherName.setText(rs.getString("OtherNames"));
					txtLevel.setText(rs.getString("Level"));
					txtPhone.setText(rs.getString("Phone"));
					position.select(rs.getString("Position"));
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		ID.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					sql = "select  `Registration`.BaptismalName, " + " `Registration`.OtherNames,"
							+ "`OutstationCommittee`.`Phone`, " + "`OutstationCommittee`.`Level`,"
							+ "`OutstationCommittee`.`Position`  from `OutstationCommittee` "
							+ "JOIN `Registration` USING(RegNo) where RegNo=?;";
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString()));
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtBapName.setText(rs.getString("BaptismalName"));
						txtOtherName.setText(rs.getString("OtherNames"));
						txtLevel.setText(rs.getString("Level"));
						txtPhone.setText(rs.getString("Phone"));
						position.select(rs.getString("Position"));
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		// If save button clicked, get the inputs from the text fields
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * Update Record
				 */
				try {
					String sql = "Update `OutstationCommittee` " + "SET `Phone`=?," + "`Position`=?," + "`Level`=?"
							+ " WHERE `RegNo`=?;";
					stmt = (PreparedStatement) con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(txtPhone.getText().toString()));
					stmt.setString(2, position.getSelectedItem());
					stmt.setString(3, txtLevel.getText());
					stmt.setInt(4, Integer.parseInt(ID.getSelectedItem().toString()));
					stmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Data updated!");

				} catch (SQLException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});

		JLabel labzone = new JLabel("Zone");
		txtBapName.setEditable(false);
		txtOtherName.setEditable(false);
		Object[] inputfields = { labzone, chcChrch, labID, ID, labBaptismalName, txtBapName, labOtherName, txtOtherName,
				labPhone, txtPhone, new JLabel("Level"), txtLevel, labPosition, position };

		JOptionPane.showOptionDialog(this, inputfields, "Enter zone committee members", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, new Object[] { save }, null);
	}

	// Display church committee members in a table
	public void showChurchCommMembers() {
		// sql command to select all records and display them
		
		ResultSet rs = null;
		String sql = "select C_Name from Outstation;";
		Choice chcChurch = new Choice();
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcChurch.addItem(rs.getString("C_Name"));
			}
		} catch (Exception e) {
		}

		Object[] obj = { chcChurch };
		int option = JOptionPane.showConfirmDialog(this, obj, "select Outstation..", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			try {
				// Register jdbc driver
				Class.forName(DRIVER_CLASS);
				// open connection
				con = DriverManager.getConnection(URL, USER, PASSWORD);
				sql = "select  `Registration`.`RegNo`,"
						+ "`Registration`.`BaptismalName`, "
						+ "`Registration`.`OtherNames`, "
						+ "`OutstationCommittee`.`Phone`," 
						+ "`OutstationCommittee`.`Level`,"
						+ "`OutstationCommittee`.`Position`,"
						+ "`OutstationCommittee`.`Church`"
						+ " from `OutstationCommittee` "
						
						+ "JOIN `Registration` USING(RegNo) where `OutstationCommittee`.Church=?;";//where Church=?
				stmt = con.prepareStatement(sql);
				stmt.setString(1, chcChurch.getSelectedItem().toString());
				rs = stmt.executeQuery();
				// TO DO:Prepare table to fill values
				DefaultTableModel tab = new DefaultTableModel();

				// get metadata
				ResultSetMetaData rsmt = rs.getMetaData();

				// create array of column names
				int colCount = rsmt.getColumnCount();// number of columns
				String colNames[] = {
						"Registration NO","Baptismal Name","Other Names","Phone Number",
						"Level","Position","Outstation"
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
				String title = "OUTSTATION COMMITTEE MEMBERS For " + chcChurch.getSelectedItem().toString();
				sp.setBorder(BorderFactory.createTitledBorder(title.toUpperCase()));
				

			} catch (SQLException | ClassNotFoundException e) {
				
			}
		}
		else 
		{
			JOptionPane.showMessageDialog(null, "Operation canceled!");
		}

	}

	/********************************************
	 * Delete church committee member
	 ********************************************/
	void delChurchComMembers() {
		ID = new JComboBox<Integer>();
		AutoCompleteDecorator.decorate(ID);
		JButton save = new JButton("Delete data");
		final JTextField txtBapName = new JTextField(5), txtOtherName = new JTextField(5), txtPhone = new JTextField(5);
		txtBapName.setEditable(false);
		txtOtherName.setEditable(false);
		txtPhone.setEditable(false);
		final Choice position = new Choice();
		final JTextField txtLevel = new JTextField(10);
		txtLevel.setEditable(false);

		final Choice chcChrch = new Choice();
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			String sql = "select C_Name from Outstation;";
			ResultSet rs = null;
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				chcChrch.add(rs.getString("C_Name"));
			}
		} catch (SQLException | ClassNotFoundException e) {
			// TODO: handle exception
		}

		Object[] outstation = { new JLabel("Outstation:"), chcChrch };
		int option = JOptionPane.showConfirmDialog(this, outstation, "Select outstation", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE);

		chcChrch.setEnabled(false);

		if (option == JOptionPane.OK_OPTION) {
			// add items to the choice box
			position.add("Chairperson");
			position.add("Vice chairperson");
			position.add("Secretary");
			position.add("Vice secretary");
			position.add("Treasurer");
			position.add("Member");
			position.select("Member");
			sql = "select RegNo from `OutstationCommittee` where Church=?;";

			ResultSet rs;
			try {
				stmt = con.prepareStatement(sql);
				stmt.setString(1, chcChrch.getSelectedItem().trim().toUpperCase());
				rs = stmt.executeQuery();
				rs.beforeFirst();
				while (rs.next()) {
					ID.addItem(Integer.parseInt(rs.getString("RegNo")));
				}
				sql = "select  `Registration`.BaptismalName, " + " `Registration`.OtherNames,"
						+ "`OutstationCommittee`.`Phone`, " + "`OutstationCommittee`.`Level`,"
						+ "`OutstationCommittee`.`Position`  from `OutstationCommittee` "
						+ "JOIN `Registration` USING(RegNo) where RegNo=?;";
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString()));
				rs = stmt.executeQuery();
				rs.beforeFirst();
				while (rs.next()) {
					txtBapName.setText(rs.getString("BaptismalName"));
					txtOtherName.setText(rs.getString("OtherNames"));
					txtLevel.setText(rs.getString("Level"));
					txtPhone.setText(rs.getString("Phone"));
					position.select(rs.getString("Position"));
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		ID.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				try {
					sql = "select  `Registration`.BaptismalName, " + " `Registration`.OtherNames,"
							+ "`OutstationCommittee`.`Phone`, " + "`OutstationCommittee`.`Level`,"
							+ "`OutstationCommittee`.`Position`  from `OutstationCommittee` "
							+ "JOIN `Registration` USING(RegNo) where RegNo=?;";
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString()));
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtBapName.setText(rs.getString("BaptismalName"));
						txtOtherName.setText(rs.getString("OtherNames"));
						txtLevel.setText(rs.getString("Level"));
						txtPhone.setText(rs.getString("Phone"));
						position.select(rs.getString("Position"));
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		// If save button clicked, get the inputs from the text fields
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * Update Record
				 */
				try {
					String sql = "delete from `OutstationCommittee` WHERE `RegNo`=?;";
					stmt = (PreparedStatement) con.prepareStatement(sql);

					stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString()));
					stmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Data deleted!");
					ID.removeItem(ID.getSelectedItem());
					txtBapName.setText("");
					txtOtherName.setText("");
					txtLevel.setText("");
					txtPhone.setText("");

				} catch (SQLException e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});

		JLabel labzone = new JLabel("Zone");
		txtBapName.setEditable(false);
		txtOtherName.setEditable(false);
		Object[] inputfields = { labzone, chcChrch, labID, ID, labBaptismalName, txtBapName, labOtherName, txtOtherName,
				labPhone, txtPhone, new JLabel("Level"), txtLevel, labPosition, position };

		JOptionPane.showOptionDialog(this, inputfields, "Enter zone committee members", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, new Object[] { save }, null);
	}

}
