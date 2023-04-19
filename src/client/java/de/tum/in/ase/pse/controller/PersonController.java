package de.tum.in.ase.pse.controller;

import de.tum.in.ase.pse.model.Person;
import de.tum.in.ase.pse.util.PersonSortingOptions;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;
import org.springframework.core.ParameterizedTypeReference;


import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PersonController {
	
	private WebClient webClient;
	
	private List<Person> persons;
	public PersonController() {
		this.webClient = WebClient.builder()
				                 .baseUrl("http://localhost:8080/")
				                 .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				                 .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				                 .build();
		this.persons = new ArrayList<>();
	}

	public void addPerson(Person person, Consumer<List<Person>> personsConsumer) {
		// TODO Part 2: Make an http post request to the server
		
			webClient.post().uri("persons")
					.bodyValue(person)
					.retrieve()
					.bodyToMono(Person.class)
					.onErrorStop()
					.subscribe(newPerson -> {
						persons.add(newPerson);
						personsConsumer.accept(persons);
					});
	
		
	}

	public void updatePerson(Person person, Consumer<List<Person>> personsConsumer) {
		// TODO Part 2: Make an http put request to the server
		webClient.put()
				.uri("persons/" + person.getId())
				.bodyValue(person)
				.retrieve()
				.bodyToMono(Person.class)
				.onErrorStop()
				.subscribe(newPersons -> {
			persons.replaceAll(oldPersons -> oldPersons.getId().equals(newPersons.getId()) ? newPersons : oldPersons);
			personsConsumer.accept(persons);
			
		});
	}

	public void deletePerson(Person person, Consumer<List<Person>> personsConsumer) {
		// TODO Part 2: Make an http delete request to the server
		webClient.delete().uri("persons/" + person.getId()).retrieve().toBodilessEntity().onErrorStop().subscribe(v-> {
			persons.remove(person);
			personsConsumer.accept(persons);
		});
	}

	public void getAllPersons(PersonSortingOptions sortingOptions, Consumer<List<Person>> personsConsumer) {
		// TODO Part 2: Make an https get request to the server
		webClient.get()
				.uri(uriBuilder -> uriBuilder
						                   .path("persons")
						                   .queryParam("sortField", "sortField" )
						                   .queryParam("sortingOrder", "sortingOrder")
						                   .build())
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<Person>>() {
		})
				.onErrorStop()
				.subscribe(newPersons -> {
			persons.clear();
			persons.addAll(newPersons);
			personsConsumer.accept(persons);
		});
	}
}
