Spring MVC and REST Annotations
-------------------------------

Under the hood of @SpringBootApplication, when the above code is executed, it adds the below annotation.

@Configuration tags the class as a source of bean definitions for the application context.

@EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.

Normally you would add @EnableWebMvc for a Spring MVC app (explicit declaration ), but Spring Boot adds it automatically when it sees spring-webmvc on the classpath. This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.

@ComponentScan tells Spring to look for other components, configurations, and services in the hello package, allowing it to find the controllers.

WebMvcConfigurationSupport
--------------------------
Spring Boot initializes WebMvcConfigurationSupport for you.
This is the main class providing the configuration behind the MVC Java config. 
It is typically imported by adding @EnableWebMvc to an application's @Configuration class. 
An alternative, more advanced, option is to extend directly from this class and override methods as necessary, 
remembering to add @Configuration to the subclass and @Bean to overridden @Bean methods. For more details, see the Java doc of @EnableWebMvc.

This class registers the following

1. Registers these HandlerMappings:

RequestMappingHandlerMapping - ordered at 0 for mapping requests to annotated controller methods.

HandlerMapping - ordered at 1 to map URL paths directly to view names.

BeanNameUrlHandlerMapping (See below)- ordered at 2 to map URL paths to controller bean names.
HandlerMapping - ordered at Integer.MAX_VALUE-1 to serve static resource requests.   
HandlerMapping - ordered at Integer.MAX_VALUE to forward requests to the default servlet.

BeanNameUrlHandlerMapping is the default handler mapping mechanism, which maps URL requests to the name of the beans.

<beans ...>
   <bean class="org.springframework.web.servlet.handler.BeanNameUrlHandlerMapping"/>
   <bean name="/welcome.htm" class="com.mkyong.common.controller.WelcomeController" />
   <bean name="/streetName.htm" class="com.mkyong.common.controller.StreetNameController" />
   <bean name="/process*.htm" class="com.mkyong.common.controller.ProcessController" />
</beans>
OR
	@Bean(name = "/example")
    public HttpRequestHandler httpRequestHandler () {
        return new MyHttpRequestHandler();
	}
	
2. Handler class extends AbstractController class and overrides "handleRequestInternal()"

2. Registers these HandlerAdapters:

RequestMappingHandlerAdapter - for processing requests with annotated controller methods.
HttpRequestHandlerAdapter - for processing requests with HttpRequestHandler.
SimpleControllerHandlerAdapter - for processing requests with interface-based Controllers. 

RequestMappingHandlerAdapter
----------------------------
It’s used with RequestMappingHandlerMapping class, which executes methods annotated with @RequestMapping.

The RequestMappingHandlerMapping is used to maintain the mapping of the request URI to the handler. Once the handler is obtained, the DispatcherServlet dispatches the request to the appropriate handler adapter, which then invokes the handlerMethod().

@Controller
public class RequestMappingHandler {
     
    @RequestMapping("/requestName")
    public ModelAndView getEmployeeName() {
        ModelAndView model = new ModelAndView("Greeting");        
        model.addObject("message", "Madhwal");        
        return model;  
    }  
}

@ComponentScan("com.baeldung.spring.controller")
@Configuration
@EnableWebMvc
public class ServletConfig implements WebMvcConfigurer {
    @Bean
    public InternalResourceViewResolver jspViewResolver() {
        InternalResourceViewResolver bean = new InternalResourceViewResolver();
        bean.setPrefix("/WEB-INF/");
        bean.setSuffix(".jsp");
        return bean;
    }
}

(OR)

<beans ...>
    <mvc:annotation-driven />	->This tag will automatically register these two classes with Spring MVC.
     
    <context:component-scan base-package="com.baeldung.spring.controller" />
     
    <bean id="viewResolver"
      class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/" />
        <property name="suffix" value=".jsp" />
    </bean>
</beans>

HttpRequestHandlerAdapter
-------------------------
This handler adapter is used for the handlers that process HttpRequests. It implements the HttpRequestHandler interface, which contains a single handleRequest() method for processing the request and generating the response.

The return type of this method is void and it doesn’t generate ModelAndView return type as produced by other handler adapters. It’s basically used to generate binary responses and it doesn’t generate a view to render.  


public class MyHttpRequestHandler implements HttpRequestHandler {
    
    @Override
    public void handleRequest (HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        PrintWriter writer = response.getWriter();
        writer.write("response from MyHttpRequestHandler, uri: "+request.getRequestURI());
    }
}

SimpleControllerHandlerAdapter
------------------------------
This is the default handler adapter registered by Spring MVC. It deals with classes implementing Controller interface 
	and is used to forward a request to a controller object.
If a web application uses only controllers then we don’t need to configure any HandlerAdapter as the framework uses this 
	class as the default adapter for handling a request.
Let’s define a simple controller class, using the older style of controller (implementing the Controller interface):

public class SimpleController implements Controller {
    @Override
    public ModelAndView handleRequest(
      HttpServletRequest request, 
      HttpServletResponse response) throws Exception {
         
        ModelAndView model = new ModelAndView("Greeting");
        model.addObject("message", "Dinesh Madhwal");
        return model;
    }
}

SimpleServletHandlerAdapter
---------------------------
This handler adapter allows the use of any Servlet to work with DispatcherServlet for handling the request. 
	It forwards the request from DispatcherServlet to the appropriate Servlet class by calling its service() method.

The beans which implement the Servlet interface are automatically handled by this adapter. 
It is not registered by default and we need to register it like any other normal bean in the configuration file of DispatcherServlet:

<bean name="simpleServletHandlerAdapter"
  class="org.springframework.web.servlet.handler.SimpleServletHandlerAdapter" />
  
---------------------------------------

3.Registers a HandlerExceptionResolverComposite with this chain of exception resolvers:

ExceptionHandlerExceptionResolver - for handling exceptions through @ExceptionHandler methods.

ResponseStatusExceptionResolver - for exceptions annotated with @ResponseStatus.

DefaultHandlerExceptionResolver - for resolving known Spring exception types.

---------------------------------------

4.Registers an AntPathMatcher and a UrlPathHelper to be used by:

The RequestMappingHandlerMapping.

The HandlerMapping for ViewControllers.

The HandlerMapping for serving resources.
_______________________________________________________________________________________________________________________

@Controller
This annotation is used on Java classes that play the role of controller in your application. 
	The @Controller annotation allows autodetection of component classes in the classpath and auto-registering 
	bean definitions for them. To enable autodetection of such annotated controllers, 
	you can add component scanning to your configuration. 
	The Java class annotated with @Controller is capable of handling multiple request mappings.

This annotation can be used with Spring MVC and Spring WebFlux.

@RequestMapping
This annotation is used at both the class and method level. 
	The @RequestMapping annotation is used to map web requests onto specific handler classes and handler methods. 
	When @RequestMapping is used on the class level, it creates a base URI for which the controller will be used. 
		When this annotation is used on methods, it will give you the URI on which the handler methods will be executed. 
		From this, you can infer that the class level request mapping will remain the same whereas each handler method 
			will have their own request mapping.

Sometimes you may want to perform different operations based on the HTTP method used, even though the request URI may remain the same. 
			In such situations, you can use the method attribute of @RequestMapping with an HTTP method value to 
			narrow down the HTTP methods in order to invoke the methods of your class.

Here is a basic example of how a controller along with request mappings work:

@Controller
@RequestMapping("/welcome")
public class WelcomeController {
    @RequestMapping(method = RequestMethod.GET)
    public String welcomeAll() {
        return "welcome all";
    }
}


In this example, only GET requests to /welcome is handled by the welcomeAll() method.

This annotation also can be used with Spring MVC and Spring WebFlux.

The @RequestMapping annotation is very versatile. Please see my in-depth post on Request Mapping here.

@CookieValue
This annotation is used at method parameter level. @CookieValue is used as an argument of a request mapping method. 
	The HTTP cookie is bound to the @CookieValue parameter for a given cookie name. 
		This annotation is used in the method annotated with @RequestMapping.
Let us consider that the following cookie value is received with an HTTP request:

JSESSIONID=418AB76CD83EF94U85YD34W

To get the value of the cookie, use @CookieValue like this:

@ReuestMapping("/cookieValue")
    public void getCookieValue(@CookieValue "JSESSIONID" String cookie){
}


@CrossOrigin
This annotation is used both at the class and method levels to enable cross-origin requests. 
	In many cases, the host that serves JavaScript will be different from the host that serves the data. In such a case, 
	Cross Origin Resource Sharing (CORS) enables cross-domain communication. To enable this communication, 
	you just need to add the @CrossOrigin annotation.

By default, the @CrossOrigin annotation allows all origin, all headers, the HTTP methods specified in the@RequestMapping annotation, 
and a maxAge of 30 min. You can customize the behavior by specifying the corresponding attribute values.

An example of using @CrossOrigin at both the controller and handler method levels is below:

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/account")
public class AccountController {
    @CrossOrigin(origins = "http://example.com")
    @RequestMapping("/message")
    public Message getMessage() {
        // ...
    }
    @RequestMapping("/note")
    public Note getNote() {
        // ...
    }
}


In this example, both the getExample() and getNote() methods will have a maxAge of 3600 seconds. 
	Also, getExample() will only allow cross-origin requests from http://example.com, while getNote() will allow 
	cross-origin requests from all hosts.

Composed @RequestMapping Variants
Spring framework 4.3 introduced the following method-level variants of @RequestMapping annotation to better express 
the semantics of the annotated methods. Using these annotations has become the standard ays of defining the endpoints. 
	They act as wrappers to @RequestMapping.

These annotations can be used with Spring MVC and Spring WebFlux.

@GetMapping
This annotation is used for mapping HTTP GET requests onto specific handler methods. 
	@GetMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.GET).

@PostMapping
This annotation is used for mapping HTTP POST requests onto specific handler methods. 
	@PostMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.POST).

@PutMapping
This annotation is used for mapping HTTP PUT requests onto specific handler methods.
	@PutMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.PUT).

@PatchMapping
This annotation is used for mapping HTTP PATCH requests onto specific handler methods. 
	@PatchMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.PATCH).

@DeleteMapping
This annotation is used for mapping HTTP DELETE requests onto specific handler methods.
	@DeleteMapping is a composed annotation that acts as a shortcut for @RequestMapping(method = RequestMethod.DELETE).

@ExceptionHandler
This annotation is used at method levels to handle exceptions at the controller level. 
	The @ExceptionHandler annotation is used to define the class of exception it will catch. 
		You can use this annotation on methods that should be invoked to handle an exception. 
		The @ExceptionHandler values can be set to an array of Exception types. 
		If an exception is thrown that matches one of the types in the list, 
		then the method annotated with the matching @ExceptionHandler will be invoked.

@InitBinder
This annotation is a method-level annotation that plays the role of identifying the methods that initialize theWebDataBinder — 
a DataBinder that binds the request parameter to JavaBean objects. To customize request parameter data binding, 
you can use @InitBinder annotated methods within our controller. 
	The methods annotated with @InitBinder includes all argument types that handler methods support.

The @InitBinder annotated methods will get called for each HTTP request if you don’t specify the value element of this annotation. 
	The value element can be a single or multiple form names or request parameters that the init binder method is applied to.

@Mappings and @Mapping
This annotation is used on fields. The @Mapping annotation is a meta-annotation that indicates a web mapping annotation. 
	When mapping different field names, you need to configure the source field to its target field, and to do that, 
	you have to add the @Mappings annotation. This annotation accepts an array of @Mapping having the source and the target fields.

@MatrixVariable
This annotation is used to annotate request handler method arguments so that Spring can inject the relevant bits of a matrix URI. 
	Matrix variables can appear on any segment each separated by a semicolon. If a URL contains matrix variables, 
	the request mapping pattern must represent them with a URI template. 
		The @MatrixVariable annotation ensures that the request is matched with the correct matrix variables of the URI.

@PathVariable
This annotation is used to annotate request handler method arguments. 
	The @RequestMapping annotation can be used to handle dynamic changes in the URI where a certain URI value acts as a parameter. 
	You can specify this parameter using a regular expression. The @PathVariable annotation can be used declare this parameter.

@RequestAttribute
This annotation is used to bind the request attribute to a handler method parameter. 
	Spring retrieves the named attribute's value to populate the parameter annotated with @RequestAttribute. 
	While the @RequestParamannotation is used bind the parameter values from a query string, 
	@RequestAttribute is used to access the objects that have been populated on the server side.

@RequestBody
This annotation is used to annotate request handler method arguments. 
	The @RequestBody annotation indicates that a method parameter should be bound to the value of the HTTP request body. 
	The HttpMessageConveter is responsible for converting from the HTTP request message to object.

@RequestHeader
This annotation is used to annotate request handler method arguments. 
	The @RequestHeader annotation is used to map controller parameter to request header value. 
	When Spring maps the request, @RequestHeader checks the header with the name specified within the annotation 
	and binds its value to the handler method parameter. 
	This annotation helps you to get the header details within the controller class.

@RequestParam
This annotation is used to annotate request handler method arguments. Sometimes you get the parameters in the request URL, 
mostly in GET requests. In that case, along with the @RequestMapping annotation, you can use the @RequestParam annotation 
to retrieve the URL parameter and map it to the method argument. 
	The @RequestParam annotation is used to bind request parameters to a method parameter in your controller.

@RequestPart
This annotation is used to annotate request handler method arguments. The @RequestPart annotation can be used instead of 
@RequestParam to get the content of a specific multipart and bind it to the method argument annotated with @RequestPart. 
	This annotation takes into consideration the “Content-Type” header in the multipart (request part).

@ResponseBody
This annotation is used to annotate request handler methods. The @ResponseBody annotation is similar to the@RequestBody annotation. 
	The @ResponseBody annotation indicates that the result type should be written straight in the response body in whatever 
	format you specify like JSON or XML. Spring converts the returned object into a response body by using the HttpMessageConveter.

@ResponseStatus
This annotation is used on methods and exception classes. @ResponseStatus marks a method or exception class with a status code 
	and a reason that must be returned. When the handler method is invoked the status code is set to the HTTP response 
	which overrides the status information provided by any other means. A controller class can also be annotated with
		@ResponseStatus, which is then inherited by all @RequestMapping methods.

@ControllerAdvice
This annotation is applied at the class level. As explained earlier, for each controller, you can use @ExceptionHandler on a 
method that will be called when a given exception occurs. But this handles only those exceptions that occur within the controller 
in which it is defined. To overcome this problem, you can now use the@ControllerAdvice annotation. 
	This annotation is used to define @ExceptionHandler, @InitBinder, and @ModelAttribute methods that apply to all 
	@RequestMapping methods. Thus, if you define the @ExceptionHandler annotation on a method in a @ControllerAdvice class, 
	it will be applied to all the controllers.

@ModelAttribute	can be applied on Parameter, Method	
When applied to a method, used to preload the model with the value returned from the method. When applied to a parameter, 
binds a model attribute to the parameter. table
Ex:
@ModelAttribute("pirate") //->creates the model with name pirate
public Pirate setupPirate() {
Pirate pirate = new Pirate();
return pirate;
}
@RequestMapping(method = RequestMethod.POST)
protected String addPirate(@ModelAttribute("pirate") Pirate pirate) {	//-> binds the pirate here
pirateService.addPirate(pirate);
return "pirateAdded";
}

@RestController
This annotation is used at the class level. The @RestController annotation marks the class as a controller where every method 
	returns a domain object instead of a view. By annotating a class with this annotation, you no longer need to add 
	@ResponseBody to all the RequestMapping methods. It means that you no long use view-resolvers or send HTML in response. 
		You just send the domain object as an HTTP response in the format that is understood by the consumers, like JSON
@RestController is a convenience annotation that combines @Controller and @ResponseBody.

@RestControllerAdvice
This annotation is applied to Java classes. @RestControllerAdvice is a convenience annotation that combines 
@ControllerAdvice and @ResponseBody. This annotation is used along with the @ExceptionHandler annotation to handle exceptions 
that occur within the controller.

@SessionAttribute
This annotation is used at method parameter level. The @SessionAttribute annotation is used to bind the method parameter
to a session attribute. This annotation provides a convenient access to the existing or permanent session attributes.

@SessionAttributes
This annotation is applied at the type level for a specific handler. The @SessionAtrributes annotation is used when you want 
to add a JavaBean object into a session. This is used when you want to keep the object in session for short lived. 
	@SessionAttributes is used in conjunction with @ModelAttribute.

Consider this example:

@ModelAttribute("person")
public Person getPerson() {}
// within the same controller as above snippet
@Controller
@SeesionAttributes(value = "person", types = {
    Person.class
})
public class PersonController {}


The @ModelAttribute name is assigned to the @SessionAttributes as a value. The @SessionAttributes has two elements. The value element is the name of the session in the model and the types element is the type of session attributes in the model.
