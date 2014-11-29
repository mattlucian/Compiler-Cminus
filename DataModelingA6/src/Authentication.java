import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Katelin on 11/28/2014.
 */
public class Authentication {
    public static Connection connection;

    Authentication(Connection establishedConnection){
        connection = establishedConnection;
        Scanner in = new Scanner(System.in);

        int choice;
        System.out.println("What are you?");
        System.out.println("1. Student");
        System.out.println("2. Faculty");
        System.out.println("3. Admin");

        choice = in.nextInt();
        String login = null;
        switch(choice)
        {
            case 1:
                login = studentLogin();
                if(login != null) {
                    StudentController controller = new StudentController();
                    controller.setConnection(connection);
                    controller.startSession(login);
                }
                break;
            case 2:
                login = facultyLogin();
                break;
            case 3:
                login = adminLogin();
                if(login != null){
                    Admin adminclass = new Admin(connection);
                    adminclass.mainMenu();
                }
                break;
            default:
                System.out.println("Please enter valid selection.");
        }
        try{
            connection.close();
        }catch (SQLException e){
            System.out.println("Error: "+e.getMessage());
        }
    }

    public static String studentLogin() {
        PreparedStatement ps = null;
        ResultSet rset = null;
        Scanner in = new Scanner(System.in);
        String nNum;
        String name;

        System.out.println("Please enter your N Number:");
        nNum = in.next();
        try {
            ps = connection.prepareStatement("SELECT * FROM Student WHERE n_number = '" + nNum + "'");
            rset = ps.executeQuery();
            if (rset.next())
            {
                name = rset.getString("first_name") + " " + rset.getString("last_name");
                System.out.println("Welcome, " + name);
                return nNum;
            }
            else
                System.out.println("You are not a current student.");
        } catch (SQLException e) {
            System.out.println("Try again.");
        }
        return null;
    }

    public static String facultyLogin() {
        PreparedStatement ps = null;
        ResultSet rset = null;

        Scanner in = new Scanner(System.in);

        String username;
        String password;
        String name;

        System.out.print("N number: ");
        username = in.next();
        System.out.print("Password: ");
        password = in.next();

        System.out.println(username + " " + password);
        try {
            ps = connection.prepareStatement("SELECT * FROM Faculty WHERE n_number = '" + username + "' AND password = '" + password + "'" );
            rset = ps.executeQuery();

            if (rset.next())
            {
                name = rset.getString("first_name") + " " + rset.getString("last_name");
                System.out.println("Welcome, " + name);
                return username;
            }
            else
                System.out.println("Incorrect faculty credentials.");
        } catch (SQLException e) {
            System.out.println("Try again.");
        }
        return null;
    }

    public static String adminLogin()
    {
        PreparedStatement ps = null;
        ResultSet rset = null;

        Scanner in = new Scanner(System.in);

        String username;
        String password;
        String name;

        System.out.print("N number: ");
        username = in.next();
        System.out.print("Password: ");
        password = in.next();

        System.out.println(username + " " + password);
        try {
            ps = connection.prepareStatement("SELECT * FROM Faculty WHERE n_number = '" + username + "' AND password = '" + password + "' AND is_administrator = 1" );
            rset = ps.executeQuery();
            if (rset.next())
            {
                name = rset.getString("first_name") + " " + rset.getString("last_name");
                System.out.println("Welcome, " + name);
                return username;
            }
            else
                System.out.println("Incorrect admin credentials.");
        } catch (SQLException e) {
            System.out.println("Try again.");
        }
        return null;
    }

}

