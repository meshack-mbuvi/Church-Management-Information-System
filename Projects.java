package databaseApp;

import java.awt.Choice;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Projects extends VariablesDefinitions {
	private static final long serialVersionUID = 6073643659743099527L;

	JScrollPane sp;

	public Projects() {
		sp = new JScrollPane();
	}

	/********************************************************************************
	 * 
	 * Projects methods
	 *
	 ********************************************************************************/
	void addProject() {
		// select church to add group
		String sql = "select * from Outstation;";
		Choice churc = new Choice();
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
				churc.addItem(rs.getString("C_Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object[] chrc = { new JLabel("Church Name:"), churc };
		int option = JOptionPane.showConfirmDialog(this, chrc, "Select church to add project",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			JTextField txtgrp = new JTextField(5);
			JTextField txttype = new JTextField(5);
			sql = "select * from  `ModeOfPayment` ;";

			Object[] obj = { new JLabel("Name of Project:"), txtgrp, new JLabel("Type of Project:"), txttype };
			// display for user to enter details
			option = JOptionPane.showConfirmDialog(this, obj, "Group details", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (option == JOptionPane.OK_OPTION) {
				sql = "INSERT INTO " + "`Projects`(`Name`, `Type`,`Church`)" + " VALUES (?,?,?);";
				try {
					stmt = con.prepareStatement(sql);
					stmt.setString(1, txtgrp.getText().toString().trim());
					stmt.setString(2, txttype.getText().toString().trim());
					stmt.setString(3, churc.getSelectedItem().trim());
					stmt.executeUpdate();
					/*******************************************
					 * show exiting groups from selected church
					 ******************************************/
					frame.repaint();

					sql = "select * from Projects where Church =?;";
					try {
						stmt = con.prepareStatement(sql);
						stmt.setString(1, churc.getSelectedItem().trim());
						rs = stmt.executeQuery();
						// TO DO:Prepare table to fill values
						DefaultTableModel tab = new DefaultTableModel();
						// get meta data
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
						String title = "TABLE OF PROJECTS IN " + churc.getSelectedItem().trim().toUpperCase()
								+ "-OUTSTATION";
						sp.setBorder(BorderFactory.createTitledBorder(title));

					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**************************************************
	 * Edit projects
	 **************************************************/
	private JComboBox<Integer> ID;
	void editProjects() {
		// select church to add group
		String sql = "select * from Outstation;";
		Choice churc = new Choice();
		final JTextField txttype = new JTextField(10);
		final  JTextField txtname = new JTextField(10);
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
				churc.addItem(rs.getString("C_Name"));
			}
		} catch (Exception e) {}
		ID=new JComboBox<Integer>();
		AutoCompleteDecorator.decorate(ID);
				
		
		Object[] chrc = { new JLabel("Church Name:"), churc };
		int option = JOptionPane.showConfirmDialog(this, chrc, "Select church to edit project",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			sql="select ID from Projects where Church=?;";
			try {
				// Register jdbc driver
				Class.forName(DRIVER_CLASS);
				// open connection
				con = DriverManager.getConnection(URL, USER, PASSWORD);
				stmt = con.prepareStatement(sql);
				stmt.setString(1, churc.getSelectedItem().trim());
				rs = stmt.executeQuery();
				rs.beforeFirst();
				while (rs.next()) {
					ID.addItem(rs.getInt("ID"));
				}
				sql="select Name,Type from Projects where ID=?;";
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString()));
				rs = stmt.executeQuery();
				while(rs.next()){
					txttype.setText(rs.getString("Type"));
					txtname.setText(rs.getString("Name"));
				}
			} catch (Exception e) {}
			
			ID.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					try {
						String sql="select Name,Type from Projects where ID=?;";
						stmt = con.prepareStatement(sql);
						stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString()));
						ResultSet rs = stmt.executeQuery();
						while(rs.next()){
							txttype.setText(rs.getString("Type"));
							txtname.setText(rs.getString("Name"));
						}
					} catch (Exception e2) {e2.printStackTrace();}
					
				}
			});
			
			churc.setEnabled(false);
			Object[] obj = { new JLabel("Church selected:"), churc,
					new JLabel("Project ID:"),ID,
					new JLabel("Name of Project:"), txtname,
					new JLabel("Type of Project:"), txttype };
			// display for user to enter details
			option = JOptionPane.showConfirmDialog(this, obj, "Edit project details", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (option == JOptionPane.OK_OPTION) {

				
				String sql2 = "UPDATE  `Projects` SET `Name`=?,`Type`=? WHERE `ID` = ?";
				try {
					stmt = (PreparedStatement) con.prepareStatement(sql2);
					stmt.setString(1, txtname.getText().toString().trim().toUpperCase());
					stmt.setString(2, txttype.getText().toString().trim().toUpperCase());
					stmt.setInt(3, Integer.parseInt(ID.getSelectedItem().toString()));
					stmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Data updated.");

					} catch (Exception e) {
						e.printStackTrace();
					}
			}
			else{
				JOptionPane.showMessageDialog(null, "Operation canceled!");
			}

		}
	}

	/**************************************************
	 * Delete projects
	 **************************************************/
	void delProject() {
		// select church to add group
				String sql = "select * from Outstation;";
				Choice churc = new Choice();
				final JTextField txttype = new JTextField(10);
				final  JTextField txtname = new JTextField(10);
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
						churc.addItem(rs.getString("C_Name"));
					}
				} catch (Exception e) {}
				ID=new JComboBox<Integer>();
				AutoCompleteDecorator.decorate(ID);
						
				
				Object[] chrc = { new JLabel("Church Name:"), churc };
				int option = JOptionPane.showConfirmDialog(this, chrc, "Select church to edit project",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

				if (option == JOptionPane.OK_OPTION) {
					sql="select ID from Projects where Church=?;";
					try {
						// Register jdbc driver
						Class.forName(DRIVER_CLASS);
						// open connection
						con = DriverManager.getConnection(URL, USER, PASSWORD);
						stmt = con.prepareStatement(sql);
						stmt.setString(1, churc.getSelectedItem().trim());
						rs = stmt.executeQuery();
						rs.beforeFirst();
						while (rs.next()) {
							ID.addItem(rs.getInt("ID"));
						}
						sql="select Name,Type from Projects where ID=?;";
						stmt = con.prepareStatement(sql);
						stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString()));
						rs = stmt.executeQuery();
						while(rs.next()){
							txttype.setText(rs.getString("Type"));
							txtname.setText(rs.getString("Name"));
						}
					} catch (Exception e) {}
					
					ID.addItemListener(new ItemListener() {
						
						@Override
						public void itemStateChanged(ItemEvent e) {
							try {
								String sql="select Name,Type from Projects where ID=?;";
								stmt = con.prepareStatement(sql);
								stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString()));
								ResultSet rs = stmt.executeQuery();
								while(rs.next()){
									txttype.setText(rs.getString("Type"));
									txtname.setText(rs.getString("Name"));
								}
							} catch (Exception e2) {e2.printStackTrace();}
							
						}
					});
					
					churc.setEnabled(false);
					txtname.setEditable(false);
					txttype.setEditable(false);
					Object[] obj = { new JLabel("Church selected:"), churc,
							new JLabel("Project ID:"),ID,
							new JLabel("Name of Project:"), txtname,
							new JLabel("Type of Project:"), txttype };
					// display for user to enter details
					option = JOptionPane.showConfirmDialog(this, obj, "Edit project details", JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
					if (option == JOptionPane.OK_OPTION) {

						
						String sql2 = "delete from Projects WHERE `ID` = ?";
						try {
							stmt = (PreparedStatement) con.prepareStatement(sql2);
							stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString()));
							stmt.executeUpdate();
							JOptionPane.showMessageDialog(null, "Data deleted.");

							} catch (Exception e) {
								e.printStackTrace();
							}
					}
					else{
						JOptionPane.showMessageDialog(null, "Operation canceled!");
					}

				}
	}

	/******************************************
	 * Show projects for a specified church
	 ******************************************/
	void showProjects() {

		// populate drop-down list
		String sql = "select C_Name from Outstation;";
		ResultSet rs = null;
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

			Object[] obj = { new JLabel("Church Name:"), chcChurch };

			// display it for user to select
			int option = JOptionPane.showConfirmDialog(this, obj, "Select church to continue", JOptionPane.OK_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (option == JOptionPane.OK_OPTION) {
				String church = chcChurch.getSelectedItem();
				sql = "select `ID`, `Date and Time Created`, "
						+ "`Name`, `Type`, `Church` FROM `Projects`  where Church =?;";
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
					String colNames[] = {
							"Project ID","Date project created","Project Name","Type of Project",
							"Church"
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
					String title = "TABLE OF PROJECTS IN " + church.toUpperCase() + "-OUTSTATION";
					sp.setBorder(BorderFactory.createTitledBorder(title));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
