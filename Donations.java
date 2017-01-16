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
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import com.toedter.calendar.JDateChooser;

public class Donations extends VariablesDefinitions {

	private static final long serialVersionUID = -1866956944120855200L;

	private JTextField txtPayCode, txtAmount, txtCampaign, txtFirstName, txtSecondName;
	private Choice chcModeOfPayment, chcOutstation, chcMode, chcZone;
	private String sql;
	private JComboBox<Integer> Reg, guestID, count;
	private JDateChooser date;
	ResultSet rs = null;

	public Donations() {
		txtPayCode = new JTextField(10);
		txtCampaign = new JTextField(10);
		txtAmount = new JTextField(10);

		txtFirstName = new JTextField(10);
		txtSecondName = new JTextField(10);

		// Add focus listener
		txtCampaign.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				txtCampaign.setText("");

			}
		});
		txtAmount.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				txtAmount.setText("");

			}
		});
		txtPayCode.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
			}

			@Override
			public void focusGained(FocusEvent e) {
				txtPayCode.setText("");

			}
		});

	}

	public void newDonation() {

		Reg = new JComboBox<Integer>();
		Reg.setBackground(Color.CYAN);
		AutoCompleteDecorator.decorate(Reg);

		guestID = new JComboBox<Integer>();
		guestID.setBackground(Color.CYAN);
		AutoCompleteDecorator.decorate(guestID);
		chcMode = new Choice();
		chcMode.add("Member");
		chcMode.add("Guest");
		chcMode.add("Outstation");
		chcMode.add("Zonal");
		chcMode.setBackground(Color.cyan);

		txtAmount.setText("Amount here");
		txtPayCode.setText("Transaction code here");
		txtCampaign.setText("Campaign name here");

		// populate mode of payment
		chcModeOfPayment = new Choice();
		chcModeOfPayment.setBackground(Color.green);
		sql = "select Name from ModeOfPayment;";
		try {
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcModeOfPayment.add(rs.getString("Name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		Object[] modes = { new JLabel("Mode of donation:"), chcMode };
		int option = JOptionPane.showConfirmDialog(this, modes, "Choose mode of donation", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		/************************************************************************************
		 * Single out selected modes
		 ***********************************************************************************/
		if (option == JOptionPane.OK_OPTION) {
			// member donation selected
			if (chcMode.getSelectedIndex() == 0) {

				/****************************************************
				 * populate Regnos
				 *****************************************************/
				sql = "select RegNo from Registration;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						Reg.addItem(Integer.parseInt(rs.getString("RegNo").trim()));
					}
					sql = "select BaptismalName,OtherNames from Registration where RegNo=?;";

					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(Reg.getSelectedItem().toString()));
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtFirstName.setText(rs.getString("BaptismalName"));
						txtSecondName.setText(rs.getString("OtherNames"));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				Reg.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "select BaptismalName,OtherNames from Registration where RegNo=?;";
						try {
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(Reg.getSelectedItem().toString()));
							rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtFirstName.setText(rs.getString("BaptismalName"));
								txtSecondName.setText(rs.getString("OtherNames"));
							}

						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});

				date = new JDateChooser();
				JPanel donations = new JPanel(new GridLayout(4, 2, 8, 8));

				donations.setBackground(Color.LIGHT_GRAY);
				donations.setBorder(BorderFactory.createCompoundBorder());
				donations.add(new JLabel("Member No:"));
				donations.add(Reg);
				donations.add(new JLabel("Date:"));
				donations.add(date);
				donations.add(new JLabel("First Name:"));
				donations.add(txtFirstName);
				donations.add(new JLabel("Second Name:"));
				donations.add(txtSecondName);
				donations.add(new JLabel("Campaign Name:"));
				donations.add(txtCampaign);
				donations.add(new JLabel("Amount:"));
				donations.add(txtAmount);
				donations.add(new JLabel("Payment Mode:"));
				donations.add(chcModeOfPayment);
				donations.add(new JLabel("Transaction Code:"));
				donations.add(txtPayCode);

				// prepare for display
				Object[] dons = { new JLabel("Fill the following fields.."), donations };
				JButton btnSave = new JButton("Save");
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						sql = "INSERT INTO `IndividualDonations`(`RegNo`, `Date`, `CampaignName`, `Amount`, "
								+ "`PaymentMode`, `PaymentCode`,Mode) " + "VALUES (?,?,?,?,?,?,?);";
						try {
							if (date.getDate() == null) {
								JOptionPane.showMessageDialog(null, "Please select valid date");
							} else if (txtAmount.getText().isEmpty() | txtPayCode.getText().isEmpty()) {
								JOptionPane.showMessageDialog(null, "Fill in the empty fields");
							} else {
								Class.forName(DRIVER_CLASS);
								// open connection
								con = DriverManager.getConnection(URL, USER, PASSWORD);
								stmt = con.prepareStatement(sql);
								stmt.setInt(1, Integer.parseInt(Reg.getSelectedItem().toString().trim()));
								stmt.setDate(2, new java.sql.Date(date.getDate().getTime()));
								stmt.setString(3, txtCampaign.getText().trim().toUpperCase());
								stmt.setDouble(4, Double.parseDouble(txtAmount.getText().trim()));
								stmt.setString(5, chcModeOfPayment.getSelectedItem().trim().toUpperCase());
								stmt.setString(6, txtPayCode.getText().trim().toUpperCase());
								stmt.setString(7, chcMode.getSelectedItem().trim().toUpperCase());
								stmt.executeUpdate();
								JOptionPane.showMessageDialog(null, "Data added!");
								// reset fields
								txtAmount.setText("Enter amount here");
								txtPayCode.setText("Enter transaction code here");
								txtCampaign.setText("Enter Cmpaign name here");
								date.setDate(null);
								sql = null;
								rs.close();
							}

						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}

					}
				});

				JOptionPane.showOptionDialog(this, dons, "", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
						null, new Object[] { btnSave }, null);

			}
			/***************************************************
			 * Guest donation mode
			 */
			if (chcMode.getSelectedIndex() == 1) {
				/****************************************************
				 * populate guestID
				 *****************************************************/
				sql = "select RegNo from Guests;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						guestID.addItem(Integer.parseInt(rs.getString("RegNo").trim()));
					}
					sql = "select FirstName,SecondName from Guests where RegNo=?;";

					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(guestID.getSelectedItem().toString()));
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtFirstName.setText(rs.getString("FirstName"));
						txtSecondName.setText(rs.getString("SecondName"));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				guestID.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "select FirstName,SecondName from Guests where RegNo=?;";
						try {
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(guestID.getSelectedItem().toString()));
							rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								txtFirstName.setText(rs.getString("FirstName"));
								txtSecondName.setText(rs.getString("SecondName"));
							}

						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});

				date = new JDateChooser();
				JPanel donations = new JPanel(new GridLayout(4, 2, 8, 8));
				donations.setBackground(Color.LIGHT_GRAY);
				donations.setBorder(BorderFactory.createCompoundBorder());
				donations.add(new JLabel("Guest ID:"));
				donations.add(guestID);
				donations.add(new JLabel("Date:"));
				donations.add(date);
				donations.add(new JLabel("First Name:"));
				donations.add(txtFirstName);
				donations.add(new JLabel("Second Name:"));
				donations.add(txtSecondName);
				donations.add(new JLabel("Campaign Name:"));
				donations.add(txtCampaign);
				donations.add(new JLabel("Amount:"));
				donations.add(txtAmount);
				donations.add(new JLabel("Payment Mode:"));
				donations.add(chcModeOfPayment);
				donations.add(new JLabel("Transaction Code:"));
				donations.add(txtPayCode);

				// prepare for display
				Object[] dons = { new JLabel("Fill the following fields.."), donations };
				JButton btnSave = new JButton("Save");
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						sql = "INSERT INTO `IndividualDonations`(`GuestID`, `Date`, `CampaignName`, `Amount`, "
								+ "`PaymentMode`, `PaymentCode`,Mode) " + "VALUES (?,?,?,?,?,?,?);";
						try {
							if (date.getDate() == null) {
								JOptionPane.showMessageDialog(null, "please select valid date");
							} else if (txtAmount.getText().isEmpty() | txtPayCode.getText().isEmpty()) {
								JOptionPane.showMessageDialog(null, "Fill in the empty fields");
							} else {
								Class.forName(DRIVER_CLASS);
								// open connection
								con = DriverManager.getConnection(URL, USER, PASSWORD);
								stmt = con.prepareStatement(sql);
								stmt.setInt(1, Integer.parseInt(guestID.getSelectedItem().toString().trim()));
								stmt.setDate(2, new java.sql.Date(date.getDate().getTime()));
								stmt.setString(3, txtCampaign.getText().trim().toUpperCase());
								stmt.setDouble(4, Double.parseDouble(txtAmount.getText().trim()));
								stmt.setString(5, chcModeOfPayment.getSelectedItem().trim().toUpperCase());
								stmt.setString(6, txtPayCode.getText().trim().toUpperCase());
								stmt.setString(7, chcMode.getSelectedItem().trim().toUpperCase());
								stmt.executeUpdate();
								JOptionPane.showMessageDialog(null, "Data added!");
								// reset fields
								txtAmount.setText("Enter amount here");
								txtPayCode.setText("Enter transaction code here");
								txtCampaign.setText("Enter Cmpaign name here");
								date.setDate(null);
								sql = null;
								rs.close();
							}

						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}

					}
				});

				JOptionPane.showOptionDialog(this, dons, "", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
						null, new Object[] { btnSave }, null);
			}

			/*****************************************************************
			 * Outstation donation selected
			 */
			else if (chcMode.getSelectedIndex() == 2) {
				date = new JDateChooser();
				chcOutstation = new Choice();
				sql = "select C_Name from Outstation;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						chcOutstation.add(rs.getString("C_Name"));
					}
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				JPanel donations = new JPanel(new GridLayout(3, 2, 10, 10));

				donations.setBackground(Color.white);
				donations.setBorder(BorderFactory.createRaisedSoftBevelBorder());
				donations.add(new JLabel("Date:"));
				donations.add(date);
				donations.add(new JLabel("Name of Outstation:"));
				donations.add(chcOutstation);
				donations.add(new JLabel("Campaign Name:"));
				donations.add(txtCampaign);
				donations.add(new JLabel("Amount:"));
				donations.add(txtAmount);
				donations.add(new JLabel("Payment Mode:"));
				donations.add(chcModeOfPayment);
				donations.add(new JLabel("Transaction Code:"));
				donations.add(txtPayCode);

				Object[] dons = { donations };
				JButton btnSave = new JButton("Save");
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (date.getDate() == null) {
							JOptionPane.showMessageDialog(null, "Enter valid date");
						}
						if (txtCampaign.getText().isEmpty() | txtAmount.getText().isEmpty()
								| txtPayCode.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Fill empty fields");
						}

						sql = "INSERT INTO `Donations`(`Date`, `Mode`, `Name`, `CampaignName`, "
								+ "`Amount`, `PaymentMode`, `paymentNumber`) " + "VALUES (?,?,?,?,?,?,?);";
						try {
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);
							stmt.setDate(1, new java.sql.Date(date.getDate().getTime()));
							stmt.setString(2, chcMode.getSelectedItem().trim().toUpperCase());
							stmt.setString(3, chcOutstation.getSelectedItem().trim().toUpperCase());
							stmt.setString(4, txtCampaign.getText().trim().toUpperCase());
							stmt.setDouble(5, Double.parseDouble(txtAmount.getText().trim()));
							stmt.setString(6, chcModeOfPayment.getSelectedItem().trim().toUpperCase());
							stmt.setString(7, txtPayCode.getText().trim().toUpperCase());
							stmt.executeUpdate();

							JOptionPane.showMessageDialog(null, "Data entered!");
							// reset fields to their default
							txtAmount.setText("Enter amount here");
							txtPayCode.setText("Enter transaction code here");
							txtCampaign.setText("Enter Cmpaign name here");
							date.setDate(null);
							sql = null;
							rs.close();
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, e2.getMessage());
						}

					}
				});

				JOptionPane.showOptionDialog(this, dons, "", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
						null, new Object[] { btnSave }, null);

			}
			/**********************************************
			 * zonal donation selected
			 **********************************************/
			else if (chcMode.getSelectedIndex() == 3) {

				date = new JDateChooser();
				chcZone = new Choice();
				sql = "select Name from Zone;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						chcZone.add(rs.getString("Name"));
					}
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				JPanel donations = new JPanel(new GridLayout(3, 2, 10, 10));

				donations.setBackground(Color.white);
				donations.setBorder(BorderFactory.createRaisedSoftBevelBorder());
				donations.add(new JLabel("Date:"));
				donations.add(date);
				donations.add(new JLabel("Name of Zone:"));
				donations.add(chcZone);
				donations.add(new JLabel("Campaign Name:"));
				donations.add(txtCampaign);
				donations.add(new JLabel("Amount:"));
				donations.add(txtAmount);
				donations.add(new JLabel("Payment Mode:"));
				donations.add(chcModeOfPayment);
				donations.add(new JLabel("Transaction Code:"));
				donations.add(txtPayCode);

				Object[] dons = { donations };
				JButton btnSave = new JButton("Save");
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (date.getDate() == null) {
							JOptionPane.showMessageDialog(null, "Enter valid date");
						}
						if (txtCampaign.getText().isEmpty() | txtAmount.getText().isEmpty()
								| txtPayCode.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Fill empty fields");
						}

						sql = "INSERT INTO `Donations`(`Date`, `Mode`, `Name`, `CampaignName`, "
								+ "`Amount`, `PaymentMode`, `paymentNumber`) " + "VALUES (?,?,?,?,?,?,?);";
						try {
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);
							stmt.setDate(1, new java.sql.Date(date.getDate().getTime()));
							stmt.setString(2, chcMode.getSelectedItem().trim().toUpperCase());
							stmt.setString(3, chcZone.getSelectedItem().trim().toUpperCase());
							stmt.setString(4, txtCampaign.getText().trim().toUpperCase());
							stmt.setDouble(5, Double.parseDouble(txtAmount.getText().trim()));
							stmt.setString(6, chcModeOfPayment.getSelectedItem().trim().toUpperCase());
							stmt.setString(7, txtPayCode.getText().trim().toUpperCase());
							stmt.executeUpdate();

							JOptionPane.showMessageDialog(null, "Data entered!");
							// reset fields to their default
							txtAmount.setText("Enter amount here");
							txtPayCode.setText("Enter transaction code here");
							txtCampaign.setText("Enter Cmpaign name here");
							date.setDate(null);
							sql = null;
							rs.close();
						} catch (Exception e2) {
							e2.printStackTrace();

							JOptionPane.showMessageDialog(null, e2.getMessage());
						}

					}
				});

				JOptionPane.showOptionDialog(this, dons, "", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
						null, new Object[] { btnSave }, null);
			}
		}
	}

	// edit donations
	public void editDonations() {

		count = new JComboBox<Integer>();
		count.setBackground(Color.CYAN);
		AutoCompleteDecorator.decorate(count);

		final JTextField txtReg = new JTextField(5);
		txtReg.setEditable(false);
		// you cannot change member details here
		txtFirstName.setEditable(false);
		txtSecondName.setEditable(false);

		chcMode = new Choice();
		chcMode.add("MEMBER");
		chcMode.add("GUEST");
		chcMode.add("OUTSTATION");
		chcMode.add("ZONAL");
		chcMode.setBackground(Color.cyan);

		// populate mode of payment
		chcModeOfPayment = new Choice();
		chcModeOfPayment.setEnabled(false);
		chcModeOfPayment.setBackground(Color.green);
		sql = "select Name from ModeOfPayment;";
		try {
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcModeOfPayment.add(rs.getString("Name"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		Object[] modes = { new JLabel("Mode of donation:"), chcMode };
		int option = JOptionPane.showConfirmDialog(this, modes, "Choose mode of donation to edit",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			// member donation selected
			if (chcMode.getSelectedIndex() == 0) {
				/****************************************************
				 * populate Regnos
				 *****************************************************/
				date = new JDateChooser();
				sql = "select Count from IndividualDonations where Mode=?;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chcMode.getSelectedItem().trim().toUpperCase());
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						count.addItem(Integer.parseInt(rs.getString("Count").trim()));
					}

					sql = "select `IndividualDonations`.RegNo," + "`IndividualDonations`.CampaignName,"
							+ "`Registration`.BaptismalName" + " from IndividualDonations " + "JOIN `Registration` "
							+ "USING(`RegNo`) where count=?;";

					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(count.getSelectedItem().toString()));
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtReg.setText(rs.getString("RegNo"));
					}

					rs.close();
					sql = "select `IndividualDonations`.RegNo," + "`IndividualDonations`.Date,"
							+ "`IndividualDonations`.CampaignName, " + "`IndividualDonations`.Amount,"
							+ "`IndividualDonations`.PaymentMode," + " `IndividualDonations`.PaymentCode,"
							+ "`Registration`.BaptismalName," + " `Registration`.OtherNames "
							+ " from `IndividualDonations` " + "JOIN `Registration` USING(`RegNo`) where Count=?;";

					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(count.getSelectedItem().toString()));
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						chcModeOfPayment.select(rs.getString("PaymentMode"));
						txtPayCode.setText(rs.getString("PaymentCode"));
						txtAmount.setText(String.valueOf(Double.parseDouble(rs.getString("Amount"))));
						txtCampaign.setText(rs.getString("CampaignName"));
						txtFirstName.setText(rs.getString("BaptismalName"));
						txtSecondName.setText(rs.getString("OtherNames"));
						txtReg.setText(rs.getString("RegNo"));
						date.setDate(rs.getDate("Date"));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				count.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "select `IndividualDonations`.RegNo," + "`IndividualDonations`.Date,"
								+ "`IndividualDonations`.CampaignName, " + "`IndividualDonations`.Amount,"
								+ "`IndividualDonations`.PaymentMode," + " `IndividualDonations`.PaymentCode,"
								+ "`Registration`.BaptismalName," + " `Registration`.OtherNames "
								+ " from `IndividualDonations` " + "JOIN `Registration` USING(`RegNo`) where Count=?;";
						try {
							stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(count.getSelectedItem().toString()));
							rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								chcModeOfPayment.select(rs.getString("PaymentMode"));
								txtPayCode.setText(rs.getString("PaymentCode"));
								txtAmount.setText(String.valueOf(Double.parseDouble(rs.getString("Amount"))));
								txtCampaign.setText(rs.getString("CampaignName"));
								txtFirstName.setText(rs.getString("BaptismalName"));
								txtSecondName.setText(rs.getString("OtherNames"));
								txtReg.setText(rs.getString("RegNo"));
								date.setDate(rs.getDate("Date"));
							}
							rs.close();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});

				JPanel donations = new JPanel(new GridLayout(4, 2, 8, 8));
				donations.setBackground(Color.LIGHT_GRAY);
				donations.setBorder(BorderFactory.createCompoundBorder());
				donations.add(new JLabel("Member No:"));
				donations.add(txtReg);
				donations.add(new JLabel("Date:"));
				donations.add(date);
				donations.add(new JLabel("First Name:"));
				donations.add(txtFirstName);
				donations.add(new JLabel("Second Name:"));
				donations.add(txtSecondName);
				donations.add(new JLabel("Campaign Name:"));
				donations.add(txtCampaign);
				donations.add(new JLabel("Amount:"));
				donations.add(txtAmount);
				donations.add(new JLabel("Payment Mode:"));
				donations.add(chcModeOfPayment);
				donations.add(new JLabel("Transaction Code:"));
				donations.add(txtPayCode);

				JPanel pancount = new JPanel(new GridLayout(1, 2, 10, 10));
				pancount.add(new JLabel("Record ID:"));
				pancount.add(count);

				JPanel par = new JPanel(new BorderLayout(10, 10));
				par.add(pancount, BorderLayout.NORTH);
				par.add(donations, BorderLayout.CENTER);

				// prepare for display
				Object[] dons = { par };
				JButton btnSave = new JButton("update");
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						sql = "UPDATE `IndividualDonations` SET `CampaignName`=?,"
								+ "`Amount`=?,`PaymentMode`=?,`PaymentCode`=?" + " where `Count`=?";
						try {
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);
							stmt.setString(1, txtCampaign.getText().trim().toUpperCase());
							stmt.setDouble(2, Double.parseDouble(txtAmount.getText().trim()));
							stmt.setString(3, chcModeOfPayment.getSelectedItem().trim().toUpperCase());
							stmt.setString(4, txtPayCode.getText().trim().toUpperCase());
							stmt.setInt(5, Integer.parseInt(count.getSelectedItem().toString()));

							stmt.executeUpdate();
							JOptionPane.showMessageDialog(null, "Record edited!");
							// reset fields
							stmt.close();

						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}

					}
				});

				JOptionPane.showOptionDialog(this, dons, "", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
						null, new Object[] { btnSave }, null);
			}

			/********************************************************************
			 * Guest donation selected
			 */
			if (chcMode.getSelectedIndex() == 1) {
				/****************************************************
				 * populate Regnos
				 *****************************************************/
				date = new JDateChooser();
				sql = "select Count from IndividualDonations where Mode=?;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chcMode.getSelectedItem().trim().toUpperCase());
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						count.addItem(Integer.parseInt(rs.getString("Count").trim()));
					}

					sql = "select `IndividualDonations`.GuestID from IndividualDonations " + "where count=?;";

					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(count.getSelectedItem().toString()));
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtReg.setText(rs.getString("GuestID"));
					}

					rs.close();
					sql = "select `IndividualDonations`.GuestID," + "`IndividualDonations`.Date,"
							+ "`IndividualDonations`.CampaignName, " + "`IndividualDonations`.Amount,"
							+ "`IndividualDonations`.PaymentMode," + " `IndividualDonations`.PaymentCode,"
							+ "`Guests`.FirstName," + " `Guests`.SecondName " + " from `IndividualDonations` "
							+ "JOIN `Guests` USING(`GuestID`) where Count=?;";

					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(count.getSelectedItem().toString()));
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						chcModeOfPayment.select(rs.getString("PaymentMode"));
						txtPayCode.setText(rs.getString("PaymentCode"));
						txtAmount.setText(String.valueOf(Double.parseDouble(rs.getString("Amount"))));
						txtCampaign.setText(rs.getString("CampaignName"));
						txtFirstName.setText(rs.getString("FirstName"));
						txtSecondName.setText(rs.getString("SecondName"));
						txtReg.setText(rs.getString("GuestID"));
						date.setDate(rs.getDate("Date"));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				count.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						sql = "select `IndividualDonations`.GuestID," + "`IndividualDonations`.Date,"
								+ "`IndividualDonations`.CampaignName, " + "`IndividualDonations`.Amount,"
								+ "`IndividualDonations`.PaymentMode," + " `IndividualDonations`.PaymentCode,"
								+ "`Guests`.FirstName," + " `Guests`.SecondName " + " from `IndividualDonations` "
								+ "JOIN `Guests` USING(`GuestID`) where Count=?;";
						try {
							stmt = con.prepareStatement(sql);
							stmt.setInt(1, Integer.parseInt(count.getSelectedItem().toString()));
							rs = stmt.executeQuery();
							rs.beforeFirst();
							while (rs.next()) {
								chcModeOfPayment.select(rs.getString("PaymentMode"));
								txtPayCode.setText(rs.getString("PaymentCode"));
								txtAmount.setText(String.valueOf(Double.parseDouble(rs.getString("Amount"))));
								txtCampaign.setText(rs.getString("CampaignName"));
								txtFirstName.setText(rs.getString("FirstName"));
								txtSecondName.setText(rs.getString("SecondName"));
								txtReg.setText(rs.getString("GuestID"));
								date.setDate(rs.getDate("Date"));
							}
							rs.close();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});

				JPanel donations = new JPanel(new GridLayout(4, 2, 8, 8));
				donations.setBackground(Color.LIGHT_GRAY);
				donations.setBorder(BorderFactory.createCompoundBorder());
				donations.add(new JLabel("Member No:"));
				donations.add(txtReg);
				donations.add(new JLabel("Date:"));
				donations.add(date);
				donations.add(new JLabel("First Name:"));
				donations.add(txtFirstName);
				donations.add(new JLabel("Second Name:"));
				donations.add(txtSecondName);
				donations.add(new JLabel("Campaign Name:"));
				donations.add(txtCampaign);
				donations.add(new JLabel("Amount:"));
				donations.add(txtAmount);
				donations.add(new JLabel("Payment Mode:"));
				donations.add(chcModeOfPayment);
				donations.add(new JLabel("Transaction Code:"));
				donations.add(txtPayCode);

				JPanel pancount = new JPanel(new GridLayout(1, 2, 10, 10));
				pancount.add(new JLabel("Record ID:"));
				pancount.add(count);

				JPanel par = new JPanel(new BorderLayout(10, 10));
				par.add(pancount, BorderLayout.NORTH);
				par.add(donations, BorderLayout.CENTER);

				// prepare for display
				Object[] dons = { par };
				JButton btnSave = new JButton("update");
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						sql = "UPDATE `IndividualDonations` SET `CampaignName`=?,"
								+ "`Amount`=?,`PaymentMode`=?,`PaymentCode`=?" + " where `Count`=?";
						try {
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);
							stmt.setString(1, txtCampaign.getText().trim().toUpperCase());
							stmt.setDouble(2, Double.parseDouble(txtAmount.getText().trim()));
							stmt.setString(3, chcModeOfPayment.getSelectedItem().trim().toUpperCase());
							stmt.setString(4, txtPayCode.getText().trim().toUpperCase());
							stmt.setInt(5, Integer.parseInt(count.getSelectedItem().toString()));

							stmt.executeUpdate();
							JOptionPane.showMessageDialog(null, "Record edited!");
							// reset fields
							stmt.close();

						} catch (Exception e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage());
						}

					}
				});

				JOptionPane.showOptionDialog(this, dons, "", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
						null, new Object[] { btnSave }, null);
			}
			/********************************************************************
			 * Outstation donation selected
			 */
			else if (chcMode.getSelectedIndex() == 2) {
				date = new JDateChooser();
				chcOutstation = new Choice();

				sql = "select C_Name from Outstation;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						chcOutstation.add(rs.getString("C_Name"));
					}
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				sql = "select Count from Donations where Mode=?;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chcMode.getSelectedItem().trim().toUpperCase());
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						count.addItem(Integer.parseInt(rs.getString("Count").toString()));
					}
					sql = "select `Date`, `Name`, `CampaignName`," + " `Amount`, `PaymentMode`, `paymentNumber` "
							+ "FROM `Donations` where Count=?;";
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(count.getSelectedItem().toString().trim()));
					rs=stmt.executeQuery();
					rs.beforeFirst();
					while(rs.next()){
						date.setDate(rs.getDate("Date"));
						chcOutstation.select(rs.getString("Name"));
						txtCampaign.setText(rs.getString("CampaignName"));
						txtAmount.setText(String.valueOf(rs.getDouble("Amount")));
						chcModeOfPayment.select(rs.getString("PaymentMode"));
						txtPayCode.setText(rs.getString("paymentNumber"));
					}
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				date.setEnabled(false);
				JPanel donations = new JPanel(new GridLayout(3, 2, 10, 10));

				donations.setBackground(Color.white);
				donations.setBorder(BorderFactory.createRaisedSoftBevelBorder());
				donations.add(new JLabel("Date:"));
				donations.add(date);
				donations.add(new JLabel("Name of Outstation:"));
				donations.add(chcOutstation);
				donations.add(new JLabel("Campaign Name:"));
				donations.add(txtCampaign);
				donations.add(new JLabel("Amount:"));
				donations.add(txtAmount);
				donations.add(new JLabel("Payment Mode:"));
				donations.add(chcModeOfPayment);
				donations.add(new JLabel("Transaction Code:"));
				donations.add(txtPayCode);

				Object[] dons = { donations };
				JButton btnSave = new JButton("update");
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (date.getDate() == null) {
							JOptionPane.showMessageDialog(null, "Enter valid date");
						}
						if (txtCampaign.getText().isEmpty() | txtAmount.getText().isEmpty()
								| txtPayCode.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Fill empty fields");
						}

						sql = "UPDATE `Donations` SET `Name`=?,`CampaignName`=?,"
								+ "`Amount`=?,`PaymentMode`=?,`paymentNumber`=? WHERE `Count`=?";
						try {
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);
							stmt.setString(1, chcOutstation.getSelectedItem().trim().toUpperCase());
							stmt.setString(2, txtCampaign.getText().trim().toUpperCase());
							stmt.setDouble(3, Double.parseDouble(txtAmount.getText().trim()));
							stmt.setString(4, chcModeOfPayment.getSelectedItem().trim().toUpperCase());
							stmt.setString(5, txtPayCode.getText().trim().toUpperCase());
							stmt.setInt(6, Integer.parseInt(count.getSelectedItem().toString().trim()));
							stmt.executeUpdate();

							JOptionPane.showMessageDialog(null, "Data updated!");
							// reset fields to their default
							sql = null;
							rs.close();
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, e2.getMessage());
						}

					}
				});

				JOptionPane.showOptionDialog(this, dons, "", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
						null, new Object[] { btnSave }, null);

			}
			/**********************************************
			 * zonal donation selected
			 **********************************************/
			else if (chcMode.getSelectedIndex() == 3) {

				date = new JDateChooser();
				chcZone = new Choice();

				sql = "select Name from Zone;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						chcZone.add(rs.getString("Name"));
					}
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				sql = "select Count from Donations where Mode=?;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chcMode.getSelectedItem().trim().toUpperCase());
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						count.addItem(Integer.parseInt(rs.getString("Count").toString()));
					}
					sql = "select `Date`, `Name`, `CampaignName`," + " `Amount`, `PaymentMode`, `paymentNumber` "
							+ "FROM `Donations` where Count=?;";
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(count.getSelectedItem().toString().trim()));
					rs=stmt.executeQuery();
					rs.beforeFirst();
					while(rs.next()){
						date.setDate(rs.getDate("Date"));
						chcZone.select(rs.getString("Name"));
						txtCampaign.setText(rs.getString("CampaignName"));
						txtAmount.setText(String.valueOf(rs.getDouble("Amount")));
						chcModeOfPayment.select(rs.getString("PaymentMode"));
						txtPayCode.setText(rs.getString("paymentNumber"));
					}
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

				date.setEnabled(false);
				JPanel donations = new JPanel(new GridLayout(3, 2, 10, 10));

				donations.setBackground(Color.white);
				donations.setBorder(BorderFactory.createRaisedSoftBevelBorder());
				donations.add(new JLabel("Date:"));
				donations.add(date);
				donations.add(new JLabel("Name of Outstation:"));
				donations.add(chcZone);
				donations.add(new JLabel("Campaign Name:"));
				donations.add(txtCampaign);
				donations.add(new JLabel("Amount:"));
				donations.add(txtAmount);
				donations.add(new JLabel("Payment Mode:"));
				donations.add(chcModeOfPayment);
				donations.add(new JLabel("Transaction Code:"));
				donations.add(txtPayCode);

				Object[] dons = { donations };
				JButton btnSave = new JButton("update");
				btnSave.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (date.getDate() == null) {
							JOptionPane.showMessageDialog(null, "Enter valid date");
						}
						if (txtCampaign.getText().isEmpty() | txtAmount.getText().isEmpty()
								| txtPayCode.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Fill empty fields");
						}

						sql = "UPDATE `Donations` SET `Name`=?,`CampaignName`=?,"
								+ "`Amount`=?,`PaymentMode`=?,`paymentNumber`=? WHERE `Count`=?";
						try {
							Class.forName(DRIVER_CLASS);
							// open connection
							con = DriverManager.getConnection(URL, USER, PASSWORD);
							stmt = con.prepareStatement(sql);
							stmt.setString(1, chcZone.getSelectedItem().trim().toUpperCase());
							stmt.setString(2, txtCampaign.getText().trim().toUpperCase());
							stmt.setDouble(3, Double.parseDouble(txtAmount.getText().trim()));
							stmt.setString(4, chcModeOfPayment.getSelectedItem().trim().toUpperCase());
							stmt.setString(5, txtPayCode.getText().trim().toUpperCase());
							stmt.setInt(6, Integer.parseInt(count.getSelectedItem().toString().trim()));
							stmt.executeUpdate();

							JOptionPane.showMessageDialog(null, "Data updated!");
							// reset fields to their default
							sql = null;
							rs.close();
						} catch (Exception e2) {
							JOptionPane.showMessageDialog(null, e2.getMessage());
						}

					}
				});

				JOptionPane.showOptionDialog(this, dons, "", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
						null, new Object[] { btnSave }, null);
			}
		}
	}
}
