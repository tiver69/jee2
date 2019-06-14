import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/get")
public class GetInfoServlet extends HttpServlet {

    private CarDAO DataBaseSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        DataBaseSource = CarDAO.getCarDAO();

        try {
            List<Car> getCar = DataBaseSource.getAll();
            out.print("<table width=25% border=1");
            out.print("<center><h1>CAR TABLE</h1></center>");
            for (Car car : getCar) {
                out.print("<tr>");
                out.print("<td>"+car.getId()+"</td>");
                out.print("<td>"+car.getBrand()+"</td>");
                out.print("<td>"+car.getModel()+"</td></tr>");
            }
            out.print("</table>");
        }
        catch (SQLException e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("<html><body><h1>Data "
                    + "Base unavailable</h1></body></html>");
        }
    }
}