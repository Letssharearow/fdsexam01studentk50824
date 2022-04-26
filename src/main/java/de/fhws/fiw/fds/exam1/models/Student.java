package de.fhws.fiw.fds.exam1.models;

public class Student extends Person
{
	private String course;
	private int semester;

	public Student()
	{

	}

	public Student(String firstname, String lastname, String course, int semester)
	{
		super(firstname, lastname);
		this.course = course;
		this.semester = semester;
	}

	public String getCourse()
	{
		return course;
	}

	public void setCourse(String course)
	{
		this.course = course;
	}

	public int getSemester()
	{
		return semester;
	}

	public void setSemester(int semester)
	{
		this.semester = semester;
	}

	@Override public String toString()
	{
		return "Student{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
			+ ", course='" + course + '\'' + ", semester=" + semester + '}';
	}
}
