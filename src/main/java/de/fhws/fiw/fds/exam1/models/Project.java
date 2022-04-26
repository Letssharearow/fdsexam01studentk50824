package de.fhws.fiw.fds.exam1.models;

import java.util.Collection;

public class Project
{
	private long id;
	private String name;
	private String description;
	private Collection<Student> students;
	private Collection<Supervisor> supervisors;
	private String semester;
	private String type;

	public Project()
	{

	}

	public Project(String name, String description, Collection<Student> students, Collection<Supervisor> supervisors,
		String semester, String type)
	{
		this.name = name;
		this.description = description;
		this.students = students;
		this.supervisors = supervisors;
		this.semester = semester;
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Collection<Student> getStudents()
	{
		return students;
	}

	public void setStudents(Collection<Student> students)
	{
		this.students = students;
	}

	public Collection<Supervisor> getSupervisors()
	{
		return supervisors;
	}

	public void setSupervisors(Collection<Supervisor> supervisors)
	{
		this.supervisors = supervisors;
	}

	public String getSemester()
	{
		return semester;
	}

	public void setSemester(String semester)
	{
		this.semester = semester;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	@Override public String toString()
	{
		return "Project{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", students=" + students
			+ ", supervisors=" + supervisors + ", semester='" + semester + '\'' + ", type='" + type + '\'' + '}';
	}

	public void setId(long newKey)
	{
		this.id = newKey;
	}

	public long getId()
	{
		return this.id;
	}
}
