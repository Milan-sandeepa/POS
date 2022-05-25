package dao;

import db.DBConnection;
import model.CustomerDTO;
import model.ItemDTO;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {

    @Override
    public ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {

        ResultSet rst = SQLUtil.executeQuery("SELECT * FROM Item");
        ArrayList<ItemDTO> allitems = new ArrayList<>();

        while (rst.next()){
            allitems.add(new ItemDTO(rst.getString(1),rst.getString(2),rst.getBigDecimal(3),rst.getInt(4)));
        }
        return allitems;
    }

    @Override
    public boolean saveItem(ItemDTO dto) throws SQLException, ClassNotFoundException {

        return SQLUtil.executeUpdate("INSERT INTO Item (code, description, unitPrice, qtyOnHand) VALUES (?,?,?,?)",dto.getCode(),dto.getDescription(),dto.getUnitPrice(),dto.getQtyOnHand());
    }

    @Override
    public boolean updateItem(ItemDTO dto) throws SQLException, ClassNotFoundException {

       return SQLUtil.executeUpdate("UPDATE Item SET description=?, unitPrice=?, qtyOnHand=? WHERE code=?",dto.getDescription(),dto.getUnitPrice(),dto.getQtyOnHand(),dto.getCode());
    }

    @Override
    public boolean deleteItem(String id) throws SQLException, ClassNotFoundException {

        return SQLUtil.executeUpdate("DELETE FROM Item WHERE code=?",id);
    }

    @Override
    public boolean existItem(String id) throws SQLException, ClassNotFoundException {

       return SQLUtil.executeUpdate("SELECT code FROM Item WHERE code=?",id);
    }

    @Override
    public String generateNewItemID() throws SQLException, ClassNotFoundException {

        ResultSet rst = SQLUtil.executeQuery("SELECT code FROM Item ORDER BY code DESC LIMIT 1;");
        if (rst.next()) {
            String id = rst.getString("code");
            int newItemId = Integer.parseInt(id.replace("I00-", "")) + 1;
            return String.format("I00-%03d", newItemId);
        } else {
            return "I00-001";
        }
    }
}
