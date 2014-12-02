import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
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

    public StudentController controller;

    // test addition
    Authentication(Connection establishedConnection){
        controller = new StudentController();
        connection = establishedConnection;
        Scanner in = new Scanner(System.in);

        int choice = 0;
        do {
            choice = 0;

            System.out.println("----------------------------");
            System.out.println("   Team 6 - Data Modeling   ");
            System.out.println("----------------------------");
            System.out.println("Please make your selection: ");
            System.out.println("[1] Login as Student");
            System.out.println("[2] Login as Faculty");
            System.out.println("[3] Login as Admin");
            System.out.println("[4] Exit the program");
            System.out.print(" >>   ");

            try {
                choice = in.nextInt();
            }catch(InputMismatchException e)
           {
               in.nextLine();
           }

            String login = null;
            switch (choice) {
                case 1:
                    login = studentLogin();
                    controller.setConnection(connection);
                    controller.startSession(login);
                    break;
                case 2:
                    login = facultyLogin();
                    if(login != null){
                        Faculty faculty = new Faculty(connection);
                        faculty.mainMenu();
                    }
                    break;
                case 3:
                    login = adminLogin();
                    if (login != null) {
                        Admin adminclass = new Admin(connection);
                        adminclass.mainMenu();
                    }
                    break;
                case 4:
                    // destroy session, log out -- move this close to official program exit
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                default:
                    System.out.println("===========Please enter valid selection.===========");
            }
        } while(choice != 4);
    }

    public String studentLogin() {
        PreparedStatement ps = null;
        ResultSet rset = null;
        Scanner in = new Scanner(System.in);
        String nNum;
        String name;

        System.out.println("");
        System.out.print("Please enter your N Number: ");
        nNum = in.next();

        if(nNum.charAt(0) == 'n' || nNum.charAt(0) == 'N')
        {
            nNum = nNum.substring(1);
        }

        try {
            ps = connection.prepareStatement("SELECT * FROM Student WHERE n_number = '" + nNum + "'");
            rset = ps.executeQuery();
            if (rset.next())
            {
                name = rset.getString("first_name") + " " + rset.getString("last_name");
                System.out.println("Welcome, " + name);
                System.out.println("");
                return nNum;
            }
            else {
                return nNum;
            }

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

        System.out.println("");
        System.out.print("N number: ");
        username = in.next();
        System.out.print("Password: ");
        password = in.next();


        if(username.charAt(0) == 'n' || username.charAt(0) == 'N')
        {
            username = username.substring(1);
        }

        try {
            ps = connection.prepareStatement("SELECT * FROM Faculty WHERE n_number = '" + username + "' AND password = '" + password + "'" );
            rset = ps.executeQuery();

            if (rset.next())
            {
                name = rset.getString("first_name") + " " + rset.getString("last_name");
                System.out.println("Welcome, " + name);
                System.out.println("");
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

        System.out.println("");
        System.out.print("N number: ");
        username = in.next();
        System.out.print("Password: ");
        password = in.next();


        if(username.charAt(0) == 'n' || username.charAt(0) == 'N')
        {
            username = username.substring(1);
        }

        try {
            ps = connection.prepareStatement("SELECT * FROM Faculty WHERE n_number = '" + username + "' AND password = '" + password + "' AND is_administrator = 1" );
            rset = ps.executeQuery();
            if (rset.next())
            {
                name = rset.getString("first_name") + " " + rset.getString("last_name");
                System.out.println("Welcome, " + name);
                System.out.println("");
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

