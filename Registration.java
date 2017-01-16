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
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

public class Registration extends VariablesDefinitions {

	/**
	 * This class provides methods concerning administration of believers The
	 * methods involved are: 1.newRegistration(): used to register new believers
	 * 2.editRegistration(): as its name suggests 3.showRegistered(): lists all
	 * registered members from registration table 4.searchRegistered():
	 */
	private static final long serialVersionUID = 478836362724640016L;

	private JLabel labBap;
	private JTextField txtBap;
	private JTextField txtOtherName;
	private JPanel groups;
	// checkboxes for group names present
	private JCheckBox chkYouth, chkChoir;
	private JRadioButton chkCma, chkcwa;
	private ButtonGroup btngroup;
	
	private JComboBox<Integer> Reg;
	
	JScrollPane sp;

	// constructor
	public Registration() {
		labBap = new JLabel("Baptismal Name:");
		txtBap = new JTextField(5);
		txtOtherName = new JTextField(5);

		chkYouth = new JCheckBox("YOUTH");
		chkChoir = new JCheckBox("CHOIR");

		btngroup = new ButtonGroup();
		// mutually exclusive options
		chkCma = new JRadioButton("CMA");
		chkcwa = new JRadioButton("CWA");

		chkcwa.setSelected(true);
		groups = new JPanel();
		
		Reg=new JComboBox<Integer>();
		Reg.setBackground(Color.white);
		AutoCompleteDecorator.decorate(Reg);

	}

	public void insertRegistration() {
		btngroup.add(chkcwa);
		btngroup.add(chkCma);

		groups.setBorder(BorderFactory.createTitledBorder("Register for various groups here"));
		groups.add(chkChoir);
		groups.add(chkYouth);
		groups.add(chkCma);
		groups.add(chkcwa);
		
		JButton btnSave=new JButton("Save");
		btnSave.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		btnSave.setBackground(Color.white);

		JPanel panel = new JPanel(new GridLayout(7, 2, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("PERSONAL DETAILS"));


		final Choice chcStatus = new Choice();
		chcStatus.add("Alive");
		chcStatus.add("Dead");
		chcStatus.select("Alive");

		final Choice chcChurches = new Choice();
		chcChurches.setBackground(Color.white);
		final Choice chcZone = new Choice();
		chcZone.setBackground(Color.white);
		final Choice chcSCC = new Choice();
		chcSCC.setBackground(Color.white);
		chcChurches.removeAll();

		panel.add(labBap);
		panel.add(txtBap);
		panel.add(labOtherName);
		panel.add(txtOtherName);
		panel.add(LaBconfName);
		panel.add(txtConfirName);
		panel.add(new JLabel("Zone"));
		panel.add(chcZone);
		panel.add(labChurch);
		panel.add(chcChurches);
		panel.add(new JLabel("SCC:"));
		panel.add(chcSCC);
		panel.add(new JLabel("Status:"));
		panel.add(chcStatus);

		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			ResultSet rs = null;
			String sql = "select * from Zone;";
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				chcZone.add(rs.getString("Name"));
			}
			
			//populate church choice item
			chcZone.select(0);
			rs = null;
			sql = "select * from Outstation where Zone=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, chcZone.getSelectedItem());
			rs = stmt.executeQuery();
			while (rs.next()) {
				chcChurches.add(rs.getString("C_Name"));
			}
			//populate scc choice item
			chcChurches.select(0);
			sql = "select * from SCC where Church=?;";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, chcChurches.getSelectedItem());
			rs = stmt.executeQuery();
			while (rs.next()) {
				chcSCC.add(rs.getString("Name"));
			}
		} catch (SQLException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		//item listener for church
		chcChurches.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				String sql = "select * from SCC where Church=?;";
				if(chcSCC.getItemCount()>0){
					chcSCC.removeAll();
				}
				try {
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chcChurches.getSelectedItem());
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						chcSCC.add(rs.getString("Name"));
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				
			}
		});

		
		//item listener for zones
		chcZone.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(chcChurches.getItemCount()>0)
				{
					chcChurches.removeAll();
				}
				ResultSet rs = null;
				String sql = "select * from Outstation where Zone=?;";
				try {
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chcZone.getSelectedItem());
					rs = stmt.executeQuery();
					while (rs.next()) {
						chcChurches.add(rs.getString("C_Name"));
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});

		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * continue to perform insertion
				 */
				if(!(txtOtherName.getText().toString().trim().isEmpty()))
				{
					String sql = "INSERT INTO `Registration`(`BaptismalName`, `OtherNames`,"
							+ " `confirmationName`, `scc`, `Church`, `Zone`, `status`) "
							+ "VALUES (?,?,?,?,?,?,?)";
					try {
						stmt = (PreparedStatement) con.prepareStatement(sql);
						stmt.setString(1, txtBap.getText().toString().trim().toUpperCase());
						stmt.setString(2, txtOtherName.getText().toString().trim().toUpperCase());
						stmt.setString(3, txtConfirName.getText().toString().trim().toUpperCase());
						stmt.setString(4, chcSCC.getSelectedItem().trim().toUpperCase());
						stmt.setString(5, chcChurches.getSelectedItem().trim().toUpperCase());
						stmt.setString(6, chcZone.getSelectedItem().trim().toUpperCase());
						stmt.setString(7, chcStatus.getSelectedItem().trim().toUpperCase());
						stmt.executeUpdate();
						
						//update groups
						if(chkCma.isSelected())
						{
							
						}
						//update number of christians in outstation
						int count=0;
						sql="select count(RegNo) from Registration where Church=?;";
						stmt = (PreparedStatement) con.prepareStatement(sql);
						stmt.setString(1, chcChurches.getSelectedItem().trim().toUpperCase());
						ResultSet rs=stmt.executeQuery();
						rs.beforeFirst();
						while(rs.next()){
							count=rs.getInt("count(Regno)");
						}
						sql="update Outstation set `TotalChristians`=? where C_Name=?";
						stmt = (PreparedStatement) con.prepareStatement(sql);
						stmt = (PreparedStatement) con.prepareStatement(sql);
						stmt.setInt(1, count);
						stmt.setString(2, chcChurches.getSelectedItem().trim().toUpperCase());
						stmt.executeUpdate();
						
						
						count=0;
						
						//update number of christians in zone
						
						sql="select count(RegNo) from Registration where Zone=?;";
						stmt = (PreparedStatement) con.prepareStatement(sql);
						stmt.setString(1, chcZone.getSelectedItem().trim().toUpperCase());
						rs=stmt.executeQuery();
						rs.beforeFirst();
						while(rs.next()){
							count=rs.getInt("count(Regno)");
						}
						sql="update Zone set `xtianCount`=? where Name=?";
						stmt = (PreparedStatement) con.prepareStatement(sql);
						stmt = (PreparedStatement) con.prepareStatement(sql);
						stmt.setInt(1, count);
						stmt.setString(2, chcZone.getSelectedItem().trim().toUpperCase());
						stmt.executeUpdate();
						
						JOptionPane.showMessageDialog(null, "Record created!");
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
				}
				else {
					JOptionPane.showMessageDialog(null, "Othername field cannot be empty!");
				}
				
			}
		});
		Object[] inputfields = { panel, groups };
		
		JOptionPane.showOptionDialog(this, inputfields, "Register new believer", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] {btnSave}, null);
		
		// reset text fields after collecting data
		txtName.setText("");
		txtConfirName.setText("");
		txtBap.setText("");
		txtOtherName.setText("");
	}
	public void editRegistration(){
		btngroup.add(chkcwa);
		btngroup.add(chkCma);

		groups.setBorder(BorderFactory.createTitledBorder("Register for various groups here"));
		groups.add(chkChoir);
		groups.add(chkYouth);
		groups.add(chkCma);
		groups.add(chkcwa);
		
		
		
		JButton btnEdit=new JButton("Update");
		btnEdit.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		btnEdit.setBackground(Color.white);

		JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("PERSONAL DETAILS"));


		final Choice chcStatus = new Choice();
		chcStatus.add("Alive");
		chcStatus.add("Dead");
		chcStatus.select("Alive");

		final Choice chcChurches = new Choice();
		chcChurches.setBackground(Color.white);
		final Choice chcZone = new Choice();
		chcZone.setBackground(Color.white);
		final Choice chcSCC = new Choice();
		chcSCC.setBackground(Color.white);
		chcChurches.removeAll();

		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			ResultSet rs = null;
			String sql = "select * from Zone;";
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				chcZone.add(rs.getString("Name"));
			}
			
			//populate church choice item
			chcZone.select(0);
			rs = null;
			sql = "select * from Outstation where Zone=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, chcZone.getSelectedItem());
			rs = stmt.executeQuery();
			while (rs.next()) {
				chcChurches.add(rs.getString("C_Name"));
			}
			//populate scc choice item
			chcChurches.select(0);
			sql = "select * from SCC where Church=?;";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, chcChurches.getSelectedItem());
			rs = stmt.executeQuery();
			while (rs.next()) {
				chcSCC.add(rs.getString("Name"));
			}
		} catch (SQLException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		Reg=new JComboBox<Integer>();
		Reg.setBackground(Color.white);
		AutoCompleteDecorator.decorate(Reg);
		
		String sql="select * from Registration;";
		try {
			if(Reg.getItemCount()>0){
				Reg.removeAllItems();
			}
			stmt=con.prepareStatement(sql);
			ResultSet rs=stmt.executeQuery();
			rs.beforeFirst();
			while(rs.next()){
				Reg.addItem(rs.getInt("RegNo"));
			}
			Reg.setSelectedIndex(0);
			sql="select * from Registration where RegNo=?;";
			stmt=con.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(Reg.getSelectedItem().toString().trim()));
			rs=stmt.executeQuery();
			rs.beforeFirst();
			while(rs.next())
			{
				txtBap.setText(rs.getString("BaptismalName"));
				txtOtherName.setText(rs.getString("OtherNames"));
				txtConfirName.setText(rs.getString("confirmationName"));
				
				chcZone.select(rs.getString("Zone"));
				
				chcChurches.select(rs.getString("Church"));
				
				chcSCC.select(rs.getString("scc"));
				chcStatus.select(rs.getString("Status"));
				
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		//Registration numbers
		Reg.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				try {
					String sql="select * from Registration where RegNo=?;";
					stmt=con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(Reg.getSelectedItem().toString().trim()));
					ResultSet rs=stmt.executeQuery();
					rs.beforeFirst();
					while(rs.next())
					{
						txtBap.setText(rs.getString("BaptismalName"));
						txtOtherName.setText(rs.getString("OtherNames"));
						txtConfirName.setText(rs.getString("confirmationName"));
						
						chcZone.select(rs.getString("Zone"));
						
						chcChurches.select(rs.getString("Church"));
						
						chcSCC.select(rs.getString("scc"));
						chcStatus.select(rs.getString("Status"));
						
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});
		
		//item listener for church
		chcChurches.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				String sql = "select * from SCC where Church=?;";
				if(chcSCC.getItemCount()>0){
					chcSCC.removeAll();
				}
				try {
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chcChurches.getSelectedItem());
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						chcSCC.add(rs.getString("Name"));
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				
			}
		});

		
		//item listener for zones
		chcZone.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(chcChurches.getItemCount()>0)
				{
					chcChurches.removeAll();
				}
				ResultSet rs = null;
				String sql = "select * from Outstation where Zone=?;";
				try {
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chcZone.getSelectedItem());
					rs = stmt.executeQuery();
					while (rs.next()) {
						chcChurches.add(rs.getString("C_Name"));
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});

		btnEdit.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * continue to perform insertion
				 */
				if(!(txtOtherName.getText().toString().trim().isEmpty()))
				{
					String sql = "update Registration "
							+ "SET `BaptismalName`=?,`OtherNames`=?,`confirmationName`=?,"
							+ "`scc`=?,`Church`=?,"
							+ "`Zone`=?,`status`=? WHERE `Reg_No`=?";
					try {
						stmt = (PreparedStatement) con.prepareStatement(sql);;
						stmt.setString(1, txtBap.getText().toString().trim().toUpperCase());
						stmt.setString(2, txtOtherName.getText().toString().trim().toUpperCase());
						stmt.setString(3, txtConfirName.getText().toString().trim().toUpperCase());
						stmt.setString(4, chcSCC.getSelectedItem().trim().toUpperCase());
						stmt.setString(5, chcChurches.getSelectedItem().trim().toUpperCase());
						stmt.setString(6, chcZone.getSelectedItem().trim().toUpperCase());
						stmt.setString(7, chcStatus.getSelectedItem().trim().toUpperCase());
						stmt.setInt(8, Integer.parseInt(Reg.getSelectedItem().toString()));
						stmt.executeUpdate();
						
						//remember to update groups
						if(chkCma.isSelected())
						{
							
						}
						JOptionPane.showMessageDialog(null, "Record updated!");
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
					
				}
				else {
					JOptionPane.showMessageDialog(null, "Othername field cannot be empty!");
				}
				
			}
		});
		
		//add items to display panel
		panel.add(new JLabel("Registration NO:"));
		panel.add(Reg);
		panel.add(labBap);
		panel.add(txtBap);
		panel.add(labOtherName);
		panel.add(txtOtherName);
		panel.add(LaBconfName);
		panel.add(txtConfirName);
		panel.add(new JLabel("Zone"));
		panel.add(chcZone);
		panel.add(labChurch);
		panel.add(chcChurches);
		panel.add(new JLabel("SCC:"));
		panel.add(chcSCC);
		panel.add(new JLabel("Status:"));
		panel.add(chcStatus);
		Object[] inputfields = { panel, groups };
		
		JOptionPane.showOptionDialog(this, inputfields, "Register new believer", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] {btnEdit}, null);
		
		// reset text fields after collecting data
		txtName.setText("");
		txtConfirName.setText("");
		txtBap.setText("");
		txtOtherName.setText("");
	}
	public void delRegistration(){
		btngroup.add(chkcwa);
		btngroup.add(chkCma);

		groups.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		groups.add(chkChoir);
		groups.add(chkYouth);
		groups.add(chkCma);
		groups.add(chkcwa);
		
		
		
		JButton btnDel=new JButton("Delete");
		btnDel.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		btnDel.setBackground(Color.white);

		JPanel panel = new JPanel(new GridLayout(8, 2, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("PERSONAL DETAILS"));


		final Choice chcStatus = new Choice();
		chcStatus.add("Alive");
		chcStatus.add("Dead");
		chcStatus.select("Alive");

		final Choice chcChurches = new Choice();
		chcChurches.setBackground(Color.white);
		final Choice chcZone = new Choice();
		chcZone.setBackground(Color.white);
		final Choice chcSCC = new Choice();
		chcSCC.setBackground(Color.white);
		chcChurches.removeAll();

		try {
			// Register jdbc driver
			Class.forName(DRIVER_CLASS);
			// open connection
			con = DriverManager.getConnection(URL, USER, PASSWORD);
			ResultSet rs = null;
			String sql = "select * from Zone;";
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			while (rs.next()) {
				chcZone.add(rs.getString("Name"));
			}
			
			//populate church choice item
			chcZone.select(0);
			rs = null;
			sql = "select * from Outstation where Zone=?;";

			stmt = con.prepareStatement(sql);
			stmt.setString(1, chcZone.getSelectedItem());
			rs = stmt.executeQuery();
			while (rs.next()) {
				chcChurches.add(rs.getString("C_Name"));
			}
			//populate scc choice item
			chcChurches.select(0);
			sql = "select * from SCC where Church=?;";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, chcChurches.getSelectedItem());
			rs = stmt.executeQuery();
			while (rs.next()) {
				chcSCC.add(rs.getString("Name"));
			}
		} catch (SQLException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		
		Reg=new JComboBox<Integer>();
		Reg.setBackground(Color.white);
		AutoCompleteDecorator.decorate(Reg);
		
		String sql="select * from Registration;";
		try {
			if(Reg.getItemCount()>0){
				Reg.removeAllItems();
			}
			stmt=con.prepareStatement(sql);
			ResultSet rs=stmt.executeQuery();
			rs.beforeFirst();
			while(rs.next()){
				Reg.addItem(rs.getInt("RegNo"));
			}
			Reg.setSelectedIndex(0);
			sql="select * from Registration where RegNo=?;";
			stmt=con.prepareStatement(sql);
			stmt.setInt(1, Integer.parseInt(Reg.getSelectedItem().toString().trim()));
			rs=stmt.executeQuery();
			rs.beforeFirst();
			while(rs.next())
			{
				txtBap.setText(rs.getString("BaptismalName"));
				txtOtherName.setText(rs.getString("OtherNames"));
				txtConfirName.setText(rs.getString("confirmationName"));
				
				chcZone.select(rs.getString("Zone"));
				
				chcChurches.select(rs.getString("Church"));
				
				chcSCC.select(rs.getString("scc"));
				chcStatus.select(rs.getString("Status"));
				
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
		//Registration numbers
		Reg.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				try {
					String sql="select * from Registration where RegNo=?;";
					stmt=con.prepareStatement(sql);
					stmt.setInt(1, Integer.parseInt(Reg.getSelectedItem().toString().trim()));
					ResultSet rs=stmt.executeQuery();
					rs.beforeFirst();
					while(rs.next())
					{
						txtBap.setText(rs.getString("BaptismalName"));
						txtOtherName.setText(rs.getString("OtherNames"));
						txtConfirName.setText(rs.getString("confirmationName"));
						
						chcZone.select(rs.getString("Zone"));
						
						chcChurches.select(rs.getString("Church"));
						
						chcSCC.select(rs.getString("scc"));
						chcStatus.select(rs.getString("Status"));
						
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, e2.getMessage());
				}
			}
		});
		
		//item listener for church
		chcChurches.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				String sql = "select * from SCC where Church=?;";
				if(chcSCC.getItemCount()>0){
					chcSCC.removeAll();
				}
				try {
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chcChurches.getSelectedItem());
					ResultSet rs = stmt.executeQuery();
					while (rs.next()) {
						chcSCC.add(rs.getString("Name"));
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
				
			}
		});

		
		//item listener for zones
		chcZone.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if(chcChurches.getItemCount()>0)
				{
					chcChurches.removeAll();
				}
				ResultSet rs = null;
				String sql = "select * from Outstation where Zone=?;";
				try {
					stmt = con.prepareStatement(sql);
					stmt.setString(1, chcZone.getSelectedItem());
					rs = stmt.executeQuery();
					while (rs.next()) {
						chcChurches.add(rs.getString("C_Name"));
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage());
				}
			}
		});

		btnDel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * continue to perform insertion
				 */
				if(!(txtOtherName.getText().toString().trim().isEmpty()))
				{
					String sql = "delete from Registration  WHERE `RegNo`=?";
					try {
						stmt = (PreparedStatement) con.prepareStatement(sql);
						stmt.setInt(1, Integer.parseInt(Reg.getSelectedItem().toString()));
						stmt.executeUpdate();
						
						//TODO remember to update groups as well
						if(chkCma.isSelected())
						{
							
						}
						
						//update number of christians in zone
						int count=0;
						sql="select count(RegNo) from Registration where Zone=?;";
						stmt = (PreparedStatement) con.prepareStatement(sql);
						stmt.setString(1, chcZone.getSelectedItem().trim().toUpperCase());
						ResultSet rs=stmt.executeQuery();
						rs.beforeFirst();
						while(rs.next()){
							count=rs.getInt("count(Regno)");
						}
						sql="update Zone set `xtianCount`=? where Name=?";
						stmt = (PreparedStatement) con.prepareStatement(sql);
						stmt = (PreparedStatement) con.prepareStatement(sql);
						stmt.setInt(1, count);
						stmt.setString(2, chcZone.getSelectedItem().trim().toUpperCase());
						stmt.executeUpdate();
						
						JOptionPane.showMessageDialog(null, "Record deleted!");
						Reg.removeItem(Reg.getSelectedItem());
					} catch (SQLException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
					}
					
				}
				else {
					
				}
				
			}
		});
		
		//add items to display panel
		panel.add(new JLabel("Registration NO:"));
		panel.add(Reg);
		panel.add(labBap);
		panel.add(txtBap);
		panel.add(labOtherName);
		panel.add(txtOtherName);
		panel.add(LaBconfName);
		panel.add(txtConfirName);
		panel.add(new JLabel("Zone"));
		panel.add(chcZone);
		panel.add(labChurch);
		panel.add(chcChurches);
		panel.add(new JLabel("SCC:"));
		panel.add(chcSCC);
		panel.add(new JLabel("Status:"));
		panel.add(chcStatus);
		Object[] inputfields = { panel, groups };
		
		JOptionPane.showOptionDialog(this, inputfields, "Remove believer from record", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[] {btnDel}, null);
		
		// reset text fields after collecting data
		txtName.setText("");
		txtConfirName.setText("");
		txtBap.setText("");
		txtOtherName.setText("");
	}
	public void showRegistration(){
		String sql="SELECT `RegNo`, `RegDate`, `BaptismalName`, `OtherNames`, "
				+ "`confirmationName`, `scc`, `Church`, `Zone`, `status` FROM "
				+ "`Registration` WHERE 1;";
		
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
					"Registration Number","Date of Registration","Baptismal Name",
					"Other Names","Confirmation Name","SCC","Outstation","Zone","Status"
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
			t.setToolTipText("Registered believers");
			sp = new JScrollPane(t);
			String title = "List of Registered believers";
			sp.setBorder(BorderFactory.createTitledBorder(title.toUpperCase()));
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
