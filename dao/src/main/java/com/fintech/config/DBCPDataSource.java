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

    public static final String URL = "jdbc:h2:mem:test";
    public static final String USER = "sa";
    public static final String PASSWORD = "";

    private static BasicDataSource ds = new BasicDataSource();


    static {
        ds.setDriver(new Driver());
        ds.setUrl(URL);
        ds.setUsername(USER);
        ds.setPassword(PASSWORD);
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        ds.setMaxOpenPreparedStatements(100);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public DataSource getDataSource(){
        return ds;
    }
//
//    private String id;
//    private Long amount;
//    private Long clientid;


    public void createDB() {

        try(Connection conn = this.getConnection()){
            Statement st = conn.createStatement();
            st.execute("create table account(id varchar(10), amount integer, clientid integer)");
            st.execute("insert into account values ('40817',1000,1 )");
            st.execute("insert into account values ('40818',1000,2 )");
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
