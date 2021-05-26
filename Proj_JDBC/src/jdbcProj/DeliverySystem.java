package jdbcProj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DeliverySystem {

	public static Scanner scan;
	public static ResultSet rs = null;
	public static int id;
	public static String address;

	public static void main(String[] args) {
		scan = new Scanner(System.in);
		boolean check = true;

		try {
			Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/dbs?useUnicode=true&useJDBCComplia ntTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
					"root", "19m28b37!");

			System.out.println("원하시는 작업을 선택해주세요.");
			System.out.println("1.로그인하기");
			System.out.println("2.회원가입하기");
			System.out.printf("입력:");
			int input = scan.nextInt();
			scan.nextLine();
			if(input == 2)
				createAccount(conn);
			while (true) {
				id = login(conn);
				System.out.println(id);
				if (id >= 0)
					break;
			}
			while (check) {

//				conn = DriverManager.getConnection(
//						"jdbc:mysql://localhost:3306/dbs?useUnicode=true&useJDBCComplia ntTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
//						"root", "19m28b37!");
				
				System.out.println("원하시는 작업을 선택해주세요:");
				System.out.println("1. 개인정보 수정(주소,전화번호)");
				System.out.println("2. 주문하기");
				System.out.println("3. 리뷰 작성하기");
				System.out.println("4. 종료하기");
				System.out.printf("입력:");
				int command = scan.nextInt();
				scan.nextLine();
				System.out.println();
				switch (command) {
				case 1:
					modifyAccount(conn);
					break;
				case 2:
					orderFood(conn);
					break;
				case 3:
					createReview(conn);
					break;
				default:
					System.exit(1);
				}
			}
		} catch (SQLException sqle) {
			System.out.println("SQL Exception: " + sqle);
		}
	}

	public static void firstPage() {
		System.out.println();
	}

	public static int login(Connection conn) throws SQLException {
		int id=-1;
		
		try {
			conn.setAutoCommit(false);
			
			String findAccountByName = "select * from account where name = ?";

			System.out.println("로그인해주세요(이름): ");
			String name = scan.nextLine();
			PreparedStatement pstmt = conn.prepareStatement(findAccountByName);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if (rs == null || !rs.next()) {
				System.out.println("아이디가 존재하지 않습니다.");
				return -1;
			}
			System.out.println("로그인에 성공했습니다. " + name + " 님 환영합니다.");
			address = rs.getString("address");
			id = rs.getInt("id");
			conn.commit();
		} catch (Exception e) {
			System.out.println(e);
			if (conn != null) {
				// 에러가 발생하면 롤백
				conn.rollback();
			}
		}
		return id;
	}

	public static void modifyAccount(Connection conn) throws SQLException {
		String modifyAddress = "update account set address = ? where id = ?";
		String modifyPhoneNum = "update account set phone_number = ? where id = ?";

		
		try {
			conn.setAutoCommit(false);
			
			PreparedStatement pstmt = conn.prepareStatement(modifyAddress);
			System.out.println("변경할 주소를 입력해주세요(넘어가기를 원하시면 ! 를 입력해주세요):");
			String address = scan.nextLine();
			if (!address.equals("!")) {
				pstmt.setString(1, address);
				pstmt.setInt(2, id);
				pstmt.executeUpdate();
				System.out.println("변경완료");
			}

			pstmt = conn.prepareStatement(modifyPhoneNum);
			System.out.println("변경할 핸드폰 번호를 입력해주세요(넘어가기를 원하시면 ! 를 입력해주세요):");
			String phoneNum = scan.nextLine();
			if (!address.equals("!")) {
				pstmt.setString(1, phoneNum);
				pstmt.setInt(2, id);
				pstmt.executeUpdate();
				System.out.println("변경완료");
			}
			
			conn.commit();
		} catch (Exception e) {
			if (conn != null) {
				System.out.println(e);
				// 에러가 발생하면 롤백
				conn.rollback();
			}
		}
	}

	public static void orderFood(Connection conn) throws SQLException {

		String findRes = "select * from restaurant where name = ?";
		String findProductByResId = "select * from product where restaurant_id = ?";
		String findProductById = "select * from product where product_id =?";
		String createOrder = "insert into `order` values(?,?,?,?,?)";
		String findMaxOrderId = "select max(order_id) from `order`";
		String findResById = "select * from restaurant where restaurant_id = ?";

		try {
			conn.setAutoCommit(false);
			// 식당 이름으로 검색하기
			System.out.print("검색하고 싶은 식당의 이름을 입력해주세요.:");
			String resName = scan.nextLine();
			System.out.println();
			PreparedStatement pstmt = conn.prepareStatement(findRes);
			pstmt.setString(1, resName);
			rs = pstmt.executeQuery();

			if (rs == null || !rs.next()) {
				System.out.println("해당 식당이 존재하지 않습니다.");
				return;
			}
			while (rs.next()) {
				System.out.println("id: " + rs.getInt("restaurant_id"));
				System.out.println("name: " + rs.getString("name"));
				System.out.println("address: " + rs.getString("address"));
				System.out.println("phone_number: " + rs.getString("phone_number"));
				System.out.println("delivery_tip: " + rs.getString("delivery_tip"));
				System.out.println("minimum_order_price: " + rs.getString("minimum_order_price") + "\n");
			}
			System.out.printf("Select Restaurant(please input restaurant_id):");
			int resId = scan.nextInt();
			scan.nextLine();

			System.out.println("\n");

			// 레스토랑의 판매 상품 검색하기
			pstmt = conn.prepareStatement(findProductByResId);
			pstmt.setInt(1, resId);
			rs = pstmt.executeQuery();
			

			while (rs.next()) {
				System.out.println("id: " + rs.getInt("product_id"));
				System.out.println("name: " + rs.getString("name"));
				System.out.println("price: " + rs.getInt("price") + "\n");
			}
			System.out.printf("제품을 선택해주세요(제품id를 입력하세요):");

			int productId = scan.nextInt();
			scan.nextLine();

			// 상품 정보 가져오기 
			pstmt = conn.prepareStatement(findProductById);
			pstmt.setInt(1, productId);
			rs = pstmt.executeQuery();
			rs.next();
			String productName = rs.getString("name");
			int productPrice = rs.getInt("price");

			// 최소주문금액 만족하는지 판단하기
			pstmt = conn.prepareStatement(findResById);
			pstmt.setInt(1, resId);
			rs = pstmt.executeQuery();
			rs.next();
			int minimum_order_price = rs.getInt("minimum_order_price");
			int delivery_tip = rs.getInt("delivery_tip");

			if (productPrice < minimum_order_price) {
				System.out.println("최소 주문 금액을 만족해야 합니다.\n");
				return;
			}

			// 주문 추가하기
			pstmt = conn.prepareStatement(findMaxOrderId);
			rs = pstmt.executeQuery();
			rs.next();
			int orderId = rs.getInt("max(order_id)") + 1;
			java.util.Date date = new java.util.Date();
			pstmt = conn.prepareStatement(createOrder);
			pstmt.setInt(1, orderId);
			pstmt.setInt(2, id);
			pstmt.setString(3, address);
			pstmt.setInt(4, productPrice + delivery_tip);
			pstmt.setTimestamp(5, new java.sql.Timestamp(date.getTime()));
			pstmt.executeUpdate();

			System.out.println("주문을 완료했습니다.");
			System.out.println("주문내역)");
			System.out.println("주문한 식당:" + resName);
			System.out.println("주문한 음식:" + productName);
			System.out.println("주문가격:" + (productPrice + delivery_tip));
			System.out.println("주소:" + address);
			System.out.println("주문시간:" + date.toString());
			
			conn.commit();
		} catch (Exception e) {
			System.out.println(e);
			if (conn != null) {
				// 에러가 발생하면 롤백
				conn.rollback();
			}
		}
	}

	public static void createReview(Connection conn) throws SQLException {
		String findRes = "select * from restaurant where name = ?";
		String createReview = "insert into review values(?,?,?,?)";
		String maxReview = "select max(review_id) from review";
		try {
			conn.setAutoCommit(false);
			// 식당 이름으로 검색하기
			System.out.print("검색하고 싶은 식당의 이름을 입력해주세요.:");
			String resName = scan.nextLine();
			System.out.println();
			PreparedStatement pstmt = conn.prepareStatement(findRes);
			pstmt.setString(1, resName);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				System.out.println("id: " + rs.getInt("restaurant_id"));
				System.out.println("name: " + rs.getString("name"));
				System.out.println("address: " + rs.getString("address"));
				System.out.println("phone_number: " + rs.getString("phone_number"));
				System.out.println("delivery_tip: " + rs.getString("delivery_tip"));
				System.out.println("minimum_order_price: " + rs.getString("minimum_order_price") + "\n");
			}
			System.out.printf("Select Restaurant(please input restaurant_id):");
			int resId = scan.nextInt();
			scan.nextLine();

			System.out.println("\n");

			// 리뷰추가
			pstmt = conn.prepareStatement(maxReview);
			rs = pstmt.executeQuery();
			rs.next();
			int max = rs.getInt("max(review_id)");

			System.out.println("추가하고 싶은 리뷰 내용을 입력해주세요:");
			String comment = scan.nextLine();
			int star;
			while (true) {
				System.out.println("별점을 주세요(1~5):");
				star = scan.nextInt();
				scan.nextLine();
				if (star <= 5 && star >= 1)
					break;
				System.out.println("1~5사이의 수를 입력해주세요.");
			}
			pstmt = conn.prepareStatement(createReview);
			pstmt.setInt(1, max + 1);
			pstmt.setInt(2, resId);
			pstmt.setInt(3, star);
			pstmt.setString(4, comment);
			pstmt.executeUpdate();
			System.out.println("리뷰가 작성되었습니다.");

			conn.commit();
		} catch (Exception e) {
			if (conn != null) {
				// 에러가 발생하면 롤백
				conn.rollback();
			}
		}
	}
	
	public static void createAccount(Connection conn) throws SQLException {
		String findAccountByName = "select * from account where name = ?";
		String createAccount = "insert into account values (?,?,?,?,?)";
		String maxAccountId = "select max(id) from account";
		String name,email,phoneNum,address;
		
		try {
			conn.setAutoCommit(false);
			
			System.out.println("정보를 입력해 주세요.");
			PreparedStatement pstmt = conn.prepareStatement(findAccountByName);

			//중복되는 이름인지 검사 
			while(true) {
				System.out.println("이름(10자이내):");
				name = scan.nextLine();
				pstmt.setString(1,name);
				rs = pstmt.executeQuery();
				if(rs == null || !rs.next())
					break;
				System.out.println("이미 존재하는 이름입니다.");
			}
			System.out.println("주소:");
			address = scan.nextLine();
			System.out.println("핸드폰 번호:");
			phoneNum = scan.nextLine();
			System.out.println("이메일:");
			email = scan.nextLine();

			//account 테이블의 마지막 id 번호 찾기 
			pstmt = conn.prepareStatement(maxAccountId);
			rs = pstmt.executeQuery();
			rs.next();
			int max = rs.getInt("max(id)");
			
			//account 생성하기
			pstmt = conn.prepareStatement(createAccount);
			pstmt.setInt(1, max+1);
			pstmt.setString(2, address);
			pstmt.setString(3,name);
			pstmt.setString(4,phoneNum);
			pstmt.setString(5, email);
			pstmt.executeUpdate();
			
			System.out.println("회원생성이 완료되었습니다.");
			
			conn.commit();
		} catch (Exception e) {
			if (conn != null) {
				System.out.println(e);
				// 에러가 발생하면 롤백
				conn.rollback();
			}
		} 
	}
}
