import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static final String url = "jdbc:postgresql://localhost:5432/mail_db";
    public static final String user = "postgres";
    public static final String passwordAdmin = "123";

    public static void main(String[] args) {


        mainMenu();

        Scanner scanner = new Scanner(System.in);
        int mainOption = scanner.nextInt();
        switch (mainOption) {
            case 1:
                System.out.print("email : ");
                String mail = scanner.next();

                System.out.print("password : ");
                String password = scanner.next();

                System.out.print("name : ");
                String name = scanner.next();

                System.out.print("surname : ");
                String surname = scanner.next();

                try {
                    Connection connection = DriverManager.getConnection(url, user, passwordAdmin);
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(getQuery(mail, password, name, surname));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            case 2:
                try {
                    System.out.print("email : ");
                    String mail2 = scanner.next();

                    System.out.print("password : ");
                    String password2 = scanner.next();
                    Connection connection = DriverManager.getConnection(url, user, passwordAdmin);
                    Statement statement = connection.createStatement();
                    String selectQuery = "SELECT * from user_man where mail='" + mail2 + "'";
                    ResultSet resultSet = statement.executeQuery(selectQuery);
                    if (resultSet.next()) {

                        String resultPassword = resultSet.getString("password");
                        Integer userId = resultSet.getInt("id");
                        String email = resultSet.getString("mail");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        User currentuser = new User(userId, email, resultPassword, firstName, lastName);

                        if (password2.equals(resultPassword)) {
                            showMenu(currentuser);
                        } else {
                            System.out.println("Password not matched !");
                        }

                    } else {
                        System.out.println("MAIL NOT FOUND !");

                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace

                            ();
                }
                break;

        }


    }

    public static String getQuery(String mail, String password, String first_name, String last_name) {
        return "INSERT INTO user_man (mail,password,first_name,last_name) values('" + mail + "','" + password + "','" + first_name + "','" + last_name + "')";

    }

    public static void showMenu(User currentuser) {
        System.out.println("\n\n\n");
        System.out.println(currentuser.getEmail());
        System.out.println("-------MENU-------");
        System.out.println("1.Received email");
        System.out.println("2.Sent email");
        System.out.println("3.Send email");
        System.out.println("4.Delete email");
        Scanner scanner = new Scanner(System.in);
        int menuOption = scanner.nextInt();
        switch (menuOption) {
            case 1:
                receivedEmails(currentuser);
                break;

            case 2:
                sentEmails(currentuser);
                break;

            case 3:
                sendEmail(currentuser);
                break;
            case 4:
                deleteEmail(currentuser);
                break;
            default:
                System.out.println("NO OTHER OPTIONS !");
        }

    }

    public static void receivedEmails(User currentuser) {
        System.out.println("1.Search by date");
        System.out.println("2.Default");
        Scanner scanner = new Scanner(System.in);
        int searchOption = scanner.nextInt();
        if (searchOption == 1) {
            String searchDate = scanner.next();
            try {
                Connection connection = DriverManager.getConnection(url, user, passwordAdmin);
                String ALL_EMAILS_USER = "SELECT * from mail where receiver_id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(ALL_EMAILS_USER);
                preparedStatement.setInt(1, currentuser.getId());
                ResultSet resultSet = preparedStatement.executeQuery();

                List<Mail> mailList = new ArrayList<Mail>();

                while (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String body = resultSet.getString("body");
                    Integer senderId = resultSet.getInt("sender_id");
                    User sender = getUser(senderId);
                    Integer receiverId = resultSet.getInt("receiver_id");
                    User receiver = getUser(receiverId);
                    Timestamp date = resultSet.getTimestamp("created_at");
                    boolean isRead = resultSet.getBoolean("is_read");
                    boolean isDeleted = resultSet.getBoolean("is_deleted");
                    Mail mail = new Mail(id, title, body, sender, receiver, date, isRead, isDeleted);
                    mailList.add(mail);
                    if (String.valueOf(mail.getDeleted()).equals("false")) {
                        if (String.valueOf(mail.getCreatedAt()).contains(searchDate)) {
                            System.out.println("id : " + mail.getId() + "\nreceiver : " + receiver.getEmail() + "\nsender : " + sender.getEmail() + "\ntitle : " + mail.getTitle() + "\n" + "body : " + mail.getBody() + "\ncreated at : " + mail.getCreatedAt() + "\nisRead : " + mail.getRead() + "\nisDeleted : " + mail.getDeleted());
                            System.out.println();
                            String is_Read = String.valueOf(mail.getRead());
                            if (is_Read.equals("false")) {
                                String updateIsRead = "update mail set is_read='true' where id='" + mail.getId() + "'";
                                Connection connection1 = DriverManager.getConnection(url, user, passwordAdmin);
                                Statement statement = connection1.createStatement();
                                statement.executeUpdate(updateIsRead);
                            }
                        }

                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        if (searchOption == 2) {
            try {
                Connection connection = DriverManager.getConnection(url, user, passwordAdmin);
                String ALL_EMAILS_USER = "SELECT * from mail where receiver_id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(ALL_EMAILS_USER);
                preparedStatement.setInt(1, currentuser.getId());
                ResultSet resultSet = preparedStatement.executeQuery();

                List<Mail> mailList = new ArrayList<Mail>();

                while (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String body = resultSet.getString("body");
                    Integer senderId = resultSet.getInt("sender_id");
                    User sender = getUser(senderId);
                    Integer receiverId = resultSet.getInt("receiver_id");
                    User receiver = getUser(receiverId);
                    Timestamp date = resultSet.getTimestamp("created_at");
                    boolean isRead = resultSet.getBoolean("is_read");
                    boolean isDeleted = resultSet.getBoolean("is_deleted");
                    Mail mail = new Mail(id, title, body, sender, receiver, date, isRead, isDeleted);
                    mailList.add(mail);
                    if (String.valueOf(mail.getDeleted()).equals("false")) {
                        System.out.println("id : " + mail.getId() + "\nreceiver : " + receiver.getEmail() + "\nsender : " + sender.getEmail() + "\ntitle : " + mail.getTitle() + "\n" + "body : " + mail.getBody() + "\ncreated at : " + mail.getCreatedAt() + "\nisRead : " + mail.getRead() + "\nisDeleted : " + mail.getDeleted());
                        System.out.println();
                        String is_Read = String.valueOf(mail.getRead());
                        if (is_Read.equals("false")) {
                            String updateIsRead = "update mail set is_read='true' where id='" + mail.getId() + "'";
                            Connection connection1 = DriverManager.getConnection(url, user, passwordAdmin);
                            Statement statement = connection1.createStatement();
                            statement.executeUpdate(updateIsRead);
                        }
                    }

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


        }
    }

    public static void sentEmails(User currentuser) {
        System.out.println("1.Search by date");
        System.out.println("2.Default");
        Scanner scanner = new Scanner(System.in);
        int searchOption = scanner.nextInt();
        if (searchOption == 1) {
            String searchDate = scanner.next();
            try {
                Connection connection = DriverManager.getConnection(url, user, passwordAdmin);
                String ALL_EMAILS_USER = "SELECT * from mail where sender_id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(ALL_EMAILS_USER);
                preparedStatement.setInt(1, currentuser.getId());
                ResultSet resultSet = preparedStatement.executeQuery();

                List<Mail> mailList = new ArrayList<Mail>();

                while (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String body = resultSet.getString("body");
                    Integer senderId = resultSet.getInt("sender_id");
                    User sender = getUser(senderId);
                    Integer receiverId = resultSet.getInt("receiver_id");
                    User receiver = getUser(receiverId);
                    Timestamp date = resultSet.getTimestamp("created_at");
                    boolean isRead = resultSet.getBoolean("is_read");
                    boolean isDeleted = resultSet.getBoolean("is_deleted");
                    Mail mail = new Mail(id, title, body, sender, receiver, date, isRead, isDeleted);
                    mailList.add(mail);
                    if (String.valueOf(mail.getDeleted()).equals("false")) {
                        if (String.valueOf(mail.getCreatedAt()).contains(searchDate)) {
                            System.out.println("id : " + mail.getId() + "\nreceiver : " + receiver.getEmail() + "\ntitle : " + mail.getTitle() + "\n" + "body : " + mail.getBody() + "\ncreated at : " + mail.getCreatedAt() + "\nisRead : " + mail.getRead() + "\nisDeleted : " + mail.getDeleted());
                            System.out.println();
                        }
                    }
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        if (searchOption == 2) {
            try {
                Connection connection = DriverManager.getConnection(url, user, passwordAdmin);
                String ALL_EMAILS_USER = "SELECT * from mail where sender_id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(ALL_EMAILS_USER);
                preparedStatement.setInt(1, currentuser.getId());
                ResultSet resultSet = preparedStatement.executeQuery();

                List<Mail> mailList = new ArrayList<Mail>();

                while (resultSet.next()) {
                    Integer id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String body = resultSet.getString("body");
                    Integer senderId = resultSet.getInt("sender_id");
                    User sender = getUser(senderId);
                    Integer receiverId = resultSet.getInt("receiver_id");
                    User receiver = getUser(receiverId);
                    Timestamp date = resultSet.getTimestamp("created_at");
                    boolean isRead = resultSet.getBoolean("is_read");
                    boolean isDeleted = resultSet.getBoolean("is_deleted");
                    Mail mail = new Mail(id, title, body, sender, receiver, date, isRead, isDeleted);
                    mailList.add(mail);
                    if (String.valueOf(mail.getDeleted()).equals("false")) {
                        System.out.println("id : " + mail.getId() + "\nreceiver : " + receiver.getEmail() + "\ntitle : " + mail.getTitle() + "\n" + "body : " + mail.getBody() + "\ncreated at : " + mail.getCreatedAt() + "\nisRead : " + mail.getRead() + "\nisDeleted : " + mail.getDeleted());
                        System.out.println();
                    }
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }

    public static User getUser(Integer userId) {
        String selectQuery = "select * from user_man where id='" + userId + "'";
        User currentUser = null;

        try {
            Connection connection = DriverManager.getConnection(url, user, passwordAdmin);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            if (resultSet.next()) {

                String resultPassword = resultSet.getString("password");
                Integer id = resultSet.getInt("id");
                String email = resultSet.getString("mail");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                currentUser = new User(id, email, resultPassword, firstName, lastName);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return currentUser;
    }

    public static void sendEmail(User user1) {
        System.out.println("From " + user1.getEmail());
        Scanner scanner = new Scanner(System.in);
        System.out.print("To :");
        String toUser = scanner.next();
        System.out.print("Title : ");
        String title = scanner.next();
        System.out.print("Body : ");
        String body = scanner.next();
        try {
            Connection connection = DriverManager.getConnection(url, user, passwordAdmin);
            String EMAIL_INSERT = "INSERT INTO mail(title,body,sender_id,receiver_id,created_at,is_read,is_deleted) values(?,?,?,?,now(),false,false)";
            PreparedStatement preparedStatement = connection.prepareStatement(EMAIL_INSERT);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, body);
            preparedStatement.setInt(3, user1.getId());
            preparedStatement.setInt(4, findToUserId(toUser));
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    public static void deleteEmail(User currentuser) {
        System.out.println("1.Delete received emails");
        System.out.println("2.Delete sent emails");
        Scanner scannerDelete = new Scanner(System.in);
        int deleteOption = scannerDelete.nextInt();

        if (deleteOption == 1) {
            receivedEmails(currentuser);
            System.out.println("If you want delete email please enter <<<email id>>>");
            int email_id = scannerDelete.nextInt();

            try {
                Connection connection = DriverManager.getConnection(url, user, passwordAdmin);
                Statement statement = connection.createStatement();
                String sqlDeleteEmail = "update mail set is_deleted='true'  where id='" + email_id + "'";
                statement.executeUpdate(sqlDeleteEmail);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

        if (deleteOption == 2) {
            sentEmails(currentuser);
            System.out.println("If you want delete email please enter <<<email id>>>");
            int email_id = scannerDelete.nextInt();
            try {

                Connection connection = DriverManager.getConnection(url, user, passwordAdmin);
                Statement statement = connection.createStatement();
                String sqlDeleteEmail = "update mail set is_deleted='true'  where id='" + email_id + "'";
                statement.executeUpdate(sqlDeleteEmail);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    public static Integer findToUserId(String toUser) {

        try {
            Connection connection = DriverManager.getConnection(url, user, passwordAdmin);
            Statement statement = connection.createStatement();
            String selectQuery = "SELECT id from user_man where mail='" + toUser + "'";
            ResultSet resultSet = statement.executeQuery(selectQuery);
            if (resultSet.next()) {
                return resultSet.getInt("id");

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;

    }

    public static void mainMenu() {
        System.out.println("----------------");
        System.out.println("1.Sign up");
        System.out.println("2.Log in");
        System.out.println("----------------");

    }

}
