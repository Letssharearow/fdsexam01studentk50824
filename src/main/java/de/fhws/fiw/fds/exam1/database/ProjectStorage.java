package de.fhws.fiw.fds.exam1.database;

import de.fhws.fiw.fds.exam1.models.Person;
import de.fhws.fiw.fds.exam1.models.Project;
import de.fhws.fiw.fds.exam1.models.Student;
import de.fhws.fiw.fds.exam1.models.Supervisor;
import org.apache.commons.lang.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProjectStorage
{
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
		this.storage = new HashMap<>();
		this.key = new AtomicLong(1l);
		create(new Project("template", "we try to create an API for projects",
			(Arrays.asList(new Student("Julian", "Sehne", "BIN", 4))),
			(Arrays.asList(new Supervisor("Peter", "Braun", "Prof.", "peter.braun@fhws.de"))), "2022ss",
			"programming project"));
	}

	public void create(final Project project)
	{
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

	public void update(final Project project)
	{
		this.storage.put(project.getId(), project);
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
	{
		return this.storage.values().stream().filter(byName(name)).filter(byType(type)).filter(bySemester(semester))
			.collect(Collectors.toList());
	}

	private Predicate<Project> byName(final String name)
	{
		return project -> StringUtils.isEmpty(name) || project.getName().equals(name);
	}

	private Predicate<Project> byType(final String type)
	{
		return project -> StringUtils.isEmpty(type) || project.getType().equals(type);
	}

	private Predicate<Project> bySemester(final String semester)
	{
		return project -> StringUtils.isEmpty(semester) || project.getSemester().equals(semester);
	}
}
