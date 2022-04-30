package de.fhws.fiw.fds.exam1.database;

import de.fhws.fiw.fds.exam1.models.Project;
import de.fhws.fiw.fds.exam1.models.Student;
import de.fhws.fiw.fds.exam1.models.Supervisor;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ProjectStorage
{
	public static final String WRONG_FILTER_INPUT = "Wrong Filter Input";
	private static ProjectStorage INSTANCE;

	public static ProjectStorage getInstance()
	{
		if (INSTANCE == null)
		{
			INSTANCE = new ProjectStorage();
		}

		return INSTANCE;
	}

	private final Map<Long, Project> storage;

	private final AtomicLong key;

	private ProjectStorage()
	{
		this.key = new AtomicLong(1L);
		this.storage = new HashMap<>();
	}

	public void create(final Project project) throws IllegalArgumentException
	{
		verifyProject(project);
		final long newKey = this.key.getAndIncrement();
		this.storage.put(newKey, project);
		project.setId(newKey);
	}

	public Optional<Project> readById(final long key)
	{
		if (this.storage.containsKey(key))
		{
			return Optional.of(this.storage.get(key));
		}
		else
		{
			return Optional.empty();
		}
	}

	public void update(final Project project) throws IllegalArgumentException
	{
		verifyProject(project);
		this.storage.put(project.getId(), project);
	}

	private void verifyProject(Project project) throws IllegalArgumentException
	{
		if (!isValidSemester(project.getSemester()))
		{
			throw new IllegalArgumentException("invalid project semester");
		}
		if (!isValidStudents(project.getStudents()))
		{
			throw new IllegalArgumentException("invalid students");
		}
		if (!isValidSupervisors(project.getSupervisors()))
		{
			throw new IllegalArgumentException("invalid supervisors");
		}
	}

	public void delete(final Project project)
	{
		this.storage.remove(project.getId());
	}

	public boolean containsId(final long id)
	{
		return this.storage.containsKey(id);
	}

	public void deleteById(final long id)
	{
		this.storage.remove(id);
	}

	public Collection<Project> findAll()
	{
		return findBy("", "", "");
	}

	public Collection<Project> findBy(final String name, final String type, final String semester)
		throws IllegalArgumentException
	{
		if (!isValidSemester(semester))
		{
			System.out.println(isValidSemester(semester));
			System.out.println("throw new IllegalArgumentException");
			throw new IllegalArgumentException(WRONG_FILTER_INPUT);
		}
		return this.storage.values().stream()
			.filter(project -> (byName(project, name) && byType(project, type) && bySemester(project, semester)))
			.collect(Collectors.toList());
	}

	private boolean matchRegex(String stringToMatch, String regex)
	{
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(stringToMatch);
		return matcher.find();
	}

	private boolean isValidSemester(String semester)
	{
		return matchRegex(semester, "(^[0-9][0-9][0-9][0-9](w|s)s$|^$)");
	}

	private boolean isValidEmail(String semester)
	{
		return matchRegex(semester, "(^.+@.+\\..+$|^$)");
	}

	private boolean isValidSemesterNumber(int semesterNumber)
	{
		return semesterNumber <= 7 && semesterNumber >= 1;
	}

	private boolean isValidCourse(String semester)
	{
		return matchRegex(semester, "[A-Z][A-Z][A-Z]");
	}

	private boolean isValidSupervisors(Collection<Supervisor> supervisors)
	{
		return supervisors.stream().anyMatch(supervisor -> isValidEmail(supervisor.getEmail()));
	}

	private boolean isValidStudents(Collection<Student> students)
	{
		return students.stream()
			.anyMatch(student -> isValidSemesterNumber(student.getSemester()) && isValidCourse(student.getCourse()));
	}

	private boolean byName(final Project project, final String name)
	{
		return StringUtils.isEmpty(name) || project.getName().equals(name);
	}

	private boolean byType(final Project project, final String type)
	{
		return StringUtils.isEmpty(type) || project.getType().equals(type);
	}

	private boolean bySemester(final Project project, final String semester)
	{
		return StringUtils.isEmpty(semester) || project.getSemester().equals(semester);
	}

	public static void main(String[] args)
	{
		ProjectStorage projectStorage = new ProjectStorage();
		System.out.println(projectStorage.matchRegex("f@f1", "(^.+@.+\\..+$|^$)"));
	}
}
