package JDBC_PROJECTS;

import java.sql.*;
import java.util.Scanner;
import java.util.SimpleTimeZone;

import static java.lang.System.exit;

public class HotelReservationSystem {
    private static final String url =  "jdbc:mysql://localhost:3306/hoteldb";
    private static final String username  = "root";
    private static final String password  = "Kishan@5822";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully");
        }catch (Exception e){
            System.out.println( e.getMessage());
        }

        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Statement stmt  = connection.createStatement();
            while (true){
                System.out.println();
                System.out.println("Hotel Reservation System");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservation");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("6. Exit");
                System.out.println("Choose an option");
                int choice = sc.nextInt();
                switch (choice){
                    case 1:
                      reserveRoom(connection ,sc,stmt);
                      break;
                    case 2:
                      viewReservation(connection,stmt);
                      break;
                    case 3:
                      getRoomnumber(connection,sc,stmt);
                      break;
                    case 4:
                      updateReservation(connection,sc,stmt);
                      break;
                    case 5:
                      deleteReservation(connection,sc,stmt);
                      break;
                    case 6:
                        Exist();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice , Try again !");
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void reserveRoom(Connection connection, Scanner sc,Statement stmt) throws SQLException {
        System.out.print("Enter the guest name :");
        sc.nextLine();//clear buffer;
        String Guestname = sc.nextLine();

        System.out.print("Enter the room number :");
        int room_no = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter the Contact detail :");
        String contact = sc.nextLine();

        String sql = "INSERT INTO reservation(guest_name,room_number,contact) " +
                "VALUES('" + Guestname + "' , " +room_no+ ", '" +contact+ "')";

        try{
          int rowaffected = stmt.executeUpdate(sql);
          if(rowaffected>0){
              System.out.println(" Congratulation Reservation successful");
          }
          else{
              System.out.println("Reservation Failed .... Sorry!");
          }
        }catch (SQLException e){
           e.printStackTrace();
        }
    }

    public  static  void viewReservation(Connection connection,Statement stmt) throws SQLException {
        String sql = "SELECT reservation_id,guest_name,room_number,contact,reservation_date FROM reservation";
        ResultSet resultSet = stmt.executeQuery(sql);
        while(resultSet.next()){
            int reservationid = resultSet.getInt("reservation_id");
            String guestname  = resultSet.getString("guest_name");
            int roomno = resultSet.getInt("room_number");
            String contact = resultSet.getString("contact");
            String reservationdate  = resultSet.getTimestamp("reservation_date").toString();

            System.out.printf("ID: %d | Guest: %s | Room: %d | Contact: %s | Date: %s%n",
                    reservationid, guestname, roomno, contact, reservationdate);
        }
    }

    public static void getRoomnumber(Connection connection,Scanner sc,Statement stmt) {
        try{
            System.out.println("Enter the reservation id please !");
            int reservationid = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter the Guest name :");
            String guestname = sc.nextLine();
            String sql = "SELECT room_number FROM reservation " +
                         "WHERE reservation_id = " + reservationid +
                         " AND guest_name = '" + guestname + "'";

            try {
                ResultSet resultSet = stmt.executeQuery(sql);
                if (resultSet.next()) {
                    int roomnumber = resultSet.getInt("room_number");
                    System.out.println("Room number for reservation id " + reservationid + " and guest name is:'" + guestname + "' is: " + roomnumber);
                } else {
                    System.out.println(" Sorry there is No any booking with this id and name....");
                    System.out.println("Kindly check id and name again....");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static  void updateReservation(Connection connection,Scanner sc,Statement stmt){
        System.out.println("Enter reservation id to update : ");
        int reservationId = sc.nextInt();
        sc.nextLine(); // consume next line character
        if(!reservationExist(connection,reservationId)){
            System.out.println("No any reservation found ....");
            return;
        }
        System.out.print("enter new guest name : ");
        String newguestname = sc.nextLine();

        System.out.println("enter new room number :");
        int newroomnumber  = sc.nextInt();
        System.out.println("enter the contact details :");
        String newcontact  = sc.nextLine();

        String sql  = "UPDATE reservation SET guest_name = '" + newguestname +
                "', room_number = " + newroomnumber +
                ",  contact= '" + newcontact +
                "' WHERE reservation_id = " + reservationId ;

        try{
            int affectedrows = stmt.executeUpdate(sql);
            if(affectedrows>0){
                System.out.println("Reservation update successfully!..");
            }
            else{
            System.out.println("Update failed ...");
           }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public  static  void deleteReservation(Connection connection,Scanner sc, Statement stmt){
        try{
            System.out.println("Enter reservation_id to delete: ");
            int reservationId = sc.nextInt();
            if(!reservationExist(connection,reservationId)){
                System.out.println("No any reservation found ....");
                return;
            }
            String sql = "DELETE FROM reservation WHERE reservation_id="+reservationId;

            try{
                int affectedrows = stmt.executeUpdate(sql);
                if(affectedrows>0){
                    System.out.println("Reservation deleted successfully");
                }
                else{
                    System.out.println("Reservation deletion failed !");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static boolean reservationExist(Connection connection,int reservationId){
      try{
          String sql = "SELECT reservation_id FROM reservation WHERE reservation_id = "+reservationId;
           try(Statement stmt = connection.createStatement();
                ResultSet resultSet = stmt.executeQuery(sql)){
                 return resultSet.next(); //if there's a result, the reservation exists;
           } catch (SQLException e) {
               throw new RuntimeException(e);
           }
      } catch (RuntimeException e) {
          throw new RuntimeException(e);
      }
    }

    public static void Exist() throws InterruptedException{
        System.out.print("Existing System");
        int i =3;
        while (i!=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using our Hotel Reservation System...!");
    }
}
