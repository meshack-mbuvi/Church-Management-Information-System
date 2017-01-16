package databaseApp;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.toedter.calendar.JDateChooser;

public class VariablesDefinitions extends JFrame{
	boolean connected=false; 

	//Variable for date selection
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	JDateChooser date;
	/**
	 * 
	 */
	private static final long serialVersionUID = -1368512957327120810L;
	/*
	 * Login variables
	 */
	//driver name and database url
	static final String URL="jdbc:mysql://localhost/Church_db";
	static final String DRIVER_CLASS=
		"com.mysql.jdbc.Driver";
	static volatile java.sql.PreparedStatement stmt;
	//Database credentials
	static String USER="";
	static String PASSWORD="";
	//labels for user login details
	JLabel LabUname,LabPword;
	//textfields for user login data
	JTextField txtUser,txtPass;
	JLabel msg;//login message
	JButton login,logOut;//login and logout button
	JFrame frame;
	/*
	 * Various panels:
	 * 1.header-displays welcome message
	 * 2.btns holds buttons on the top of the frame
	 * 3.logpanel holds the user input fields
	 * 4.msgpanel displays login status of the user
	 * 
	 */
	JPanel logpanel,msgPanel,header,parent,btns,display;
	ImageIcon img;//holds background image
	JLabel background;//container for holding other components
	JLabel hdr;
	
	Connection con=null;
	
	/*
	 * Menu bar and the menu items
	 */
	JMenuBar bar;
	//Menu items
	JMenu zonesMenu;
	JMenuItem newZone,EditZone,deleteZone,SearchZones,showZones;
	
	JMenu churches;
	
	JMenu baptisms;
	JMenuItem newBap,editBap,searchBap,delBap,showAllBaptisms;
	
	JMenu accounts;
	JMenu chchCommittee;
	JMenuItem newComMember,editComMember,delComMember,showComMembers;
	
	
	//Menu items for church
	JMenu church;
	JMenuItem newChurch,delChurch,searchChurch,editChurch,showChurches;
	//scc menu
	JMenu scc;
	JMenuItem newScc,editScc,delScc,showScc;
	JTextField txtsccName;

	//contributions
	JMenu contributions;
	JMenuItem newCon,editCon,C_show,C_search,delContribution;
	
	//Donations
	JMenu Donations;
	JMenuItem newDon,editDon,showDons;
	
	//sadaka/offerings
	JMenu Offerings;
	JMenuItem newOffering,editOffering,delOffering,showOffering;
	//Groups 
	JMenu groupTypes;
	JMenuItem newGroupType,editGroupType,delGroupType,showGroupType;
	//Group and projects menu
	JMenu groups,menuGroups;
	JMenuItem newGroup,editGroup,delGroup,showGroup;
	JMenu projects;
	JMenuItem newProject,editProject,delProject,showProject;
	/*
	 * Church/outstation variables
	 */
	String chrchName,zone,catechistName;
	int christianCount;
	
	//txtfields and labels for the gui
	JLabel labName,labZone,labCount;
	JTextField txtName,txtCount,txtCatechist;
	Choice chcZones;
	
	/*
	 * Fields for church committee
	 */
	int ID;
	//designation can be chairperson,secretary,treasurer ,member,etc
	String FName,SName,phone,designation,churchName;
	JLabel labID,labBaptismalName,labOtherName,labPhone,labPosition,labChurch;
	JTextField txtId,txtFname,txtSname,txtDesingation,txtChurch,txtBapName,txtOtherName;
	String regNo;
	String type,period,paymentMode;
	double amount;
	
	/*
	 * Variables for contribution
	 */
	JLabel labReg;		JTextField txtReg;
	JLabel labType;		JTextField txtType;
	JLabel labPeriod;	JTextField txtPeriod;
	JLabel labAmount;		JTextField txtAmount;
	JLabel labPayMode;	JTextField txtPayMode;
	

/****************************************************************************
 * 
 *					 Baptism variable declaration begins here
 * 
 * **************************************************************************/
	Date dateOfConfirmation;
	Date dateOfBirth;
	Date dateOfBaptism;
	Date FHC;
	String placeOfBirth;
	String cName,fathersName,mothersName,Residence
	,minister,sponsor,confirName;//church
	//Labels
	JLabel LaBRegNo,LaB_D_Of_B,LaB_D_Of_Bap,LaB_D_Of_Conf,LaBFCH,LaBC_Name,LaBFname
	,LaBMName,LaBRes,LaBP_OF_birth,LaBchrc,LaBMins,LaBspon,LaBconfName;
	//textfields
	JTextField txtFCH,txtCName,txtFName,
	txtMName,txtResidence,txtPlace_Of_Birth,txtChch,txtConfMin,txtSponsor,txtConfirName;
	JDateChooser jfDOBirth,jfDatOfBap,jf_DOfConfi,Jf_FCH;
	JButton Save;
	
	//various panels used in baptism fields
	JPanel personalDetails,parentsDetails,fcnb,conf,baptism,btnPanel;
	JLabel parent2;
	/********************************************************************
	 * 
	 *							Zone variables
	 *
	 ********************************************************************/
	JLabel LabzoneName;
	JTextField txtZoneName;
	private JMenu zones;
	private JMenu zonesComm;
	JMenuItem newZonecom,editZoneComm,delZoneCom,showZoneCom;
	
	/********************************************************************
	 * 
	 *				Mode of payment variables
	 *
	 ********************************************************************/
	JMenu modeOfPayment;
	JMenuItem newMod,editMod,delMod,showMod;
	
	/********************************************************************
	 * 
	 *					Registration and Guest modules variables
	 *
	 ********************************************************************/
	JMenu Registration,Guest;
	JMenuItem newReg,editReg,delReg,showReg,
	newGuest,editGuest,delGuest,showGuest;
/*************************************************************************************
 * 
 * main Constructor
 * 
 *************************************************************************************/
	//JComboBox<Integer> IDs;
	public VariablesDefinitions() {
		/********************************************************************
		 * 
		 *							Home screen set up
		 *
		 ********************************************************************/
		//IDs = new JComboBox<Integer>();
		//Instantiate class variables
		frame=new JFrame();
		frame.setTitle("Church Management Information System(CMIS).");	
		frame.setLayout(new BorderLayout());//set layout manager
		//set frame size
		frame.setSize((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
				(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
		frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				try {
					con.close();
					System.exit(0);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {}
		});
				
		// Variable initialization
		//log message
		msg=new JLabel("Please Log in to continue");			
		login=new JButton("Sign in");
		//Instantiate class variables

		//login variables
		LabUname=new JLabel("UserName:");
		txtUser=new JTextField(8);
		LabPword=new JLabel("Password:");
		txtPass=new JPasswordField(8);
		
		//parent and btns panel
		parent=new JPanel(new BorderLayout());
		parent.setOpaque(false);
		
		btns=new JPanel();
		
		//Prepare Background Image
		img=new ImageIcon("/home/mbuvi/Desktop/water.jpg");
		background=new JLabel(img);
		background.setLayout(new BorderLayout());
		background.setOpaque(false);
		frame.add(background,BorderLayout.CENTER);
		/*
		 * Header panel
		 */
		String title="WELCOME TO CHURCH MANAGEMENT INFORMATION SYSTEM ";
		header=new JPanel();
		header.setOpaque(false);
		hdr=new JLabel(title);
		hdr.setFont(new Font("Serif",Font.BOLD,28));
		hdr.setForeground(Color.red);
		header.add(hdr);
		background.add(header,BorderLayout.SOUTH);
		//msg panel features
		msgPanel=new JPanel();
		msg.setBackground(getForeground());//msg label background color
		msg.setOpaque(false);
		msgPanel.add(msg);
		//add panels to frame
		frame.add(msgPanel,BorderLayout.NORTH);
		
		display=new JPanel();
		//background.add(display,BorderLayout.CENTER);
		//frame.add(display,BorderLayout.CENTER);
/********************************************************************
* 
*							 Menu Bar and zones menu
*
********************************************************************/
		bar=new JMenuBar();
		//bar.setBackground(Color.cyan);
		
		zonesMenu=new JMenu("ZONES ");
		zones=new JMenu("Zones");
		
		//initialize Menu items
		newZone=new JMenuItem("New ");
		EditZone=new JMenuItem("Edit");
		SearchZones=new JMenuItem("Search");
		
		deleteZone=new JMenuItem("Delete ");
		showZones=new JMenuItem("Show All");
		
		zones.add(newZone);
		zones.addSeparator();
		zones.add(EditZone);
		zones.addSeparator();
		zones.add(deleteZone);
		zones.addSeparator();	

		SearchZones=new JMenuItem("Search Zone");
		zones.add(SearchZones);
		
		zones.addSeparator();
		zones.add(showZones);
		zonesMenu.add(zones);
		bar.add(zonesMenu);
		
		/**************************************************
		 * 
		 * zone committee
		 * 
		 **************************************************/
		zonesComm=new JMenu("Zone Committee");
		newZonecom=new JMenuItem("New");
		zonesComm.add(newZonecom);
		zonesComm.addSeparator();
		
		editZoneComm=new JMenuItem("Edit");
		zonesComm.add(editZoneComm);
		zonesComm.addSeparator();
		
		delZoneCom=new JMenuItem("Delete");
		zonesComm.add(delZoneCom);
		zonesComm.addSeparator();
		
		showZoneCom=new JMenuItem("Show All");
		zonesComm.add(showZoneCom);
		
		zonesMenu.addSeparator();
		zonesMenu.add(zonesComm);
/********************************************************************
 * 
 *							 church Menu
 *
 ********************************************************************/
		churches=new JMenu("OUTSTATION");
		church=new JMenu("Outstations");
		//subMenus for churches
		newChurch=new JMenuItem("New ");		
		church.add(newChurch);
		
		church.addSeparator();
		editChurch = new JMenuItem("Edit");
		church.add(editChurch);
		
		church.addSeparator();
		delChurch=new JMenuItem("Delete ");
		church.add(delChurch);
		
		church.addSeparator();
		searchChurch=new JMenuItem("Search Outstation");
		church.add(searchChurch);
		
		church.addSeparator();
		showChurches=new JMenuItem("Show Outstations");
		church.add(showChurches);
		churches.add(church);
		
		churches.addSeparator();
		
		/****************************************************
		 * 
		 * church committee
		 *
		 *****************************************************/
		chchCommittee=new JMenu("Outstation Committee");
		newComMember=new JMenuItem("New Member");
		chchCommittee.add(newComMember);
		chchCommittee.addSeparator();
		
		editComMember=new JMenuItem("Edit Member");
		chchCommittee.add(editComMember);
		chchCommittee.addSeparator();
		
		delComMember=new JMenuItem("Delete Member");
		chchCommittee.add(delComMember);
		chchCommittee.addSeparator();
		
		showComMembers=new JMenuItem("Show All Members");
		chchCommittee.add(showComMembers);

		/*****************************************************
		 * 
		 * Groups Menu
		 *
		 *****************************************************/
		menuGroups=new JMenu("GROUPS");
		
		groups=new JMenu("Groups");
		newGroup=new JMenuItem("New");
		groups.add(newGroup);
		groups.addSeparator();
		
		editGroup=new JMenuItem("Edit");
		groups.add(editGroup);
		groups.addSeparator();
		
		delGroup=new JMenuItem("Delete");
		groups.add(delGroup);
		groups.addSeparator();
		
		showGroup=new JMenuItem("Show All");
		groups.add(showGroup);
		
		
		/*****************************************************
		 * 
		 * Group types Menu
		 *
		 *****************************************************/
		groupTypes=new JMenu("GROUP TYPES");
		newGroupType=new JMenuItem("New");
		groupTypes.add(newGroupType);
		groupTypes.addSeparator();
		
		editGroupType=new JMenuItem("Edit");
		groupTypes.add(editGroupType);
		groupTypes.addSeparator();
		
		delGroupType=new JMenuItem("Delete");
		groupTypes.add(delGroupType);
		groupTypes.addSeparator();
		
		showGroupType=new JMenuItem("Show All");
		groupTypes.add(showGroupType);
		
		
		
		
		/*****************************************************
		 * 
		 * Projects Menu
		 *
		 *****************************************************/
		projects=new JMenu("PROJECTS");
		newProject=new JMenuItem("New");
		projects.add(newProject);
		projects.addSeparator();
		
		editProject=new JMenuItem("Edit");
		projects.add(editProject);
		projects.addSeparator();
		
		delProject=new JMenuItem("Delete");
		projects.add(delProject);
		projects.addSeparator();
		
		showProject=new JMenuItem("Show All");
		projects.add(showProject);
		
		/**************************************
		 * SCC (small Christians communities)
		 **************************************/
		scc=new JMenu("SCC");
		newScc=new JMenuItem("New");
		scc.add(newScc);
		scc.addSeparator();
		
		editScc=new JMenuItem("Edit");
		scc.add(editScc);
		scc.addSeparator();
		
		delScc=new JMenuItem("Delete");
		scc.add(delScc);
		scc.addSeparator();
		
		showScc=new JMenuItem("Show all");
		scc.add(showScc);
		
		churches.add(chchCommittee);
		churches.addSeparator();
		churches.add(scc);
		bar.add(churches);
		/*
		 * Groups and projects
		 */
		menuGroups.add(groups);
		menuGroups.addSeparator();
		menuGroups.add(groupTypes);
		
		bar.add(menuGroups);
		bar.add(projects);

		/*
		 * baptisms menu 
		 */
		baptisms=new JMenu("BAPTISMS");
		newBap=new JMenuItem("New Baptist");
		baptisms.add(newBap);
		baptisms.addSeparator();
		
		editBap=new JMenuItem("Edit");
		baptisms.add(editBap);
		baptisms.addSeparator();
		
		delBap=new JMenuItem("Delete");
		baptisms.add(delBap);
		baptisms.addSeparator();
		
		searchBap=new JMenuItem("Search baptism");
		baptisms.add(searchBap);
		baptisms.addSeparator();
		
		showAllBaptisms=new JMenuItem("Show All Baptisms");
		baptisms.add(showAllBaptisms);
				
		bar.add(baptisms);

		/*
		 * Accounts menu item.
		 * It holds menu items for donations and contributions from church members
		 */
		accounts=new JMenu("FINANCES");
		//new contribution
		contributions=new JMenu("Contributions");
		newCon=new JMenuItem("New");
		contributions.add(newCon);
		
		//edit menuItem
		editCon=new JMenuItem("Edit");
		contributions.add(editCon);
		
		
		
		//search menuItem
		C_search=new JMenuItem("Search");
		//contributions.add(C_search);
		
		delContribution=new JMenuItem("Delete");
		contributions.add(delContribution);
		
		C_show=new JMenuItem("Show All");
		contributions.add(C_show);
		
		
		
		//Donations menu
		Donations=new JMenu("Donations");
		newDon=new JMenuItem("New");
		Donations.add(newDon);
		Donations.addSeparator();
		
		editDon=new JMenuItem("Edit");
		Donations.add(editDon);
		Donations.addSeparator();
		
		showDons=new JMenuItem("Show All");
		Donations.add(showDons);
		
		
		
		Offerings=new JMenu("Offering/sadaka");
		newOffering=new JMenuItem("New");
		Offerings.add(newOffering);
		Offerings.addSeparator();
		
		editOffering=new JMenuItem("Edit");
		Offerings.add(editOffering);
		Offerings.addSeparator();
		
		delOffering=new JMenuItem("Delete");
		Offerings.add(delOffering);
		Offerings.addSeparator();
		
		showOffering=new JMenuItem("Show All");
		Offerings.add(showOffering);
		
		modeOfPayment=new JMenu("Payment modes");
		newMod=new JMenuItem("New");
		modeOfPayment.add(newMod);
		modeOfPayment.addSeparator();
		
		editMod=new JMenuItem("Edit");
		modeOfPayment.add(editMod);
		modeOfPayment.addSeparator();
		
		delMod=new JMenuItem("Delete");
		modeOfPayment.add(delMod);
		modeOfPayment.addSeparator();		
		
		showMod=new JMenuItem("Show all");
		modeOfPayment.add(showMod);
		
		
		accounts.add(contributions);
		accounts.addSeparator();
		
		accounts.add(Donations);
		accounts.addSeparator();
		
		accounts.add(Offerings);
		accounts.addSeparator();
		
		accounts.add(modeOfPayment);
		
		
		bar.add(accounts);
		/********************************************************************
		 * 
		 *		variable initialization for Registration and Guests module 
		 *
		 ********************************************************************/
		Registration=new JMenu("REGISTRATION");
		newReg=new JMenuItem("New");
		Registration.add(newReg);
		Registration.addSeparator();
		
		editReg=new JMenuItem("Edit");
		Registration.add(editReg);
		Registration.addSeparator();
		
		delReg=new JMenuItem("Delete");
		Registration.add(delReg);
		Registration.addSeparator();
		
		showReg=new JMenuItem("Show all");
		Registration.add(showReg);
		
		Guest=new JMenu("GUESTS");
		newGuest=new JMenuItem("New");
		Guest.add(newGuest);
		Guest.addSeparator();
		
		editGuest=new JMenuItem("Edit");
		Guest.add(editGuest);
		Guest.addSeparator();
		
		delGuest=new JMenuItem("Delete");
		Guest.add(delGuest);
		Guest.addSeparator();		
		
		showGuest=new JMenuItem("Show All");
		Guest.add(showGuest);
		
		bar.add(Guest);
		bar.add(Registration);
		//set up login panel
		logpanel=new JPanel();
		logpanel.setSize(img.getIconWidth(),img.getIconHeight());
		logpanel.setOpaque(false);
		//set border property for the login panel
		logpanel.setBorder(BorderFactory.createTitledBorder("Login"));
		
		/*
		 * Button to log out of the system
		 */
		logOut=new JButton("Log Out");
		logOut.setBackground(null);
		logOut.setOpaque(false);

		/*
		 * 
		 * Fields for contributions
		 */

		labReg=new JLabel("Reg NO:");			txtReg=new JTextField(10);
		labType=new JLabel("Type :");			txtType=new JTextField(10);
		labPeriod=new JLabel("Period :");		txtPeriod=new JTextField(10);
		labAmount=new JLabel("Amount:");		txtAmount=new JTextField(10);
		labPayMode=new JLabel("Payment mode:");	txtPayMode=new JTextField(10);		
		

		/*
		 * setting up the variables for church committee
		 */
		ID=0;FName=SName=phone=designation=churchName="";
		labID=new JLabel("Enter ID to search:"); 				txtId=new JTextField(10);
		labBaptismalName=new JLabel("Baptismal Name:");		txtFname=new JTextField(10);
		labOtherName=new JLabel("Other Names:");	txtSname=new JTextField(10);
		labPhone=new JLabel("Phone:");			
		labPosition=new JLabel("Position:");	txtDesingation=new JTextField(10);
		labChurch=new JLabel("Church:");		txtChurch=new JTextField(10);
		
		
		/*
		 * setting up variables for contributions
		 */
		regNo="";type=period=paymentMode="";
		amount=0.0;
		
		//class variables
		chrchName="";
		zone="";
		christianCount=0;
		
		labName=new JLabel("Church Name: ");
		labZone=new JLabel("Zone: ");
		labCount=new JLabel("Total Christians: ");
		txtName=new JTextField(15);
		txtCount=new JTextField(15);
		txtCatechist=new JTextField(10);
		chcZones=new Choice();
		chcZones.setBackground(Color.white);
		
		/*
		 * To be implemented:
		 * 	Loop in the zone table while adding the values to the zones choice
		 * 		use for loop
		 */
		btns.setOpaque(false);
		
/************************************************************
 * 					Baptism variable initialization
 * 
 ************************************************************/
		/*
		 * Instantiate  labels
		 */
		//DateFormat df=new SimpleDateFormat("MM/dd/yyyy");
		LaBRegNo=new JLabel("Reg No:");		LaBC_Name=new JLabel("Christian Name:");
		LaBFname=new JLabel("Fathers Name :");	LaBMName=new JLabel("Mothers Name:");
		LaB_D_Of_B=new JLabel("Date Of Birth:");	LaBP_OF_birth=new JLabel("Place Of Birth:");
		LaBRes=new JLabel("Residence:");			LaB_D_Of_Bap=new JLabel("Date Of Baptism:");
		LaBchrc=new JLabel("Church:");
		LaBFCH =new JLabel("Date of 1st Holy Communion:");		
		LaBconfName=new JLabel("Confirmation Name:");
		LaB_D_Of_Conf=new JLabel("Date of confirmation:");
		LaBMins=new JLabel("Minister:");
		LaBspon=new JLabel("Sponsor:");	
		
		//TextFields
		txtReg=new JTextField(10);					
		txtCName=new JTextField(10);
		jfDatOfBap=new JDateChooser();
		Jf_FCH=new JDateChooser();				txtFName=new JTextField(10);
		txtMName=new JTextField(10);			txtResidence=new JTextField(10);
		txtPlace_Of_Birth=new JTextField(10);
		txtChch=new JTextField(10);				txtConfMin=new JTextField(10);	
		txtSponsor=new JTextField(10);			txtConfirName=new JTextField(10);
		jfDOBirth=new JDateChooser();
		jf_DOfConfi=new JDateChooser();
		
		txtsccName = new JTextField(10);
		/********************************************************************
		 * 
		 *		Zone variable initialization 
		 *
		 ********************************************************************/
		LabzoneName=new JLabel("Zone Name:");
		txtZoneName=new JTextField(10);	
		txtBapName=new JTextField(10);
		txtOtherName=new JTextField(10);
	}
}
