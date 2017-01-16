package databaseApp;

import java.awt.Choice;
import java.awt.Color;
import java.awt.GridLayout;
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

@SuppressWarnings("serial")
public class Outstation extends VariablesDefinitions {

	JScrollPane sp;

	public Outstation() {
		// TODO Auto-generated constructor stub
	}

	/**************************************************************************
	 * 
	 * Method to add new church to the database
	 * 
	 **************************************************************************/
	public void newChurch() {
		ResultSet rs = null;

		txtName.setText("");
		txtCatechist.setText("");
		chrchName = "";
		catechistName = "";
		JLabel labCatechist = new JLabel("Catechist Name:");
		chcZones.removeAll();
		chcZones.add("");
		chcZones.select("");// select this by default

		// Select existing zones first
		String sql = "select Name from Zone ;";
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {

				chcZones.add(rs.getString("Name"));
			}
			Object[] inputfields = { labZone, chcZones };
			int option = JOptionPane.showConfirmDialog(this, inputfields,
					"Select Zone before you proceed to next section", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (option == JOptionPane.OK_OPTION) {
				zone = chcZones.getSelectedItem();// return selected item
				// Then from selected zone,create new outstation
				// Check whether zone exists
				if (!(zone.isEmpty())) {
					final JTextArea comments = new JTextArea(5, 10);
					comments.setAutoscrolls(true);
					comments.setLineWrap(true);
					comments.setWrapStyleWord(true);
					comments.setDocument(new FixedSizeDoc(1000));
					comments.setToolTipText("Write short notes here or leave blank");
					comments.setText("");

					JScrollPane txt = new JScrollPane(comments);
					txt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

					JPanel p = new JPanel(new GridLayout(4, 2, 4, 4));
					p.add(labName);
					p.add(txtName);
					p.add(labCatechist);
					p.add(txtCatechist);
					final JDateChooser date = new JDateChooser();
					p.add(new JLabel("Date opened:"));
					p.add(date);
					JPanel pComments = new JPanel(new GridLayout());
					pComments.add(txt);
					JButton btnSave = new JButton("Save record");
					btnSave.setBackground(Color.LIGHT_GRAY);
					btnSave.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
					btnSave.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e2) {
							chrchName = txtName.getText().toString();
							catechistName = txtCatechist.getText().toString();
							/*
							 * continue to perform insertion
							 */

							if (!catechistName.isEmpty() & !chrchName.isEmpty()) {
								if (date.getDate() != null) {
									try {
										// Register jdbc driver
										Class.forName(DRIVER_CLASS);
										// open connection
										con = DriverManager.getConnection(URL, USER, PASSWORD);
										stmt = (PreparedStatement) con
												.prepareStatement("INSERT INTO Outstation VALUES (?,?,?,?,?,?);");
										stmt.setString(1, chrchName.toUpperCase());
										stmt.setDate(2, new java.sql.Date(date.getDate().getTime()));
										stmt.setString(3, chcZones.getSelectedItem().toUpperCase());
										stmt.setInt(4, 0);
										stmt.setString(5, catechistName.toUpperCase().trim());
										stmt.setString(6, comments.getText().toUpperCase());
										stmt.executeUpdate();
										// reset text fields after collecting
										// data
										txtName.setText("");
										txtCatechist.setText("");
										chrchName = "";
										catechistName = "";
										date.setDate(null);
										
										//update outstation count in zone
										int count=0;
										String sql="select count(C_Name) from Outstation where Zone=?;";
										stmt=con.prepareStatement(sql);
										stmt.setString(1, zone);
										ResultSet rs=stmt.executeQuery();
										rs.beforeFirst();
										//get the tally
										while(rs.next()){
											count=rs.getInt("count(C_Name)");
										}
										//update it
										sql="update Zone set No_Of_Outstations=? where Name=?;";
										stmt=con.prepareStatement(sql);
										stmt.setInt(1, count);
										stmt.setString(2, zone);
										stmt.executeUpdate();
										
										JOptionPane.showMessageDialog(null, " Data recorded.");
										stmt.close();
									} catch (SQLException | ClassNotFoundException e) {
										e.printStackTrace();
									}
								} else {
									JOptionPane.showMessageDialog(null, " Select valid date.");
								}

							} else {
								JOptionPane.showMessageDialog(null, " You have empty fields.");
							}
						}

					});

					JButton clear = new JButton("Reset");
					clear.setBackground(Color.LIGHT_GRAY);
					clear.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

					clear.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							// reset text fields after collecting data
							txtName.setText("");
							chcZones.select(0);
							txtCatechist.setText("");
							comments.setText("");
							date.setDate(null);
						}
					});

					Object[] obj = { p, new JLabel("Comments"), pComments };

					JOptionPane.showOptionDialog(this, obj, "Enter Church details to create it",
							JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
							new Object[] { clear, btnSave }, null);

				} else {
					JOptionPane.showMessageDialog(null, "No zone selected!");
				}
			}

		} catch (SQLException | ClassNotFoundException e) {
		}

	}

	public void editChurch() {
		final JTextArea comments = new JTextArea(5, 10);
		comments.setAutoscrolls(true);
		comments.setLineWrap(true);
		comments.setWrapStyleWord(true);
		comments.setDocument(new FixedSizeDoc(1000));
		// zones.add(comments);
		JScrollPane txt = new JScrollPane(comments);
		txt.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		final JDateChooser date = new JDateChooser();

		final JTextField txtCatechist = new JTextField(10);
		String zone = "";
		Choice chczon = new Choice();
		final Choice church = new Choice();

		try {// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			String sql = "select * from Zone;";
			ResultSet rs = null;
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				chczon.addItem(rs.getString("Name"));
			}

			int option = JOptionPane.showConfirmDialog(this, chczon, "Select zone first", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);

			if (option == JOptionPane.OK_OPTION) {
				zone = chczon.getSelectedItem().toString();
				try {
					sql = "select * from Outstation where Zone=?;";
					rs = null;
					stmt = con.prepareStatement(sql);
					stmt.setString(1, zone.toLowerCase());
					rs = stmt.executeQuery();
					while (rs.next()) {
						church.addItem(rs.getString("C_Name"));
					}
					if (church.getItemCount() > 0) {
						church.select(0);
						sql = "select `DateOpened`, " + " `CatechistName`, "
								+ "`Comments` FROM `Outstation` WHERE `C_Name`=?;";
						try {
							stmt = con.prepareStatement(sql);
							stmt.setString(1, church.getSelectedItem());
							rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								comments.setText(rs.getString("Comments"));
								date.setDate(rs.getDate("DateOpened"));
								txtCatechist.setText(rs.getString("CatechistName"));
							}
						} catch (Exception e2) {
						}

						church.addItemListener(new ItemListener() {
							@Override
							public void itemStateChanged(ItemEvent e) {
								String sql = "select `DateOpened`, " + " `CatechistName`, "
										+ "`Comments` FROM `Outstation` WHERE `C_Name`=?;";
								try {
									stmt = con.prepareStatement(sql);
									stmt.setString(1, church.getSelectedItem());
									ResultSet rs = stmt.executeQuery();
									rs.beforeFirst();
									while (rs.next()) {
										comments.setText(rs.getString("Comments"));
										date.setDate(rs.getDate("DateOpened"));
										txtCatechist.setText(rs.getString("CatechistName"));
									}
								} catch (Exception e2) {
									// TODO: handle exception
								}
							}
						});
					}

				} catch (SQLException e) {
				}
			}
		} catch (Exception e) {
		}
		JPanel p = new JPanel(new GridLayout(4, 2, 4, 4));
		p.add(labName);
		p.add(church);
		p.add(new JLabel("Date opened:"));
		p.add(date);
		JLabel labCatechist = new JLabel("Catechist name:");
		p.add(labCatechist);
		p.add(txtCatechist);

		JPanel pComments = new JPanel(new GridLayout());
		pComments.add(txt);
		JButton btnSave = new JButton("Update record");
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e2) {
				catechistName = txtCatechist.getText().toString();
				/*
				 * continue to perform insertion
				 */
				chrchName = church.getSelectedItem();
				if (!catechistName.isEmpty() & !chrchName.isEmpty()) {
					try {
						String sql = "UPDATE `Outstation` SET `DateOpened`=?,"
								+ "`CatechistName`=?,`Comments`=? WHERE `C_Name`=?";
						stmt = (PreparedStatement) con.prepareStatement(sql);

						stmt.setDate(1, new java.sql.Date(date.getDate().getTime()));
						stmt.setString(2, catechistName.toLowerCase());
						stmt.setString(3, comments.getText().toLowerCase());
						stmt.setString(4, church.getSelectedItem().toLowerCase());
						stmt.executeUpdate();
						stmt.close();
						JOptionPane.showMessageDialog(null, "Data updated successfully.");
					} catch (SQLException e) {
					}

				} else {
					JOptionPane.showMessageDialog(null, "Check for blank fields .");
				}
			}

		});

		Object[] chcrc = { p, pComments };
		JOptionPane.showOptionDialog(this, chcrc, "Select church to edit", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, new Object[] { btnSave }, null);

	}

	/*******************************************
	 * method to delete existing church
	 *******************************************/
	public void deleteChurch() {
		// sql command to delete record
		Choice chrc = new Choice();
		chrc.removeAll();
		ResultSet rs2 = null;
		Choice chcZone=new Choice();
		String sql="select Name from Zone;";
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			rs2 = stmt.executeQuery();
			rs2.beforeFirst();
			while(rs2.next()){
				chcZone.add(rs2.getString("Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object []zon={
				new JLabel("Zone:"),chcZone
		};
		int option=JOptionPane.showConfirmDialog(this,zon, "",
				JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
		
		if(option==JOptionPane.OK_OPTION){
			String sql2 = "select C_Name from Outstation where Zone=?;";
			try {
				// Register jdbc driver
				Class.forName(DRIVER_CLASS);
				// open connection
				con = DriverManager.getConnection(URL, USER, PASSWORD);
				stmt = con.prepareStatement(sql2);
				stmt.setString(1, chcZone.getSelectedItem().toUpperCase().trim());
				rs2 = stmt.executeQuery();
				while (rs2.next()) {
					chrc.add(rs2.getString("C_Name"));
				}
			} catch (SQLException | ClassNotFoundException e) {
			}

			Object[] inputfields = { labName, chrc };
			int option2 = JOptionPane.showConfirmDialog(this, inputfields, "Select church to delete it",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (option2 == JOptionPane.OK_OPTION) {
				chrchName = chrc.getSelectedItem();
				/*
				 * continue to perform deletion
				 */

				try {
					stmt = (PreparedStatement) con.prepareStatement("delete from Outstation where C_Name=?;");
					stmt.setString(1, chrchName);
					stmt.executeUpdate();
					
					
					//update outstation count in zone
					int count=0;
					sql="select count(C_Name) from Outstation where Zone=?;";
					stmt=con.prepareStatement(sql);
					stmt.setString(1, chcZone.getSelectedItem().toUpperCase());
					ResultSet rs=stmt.executeQuery();
					rs.beforeFirst();
					//get the tally
					while(rs.next()){
						count=rs.getInt("count(C_Name)");
					}
					//update it
					sql="update Zone set No_Of_Outstations=? where Name=?;";
					stmt=con.prepareStatement(sql);
					stmt.setInt(1, count);
					stmt.setString(2, chcZone.getSelectedItem().toUpperCase());
					stmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record deleted.");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void showChurches() {
		// sql command to select all records and display them
		parent.removeAll();
		ResultSet rs = null;
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			String sql = "select * from Outstation;";
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery(sql);
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
			t.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			sp = new JScrollPane(t);
			sp.setBorder(BorderFactory.createTitledBorder("LIST OF OUTSTATIONS "));

		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void searchChurch() {
		final JTextField txtname = new JTextField(10);
		Object[] p = { new JLabel("Enter name of outstation:"), txtname };
		int option = JOptionPane.showConfirmDialog(this, p, "Enter new zone", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			// search record and display it if it exists
			parent.removeAll();
			ResultSet rs = null;
			try {
				// Register jdbc driver
				Class.forName(DRIVER_CLASS);
				// open connection
				con = DriverManager.getConnection(URL, USER, PASSWORD);
				String sql = "select * from Outstation where C_Name=? ;";
				stmt = con.prepareStatement(sql);
				stmt.setString(1, txtname.getText().toString().trim().toUpperCase());
				rs = stmt.executeQuery();
				// TO DO:Prepare table to fill values
				DefaultTableModel tab = new DefaultTableModel();
				// get metadata
				ResultSetMetaData rsmt = rs.getMetaData();

				// create array of column names
				int colCount = rsmt.getColumnCount();// number of columns
				String colNames[] = {
						"Outstation Name","Date Opened","Zone","Total Believers","Catechist Name",
						"short notes"
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
				t.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
				sp = new JScrollPane(t);
				sp.setBorder(BorderFactory.createTitledBorder("LIST OF OUTSTATIONS "));
				sp.setBackground(Color.gray);

			} catch (SQLException | ClassNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
	}

}
