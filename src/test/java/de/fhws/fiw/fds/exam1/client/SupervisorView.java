package de.fhws.fiw.fds.exam1.client;

public class SupervisorView extends PersonView
{
	private String title;
	private String email;

	public SupervisorView()
	{

	}

	public SupervisorView(String firstname, String lastname, String title, String email)
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
		return "Supervisor{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
			+ ", title='" + title + '\'' + ", email='" + email + '\'' + '}';
	}
}
