package databaseApp;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
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
public class ZoneCommittee extends VariablesDefinitions{

	JScrollPane sp;
	private JComboBox<Integer> id;
	public ZoneCommittee() {
		// TODO Auto-generated constructor stub
	}
	// search existing zone

		/******************************************************
		 * 
		 * Zone committee
		 *****************************************************/
		public void addZoneCommitte() {
			id = new JComboBox<Integer>();
			AutoCompleteDecorator.decorate(id);
			id.setBackground(Color.white);
			JButton save = new JButton("Save members ");
			final Choice position = new Choice();
			final JTextField txtLevel = new JTextField(10), txtPhone = new JTextField(10);
			final Choice chczon = new Choice();

			try {
				String sql = "select * from Zone;";
				ResultSet rs = null;
				// Register jdbc driver
				Class.forName(DRIVER_CLASS);
				// open connection
				con = DriverManager.getConnection(URL, USER, PASSWORD);
				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();
				while (rs.next()) {
					chczon.add(rs.getString("Name"));
				}
			} catch (SQLException | ClassNotFoundException e) {
			}

			// select zone before proceeding
			Object[] zone = { new JLabel("Zone"), chczon };
			int option = JOptionPane.showConfirmDialog(this, zone, "Choose zone..", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE);

			if (option == JOptionPane.OK_OPTION) {

				try {
					String sql = "select * from Registration where Zone=?;";
					ResultSet rs = null;
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chczon.getSelectedItem());
					rs = stmt.executeQuery();

					while (rs.next()) {
						id.addItem(Integer.parseInt(rs.getString("RegNo")));
					}
					if (id.getItemCount() > 0) {
						sql = "select * from Registration where RegNo=?;";
						stmt = con.prepareStatement(sql);
						stmt.setInt(1, Integer.parseInt(id.getSelectedItem().toString()));

						rs = stmt.executeQuery();
						rs.beforeFirst();
						while (rs.next()) {
							txtFname.setText(rs.getString("BaptismalName"));
							txtSname.setText(rs.getString("OtherNames"));
						}
					}

				} catch (SQLException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}

				id.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						try {
							String sql = "select * from Registration where RegNo=?;";
							stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(id.getSelectedItem().toString()));
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtFname.setText(rs.getString("BaptismalName"));
								txtSname.setText(rs.getString("OtherNames"));

							}

						} catch (SQLException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
					}
				});
				// If save button clicked, get the inputs from the text fields
				save.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// ID = Integer.parseInt(IDs.getSelectedItem().toString());
						int RegNo = Integer.parseInt(id.getSelectedItem().toString());
						/*
						 * Insertion to database
						 */

						if (txtPhone.getText().isEmpty() | txtLevel.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Phone Number or Level cannot be empty");
						} else {
							try {
								// Register jdbc driver
								Class.forName(DRIVER_CLASS);
								// open connection
								con = DriverManager.getConnection(URL, USER, PASSWORD);
								String sql = "INSERT INTO  `Zone committee`(`ID`, `BaptismalName`, `OtherName`, `Phone`, `Position`, `Level`, `Zone`)"
										+ " VALUES (?,?,?,?,?,?,?);";
								stmt = (PreparedStatement) con.prepareStatement(sql);
								stmt.setInt(1, RegNo);
								stmt.setString(2, txtFname.getText().toString().toUpperCase());
								stmt.setString(3, txtSname.getText().toString().toUpperCase());

								stmt.setInt(4, Integer.parseInt(txtPhone.getText().toString()));
								stmt.setString(5, position.getSelectedItem().toUpperCase());
								stmt.setString(6, txtLevel.getText().toUpperCase());
								stmt.setString(7, chczon.getSelectedItem().toUpperCase());
								stmt.executeUpdate();
								showZonecomm();
								id.setSelectedIndex(0);
								txtFname.setText("");
								txtSname.setText("");
								txtLevel.setText("");
								txtPhone.setText("");
								position.select(0);
							} catch (SQLException | ClassNotFoundException e2) {
								JOptionPane.showMessageDialog(null, "Member exists in the committee list!");
							}
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

				Object[] inputfields = { new JLabel("Type ID and press ENTER"), id, labBaptismalName, txtFname,
						labOtherName, txtSname, labPhone, txtPhone, new JLabel("Level"), txtLevel, labPosition, position };

				JOptionPane.showOptionDialog(this, inputfields, "Enter zone committee members", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, new Object[] { save }, null);
			}

		}

		// show zone committee members
		public void showZonecomm() {
			// sql command to select all records and display them
			parent.removeAll();
			ResultSet rs = null;
			try {
				// Register jdbc driver
				Class.forName(DRIVER_CLASS);
				// open connection
				con = DriverManager.getConnection(URL, USER, PASSWORD);
				String sql = "select * from  `Zone committee` ;";
				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery(sql);
				// TO DO:Prepare table to fill values
				DefaultTableModel tab = new DefaultTableModel();

				// get metadata
				ResultSetMetaData rsmt = rs.getMetaData();

				// create array of column names
				int colCount = rsmt.getColumnCount();// number of columns
				String colNames[] = { "Member ID", "Baptismal Name", "Other Names", "Position", "Level", "Zone" };
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
				t.setBorder(BorderFactory.createRaisedSoftBevelBorder());
				sp = new JScrollPane(t);
				sp.setBorder(BorderFactory.createTitledBorder("Table Of Committee" + " members for selected zone"));
				parent.add(sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();

			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		/*********************************************************
		 * edit zone committee
		 *********************************************************/
		public void editZoneCommitte() {
			final JComboBox<Integer> id = new JComboBox<Integer>();
			id.setBackground(Color.white);
			JButton save = new JButton("Update Record ");

			final Choice position = new Choice();
			position.add("CHAIRPERSON");
			position.add("VICE-CHAIRPERSON");
			position.add("SECRETARY");
			position.add("VICE-SECRETARY");
			position.add("TREASURER");
			position.add("MEMBER");
			position.select("MEMBER");
			final JTextField txtLevel = new JTextField(10), txtPhone = new JTextField(10);
			final Choice chczon = new Choice();

			try {
				// Register jdbc driver
				Class.forName(DRIVER_CLASS);
				// open connection
				con = DriverManager.getConnection(URL, USER, PASSWORD);
				String sql = "select * from Zone;";
				ResultSet rs = null;
				stmt = con.prepareStatement(sql);
				rs = stmt.executeQuery();
				while (rs.next()) {
					chczon.add(rs.getString("Name"));
				}
			} catch (SQLException | ClassNotFoundException e) {
			}
			Object[] zone = { new JLabel("Zone"), chczon };

			int option = JOptionPane.showConfirmDialog(this, zone, "select zone..", JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (option == JOptionPane.OK_OPTION) {
				try {
					String sql = "select ID from `Zone committee` where Zone=?;";
					ResultSet rs = null;
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chczon.getSelectedItem().toString().toUpperCase());
					rs = stmt.executeQuery();
					while (rs.next()) {
						id.addItem(Integer.parseInt(rs.getString("ID")));
					}
					
					if(id.getItemCount()>0){
						sql = "select * from `Zone committee` where ID=?;";
						stmt = con.prepareStatement(sql);
						stmt.setInt(1, Integer.parseInt(id.getSelectedItem().toString()));
						rs = stmt.executeQuery();
						rs.beforeFirst();
						while (rs.next()) {
							txtBapName.setText(rs.getString("BaptismalName"));
							txtOtherName.setText(rs.getString("OtherName"));
							txtLevel.setText(rs.getString("Level"));
							txtPhone.setText(rs.getString("Phone"));
							position.select(rs.getString("Position"));
							chczon.select(rs.getString("Zone"));
						}
					}

				} catch (SQLException e) {}
				AutoCompleteDecorator.decorate(id);
				id.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {

						int ids = Integer.parseInt(id.getSelectedItem().toString());
						try {
							String sql = "select * from `Zone committee` where ID=?;";
							stmt = con.prepareStatement(sql);
							stmt.setInt(1, ids);
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtBapName.setText(rs.getString("BaptismalName"));
								txtOtherName.setText(rs.getString("OtherName"));
								txtLevel.setText(rs.getString("Level"));
								txtPhone.setText(rs.getString("Phone"));
								position.select(rs.getString("Position"));
								chczon.select(rs.getString("Zone"));
							}

						} catch (SQLException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, e1.getMessage());

						}
					}
				});
				// If save button clicked, get the inputs from the text fields
				save.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// ID = Integer.parseInt(IDs.getSelectedItem().toString());
						int RegNo = Integer.parseInt(id.getSelectedItem().toString());
						/*
						 * Update Record
						 */
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							String sql = "Update `Zone committee` " + "SET `Phone`=?," + "`Position`=?," + "`Level`=?"
									+ " WHERE `ID`=?;";
							stmt = (PreparedStatement) con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(txtPhone.getText().toString()));
							stmt.setString(2, position.getSelectedItem());
							stmt.setString(3, txtLevel.getText());
							stmt.setInt(4, RegNo);
							stmt.executeUpdate();
							showZonecomm();
							txtBapName.setText("");
							txtOtherName.setText("");
							txtLevel.setText("");
							txtPhone.setText("");
							position.select(0);
						} catch (SQLException | ClassNotFoundException e2) {
							e2.printStackTrace();
						}
					}
				});

				txtBapName.setEditable(false);
				txtOtherName.setEditable(false);
				Object[] inputfields = { new JLabel("Type ID and press ENTER"), id, labBaptismalName, txtBapName,
						labOtherName, txtOtherName, labPhone, txtPhone, new JLabel("Level"), txtLevel, labPosition,
						position };

				JOptionPane.showOptionDialog(this, inputfields, "Enter zone committee members", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, new Object[] { save }, null);
			}
		}

		/***********************************************************
		 * delete zone committee members
		 ***********************************************************/
		public void delZoneComm() {

			final JTextField txtLevel = new JTextField(10), txtPhone = new JTextField(10);
			final JComboBox<Integer> id = new JComboBox<Integer>();
			id.setBackground(Color.white);
			JButton save = new JButton("Delete Record ");
			save.setBackground(Color.RED);
			save.setBorder(BorderFactory.createRaisedSoftBevelBorder());

			final JComboBox<String>  chczon = new JComboBox<String>();
			AutoCompleteDecorator.decorate(chczon);
			AutoCompleteDecorator.decorate(id);
			
			final Choice position = new Choice();
			position.add("CHAIRPERSON");
			position.add("VICE-CHAIRPERSON");
			position.add("SECRETARY");
			position.add("VICE-SECRETARY");
			position.add("TREASURER");
			position.add("MEMBER");
			position.select("MEMBER");

			txtBapName.setEditable(false);
			txtOtherName.setEditable(false);
			txtLevel.setEditable(false);
			txtPhone.setEditable(false);
			position.setEnabled(false);

			try {
				// Register jdbc driver
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
			} catch (SQLException | ClassNotFoundException e) {}

			Object[] zone = { new JLabel("Zone name:"), chczon};

			int option=JOptionPane.showConfirmDialog(this, zone, "select zone and press OK",
					JOptionPane.DEFAULT_OPTION,	JOptionPane.INFORMATION_MESSAGE);
			if(option==JOptionPane.OK_OPTION)
			{
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					String sql = "select ID from `Zone committee` where Zone=?;";
					ResultSet rs = null;
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chczon.getSelectedItem().toString().trim().toUpperCase());
					rs = stmt.executeQuery();
					while (rs.next()) {
						id.addItem(Integer.parseInt(rs.getString("ID")));
					}
					
						if(id.getItemCount()>0){
							sql = "select `BaptismalName`, `OtherName`, `Phone`, `Position`, `Level`, `Zone`"
									+ " from `Zone committee` where ID=?;";
							stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(id.getSelectedItem().toString()));
							rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtBapName.setText(rs.getString("BaptismalName"));
								txtOtherName.setText(rs.getString("OtherName"));
								txtLevel.setText(rs.getString("Level"));
								txtPhone.setText(rs.getString("Phone"));
								position.select(rs.getString("Position"));
								chczon.setSelectedItem(rs.getString("Zone"));
							}
						}

					} catch (SQLException | ClassNotFoundException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, e1.getMessage());

					}
				AutoCompleteDecorator.decorate(id);
				id.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {

						int ids = Integer.parseInt(id.getSelectedItem().toString());
						try {
							String sql = "select `BaptismalName`, `OtherName`, `Phone`, `Position`, `Level`, `Zone`"
									+ " from `Zone committee` where ID=?;";
							stmt = con.prepareStatement(sql);
							stmt.setInt(1, ids);
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtBapName.setText(rs.getString("BaptismalName"));
								txtOtherName.setText(rs.getString("OtherName"));
								txtLevel.setText(rs.getString("Level"));
								txtPhone.setText(rs.getString("Phone"));
								position.select(rs.getString("Position"));
								chczon.setSelectedItem(rs.getString("Zone"));
							}

						} catch (SQLException e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, e1.getMessage());

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
						if(id.getItemCount()>0){
							try {
								// Register jdbc driver
								Class.forName(DRIVER_CLASS);
								// open connection
								con = DriverManager.getConnection(URL, USER, PASSWORD);
								String sql = "delete from `Zone committee`  WHERE `ID`=?;";
								stmt = (PreparedStatement) con.prepareStatement(sql);
								stmt.setInt(1, Integer.parseInt(id.getSelectedItem().toString()));
								stmt.executeUpdate();
								showZonecomm();
								txtBapName.setText("");
								txtOtherName.setText("");
								txtLevel.setText("");
								txtPhone.setText("");
								position.select(0);
								id.removeItem(id.getSelectedItem().toString());
							} catch (SQLException | ClassNotFoundException e2) {
								JOptionPane.showMessageDialog(null, e2.getMessage());
								e2.printStackTrace();
							}
						}
						else {
							JOptionPane.showMessageDialog(null, "Record does not exist.");
						}
					}
				});
				txtBapName.setEditable(false);
				txtOtherName.setEditable(false);
				Object[] inputfields = { new JLabel("Type ID and press ENTER"), id, labBaptismalName,
						txtBapName, labOtherName, txtOtherName, labPhone, txtPhone, new JLabel("Level"), txtLevel, labPosition,
						position };

				JOptionPane.showOptionDialog(this, inputfields, "Enter zone committee members", JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE, null, new Object[] { save }, null);
			}
			
			
		}

}
