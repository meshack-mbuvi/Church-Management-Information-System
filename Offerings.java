package databaseApp;

import java.awt.Choice;
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

public class Offerings extends VariablesDefinitions {

	private static final long serialVersionUID = -1721484333426873135L;

	private JTextField txtPayCode, txtAmount;
	private Choice chcModeOfPayment, chcOutstation;
	private JComboBox<String> month, year;
	private String sql;

	JPanel panForm;
	
	JScrollPane sp;

	public Offerings() {

		txtPayCode = new JTextField(10);
		txtAmount = new JTextField(10);
		month = new JComboBox<String>();
		AutoCompleteDecorator.decorate(month);
		year = new JComboBox<String>();
		AutoCompleteDecorator.decorate(year);
		year.setEditable(true);
		month.setEditable(true);
		sql = null;
	}

	public void newOffering() {
		
		if (month.getItemCount() > 0) {
			month.removeAllItems();
		}
		String[] monthItems = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
		for (int i = 0; i < monthItems.length; i++) {
			month.addItem(monthItems[i]);
		}
		if (year.getItemCount() > 0) {
			year.removeAllItems();
		}
		// Just testing things
		int yr = 2000;
		for (int i = 0; i < 500; i++) {
			year.addItem(String.valueOf(yr + i));
		}
		chcModeOfPayment = new Choice();
		ResultSet rs;
		sql = "select Name from ModeOfPayment;";
		try {
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcModeOfPayment.addItem(rs.getString("Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

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
				chcOutstation.addItem(rs.getString("C_Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sql = "INSERT INTO `Offering`(`Church`,`Year`,`Month`,`Amount`,`ModeOfPayment`," + "`TransactionCode`) "
						+ "VALUES (?,?,?,?,?,?);";
				try {
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chcOutstation.getSelectedItem().trim().toUpperCase());
					stmt.setString(2, year.getSelectedItem().toString().trim().toUpperCase());
					stmt.setString(3, month.getSelectedItem().toString().trim().toUpperCase());
					stmt.setDouble(4, Double.parseDouble(txtAmount.getText().trim()));
					stmt.setString(5, chcModeOfPayment.getSelectedItem().trim().toUpperCase());
					stmt.setString(6, txtPayCode.getText().trim().toUpperCase());
					stmt.executeUpdate();
					txtAmount.setText("0.00");
					txtPayCode.setText("");
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}

			}
		});
		chcOutstation.setEnabled(true);
		year.setEnabled(true);
		month.setEnabled(true);
		txtAmount.setEditable(true);
		chcModeOfPayment.setEnabled(true);
		txtPayCode.setEditable(true);
		
		txtAmount.setText("0.00");
		panForm = new JPanel(new GridLayout(6, 2, 5, 5));
		panForm.add(new JLabel("Outstation Name:"));
		panForm.add(chcOutstation);

		panForm.add(new JLabel("Year:"));
		panForm.add(year);

		panForm.add(new JLabel("Month:"));
		panForm.add(month);

		panForm.add(new JLabel("Amount:"));
		panForm.add(txtAmount);

		panForm.add(new JLabel("Mode Of payment:"));
		panForm.add(chcModeOfPayment);

		panForm.add(new JLabel("Transaction Code:"));
		panForm.add(txtPayCode);

		Object[] offering = { panForm };
		JOptionPane.showOptionDialog(this, offering, "Offering/sadaka", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { btnSave }, null);

	}

	public void editOffering() {
		if (month.getItemCount() > 0) {
			month.removeAllItems();
		}
		String[] monthItems = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
		for (int i = 0; i < monthItems.length; i++) {
			month.addItem(monthItems[i]);
		}
		if (year.getItemCount() > 0) {
			year.removeAllItems();
		}
		// Just testing things
		int yr = 2000;
		for (int i = 0; i < 500; i++) {
			year.addItem(String.valueOf(yr + i));
		}
		chcModeOfPayment = new Choice();
		ResultSet rs;
		sql = "select Name from ModeOfPayment;";
		try {
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcModeOfPayment.addItem(rs.getString("Name"));
			}
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		chcOutstation = new Choice();
		chcOutstation.setEnabled(false);
		sql = "select C_Name from Outstation;";
		try {
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcOutstation.addItem(rs.getString("C_Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		final JComboBox<Integer> count = new JComboBox<Integer>();

		sql = "SELECT `Count`,`Church`, `Year`, `Month`, " + "`Amount`, `ModeOfPayment`, "
				+ "`TransactionCode` FROM `Offering` WHERE 1;";
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				count.addItem(rs.getInt("Count"));
			}
			count.setSelectedIndex(0);

			sql = "SELECT `Church`, `Year`, `Month`, " + "`Amount`, `ModeOfPayment`, "
					+ "`TransactionCode` FROM `Offering` WHERE Count=?;";

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(count.getSelectedItem().toString()));
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcOutstation.select(rs.getString("Church"));
				year.setSelectedItem(rs.getDate("Year"));
				txtAmount.setText(String.valueOf(rs.getDouble("Amount")));
				chcModeOfPayment.select(rs.getString("ModeOfPayment"));
				txtPayCode.setText(rs.getString("TransactionCode"));
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		count.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				sql = "SELECT `Church`, `Year`, `Month`, " + "`Amount`, `ModeOfPayment`, "
						+ "`TransactionCode` FROM `Offering` WHERE Count=?;";
				try {
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(count.getSelectedItem().toString()));
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						chcOutstation.select(rs.getString("Church"));
						year.setSelectedItem(rs.getObject("Year"));
						txtAmount.setText(String.valueOf(rs.getDouble("Amount")));
						chcModeOfPayment.select(rs.getString("ModeOfPayment"));
						txtPayCode.setText(rs.getString("TransactionCode"));
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});

		JButton btnUpdate = new JButton("update");
		btnUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sql = "update `Offering` set `Year`=?,`Month`=?,`Amount`=?," + "`ModeOfPayment`=?,"
						+ "`TransactionCode`=? where Count=?;";
				try {
					stmt = con.prepareStatement(sql);

					stmt.setString(1, year.getSelectedItem().toString().trim().toUpperCase());
					stmt.setString(2, month.getSelectedItem().toString().trim().toUpperCase());
					stmt.setDouble(3, Double.parseDouble(txtAmount.getText().trim()));
					stmt.setString(4, chcModeOfPayment.getSelectedItem().trim().toUpperCase());
					stmt.setString(5, txtPayCode.getText().trim().toUpperCase());
					stmt.setInt(6, Integer.parseInt(count.getSelectedItem().toString()));

					// todo something here
					stmt.executeUpdate();
					txtAmount.setText("0.00");
					txtPayCode.setText("");
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}

			}
		});
		
		panForm = new JPanel(new GridLayout(7, 2, 5, 5));
		panForm.add(new JLabel("Count:"));
		panForm.add(count);
		panForm.add(new JLabel("Outstation Name:"));
		panForm.add(chcOutstation);

		panForm.add(new JLabel("Year:"));
		panForm.add(year);

		panForm.add(new JLabel("Month:"));
		panForm.add(month);

		panForm.add(new JLabel("Amount:"));
		panForm.add(txtAmount);

		panForm.add(new JLabel("Mode Of payment:"));
		panForm.add(chcModeOfPayment);

		panForm.add(new JLabel("Transaction Code:"));
		panForm.add(txtPayCode);

		Object[] offering = { panForm };
		JOptionPane.showOptionDialog(this, offering, "Offering/sadaka", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { btnUpdate }, null);

	}
	
	public void delOffering(){
		if (month.getItemCount() > 0) {
			month.removeAllItems();
		}
		String[] monthItems = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
		for (int i = 0; i < monthItems.length; i++) {
			month.addItem(monthItems[i]);
		}
		if (year.getItemCount() > 0) {
			year.removeAllItems();
		}
		// Just testing things
		int yr = 2000;
		for (int i = 0; i < 500; i++) {
			year.addItem(String.valueOf(yr + i));
		}
		chcModeOfPayment = new Choice();
		ResultSet rs;
		sql = "select Name from ModeOfPayment;";
		try {
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcModeOfPayment.addItem(rs.getString("Name"));
			}
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		chcOutstation = new Choice();
		chcOutstation.setEnabled(false);
		sql = "select C_Name from Outstation;";
		try {
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcOutstation.addItem(rs.getString("C_Name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		final JComboBox<Integer> count = new JComboBox<Integer>();

		sql = "SELECT `Count`,`Church`, `Year`, `Month`, " + "`Amount`, `ModeOfPayment`, "
				+ "`TransactionCode` FROM `Offering` WHERE 1;";
		try {
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				count.addItem(rs.getInt("Count"));
			}
			count.setSelectedIndex(0);

			sql = "SELECT `Church`, `Year`, `Month`, " + "`Amount`, `ModeOfPayment`, "
					+ "`TransactionCode` FROM `Offering` WHERE Count=?;";

			stmt = con.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(count.getSelectedItem().toString()));
			rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				chcOutstation.select(rs.getString("Church"));
				year.setSelectedItem(rs.getDate("Year"));
				txtAmount.setText(String.valueOf(rs.getDouble("Amount")));
				chcModeOfPayment.select(rs.getString("ModeOfPayment"));
				txtPayCode.setText(rs.getString("TransactionCode"));
			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		count.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				sql = "SELECT `Church`, `Year`, `Month`, " + "`Amount`, `ModeOfPayment`, "
						+ "`TransactionCode` FROM `Offering` WHERE Count=?;";
				try {
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(count.getSelectedItem().toString()));
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						chcOutstation.select(rs.getString("Church"));
						year.setSelectedItem(rs.getObject("Year"));
						txtAmount.setText(String.valueOf(rs.getDouble("Amount")));
						chcModeOfPayment.select(rs.getString("ModeOfPayment"));
						txtPayCode.setText(rs.getString("TransactionCode"));
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});

		JButton btnUpdate = new JButton("delete");
		btnUpdate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sql = "delete from `Offering` where Count=?;";
				try {
					stmt = con.prepareStatement(sql);

					stmt.setInt(1, Integer.parseInt(count.getSelectedItem().toString()));

					// todo something here
					stmt.executeUpdate();
					count.removeItem(count.getSelectedItem());
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}

			}
		});
		
		chcOutstation.setEnabled(false);
		year.setEnabled(false);
		month.setEnabled(false);
		txtAmount.setEditable(false);
		chcModeOfPayment.setEnabled(false);
		txtPayCode.setEditable(false);
		panForm = new JPanel(new GridLayout(7, 2, 5, 5));
		panForm.add(new JLabel("Count:"));
		panForm.add(count);
		panForm.add(new JLabel("Outstation Name:"));
		panForm.add(chcOutstation);

		panForm.add(new JLabel("Year:"));
		panForm.add(year);

		panForm.add(new JLabel("Month:"));
		panForm.add(month);

		panForm.add(new JLabel("Amount:"));
		panForm.add(txtAmount);

		panForm.add(new JLabel("Mode Of payment:"));
		panForm.add(chcModeOfPayment);

		panForm.add(new JLabel("Transaction Code:"));
		panForm.add(txtPayCode);

		Object[] offering = { panForm };
		JOptionPane.showOptionDialog(this, offering, "Offering/sadaka", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { btnUpdate }, null);

	}
	
	public void showOffering(){
		sp=new JScrollPane();
		ResultSet rs=null;
		sql="select Offering.Date," + "Offering.Church,"
				+ "Offering.Year," + "Offering.Month,"+ "Offering.Amount,"
				+ "Offering.ModeOfPayment,"
				+ "Offering.TransactionCode from Offering;";
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
			String colNames[] = { "Date ", "Church", "Year", "Month","Amount", "Payment Mode",
					"Transaction code" };
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
			t.setToolTipText("All offerings/sadaka");
			sp = new JScrollPane(t);
			String title = "All offerings/sadaka";
			sp.setBorder(BorderFactory.createTitledBorder(title.toUpperCase()));
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
