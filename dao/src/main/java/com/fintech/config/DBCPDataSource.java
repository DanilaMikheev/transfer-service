package com.fintech.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.h2.Driver;
import org.h2.tools.RunScript;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * H2 configuration
 *
 * @author d.mikheev on 11.05.19
 */
public class DBCPDataSource {

    private static final String URL = "jdbc:h2:mem:test";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final int MIN_IDLE = 5;
    private static final int MAX_IDLE = 5;
    private static final int MAX_OPEN_PREPARED_STATEMENTS = 100;

    private static BasicDataSource ds = new BasicDataSource();

    static {
        ds.setDriver(new Driver());
        ds.setUrl(URL);
        ds.setUsername(USER);
        ds.setPassword(PASSWORD);
        ds.setMinIdle(MIN_IDLE);
        ds.setMaxIdle(MAX_IDLE);
        ds.setMaxOpenPreparedStatements(MAX_OPEN_PREPARED_STATEMENTS);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public DataSource getDataSource() {
        return ds;
    }

    /**
     * Create DB from file
     *
     * @param dbInitFile script fileName in resource folder
     */
    public void createDB(String dbInitFile) {
        ClassLoader classLoader = getClass().getClassLoader();
        try (Connection conn = this.getConnection()) {
            File file = new File(classLoader.getResource(dbInitFile).getFile());
            RunScript.execute(conn, new FileReader(file));
            System.out.println(file.getName()+" executed");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create tables from statements
     */
    public void createDB() {
        ClassLoader classLoader = getClass().getClassLoader();
        try (Connection conn = this.getConnection()) {
            conn.createStatement().execute("create table account(id varchar(20), amount integer, clientid integer);");
            conn.createStatement().execute("insert into account values ('40817810123456789011',1000,1);");
            conn.createStatement().execute("insert into account values ('40817810123456789012',1000,2);");
            conn.createStatement().execute("create table transfer(id varchar(36),accFrom varchar(20), accTo varchar(20), amount bigint, status integer, updated timestamp);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropAll(){
        try (Connection conn = this.getConnection()) {
            Statement st = conn.createStatement();
            st.execute("DROP ALL OBJECTS");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
