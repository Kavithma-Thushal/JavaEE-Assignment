package lk.ijse.gdse66.pos.api;

import jakarta.json.*;
import lk.ijse.gdse66.pos.dto.CustomerDTO;
import lk.ijse.gdse66.pos.util.CrudUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author : Kavithma Thushal
 * @project : JavaEE-POS
 * @since : 6:56 PM - 1/15/2024
 **/
@WebServlet(urlPatterns = "/customer", initParams = {
        @WebInitParam(name = "username", value = "root"),
        @WebInitParam(name = "password", value = "1234"),
        @WebInitParam(name = "url", value = "jdbc:mysql://localhost:3306/javaee_customer")})
public class CustomerServlet extends HttpServlet {
    private String username;
    private String password;
    private String url;

    @Override
    public void init() throws ServletException {
        /*ServletConfig is used to get configuration information such as database username, password and url*/
        ServletConfig servletConfig = getServletConfig();
        username = servletConfig.getInitParameter("username");
        password = servletConfig.getInitParameter("password");
        url = servletConfig.getInitParameter("url");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String address = req.getParameter("address");
        double salary = Double.parseDouble(req.getParameter("salary"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "INSERT INTO Customer VALUES (?,?,?,?)";
            CustomerDTO c = new CustomerDTO(id, name, address, salary);
            boolean b = CrudUtil.execute(connection, sql, c.getId(), c.getName(), c.getAddress(), c.getSalary());
            if (b) {
                JsonObjectBuilder responseObject = Json.createObjectBuilder();
                responseObject.add("state", "OK");
                responseObject.add("message", "Customer Saved Successfully...!");
                responseObject.add("data", "");
                resp.getWriter().print(responseObject.build());
            } else {
                System.out.println("Error...!");
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error...!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonArrayBuilder allCustomers = Json.createArrayBuilder();

        String id = req.getParameter("id");
        String option = req.getParameter("option");

        PrintWriter writer = resp.getWriter();
        switch (option) {
            case "searchCusId":
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url, username, password);
                    String sql = "SELECT * FROM Customer WHERE id=?";

                    ResultSet rst = CrudUtil.execute(connection, sql, id);
                    ArrayList<CustomerDTO> allCustomer = new ArrayList<>();
                    while (rst.next()) {
                        allCustomer.add(new CustomerDTO(rst.getString(1), rst.getString(2), rst.getString(3), rst.getDouble(4)));
                    }

                    if (!allCustomer.isEmpty()) {
                        for (CustomerDTO customerDTO : allCustomer) {
                            JsonObjectBuilder customer = Json.createObjectBuilder();
                            customer.add("id", customerDTO.getId());
                            customer.add("name", customerDTO.getName());
                            customer.add("address", customerDTO.getAddress());
                            customer.add("salary", customerDTO.getSalary());
                            writer.print(customer.build());
                        }
                    } else {
                        System.out.println("Error...!");
                    }

                } catch (SQLException | ClassNotFoundException e) {
                    System.out.println("Error...!");
                }
                break;

            case "loadAllCustomer":
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection connection = DriverManager.getConnection(url, username, password);
                    String sql = "SELECT * FROM Customer";

                    ResultSet result = CrudUtil.execute(connection, sql);
                    ArrayList<CustomerDTO> obList = new ArrayList<>();
                    while (result.next()) {
                        obList.add(new CustomerDTO(result.getString(1), result.getString(2), result.getString(3), result.getDouble(4)));
                    }

                    for (CustomerDTO customerDTO : obList) {
                        JsonObjectBuilder customer = Json.createObjectBuilder();
                        customer.add("id", customerDTO.getId());
                        customer.add("name", customerDTO.getName());
                        customer.add("address", customerDTO.getAddress());
                        customer.add("salary", customerDTO.getSalary());
                        allCustomers.add(customer.build());
                    }

                    JsonObjectBuilder job = Json.createObjectBuilder();
                    job.add("state", "OK");
                    job.add("message", "Customer Loaded Successfully...!");
                    job.add("data", allCustomers.build());
                    resp.getWriter().print(job.build());

                } catch (ClassNotFoundException | SQLException e) {
                    System.out.println("Error...!");
                }
                break;
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customer = reader.readObject();

        String id = customer.getString("id");
        String name = customer.getString("name");
        String address = customer.getString("address");
        double salary = Double.parseDouble(customer.getString("salary"));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "UPDATE Customer SET name= ? , address=? , salary=? WHERE id=?";
            CustomerDTO c = new CustomerDTO(id, name, address, salary);
            boolean b = CrudUtil.execute(connection, sql, c.getName(), c.getAddress(), c.getSalary(), c.getId());
            if (b) {
                JsonObjectBuilder responseObject = Json.createObjectBuilder();
                responseObject.add("state", "OK");
                responseObject.add("message", "Customer Updated Successfully...!");
                responseObject.add("data", "");
                resp.getWriter().print(responseObject.build());
            } else {
                System.out.println("Error...!");
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error...!");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customer = reader.readObject();

        String id = customer.getString("id");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "DELETE FROM Customer WHERE id=?";
            boolean b = CrudUtil.execute(connection, sql, id);
            if (b) {
                JsonObjectBuilder rjo = Json.createObjectBuilder();
                rjo.add("state", "OK");
                rjo.add("message", "Customer Deleted Successfully...!");
                rjo.add("data", "");
                resp.getWriter().print(rjo.build());
            } else {
                System.out.println("Error...!");
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error...!");
        }
    }
}