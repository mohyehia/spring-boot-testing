# spring-boot-testing
Demo project for Spring Boot Testing using Junit & Mockito

## Overview
This project demonstrates how to write unit tests & integration tests for your spring boot application.

## Why You Should Use Constructor Injection in Spring
- First we need to know what is `dependency injection`:
    - **Dependency**: An object usually requires objects of other classes to perform its operations. We call these objects dependencies.
    - **Injection**: The process of providing the required dependencies to an object.
- In constructor-based injection, the dependencies required for the class are provided as arguments to the constructor.
- advantages of using constructor injection:
    - All Required Dependencies Are Available at Initialization Time.
        - The IoC container makes sure that all the arguments provided in the constructor are available before passing them into the constructor.
        - This helps in preventing the infamous `NullPointerException`.
    - Identifying Code Smells
        - Constructor injection helps us to identify if our bean is dependent on too many other objects.
        - If our constructor has a large number of arguments this may be a sign that our class has too many responsibilities.
        - We may want to think about refactoring our code to better address proper separation of concerns.
    - Preventing Errors in Tests
        - Constructor injection simplifies writing unit tests.
        - The constructor forces us to provide valid objects for all dependencies.
        - Using mocking libraries like Mockito, we can create mock objects that we can then pass into the constructor.
        - Constructor injection ensures that our test cases are executed only when all the dependencies are available.
        - It’s not possible to have half created objects in unit tests (or anywhere else for that matter).
    - Immutability
        - Constructor injection helps in creating immutable objects because a constructor’s signature is the only possible way to create objects.
        - Once we create a bean, we cannot alter its dependencies anymore.

## What is Mocks
- The basic concept of mocking is **replacing real objects with doubles**
- We can control how these doubles behave. These doubles we call test doubles.

## Different Types of Test Doubles
| Type     | Description                                                                                                                                                                                                                 |
|----------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Stub** | A stub is an object that always returns the same value, regardless of which parameters you provide on a stub’s methods.                                                                                                     |
| **Mock** | A mock is an object whose behavior - in the form of parameters and return values - is declared before the test is run. (This is exactly what Mockito is made for!)                                                          |
| **Spy**  | spy is an object that logs each method call that is performed on it (including parameter values). It can be queried to create assertions to verify the behavior of the system under test. (Spies are supported by Mockito!) |

## Dependencies
- We will use JUnit Jupiter (JUnit 5), Mockito, and AssertJ.<br/>
- Luckily all these dependencies are automatically imported with the `spring-boot-starter-test` dependency like below:
```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
```

## Don’t Use Spring in Unit Tests
- Consider the following “unit” test that tests a single method of the RegisterUseCase class: 
```java
@SpringBootTest
class RegisterUseCaseTest {
    @Autowired
    private RegisterUseCase registerUseCase;
    
    @Test
    void savedUserHasRegistrationDate() {
        User user = new User("mohamed", "mohamed@mail.com");
        User savedUser = registerUseCase.registerUser(user);
        assertThat(savedUser.getRegistrationDate()).isNotNull();
    }
}
```
- This test takes 4 or 5 seconds to run on an empty spring boot project.
- But a good unit test only takes milliseconds. Otherwise, it hinders the “test / code / test” flow promoted by the idea of Test-Driven Development (TDD).
- But even when we’re not practicing TDD, waiting on a test that takes too long ruins our concentration.
- The execution of the above method only took milliseconds. The rest of the 5 seconds is due to the `@SpringBootTest` telling Spring Boot to set up a whole Spring Boot application context.
- So we have started the whole application only to autowire a `RegisterUseCase` instance into our test.
- It will take even longer once the application gets bigger and Spring has to load more and more beans into the application context.

## Creating a Testable Spring Bean
- Field Injection is Evil. So, don't use field injection.
- Use **constructor injection** instead of using field injection.
- Reduce Boilerplate Code by using `lombok` annotations.
- Here is a sample of a class that can be easily unit tested:
```java
@Service
@RequiredArgsConstructor
public class RegisterService {
  private final UserRepository userRepository;

  public User registerUser(User user) {
    user.setRegistrationDate(LocalDateTime.now());
    return userRepository.save(user);
  }
}
```

## Using Mockito to mock dependencies for unit testing
- The de-facto standard mocking library nowadays is `Mockito`.
- It provides at least two ways to create a mocked `UserRepository` to fill the blank in the previous code example.

## Mocking Dependencies with plain `Mockito`
- The first way is to use `Mockito` programmatically:
- `private UserRepository userRepository = Mockito.mock(UserRepository.class);`
- This will create an object that looks like a `UserRepository` from the outside.
- By default, it will do nothing when a method is called and return null if the method has a return value.
- we have to tell `Mockito` to return something when `userRepository.save()` is called.
- We can do this with the static `when` or `given` method:
```java
class RegisterServiceTes{
    @Test
    void savedUserHasRegistrationDate() {
        User user = new User("mohamed", "mohamed@mail.com");
        given(userRepository.save(any(User.class))).willReturn(returnsFirstArg());
        User savedUser = registerService.registerUser(user);
        assertThat(savedUser.getRegistrationDate()).isNotNull();
    }
}
```
- This will make `userRepository.save()` return the same user object that is passed into the method.

## Mocking Dependencies with Mockito’s `@Mock` Annotation
- An alternative way of creating mock objects is Mockito’s `@Mock` annotation in combination with the `MockitoExtension` for JUnit Jupiter:
- Consider the below sample:
```java
@ExtendWith(MockitoExtension.class)
class RegisterServiceTest{
    @Mock
    private UserRepository userRepository;
    
    @Test
    void savedUserHasRegistrationDate() {
        // ...
    }
} 
```
- The `@Mock` annotation specifies the fields in which `Mockito` should inject mock objects.
- The `@MockitoExtension` tells `Mockito` to evaluate those `@Mock` annotations because JUnit does not do this automatically.
- We can use ` @InjectMocks` annotation on the `RegisterService` field. Mockito will then create an instance for us.
```java
@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private RegisterUseCase registerUseCase;

  @Test
  void savedUserHasRegistrationDate() {
    // ...
  }
}
```

## Creating Readable Assertions with AssertJ
- Another library that comes automatically with the Spring Boot test support is `AssertJ`.
- We have used it already above to implement our assertion: `assertThat(savedUser.getRegistrationDate()).isNotNull();`
- However, wouldn't it be nice to make the assertion even more readable? Like this, for example:
- `assertThat(savedUser).hasRegistrationDate();`
- There are many cases where small changes like this make the test so much better to understand.
-  So, let’s create our own `custom assertion`:
```java
class UserAssert extends AbstractAssert<UserAssert, User> {
  UserAssert(User user) {
    super(user, UserAssert.class);
  }
  
  static UserAssert assertThat(User actual) {
    return new UserAssert(actual);
  }

  UserAssert hasRegistrationDate() {
    isNotNull();
    if (actual.getRegistrationDate() == null) {
      failWithMessage(
        "Expected user to have a registration date, but it was null"
      );
    }
    return this;
  }
}
```
- Now, if we import the `assertThat` method from the new `UserAssert` class instead from the AssertJ library, we can use the new, easier to read assertion.
- Creating a custom assertion like this may seem like a lot of work, but it’s actually done in a couple of minutes.
- We only write the test code once, after all, and others have to read, understand and then manipulate the code many, many times during the lifetime of the software.

## Mockito Best Practices
- **Don’t Share Mock behavior Between Tests**
    - It’s important to make methods with shared mock behavior very specific and name them properly to keep the test cases readable.
- **Write Self-Contained Test Cases**
    - The unit tests we write should be runnable on any machine with the same result.
    - They shouldn’t affect other test cases in any way.
    - So we must write every unit test self-contained and independent of test execution order.
- **Avoid `Mockito.reset()` for Better Unit Tests**
    - `Mockito` recommends in their documentation to prefer recreation of mocks over resetting them.
    - Smart Mockito users hardly use this feature because they know it could be a sign of poor tests.
    - Normally, you don’t need to reset your mocks, just create new mocks for each test method.
    - We better create simple and small test cases than lengthy and over-specified tests.
- **Don’t Mock Value Objects or Collections**
    - `Mockito` is a framework to mock objects with behavior that can be declared at the beginning of our test.
    - There is no value added to mock the list.
    - It’s even harder to understand what we expected from our list.
    - In comparison with a real `List` (i.e. `ArrayList`) things get clearer right away.
    - Using mocks for collections we might hide the natural behavior of a `List`.
    - In the worst case, our application fails in production because we assumed a `List` to behave differently from how it actually does!
    - **Mockito is a framework to mock behavior of components based on values and not to mock values.**
    - This means that we better create tests for components that process DTOs rather than for the DTOs themselves.
- **Testing Error Handling with Mockito**


## Mockito FAQ
- What types can I mock?
    - Mockito allows us to mock not only interfaces but also concrete classes.
- What is returned if I don’t declare a mock's behavior?
    - Mockito by default returns null for complex objects, and the default values for primitive data types (for example 0 for int and false for boolean).
- Can I mock `final` classes?
    - No, final classes can’t be mocked and neither can final methods.
    - Mockito also can’t mock constructors, static methods, `equals()` nor `hashCode()`.

## Testing MVC Web Controllers with Spring Boot

## Responsibilities of a Web Controller
| #   | Responsibility          | Description                                                                                                                                                                                      |
|-----|-------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1   | Listen to HTTP Requests | The controller should respond to certain URLs, HTTP methods and content types.                                                                                                                   |
| 2   | Deserialize Input       | The controller should parse the incoming HTTP request and create java objects from variables in the URL, HTTP request parameters and the request body so that we can work with them in the code. |
| 3   | Validate Input          | The controller is the first line of defense against bad input, so it's a place where we can validate the input.                                                                                  |
| 4   | Call the Business Logic | Having parsed the input, the controller must transform the input into the model expected by the business logic and pass it on to the business logic.                                             |
| 5   | Serialize the Output    | The controller takes the output of the business logic and serializes it into an HTTP response.                                                                                                   |
| 6   | Translate Exceptions    | If an exception occurs somewhere on the way, the controller should translate it into a meaningful error message and HTTP status for the user.                                                    |

**We should take care not to add even more responsibilities like performing business logic.**
Otherwise, our controller tests will become fat and unmaintainable.

## Unit or Integration Test?
- In a unit test, we would test the controller in isolation.
- That means we would instantiate a controller object, mocking away the business logic, and then call the controller’s methods and verify the response.
- But that will not work in our case. 
- Let’s check which of the 6 responsibilities we have identified above we can cover in an isolated unit test:

| #   | Responsibility          | Covered in a Unit Test?                                                                                                                                                                                   |
|-----|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 1   | Listen to HTTP Requests | **No,** because the unit test would not evaluate the `@PostMapping` annotation and similar annotations specifying the properties of a HTTP request.                                                       |
| 2   | Deserialize Input       | **No,** because annotations like @RequestParam and `@PathVariable` would not be evaluated. Instead we would provide the input as Java objects, effectively skipping deserialization from an HTTP request. |
| 3   | Validate Input          | **No** Not when depending on bean validation, because the `@Valid` annotation would not be evaluated.                                                                                                     |
| 4   | Call the Business Logic | **Yes**, because we can verify if the mocked business logic has been called with the expected arguments.                                                                                                  |
| 5   | Serialize the Output    | **No**, because we can only verify the Java version of the output, and not the HTTP response that would be generated.                                                                                     |
| 6   | Translate Exceptions    | **No** We could check if a certain exception was raised, but not that it was translated to a certain JSON response or HTTP status code.                                                                   |


- A simple unit test will not cover the HTTP layer.
- So, we need to introduce Spring to our test to do the HTTP magic for us.
- Thus, we’re building an integration test that tests the integration between our controller code and the components Spring provides for HTTP support.
- An integration test with Spring fires up a Spring application context that contains all the beans we need.
- This includes framework beans that are responsible for listening to certain URLs, serializing and deserializing to and from JSON and translating exceptions to HTTP.
- These beans will evaluate the annotations that would be ignored by a simple unit test.

## Creating Custom ResultMatchers
- Certain assertions are rather hard to write and, more importantly, hard to read.
- Especially when we want to compare the JSON string from the HTTP response to an expected value it takes a lot of code.
- Luckily, we can create custom `ResultMatchers` that we can use within the fluent API of `MockMvc`.

## Matching JSON Output
- Using the below code, we can match the json output:
```java
public class ResponseBodyMatchers{
    private ObjectMapper objectMapper = new ObjectMapper();
    public <T> ResultMatcher containsObjectAsJson(
            Object expectedObject,
            Class<T> targetClass) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            T actualObject = objectMapper.readValue(json, targetClass);
            assertThat(actualObject).isEqualToComparingFieldByField(expectedObject);
        };
    }
    static ResponseBodyMatchers responseBody(){
        return new ResponseBodyMatchers();
    }
}
```
- We can use the above code like below:
- `mockMvc.perform(...).....andExpect(responseBody().containsObjectAsJson(expected, urClass.class));`

## Matching Expected Validation Errors
- We can simplify our exception handling test by using only the below code:
- `mockMvc.perform(...).....andExpect(responseBody().containsError("name", "must not be null"));`
- To enable the above code, we should add the method `containsError()` to our `ResponseBodyMatchers` class from above:
```java
public class ResponseBodyMatchers{
    // the code from above is here
    public ResultMatcher containsError(String expectedFieldName, String expectedMessage) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            ErrorResult errorResult = objectMapper.readValue(json, ErrorResult.class);
            List<FieldValidationError> fieldErrors = errorResult.getFieldErrors().stream()
                    .filter(fieldError -> fieldError.getField().equals(expectedFieldName))
                    .filter(fieldError -> fieldError.getMessage().equals(expectedMessage))
                    .collect(Collectors.toList());

            assertThat(fieldErrors)
                    .hasSize(1)
                    .withFailMessage("expecting exactly 1 error message" + "with field name '%s' and message '%s'", expectedFieldName, expectedMessage);
        };
    }
}
```
- All the ugly code is hidden within this helper class, and we can happily write clean assertions in our integration tests.

## References
- [Why You Should Use Constructor Injection in Spring](https://reflectoring.io/constructor-injection/)
- [Unit Testing with Spring Boot](https://reflectoring.io/unit-testing-spring-boot/)
- [Clean Unit Tests with Mockito](https://reflectoring.io/clean-unit-tests-with-mockito/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Creating custom assertions using AssertJ](http://joel-costigliola.github.io/assertj/assertj-core-custom-assertions.html)
- [Testing MVC Web Controllers with Spring Boot](https://reflectoring.io/spring-boot-web-controller-test/)