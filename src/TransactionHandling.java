import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionHandling {
    public static void main(String[] args) throws  ClassNotFoundException{
        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username  = "root";
        String password = "Kishan@5822";
        String withdraw = "UPDATE accounts SET balance  = balance - ? WHERE account_number = ?";
        String deposit =  "UPDATE accounts SET balance  = balance + ? WHERE account_number = ?";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successful");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try {

            Connection connection = DriverManager.getConnection(url,username,password);
            System.out.println("Connection established successfully");
            connection.setAutoCommit(false);

            try{
                PreparedStatement withdrawStatement = connection.prepareStatement(withdraw);
                PreparedStatement depositStatement  = connection.prepareStatement(deposit);
                withdrawStatement.setDouble(1,0.00);
                withdrawStatement.setString(2, "account23");
                depositStatement.setDouble(1,3000.00);
                depositStatement.setString(2,"account56");
               int WithdrawRowAffected =  withdrawStatement.executeUpdate();
               int DepositRowAffected  =  depositStatement.executeUpdate();
               if(WithdrawRowAffected>0 && DepositRowAffected>0){
                   connection.commit();
                   System.out.println("Transaction successful");
               }
             else{
                   connection.rollback();
                   System.out.println("Transaction failed ");
               }
                connection.close();
                System.out.println("Connection closed");

            }catch (Exception e){
                System.out.println(e.getMessage());

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
