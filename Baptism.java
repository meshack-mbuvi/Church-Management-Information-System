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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 * This class deals with all functions for creating,editing, deletin/removing
 * and showing baptisms among other related functions This class has the
 * following methods: 1.createBaptism 2.editBaptism 3.deleteBaptism
 * 4.showBaptism 5.searchBaptism
 * 
 * 
 * @author mbuvi Date :26/10/2016
 *
 */
public class Baptism extends VariablesDefinitions {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3890852477723974641L;
	private JButton btnSave, btnEdit, btnDel;
	JPanel bap, memberDetails, otherDetails;

	private Choice chcChurch;
	private JComboBox<Integer> id;

	JScrollPane sp;

	/**
	 * method declaration
	 */

	// constructor
	public Baptism() {
		// init

		btnSave = new JButton("Save");
		btnSave.setBorder(BorderFactory.createEtchedBorder());
		btnSave.setBackground(Color.lightGray);

		btnEdit = new JButton("Update");
		btnEdit.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		btnEdit.setBackground(Color.lightGray);

		btnDel = new JButton("Delete Record");
		btnDel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
		btnDel.setBackground(Color.lightGray);

		bap = new JPanel();
		memberDetails = new JPanel();
		otherDetails = new JPanel();

		

		// JscrolPane to hold table fro displaying existing records
		sp = new JScrollPane();

		

	}

	// method to create new baptism
	void createBaptism() {
		id = new JComboBox<Integer>();
		AutoCompleteDecorator.decorate(id);
		id.setEditable(true);
		chcChurch = new Choice();
		// remove all items from the main panel
		bap.removeAll();
		memberDetails.removeAll();
		otherDetails.removeAll();
		final JTextField txtFamName = new JTextField(10), baptMin = new JTextField(10);
		txtCName.setEditable(false);
		txtFamName.setEditable(false);
		txtConfirName.setEditable(false);

		JLabel labFamname = new JLabel("Family Name:");
		// get church first

		String sql = "select C_Name from Outstation;";
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);

			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcChurch.addItem(rs.getString("C_Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object[] outstation={
				new JLabel("Outstation:"),
				chcChurch
		};
		
		int option=JOptionPane.showConfirmDialog(this, outstation, "select outstation and press ok", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if(option==JOptionPane.OK_OPTION)
		{

			sql = "select RegNo from Registration where Church=?;";
			try {
				stmt = con.prepareStatement(sql);
			stmt.setString(1, chcChurch.getSelectedItem().toString());
				ResultSet rs = stmt.executeQuery();
				rs.beforeFirst();

				while (rs.next()) {
					id.addItem(rs.getInt("RegNo"));
				}
				try {
					sql = "select BaptismalName,OtherNames,confirmationName from Registration where RegNo=?;";
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, (int) id.getSelectedItem());
					rs = stmt.executeQuery();
					rs.beforeFirst();

					while (rs.next()) {
						txtCName.setText(rs.getString("BaptismalName"));
						txtFamName.setText(rs.getString("OtherNames"));
						txtConfirName.setText(rs.getString("confirmationName"));

					}
				} catch (Exception e2) {
				}
			} catch (Exception e2) {
			}
			if(id.getItemCount()>0)
			{
				id.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						try {
							String sql = "select BaptismalName,OtherNames,confirmationName from Registration where RegNo=?;";
							stmt = con.prepareStatement(sql);
							stmt.setInt(1, (int) id.getSelectedItem());
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();

							while (rs.next()) {
								txtCName.setText(rs.getString("BaptismalName"));
								txtFamName.setText(rs.getString("OtherNames"));
								txtConfirName.setText(rs.getString("confirmationName"));
								
							}
						} catch (Exception e2) {
						}
					}
				});

				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						String sql = "INSERT INTO `Baptism`(`RegNo`, `D.O.B`, `DateOfBaptism`, "
								+ "`BaptMinister`, `ChristianName`, `FamilyName`, `FathersName`,"
								+ " `MothersName`, `Residence`,"
								+ " `PlaceOfBirth`, `Church`, `ConfirmationName`, `ConfirmationMinister`, "
								+ "`DateOfConfirmation`, `NameOfSponsor`, `DateOfF.H.C`) " + "VALUES (?,?,?,?,?,?,?,?,?,?,"
								+ "?,?,?,?,?,?);";

						try {
							stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(id.getSelectedItem().toString()));
							stmt.setDate(2, new java.sql.Date(jfDOBirth.getDate().getTime()));
							stmt.setDate(3, new java.sql.Date(jfDatOfBap.getDate().getTime()));
							stmt.setString(4, baptMin.getText());
							stmt.setString(5, txtCName.getText());
							stmt.setString(6, txtFamName.getText().toString());
							stmt.setString(7, txtFName.getText());
							stmt.setString(8, txtMName.getText());
							stmt.setString(9, txtResidence.getText());
							stmt.setString(10, txtPlace_Of_Birth.getText().toString());
							stmt.setString(11, chcChurch.getSelectedItem());
							stmt.setString(12, txtConfirName.getText());
							stmt.setString(13, txtConfMin.getText());
							stmt.setDate(14, new java.sql.Date(jf_DOfConfi.getDate().getTime()));
							stmt.setString(15, txtSponsor.getText());
							stmt.setDate(16, new java.sql.Date(Jf_FCH.getDate().getTime()));
							stmt.executeUpdate();
							stmt.clearParameters();
							stmt.close();
							txtCName.setText("");
							txtFamName.setText("");
							txtConfirName.setText("");
							jfDOBirth.setDate(null);
							jfDatOfBap.setDate(null);
							baptMin.setText("");
							txtFName.setText("");
							txtMName.setText("");
							txtResidence.setText("");
							txtPlace_Of_Birth.setText("");
							txtConfirName.setText("");
							txtConfMin.setText("");
							jf_DOfConfi.setDate(null);
							txtSponsor.setText("");
							Jf_FCH.setDate(null);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}
					}
				});
				chcChurch.setEnabled(false);
				bap.setLayout(new BorderLayout(3, 3));
				bap.setBorder(BorderFactory.createRaisedSoftBevelBorder());
				// personal details of new baptist
				// memberDetails.setLayout(new GridLayout(3, 4));
				memberDetails.setBorder(BorderFactory.createTitledBorder("Personal Details:"));
				memberDetails.add(new JLabel("Search ID"));
				memberDetails.add(id);
				memberDetails.add(LaBC_Name);
				memberDetails.add(txtCName);
				memberDetails.add(labFamname);
				memberDetails.add(txtFamName);
				memberDetails.add(LaBconfName);
				memberDetails.add(txtConfirName);

				// other details of the baptist
				JPanel temp = new JPanel();
				temp.setBorder(BorderFactory.createTitledBorder("Other Details:"));
				temp.setLayout(new GridLayout(4, 6, 10, 6));
				temp.add(LaB_D_Of_B);
				temp.add(jfDOBirth);
				temp.add(LaBP_OF_birth);
				temp.add(txtPlace_Of_Birth);
				temp.add(LaBchrc);
				temp.add(chcChurch);
				temp.add(LaB_D_Of_Bap);
				temp.add(jfDatOfBap);
				temp.add(new JLabel("Baptism Minister"));
				temp.add(baptMin);
				temp.add(LaB_D_Of_Conf);
				temp.add(jf_DOfConfi);
				temp.add(new JLabel("Confirmation Minister:"));
				temp.add(txtConfMin);
				temp.add(LaBspon);
				temp.add(txtSponsor);
				temp.add(LaBFname);
				temp.add(txtFName);
				temp.add(LaBMName);
				temp.add(txtMName);
				temp.add(LaBRes);
				temp.add(txtResidence);
				temp.add(LaBFCH);
				temp.add(Jf_FCH);

				JPanel panBtns = new JPanel();
				panBtns.add(btnSave);
				JPanel temp1 = new JPanel(new BorderLayout());
				temp1.add(temp, BorderLayout.NORTH);
				temp1.add(panBtns, BorderLayout.CENTER);

				otherDetails.setLayout(new BorderLayout(5, 5));
				otherDetails.add(temp1, BorderLayout.CENTER);

				// otherDetails.add(panBtns,BorderLayout.SOUTH);
				bap.add(memberDetails, BorderLayout.NORTH);
				bap.add(otherDetails, BorderLayout.CENTER);
			}
			else {
				JOptionPane.showMessageDialog(null, "No record found for specified Outstation",null,
						JOptionPane.WARNING_MESSAGE);
			}
			
		}
		else{
			JOptionPane.showMessageDialog(null, "Operation canceled!");
		}

	}

	/**************************************************
	 * Edit existing baptisms
	 **************************************************/
	void editBaptism() {
		id = new JComboBox<Integer>();
		AutoCompleteDecorator.decorate(id);
		id.setEditable(true);
		chcChurch = new Choice();
		// remove all items from the main panel
		bap.removeAll();
		memberDetails.removeAll();
		otherDetails.removeAll();
		final JTextField txtFamName = new JTextField(10), baptMin = new JTextField(10);
		txtCName.setEditable(false);
		txtFamName.setEditable(false);
		txtConfirName.setEditable(false);

		JLabel labFamname = new JLabel("Family Name:");
		// get church first

		String sql = "select C_Name from Outstation;";
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);

			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcChurch.addItem(rs.getString("C_Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Object[] outstation={
				new JLabel("Outstation:"),
				chcChurch
		};
		
		int option=JOptionPane.showConfirmDialog(this, outstation, "select outstation and press ok", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if(option==JOptionPane.OK_OPTION)
		{

			sql = "select RegNo from Baptism where Church=?;";
			try {
				stmt = con.prepareStatement(sql);
			stmt.setString(1, chcChurch.getSelectedItem().toString());
				ResultSet rs = stmt.executeQuery();
				rs.beforeFirst();

				while (rs.next()) {
					id.addItem(rs.getInt("RegNo"));
				}
				try {
					sql = "select `Baptism_No`, `D.O.B`, `DateOfBaptism`, `BaptMinister`, "
							+ "`ChristianName`, `FamilyName`, `FathersName`, `MothersName`, "
							+ "`Residence`, `PlaceOfBirth`, `Church`, `ConfirmationName`,"
							+ " `ConfirmationMinister`, `DateOfConfirmation`, `NameOfSponsor`,"
							+ " `DateOfF.H.C` from Baptism where RegNo=?;";
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, (int) id.getSelectedItem());
					rs = stmt.executeQuery();
					rs.beforeFirst();

					while (rs.next()) {
						txtCName.setText(rs.getString("ChristianName"));
						txtFamName.setText(rs.getString("FamilyName"));
						txtConfirName.setText(rs.getString("ConfirmationName"));
						jfDOBirth.setDate(rs.getDate("D.O.B"));
						jfDatOfBap.setDate(rs.getDate("DateOfBaptism"));
						baptMin.setText(rs.getString("BaptMinister"));
						txtFName.setText(rs.getString("FathersName"));
						txtMName.setText(rs.getString("MothersName"));
						txtResidence.setText(rs.getString("Residence"));
						txtPlace_Of_Birth.setText(rs.getString("PlaceOfBirth"));
						txtConfirName.setText(rs.getString("ConfirmationName"));
						txtConfMin.setText(rs.getString("ConfirmationMinister"));
						jf_DOfConfi.setDate(rs.getDate("DateOfConfirmation"));
						txtSponsor.setText(rs.getString("NameOfSponsor"));
						Jf_FCH.setDate(rs.getDate("DateOfF.H.C"));

					}
				} catch (Exception e2) {
				}
			} catch (Exception e2) {
			}
			if(id.getItemCount()>0)
			{
				id.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {

						try {
							String sql = "select `Baptism_No`, `D.O.B`, `DateOfBaptism`, `BaptMinister`, "
									+ "`ChristianName`, `FamilyName`, `FathersName`, `MothersName`, "
									+ "`Residence`, `PlaceOfBirth`, `Church`, `ConfirmationName`,"
									+ " `ConfirmationMinister`, `DateOfConfirmation`, `NameOfSponsor`,"
									+ " `DateOfF.H.C` from Baptism where RegNo=?;";
							stmt = con.prepareStatement(sql);
							stmt.setInt(1, (int) id.getSelectedItem());
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();

							while (rs.next()) {
								txtCName.setText(rs.getString("ChristianName"));
								txtFamName.setText(rs.getString("FamilyName"));
								txtConfirName.setText(rs.getString("ConfirmationName"));
								jfDOBirth.setDate(rs.getDate("D.O.B"));
								jfDatOfBap.setDate(rs.getDate("DateOfBaptism"));
								baptMin.setText(rs.getString("BaptMinister"));
								txtFName.setText(rs.getString("FathersName"));
								txtMName.setText(rs.getString("MothersName"));
								txtResidence.setText(rs.getString("Residence"));
								txtPlace_Of_Birth.setText(rs.getString("PlaceOfBirth"));
								txtConfirName.setText(rs.getString("ConfirmationName"));
								txtConfMin.setText(rs.getString("ConfirmationMinister"));
								jf_DOfConfi.setDate(rs.getDate("DateOfConfirmation"));
								txtSponsor.setText(rs.getString("NameOfSponsor"));
								Jf_FCH.setDate(rs.getDate("DateOfF.H.C"));

							}
						} catch (Exception e2) {
						}
					}
				});

				btnEdit.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						String sql = "UPDATE `Baptism` SET "
								+ "`D.O.B`=?,"
								+ "`DateOfBaptism`=?,`BaptMinister`=?,"
								+ "`ChristianName`=?,`FamilyName`=?,"
								+ "`FathersName`=?,`MothersName`=?,"
								+ "`Residence`=?,`PlaceOfBirth`=?,"
								+ "`ConfirmationName`=?,"
								+ "`ConfirmationMinister`=?,`DateOfConfirmation`=?,"
								+ "`NameOfSponsor`=?,`DateOfF.H.C`=? where `RegNo`=?;";

						try {
							stmt = con.prepareStatement(sql);
							
							stmt.setDate(1, new java.sql.Date(jfDOBirth.getDate().getTime()));
							stmt.setDate(2, new java.sql.Date(jfDatOfBap.getDate().getTime()));
							stmt.setString(3, baptMin.getText());
							stmt.setString(4, txtCName.getText());
							stmt.setString(5, txtFamName.getText().toString());
							stmt.setString(6, txtFName.getText());
							stmt.setString(7, txtMName.getText());
							stmt.setString(8, txtResidence.getText());
							stmt.setString(9, txtPlace_Of_Birth.getText().toString());
							stmt.setString(10, txtConfirName.getText());
							stmt.setString(11, txtConfMin.getText());
							stmt.setDate(12, new java.sql.Date(jf_DOfConfi.getDate().getTime()));
							stmt.setString(13, txtSponsor.getText());
							stmt.setDate(14, new java.sql.Date(Jf_FCH.getDate().getTime()));
							stmt.setInt(15, Integer.parseInt(id.getSelectedItem().toString()));
							stmt.executeUpdate();
							stmt.clearParameters();
							stmt.close();
							txtCName.setText("");
							txtFamName.setText("");
							txtConfirName.setText("");
							jfDOBirth.setDate(null);
							jfDatOfBap.setDate(null);
							baptMin.setText("");
							txtFName.setText("");
							txtMName.setText("");
							txtResidence.setText("");
							txtPlace_Of_Birth.setText("");
							txtConfirName.setText("");
							txtConfMin.setText("");
							jf_DOfConfi.setDate(null);
							txtSponsor.setText("");
							Jf_FCH.setDate(null);
						} catch (SQLException e1) {
							e1.printStackTrace();
							//JOptionPane.showMessageDialog(null, e1.getMessage());
						}
					}
				});
				chcChurch.setEnabled(false);
				bap.setLayout(new BorderLayout(3, 3));
				bap.setBorder(BorderFactory.createRaisedSoftBevelBorder());
				// personal details of new baptist
				// memberDetails.setLayout(new GridLayout(3, 4));
				memberDetails.setBorder(BorderFactory.createTitledBorder("Personal Details:"));
				memberDetails.add(new JLabel("Search ID"));
				memberDetails.add(id);
				memberDetails.add(LaBC_Name);
				memberDetails.add(txtCName);
				memberDetails.add(labFamname);
				memberDetails.add(txtFamName);
				memberDetails.add(LaBconfName);
				memberDetails.add(txtConfirName);

				// other details of the baptist
				JPanel temp = new JPanel();
				temp.setBorder(BorderFactory.createTitledBorder("Other Details:"));
				temp.setLayout(new GridLayout(4, 6, 10, 6));
				temp.add(LaB_D_Of_B);
				temp.add(jfDOBirth);
				temp.add(LaBP_OF_birth);
				temp.add(txtPlace_Of_Birth);
				temp.add(LaBchrc);
				temp.add(chcChurch);
				temp.add(LaB_D_Of_Bap);
				temp.add(jfDatOfBap);
				temp.add(new JLabel("Baptism Minister"));
				temp.add(baptMin);
				temp.add(LaB_D_Of_Conf);
				temp.add(jf_DOfConfi);
				temp.add(new JLabel("Confirmation Minister:"));
				temp.add(txtConfMin);
				temp.add(LaBspon);
				temp.add(txtSponsor);
				temp.add(LaBFname);
				temp.add(txtFName);
				temp.add(LaBMName);
				temp.add(txtMName);
				temp.add(LaBRes);
				temp.add(txtResidence);
				temp.add(LaBFCH);
				temp.add(Jf_FCH);

				JPanel panBtns = new JPanel();
				panBtns.add(btnEdit);
				JPanel temp1 = new JPanel(new BorderLayout());
				temp1.add(temp, BorderLayout.NORTH);
				temp1.add(panBtns, BorderLayout.CENTER);

				otherDetails.setLayout(new BorderLayout(5, 5));
				otherDetails.add(temp1, BorderLayout.CENTER);

				// otherDetails.add(panBtns,BorderLayout.SOUTH);
				bap.add(memberDetails, BorderLayout.NORTH);
				bap.add(otherDetails, BorderLayout.CENTER);
			}
			else {
				JOptionPane.showMessageDialog(null, "No record found for specified Outstation",null,
						JOptionPane.WARNING_MESSAGE);
			}
			
		}
		else{
			JOptionPane.showMessageDialog(null, "Operation canceled!");
		}

	}

	// method to delete existing baptism
	void deleteBaptism() {
		// remove all items from the main panel
		bap.removeAll();
		memberDetails.removeAll();
		otherDetails.removeAll();
		final JTextField txtFamName = new JTextField(10), baptMin = new JTextField(10);
		txtCName.setEditable(false);
		txtFamName.setEditable(false);
		txtConfirName.setEditable(false);

		JLabel labFamname = new JLabel("Family Name:");
		// get church first

		String sql = "select * from Outstation;";
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);

			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcChurch.addItem(rs.getString("C_Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sql = "select * from Baptism ;";
		try {
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.beforeFirst();

			while (rs.next()) {
				id.addItem(rs.getInt("RegNo"));
			}
			id.setSelectedIndex(0);
			try {
				sql = "select * from Baptism where RegNo=?;";
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, (int) id.getSelectedItem());
				rs = stmt.executeQuery();
				rs.beforeFirst();

				while (rs.next()) {
					txtCName.setText(rs.getString("ChristianName"));
					txtFamName.setText(rs.getString("FamilyName"));
					txtConfirName.setText(rs.getString("ConfirmationName"));
					chcChurch.select(rs.getString("Church"));
					jfDOBirth.setDate(rs.getDate("D.O.B"));
					txtPlace_Of_Birth.setText(rs.getString("PlaceOfBirth"));
					baptMin.setText(rs.getString("BaptMinister"));
					txtConfirName.setText(rs.getString("ConfirmationName"));
					jf_DOfConfi.setDate(rs.getDate("DateOfConfirmation"));
					txtConfMin.setText(rs.getString("ConfirmationMinister"));
					txtSponsor.setText(rs.getString("NameOfSponsor"));
					txtFName.setText(rs.getString("FathersName"));
					txtMName.setText(rs.getString("MothersName"));
					txtResidence.setText(rs.getString("Residence"));
					Jf_FCH.setDate(rs.getDate("DateOfF.H.C"));
					jfDatOfBap.setDate(rs.getDate("DateOfBaptism"));

				}
			} catch (Exception e2) {
				JOptionPane.showMessageDialog(null, e2.getMessage());
			}
		} catch (Exception e2) {
			JOptionPane.showMessageDialog(null, e2.getMessage());
		}
		

		if(id.getItemCount()>0)
		{
			id.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					String sql = "select * from Baptism where RegNo=?";
					try {
						stmt = con.prepareStatement(sql);
						stmt.setInt(1, Integer.parseInt(id.getSelectedItem().toString()));
						ResultSet rs = stmt.executeQuery();
						rs.beforeFirst();
						while (rs.next()) {

							txtCName.setText(rs.getString("ChristianName"));
							txtFamName.setText(rs.getString("FamilyName"));
							txtConfirName.setText(rs.getString("ConfirmationName"));
							chcChurch.select(rs.getString("Church"));
							jfDOBirth.setDate(rs.getDate("D.O.B"));
							txtPlace_Of_Birth.setText(rs.getString("PlaceOfBirth"));
							baptMin.setText(rs.getString("BaptMinister"));
							txtConfirName.setText(rs.getString("ConfirmationName"));
							jf_DOfConfi.setDate(rs.getDate("DateOfConfirmation"));
							txtConfMin.setText(rs.getString("ConfirmationMinister"));
							txtSponsor.setText(rs.getString("NameOfSponsor"));
							txtFName.setText(rs.getString("FathersName"));
							txtMName.setText(rs.getString("MothersName"));
							txtResidence.setText(rs.getString("Residence"));
							Jf_FCH.setDate(rs.getDate("DateOfF.H.C"));
							jfDatOfBap.setDate(rs.getDate("DateOfBaptism"));

						}
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage());
					}
				}
			});
			

			btnDel.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String sql = "delete from Baptism where `RegNo`=? ;";

					try {
						stmt = con.prepareStatement(sql);

						stmt.setInt(1, Integer.parseInt(id.getSelectedItem().toString()));

						stmt.executeUpdate();
						stmt.clearParameters();

						stmt.close();

						txtCName.setText("");
						txtCName.setText("");
						txtFamName.setText("");
						txtConfirName.setText("");
						chcChurch.select("");
						jfDOBirth.setDate(null);
						txtPlace_Of_Birth.setText("");
						baptMin.setText("");
						txtConfirName.setText("");
						jf_DOfConfi.setDate(null);
						txtConfMin.setText("");
						txtSponsor.setText("");
						txtFName.setText("");
						txtMName.setText("");
						txtResidence.setText("");
						Jf_FCH.setDate(null);
					} catch (Exception e2) {
						JOptionPane.showMessageDialog(null, e2.getMessage());
					}
				}
			});
			chcChurch.setEnabled(false);
			bap.setLayout(new BorderLayout(3, 3));
			bap.setBorder(BorderFactory.createRaisedSoftBevelBorder());

			memberDetails.setBorder(BorderFactory.createTitledBorder("Personal Details:"));
			memberDetails.add(new JLabel("Search ID"));
			memberDetails.add(id);
			memberDetails.add(LaBC_Name);
			memberDetails.add(txtCName);
			memberDetails.add(labFamname);
			memberDetails.add(txtFamName);
			memberDetails.add(LaBconfName);
			memberDetails.add(txtConfirName);

			// other details of the baptist
			JPanel temp = new JPanel();
			temp.setBorder(BorderFactory.createTitledBorder("Other Details:"));
			temp.setLayout(new GridLayout(4, 6, 10, 6));
			temp.add(LaB_D_Of_B);
			temp.add(jfDOBirth);
			temp.add(LaBP_OF_birth);
			temp.add(txtPlace_Of_Birth);
			temp.add(LaBchrc);
			temp.add(chcChurch);
			temp.add(LaB_D_Of_Bap);
			temp.add(jfDatOfBap);
			temp.add(new JLabel("Baptism Minister"));
			temp.add(baptMin);
			temp.add(LaB_D_Of_Conf);
			temp.add(jf_DOfConfi);
			temp.add(new JLabel("Confirmation Minister:"));
			temp.add(txtConfMin);
			temp.add(LaBspon);
			temp.add(txtSponsor);
			temp.add(LaBFname);
			temp.add(txtFName);
			temp.add(LaBMName);
			temp.add(txtMName);
			temp.add(LaBRes);
			temp.add(txtResidence);
			temp.add(LaBFCH);
			temp.add(Jf_FCH);

			JPanel panBtns = new JPanel();
			panBtns.add(btnDel);
			JPanel temp1 = new JPanel(new BorderLayout());
			temp1.add(temp, BorderLayout.NORTH);
			temp1.add(panBtns, BorderLayout.CENTER);

			otherDetails.setLayout(new BorderLayout(5, 5));
			otherDetails.add(temp1, BorderLayout.CENTER);

			// otherDetails.add(panBtns,BorderLayout.SOUTH);
			bap.add(memberDetails, BorderLayout.NORTH);
			bap.add(otherDetails, BorderLayout.CENTER);
		}
		else{
			JOptionPane.showMessageDialog(null, "No records found");
		}

	}

	/***************************************************************************
	 * 
	 * List all existing baptism
	 * 
	 ***************************************************************************/
	void showBaptism() {

		String sql = "select * from Baptism ;";
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

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
			sp = new JScrollPane(t);
			String title = "LIST OF ALL BAPTISMS.";
			sp.setBorder(BorderFactory.createTitledBorder(title));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***************************************************************************
	 * 
	 * method to search a particular baptism
	 * 
	 ***************************************************************************/

	void searchBaptism() {
		// populate drop-down list
		String sql = "select * from Outstation;";
		JTextField txtBapId = new JTextField(5);
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

			Object[] obj = { // new JLabel("Church Name:"), chcChurch
					new JLabel("Enter Baptism number here:"), txtBapId };

			// display it for user to select
			int option = JOptionPane.showConfirmDialog(this, obj, "Enter Baptism Number", JOptionPane.OK_OPTION,
					JOptionPane.INFORMATION_MESSAGE);
			if (option == JOptionPane.OK_OPTION) {
				String church = chcChurch.getSelectedItem();
				sql = "select * from Baptism where RegNo=?;";
				try {
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(txtBapId.getText().toString()));
					// stmt.setString(1, church);
					rs = stmt.executeQuery();

					// TO DO:Prepare table to fill values
					DefaultTableModel tab = new DefaultTableModel();

					// get metadata
					java.sql.ResultSetMetaData rsmt = rs.getMetaData();

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
					sp = new JScrollPane(t);
					String title = "TABLE OF BAPTISMS IN " + church.toUpperCase() + "-OUTSTATION";
					sp.setBorder(BorderFactory.createTitledBorder(title));
					// bap.add(sp);
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
