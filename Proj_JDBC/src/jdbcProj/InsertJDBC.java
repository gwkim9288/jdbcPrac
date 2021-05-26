package jdbcProj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertJDBC {
	public static void main(String[] args) throws ClassNotFoundException,SQLException {
		try {
			Connection conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/dbs?useUnicode=true&useJDBCComplia ntTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC" , "root" , "19m28b37!");
			String accountInsertSql = "insert into account values (?,?,?,?,?)";
			String restaurantInsertSql = "insert into restaurant values (?,?,?,?,?,?)";
			String productInsertSql = "insert into product values (?,?,?,?,?)";
			String orderInsertSql = "insert into `order` values (?,?,?,?,?)";
			String ordersInsertSql = "insert into orders values (?,?,?,?,?)";
			String deliverInsertSql = "insert into deliver values (?,?,?,?)";
			String riderInsertSql = "insert into rider values (?,?,?,?)";
			String membershipInsertSql = "insert into membership values (?,?,?,?)";
			String belongInsertSql = "insert into belong values (?,?,?,?,?)";
			String reviewInsertSql = "insert into review values (?,?,?,?)";
			
			PreparedStatement pstmt = conn.prepareStatement(membershipInsertSql);
			for(int i=0; i<5;i++) {
			
				pstmt.setInt(1, i);
				pstmt.setInt(2, i);
				pstmt.setInt(3, i+1);
				pstmt.setString(4, "special DC"+i);
				pstmt.executeUpdate();
			}
			
			pstmt = conn.prepareStatement(accountInsertSql);
			for(int i=0;i<20000;i++) {
				pstmt.setInt(1, i);
				pstmt.setString(2,"chung-ang univ");
				pstmt.setString(3,"guenwookim"+i);
				pstmt.setString(4, "010-0000-0000");
				pstmt.setString(5, "guenwoo@naver.com");
				pstmt.executeUpdate();
			}
			
			pstmt = conn.prepareStatement(orderInsertSql);
			java.util.Date date = new java.util.Date();
			for(int i =0;i<20000;i++) {
				pstmt.setInt(1, i);
				pstmt.setInt(2,i);
				pstmt.setString(3,"chung-ang univ");
				pstmt.setInt(4,i);
				pstmt.setTimestamp(5, new java.sql.Timestamp(date.getTime()));
				pstmt.executeUpdate();
			}
			
			pstmt = conn.prepareStatement(restaurantInsertSql);
			for(int i =0; i <10000;i++) {
				pstmt.setInt(1, i);
				pstmt.setString(2,"restaurant"+i/2);
				pstmt.setString(3,"chung-ang univ");
				pstmt.setString(4,"010-0000-0000");
				pstmt.setInt(5, (i/2500)*1000);
				pstmt.setInt(6, ((i/2000)+1)*5000);
				pstmt.executeUpdate();
			}
			
			pstmt = conn.prepareStatement(productInsertSql);
			for(int i =0;i<50000;i++) {
				pstmt.setInt(1, i);
				pstmt.setInt(2,i%10000);
				pstmt.setString(3,"food"+i);
				pstmt.setString(4, "image");
				pstmt.setInt(5,i);
				pstmt.executeUpdate();
			}
			
			pstmt = conn.prepareStatement(riderInsertSql);
			for(int i =0;i<10000;i++) {
				pstmt.setInt(1, i);
				pstmt.setInt(2,i/100);
				pstmt.setString(3, "dongjak-gu");
				pstmt.setString(4, "010-0000-0000");
				pstmt.executeUpdate();
			}
			
			pstmt = conn.prepareStatement(deliverInsertSql);
			for(int i=0;i<20000;i++) {
				pstmt.setInt(1, i);
				pstmt.setInt(2, i%10000);
				pstmt.setInt(3,i);
				pstmt.setInt(4, i/800);
				pstmt.executeUpdate();
			}
			
			pstmt = conn.prepareStatement(reviewInsertSql);
			for(int i =0; i<200000;i++) {
				pstmt.setInt(1, i);
				pstmt.setInt(2,i%10000);
				pstmt.setInt(3,i%5+1);
				pstmt.setString(4, "good");
				pstmt.executeUpdate();
			}
			
			pstmt = conn.prepareStatement(ordersInsertSql);
			for(int i=0;i<20000;i++) {
				pstmt.setInt(1, i);
				pstmt.setInt(2, i);
				pstmt.setInt(3, i%10000);
				pstmt.setInt(4,i);
				pstmt.setInt(5,i);
				pstmt.executeUpdate();
			}
			
			pstmt = conn.prepareStatement(belongInsertSql);
			for(int i =0;i<20000;i++) {
				pstmt.setInt(1,i);
				pstmt.setInt(2,i%5);
				pstmt.setInt(3,i);
				pstmt.setInt(4, i%20);
				pstmt.setInt(5, i);
				pstmt.executeUpdate();
			}
			
			pstmt.close();
			conn.close();
			
		} catch(SQLException sqle) {
			System.out.println("SQL Exception: "+sqle);
		}
	}
}
