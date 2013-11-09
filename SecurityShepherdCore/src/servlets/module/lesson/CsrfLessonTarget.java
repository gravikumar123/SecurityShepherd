package servlets.module.lesson;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import utils.Validate;

/**
 * CSRF Lesson module Target - Does not return result key
 * <br/><br/>
 * This file is part of the Security Shepherd Project.
 * 
 * The Security Shepherd project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.<br/>
 * 
 * The Security Shepherd project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.<br/>
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Security Shepherd project.  If not, see <http://www.gnu.org/licenses/>. 
 * @author Mark Denihan
 *
 */
public class CsrfLessonTarget extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static org.apache.log4j.Logger log = Logger.getLogger(ed4182af119d97728b2afca6da7cdbe270a9e9dd714065f0f775cd40dc296bc7.class);
	public void doGet (HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException
	{
		log.debug("Cross-Site Request Forgery Lesson Target Servlet");
		PrintWriter out = response.getWriter();  
		out.print(getServletInfo());
		try
		{
			HttpSession ses = request.getSession(true);
			if(Validate.validateAdminSession(ses))
			{
				log.debug("CSRF Lesson Target Hit By Admin");
				out.write("<p>User Marked as completed CSRF Lesson</p>");
			}
			else
			{
				log.debug("CSRF Lesson Target Hit");
				out.write("<p>You must be an administrator to perform this function</p>");
			}
		}
		catch(Exception e)
		{
			log.error("CsrfLessonTarget Error: " + e.toString());
		}
	}
}
