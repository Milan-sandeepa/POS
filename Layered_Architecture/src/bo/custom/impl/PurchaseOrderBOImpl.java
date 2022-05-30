package bo.custom.impl;

import bo.custom.PurchaseOrderBO;
import dao.DAOFactory;
import dao.custom.*;
import db.DBConnection;
import dto.CustomerDTO;
import dto.ItemDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderBOImpl implements PurchaseOrderBO {

    private CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEM);
    private OrderDAO oderDAO = (OrderDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDER);
    private OrderDetailsDAO oderDetailsDAO = (OrderDetailsDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILS);
    private QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERYDAO);

    @Override
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

                ItemDTO item = searchItem(detail.getItemCode());
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

    @Override
    public CustomerDTO searchCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.search(id);
    }

    @Override
    public ItemDTO searchItem(String id) throws SQLException, ClassNotFoundException {
        return itemDAO.search(id);
    }

    @Override
    public boolean checkItemIsAvailable(String code) throws SQLException, ClassNotFoundException {
       return itemDAO.exist(code);
    }

    @Override
    public boolean checkCustomerIsAvailable(String id) throws SQLException, ClassNotFoundException {
        return customerDAO.exist(id);
    }

    @Override
    public String generateNewOrderID() throws SQLException, ClassNotFoundException {
        return oderDAO.generateNewID();
    }

    @Override
    public ArrayList<CustomerDTO> gerAllCustomers() throws SQLException, ClassNotFoundException {
        return customerDAO.getAll();
    }

    @Override
    public ArrayList<ItemDTO> gerAllItem() throws SQLException, ClassNotFoundException {
        return itemDAO.getAll();
    }
}
