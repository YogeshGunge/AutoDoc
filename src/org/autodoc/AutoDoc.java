package org.autodoc;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

import org.autodoc.database.DBMetaData;
import org.autodoc.interfaces.graphics.Dashboard;
import org.autodoc.interfaces.graphics.Login;
import org.autodoc.resources.Strings;
import org.h2.tools.RunScript;

/**
 *
 * @author yogesh.gunge
 */
public class AutoDoc {
	public static String mode;
	public static String collection = "nails";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
		
		if(args.length > 0) {
	        // Mode: Training Mode for Creating Database From Training Samples
	        if(args[0].equals("TRAIN_MODE"))
	        {
	        	mode = Strings.trainMode;
	        	Connection conn;
	        	String password = JOptionPane.showInputDialog("Enter Password for System Training Administrator:");
	        	conn = DriverManager.getConnection( "jdbc:h2:./autodoc", "yogesh", "autodoc" );
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM "+DBMetaData.loginTable+" WHERE "+DBMetaData.loginUserName+" = \'admin\';");
                if(rs.next()) {
		        	String dbPassword = rs.getString(DBMetaData.loginPassword);
		            if(password.equals(dbPassword)) {
		            	java.awt.EventQueue.invokeLater(
		        	        new Runnable()
		        	            {
		        	                @Override
		        	                public void run()
		        	                {
		        	                	GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		                				GraphicsDevice[] devices = env.getScreenDevices();
		                				Dashboard window = new Dashboard( devices[0] );	//Assume Single Monitor
		                				window.onFullScreen();
		        	                }
		        	            }
		        	    );
		            } else {
		            	JOptionPane.showMessageDialog(null, "Passwords do not match!! Please try again.");
		            }
	            } else {
	            	JOptionPane.showMessageDialog(null, "Database is currupt!! Reinstall the system.");
	            }
                rs.close();
                st.close();
                conn.close();
	        }
	        
	        // Admin Login        
	        else if( args[0].equals("USE_MODE") )
	        {
	        	mode = Strings.useMode;
	            java.awt.EventQueue.invokeLater(
	            new Runnable()
	            {
	                @Override
	                public void run()
	                {
	                	Login l = new Login();
	                	l.setVisible(true);
	                }
	            });
	        }
	        
	        // Install Mode
	        else if ( args[0].equals("INSTALL_MODE") ) {
	        	mode = Strings.installMode;
	            Connection conn;
	            try {
	                Class.forName("org.h2.Driver");   //Register JDBC driver
	            } catch (ClassNotFoundException e) {
	                System.out.println("Can't find class org.h2.Driver!!");
	                System.exit(1);
	            }
	            try {
	                // Open a connection
	                conn = DriverManager.getConnection( "jdbc:h2:./autodoc", "yogesh", "autodoc" );
	                Statement st = conn.createStatement();
	                st.execute("SELECT * FROM "+DBMetaData.loginTable+";");
	                st.close();
	                conn.close();
	                JOptionPane.showMessageDialog(null, "AutoDoc Database is already installed on your machine.");
	            } catch (Exception e) {
	                // Import Zero To Five Database into H2
	                String params[] = new String[8];
	                params[0] = "-url";
	                params[1] = "jdbc:h2:./autodoc";
	                params[2] = "-user";
	                params[3] = "yogesh";
	                params[4] = "-password";
	                params[5] = "autodoc";
	                params[6] = "-script";
	                params[7] = "./autodoc.sql";                
	                try {
	                    new RunScript().runTool(params);
	                    conn = DriverManager.getConnection("jdbc:h2:./autodoc", "yogesh", "autodoc");
	                    Statement st = conn.createStatement();
	                    st.execute("SELECT * FROM "+DBMetaData.loginTable+";");
	                    st.close();
	                    conn.close();
	                    JOptionPane.showMessageDialog(null, "Congratulations, AutoDoc successfully installed on your machine.");
	                } catch (SQLException ex) {
	                    JOptionPane.showMessageDialog(null, "Failed to install AutoDoc on your machine. Please contact support." , null, JOptionPane.ERROR_MESSAGE );
	                    System.out.println("Error: "+ex.getMessage());
	                }
	                catch (Exception ex) {
	                    JOptionPane.showMessageDialog(null, "Failed to install AutoDoc on your machine. Please check autodoc.sql." , null, JOptionPane.ERROR_MESSAGE );
	                }
	            }
	        }
		}
    }
}