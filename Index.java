package databaseApp;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class Index extends VariablesDefinitions {

	private static final long serialVersionUID = -7505774995565898116L;

	// remember to initialize the classes in after declaring them here
	Contributions contribution; // contribution class object
	Baptism baptism; // Baptism class object
	Zone Zone; // zone class object
	Registration registration; // Registration class object
	Projects project; // Project class object
	Groups group; // Group class object
	Offerings offering; // Offering class object
	Payment_Modes modes;
	Guests guest;
	Donations donation;
	ZoneCommittee zonecommittee;
	Outstation outstation;
	OutstationCommittee outstationcommittee;

	public Index() {

		/*
		 * instantiate other classes
		 */
		contribution = new Contributions();
		baptism = new Baptism();
		Zone = new Zone();
		registration = new Registration();
		project = new Projects();
		group = new Groups();
		offering = new Offerings();
		modes = new Payment_Modes();
		guest = new Guests();
		donation = new Donations();
		zonecommittee = new ZoneCommittee();
		outstation = new Outstation();
		outstationcommittee = new OutstationCommittee();

		/**************************************************************************
		 * 
		 * Button to log in to the database system
		 * 
		 **************************************************************************/

		frame.getRootPane().setDefaultButton(login);

		login.setBorder(BorderFactory.createEtchedBorder());
		login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				VariablesDefinitions.USER = txtUser.getText();
				PASSWORD = txtPass.getText();
				Connect();
			}
		});
		/**************************************************************************
		 * 
		 * Button to log out of the database system
		 * 
		 **************************************************************************/
		logOut = new JButton("Log Out");
		logOut.setBackground(null);
		logOut.setOpaque(false);
		logOut.setBorder(BorderFactory.createEtchedBorder());
		logOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				this.logOut();
			}

			private void logOut() {
				// TODO Auto-generated method stub
				try {
					String title = "WELCOME TO CHURCH MANAGEMENT INFORMATION SYSTEM ";
					hdr.setText(title);
					con.close();
					msg.setText("You are logged out.Please Log in to continue.");
					background.add(logpanel);
					btns.remove(logOut);
					parent.removeAll();// reset everything
					// background.remove(parent);
					connected = false;
					// delete user variables
					VariablesDefinitions.USER = null;
					VariablesDefinitions.PASSWORD = null;

					frame.setJMenuBar(null);
					;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		btns.add(logOut);

		logpanel.setOpaque(false);
		// set border property for the login panel
		logpanel.setBorder(BorderFactory.createTitledBorder("Login"));

		/**************************************************************************
		 * 
		 * fields for user login credentials
		 * 
		 **************************************************************************/
		LabUname.setBounds(img.getIconWidth() / 3 + 30, img.getIconHeight() / 3, 80, 20);
		logpanel.add(LabUname);
		txtUser.setBounds(img.getIconWidth() / 3 + 150, img.getIconHeight() / 3, 100, 20);
		logpanel.add(txtUser);
		LabPword.setBounds(img.getIconWidth() / 3 + 30, img.getIconHeight() / 3 + 30, 100, 20);
		logpanel.add(LabPword);
		txtPass.setBounds(img.getIconWidth() / 3 + 150, img.getIconHeight() / 3 + 30, 100, 20);
		logpanel.add(txtPass);
		login.setBounds(100, 60, 100, 20);
		logpanel.add(login);
		background.add(logpanel);

		/**************************************************************************
		 * 
		 * Church menus and their click listeners
		 * 
		 **************************************************************************/
		// new church record
		newChurch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				outstation.newChurch();
			}
		});
		// edit church record
		editChurch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				outstation.editChurch();

			}
		});

		// delete church record
		delChurch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				outstation.deleteChurch();
			}
		});
		showChurches.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Implement sql select commands for churches
				outstation.showChurches();
				parent.removeAll();
				parent.add(outstation.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});
		searchChurch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				outstation.searchChurch();
				if (outstation.sp.getComponentCount() > 0) {
					parent.removeAll();
					parent.add(outstation.sp, BorderLayout.CENTER);
					frame.pack();
					frame.repaint();
				}

			}
		});

		/***********************************************************
		 * small Christian community
		 ***********************************************************/
		newScc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createScc();
			}
		});
		// edit scc
		delScc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteScc();
			}
		});
		// Show existing scc
		showScc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				showScc();
			}
		});
		/*************************************************************************
		 * GroupsTypes menu items
		 *************************************************************************/
		newGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				group.addGroup();
			}
		});

		showGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				group.showGroups();
				parent.removeAll();
				parent.add(group.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});
		editGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// editGroups();
			}
		});
		delGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// delGroup();
			}
		});
		/*************************************************************************
		 * Projects menu items
		 *************************************************************************/
		newProject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				project.addProject();
				parent.removeAll();
				parent.add(project.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});

		showProject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				project.showProjects();
				parent.removeAll();
				parent.add(project.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});
		editProject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				project.editProjects();
			}
		});
		delProject.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// delProject();
				project.delProject();
			}
		});
		/*************************************************************************
		 * Contribution menu
		 *************************************************************************/
		newCon.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				parent.removeAll();
				contribution.newContribution();
				parent.add(contribution.contribute, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});

		C_show.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.removeAll();
				contribution.showContribution();
				parent.add(contribution.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});
		delContribution.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.removeAll();
				contribution.delContribution();
				parent.add(contribution.contribute, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});
		// edit contribution
		editCon.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.removeAll();
				contribution.editContribution();
				parent.add(contribution.contribute, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});

		/************************************************************************
		 * Baptism menu items
		 ************************************************************************/
		newBap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.removeAll();
				baptism.createBaptism();
				parent.add(baptism.bap, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});
		/*
		 * edit baptism record
		 */
		editBap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.removeAll();
				baptism.editBaptism();
				parent.add(baptism.bap, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});
		delBap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.removeAll();
				baptism.deleteBaptism();
				parent.add(baptism.bap, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});
		/*
		 * Show baptism record
		 */
		searchBap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				parent.removeAll();
				baptism.searchBaptism();
				parent.add(baptism.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});
		/*
		 * show all baptisms
		 */
		showAllBaptisms.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				parent.removeAll();
				baptism.showBaptism();
				parent.add(baptism.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});

		/**************************************************************************
		 * 
		 * New church committee members menuItem
		 * 
		 **************************************************************************/
		newComMember.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				outstationcommittee.addChrchCommitte();
			}
		});

		editComMember.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Edit church committee members
				outstationcommittee.EditChrchCommitte();
			}
		});
		showComMembers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				outstationcommittee.showChurchCommMembers();

				parent.removeAll();
				parent.add(outstationcommittee.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();

			}
		});

		// delete church committee members
		delComMember.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				outstationcommittee.delChurchComMembers();
			}
		});
		/**************************************************************************
		 * 
		 * New zone and zone committee
		 * 
		 **************************************************************************/
		// new zone
		newZone.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Zone.insertZone();
				Zone.showZones();
				parent.removeAll();
				parent.add(Zone.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});
		// edit zones
		EditZone.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Zone.editZone();
				Zone.showZones();
				parent.removeAll();
				parent.add(Zone.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();

			}
		});
		showZones.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Zone.showZones();
				parent.removeAll();
				parent.add(Zone.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();

			}
		});
		deleteZone.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Zone.deleteZone();
				parent.removeAll();
				Zone.showZones();
				parent.add(Zone.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});
		SearchZones.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Zone.searchZone();
				parent.removeAll();
				parent.add(Zone.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});
		/*
		 * new Zone committee
		 */
		newZonecom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				zonecommittee.addZoneCommitte();
			}
		});
		editZoneComm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				zonecommittee.editZoneCommitte();
			}
		});
		delZoneCom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				zonecommittee.delZoneComm();
			}
		});
		showZoneCom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				zonecommittee.showZonecomm();
				Zone.searchZone();
				parent.removeAll();
				parent.add(zonecommittee.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();

			}
		});

		/********************************************************************
		 * 
		 * Registration module variable initialization
		 *
		 ********************************************************************/
		// Method to register new christians
		newReg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				registration.insertRegistration();

			}
		});

		editReg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				registration.editRegistration();
			}
		});

		delReg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				registration.delRegistration();
			}
		});

		showReg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				registration.showRegistration();
				parent.removeAll();
				parent.add(registration.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();
			}
		});

		/***********************************
		 * Offering
		 */
		newOffering.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				offering.newOffering();

			}
		});
		editOffering.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				offering.editOffering();

			}
		});
		delOffering.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				offering.delOffering();

			}
		});
		showOffering.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				offering.showOffering();
				parent.removeAll();
				parent.add(offering.sp, BorderLayout.CENTER);
				frame.pack();
				frame.repaint();

			}
		});

		/***************************************
		 * donations
		 */
		newDon.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				donation.newDonation();
			}
		});
		editDon.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				donation.editDonations();
			}
		});
		/******************************************
		 * mode of payment
		 */
		newMod.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				modes.newMode();

			}
		});

		editMod.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				modes.editMode();

			}
		});

		delMod.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				modes.delMode();

			}
		});

		showMod.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				modes.showMode();
				parent.removeAll();
				parent.add(modes.modes);

				frame.pack();
				frame.repaint();

			}
		});

		newGuest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				guest.newGuest();

			}
		});

		editGuest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				guest.editGuest();

			}
		});

		delGuest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				guest.delGuest();

			}
		});

		showGuest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				guest.showGuests();
				parent.removeAll();
				parent.add(guest.guest);

				frame.pack();
				frame.repaint();

			}
		});

		// show frame

		pack();

		frame.setResizable(true);
		frame.setVisible(true);

	}

	/********************************************************************************
	 * 
	 * Methods for Small Christian communities
	 *
	 ********************************************************************************/
	// create new scc
	protected void createScc() {
		JButton save = new JButton("Save SCC");
		churchName = "";

		// select church first to create new scc
		String sql = "select * from Outstation;";
		ResultSet rs = null;
		final Choice church = new Choice();
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				church.addItem(rs.getString("C_Name"));
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}

		Object[] scc = { labChurch, church, new JLabel("Name of SCC:"), txtsccName

		};

		// If save button clicked, get the inputs from the text fields
		save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Insert new scc to the selected church
				try {
					String sql = "insert into SCC values(?,?)";
					stmt = con.prepareStatement(sql);
					stmt.setString(1, txtsccName.getText().toString());
					stmt.setString(2, church.getSelectedItem());
					stmt.executeUpdate();
					showScc();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JOptionPane.showOptionDialog(this, scc, "Enter details of SCC", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, new Object[] { save }, null);

	}

	// edit edit scc
	protected void deleteScc() {
		churchName = "";
		String SCC = "";
		JTextField txtsccName = new JTextField(10);
		// select church first to create new scc
		String sql = "select * from Outstation;";
		ResultSet rs = null;
		Choice church = new Choice();
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				church.addItem(rs.getString("C_Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Object[] scc = { labChurch, church

		};

		int option = JOptionPane.showConfirmDialog(this, scc, "Select Church to proceed", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE);

		// If ok button clicked, get the inputs from the text fields
		if (option == JOptionPane.OK_OPTION) {
			Choice chcscc = new Choice();

			churchName = church.getSelectedItem();
			{
				// Insert new scc to the selected church
				try {
					sql = "select * from SCC where Church=?";
					stmt = con.prepareStatement(sql);
					stmt.setString(1, churchName);
					rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						chcscc.addItem(rs.getString("Name"));
					}
					Object[] obj2 = { new JLabel("SCC Name:"), chcscc };
					int opt2 = JOptionPane.showConfirmDialog(this, obj2, "Select scc to delete",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

					if (opt2 == JOptionPane.OK_OPTION) {
						SCC = chcscc.getSelectedItem();
						sql = "delete from SCC where Name=?;";
						try {
							stmt = con.prepareStatement(sql);
							stmt.setString(1, SCC);
							stmt.executeUpdate();
							showScc();
						} catch (Exception e) {
							JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
						}
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}

			txtsccName.setText("");
			txtChurch.setText("");
		}
	}

	/********************
	 * method to list scc
	 ********************/
	protected void showScc() {
		parent.removeAll();
		ResultSet rs = null;
		try {
			String sql = "select * from SCC;";
			stmt = con.prepareStatement(sql);
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
			t.setAutoCreateRowSorter(true);
			t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			JPanel table = new JPanel();

			JScrollPane sp = new JScrollPane(t);
			sp.setBorder(BorderFactory.createTitledBorder("LIST OF SMALL CHRISTIAN COMMUNITIES"));
			table.add(sp);
			parent.add(table, BorderLayout.CENTER);
			frame.pack();
			frame.repaint();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	// **************
	protected void editRegistration() {
		JLabel labBap = new JLabel("Baptismal Name:");
		JLabel labOtherName = new JLabel("Other Name:");
		JTextField txtBap = new JTextField(10);
		JTextField txtOtherName = new JTextField(10);
		JLabel labScc = new JLabel("SCC:");
		JTextField txtScc = new JTextField(10);
		JLabel labStatus = new JLabel("Status:");
		JLabel labRegDate = new JLabel("Registration Date:");
		JTextField txtRegDate = new JTextField(10);

		Choice chcStatus = new Choice();
		chcStatus.add("Alive");
		chcStatus.add("Dead");
		chcStatus.select("Alive");
		Object[] inputfields = { LaBRegNo, txtReg, labBap, txtBap, labOtherName, txtOtherName, LaBconfName,
				txtConfirName, labScc, txtScc, labChurch, txtChurch, labStatus, chcStatus, labRegDate, txtRegDate };
		int option = JOptionPane.showConfirmDialog(this, inputfields, "Enter new Registration details",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		if (option == JOptionPane.OK_OPTION) {
			regNo = txtName.getText();
			txtBap.getText().toString();
			txtOtherName.getText().toString();
			confirName = txtConfirName.getText().toString();
			txtScc.getText().toString();
			chrchName = txtChurch.getText().toString();
			chcStatus.getSelectedItem();
			txtRegDate.getText().toString();

			// reset text fields after collecting data
			txtName.setText("");
			txtConfirName.setText("");
			txtBap.setText("");
			txtScc.setText("");
			txtOtherName.setText("");
			txtChurch.setText("");
			chcStatus.select(0);
			txtRegDate.setText("");
		}
	}

	/**************************************************************************
	 * 
	 * Method to connect to the database system
	 * 
	 **************************************************************************/
	public void Connect() {
		con = null;

		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);

			if (con != null) {
				connected = true;
				String loged = "You are Logged in as " + USER.toLowerCase();
				msg.setText(loged);
				msg.setForeground(Color.black);// color of text
				String title = "WELCOME " + USER.toUpperCase() + ".";
				hdr.setText(title);
				hdr.setForeground(Color.red);
				background.remove(logpanel);
				btns.add(logOut);
				background.add(btns, BorderLayout.NORTH);
				// add parent panel to background(parent) label
				background.add(parent, BorderLayout.CENTER);

				// clear textfields
				txtUser.setText("");
				txtPass.setText("");

				// for debugging purpose
				System.out.println(USER.toUpperCase() + " LOGGED IN.\n");

				frame.setJMenuBar(bar);
			}

		}
		// catch sql exceptions
		catch (SQLException se) {
			// handle errors for JDBC
			msg.setForeground(Color.red);// color of text
			msg.setText("ERROR: Incorrect login credentials!");
			se.printStackTrace();
		} catch (Exception e) {
			// handle errors for class.forName
			e.printStackTrace();
		}

	}

	// main method
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame.setDefaultLookAndFeelDecorated(true);
				new Index();
			}
		});

	}
}
