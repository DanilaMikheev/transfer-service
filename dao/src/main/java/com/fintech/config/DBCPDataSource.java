package com.fintech.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.h2.Driver;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
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

    public void createDB() {

        try (Connection conn = this.getConnection()) {
            Statement st = conn.createStatement();
            st.execute("create table account(id varchar(10), amount integer, clientid integer)");
            st.execute("insert into account values ('40817',1000,1 )");
            st.execute("insert into account values ('40818',1000,2 )");

//            st.execute("create table account(id varchar(10), amount integer, clientid integer)");
//            st.execute("insert into account values ('40817',1000,1 )");
//            st.execute("insert into account values ('40818',1000,2 )");
//

            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select id from account");
            while (rset.next()) {
                String name = rset.getString(1);
                System.out.println(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
