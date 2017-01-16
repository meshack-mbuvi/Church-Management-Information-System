package databaseApp;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Contributions extends VariablesDefinitions {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8328076287138742333L;
	private String sql;
	private JTextField txtTotal;
	private JTextField txteaster;
	private JTextField txtdiocese;
	private JTextField txtxmass;
	private JTextField txtTransCode;
	private JTextField txtOtherContr;
	private JComboBox<Integer> regNos;

	// variables for holding
	private double sum = 0, jan = 0, feb = 0, mar = 0, apr = 0, may = 0, jun = 0, jul = 0, aug = 0, sep = 0, oct = 0,
			nov = 0, dec = 0, easter = 0, diocese = 0, xmass = 0;
	private JTextField txtFirstName, txtSecondName, txtChurch;

	// MONTHS
	JTextField txtJan, txtFeb, txtMar, txtApr, txtMay, txtJun, txtJul, txtAug, txtSep, txtOct, txtNov, txtDec;

	private Choice cont_mod, payMode;
	private Choice chcChurch, chcSCC, chcZone;
	// contribution panel
	JPanel contribute;
	// button to save record
	JButton btnSave;

	private Connection con = null;
	JScrollPane sp;

	public Contributions() {

		// open connection

		regNos = new JComboBox<Integer>();
		txtTotal = new JTextField(10);
		txteaster = new JTextField(5);
		txtdiocese = new JTextField(5);
		txtxmass = new JTextField(5);

		txtFirstName = new JTextField(10);
		txtSecondName = new JTextField(10);
		txtChurch = new JTextField(10);
		// MONTHS
		cont_mod = new Choice();
		// choices
		chcChurch = new Choice();
		chcChurch.setBackground(Color.white);
		chcSCC = new Choice();
		chcSCC.setBackground(Color.white);
		payMode = new Choice();
		payMode.setBackground(Color.white);
		chcZone = new Choice();
		chcZone.setBackground(Color.WHITE);

		// contribution panel
		contribute = new JPanel();
		txtJan = new JTextField(5);
		txtFeb = new JTextField(5);
		txtMar = new JTextField(5);
		txtApr = new JTextField(5);

		txtMay = new JTextField(5);
		txtJun = new JTextField(5);
		txtJul = new JTextField(5);
		txtAug = new JTextField(5);

		txtSep = new JTextField(5);
		txtOct = new JTextField(5);
		txtNov = new JTextField(5);
		txtDec = new JTextField(5);
		txtTransCode = new JTextField(10);

		btnSave = new JButton("SAVE");
		// btnSave.setSize(20, 30);

		cont_mod.addItem("Individual wise");
		cont_mod.addItem("SCC wise");
		cont_mod.addItem("Outstation wise");
		cont_mod.addItem("Other Contribution");

		sp = new JScrollPane();
	}

	/*****************************************************************
	 * 
	 * NEW CONTRIBUTION
	 * 
	 *****************************************************************/

	public void newContribution() {
		// It does not hurt to do the following
		txtJan.setEditable(true);
		txtFeb.setEditable(true);
		txtMar.setEditable(true);
		txtApr.setEditable(true);
		txtMay.setEditable(true);
		txtJun.setEditable(true);
		txtJul.setEditable(true);
		txtAug.setEditable(true);
		txtSep.setEditable(true);
		txtOct.setEditable(true);
		txtNov.setEditable(true);
		txtDec.setEditable(true);
		txtTransCode.setEditable(true);

		payMode.setEnabled(true);

		contribute.removeAll();
		if (payMode.getItemCount() > 0) {
			payMode.removeAll();
		}
		Object[] obj = { cont_mod };
		JPanel panAll = new JPanel();
		panAll.setOpaque(false);
		// panel for common contribution fields
		JPanel panMonths = new JPanel(new GridLayout(5, 6, 5, 5));
		panMonths.add(new JLabel("JANUARY:"));
		panMonths.add(txtJan);
		panMonths.add(new JLabel("FEBRUARY:"));
		panMonths.add(txtFeb);
		panMonths.add(new JLabel("MARCH:"));
		panMonths.add(txtMar);
		panMonths.add(new JLabel("APRIL:"));
		panMonths.add(txtApr);
		panMonths.add(new JLabel("MAY:"));
		panMonths.add(txtMay);
		panMonths.add(new JLabel("JUNE:"));
		panMonths.add(txtJun);
		panMonths.add(new JLabel("JULY:"));
		panMonths.add(txtJul);
		panMonths.add(new JLabel("AUGUST:"));
		panMonths.add(txtAug);
		panMonths.add(new JLabel("SEPTEMBER:"));
		panMonths.add(txtSep);
		panMonths.add(new JLabel("OCTOMBER:"));
		panMonths.add(txtOct);
		panMonths.add(new JLabel("NOVEMBER:"));
		panMonths.add(txtNov);
		panMonths.add(new JLabel("DECEMBER:"));
		panMonths.add(txtDec);

		JPanel panOtherXmass = new JPanel(new GridBagLayout());
		panMonths.add(new JLabel("EASTER COLLECTION:"));
		panMonths.add(txteaster);
		panMonths.add(new JLabel("DIOCESE COLLECTION:"));
		panMonths.add(txtdiocese);
		panMonths.add(new JLabel("CHRISTMASS COLLECTION:"));
		panMonths.add(txtxmass);
		JPanel panTotal = new JPanel();
		panTotal.add(new JLabel("Grant Total:"));
		sql = "select * from ModeOfPayment;";
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);

			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				payMode.addItem(rs.getString("Name"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		txtTotal.setEditable(false);
		txtTotal.setText(String.valueOf(0));
		txtTotal.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				findTotal();
			}
		});
		panTotal.add(txtTotal);
		panTotal.add(new JLabel("PAYMENT MODE:"));
		panTotal.add(payMode);
		panTotal.add(new JLabel("TRANSACTION CODE"));
		panTotal.add(txtTransCode);
		// save record

		int option = JOptionPane.showConfirmDialog(this, obj, "Select mode of contribution first..",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		/*******************************************************************************
		 * if selected mode is individual, load panel with individual
		 * registration numbers and autofill other details as names, etc.
		 * 
		 * if scc-wise mode is selected,load panel with all scc and church if
		 * outstation-wise,load panel with outstations
		 *******************************************************************************/

		if (option == JOptionPane.OK_OPTION) {
			// Individual mode of contribution
			if (cont_mod.getSelectedIndex() == 0) {
				// individual-wise mode selected
				btnSave = new JButton("SAVE");
				btnSave.setBorder(BorderFactory.createEtchedBorder());
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						findTotal();
						sql = "INSERT INTO `IndividualContributions`(`RegNo`, `JAN`, `FEB`, `MAR`, "
								+ "`APRI`, `MAY`, `JUNE`, `JULY`,"
								+ " `AUG`, `SEP`, `OCT`, `NOV`, `DECE`, `Easter`, `DioceseCollection`,"
								+ " `Christmas`, `Total`," + " `PaymentMode`, `PaymentCode`,`Received By`)"
								+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(regNos.getSelectedItem().toString()));
							stmt.setDouble(2, jan);
							stmt.setDouble(3, feb);
							stmt.setDouble(4, mar);
							stmt.setDouble(5, apr);
							stmt.setDouble(6, may);
							stmt.setDouble(7, jun);
							stmt.setDouble(8, jul);
							stmt.setDouble(9, aug);
							stmt.setDouble(10, sep);
							stmt.setDouble(11, oct);
							stmt.setDouble(12, nov);
							stmt.setDouble(13, dec);
							stmt.setDouble(14, easter);
							stmt.setDouble(15, diocese);
							stmt.setDouble(16, xmass);
							stmt.setDouble(17, Double.parseDouble(txtTotal.getText()));
							stmt.setString(18, payMode.getSelectedItem());
							stmt.setString(19, txtTransCode.getText());
							stmt.setString(20, USER);

							stmt.executeUpdate();

							regNos.setSelectedIndex(0);
							txtJan.setText("");
							txtFeb.setText("");
							txtMar.setText("");
							txtApr.setText("");
							txtMay.setText("");
							txtJun.setText("");
							txtJul.setText("");
							txtAug.setText("");
							txtSep.setText("");
							txtOct.setText("");
							txtNov.setText("");
							txtDec.setText("");
							txtdiocese.setText("");
							txtxmass.setText("");
							payMode.select(0);
							txtTransCode.setText("");
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
				sql = "SELECT * FROM `Registration` ;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					stmt = con.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					if (regNos.getItemCount() > 0) {
						regNos.removeAllItems();
					}
					while (rs.next()) {
						regNos.addItem(Integer.parseInt(rs.getString("RegNo")));
					}
					regNos.setSelectedIndex(0);
					sql = "SELECT * FROM `Registration` where RegNo=?;";
					try {
						// Register jdbc driver
						Class.forName(DRIVER_CLASS);
						// open connection
						con = DriverManager.getConnection(URL, USER, PASSWORD);

						java.sql.PreparedStatement stmt = con.prepareStatement(sql);
						stmt.setInt(1, Integer.parseInt(regNos.getSelectedItem().toString()));
						rs = stmt.executeQuery();
						rs.beforeFirst();
						while (rs.next()) {
							txtFirstName.setText(rs.getString("BaptismalName"));
							txtSecondName.setText(rs.getString("OtherNames"));
							txtChurch.setText(rs.getString("Church"));
						}

					} catch (Exception e1) {
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				regNos.setEditable(true);
				regNos.setBorder(BorderFactory.createEtchedBorder());
				AutoCompleteDecorator.decorate(regNos);
				regNos.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "SELECT * FROM `Registration` where RegNo=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);

							java.sql.PreparedStatement stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(regNos.getSelectedItem().toString()));
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtFirstName.setText(rs.getString("BaptismalName"));
								txtSecondName.setText(rs.getString("OtherNames"));
								txtChurch.setText(rs.getString("Church"));
							}

						} catch (Exception e1) {
						}
					}
				});
				;
				JPanel userDetails = new JPanel();
				userDetails.setOpaque(false);
				userDetails.add(new JLabel("User Registration Number:"));
				userDetails.add(regNos);
				userDetails.add(new JLabel("First Name:"));
				userDetails.add(txtFirstName);
				userDetails.add(new JLabel("Second Name:"));
				userDetails.add(txtSecondName);
				userDetails.add(new JLabel("Church:"));
				userDetails.add(txtChurch);
				userDetails.setBorder(BorderFactory.createTitledBorder("Member Details"));
				panAll.setLayout(new BorderLayout(10, 4));
				panAll.setBorder(BorderFactory.createTitledBorder("INDIVIDUAL CONTRIBUTIONS"));
				panMonths.setBorder(BorderFactory.createTitledBorder("Enter amount for each field"));
				panAll.add(userDetails, BorderLayout.NORTH);
				panAll.add(panMonths, BorderLayout.CENTER);
				JPanel btns = new JPanel();
				btns.add(btnSave);

				panOtherXmass.setLayout(new BorderLayout());
				panOtherXmass.add(panTotal, BorderLayout.NORTH);
				panOtherXmass.add(btns, BorderLayout.SOUTH);
				panAll.add(panOtherXmass, BorderLayout.SOUTH);

				contribute.add(panAll);

			}

			// SCC mode of contribution
			else if (cont_mod.getSelectedIndex() == 1) {
				if (chcSCC.getItemCount() > 0) {
					chcSCC.removeAll();
				}
				/*
				 * SCC-Wise is selected choose outstation choose scc name
				 * specify type of contribution
				 */
				btnSave = new JButton("SAVE");
				btnSave.setBorder(BorderFactory.createEtchedBorder());
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// get totals first
						findTotal();
						sql = "INSERT INTO `sccContribution`(`SCCName`, `Church`, `JAN`, `FEB`, `MAR`, "
								+ "`APRI`, `MAY`, `JUNE`, `JULY`,"
								+ " `AUG`, `SEP`, `OCT`, `NOV`, `DECE`, `Easter`, `DioceseCollection`,"
								+ " `Christmas`, `Total`," + " `PaymentMode`, `PaymentCode`,`Received By`)"
								+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);
							stmt.setString(1, chcSCC.getSelectedItem());
							stmt.setString(2, chcChurch.getSelectedItem());
							stmt.setDouble(3, jan);
							stmt.setDouble(4, feb);
							stmt.setDouble(5, mar);
							stmt.setDouble(6, apr);
							stmt.setDouble(7, may);
							stmt.setDouble(8, jun);
							stmt.setDouble(9, jul);
							stmt.setDouble(10, aug);
							stmt.setDouble(11, sep);
							stmt.setDouble(12, oct);
							stmt.setDouble(13, nov);
							stmt.setDouble(14, dec);
							stmt.setDouble(15, easter);
							stmt.setDouble(16, diocese);
							stmt.setDouble(17, xmass);
							stmt.setDouble(18, Double.parseDouble(txtTotal.getText()));
							stmt.setString(19, payMode.getSelectedItem());
							stmt.setString(20, txtTransCode.getText());
							stmt.setString(21, USER);

							stmt.executeUpdate();
							txtJan.setText("");
							txtFeb.setText("");
							txtMar.setText("");
							txtApr.setText("");
							txtMay.setText("");
							txtJun.setText("");
							txtJul.setText("");
							txtAug.setText("");
							txtSep.setText("");
							txtOct.setText("");
							txtNov.setText("");
							txtDec.setText("");
							txtdiocese.setText("");
							txtxmass.setText("");
							payMode.select(0);
							txtTransCode.setText("");
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
				sql = "select * from Outstation;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						chcChurch.addItem(rs.getString("C_Name"));
					}
					chcChurch.select(0);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				chcChurch.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						if (chcSCC.getItemCount() > 0) {
							chcSCC.removeAll();
						}
						sql = "select * from SCC where Church=?";
						// Register jdbc driver
						try {
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);

							java.sql.PreparedStatement stmt = con.prepareStatement(sql);
							stmt.setString(1, chcChurch.getSelectedItem());
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();

							while (rs.next()) {
								chcSCC.addItem(rs.getString("Name"));
							}
						} catch (SQLException | ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				sql = "select * from SCC where Church=?";
				// Register jdbc driver
				try {

					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setString(1, chcChurch.getSelectedItem());
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					if (chcSCC.getItemCount() > 0) {
						chcSCC.removeAll();
					}
					while (rs.next()) {
						chcSCC.addItem(rs.getString("Name"));
					}
				} catch (SQLException | ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				panAll.removeAll();
				JPanel userDetails = new JPanel(new BorderLayout());
				JPanel temp = new JPanel(), temp1 = new JPanel();
				temp1.add(new JLabel("Select SCC :"));
				temp1.add(chcSCC);
				temp.add(new JLabel("Select Outstation:"));
				temp.add(chcChurch);
				userDetails.add(temp, BorderLayout.NORTH);

				userDetails.add(temp1, BorderLayout.SOUTH);
				panAll.setLayout(new BorderLayout(10, 1));
				panMonths.setBorder(BorderFactory.createTitledBorder("Enter Fields for months"));
				panAll.add(userDetails, BorderLayout.NORTH);
				panAll.add(panMonths, BorderLayout.CENTER);

				panOtherXmass.setLayout(new BorderLayout());
				panOtherXmass.add(panTotal, BorderLayout.NORTH);
				JPanel btns = new JPanel();
				btns.add(btnSave);
				panOtherXmass.add(btns, BorderLayout.SOUTH);
				panAll.add(panOtherXmass, BorderLayout.SOUTH);
				panAll.setBorder(BorderFactory.createTitledBorder("SCC-WISE CONTRIBUTIONS"));

				contribute.add(panAll);
			}

			else if (cont_mod.getSelectedIndex() == 2) {
				// remove all items from church
				if (chcChurch.getItemCount() > 0) {
					chcChurch.removeAll();
				}

				/*
				 * OUTSTATION-Wise is selected choose outstation and Zone
				 */
				btnSave = new JButton("SAVE");
				btnSave.setBorder(BorderFactory.createEtchedBorder());
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// get totals first
						findTotal();
						sql = "INSERT INTO `OutstationContribution` (`Zone`, `Church`, `JAN`, `FEB`, `MAR`, "
								+ "`APRI`, `MAY`, `JUNE`, `JULY`,"
								+ " `AUG`, `SEP`, `OCT`, `NOV`, `DECE`, `Easter`, `DioceseCollection`,"
								+ " `Christmas`, `Total`," + " `PaymentMode`, `PaymentCode`,`Received By`)"
								+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);
							stmt.setString(1, chcZone.getSelectedItem());
							stmt.setString(2, chcChurch.getSelectedItem());
							stmt.setDouble(3, jan);
							stmt.setDouble(4, feb);
							stmt.setDouble(5, mar);
							stmt.setDouble(6, apr);
							stmt.setDouble(7, may);
							stmt.setDouble(8, jun);
							stmt.setDouble(9, jul);
							stmt.setDouble(10, aug);
							stmt.setDouble(11, sep);
							stmt.setDouble(12, oct);
							stmt.setDouble(13, nov);
							stmt.setDouble(14, dec);
							stmt.setDouble(15, easter);
							stmt.setDouble(16, diocese);
							stmt.setDouble(17, xmass);
							stmt.setDouble(18, Double.parseDouble(txtTotal.getText()));
							stmt.setString(19, payMode.getSelectedItem());
							stmt.setString(20, txtTransCode.getText());
							stmt.setString(21, USER);

							stmt.executeUpdate();

							txtJan.setText("");
							txtFeb.setText("");
							txtMar.setText("");
							txtApr.setText("");
							txtMay.setText("");
							txtJun.setText("");
							txtJul.setText("");
							txtAug.setText("");
							txtSep.setText("");
							txtOct.setText("");
							txtNov.setText("");
							txtDec.setText("");
							txtdiocese.setText("");
							txtxmass.setText("");
							payMode.select(0);
							txtTransCode.setText("");
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
				sql = "select * from Zone;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						chcZone.addItem(rs.getString("Name"));
					}
					chcZone.select(0);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				chcZone.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "select * from Outstation where Zone=?";
						// Register jdbc driver
						try {
							if (chcChurch.getItemCount() > 0) {
								chcChurch.removeAll();
							}
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);

							java.sql.PreparedStatement stmt = con.prepareStatement(sql);
							stmt.setString(1, chcZone.getSelectedItem());
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								chcChurch.addItem(rs.getString("C_Name"));
							}
						} catch (SQLException | ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				sql = "select * from Outstation where Zone=?";
				// Register jdbc driver
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setString(1, chcZone.getSelectedItem());
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						chcChurch.addItem(rs.getString("C_Name"));
					}
				} catch (SQLException | ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				panAll.removeAll();
				JPanel userDetails = new JPanel(new BorderLayout());
				JPanel temp = new JPanel(), temp1 = new JPanel();
				temp1.add(new JLabel("Select Outstation:"));
				temp1.add(chcChurch);
				temp.add(new JLabel("Select Zone:"));
				temp.add(chcZone);
				userDetails.add(temp, BorderLayout.NORTH);

				userDetails.add(temp1, BorderLayout.SOUTH);
				panAll.setLayout(new BorderLayout(10, 1));
				panMonths.setBorder(BorderFactory.createTitledBorder("Enter Fields for months"));
				panAll.add(userDetails, BorderLayout.NORTH);
				panAll.add(panMonths, BorderLayout.CENTER);

				panOtherXmass.setLayout(new BorderLayout());
				panOtherXmass.add(panTotal, BorderLayout.NORTH);
				JPanel btns = new JPanel();
				btns.add(btnSave);
				panOtherXmass.add(btns, BorderLayout.SOUTH);
				panAll.add(panOtherXmass, BorderLayout.SOUTH);
				panAll.setBorder(BorderFactory.createTitledBorder("OUTSTATION-WISE CONTRIBUTIONS"));

				contribute.add(panAll);
			}
			else if(cont_mod.getSelectedIndex() == 3){
				txtOtherContr=new JTextField(15);
				/*
				 * Other contribution 
				 */
				btnSave = new JButton("SAVE");
				btnSave.setBorder(BorderFactory.createEtchedBorder());
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if(!(txtOtherContr.getText().toString().trim().isEmpty())){
							// get totals first
							findTotal();
							sql = "INSERT INTO  `Other Contribution` (`CampaignName`, `JAN`, `FEB`, `MAR`, "
									+ "`APRI`, `MAY`, `JUNE`, `JULY`,"
									+ " `AUG`, `SEP`, `OCT`, `NOV`, `DECE`, `Easter`, `DioceseCollection`,"
									+ " `Christmas`, `Total`," + " `PaymentMode`, `PaymentCode`,`Received By`)"
									+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							try {
								// Register jdbc driver
								Class.forName(DRIVER_CLASS);
								// open connection
								con = DriverManager.getConnection(URL, USER, PASSWORD);
								stmt = con.prepareStatement(sql);
								stmt.setString(1, txtOtherContr.getText());
								stmt.setDouble(2, jan);
								stmt.setDouble(3, feb);
								stmt.setDouble(4, mar);
								stmt.setDouble(5, apr);
								stmt.setDouble(6, may);
								stmt.setDouble(7, jun);
								stmt.setDouble(8, jul);
								stmt.setDouble(9, aug);
								stmt.setDouble(10, sep);
								stmt.setDouble(11, oct);
								stmt.setDouble(12, nov);
								stmt.setDouble(13, dec);
								stmt.setDouble(14, easter);
								stmt.setDouble(15, diocese);
								stmt.setDouble(16, xmass);
								stmt.setDouble(17, Double.parseDouble(txtTotal.getText()));
								stmt.setString(18, payMode.getSelectedItem());
								stmt.setString(19, txtTransCode.getText());
								stmt.setString(20, USER);

								stmt.executeUpdate();

								txtOtherContr.setText("");
								txtJan.setText("");
								txtFeb.setText("");
								txtMar.setText("");
								txtApr.setText("");
								txtMay.setText("");
								txtJun.setText("");
								txtJul.setText("");
								txtAug.setText("");
								txtSep.setText("");
								txtOct.setText("");
								txtNov.setText("");
								txtDec.setText("");
								txtdiocese.setText("");
								txtxmass.setText("");
								payMode.select(0);
								txtTransCode.setText("");
							} catch (Exception e2) {
								e2.printStackTrace();
							}
						}
					}
				});
				}
				panAll.removeAll();
				JPanel userDetails = new JPanel(new BorderLayout());
				JPanel temp = new JPanel(), temp1 = new JPanel();
				temp1.add(new JLabel("Campaign Name:"));
				temp1.add(txtOtherContr);
				userDetails.add(temp, BorderLayout.NORTH);

				userDetails.add(temp1, BorderLayout.SOUTH);
				panAll.setLayout(new BorderLayout(10, 1));
				panMonths.setBorder(BorderFactory.createTitledBorder("Fields for monthly Contributions"));
				panAll.add(userDetails, BorderLayout.NORTH);
				panAll.add(panMonths, BorderLayout.CENTER);

				panOtherXmass.setLayout(new BorderLayout());
				panOtherXmass.add(panTotal, BorderLayout.NORTH);
				JPanel btns = new JPanel();
				btns.add(btnSave);
				panOtherXmass.add(btns, BorderLayout.SOUTH);
				panAll.add(panOtherXmass, BorderLayout.SOUTH);
				panAll.setBorder(BorderFactory.createTitledBorder("FORM FOR OTHER CONTRIBUTIONS"));

				contribute.add(panAll);
			
		}
	}

	/*****************************************************************
	 * 
	 * DELETE CONTRIBUTION
	 * 
	 *****************************************************************/
	void delContribution() {
		contribute.removeAll();
		final JComboBox<Integer> cont_IDS = new JComboBox<Integer>();
		cont_IDS.setBackground(Color.white);
		final JTextField txtReg = new JTextField(10);
		if (payMode.getItemCount() > 0) {
			payMode.removeAll();
		}
		Object[] obj = { cont_mod };
		JPanel panAll = new JPanel();
		panAll.setOpaque(false);
		// panel for common contribution fields
		JPanel panMonths = new JPanel(new GridLayout(5, 6, 4, 4));
		panMonths.add(new JLabel("JANUARY:"));
		panMonths.add(txtJan);
		panMonths.add(new JLabel("FEBRUARY:"));
		panMonths.add(txtFeb);
		panMonths.add(new JLabel("MARCH:"));
		panMonths.add(txtMar);
		panMonths.add(new JLabel("APRIL:"));
		panMonths.add(txtApr);
		panMonths.add(new JLabel("MAY:"));
		panMonths.add(txtMay);
		panMonths.add(new JLabel("JUNE:"));
		panMonths.add(txtJun);
		panMonths.add(new JLabel("JULY:"));
		panMonths.add(txtJul);
		panMonths.add(new JLabel("AUGUST:"));
		panMonths.add(txtAug);
		panMonths.add(new JLabel("SEPTEMBER:"));
		panMonths.add(txtSep);
		panMonths.add(new JLabel("OCTOMBER:"));
		panMonths.add(txtOct);
		panMonths.add(new JLabel("NOVEMBER:"));
		panMonths.add(txtNov);
		panMonths.add(new JLabel("DECEMBER:"));
		panMonths.add(txtDec);

		JPanel panOtherXmass = new JPanel(new GridBagLayout());
		panMonths.add(new JLabel("EASTER COLLECTION:"));
		panMonths.add(txteaster);
		panMonths.add(new JLabel("DIOCESE COLLECTION:"));
		panMonths.add(txtdiocese);
		panMonths.add(new JLabel("CHRISTMASS COLLECTION:"));
		panMonths.add(txtxmass);
		JPanel panTotal = new JPanel();
		panTotal.add(new JLabel("Grant Total:"));
		sql = "select * from ModeOfPayment;";
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);

			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				payMode.addItem(rs.getString("Name"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		txtTotal.setEditable(false);

		panTotal.add(txtTotal);
		panTotal.add(new JLabel("PAYMENT MODE:"));
		panTotal.add(payMode);
		panTotal.add(new JLabel("TRANSACTION CODE"));
		panTotal.add(txtTransCode);
		// save record

		int option = JOptionPane.showConfirmDialog(this, obj, "Select contribution mode to edit..",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			/*
			 * if selected mode is individual, load panel with individual
			 * registration numbers and autofill other details as names, etc.
			 * 
			 * if scc-wise mode is selected,load panel with all scc and church
			 * if outstation-wise,load panel with outstations
			 */

			if (cont_mod.getSelectedIndex() == 0) {
				// individual-wise mode selected
				// set reg and names to be uneditable
				txtReg.setEditable(false);
				txtFirstName.setEditable(false);
				txtSecondName.setEditable(false);

				txtReg.setEditable(false);
				txtFirstName.setEditable(false);
				txtSecondName.setEditable(false);
				txtJan.setEditable(false);
				txtFeb.setEditable(false);
				txtMar.setEditable(false);
				txtApr.setEditable(false);
				txtMay.setEditable(false);
				txtJun.setEditable(false);
				txtJul.setEditable(false);
				txtAug.setEditable(false);
				txtSep.setEditable(false);
				txtOct.setEditable(false);
				txtNov.setEditable(false);
				txtDec.setEditable(false);
				txteaster.setEditable(false);
				txtdiocese.setEditable(false);
				txtxmass.setEditable(false);
				txtTotal.setEditable(false);
				payMode.setEnabled(false);
				txtTransCode.setEditable(false);

				btnSave = new JButton("DELETE RECORD");
				btnSave.setBorder(BorderFactory.createEtchedBorder());
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						sql = "DELETE FROM `IndividualContributions` WHERE  `CONTRIBUTION_ID`=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);

							stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));

							stmt.executeUpdate();
							txtJan.setText("");
							txtFeb.setText("");
							txtMar.setText("");
							txtApr.setText("");
							txtMay.setText("");
							txtJun.setText("");
							txtJul.setText("");
							txtAug.setText("");
							txtSep.setText("");
							txtOct.setText("");
							txtNov.setText("");
							txtDec.setText("");
							txtdiocese.setText("");
							txtxmass.setText("");
							payMode.select(0);
							txtTransCode.setText("");
							cont_IDS.removeItem(cont_IDS.getSelectedItem());

						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
				sql = "SELECT * FROM `IndividualContributions`;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					stmt = con.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					if (regNos.getItemCount() > 0) {
						regNos.removeAllItems();
					}
					while (rs.next()) {
						cont_IDS.addItem(Integer.parseInt(rs.getString("CONTRIBUTION_ID")));
					}

					cont_IDS.setSelectedIndex(0);

				} catch (Exception e) {
					e.printStackTrace();
				}
				sql = "select `Registration`.BaptismalName," + "`Registration`.OtherNames, `IndividualContributions`.* "
						+ "from `IndividualContributions` JOIN `Registration` "
						+ "USING(`RegNo`) where CONTRIBUTION_ID=?;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtReg.setText(rs.getString("RegNo"));
						txtFirstName.setText(rs.getString("BaptismalName"));
						txtSecondName.setText(rs.getString("OtherNames"));
						txtJan.setText(String.valueOf(rs.getDouble("JAN")));
						txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
						txtMar.setText(String.valueOf(rs.getDouble("MAR")));
						txtApr.setText(String.valueOf(rs.getDouble("APRI")));
						txtMay.setText(String.valueOf(rs.getDouble("MAY")));
						txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
						txtJul.setText(String.valueOf(rs.getDouble("JULY")));
						txtAug.setText(String.valueOf(rs.getDouble("AUG")));
						txtSep.setText(String.valueOf(rs.getDouble("SEP")));
						txtOct.setText(String.valueOf(rs.getDouble("OCT")));
						txtNov.setText(String.valueOf(rs.getDouble("NOV")));
						txtDec.setText(String.valueOf(rs.getDouble("DECE")));
						txteaster.setText(String.valueOf(rs.getDouble("Easter")));
						txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
						txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
						txtTotal.setText(String.valueOf(rs.getDouble("Total")));
						payMode.select((rs.getString("PaymentMode")));
						txtTransCode.setText(rs.getString("PaymentCode"));

					}

				} catch (Exception e1) {
				}
				cont_IDS.setEditable(true);
				cont_IDS.setBorder(BorderFactory.createEtchedBorder());
				AutoCompleteDecorator.decorate(cont_IDS);
				cont_IDS.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "select `Registration`.BaptismalName,"
								+ "`Registration`.OtherNames, `IndividualContributions`.* "
								+ "from `IndividualContributions` JOIN `Registration` "
								+ "USING(`RegNo`) where CONTRIBUTION_ID=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);

							java.sql.PreparedStatement stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtReg.setText(rs.getString("RegNo"));
								txtFirstName.setText(rs.getString("BaptismalName"));
								txtSecondName.setText(rs.getString("OtherNames"));
								txtJan.setText(String.valueOf(rs.getDouble("JAN")));
								txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
								txtMar.setText(String.valueOf(rs.getDouble("MAR")));
								txtApr.setText(String.valueOf(rs.getDouble("APRI")));
								txtMay.setText(String.valueOf(rs.getDouble("MAY")));
								txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
								txtJul.setText(String.valueOf(rs.getDouble("JULY")));
								txtAug.setText(String.valueOf(rs.getDouble("AUG")));
								txtSep.setText(String.valueOf(rs.getDouble("SEP")));
								txtOct.setText(String.valueOf(rs.getDouble("OCT")));
								txtNov.setText(String.valueOf(rs.getDouble("NOV")));
								txtDec.setText(String.valueOf(rs.getDouble("DECE")));
								txteaster.setText(String.valueOf(rs.getDouble("Easter")));
								txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
								txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
								txtTotal.setText(String.valueOf(rs.getDouble("Total")));
								payMode.select((rs.getString("PaymentMode")));
								txtTransCode.setText(rs.getString("PaymentCode"));
							}

						} catch (Exception e1) {
						}
					}
				});

				// add fields to display
				JPanel userDetails = new JPanel();
				userDetails.setOpaque(false);
				userDetails.add(new JLabel("Contribution ID:"));
				userDetails.add(cont_IDS);
				userDetails.add(new JLabel("Reg Number:"));

				userDetails.add(txtReg);
				userDetails.add(new JLabel("First Name:"));

				userDetails.add(txtFirstName);
				userDetails.add(new JLabel("Second Name:"));
				userDetails.add(txtSecondName);
				userDetails.setBorder(BorderFactory.createTitledBorder("User Details"));
				panAll.setLayout(new BorderLayout(10, 1));
				panAll.setBorder(BorderFactory.createTitledBorder("INDIVIDUAL CONTRIBUTIONS"));
				panMonths.setBorder(BorderFactory.createTitledBorder("monthly contributions"));
				panAll.add(userDetails, BorderLayout.NORTH);
				panAll.add(panMonths, BorderLayout.CENTER);
				JPanel btns = new JPanel();
				btns.add(btnSave);

				panOtherXmass.setLayout(new BorderLayout());
				panOtherXmass.add(panTotal, BorderLayout.NORTH);
				panOtherXmass.add(btns, BorderLayout.SOUTH);
				panAll.add(panOtherXmass, BorderLayout.SOUTH);

				contribute.add(panAll);

			}
			/***************************************
			 * SCC-contributions selected
			 **************************************/
			else if (cont_mod.getSelectedIndex() == 1) {
				final JTextField txtScc = new JTextField(10);
				final JTextField txtChurch = new JTextField(10);
				txtScc.setEditable(false);
				txtChurch.setEditable(false);
				txtJan.setEditable(false);
				txtFeb.setEditable(false);
				txtMar.setEditable(false);
				txtApr.setEditable(false);
				txtMay.setEditable(false);
				txtJun.setEditable(false);
				txtJul.setEditable(false);
				txtAug.setEditable(false);
				txtSep.setEditable(false);
				txtOct.setEditable(false);
				txtNov.setEditable(false);
				txtDec.setEditable(false);
				txteaster.setEditable(false);
				txtdiocese.setEditable(false);
				txtxmass.setEditable(false);
				txtTotal.setEditable(false);
				payMode.setEnabled(false);
				txtTransCode.setEditable(false);
				/*
				 * SCC-Wise is selected choose outstation choose scc name
				 * specify type of contribution
				 */
				btnSave = new JButton("DELETE RECORD");
				btnSave.setBorder(BorderFactory.createEtchedBorder());
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						sql = "delete from  `sccContribution` where `CONTRIBUTION_ID`=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);

							stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));

							stmt.executeUpdate();
							txtJan.setText("");
							txtFeb.setText("");
							txtMar.setText("");
							txtApr.setText("");
							txtMay.setText("");
							txtJun.setText("");
							txtJul.setText("");
							txtAug.setText("");
							txtSep.setText("");
							txtOct.setText("");
							txtNov.setText("");
							txtDec.setText("");
							txtdiocese.setText("");
							txtxmass.setText("");
							payMode.select(0);
							txtTransCode.setText("");

							cont_IDS.removeItem(cont_IDS.getSelectedItem());
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
				sql = "SELECT * FROM `sccContribution`;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					stmt = con.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					if (cont_IDS.getItemCount() > 0) {
						cont_IDS.removeAllItems();
					}
					while (rs.next()) {
						cont_IDS.addItem(Integer.parseInt(rs.getString("CONTRIBUTION_ID")));
					}
					cont_IDS.setSelectedIndex(0);

				} catch (Exception e) {
					e.printStackTrace();
				}

				sql = "SELECT * FROM `sccContribution` where CONTRIBUTION_ID=?;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtScc.setText(rs.getString("SCCName"));
						txtChurch.setText(rs.getString("Church"));
						txtJan.setText(String.valueOf(rs.getDouble("JAN")));
						txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
						txtMar.setText(String.valueOf(rs.getDouble("MAR")));
						txtApr.setText(String.valueOf(rs.getDouble("APRI")));
						txtMay.setText(String.valueOf(rs.getDouble("MAY")));
						txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
						txtJul.setText(String.valueOf(rs.getDouble("JULY")));
						txtAug.setText(String.valueOf(rs.getDouble("AUG")));
						txtSep.setText(String.valueOf(rs.getDouble("SEP")));
						txtOct.setText(String.valueOf(rs.getDouble("OCT")));
						txtNov.setText(String.valueOf(rs.getDouble("NOV")));
						txtDec.setText(String.valueOf(rs.getDouble("DECE")));
						txteaster.setText(String.valueOf(rs.getDouble("Easter")));
						txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
						txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
						txtTotal.setText(String.valueOf(rs.getDouble("Total")));
						payMode.select((rs.getString("PaymentMode")));
						txtTransCode.setText(rs.getString("PaymentCode"));

					}

				} catch (Exception e1) {
				}
				cont_IDS.setEditable(true);
				cont_IDS.setBorder(BorderFactory.createEtchedBorder());
				AutoCompleteDecorator.decorate(cont_IDS);
				cont_IDS.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "SELECT * FROM `sccContribution` where CONTRIBUTION_ID=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);

							java.sql.PreparedStatement stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtScc.setText(rs.getString("SCCName"));
								txtChurch.setText(rs.getString("Church"));
								txtJan.setText(String.valueOf(rs.getDouble("JAN")));
								txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
								txtMar.setText(String.valueOf(rs.getDouble("MAR")));
								txtApr.setText(String.valueOf(rs.getDouble("APRI")));
								txtMay.setText(String.valueOf(rs.getDouble("MAY")));
								txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
								txtJul.setText(String.valueOf(rs.getDouble("JULY")));
								txtAug.setText(String.valueOf(rs.getDouble("AUG")));
								txtSep.setText(String.valueOf(rs.getDouble("SEP")));
								txtOct.setText(String.valueOf(rs.getDouble("OCT")));
								txtNov.setText(String.valueOf(rs.getDouble("NOV")));
								txtDec.setText(String.valueOf(rs.getDouble("DECE")));
								txteaster.setText(String.valueOf(rs.getDouble("Easter")));
								txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
								txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
								txtTotal.setText(String.valueOf(rs.getDouble("Total")));
								payMode.select((rs.getString("PaymentMode")));
								txtTransCode.setText(rs.getString("PaymentCode"));
							}

						} catch (Exception e1) {
						}
					}
				});

				// add fields to display
				JPanel userDetails = new JPanel();
				userDetails.setOpaque(false);
				userDetails.add(new JLabel("Contribution ID:"));
				userDetails.add(cont_IDS);
				userDetails.add(new JLabel("SCC Name:"));

				userDetails.add(txtScc);
				userDetails.add(new JLabel("Church:"));

				userDetails.add(txtChurch);
				panAll.setLayout(new BorderLayout(10, 1));
				panAll.setBorder(BorderFactory.createTitledBorder("SCC CONTRIBUTIONS"));
				panMonths.setBorder(BorderFactory.createTitledBorder("Enter amount for each field"));
				panAll.add(userDetails, BorderLayout.NORTH);
				panAll.add(panMonths, BorderLayout.CENTER);
				JPanel btns = new JPanel();
				btns.add(btnSave);

				panOtherXmass.setLayout(new BorderLayout());
				panOtherXmass.add(panTotal, BorderLayout.NORTH);
				panOtherXmass.add(btns, BorderLayout.SOUTH);
				panAll.add(panOtherXmass, BorderLayout.SOUTH);

				contribute.add(panAll);

			}

			/******************************************
			 * Outstation contribution selected
			 ******************************************/
			else if (cont_mod.getSelectedIndex() == 2) {
				final JTextField txtZone = new JTextField(10);
				final JTextField txtChurch = new JTextField(10);
				txtZone.setEditable(false);
				txtChurch.setEditable(false);
				txtJan.setEditable(false);
				txtFeb.setEditable(false);
				txtMar.setEditable(false);
				txtApr.setEditable(false);
				txtMay.setEditable(false);
				txtJun.setEditable(false);
				txtJul.setEditable(false);
				txtAug.setEditable(false);
				txtSep.setEditable(false);
				txtOct.setEditable(false);
				txtNov.setEditable(false);
				txtDec.setEditable(false);
				txteaster.setEditable(false);
				txtdiocese.setEditable(false);
				txtxmass.setEditable(false);
				txtTotal.setEditable(false);
				payMode.setEnabled(false);
				txtTransCode.setEditable(false);
				/*
				 * SCC-Wise is selected choose outstation choose scc name
				 * specify type of contribution
				 */
				btnSave = new JButton("DELETE RECORD");
				btnSave.setBorder(BorderFactory.createEtchedBorder());
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						sql = "delete from `OutstationContribution`  where `CONTRIBUTION_ID`=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);

							stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));

							stmt.executeUpdate();
							txtJan.setText("");
							txtFeb.setText("");
							txtMar.setText("");
							txtApr.setText("");
							txtMay.setText("");
							txtJun.setText("");
							txtJul.setText("");
							txtAug.setText("");
							txtSep.setText("");
							txtOct.setText("");
							txtNov.setText("");
							txtDec.setText("");
							txtdiocese.setText("");
							txtxmass.setText("");
							payMode.select(0);
							txtTransCode.setText("");

							// Remove the contribution ID from the combobox
							cont_IDS.removeItem(cont_IDS.getSelectedItem());

						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
				sql = "SELECT * FROM `OutstationContribution`;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					stmt = con.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					if (cont_IDS.getItemCount() > 0) {
						cont_IDS.removeAllItems();
					}
					while (rs.next()) {
						cont_IDS.addItem(Integer.parseInt(rs.getString("CONTRIBUTION_ID")));
					}
					cont_IDS.setSelectedIndex(0);

				} catch (Exception e) {
					e.printStackTrace();
				}

				sql = "SELECT * FROM `OutstationContribution` where CONTRIBUTION_ID=?;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtZone.setText(rs.getString("Zone"));
						txtChurch.setText(rs.getString("Church"));
						txtJan.setText(String.valueOf(rs.getDouble("JAN")));
						txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
						txtMar.setText(String.valueOf(rs.getDouble("MAR")));
						txtApr.setText(String.valueOf(rs.getDouble("APRI")));
						txtMay.setText(String.valueOf(rs.getDouble("MAY")));
						txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
						txtJul.setText(String.valueOf(rs.getDouble("JULY")));
						txtAug.setText(String.valueOf(rs.getDouble("AUG")));
						txtSep.setText(String.valueOf(rs.getDouble("SEP")));
						txtOct.setText(String.valueOf(rs.getDouble("OCT")));
						txtNov.setText(String.valueOf(rs.getDouble("NOV")));
						txtDec.setText(String.valueOf(rs.getDouble("DECE")));
						txteaster.setText(String.valueOf(rs.getDouble("Easter")));
						txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
						txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
						txtTotal.setText(String.valueOf(rs.getDouble("Total")));
						payMode.select((rs.getString("PaymentMode")));
						txtTransCode.setText(rs.getString("PaymentCode"));

					}

				} catch (Exception e1) {
				}
				cont_IDS.setEditable(true);
				cont_IDS.setBorder(BorderFactory.createEtchedBorder());
				AutoCompleteDecorator.decorate(cont_IDS);
				cont_IDS.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "SELECT * FROM `OutstationContribution` where CONTRIBUTION_ID=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);

							java.sql.PreparedStatement stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtZone.setText(rs.getString("Zone"));
								txtChurch.setText(rs.getString("Church"));
								txtJan.setText(String.valueOf(rs.getDouble("JAN")));
								txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
								txtMar.setText(String.valueOf(rs.getDouble("MAR")));
								txtApr.setText(String.valueOf(rs.getDouble("APRI")));
								txtMay.setText(String.valueOf(rs.getDouble("MAY")));
								txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
								txtJul.setText(String.valueOf(rs.getDouble("JULY")));
								txtAug.setText(String.valueOf(rs.getDouble("AUG")));
								txtSep.setText(String.valueOf(rs.getDouble("SEP")));
								txtOct.setText(String.valueOf(rs.getDouble("OCT")));
								txtNov.setText(String.valueOf(rs.getDouble("NOV")));
								txtDec.setText(String.valueOf(rs.getDouble("DECE")));
								txteaster.setText(String.valueOf(rs.getDouble("Easter")));
								txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
								txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
								txtTotal.setText(String.valueOf(rs.getDouble("Total")));
								payMode.select((rs.getString("PaymentMode")));
								txtTransCode.setText(rs.getString("PaymentCode"));
							}

						} catch (Exception e1) {
						}
					}
				});

				// add fields to display
				JPanel userDetails = new JPanel();
				userDetails.setOpaque(false);
				userDetails.add(new JLabel("Contribution ID:"));
				userDetails.add(cont_IDS);
				userDetails.add(new JLabel("SCC Name:"));
				userDetails.add(txtZone);

				userDetails.add(new JLabel("Church:"));
				userDetails.add(txtChurch);

				panAll.setLayout(new BorderLayout(10, 1));
				panAll.setBorder(BorderFactory.createTitledBorder("OUTSTATION CONTRIBUTIONS"));
				panMonths.setBorder(BorderFactory.createTitledBorder("Enter amount for each field"));
				panAll.add(userDetails, BorderLayout.NORTH);
				panAll.add(panMonths, BorderLayout.CENTER);
				JPanel btns = new JPanel();
				btns.add(btnSave);

				panOtherXmass.setLayout(new BorderLayout());
				panOtherXmass.add(panTotal, BorderLayout.NORTH);
				panOtherXmass.add(btns, BorderLayout.SOUTH);
				panAll.add(panOtherXmass, BorderLayout.SOUTH);

				contribute.add(panAll);
				

			}
			else if(cont_mod.getSelectedIndex() == 3){
				txtOtherContr=new JTextField(15);
				txtOtherContr.setEditable(true);
				txtJan.setEditable(false);
				txtFeb.setEditable(false);
				txtMar.setEditable(false);
				txtApr.setEditable(false);
				txtMay.setEditable(false);
				txtJun.setEditable(false);
				txtJul.setEditable(false);
				txtAug.setEditable(false);
				txtSep.setEditable(false);
				txtOct.setEditable(false);
				txtNov.setEditable(false);
				txtDec.setEditable(false);
				txteaster.setEditable(false);
				txtdiocese.setEditable(false);
				txtxmass.setEditable(false);
				txtTotal.setEditable(false);
				payMode.setEnabled(false);
				txtTransCode.setEditable(false);
				/*
				 * SCC-Wise is selected choose outstation choose scc name
				 * specify type of contribution
				 */
				btnSave = new JButton("DELETE RECORD");
				btnSave.setBorder(BorderFactory.createEtchedBorder());
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						sql = "delete from `Other Contribution`  where `CONTRIBUTION_ID`=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);

							stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));

							stmt.executeUpdate();
							txtJan.setText("");
							txtFeb.setText("");
							txtMar.setText("");
							txtApr.setText("");
							txtMay.setText("");
							txtJun.setText("");
							txtJul.setText("");
							txtAug.setText("");
							txtSep.setText("");
							txtOct.setText("");
							txtNov.setText("");
							txtDec.setText("");
							txtdiocese.setText("");
							txtxmass.setText("");
							payMode.select(0);
							txtTransCode.setText("");
							txtOtherContr.setText("");

							// Remove the contribution ID from the combobox
							cont_IDS.removeItem(cont_IDS.getSelectedItem());

						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
				sql = "SELECT * FROM `Other Contribution`;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					stmt = con.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					if (cont_IDS.getItemCount() > 0) {
						cont_IDS.removeAllItems();
					}
					while (rs.next()) {
						cont_IDS.addItem(Integer.parseInt(rs.getString("CONTRIBUTION_ID")));
					}
					cont_IDS.setSelectedIndex(0);

				} catch (Exception e) {
					e.printStackTrace();
				}

				sql = "SELECT * FROM `Other Contribution` where CONTRIBUTION_ID=?;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtOtherContr.setText(rs.getString("CampaignName"));
						txtJan.setText(String.valueOf(rs.getDouble("JAN")));
						txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
						txtMar.setText(String.valueOf(rs.getDouble("MAR")));
						txtApr.setText(String.valueOf(rs.getDouble("APRI")));
						txtMay.setText(String.valueOf(rs.getDouble("MAY")));
						txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
						txtJul.setText(String.valueOf(rs.getDouble("JULY")));
						txtAug.setText(String.valueOf(rs.getDouble("AUG")));
						txtSep.setText(String.valueOf(rs.getDouble("SEP")));
						txtOct.setText(String.valueOf(rs.getDouble("OCT")));
						txtNov.setText(String.valueOf(rs.getDouble("NOV")));
						txtDec.setText(String.valueOf(rs.getDouble("DECE")));
						txteaster.setText(String.valueOf(rs.getDouble("Easter")));
						txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
						txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
						txtTotal.setText(String.valueOf(rs.getDouble("Total")));
						payMode.select((rs.getString("PaymentMode")));
						txtTransCode.setText(rs.getString("PaymentCode"));

					}

				} catch (Exception e1) {
					e1.printStackTrace();
				}
				cont_IDS.setEditable(true);
				cont_IDS.setBorder(BorderFactory.createEtchedBorder());
				AutoCompleteDecorator.decorate(cont_IDS);
				cont_IDS.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "SELECT * FROM `Other Contribution` where CONTRIBUTION_ID=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);

							java.sql.PreparedStatement stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtOtherContr.setText(rs.getString("CampaignName"));
								txtChurch.setText(rs.getString("Church"));
								txtJan.setText(String.valueOf(rs.getDouble("JAN")));
								txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
								txtMar.setText(String.valueOf(rs.getDouble("MAR")));
								txtApr.setText(String.valueOf(rs.getDouble("APRI")));
								txtMay.setText(String.valueOf(rs.getDouble("MAY")));
								txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
								txtJul.setText(String.valueOf(rs.getDouble("JULY")));
								txtAug.setText(String.valueOf(rs.getDouble("AUG")));
								txtSep.setText(String.valueOf(rs.getDouble("SEP")));
								txtOct.setText(String.valueOf(rs.getDouble("OCT")));
								txtNov.setText(String.valueOf(rs.getDouble("NOV")));
								txtDec.setText(String.valueOf(rs.getDouble("DECE")));
								txteaster.setText(String.valueOf(rs.getDouble("Easter")));
								txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
								txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
								txtTotal.setText(String.valueOf(rs.getDouble("Total")));
								payMode.select((rs.getString("PaymentMode")));
								txtTransCode.setText(rs.getString("PaymentCode"));
							}

						} catch (Exception e1) {
						}
					}
				});

				// add fields to display
				JPanel userDetails = new JPanel();
				userDetails.setOpaque(false);
				userDetails.add(new JLabel("Contribution ID:"));
				userDetails.add(cont_IDS);
				userDetails.add(new JLabel("Campaign Name:"));
				userDetails.add(txtOtherContr);

				panAll.setLayout(new BorderLayout(10, 1));
				panAll.setBorder(BorderFactory.createTitledBorder("OTHER CONTRIBUTIONS"));
				panMonths.setBorder(BorderFactory.createTitledBorder("Enter amount for each field"));
				panAll.add(userDetails, BorderLayout.NORTH);
				panAll.add(panMonths, BorderLayout.CENTER);
				JPanel btns = new JPanel();
				btns.add(btnSave);

				panOtherXmass.setLayout(new BorderLayout());
				panOtherXmass.add(panTotal, BorderLayout.NORTH);
				panOtherXmass.add(btns, BorderLayout.SOUTH);
				panAll.add(panOtherXmass, BorderLayout.SOUTH);

				contribute.add(panAll);
				
			}
		}

	}

	/*****************************************************************
	 * 
	 * EDIT CONTRIBUTION
	 * 
	 *****************************************************************/
	void editContribution() {
		// It does not hurt to do the following
		txtJan.setEditable(true);
		txtFeb.setEditable(true);
		txtMar.setEditable(true);
		txtApr.setEditable(true);
		txtMay.setEditable(true);
		txtJun.setEditable(true);
		txtJul.setEditable(true);
		txtAug.setEditable(true);
		txtSep.setEditable(true);
		txtOct.setEditable(true);
		txtNov.setEditable(true);
		txtDec.setEditable(true);
		txtTransCode.setEditable(true);

		payMode.setEnabled(true);
		contribute.removeAll();
		final JComboBox<Integer> cont_IDS = new JComboBox<Integer>();
		cont_IDS.setBackground(Color.white);
		final JTextField txtReg = new JTextField(10);
		if (payMode.getItemCount() > 0) {
			payMode.removeAll();
		}
		Object[] obj = { cont_mod };
		JPanel panAll = new JPanel();
		panAll.setOpaque(false);
		// panel for common contribution fields
		JPanel panMonths = new JPanel(new GridLayout(5, 6, 4, 4));
		panMonths.add(new JLabel("JANUARY:"));
		panMonths.add(txtJan);
		panMonths.add(new JLabel("FEBRUARY:"));
		panMonths.add(txtFeb);
		panMonths.add(new JLabel("MARCH:"));
		panMonths.add(txtMar);
		panMonths.add(new JLabel("APRIL:"));
		panMonths.add(txtApr);
		panMonths.add(new JLabel("MAY:"));
		panMonths.add(txtMay);
		panMonths.add(new JLabel("JUNE:"));
		panMonths.add(txtJun);
		panMonths.add(new JLabel("JULY:"));
		panMonths.add(txtJul);
		panMonths.add(new JLabel("AUGUST:"));
		panMonths.add(txtAug);
		panMonths.add(new JLabel("SEPTEMBER:"));
		panMonths.add(txtSep);
		panMonths.add(new JLabel("OCTOMBER:"));
		panMonths.add(txtOct);
		panMonths.add(new JLabel("NOVEMBER:"));
		panMonths.add(txtNov);
		panMonths.add(new JLabel("DECEMBER:"));
		panMonths.add(txtDec);

		JPanel panOtherXmass = new JPanel(new GridBagLayout());
		panMonths.add(new JLabel("EASTER COLLECTION:"));
		panMonths.add(txteaster);
		panMonths.add(new JLabel("DIOCESE COLLECTION:"));
		panMonths.add(txtdiocese);
		panMonths.add(new JLabel("CHRISTMASS COLLECTION:"));
		panMonths.add(txtxmass);
		JPanel panTotal = new JPanel();
		panTotal.add(new JLabel("Grant Total:"));
		sql = "select * from ModeOfPayment;";
		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);

			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				payMode.addItem(rs.getString("Name"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		txtTotal.setEditable(false);
		txtTotal.setText(String.valueOf(0));
		txtTotal.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				findTotal();
			}
		});
		panTotal.add(txtTotal);
		panTotal.add(new JLabel("PAYMENT MODE:"));
		panTotal.add(payMode);
		panTotal.add(new JLabel("TRANSACTION CODE"));
		panTotal.add(txtTransCode);
		// save record

		int option = JOptionPane.showConfirmDialog(this, obj, "Select contribution mode to edit..",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			/*
			 * if selected mode is individual, load panel with individual
			 * registration numbers and autofill other details as names, etc.
			 * 
			 * if scc-wise mode is selected,load panel with all scc and church
			 * if outstation-wise,load panel with outstations
			 */

			if (cont_mod.getSelectedIndex() == 0) {
				// individual-wise mode selected
				// set reg and names to be uneditable
				txtReg.setEditable(false);
				txtFirstName.setEditable(false);
				txtSecondName.setEditable(false);
				btnSave = new JButton("EDIT");
				btnSave.setBorder(BorderFactory.createEtchedBorder());
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						findTotal();
						sql = "UPDATE `IndividualContributions` SET " + "`JAN`=?,`FEB`=?,`MAR`=?,"
								+ "`APRI`=?,`MAY`=?,`JUNE`=?,`JULY`=?," + "`AUG`=?,`SEP`=?,`OCT`=?,"
								+ "`NOV`=?,`DECE`=?,`Easter`=?," + "`DioceseCollection`=?,`Christmas`=?,"
								+ "`Total`=?,`PaymentMode`=?,"
								+ "`PaymentCode`=?,`Received By`=? where `CONTRIBUTION_ID`=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);

							stmt.setDouble(1, jan);
							stmt.setDouble(2, feb);
							stmt.setDouble(3, mar);
							stmt.setDouble(4, apr);
							stmt.setDouble(5, may);
							stmt.setDouble(6, jun);
							stmt.setDouble(7, jul);
							stmt.setDouble(8, aug);
							stmt.setDouble(9, sep);
							stmt.setDouble(10, oct);
							stmt.setDouble(11, nov);
							stmt.setDouble(12, dec);
							stmt.setDouble(13, easter);
							stmt.setDouble(14, diocese);
							stmt.setDouble(15, xmass);
							stmt.setDouble(16, Double.parseDouble(txtTotal.getText()));
							stmt.setString(17, payMode.getSelectedItem());
							stmt.setString(18, txtTransCode.getText().toUpperCase());
							stmt.setString(19, USER.toUpperCase());
							stmt.setInt(20, Integer.parseInt(cont_IDS.getSelectedItem().toString()));

							stmt.executeUpdate();
							txtJan.setText("");
							txtFeb.setText("");
							txtMar.setText("");
							txtApr.setText("");
							txtMay.setText("");
							txtJun.setText("");
							txtJul.setText("");
							txtAug.setText("");
							txtSep.setText("");
							txtOct.setText("");
							txtNov.setText("");
							txtDec.setText("");
							txtdiocese.setText("");
							txtxmass.setText("");
							payMode.select(0);
							txtTransCode.setText("");

							txtApr.setText("");
							txtMay.setText("");
							txtJun.setText("");
							txtJul.setText("");
							txtAug.setText("");
							txtSep.setText("");
							txtOct.setText("");
							txtNov.setText("");
							txtDec.setText("");
							txtdiocese.setText("");
							txtxmass.setText("");
							payMode.select(0);
							txtTransCode.setText("");
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
				sql = "SELECT CONTRIBUTION_ID FROM `IndividualContributions`;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					stmt = con.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					if (regNos.getItemCount() > 0) {
						regNos.removeAllItems();
					}
					while (rs.next()) {
						cont_IDS.addItem(Integer.parseInt(rs.getString("CONTRIBUTION_ID")));
					}
					cont_IDS.setSelectedIndex(0);

				} catch (Exception e) {
					e.printStackTrace();
				}
				sql = "select `Registration`.BaptismalName," + "`Registration`.OtherNames, `IndividualContributions`.* "
						+ "from `IndividualContributions` JOIN `Registration` "
						+ "USING(`RegNo`) where CONTRIBUTION_ID=?;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtReg.setText(rs.getString("RegNo"));
						txtFirstName.setText(rs.getString("BaptismalName"));
						txtSecondName.setText(rs.getString("OtherNames"));
						txtJan.setText(String.valueOf(rs.getDouble("JAN")));
						txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
						txtMar.setText(String.valueOf(rs.getDouble("MAR")));
						txtApr.setText(String.valueOf(rs.getDouble("APRI")));
						txtMay.setText(String.valueOf(rs.getDouble("MAY")));
						txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
						txtJul.setText(String.valueOf(rs.getDouble("JULY")));
						txtAug.setText(String.valueOf(rs.getDouble("AUG")));
						txtSep.setText(String.valueOf(rs.getDouble("SEP")));
						txtOct.setText(String.valueOf(rs.getDouble("OCT")));
						txtNov.setText(String.valueOf(rs.getDouble("NOV")));
						txtDec.setText(String.valueOf(rs.getDouble("DECE")));
						txteaster.setText(String.valueOf(rs.getDouble("Easter")));
						txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
						txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
						txtTotal.setText(String.valueOf(rs.getDouble("Total")));
						payMode.select((rs.getString("PaymentMode")));
						txtTransCode.setText(rs.getString("PaymentCode"));

					}

				} catch (Exception e1) {
				}
				cont_IDS.setEditable(true);
				cont_IDS.setBorder(BorderFactory.createEtchedBorder());
				AutoCompleteDecorator.decorate(cont_IDS);
				cont_IDS.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "select `Registration`.BaptismalName,"
								+ "`Registration`.OtherNames, `IndividualContributions`.* "
								+ "from `IndividualContributions` JOIN `Registration` "
								+ "USING(`RegNo`) where CONTRIBUTION_ID=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);

							java.sql.PreparedStatement stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtReg.setText(rs.getString("RegNo"));
								txtFirstName.setText(rs.getString("BaptismalName"));
								txtSecondName.setText(rs.getString("OtherNames"));
								txtJan.setText(String.valueOf(rs.getDouble("JAN")));
								txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
								txtMar.setText(String.valueOf(rs.getDouble("MAR")));
								txtApr.setText(String.valueOf(rs.getDouble("APRI")));
								txtMay.setText(String.valueOf(rs.getDouble("MAY")));
								txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
								txtJul.setText(String.valueOf(rs.getDouble("JULY")));
								txtAug.setText(String.valueOf(rs.getDouble("AUG")));
								txtSep.setText(String.valueOf(rs.getDouble("SEP")));
								txtOct.setText(String.valueOf(rs.getDouble("OCT")));
								txtNov.setText(String.valueOf(rs.getDouble("NOV")));
								txtDec.setText(String.valueOf(rs.getDouble("DECE")));
								txteaster.setText(String.valueOf(rs.getDouble("Easter")));
								txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
								txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
								txtTotal.setText(String.valueOf(rs.getDouble("Total")));
								payMode.select((rs.getString("PaymentMode")));
								txtTransCode.setText(rs.getString("PaymentCode"));
							}

						} catch (Exception e1) {
						}
					}
				});

				// add fields to display
				JPanel userDetails = new JPanel();
				userDetails.setOpaque(false);
				userDetails.add(new JLabel("Contribution ID:"));
				userDetails.add(cont_IDS);
				userDetails.add(new JLabel("Reg Number:"));

				userDetails.add(txtReg);
				userDetails.add(new JLabel("First Name:"));

				userDetails.add(txtFirstName);
				userDetails.add(new JLabel("Second Name:"));
				userDetails.add(txtSecondName);
				userDetails.setBorder(BorderFactory.createTitledBorder("User Details"));
				panAll.setLayout(new BorderLayout(10, 1));
				panAll.setBorder(BorderFactory.createTitledBorder("INDIVIDUAL CONTRIBUTIONS"));
				panMonths.setBorder(BorderFactory.createTitledBorder("Enter Fields for months"));
				panAll.add(userDetails, BorderLayout.NORTH);
				panAll.add(panMonths, BorderLayout.CENTER);
				JPanel btns = new JPanel();
				btns.add(btnSave);

				panOtherXmass.setLayout(new BorderLayout());
				panOtherXmass.add(panTotal, BorderLayout.NORTH);
				panOtherXmass.add(btns, BorderLayout.SOUTH);
				panAll.add(panOtherXmass, BorderLayout.SOUTH);

				contribute.add(panAll);

			}
			/***************************************
			 * SCC-contributions selected
			 **************************************/
			else if (cont_mod.getSelectedIndex() == 1) {
				final JTextField txtScc = new JTextField(10);
				final JTextField txtChurch = new JTextField(10);
				txtScc.setEditable(false);
				txtChurch.setEditable(false);
				/*
				 * SCC-Wise is selected choose outstation choose scc name
				 * specify type of contribution
				 */
				btnSave = new JButton("EDIT");
				btnSave.setBorder(BorderFactory.createEtchedBorder());
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						sql = "UPDATE `sccContribution` SET " + "`JAN`=?,`FEB`=?,`MAR`=?,"
								+ "`APRI`=?,`MAY`=?,`JUNE`=?,`JULY`=?," + "`AUG`=?,`SEP`=?,`OCT`=?,"
								+ "`NOV`=?,`DECE`=?,`Easter`=?," + "`DioceseCollection`=?,`Christmas`=?,"
								+ "`Total`=?,`PaymentMode`=?,"
								+ "`PaymentCode`=?,`Received By`=? where `CONTRIBUTION_ID`=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);

							stmt.setDouble(1, jan);
							stmt.setDouble(2, feb);
							stmt.setDouble(3, mar);
							stmt.setDouble(4, apr);
							stmt.setDouble(5, may);
							stmt.setDouble(6, jun);
							stmt.setDouble(7, jul);
							stmt.setDouble(8, aug);
							stmt.setDouble(9, sep);
							stmt.setDouble(10, oct);
							stmt.setDouble(11, nov);
							stmt.setDouble(12, dec);
							stmt.setDouble(13, easter);
							stmt.setDouble(14, diocese);
							stmt.setDouble(15, xmass);
							stmt.setDouble(16, Double.parseDouble(txtTotal.getText()));
							stmt.setString(17, payMode.getSelectedItem());
							stmt.setString(18, txtTransCode.getText().toUpperCase());
							stmt.setString(19, USER);
							stmt.setInt(20, Integer.parseInt(cont_IDS.getSelectedItem().toString()));

							stmt.executeUpdate();
							txtJan.setText("");
							txtFeb.setText("");
							txtMar.setText("");
							txtApr.setText("");
							txtMay.setText("");
							txtJun.setText("");
							txtJul.setText("");
							txtAug.setText("");
							txtSep.setText("");
							txtOct.setText("");
							txtNov.setText("");
							txtDec.setText("");
							txtdiocese.setText("");
							txtxmass.setText("");
							payMode.select(0);
							txtTransCode.setText("");
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
				sql = "SELECT * FROM `sccContribution`;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					stmt = con.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					if (cont_IDS.getItemCount() > 0) {
						cont_IDS.removeAllItems();
					}
					while (rs.next()) {
						cont_IDS.addItem(Integer.parseInt(rs.getString("CONTRIBUTION_ID")));
					}
					cont_IDS.setSelectedIndex(0);

				} catch (Exception e) {
					e.printStackTrace();
				}

				sql = "SELECT * FROM `sccContribution` where CONTRIBUTION_ID=?;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtScc.setText(rs.getString("SCCName"));
						txtChurch.setText(rs.getString("Church"));
						txtJan.setText(String.valueOf(rs.getDouble("JAN")));
						txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
						txtMar.setText(String.valueOf(rs.getDouble("MAR")));
						txtApr.setText(String.valueOf(rs.getDouble("APRI")));
						txtMay.setText(String.valueOf(rs.getDouble("MAY")));
						txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
						txtJul.setText(String.valueOf(rs.getDouble("JULY")));
						txtAug.setText(String.valueOf(rs.getDouble("AUG")));
						txtSep.setText(String.valueOf(rs.getDouble("SEP")));
						txtOct.setText(String.valueOf(rs.getDouble("OCT")));
						txtNov.setText(String.valueOf(rs.getDouble("NOV")));
						txtDec.setText(String.valueOf(rs.getDouble("DECE")));
						txteaster.setText(String.valueOf(rs.getDouble("Easter")));
						txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
						txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
						txtTotal.setText(String.valueOf(rs.getDouble("Total")));
						payMode.select((rs.getString("PaymentMode")));
						txtTransCode.setText(rs.getString("PaymentCode"));

					}

				} catch (Exception e1) {
				}
				cont_IDS.setEditable(true);
				cont_IDS.setBorder(BorderFactory.createEtchedBorder());
				AutoCompleteDecorator.decorate(cont_IDS);
				cont_IDS.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "SELECT * FROM `sccContribution` where CONTRIBUTION_ID=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);

							java.sql.PreparedStatement stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtScc.setText(rs.getString("SCCName"));
								txtChurch.setText(rs.getString("Church"));
								txtJan.setText(String.valueOf(rs.getDouble("JAN")));
								txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
								txtMar.setText(String.valueOf(rs.getDouble("MAR")));
								txtApr.setText(String.valueOf(rs.getDouble("APRI")));
								txtMay.setText(String.valueOf(rs.getDouble("MAY")));
								txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
								txtJul.setText(String.valueOf(rs.getDouble("JULY")));
								txtAug.setText(String.valueOf(rs.getDouble("AUG")));
								txtSep.setText(String.valueOf(rs.getDouble("SEP")));
								txtOct.setText(String.valueOf(rs.getDouble("OCT")));
								txtNov.setText(String.valueOf(rs.getDouble("NOV")));
								txtDec.setText(String.valueOf(rs.getDouble("DECE")));
								txteaster.setText(String.valueOf(rs.getDouble("Easter")));
								txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
								txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
								txtTotal.setText(String.valueOf(rs.getDouble("Total")));
								payMode.select((rs.getString("PaymentMode")));
								txtTransCode.setText(rs.getString("PaymentCode"));
							}

						} catch (Exception e1) {
						}
					}
				});

				// add fields to display
				JPanel userDetails = new JPanel();
				userDetails.setOpaque(false);
				userDetails.add(new JLabel("Contribution ID:"));
				userDetails.add(cont_IDS);
				userDetails.add(new JLabel("SCC Name:"));

				userDetails.add(txtScc);
				userDetails.add(new JLabel("Church:"));

				userDetails.add(txtChurch);
				panAll.setLayout(new BorderLayout(10, 1));
				panAll.setBorder(BorderFactory.createTitledBorder("SCC CONTRIBUTIONS"));
				panMonths.setBorder(BorderFactory.createTitledBorder("Enter amount for each field"));
				panAll.add(userDetails, BorderLayout.NORTH);
				panAll.add(panMonths, BorderLayout.CENTER);
				JPanel btns = new JPanel();
				btns.add(btnSave);

				panOtherXmass.setLayout(new BorderLayout());
				panOtherXmass.add(panTotal, BorderLayout.NORTH);
				panOtherXmass.add(btns, BorderLayout.SOUTH);
				panAll.add(panOtherXmass, BorderLayout.SOUTH);

				contribute.add(panAll);

			}

			/******************************************
			 * Outstation contribution selected
			 ******************************************/
			else if (cont_mod.getSelectedIndex() == 2) {
				final JTextField txtZone = new JTextField(10);
				final JTextField txtChurch = new JTextField(10);
				txtZone.setEditable(false);
				txtChurch.setEditable(false);
				/*
				 * SCC-Wise is selected choose outstation choose scc name
				 * specify type of contribution
				 */
				btnSave = new JButton("EDIT");
				btnSave.setBorder(BorderFactory.createEtchedBorder());
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						findTotal();
						sql = "UPDATE `OutstationContribution` SET " + "`JAN`=?,`FEB`=?,`MAR`=?,"
								+ "`APRI`=?,`MAY`=?,`JUNE`=?,`JULY`=?," + "`AUG`=?,`SEP`=?,`OCT`=?,"
								+ "`NOV`=?,`DECE`=?,`Easter`=?," + "`DioceseCollection`=?,`Christmas`=?,"
								+ "`Total`=?,`PaymentMode`=?,"
								+ "`PaymentCode`=?,`Received By`=? where `CONTRIBUTION_ID`=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);

							stmt.setDouble(1, jan);
							stmt.setDouble(2, feb);
							stmt.setDouble(3, mar);
							stmt.setDouble(4, apr);
							stmt.setDouble(5, may);
							stmt.setDouble(6, jun);
							stmt.setDouble(7, jul);
							stmt.setDouble(8, aug);
							stmt.setDouble(9, sep);
							stmt.setDouble(10, oct);
							stmt.setDouble(11, nov);
							stmt.setDouble(12, dec);
							stmt.setDouble(13, easter);
							stmt.setDouble(14, diocese);
							stmt.setDouble(15, xmass);
							stmt.setDouble(16, Double.parseDouble(txtTotal.getText()));
							stmt.setString(17, payMode.getSelectedItem());
							stmt.setString(18, txtTransCode.getText().toUpperCase());
							stmt.setString(19, USER);
							stmt.setInt(20, Integer.parseInt(cont_IDS.getSelectedItem().toString()));

							stmt.executeUpdate();
							txtJan.setText("");
							txtFeb.setText("");
							txtMar.setText("");
							txtApr.setText("");
							txtMay.setText("");
							txtJun.setText("");
							txtJul.setText("");
							txtAug.setText("");
							txtSep.setText("");
							txtOct.setText("");
							txtNov.setText("");
							txtDec.setText("");
							txtdiocese.setText("");
							txtxmass.setText("");
							payMode.select(0);
							txtTransCode.setText("");
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
				sql = "SELECT * FROM `OutstationContribution`;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					stmt = con.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					if (cont_IDS.getItemCount() > 0) {
						cont_IDS.removeAllItems();
					}
					while (rs.next()) {
						cont_IDS.addItem(Integer.parseInt(rs.getString("CONTRIBUTION_ID")));
					}
					cont_IDS.setSelectedIndex(0);

				} catch (Exception e) {
					e.printStackTrace();
				}

				sql = "SELECT * FROM `OutstationContribution` where CONTRIBUTION_ID=?;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtZone.setText(rs.getString("Zone"));
						txtChurch.setText(rs.getString("Church"));
						txtJan.setText(String.valueOf(rs.getDouble("JAN")));
						txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
						txtMar.setText(String.valueOf(rs.getDouble("MAR")));
						txtApr.setText(String.valueOf(rs.getDouble("APRI")));
						txtMay.setText(String.valueOf(rs.getDouble("MAY")));
						txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
						txtJul.setText(String.valueOf(rs.getDouble("JULY")));
						txtAug.setText(String.valueOf(rs.getDouble("AUG")));
						txtSep.setText(String.valueOf(rs.getDouble("SEP")));
						txtOct.setText(String.valueOf(rs.getDouble("OCT")));
						txtNov.setText(String.valueOf(rs.getDouble("NOV")));
						txtDec.setText(String.valueOf(rs.getDouble("DECE")));
						txteaster.setText(String.valueOf(rs.getDouble("Easter")));
						txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
						txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
						txtTotal.setText(String.valueOf(rs.getDouble("Total")));
						payMode.select((rs.getString("PaymentMode")));
						txtTransCode.setText(rs.getString("PaymentCode"));

					}

				} catch (Exception e1) {
				}
				cont_IDS.setEditable(true);
				cont_IDS.setBorder(BorderFactory.createEtchedBorder());
				AutoCompleteDecorator.decorate(cont_IDS);
				cont_IDS.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "SELECT * FROM `OutstationContribution` where CONTRIBUTION_ID=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);

							java.sql.PreparedStatement stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtZone.setText(rs.getString("Zone"));
								txtChurch.setText(rs.getString("Church"));
								txtJan.setText(String.valueOf(rs.getDouble("JAN")));
								txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
								txtMar.setText(String.valueOf(rs.getDouble("MAR")));
								txtApr.setText(String.valueOf(rs.getDouble("APRI")));
								txtMay.setText(String.valueOf(rs.getDouble("MAY")));
								txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
								txtJul.setText(String.valueOf(rs.getDouble("JULY")));
								txtAug.setText(String.valueOf(rs.getDouble("AUG")));
								txtSep.setText(String.valueOf(rs.getDouble("SEP")));
								txtOct.setText(String.valueOf(rs.getDouble("OCT")));
								txtNov.setText(String.valueOf(rs.getDouble("NOV")));
								txtDec.setText(String.valueOf(rs.getDouble("DECE")));
								txteaster.setText(String.valueOf(rs.getDouble("Easter")));
								txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
								txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
								txtTotal.setText(String.valueOf(rs.getDouble("Total")));
								payMode.select((rs.getString("PaymentMode")));
								txtTransCode.setText(rs.getString("PaymentCode"));
							}

						} catch (Exception e1) {
						}
					}
				});

				// add fields to display
				JPanel userDetails = new JPanel();
				userDetails.setOpaque(false);
				userDetails.add(new JLabel("Contribution ID:"));
				userDetails.add(cont_IDS);
				userDetails.add(new JLabel("SCC Name:"));
				userDetails.add(txtZone);

				userDetails.add(new JLabel("Church:"));
				userDetails.add(txtChurch);

				panAll.setLayout(new BorderLayout(10, 1));
				panAll.setBorder(BorderFactory.createTitledBorder("OUTSTATION CONTRIBUTIONS"));
				panMonths.setBorder(BorderFactory.createTitledBorder("Enter amount for each field"));
				panAll.add(userDetails, BorderLayout.NORTH);
				panAll.add(panMonths, BorderLayout.CENTER);
				JPanel btns = new JPanel();
				btns.add(btnSave);

				panOtherXmass.setLayout(new BorderLayout());
				panOtherXmass.add(panTotal, BorderLayout.NORTH);
				panOtherXmass.add(btns, BorderLayout.SOUTH);
				panAll.add(panOtherXmass, BorderLayout.SOUTH);

				contribute.add(panAll);

			}
			else if(cont_mod.getSelectedIndex() == 3){
				// other-contribution selected
				// set campaign name to uneditable
				txtOtherContr=new JTextField(15);
				txtOtherContr.setEditable(false);
				btnSave = new JButton("EDIT");
				btnSave.setBorder(BorderFactory.createEtchedBorder());
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						findTotal();
						sql = "UPDATE `Other Contribution` SET " + "`JAN`=?,`FEB`=?,`MAR`=?,"
								+ "`APRI`=?,`MAY`=?,`JUNE`=?,`JULY`=?," + "`AUG`=?,`SEP`=?,`OCT`=?,"
								+ "`NOV`=?,`DECE`=?,`Easter`=?," + "`DioceseCollection`=?,`Christmas`=?,"
								+ "`Total`=?,`PaymentMode`=?,"
								+ "`PaymentCode`=?,`Received By`=? where `CONTRIBUTION_ID`=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);

							stmt.setDouble(1, jan);
							stmt.setDouble(2, feb);
							stmt.setDouble(3, mar);
							stmt.setDouble(4, apr);
							stmt.setDouble(5, may);
							stmt.setDouble(6, jun);
							stmt.setDouble(7, jul);
							stmt.setDouble(8, aug);
							stmt.setDouble(9, sep);
							stmt.setDouble(10, oct);
							stmt.setDouble(11, nov);
							stmt.setDouble(12, dec);
							stmt.setDouble(13, easter);
							stmt.setDouble(14, diocese);
							stmt.setDouble(15, xmass);
							stmt.setDouble(16, Double.parseDouble(txtTotal.getText()));
							stmt.setString(17, payMode.getSelectedItem());
							stmt.setString(18, txtTransCode.getText().toUpperCase());
							stmt.setString(19, USER.toUpperCase());
							stmt.setInt(20, Integer.parseInt(cont_IDS.getSelectedItem().toString()));

							stmt.executeUpdate();
							txtJan.setText("");
							txtFeb.setText("");
							txtMar.setText("");
							txtApr.setText("");
							txtMay.setText("");
							txtJun.setText("");
							txtJul.setText("");
							txtAug.setText("");
							txtSep.setText("");
							txtOct.setText("");
							txtNov.setText("");
							txtDec.setText("");
							txtdiocese.setText("");
							txtxmass.setText("");
							payMode.select(0);
							txtTransCode.setText("");

							txtApr.setText("");
							txtMay.setText("");
							txtJun.setText("");
							txtJul.setText("");
							txtAug.setText("");
							txtSep.setText("");
							txtOct.setText("");
							txtNov.setText("");
							txtDec.setText("");
							txtdiocese.setText("");
							txtxmass.setText("");
							payMode.select(0);
							txtTransCode.setText("");
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				});
				sql = "SELECT CONTRIBUTION_ID FROM `Other Contribution`;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					stmt = con.prepareStatement(sql);
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					if (regNos.getItemCount() > 0) {
						regNos.removeAllItems();
					}
					while (rs.next()) {
						cont_IDS.addItem(Integer.parseInt(rs.getString("CONTRIBUTION_ID")));
					}
					if(cont_IDS.getItemCount()>0){
						cont_IDS.setSelectedIndex(0);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				sql = "select * from `Other Contribution` where CONTRIBUTION_ID=?;";
				try {
					// Register jdbc driver
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtOtherContr.setText(rs.getString("CampaignName"));
						txtJan.setText(String.valueOf(rs.getDouble("JAN")));
						txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
						txtMar.setText(String.valueOf(rs.getDouble("MAR")));
						txtApr.setText(String.valueOf(rs.getDouble("APRI")));
						txtMay.setText(String.valueOf(rs.getDouble("MAY")));
						txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
						txtJul.setText(String.valueOf(rs.getDouble("JULY")));
						txtAug.setText(String.valueOf(rs.getDouble("AUG")));
						txtSep.setText(String.valueOf(rs.getDouble("SEP")));
						txtOct.setText(String.valueOf(rs.getDouble("OCT")));
						txtNov.setText(String.valueOf(rs.getDouble("NOV")));
						txtDec.setText(String.valueOf(rs.getDouble("DECE")));
						txteaster.setText(String.valueOf(rs.getDouble("Easter")));
						txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
						txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
						txtTotal.setText(String.valueOf(rs.getDouble("Total")));
						payMode.select((rs.getString("PaymentMode")));
						txtTransCode.setText(rs.getString("PaymentCode"));

					}

				} catch (Exception e1) {
				}
				cont_IDS.setEditable(true);
				cont_IDS.setBorder(BorderFactory.createEtchedBorder());
				AutoCompleteDecorator.decorate(cont_IDS);
				cont_IDS.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "select * from `Other Contribution` where CONTRIBUTION_ID=?;";
						try {
							// Register jdbc driver
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);

							java.sql.PreparedStatement stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(cont_IDS.getSelectedItem().toString()));
							ResultSet rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtOtherContr.setText(rs.getString("CampaignName"));
								txtJan.setText(String.valueOf(rs.getDouble("JAN")));
								txtFeb.setText(String.valueOf(rs.getDouble("FEB")));
								txtMar.setText(String.valueOf(rs.getDouble("MAR")));
								txtApr.setText(String.valueOf(rs.getDouble("APRI")));
								txtMay.setText(String.valueOf(rs.getDouble("MAY")));
								txtJun.setText(String.valueOf(rs.getDouble("JUNE")));
								txtJul.setText(String.valueOf(rs.getDouble("JULY")));
								txtAug.setText(String.valueOf(rs.getDouble("AUG")));
								txtSep.setText(String.valueOf(rs.getDouble("SEP")));
								txtOct.setText(String.valueOf(rs.getDouble("OCT")));
								txtNov.setText(String.valueOf(rs.getDouble("NOV")));
								txtDec.setText(String.valueOf(rs.getDouble("DECE")));
								txteaster.setText(String.valueOf(rs.getDouble("Easter")));
								txtdiocese.setText(String.valueOf(rs.getDouble("DioceseCollection")));
								txtxmass.setText(String.valueOf(rs.getDouble("Christmas")));
								txtTotal.setText(String.valueOf(rs.getDouble("Total")));
								payMode.select((rs.getString("PaymentMode")));
								txtTransCode.setText(rs.getString("PaymentCode"));
							}

						} catch (Exception e1) {
						}
					}
				});

				// add fields to display
				JPanel userDetails = new JPanel();
				userDetails.setOpaque(false);
				userDetails.add(new JLabel("Contribution ID:"));
				userDetails.add(cont_IDS);
				userDetails.add(new JLabel("CAMPAIGN NAME:"));

				userDetails.add(txtOtherContr);
				userDetails.setBorder(BorderFactory.createTitledBorder("User Details"));
				panAll.setLayout(new BorderLayout(10, 1));
				panAll.setBorder(BorderFactory.createTitledBorder("INDIVIDUAL CONTRIBUTIONS"));
				panMonths.setBorder(BorderFactory.createTitledBorder("Enter Fields for months"));
				panAll.add(userDetails, BorderLayout.NORTH);
				panAll.add(panMonths, BorderLayout.CENTER);
				JPanel btns = new JPanel();
				btns.add(btnSave);

				panOtherXmass.setLayout(new BorderLayout());
				panOtherXmass.add(panTotal, BorderLayout.NORTH);
				panOtherXmass.add(btns, BorderLayout.SOUTH);
				panAll.add(panOtherXmass, BorderLayout.SOUTH);

				contribute.add(panAll);
			}
		}
	}

	/*****************************************************************
	 * 
	 * SHOW CONTRIBUTIONS
	 * 
	 *****************************************************************/
	public void showContribution() {
		// filter by mode of contribution
		Choice cont_mod = new Choice();
		cont_mod.add("Individual contributions");
		cont_mod.add("SCC contributions");
		cont_mod.add("Outstation contributions");
		Object[] contributionMode = { cont_mod };
		int option = JOptionPane.showConfirmDialog(this, contributionMode, "Select contribution category",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		// sql command to select all records and display them
		if (option == JOptionPane.OK_OPTION) {
			/*
			 * check mode selected and show results for the mode
			 */
			if (cont_mod.getSelectedIndex() == 0) {
				// Individual contributions selected
				ResultSet rs = null;
				String sql = "select `IndividualContributions`.`Date Received`,"
						+ "`IndividualContributions`.CONTRIBUTION_ID," + "`IndividualContributions`.RegNo,"
						+ "`Registration`.BaptismalName," + "`Registration`.OtherNames, "
						+ "`IndividualContributions`.Total," + "`IndividualContributions`.PaymentMode,"
						+ "`IndividualContributions`.PaymentCode"
						+ " from `IndividualContributions` JOIN `Registration` USING(`RegNo`);";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					rs = stmt.executeQuery();
					// Prepare table to fill values
					DefaultTableModel tab = new DefaultTableModel();

					// get metadata
					ResultSetMetaData rsmt = rs.getMetaData();

					// create array of column names
					int colCount = rsmt.getColumnCount();// number of columns
					// String colNames[] = new String[colCount];
					String colNames[] = { "Date Received", "Contribution ID", "Registration Number", "Baptismal Name",
							"Family/Other Names", "Total", "Payment mode", "Payment Code" };
					/*
					 * for (int i = 1; i <= colCount; i++) { colNames[i - 1] =
					 * rsmt.getColumnName(i); }
					 */
					tab.setColumnIdentifiers(colNames);// set column headings

					// now populate the data
					rs.beforeFirst();// make sure to move cursor to beginning
					while (rs.next()) {
						String[] rowData = new String[colCount];
						for (int i = 1; i <= colCount; i++) {
							rowData[i - 1] = rs.getString(i);
							// TODO :determine total for similaar records and
							// display only one of them
							// rowData[5] = "123";
						}
						tab.addRow(rowData);
					}
					JTable t = new JTable(tab);
					// t.setEnabled(false);
					t.setCellSelectionEnabled(true);
					sp = new JScrollPane(t);
					String title = "INDIVIDUAL CONTRIBUTIONS";
					sp.setWheelScrollingEnabled(true);
					sp.setBorder(BorderFactory.createTitledBorder(title.toUpperCase()));
				} catch (SQLException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (cont_mod.getSelectedIndex() == 1) {
				// SCC contributions selected
				ResultSet rs = null;
				String sql = "select sccContribution.`Date Received`," + "sccContribution.CONTRIBUTION_ID,"
						+ "sccContribution.SCCName," + "sccContribution.Total," + "sccContribution.PaymentMode,"
						+ "sccContribution.PaymentCode" + " from sccContribution;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					rs = stmt.executeQuery();
					// Prepare table to fill values
					DefaultTableModel tab = new DefaultTableModel();

					// get metadata
					ResultSetMetaData rsmt = rs.getMetaData();

					// create array of column names
					int colCount = rsmt.getColumnCount();// number of columns
					String colNames[] = { "Date Received", "Contribution ID", "Name of SCC", "Total", "Payment Mode",
							"Payment code" };
					/*
					 * for (int i = 1; i <= colCount; i++) { colNames[i - 1] =
					 * rsmt.getColumnName(i); }
					 */
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
					t.setCellSelectionEnabled(true);
					t.setToolTipText("Table showing scc contributions");
					sp = new JScrollPane(t);
					String title = "SMALL CHRISTIANS COMMUNITY CONTRIBUTIONS";
					sp.setBorder(BorderFactory.createTitledBorder(title.toUpperCase()));
				} catch (SQLException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (cont_mod.getSelectedIndex() == 2) {
				// Outstation contributions selected
				ResultSet rs = null;
				String sql = "select * from OutstationContribution;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					rs = stmt.executeQuery();
					// Prepare table to fill values
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
					t.setEnabled(false);
					sp = new JScrollPane(t);
					String title = "OUTSTATION CONTRIBUTIONS";
					sp.setBorder(BorderFactory.createTitledBorder(title.toUpperCase()));
				} catch (SQLException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else if (cont_mod.getSelectedIndex() == 3) {
				// other contributions selected
				ResultSet rs = null;
				String sql = "select * from  `Other Contribution` ;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);

					java.sql.PreparedStatement stmt = con.prepareStatement(sql);
					rs = stmt.executeQuery();
					// Prepare table to fill values
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
					t.setEnabled(false);
					sp = new JScrollPane(t);
					String title = "OTHER CONTRIBUTIONS";
					sp.setBorder(BorderFactory.createTitledBorder(title.toUpperCase()));
				} catch (SQLException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	private void findTotal() {
		if (!(txtJan.getText().trim().isEmpty())) {
			jan = Double.parseDouble(txtJan.getText());
		}
		if (!(txtFeb.getText().trim().isEmpty())) {
			feb = Double.parseDouble(txtFeb.getText());
		}
		if (!(txtMar.getText().trim().isEmpty())) {
			mar = Double.parseDouble(txtMar.getText());
		}
		if (!(txtApr.getText().trim().isEmpty())) {
			apr = Double.parseDouble(txtApr.getText());
		}
		if (!(txtMay.getText().trim().isEmpty())) {
			may = Double.parseDouble(txtMay.getText());
		}
		if (!(txtJun.getText().trim().isEmpty())) {
			jun = Double.parseDouble(txtJun.getText());
		}
		if (!(txtJul.getText().trim().isEmpty())) {
			jul = Double.parseDouble(txtJul.getText());
		}
		if (!(txtAug.getText().trim().isEmpty())) {
			aug = Double.parseDouble(txtAug.getText());
		}
		if (!(txtSep.getText().trim().isEmpty())) {
			sep = Double.parseDouble(txtSep.getText());
		}
		if (!(txtOct.getText().trim().isEmpty())) {
			oct = Double.parseDouble(txtOct.getText());
		}
		if (!(txtNov.getText().trim().isEmpty())) {
			nov = Double.parseDouble(txtNov.getText());
		}
		if (!(txtDec.getText().trim().isEmpty())) {
			dec = Double.parseDouble(txtDec.getText());
		}
		if (!(txteaster.getText().trim().isEmpty())) {
			easter = Double.parseDouble(txteaster.getText());
		}
		if (!(txtdiocese.getText().trim().isEmpty())) {
			diocese = Double.parseDouble(txtdiocese.getText());
		}
		if (!(txtxmass.getText().trim().isEmpty())) {
			xmass = Double.parseDouble(txtxmass.getText());
		}
		sum = jan + feb + mar + apr + may + jun + jul + aug + sep + oct + nov + dec + easter + diocese + xmass;
		txtTotal.setText(String.valueOf(sum));
	}

}
