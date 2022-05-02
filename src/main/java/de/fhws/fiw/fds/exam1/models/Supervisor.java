package de.fhws.fiw.fds.exam1.models;

public class Supervisor extends Person
{
	private String title;
	private String email;

	public Supervisor()
	{

	}

	public Supervisor(String firstname, String lastname, String title, String email)
	{
		super(firstname, lastname);
		this.title = title;
		this.email = email;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	@Override public String toString()
	{
		return "Supervisor{" + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
			+ ", title='" + title + '\'' + ", email='" + email + '\'' + '}';
	}
}
