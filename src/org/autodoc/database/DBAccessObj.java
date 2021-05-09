package org.autodoc.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yogesh.gunge
 *
 * This DBAccessObj Class is for maintain all the Database related Operation
 */
public class DBAccessObj {

    private Connection conn; //Connection Obj
    ResultSet rs = null; //Resultset Obj
    Statement statement = null; // Statement Obj
    static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME); // Maintain Logger

    /**
     * Create New Database Connection
     */
    public DBAccessObj() {
        this.createDBConnection();
    }
    //Method for Creating New Database Connections

    private void createDBConnection() {
        try {
            Class.forName("org.h2.Driver");   //Register JDBC driver
        } catch (ClassNotFoundException e) {
            log.info("Can't find class org.h2.Driver!!");
            System.exit(1);
        }
        try {
            //Open a connection
            conn = DriverManager.getConnection( "jdbc:h2:./autodoc", "yogesh", "autodoc" );
        } catch (Exception e) {
            log.log(Level.INFO, "ERROR {0}", e.getMessage());
        }
    }

    /**
     * Execute a query by using createStatement
     *
     * @return
     */
    public Statement createStatement() {
        Statement st = null;
        if (this.conn == null) {
            this.createDBConnection();
        }
        try {
            st = this.conn.createStatement();
        } catch (Exception e) {
            log.log(Level.INFO, "ERROR {0}", e.getMessage());
        }
        return st;
    }

    /**
     * Execute CreateStatement
     *
     * @param SqlComm
     */
    public void executeSQL(String SqlComm) {
        Statement st = this.createStatement();
        try {
            st.execute(SqlComm);
        } catch (Exception e) {
            log.log(Level.INFO, "ERROR {0}", e.getMessage());
        }

    }

    /**
     * Get ResultSet
     *
     * @param SqlComm
     * @return ResultSet
     */
    public ResultSet executeQuery(String SqlComm) {
        Statement st = this.createStatement();

        try {
            rs = st.executeQuery(SqlComm);
        } catch (Exception e) {
            log.log(Level.INFO, "ERROR {0}", e.getMessage());
        }
        return rs;
    }

    /**
     * Execute Update for Insert and Update
     *
     * @param SqlComm
     * @return
     */
    public int executeUpdate(String SqlComm) {
        Statement st = this.createStatement();
        int updateFlag = 0;

        try {
            updateFlag = st.executeUpdate(SqlComm);
        } catch (Exception e) {
            log.log(Level.INFO, "ERROR {0}", e.getMessage());
        }
        return updateFlag;
    }

    /**
     * Open New Connection
     *
     * @return
     */
    public Connection openConnection() {
        if (this.conn == null) {
            this.createDBConnection();
        }

        return this.conn;
    }

    /**
     * If Connection is Null Close Connection
     */
    public void closeConnection() {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (Exception e) {
                log.log(Level.INFO, "ERROR {0}", e.getMessage());
            }
        }
    }

    /**
     * Close ResultSet
     */
    public void closeResultSet() {
        if (this.rs != null) {
            try {
                this.rs.close();
            } catch (Exception e) {
                log.log(Level.INFO, "ERROR {0}", e.getMessage());
            }
        }
    }

    /**
     * Close Statement
     */
    public void closeStatement() {
        if (this.statement != null) {
            try {
                this.statement.close();
            } catch (Exception e) {
                log.log(Level.INFO, "ERROR {0}", e.getMessage());
            }
        }
    }
}