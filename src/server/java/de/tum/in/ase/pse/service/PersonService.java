package de.tum.in.ase.pse.service;

import de.tum.in.ase.pse.model.Person;
import de.tum.in.ase.pse.util.PersonSortingOptions;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PersonService {
	// do not change this
	private final List<Person> persons;

	public PersonService() {
		this.persons = new ArrayList<>();
	}

	public Person savePerson(Person person) {
		var optionalPerson = persons.stream().filter(existingPerson -> existingPerson.getId().equals(person.getId())).findFirst();
		if (optionalPerson.isEmpty()) {
			person.setId(UUID.randomUUID());
			persons.add(person);
			return person;
		} else {
			var existingPerson = optionalPerson.get();
			existingPerson.setFirstName(person.getFirstName());
			existingPerson.setLastName(person.getLastName());
			existingPerson.setBirthday(person.getBirthday());
			return existingPerson;
		}
	}

	public void deletePerson(UUID personId) {
		this.persons.removeIf(person -> person.getId().equals(personId));
	}

	public List<Person> getAllPersons(PersonSortingOptions sortingOptions) {
		
		
		List<Person> sortedPersons = new ArrayList<>(this.persons);
		Comparator<Person> comparator;
		switch (sortingOptions.getSortField()) {
			case FIRST_NAME:
				comparator = Comparator.comparing(Person::getFirstName);
				break;
			case LAST_NAME:
				comparator = Comparator.comparing(Person::getLastName);
				break;
			case BIRTHDAY:
				comparator = Comparator.comparing(Person::getBirthday);
				break;
			default:
				comparator = Comparator.comparing(Person::getId);
				break;
		}
		if (sortingOptions.getSortingOrder() == PersonSortingOptions.SortingOrder.DESCENDING) {
			comparator = comparator.reversed();
		}
		Collections.sort(sortedPersons, comparator);
		return sortedPersons;
	}
}
