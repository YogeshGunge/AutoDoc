package org.autodoc.interfaces.graphics;

import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

import org.autodoc.database.*;
import org.autodoc.resources.*;
import org.autodoc.model.*;

/**
*
* @author yogesh.gunge
*/
public class Login extends Frame implements ActionListener {
	private static final long serialVersionUID = 1L;
	TextField t1 = new TextField();
	TextField t2 = new TextField();
	TextField n1 = new TextField();
	TextField n2 = new TextField();
	TextField n3 = new TextField();
	TextField n4 = new TextField();
	TextField n5 = new TextField();
	TextField n6 = new TextField();
	Choice gender = new Choice();
	Choice month = new Choice();
	Choice day = new Choice();
	Choice year = new Choice();
	Label l = new Label(Strings.welcomeMsg);
	Button b1 = new Button(Strings.loginBtn);
	Button b2 = new Button(Strings.exitBtn);
	Button b3 = new Button(Strings.signUpBtn);
	private static Connection con;
	private static Statement stmt;

	static int count = 0;
	Date today;

	public Login() {
		super(Strings.project + " : " + Strings.loginForm);
		setLayout(null);
		setVisible(true);
		setSize(640, 480);
		t1.setBounds(20, 200, 150, 20);
		t2.setBounds(20, 280, 150, 20);
		n1.setBounds(310, 205, 150, 20);
		n2.setBounds(310, 235, 150, 20);
		n3.setBounds(310, 265, 150, 20);
		n4.setBounds(310, 295, 150, 20);
		n5.setBounds(310, 325, 150, 20);
		n6.setBounds(310, 355, 150, 20);
		gender.setBounds(310, 385, 100, 20);
		month.setBounds(310, 415, 60, 20);
		day.setBounds(380, 415, 60, 20);
		year.setBounds(450, 415, 60, 20);
		b1.setBounds(20, 340, 100, 30);
		b2.setBounds(20, 400, 100, 30);
		b3.setBounds(520, 400, 100, 30);
		l.setBounds(200, 450, 340, 30);

		gender.addItem("Select Sex:");
		gender.addItem("Male");
		gender.addItem("Female");

		month.addItem("Month:");
		month.addItem("Jan");
		month.addItem("Feb");
		month.addItem("Mar");
		month.addItem("Apr");
		month.addItem("May");
		month.addItem("Jun");
		month.addItem("Jul");
		month.addItem("Aug");
		month.addItem("Sep");
		month.addItem("Oct");
		month.addItem("Nov");
		month.addItem("Dec");

		day.addItem("Day:");
		for (int i = 1; i < 32; i++)
			day.addItem("" + i);

		year.addItem("Year:");
		for (int i = 1920; i < 2020; i++)
			year.addItem("" + i);

		t2.setEchoChar('*');
		n2.setEchoChar('*');
		n3.setEchoChar('*');

		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);

		b1.setForeground(Color.white);
		b2.setForeground(Color.white);
		b3.setForeground(Color.white);
		Color c = Colors.btnColor;
		b3.setBackground(c);
		b1.setBackground(c);
		b2.setBackground(c);
		l.setForeground(Color.green);

		add(b1);
		add(b2);
		add(b3);
		add(t1);
		add(t2);
		add(t1);
		add(t2);
		add(n1);
		add(n2);
		add(n3);
		add(n4);
		add(n5);
		add(n6);
		add(gender);
		add(month);
		add(day);
		add(year);
		add(l);

		today = new Date();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				dispose();
			}
		});
	}

	public void paint(Graphics g) {
		g.setColor(Colors.background);
		g.fillRect(0, 0, 180, 480);
		Font f = new Font("Crystal", Font.BOLD, 14);
		g.setFont(f);
		g.drawString(Strings.aboutProject1,
				190, 100);
		g.drawString(Strings.aboutProject2
				,190, 125);
		f = new Font("Times", Font.PLAIN, 12);
		g.setFont(f);
		g.drawString("Create New User", 190, 190);
		g.drawLine(185, 200, 500, 200);
		g.drawString(Strings.yourName, 190, 220);
		g.drawString(Strings.password, 190, 250);
		g.drawString(Strings.confirmPassword, 190, 280);
		g.drawString(Strings.yourEmailId, 190, 310);
		g.drawString(Strings.yourHomeTown, 190, 340);
		g.drawString(Strings.yourMobileNo, 190, 370);
		g.drawString(Strings.yourGender, 190, 400);
		g.drawString(Strings.yourBirthday, 190, 430);
		g.setColor(Color.white);
		f = new Font("Comic Sans MS", Font.BOLD, 30);
		g.setFont(f);
		g.drawString(Strings.project, 20, 90);
		f = new Font("TimesRoman", Font.PLAIN, 20);
		g.setFont(f);
		g.drawString(Strings.username, 20, 190);
		g.drawString(Strings.password, 20, 270);
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("Login")) {
			char ch;
			String Username = t1.getText(), Password = t2.getText();
			if (Username.equals("")) {
				l.setText("Username can't be blank. Please Fill the Field.");
				l.setForeground(Color.red);
				return;
			}
			if (Username.length() > 15) {
				l.setText("Username too long max length = 15.");
				l.setForeground(Color.red);
				return;
			}
			for (int i = 0; i < Username.length(); i++) {
				ch = Username.charAt(i);
				if ((ch > 'z' || ch < 'A') && ch != ' ') {
					l.setText("Username should not contain spaces or numbers.");
					l.setForeground(Color.red);
					return;
				}
			}
			if (Password.equals("")) {
				l.setText("Password can't be blank. Please Fill the Field.");
				l.setForeground(Color.red);
				return;
			}
			if (Password.length() > 15) {
				l.setText("Password too long max length = 15.");
				l.setForeground(Color.red);
				return;
			}
			for (int i = 0; i < Password.length(); i++) {
				ch = Password.charAt(i);
				if ((ch > 'z' || ch < 'A') && ch != ' ') {
					l.setText("Password should not contain spaces or numbers.");
					l.setForeground(Color.red);
					return;
				}
			}
			if (count < 3) {
				if (searchUsername(t1.getText(), t2.getText()) == 1) {
					java.awt.EventQueue.invokeLater(
	        	        new Runnable()
	        	            {
	        	                @Override
	        	                public void run()
	        	                {
	        	                	GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	                				GraphicsDevice[] devices = env.getScreenDevices();
	                				CheckupScreen window = new CheckupScreen( devices[0] );	//Assume Single Monitor
	                				window.onFullScreen();
	        	                }
	        	            }
	        	    );
					this.dispose();
				} else {
					count++;
					l.setText("Invalid Username or Password !! Please try again.");
					l.setForeground(Color.red);
					t1.setText("");
					t2.setText("");
				}
			} else {
				l.setText("Maximum Trial Limit of password exeeded !! Exit Application.");
			}
		}
		if (ae.getActionCommand().equals("Exit")) {
			System.exit(0);
		}

		if (ae.getActionCommand().equals("Sign Up")) {
			char ch;
			String Username = n1.getText(), Password = n2.getText(), Query;

			if (Username.equals("")) {
				l.setText("Username can't be blank. Please Fill the Field.");
				l.setForeground(Color.red);
				return;
			}
			for (int i = 0; i < Username.length(); i++) {
				ch = Username.charAt(i);
				if ((ch > 'z' || ch < 'A') && ch != ' ') {
					l.setText("Username should not contain spaces or numbers.");
					l.setForeground(Color.red);
					return;
				}
			}
			if (Username.length() > 15) {
				l.setText("Username too long max length = 15.");
				l.setForeground(Color.red);
				return;
			}
			if (Password.equals("")) {
				l.setText("Password can't be blank. Please Fill the Field.");
				l.setForeground(Color.red);
				return;
			}
			for (int i = 0; i < Password.length(); i++) {
				ch = Password.charAt(i);
				if ((ch > 'z' || ch < 'A') && ch != ' ') {
					l.setText("Password should not contain spaces or numbers.");
					l.setForeground(Color.red);
					return;
				}
			}
			if (!Password.equals(n3.getText())) {
				l.setText("Passwords do not match.");
				l.setForeground(Color.red);
				return;
			}
			if (Password.length() > 15) {
				l.setText("Password too long max length = 15.");
				l.setForeground(Color.red);
				return;
			}
			if (n4.getText().equals("")) {
				l.setText("Where should I mail you? Please Fill the Field.");
				l.setForeground(Color.red);
				return;
			}
			if (n5.getText().equals("")) {
				l.setText("Where do you live? Please Fill the Field.");
				l.setForeground(Color.red);
				return;
			}
			if (n6.getText().equals("")) {
				l.setText("Please Fill the Field Mobile Number.");
				l.setForeground(Color.red);
				return;
			}
			if (gender.getSelectedItem().equals("Select Sex:")) {
				l.setText("Please Select your gender.");
				l.setForeground(Color.red);
				return;
			}
			if (day.getSelectedItem().equals("Day:")) {
				l.setText("Check your birthdate.");
				l.setForeground(Color.red);
				return;
			}
			if (month.getSelectedItem().equals("Month:")) {
				l.setText("Check your birth month.");
				l.setForeground(Color.red);
				return;
			}
			for (int i = 0; i < n6.getText().length(); i++) {
				ch = n6.getText().charAt(i);
				if ((ch > '9' || ch < '0') && ch != '+') {
					l.setText("Invalid Mobile No. Please Fill the Field.");
					l.setForeground(Color.red);
					return;
				}
			}
			int Byear = 2050;
			try {
				Byear = Integer.parseInt(year.getSelectedItem());
			} catch (Exception e) {
			}
			if (today.getYear() + 1900 <= Byear) {
				l.setText("Please check your birthyear.");
				l.setForeground(Color.red);
				return;
			}
			try {
				con = JDBC.connect();
				stmt = con.createStatement();
				Query = "INSERT INTO " + DBMetaData.loginTable + " (USER_NAME, PASSWORD, EMAIL_ID, HOMETOWN, MOBILE_NO, SEX, BIRTHDAY) VALUES (";
				Query += "\'" + Username + "\',";
				Query += "\'" + Password + "\',";
				Query += "\'" + n4.getText() + "\',";
				Query += "\'" + n5.getText() + "\',";
				Query += "\'" + n6.getText() + "\',";
				Query += "\'" + gender.getSelectedItem() + "\',";
				Query += "\'" + month.getSelectedItem() + "/"
						+ day.getSelectedItem() + "/" + year.getSelectedItem()
						+ "\')";
				System.out.println(Query);
				stmt.executeUpdate(Query);
				stmt.close();
			} catch (Exception e) {
				String err = "" + e;
				System.out.println("Error=<" + err + ">");
				if (err.equals("java.sql.SQLException: No ResultSet was produced")) {
					l.setText("You Signed Up Successfully");
					l.setForeground(Color.green);
				} else if (err
						.equals("java.sql.SQLException: [Microsoft][ODBC Microsoft Access Driver] Data type mismatch in criteria expression.")) {
					l.setText("Some Field is Missing.");
					l.setForeground(Color.red);
				} else if (err
						.equals("java.sql.SQLException: [Microsoft][ODBC Microsoft Access Driver] Could not use '(unknown)'; file already in use.")) {
					l.setText("Please close database OR Restart computer.");
					l.setForeground(Color.red);
				} else if (err
						.equals("java.sql.SQLException: [Microsoft][ODBC Microsoft Access Driver] Could not find file '(unknown)'.")) {
					l.setText("Check connection path jdbc:odbc:stiki.");
					l.setForeground(Color.red);
				} else if (err.equals("java.sql.SQLException: General error")) {
					l.setText("Username already exists. Please try with different username.");
					l.setForeground(Color.red);
				} else {
					l.setText("DB Connection Fail : See Command Promt for details");
					l.setForeground(Color.red);
				}
			}
		}
	}

	public static int searchUsername(String u, String p) {
		ResultSet rs;
		String user = "", pass = "";
		try {
			con = JDBC.connect();
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from " + DBMetaData.loginTable
					+ ";");
			while (rs.next()) {
				user = rs.getString(DBMetaData.loginUserName);
				if (u.equals(user))
					break;
			}
			if (u.equals(user)) {
				pass = rs.getString(DBMetaData.loginPassword);
				if (p.equals(pass)) {
					UserInfo.userId = rs.getInt(DBMetaData.loginUserId);
					UserInfo.username = user;
					UserInfo.emailId = rs.getString(DBMetaData.loginUserEmailId);
					UserInfo.nativeplace = rs.getString(DBMetaData.loginHomeTown);
					UserInfo.mobileNo = rs.getString(DBMetaData.loginMobileNo);
					UserInfo.sex = rs.getString(DBMetaData.loginSex);
					UserInfo.birthday = rs.getString(DBMetaData.loginBirthday);
					return (1); // 1-> success
				} else
					return (0); // 0 -> Pass not match
			}
			stmt.close();
		} catch (Exception e) {
			System.out.println("Error=" + e);
		}
		return (-1); // -1-> not found
	}

	@SuppressWarnings("deprecation")
	public static void main(String cp[]) {
		Login page = new Login();
		page.show();
	}
}