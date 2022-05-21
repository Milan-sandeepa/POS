package dao;

import db.DBConnection;
import model.CustomerDTO;
import model.ItemDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ItemDAOImpl {
    public ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getDbConnection().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT * FROM Item");
        ArrayList<ItemDTO> allitems = new ArrayList<>();

        while (rst.next()){
            String code=rst.getString(1);
            String description=rst.getString(2);
            BigDecimal Price=rst.getBigDecimal(3);
            int qtyOnHand=rst.getInt(4);
            allitems.add(new ItemDTO(code,description,Price,qtyOnHand));
        }
        return allitems;
    }
}
