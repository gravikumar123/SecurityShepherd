package servlets.admin.userManagement;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.Encoder;

import utils.Validate;
import dbProcs.Setter;

/**
 * This class is the control structure of the Create class vulnerability
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
public class CreateClass extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static org.apache.log4j.Logger log = Logger.getLogger(CreateClass.class);
	
	/**
	 * Initiated by createClass.jsp. Class is added to the system if valid data has been submitted. Otherwise no class is added to the core database schema.
	 * Adding of class to Core Database is handed by Setter.createClass
	 * @param className Name of the new class
	 * @param classyear Class's year, in the format YY/YY, eg 11/12
	 * @param csrfToken
	 */
	public void doPost (HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException
	{
		log.debug("*** servlets.Admin.createClass ***");
		Encoder encoder = ESAPI.encoder();
		PrintWriter out = response.getWriter();  
		out.print(getServletInfo());
		HttpSession ses = request.getSession(true);
		if(Validate.validateAdminSession(ses))
		{
			Cookie tokenCookie = Validate.getToken(request.getCookies());
			Object tokenParmeter = request.getParameter("csrfToken");
			if(Validate.validateTokens(tokenCookie, tokenParmeter))
			{
				boolean notNull = false;
				boolean notEmpty = false;
				boolean validYear = false;
				boolean classValidate = false;
				try
				{
					log.debug("Getting ApplicationRoot");
					String ApplicationRoot = getServletContext().getRealPath("");
					log.debug("Servlet root = " + ApplicationRoot );
					
					log.debug("Getting Parameters");
					String className = (String)request.getParameter("className");
					log.debug("className = " + className);
					String classYear = (String)request.getParameter("classYear");
					log.debug("classYear = " + classYear);
					
					//Validation
					log.debug("Checking for nulls");
					notNull = (classYear != null && className != null);
					log.debug("Ensuring strings are not empty");
					notEmpty = (!classYear.isEmpty() && !className.isEmpty());
					log.debug("Validating Year");
					validYear = Validate.isValidClassYear(classYear);
					log.debug("Validating Name");
					classValidate = className.length() > 4 && className.length() <= 32;
	
					if(notNull && notEmpty && validYear && classValidate)
					{
						String reponseMessage = new String();
						//Data is good, Add user
						log.debug("Adding class to database");
						if(Setter.classCreate(ApplicationRoot, className, classYear))
						{
							reponseMessage = "<h2 class=\"title\">Class Created</h2><br>" +
								"<p>" +
								"Class <a>" + encoder.encodeForHTML(className) +"</a> of <a>" + encoder.encodeForHTML(classYear) + "</a> created successfully." +
								"</p>";
						}
						else
						{
							reponseMessage = "<h2 class=\"title\">Class Create Failure</h2><br>" +
							"<p>" +
							"Sorry but an error occured! Please try again. If the problem persists please contact an administrator!" +
							"<p>";
						}
						out.print(reponseMessage);
					}
					else
					{
						//Validation Error Responses
						String errorMessage = "An Error Occured: ";
						if(!notNull || !notEmpty)
						{
							log.error("Null values detected");
							errorMessage += "Invalid Request. Please try again";
						}
						else if(!validYear)
						{
							log.error("Invalid Year Detected");
							errorMessage += "Invalid Year submitted. Should be in format \"YY/YY\", eg; \"11/12\".";
						}
						else if(!classValidate)
						{
							log.error("Invalid Class Name");
							errorMessage += "Invalid Class Name. Please try again";
						}
						out.print("<h2 class=\"title\">Class Create Failure</h2><br>" +
								"<p><font color=\"red\">" +
								encoder.encodeForHTML(errorMessage) +
								"</font><p>");
					}
				}
				catch (Exception e)
				{
					log.error("Create New Class Error: " + e.toString());
					out.print("<h2 class=\"title\">Class Create Failure</h2><br>" +
							"<p>" +
							"<font color=\"red\">An error occured! Please try again.</font>" +
							"<p>");
				}
			}
			else
			{
				log.debug("CSRF tokens did not match");
				out.print("<h2 class=\"title\">Class Create Failure</h2><br>" +
					"<p>" +
					"<font color=\"red\">An error occured! CSRF Tokens did not match.</font>" +
					"<p>");
			}
		}
		else
		{
			out.print("<h2 class=\"title\">Class Create Failure</h2><br>" +
					"<p>" +
					"<font color=\"red\">An error occured! Please try non administrator functions!</font>" +
					"<p>");
		}
		log.debug("*** createClass END ***");
	}

}
