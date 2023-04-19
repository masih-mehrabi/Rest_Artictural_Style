package de.tum.in.ase.pse.rest;

import de.tum.in.ase.pse.model.Person;
import de.tum.in.ase.pse.service.PersonService;
import de.tum.in.ase.pse.util.PersonSortingOptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseExtractor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
public class PersonResource {

	private final PersonService personService;
//	private PersonSortingOptions sortingOptions;

	public PersonResource(PersonService personService) {
		this.personService = personService;
//		sortingOptions = new PersonSortingOptions();
	}

	@PostMapping("persons")
	public ResponseEntity<Person> createPerson(@RequestBody Person person) {
		if (person.getId() != null) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(personService.savePerson(person));
	}
	
	@PutMapping("persons/{personId}")
	public ResponseEntity<Person> updatePerson(@RequestBody Person updatePerson, @PathVariable("personId") UUID personId) {
		if (!updatePerson.getId().equals(personId)) {
			return ResponseEntity.badRequest().build();
		}
		return ResponseEntity.ok(personService.savePerson(updatePerson));
	}
	
	@DeleteMapping("persons/{personId}")
	public ResponseEntity<Void> deletePerson(@PathVariable("personId") UUID personId) {
		personService.deletePerson(personId);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("persons")
	public ResponseEntity<List<Person>> getAllPersons(@RequestParam (name = "sortField" ,  defaultValue = "ID") PersonSortingOptions.SortField  sortField,
	                                                  @RequestParam (name = "sortingOrder", defaultValue = "ASCENDING") PersonSortingOptions.SortingOrder sortingOrder) {
		
		PersonSortingOptions sortingOptions = new PersonSortingOptions(sortingOrder, sortField);
		
		return ResponseEntity.ok(personService.getAllPersons(sortingOptions));
		
	}
}
