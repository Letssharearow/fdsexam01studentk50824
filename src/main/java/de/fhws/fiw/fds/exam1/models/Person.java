package de.fhws.fiw.fds.exam1.models;

public class Person
{
	protected String firstName;
	protected String lastName;

	public Person()
	{
	}

	public Person(final String firstname, final String lastname)
	{
		this.firstName = firstname;
		this.lastName = lastname;
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
		return "Person{" + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '}';
	}
}
