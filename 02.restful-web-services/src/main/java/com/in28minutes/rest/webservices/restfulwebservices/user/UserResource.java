package com.in28minutes.rest.webservices.restfulwebservices.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

@RestController
public class UserResource {


     @Autowired
	private ResourceBundleMessageSource messageSource;
	 @Autowired
	 private LocaleResolver resolver;
	private final  UserDaoService service;

	@Autowired
	public UserResource( UserDaoService service) {
		this.service = service;
	}


	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return service.findAll();
	}

	@GetMapping("/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		User user = service.findOne(id);
		
		if(user==null)
			throw new UserNotFoundException("id-"+ id);
		
		
		//"all-users", SERVER_PATH + "/users"
		//retrieveAllUsers
		EntityModel<User> resource = EntityModel.of(user);
		
		WebMvcLinkBuilder linkTo = 
				linkTo(methodOn(this.getClass()).retrieveAllUsers());
		
		resource.add(linkTo.withRel("all-users"));
		
		//HATEOAS
		
		return resource;
	}

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		service.deleteById(id);
		
	}

	//
	// input - details of user
	// output - CREATED & Return the created URI
	
	//HATEOAS
	
	@PostMapping("/users")
	public ResponseEntity<? super  Object> createUser(@Valid @RequestBody User user) {

		User userFound = service.save(user);
		//ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(userFound.getId()).toUri();

		try{


			return ResponseEntity.created(new URI("http://localhiost:8080/users/"+userFound.getId())).body(userFound);
		}catch (Exception exception){
			return  ResponseEntity.internalServerError().body(exception.getLocalizedMessage());
		}




	}

	@GetMapping("/hello-world-internationalization")
	public String helloWorld(@RequestHeader(value = "Accept-language", required = false) Locale locale){
		return messageSource.getMessage("good.morning.message",null, locale);
	}

}
