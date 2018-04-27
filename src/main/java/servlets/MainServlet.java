package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import exceptions.LoginException;
import exceptions.PostException;

@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MainServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String userName = (String) request.getSession().getAttribute("USER");
		if (session == null || userName == null) {
			request.getSession(true);
			response.sendRedirect("./");
			return;
		}				

		RequestDispatcher rd1 = request.getRequestDispatcher("Header.jsp");
		rd1.include(request, response);
		response.setContentType("text/html");
		
		// it should list the posts of the followers
		RequestDispatcher rd3 = request.getRequestDispatcher("LinkToPost.jsp");
		rd3.include(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
