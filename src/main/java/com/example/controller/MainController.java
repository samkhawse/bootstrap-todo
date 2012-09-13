package com.example.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.example.model.Person;
import com.example.model.Tag;
import com.example.model.Task;
import com.example.model.User;
import com.example.service.PersonService;
import com.example.service.TaskService;
import com.example.service.UserService;

@Controller
public class MainController {

	@Autowired
	private PersonService personService;

	@Autowired
	private UserService userService;

	@Autowired
	private TaskService taskService;
	
	@PersistenceContext
	EntityManager em;

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);

	private User sam = new User("sam", "secr3t");
	
	private Tag defaultTag = new Tag("Default");
	private Tag workTag = new Tag("Work");
	private Tag personalTag = new Tag("Personal");

/* 	
	@RequestMapping(value="/", method= RequestMethod.GET)
	public String handleDefault(ModelMap model, Principal principal){

		String name = principal.getName();
		if (null != name){
			logger.info("intercepted /. Sending user to /tasks/tasks.jsp");
			return "redirect:/tasks/";
		} else {
			logger.info("caught / Redirecting to login page");
			return "redirect:/login";
		}
		//return "redirect:/welcome";
	}
*/
	
	@RequestMapping(value ="/welcome", method = RequestMethod.GET)
	public String listPeople(ModelMap model, Principal principal) {

		if (null != principal){
			logger.info("intercepted /welcome. name is: "+principal.getName());	
			return "redirect:/tasks/";		
		} else {
			logger.info("weird no principal found:");
			return "redirect:/loginfailed";
		}
		
/*	heroku doesn't like cookies, it seems or maybe I tripped on some settings 		
		Cookie newCookie = null;
		Cookie[] myCookie = request.getCookies();
		boolean foundACookie = false;
		
		if (myCookie.length != 0){
			for (Cookie cookie : myCookie) {
				if (cookie.getName().equals("session"));{
					foundACookie = true;
					return "redirect:/tasks/";
				}
			}
		}
		logger.info("Cookie found:"+foundACookie);
		if (!foundACookie){
			newCookie = new Cookie("session", "Sam");
			newCookie.setMaxAge(24*60*60);
			response.addCookie(newCookie);
		}	

		// create a test user if new session 

		if (null == sam) {
			sam = new User("sam", "secr3t");
			userService.addUser(sam);
			map.put("user", sam);
			logger.info("added user "+sam.getUserName()+" to Datastore");			
		} else {
			logger.info("message: " + map.get("message"));
		}
		
		userService.addUser(sam);
		map.put("user", sam);
		logger.info("added user "+sam.getUserName()+" to Datastore");
		
		return "index";
*/		

	}

	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(ModelMap model){
		
		Query qry = em.createNativeQuery("SELECT USERNAME FROM USERS WHERE USER_ID = 001", String.class);
		for (Object item : qry.getResultList()) {
			logger.info("item: "+(String)item);
		}		
		
		logger.info("intercepted /login. Sending to index");
		return "index";
	}
	
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
 
		model.addAttribute("error", "true");
		return "index";
 
	}
	
	@RequestMapping(value="/logout", method= RequestMethod.GET)
	public String logout(ModelMap model){
		return "index";
	}
	
	// This will be replaced by Spring Security; delete on success 
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public String validate(@ModelAttribute("user") User user,
			BindingResult result, HttpServletRequest request) {
		logger.info("user recieved is: " + user.getUserName() + " with passwd: " + user.getPasswd());
		
		if (userService.validateUser(user)) {
			return "redirect:/tasks/";
		} else {
			logger.info("login failed");
			request.setAttribute("message", "Invalid login credentials");
			return "forward:/";
		}
	}

/* handle tasks here */
	
	@RequestMapping(value = "/tasks/add", method=RequestMethod.POST)
	public String addTask(@ModelAttribute("task") Task task ){
		
		logger.info("creating a task, with:"+task.getTaskName()+" "+task.getCompleteBy());
		
		task.setCreatedDate(Calendar.getInstance().getTime());
		task.setCompleted(false);
		if (null == task.getCompleteBy())
			task.setCompleteBy(Calendar.getInstance().getTime());
		
		// add some default tags
		List<Tag> tags = new ArrayList<Tag>();
		tags.add(defaultTag);
		tags.add(personalTag);
		tags.add(workTag);
		
		task.setTags(tags);
		
		task.setUser(sam);
		
		logger.info("User sam is :"+sam);
		
		taskService.createTask(task);		
		
		return "redirect:/tasks/";
	}
	
	@RequestMapping("/tasks/delete/{taskId}")
	public String deleteTask(@PathVariable("taskId") Integer taskId){
		logger.info("deleting task with id:"+taskId);
		taskService.deleteTask(taskId);
		return "redirect:/tasks/";
	}

	@RequestMapping("/tasks/")
	public String listTasks(Map<String, Object> map){
		logger.info("handling path /tasks/ ");
		
		map.put("task", new Task());
		map.put("taskList", taskService.listAll());
		
		for (Task task : taskService.listAll()) {
			for (Tag tag : task.getTags()) {
				logger.info("tag fetched: "+tag.getTagName());
			}
		}
		
		return "tasks";
	}

/* People methods, not used anymore */	

	
	@RequestMapping(value = "/people/add", method = RequestMethod.POST)
	public String addPerson(@ModelAttribute("person") Person person,
			BindingResult result) {

		personService.addPerson(person);

		logger.info("added person");
		return "redirect:/people/";
	}

	@RequestMapping("/people/delete/{personId}")
	public String deletePerson(@PathVariable("personId") Integer personId) {

		personService.removePerson(personId);
		logger.info("removed person");
		return "redirect:/people/";
	}

	@RequestMapping("/people/")
	public String handlePeople(Map<String, Object> map) {

		map.put("person", new Person());
		map.put("peopleList", personService.listPeople());

		logger.info("handled loading of /people/");
		return "people";
	}

}
