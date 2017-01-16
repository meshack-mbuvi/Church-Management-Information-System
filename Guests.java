package databaseApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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

/***************************************************
 * This class contains data and methods necessary to manipulate field for guests
 * It contains method to create new,edit existing and delete guests
 * 
 * @author mbuvi
 *
 */
@SuppressWarnings("serial")
public class Guests extends VariablesDefinitions{

	private JTextField txtFirstName,txtSecondName,txtId,txtPhone;
	private String sql=null;
	private ResultSet rs=null;
	private JComboBox<Integer> ID;
	
	public JPanel guest;
	public Guests() {
		txtFirstName=new JTextField(10);
		txtSecondName=new JTextField(10);
		txtId=new JTextField(10);
		txtPhone=new JTextField(10);
		
		ID=new JComboBox<Integer>();
		AutoCompleteDecorator.decorate(ID);
		
		guest=new JPanel();
	}
	//create new guest
	public void newGuest(){
		txtId.setText("Type Identity number of guest here!");
		txtId.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				txtId.setText("");
			}
		});
		
		txtFirstName.setText("Type First name of guest here!");
		txtFirstName.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				txtFirstName.setText("");
			}
		});
		
		txtSecondName.setText("Type Second name of guest here!");
		txtSecondName.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				txtSecondName.setText("");
			}
		});
		
		txtPhone.setText("Type Phone number of guest here!");
		txtPhone.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				txtPhone.setText("");
			}
		});
		Object [] guests={
				new JLabel("ID NO:"),txtId,
				new JLabel("FIRST NAME:"),txtFirstName,
				new JLabel("SECOND NAME:"),txtSecondName,
				new JLabel("PHONE NO:"),txtPhone,
				
		};
		
		JButton btnSave=new JButton("save");
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sql="INSERT INTO `Guests`(`RegNo`,`FirstName`,`SecondName`,`Phone`) "
						+ "VALUES (?,?,?,?)";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(txtId.getText().trim()));
					stmt.setString(2, txtFirstName.getText().trim().toUpperCase());
					stmt.setString(3, txtSecondName.getText().trim().toUpperCase());
					stmt.setInt(4, Integer.parseInt(txtPhone.getText().trim()));
					
					stmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record inserted.");
					//reset fields to default
					txtId.setText("Type Identity number of guest here!");
					txtFirstName.setText("Type First name of guest here!");
					txtSecondName.setText("Type Second name of guest here!");
					txtPhone.setText("Type Phone number of guest here!");
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}

				
			}
		});
		JOptionPane.showOptionDialog(this, guests, "New Guest detatils", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { btnSave }, null);
		
		//reset textfields before exiting
		txtFirstName.setText("");
		txtSecondName.setText("");
		txtId.setText("");
		txtPhone.setText("");
		sql="";
	}
	
	//edit existing guest
	public void editGuest()
	{
		sql="select RegNo from Guests;";
		try {
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);			
			rs=stmt.executeQuery();
			rs.beforeFirst();
			while(rs.next())
			{
				ID.addItem(rs.getInt("RegNo"));
			}
			sql="select FirstName,SecondName,Phone from Guests where RegNo=?;";
			stmt=con.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString().trim()));
			rs=stmt.executeQuery();
			rs.beforeFirst();
			while(rs.next()){
				txtFirstName.setText(rs.getString("FirstName"));
				txtSecondName.setText(rs.getString("SecondName"));
				txtPhone.setText(rs.getString("Phone"));
			}
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
		
		ID.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sql="select FirstName,SecondName,Phone from Guests where RegNo=?;";
				try {
					stmt=con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString().trim()));
					rs=stmt.executeQuery();
					rs.beforeFirst();
					while(rs.next()){
						txtFirstName.setText(rs.getString("FirstName"));
						txtSecondName.setText(rs.getString("SecondName"));
						txtPhone.setText(rs.getString("Phone"));
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		
		txtFirstName.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				txtFirstName.setText("");
			}
		});
		txtSecondName.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				txtSecondName.setText("");
			}
		});
		
		txtPhone.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				txtPhone.setText("");
			}
		});
		
		//select existing detatils
		
		Object [] guests={
				new JLabel("ID NO:"),ID,
				new JLabel("FIRST NAME:"),txtFirstName,
				new JLabel("SECOND NAME:"),txtSecondName,
				new JLabel("PHONE NO:"),txtPhone,
				
		};
		
		JButton btnEdit=new JButton("update");
		btnEdit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sql="UPDATE `Guests` SET `FirstName`=?,`SecondName`=?,"
						+ "`Phone`=? where RegNo=?;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					
					stmt.setString(1, txtFirstName.getText().trim().toUpperCase());
					stmt.setString(2, txtSecondName.getText().trim().toUpperCase());
					stmt.setInt(3, Integer.parseInt(txtPhone.getText().trim()));
					stmt.setInt(4, Integer.parseInt(ID.getSelectedItem().toString().trim()));
					
					stmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record updated.");
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
					
				}

				
			}
		});
		JOptionPane.showOptionDialog(this, guests, "Edit details of guest", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { btnEdit }, null);
		
		//reset textfields before exiting
		txtFirstName.setText("");
		txtSecondName.setText("");
		txtId.setText("");
		txtPhone.setText("");
	}
	
	//delete guest
	public void delGuest()
	{

		sql="select RegNo from Guests;";
		try {
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			stmt = con.prepareStatement(sql);			
			rs=stmt.executeQuery();
			rs.beforeFirst();
			while(rs.next())
			{
				ID.addItem(rs.getInt("RegNo"));
			}
			sql="select FirstName,SecondName,Phone from Guests where RegNo=?;";
			stmt=con.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString().trim()));
			rs=stmt.executeQuery();
			rs.beforeFirst();
			while(rs.next()){
				txtFirstName.setText(rs.getString("FirstName"));
				txtSecondName.setText(rs.getString("SecondName"));
				txtPhone.setText(rs.getString("Phone"));
			}
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
		
		ID.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sql="select FirstName,SecondName,Phone from Guests where RegNo=?;";
				try {
					stmt=con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString().trim()));
					rs=stmt.executeQuery();
					rs.beforeFirst();
					while(rs.next()){
						txtFirstName.setText(rs.getString("FirstName"));
						txtSecondName.setText(rs.getString("SecondName"));
						txtPhone.setText(rs.getString("Phone"));
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		
		txtFirstName.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				txtFirstName.setText("");
			}
		});
		txtSecondName.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				txtSecondName.setText("");
			}
		});
		
		txtPhone.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {}
			
			@Override
			public void focusGained(FocusEvent e) {
				txtPhone.setText("");
			}
		});
		
		//select existing detatils
		
		Object [] guests={
				new JLabel("ID NO:"),ID,
				new JLabel("FIRST NAME:"),txtFirstName,
				new JLabel("SECOND NAME:"),txtSecondName,
				new JLabel("PHONE NO:"),txtPhone,
				
		};
		
		JButton btnDel=new JButton("delete record");
		btnDel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sql="delete from `Guests` where RegNo=?;";
				try {
					Class.forName(DRIVER_CLASS);
					// open connection
					con = DriverManager.getConnection(URL, USER, PASSWORD);
					stmt = con.prepareStatement(sql);
					
					stmt.setInt(1, Integer.parseInt(ID.getSelectedItem().toString().trim()));
					
					stmt.executeUpdate();
					JOptionPane.showMessageDialog(null, "Record deleted.");
					ID.removeItem(ID.getSelectedItem());
					
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
					
				}

				
			}
		});
		JOptionPane.showOptionDialog(this, guests, "Delete guest", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] { btnDel }, null);
		
		//reset textfields before exiting
		txtFirstName.setText("");
		txtSecondName.setText("");
		txtId.setText("");
		txtPhone.setText("");
	}
	//list guests
	public void showGuests()
	{
		guest = new JPanel();
		JScrollPane sp = new JScrollPane();
		sql = "SELECT `RegNo`, `FirstName`,SecondName,Phone FROM Guests WHERE 1;";
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
			String colNames[] = {
					"ID Number","First Name","Second Name","Phone"
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
			t.setCellSelectionEnabled(true);
			t.setToolTipText("Table of currently registered guests");
			sp = new JScrollPane(t);
			String title = "Table of guests";
			sp.setBorder(BorderFactory.createTitledBorder(title.toUpperCase()));
			guest.add(sp);
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
