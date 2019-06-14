import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/add")
public class AddInfoServlet extends HttpServlet {
    private String id = "", brand = "", model = "";
    private Car car = null;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        id = request.getParameter("id");
        brand = request.getParameter("brand");
        model = request.getParameter("model");

        try {
            if (!validation()){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("<html><body><h1>Wrong "
                        + "input data.</h1></body></html>");
                return;
            }
            car = new Car(Integer.parseInt(id), brand, model);
            CarDAO dataBase = CarDAO.getCarDAO();
            dataBase.insert(car);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("<html><body><h1>Data "
                    + "Base unavailable</h1></body></html>");
            return;
        }

        response.setContentType("text/html");
        response.getWriter().println("<h3>Successfully add car:</h3><br>");
        response.getWriter().println("<h4>" + car.toString()+"</h4><br>");
        response.getWriter().println("<input type=\"button\" value=\"Return\"" +
                "onclick='location.href=\"/index.html\"'>");
    }

    private boolean validation() throws SQLException {
        if (id.isEmpty()|| model.isEmpty()|| brand.isEmpty()) return false;

        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e){
            return false;
        }

        CarDAO dataBase = CarDAO.getCarDAO();
        return dataBase.selectById(Integer.parseInt(id)) == null;
    }
}
