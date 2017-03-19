package tools;

/**
 *
 * @author Ceyhun
 */
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Controller {

    private static Connection conn;
    public Controller() {
        try {
            conn = DriverManager.getConnection("jdbc:ucanaccess://D:/Cloud/Documents/sql/izim.accdb");
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }

    public static Boolean loginController(String username, String password) {
        boolean suc = false;
        if (username.equals("admin") && password.equals("admin")) {
            suc = true;
        }
        return suc;
    }

    public  List<Field> showFields(String search) {
        List<Field> fields = new ArrayList<>();
        
        
        String query = "select * from phoneBook where Fname like ? or Lname like ?"
                + " or FatherName like ? or PhoneNumber like ?";
        
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");
            ps.setString(3, "%" + search + "%");
            ps.setString(4, "%" + search + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Field fld = new Field();
                fld.setId(rs.getString("ID"));
                fld.setFirstName(rs.getString("Fname"));
                fld.setLastName(rs.getString("Lname"));
                fld.setFatherName(rs.getString("FatherName"));
                fld.setPhoneNumber(rs.getString("PhoneNumber"));
                fld.setBornDate(rs.getString("BornDate"));
                fields.add(fld);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fields;
    }

    public void addField(String id, String firstName, String lastName, String fatherName, String phoneNumber, String bornDate) {

        try {
            String query;
            if (!id.equals("Yeni")) {
               
                query = "update phoneBook set Fname=?,Lname=?,FatherName=?,PhoneNumber=?,BornDate=? where ID=?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setString(1, firstName);
                ps.setString(2, lastName);
                ps.setString(3, fatherName);
                ps.setString(4, phoneNumber);
                ps.setString(5, bornDate);
                ps.setString(6, id);
                ps.execute();
            }else{
            query = "insert into phoneBook (Fname, Lname, FatherName, PhoneNumber, BornDate) values(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, fatherName);
            ps.setString(4, phoneNumber);
            ps.setString(5, bornDate);
            ps.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteField(String id) {
        String query = "delete from phoneBook where ID=?";
        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, id);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
