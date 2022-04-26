package de.fhws.fiw.fds.exam1.client;

public class PersonView
{
	protected long id;
	protected String firstName;
	protected String lastName;

	public PersonView()
	{
	}

	public PersonView(final String firstname, final String lastname)
	{
		this.firstName = firstname;
		this.lastName = lastname;
	}

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	@Override public String toString()
	{
		return "Person{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '}';
	}
}
