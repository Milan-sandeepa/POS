package dao;

import model.ItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemDAO {
    public ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException;
    public boolean saveItem(ItemDTO dto) throws SQLException, ClassNotFoundException;
    public boolean updateItem(ItemDTO dto) throws SQLException, ClassNotFoundException;
    public boolean deleteItem(String id) throws SQLException, ClassNotFoundException;
    public boolean existItem(String id) throws SQLException, ClassNotFoundException;
    public String generateNewItemID() throws SQLException, ClassNotFoundException;
}
