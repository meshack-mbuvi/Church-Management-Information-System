package databaseApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

public class Payment_Modes extends VariablesDefinitions {

	private static final long serialVersionUID = -4790837382666453432L;

	private JTextField txtname;
	private String sql = null;
	private JComboBox<Integer> codes;
	JPanel modes;

	// construtor
	public Payment_Modes() {
		txtname = new JTextField(10);
		codes = new JComboBox<Integer>();
		AutoCompleteDecorator.decorate(codes);
	}

	public void newMode() {
		Object[] modes = { new JLabel("New Payment mode:"), txtname };
		JButton btnSave = new JButton("Save");
		txtname.setText("Name of payment mode here");
		txtname.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {

			}

			@Override
			public void focusGained(FocusEvent e) {
				txtname.setText("");

			}
		});

		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sql = "INSERT INTO `ModeOfPayment`(`Name`) VALUES (?);";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					stmt.setString(1, txtname.getText().trim().toUpperCase());
					stmt.executeUpdate();
					txtname.setText("Name of payment mode here");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}

			}
		});

		JOptionPane.showOptionDialog(this, modes, "New Payment mode", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { btnSave }, null);
	}

	/**************************************
	 * edit modes of payment
	 */
	public void editMode() {
		if (codes.getItemCount() > 0) {
			codes.removeAllItems();
		}
		// populate codes for existing payment modes
		sql = "select code from ModeOfPayment;";
		try {
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				codes.addItem(rs.getInt("Code"));
			}

		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
		codes.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				sql = "select Name from ModeOfPayment where Code=? ;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(codes.getSelectedItem().toString()));
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtname.setText(rs.getString("Name"));
					}

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});

		Object[] modes = { new JLabel("Code"), codes, new JLabel("Name:"), txtname };
		JButton btnSave = new JButton("update");

		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sql = "update `ModeOfPayment` set `Name`=? where Code=?;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					stmt.setString(1, txtname.getText().trim().toUpperCase());
					stmt.setInt(2, Integer.parseInt(codes.getSelectedItem().toString().trim()));
					stmt.executeUpdate();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}

			}
		});

		JOptionPane.showOptionDialog(this, modes, "Edit Payment modes", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { btnSave }, null);
	}

	/*****************************************************************
	 * delete payment modes
	 */
	public void delMode() {

		if (codes.getItemCount() > 0) {
			codes.removeAllItems();
		}
		txtname.setEditable(false);
		// populate codes for existing payment modes
		sql = "select code from ModeOfPayment;";
		try {
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				codes.addItem(rs.getInt("Code"));
			}

		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
		codes.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				sql = "select Name from ModeOfPayment where Code=? ;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(codes.getSelectedItem().toString()));
					ResultSet rs = stmt.executeQuery();
					rs.beforeFirst();
					while (rs.next()) {
						txtname.setText(rs.getString("Name"));
					}

				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});

		Object[] modes = { new JLabel("Code"), codes, new JLabel("Name:"), txtname };
		JButton btnSave = new JButton("Delete");

		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sql = "delete from `ModeOfPayment` where Code=?;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(codes.getSelectedItem().toString().trim()));
					stmt.executeUpdate();
					codes.removeItem(codes.getSelectedItem());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}

			}
		});

		JOptionPane.showOptionDialog(this, modes, "Delete Payment modes", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { btnSave }, null);
	}

	public void showMode() {
		modes = new JPanel();
		JScrollPane sp = new JScrollPane();
		sql = "SELECT `Code`, `Name` FROM `ModeOfPayment` WHERE 1;";
		ResultSet rs = null;
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
			t.setCellSelectionEnabled(true);
			t.setToolTipText("Payment modes");
			sp = new JScrollPane(t);
			String title = "Payment modes";
			sp.setBorder(BorderFactory.createTitledBorder(title.toUpperCase()));
			modes.add(sp);
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
