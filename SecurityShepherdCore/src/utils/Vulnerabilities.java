package utils;

/**
 * Holds available vulnerabilities for the Challenge Builder
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
public class Vulnerabilities 
{
	public class Vulnerability
	{
		public boolean secureDatabaseCall;
		public boolean secureDisplay;
		public boolean outputError;
		public boolean outputErrorSecure;
		//Further things can be added here as the Framework is developed
		Vulnerability(boolean callDatabaseSecure, boolean noXssDisplay, boolean outputError, boolean noXssError)
		{
			secureDatabaseCall = callDatabaseSecure;
			secureDisplay = noXssDisplay;
			this.outputError = outputError;
			outputErrorSecure = noXssError;
		}
	}
	//Vulnerabilitys To be used by the Challenge Builder
	public Vulnerability sqlInjection = new Vulnerability(false, true, true, true);
	public Vulnerability blindSqlInjection = new Vulnerability(false, true, false, false);
	public Vulnerability crossSiteScripting = new Vulnerability(true, false, false, false);
	public Vulnerability reflectedCrossSiteScripting = new Vulnerability(true, true, true, false);
}
