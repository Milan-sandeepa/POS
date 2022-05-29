package bo;

import dao.custom.CustomerDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailsDAO;
import dao.custom.impl.CustomerDAOImpl;
import dao.custom.impl.ItemDAOImpl;
import dao.custom.impl.OderDAOImpl;
import dao.custom.impl.OderDetailsDAOImpl;
import db.DBConnection;
import model.CustomerDTO;
import model.ItemDTO;
import model.OrderDTO;
import model.OrderDetailDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderBOImpl {

    private CustomerDAO customerDAO = new CustomerDAOImpl();
    private ItemDAO itemDAO = new ItemDAOImpl();
    private OrderDAO oderDAO = new OderDAOImpl();
    private OrderDetailsDAO oderDetailsDAO = new OderDetailsDAOImpl();

    public boolean purchaseOrder(String orderId, LocalDate orderDate, String customerId, List<OrderDetailDTO> orderDetails) throws SQLException, ClassNotFoundException {
        /*Transaction*/
        Connection connection = null;

            connection = DBConnection.getDbConnection().getConnection();
            /*if order id already exist*/
            if (oderDAO.exist(orderId)) {

            }
            connection.setAutoCommit(false);

            boolean save = oderDAO.save(new OrderDTO(orderId, orderDate, customerId));

            if (!save) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            for (OrderDetailDTO detail : orderDetails) {

                boolean save1 = oderDetailsDAO.save(detail);

                if (!save1) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
//                //Search & Update Item
//                ItemDTO item = findItem(detail.getItemCode());
                ItemDTO item = null;
                item.setQtyOnHand(item.getQtyOnHand() - detail.getQty());

                boolean update = itemDAO.update(new ItemDTO(item.getCode(), item.getDescription(), item.getUnitPrice(), item.getQtyOnHand()));

                if (!update) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
            return true;
    }

    public CustomerDTO searchCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.search(id);
    }

    public ItemDTO searchItem(String id) throws SQLException, ClassNotFoundException {
        return itemDAO.search(id);
    }

    public boolean checkItemIsAvailable(String code) throws SQLException, ClassNotFoundException {
       return itemDAO.exist(code);
    }

    public boolean checkCustomerIsAvailable(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.exist(id);
    }

    public String generateNewOrderID() throws SQLException, ClassNotFoundException {
        return oderDAO.generateNewID();
    }

    public ArrayList<CustomerDTO> gerAllCustomers() throws SQLException, ClassNotFoundException {
        return customerDAO.getAll();
    }

    public ArrayList<ItemDTO> gerAllItem() throws SQLException, ClassNotFoundException {
        return itemDAO.getAll();
    }
}