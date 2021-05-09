package org.autodoc.database;

import java.sql.*;

/**
*
* @author yogesh.gunge
*/
public class JDBC 
{
public static Connection con;
public static Statement st;
static int connectFlag=0;

public static Connection connect() throws Exception
	{
	if(connectFlag==0)
		{Class.forName("org.h2.Driver");		
		con=DriverManager.getConnection( "jdbc:h2:./autodoc", "yogesh", "autodoc" );
		connectFlag=1;
		}
	return(con);
	}
public static void saveStatement( Statement stmt ) throws Exception
	{
	st=stmt;
	}
}