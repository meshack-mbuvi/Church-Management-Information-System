package databaseApp;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class Groups extends VariablesDefinitions {
	private static final long serialVersionUID = -7747029148638774509L;
	JScrollPane sp;

	public Groups() {
		sp = new JScrollPane();
	}

	/*********************
	 * new group
	 ********************/
	void addGroup() {
		// select church to add group
		String sql = "select * from Zone;";
		Choice chcZone = new Choice();
		Choice chcGroupTypes = new Choice();
		ResultSet rs = null;
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcZone.addItem(rs.getString("Name"));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		sql = "select * from `GROUP_TYPES`;";
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcGroupTypes.addItem(rs.getString("GroupType"));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
		Object[] chrc = { new JLabel("Zone Name:"), chcZone };
		int option = JOptionPane.showConfirmDialog(this, chrc, "Select Zone to add group", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE);

		String item = "", group = "", type = "";
		double amount = 0.0;
		if (option == JOptionPane.OK_OPTION) {
			item = chcZone.getSelectedItem();
			JTextField txtgrp = new JTextField(10);
			JTextField txttype = new JTextField(10);
			JTextField txtAmount = new JTextField(10);
			Object[] obj = { new JLabel("Name of Group:"), txtgrp, new JLabel("Type of Group:"), chcGroupTypes,
					new JLabel("Amount in Kshs:"), txtAmount };
			// display for user to enter details
			option = JOptionPane.showConfirmDialog(this, obj, "Group details", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (option == JOptionPane.OK_OPTION) {
				group = txtgrp.getText().toString();
				type = txttype.getText().toString();
				amount = Double.parseDouble(txtAmount.getText());
				sql = "INSERT INTO " + "`Groups`(`Name`, `Type`, `Amount`, `Church`,`Transacted By`)"
						+ " VALUES (?,?,?,?,?);";
				try {
					stmt = con.prepareStatement(sql);
					stmt.setString(1, group);
					stmt.setString(2, type);
					stmt.setDouble(3, amount);
					stmt.setString(4, item);
					stmt.setString(5, USER);
					stmt.executeUpdate();
					/*******************************************
					 * show exiting groups from selected church
					 ******************************************/
					frame.repaint();

					sql = "select * from Groups where Church =?;";
					try {
						stmt = con.prepareStatement(sql);
						stmt.setString(1, item);
						rs = stmt.executeQuery();
						// TO DO:Prepare table to fill values
						DefaultTableModel tab = new DefaultTableModel();
						// get metadata
						ResultSetMetaData rsmt = rs.getMetaData();
						// create array of column names
						int colCount = rsmt.getColumnCount();// number of
																// columns
						String colNames[] = new String[colCount];
						for (int i = 1; i <= colCount; i++) {
							colNames[i - 1] = rsmt.getColumnName(i);
						}
						tab.setColumnIdentifiers(colNames);// set column
															// headings
						// now populate the data
						rs.beforeFirst();// make sure to move cursor to
											// beginning
						while (rs.next()) {
							String[] rowData = new String[colCount];
							for (int i = 1; i <= colCount; i++) {
								rowData[i - 1] = rs.getString(i);
							}
							tab.addRow(rowData);
						}
						JTable t = new JTable(tab);
						sp = new JScrollPane(t);
						String title = "TABLE OF GROUPS IN " + item.toUpperCase() + "-OUTSTATION";
						sp.setBorder(BorderFactory.createTitledBorder(title));
						parent.removeAll();
					} catch (Exception e) {
						// TODO: handle exception
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	/*******************************
	 * Edit group method
	 *******************************/
	void editGroups() {
		// select church to add group
		String sql = "select * from Church;";
		Choice churc = new Choice();
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				churc.addItem(rs.getString("C_Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object[] chrc = { new JLabel("Church Name:"), churc };
		int option = JOptionPane.showConfirmDialog(this, chrc, "Select church to edit group",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		String item = "", group = "", type = "";

		double amount = 0.0;
		if (option == JOptionPane.OK_OPTION) {
			item = churc.getSelectedItem();
			// load existing groups for specified church
			Choice chcgrp = new Choice();
			sql = "select * from Groups where Church=?;";
			try {
				stmt = con.prepareStatement(sql);
				stmt.setString(1, item);
				rs = stmt.executeQuery();
				rs.beforeFirst();
				while (rs.next()) {
					chcgrp.addItem(rs.getString("Name"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			//
			churc.select(item);
			churc.setEnabled(false);
			JTextField txttype = new JTextField(10);
			JTextField txtAmount = new JTextField(10);
			txtAmount.setText("0");
			Object[] obj = { new JLabel("Church selected:"), churc, new JLabel("Name of Group:"), chcgrp,
					new JLabel("Type of Group:"), txttype, new JLabel("Amount in Kshs:"), txtAmount };
			// display for user to enter details
			option = JOptionPane.showConfirmDialog(this, obj, "Group details", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (option == JOptionPane.OK_OPTION) {
				group = chcgrp.getSelectedItem();
				type = txttype.getText().toString();
				amount = Double.parseDouble(txtAmount.getText());
				sql = "UPDATE `Groups` SET `Type` = ?,`Amount` = ?," + "`Transacted By` = ? WHERE `Name` = ?";
				try {
					stmt = (PreparedStatement) con.prepareStatement(sql);
					stmt.setString(1, type);
					stmt.setDouble(2, amount);
					stmt.setString(3, USER);
					stmt.setString(4, group);
					stmt.executeUpdate();
					showGroups();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**************************************
	 * Delete group
	 **************************************/
	void delGroup() {
		// display drop-down with churches to select
		String sql = "select * from Church;";
		Choice churc = new Choice();
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				churc.addItem(rs.getString("C_Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object[] chrc = { new JLabel("Church Name:"), churc };
		int option = JOptionPane.showConfirmDialog(this, chrc, "Select Outstation to continue",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		String item = "", group = "";
		Choice chcgrp = new Choice();
		if (option == JOptionPane.OK_OPTION) {
			item = churc.getSelectedItem();
			sql = "select *from Groups where Church=?;";
			try {
				stmt = con.prepareStatement(sql);
				stmt.setString(1, item);
				rs = stmt.executeQuery();
				rs.beforeFirst();
				while (rs.next()) {
					chcgrp.addItem(rs.getString("Name"));
				}
				Object[] grps = {
						// new JLabel("Group Name:"),
						chcgrp };

				option = JOptionPane.showConfirmDialog(this, grps, "Select group to delete",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

				if (option == JOptionPane.OK_OPTION) {
					group = chcgrp.getSelectedItem();
					sql = "delete from Groups where Name=?;";

					try {
						stmt = con.prepareStatement(sql);
						stmt.setString(1, group);
						stmt.executeUpdate();
					} catch (Exception e) {
						e.printStackTrace();
					}

					/***************************************
					 * show existing groups from the selected church
					 */

					frame.repaint();

					sql = "select * from Groups where Church =?;";
					try {
						stmt = con.prepareStatement(sql);
						stmt.setString(1, item);
						rs = stmt.executeQuery();
						// TO DO:Prepare table to fill values
						DefaultTableModel tab = new DefaultTableModel();
						// get metadata
						ResultSetMetaData rsmt = rs.getMetaData();
						// create array of column names
						int colCount = rsmt.getColumnCount();// number of
																// columns
						String colNames[] = new String[colCount];
						for (int i = 1; i <= colCount; i++) {
							colNames[i - 1] = rsmt.getColumnName(i);
						}
						tab.setColumnIdentifiers(colNames);// set column
															// headings
						// now populate the data
						rs.beforeFirst();// make sure to move cursor to
											// beginning
						while (rs.next()) {
							String[] rowData = new String[colCount];
							for (int i = 1; i <= colCount; i++) {
								rowData[i - 1] = rs.getString(i);
							}
							tab.addRow(rowData);
						}
						JTable t = new JTable(tab);
						JScrollPane sp = new JScrollPane(t);
						String title = "TABLE OF GROUPS IN " + item.toUpperCase() + "-OUTSTATION";
						sp.setBorder(BorderFactory.createTitledBorder(title));
						parent.removeAll();
						parent.add(sp, BorderLayout.CENTER);
						frame.pack();
						frame.repaint();
						//
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
			}
		}

	}

	/**************************************
	 * Show existing church groups
	 **************************************/
	void showGroups() {

		frame.repaint();
		// populate drop-down list
		String sql = "select * from Church;";
		ResultSet rs = null;
		Choice chcChurch = new Choice();
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcChurch.addItem(rs.getString("C_Name"));
			}

			Object[] obj = { new JLabel("Church Name:"), chcChurch };

			// display it for user to select
			int option = JOptionPane.showConfirmDialog(this, obj, "Select church to continue", JOptionPane.OK_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (option == JOptionPane.OK_OPTION) {
				String church = chcChurch.getSelectedItem();
				sql = "select * from Groups where Church =?;";
				try {
					stmt = con.prepareStatement(sql);
					stmt.setString(1, church);
					rs = stmt.executeQuery();

					// TO DO:Prepare table to fill values
					DefaultTableModel tab = new DefaultTableModel();

					// get metadata
					ResultSetMetaData rsmt = rs.getMetaData();

					// create array of column names
					int colCount = rsmt.getColumnCount();// number of columns
					String colNames[] = new String[colCount];
					for (int i = 1; i <= colCount; i++) {
						colNames[i - 1] = rsmt.getColumnName(i);
					}
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
					JScrollPane sp = new JScrollPane(t);
					String title = "TABLE OF GROUPS IN " + church.toUpperCase() + "-OUTSTATION";
					sp.setBorder(BorderFactory.createTitledBorder(title));
					parent.removeAll();
					parent.add(sp, BorderLayout.CENTER);
					frame.pack();
					frame.repaint();
					//
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
