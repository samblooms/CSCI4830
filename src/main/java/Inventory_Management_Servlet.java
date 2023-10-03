

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/Inventory_Management_Servlet")
public class Inventory_Management_Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public Inventory_Management_Servlet() {
        super();
        // TODO Auto-generated constructor stub
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Set the content type to HTML as we will be outputting HTML content
        response.setContentType("text/html");
        
        // Obtain the PrintWriter object from response to write the HTML content.
        java.io.PrintWriter out = response.getWriter();
        
        // Writing "Hello, World!" HTML content to response
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Hello, World!</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Hello, World!</h1>");
        out.println("</body>");
        out.println("</html>");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
