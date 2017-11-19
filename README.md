# SpringBoot Demo

- seeded with http://start.spring.io/
- OOB Tomcat replaced with Jetty

Based on https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/

### NOTE

In this demo, one resources is unprotected (`/users/sign-up`), and
all others are protected.

This means that the security filters need to be applied only to secured
endpoints.  Combining the entire spring security configuration in a single
HttpSecurity instance like this

```
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().authorizeRequests()
				.antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
				.anyRequest().authenticated()
				.and()
				.addFilter(new JWTAuthenticationFilter(authenticationManager(), applicationUserRepository))
				.addFilter(new JWTAuthorizationFilter(authenticationManager()));
	}
```


did not work as indicated

   here: https://stackoverflow.com/questions/36795894/how-to-apply-spring-security-filter-only-on-secured-endpoints

and

   here: https://stackoverflow.com/questions/45833065/spring-security-antmatchers-permitall-doesnt-work

There are a couple of solutions to this.

1. as indicated in the 1st link above, you can apply restricted Spring Security
to a given ant match pattern like this:

```
http.antMatcher("/api/**").authorizeRequests() //
        .anyRequest().authenticated() //
        .and()
        .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
```

Essentially, the above configuration will be invoked on `/api/**`.
For all other requests, this configuration will have no effect.

2. Create Multiple HttpSecurity instances as described at the link below.

https://docs.spring.io/spring-security/site/docs/current/reference/html/jc.html#multiple-httpsecurity

That's what I ended up doing here.

### to package

    $ mvn clean package

### to run

    $ mvn spring-boot:run
or
    $ java -jar target/demo-0.0.1-SNAPSHOT.jar


### externalized YAML config file

    application.yml

Default spring-boot console log pattern:

```
%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(18971){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wEx
```


## Spring Annotations

`@RequestBody` maps the `HttpRequest` body to a transfer or domain object
enabling automatic deserialization of the input `HttpRequest` body
onto a Java object.


`@ResponseBody` tells a controller that the object returned is
automatically serialized into JSON and passed back into the
`HttpResponse` object.



### resources

All resources, except for /users/sign-up are protected.
Here are the steps to use this demo:

#### Step 1: sign up

```
curl -H "Content-Type: application/json" -X POST -d '{
    "username": "jsmith",
    "password": "secret"
}' http://localhost:8080/users/sign-up
```

#### Step 2: login

```
curl -i -H "Content-Type: application/json" -X POST -d '{
    "username": "jsmith",
    "password": "secret"
}' http://localhost:8080/login
```

Obtain `Authorization` header content, e.g.:

```
Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqc21pdGgiLCJleHAiOjE1MTE5MzQ4NjR9.LGvNMqkAwaOwz6kyBp7M5quFYQOXvKozXXmv5GOtYdEeAPOW5qxaeXyIXrc96wgtr_QBVYIW1YQ_gZeWqr-Mtg
```


#### get `hello-world`

```
$ curl \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqc21pdGgiLCJleHAiOjE1MTE5MzQ4NjR9.LGvNMqkAwaOwz6kyBp7M5quFYQOXvKozXXmv5GOtYdEeAPOW5qxaeXyIXrc96wgtr_QBVYIW1YQ_gZeWqr-Mtg" \
    -X GET http://localhost:8080
```

#### post `/tasks`

```
$ curl \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqc21pdGgiLCJleHAiOjE1MTE5MzQ4NjR9.LGvNMqkAwaOwz6kyBp7M5quFYQOXvKozXXmv5GOtYdEeAPOW5qxaeXyIXrc96wgtr_QBVYIW1YQ_gZeWqr-Mtg" \
    -X POST -d '{
    "description": "Buy some milk(shake)"
}'  http://localhost:8080/tasks
```


#### get `/tasks`

```
$ curl \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqc21pdGgiLCJleHAiOjE1MTE5MzQ4NjR9.LGvNMqkAwaOwz6kyBp7M5quFYQOXvKozXXmv5GOtYdEeAPOW5qxaeXyIXrc96wgtr_QBVYIW1YQ_gZeWqr-Mtg" \
    -X GET http://localhost:8080/tasks
```

response:
```
[
    {
        "id": 1,
        "description": "Buy some milk(shake)"
    }
]
```

#### put `/tasks/#`

```
$ curl \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqc21pdGgiLCJleHAiOjE1MTE5MzQ4NjR9.LGvNMqkAwaOwz6kyBp7M5quFYQOXvKozXXmv5GOtYdEeAPOW5qxaeXyIXrc96wgtr_QBVYIW1YQ_gZeWqr-Mtg" \
    -X PUT -d '{
    "description": "Buy some milk"
}'  http://localhost:8080/tasks/1
```

#### delete `/tasks/#

```
$ curl \
    -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqc21pdGgiLCJleHAiOjE1MTE5MzQ4NjR9.LGvNMqkAwaOwz6kyBp7M5quFYQOXvKozXXmv5GOtYdEeAPOW5qxaeXyIXrc96wgtr_QBVYIW1YQ_gZeWqr-Mtg" \
    -X DELETE http://localhost:8080/tasks/1
```


## Enable user registration

Allow new users to self-register.

`ApplicationUser` entity class contains 3 properties:

* id
* username
* password

`ApplicationUserRepository` extends JpaRepository which
provides access to common methods like `save`, and
defines a new method `findByUsername`.

`UserController` exposes the user self-registration endpoint.

`/users`


#### post `/users/sign-up`

`UserController` uses `BCryptPasswordEncoder` to encrypt
user password before storing it in DB.

`BCryptPasswordEncoder` is instantiated in the
Application class as Singleton using `@Bean` annotation.


## Authn and Authz

To support authn and authz:

* implement an authn filter to issue JWT to users sending
credentials
* implement an authz filter to validate requests
containing JWTs
* create a custom implementation of `UserDetailsService`
to help Spring Security loading user-specific data in the framework
* extend the `WebSecurityConfigurerAdapter` class to
customize the security framework to our needs.

### JWTAuthenticationFilter

`JwtAuthenticationFilter` extends Spring's `UsernamePasswordAuthenticationFilter`
When adding a new filter to Spring Security, there are a couple of options:
1. explicitly define where in the `filter chain` we want this filter, or
2. extend a Spring provided filter so that Spring can figure out
itself where to insert this filter.

This implementation overrides two methods of the base class:

`attemptAuthentication`: where user credentials are parsed and handed
over to `AuthenticationManager` to `authenticate`.

`successfulAuthentication`: which is called when a user successfully
logs in.  This method generates a JWT for this user.

### Authorization Filter

`JwtAuthorizationFilter` extends Spring's `BasicAuthenticationFilter`
to make Spring replace it in the `filter chain` with this
custom implementation.

`doFilterInterval` method ensures that `Authorization` header is present.
if it is not, `401` is returned.
If the `Authorization` header is present, `getAuthentication`
method performs JWT validation.

`getAuthentication` method reads the JWT from the `Authorization` header,
and then uses the `Jwts` library to parse and validate the JWT token.
Once validation is complete, `UserPasswordAuthenticationToken` is
set on the `SecurityContext` allowing request to move forward.

### Integrate Security Filters

To configure the filters onto the Spring Security filter chain,
create `WebSecurity` class extending `WebSecurityConfigurerAdapter`
and decorate with two annotations:

`@Configuration`: Spring's new Java-configuration support.
These classes consist mainly of @Bean-annotated methods that
define instantiation, configuration, and initialization logic
for objects that are managed by the Spring IoC container.
Indicates that this class is a source of bean definitions.

`@EnableWebSecurity`: add this annotation to a `@Configuration` class
to have the Spring Security configuration defined in any
`WebSecurityConfigurer` or more likely by extending
the `WebSecurityConfigurerAdapter` base class and
overriding individual methods:

- `configure(HttpSecurity http): a method were we can define
which resources are public and which are secured.
In this case, the `SIGN_UP_URL` endpoint is public, and
everything else is protected.  Also configure `CORS` (Cross-Origin Resources Sharing)
support thru `http.cors()` and add a custom security filter
in the Spring Security filter chain:

```
    @Configuration
    @Order(1)
    public static class UserSelfServiceWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher(SIGN_UP_URL)
                    .authorizeRequests().anyRequest().permitAll()
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .cors().and().csrf().disable();


        }
    }

    @Configuration
    @Order(2)
    public static class ProtectedApiWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                    .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                    // this disables session creation on Spring Security
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }
```

- `configure(AuthenticationManagerBuilder auth)` a method where
we defined a custom implementation of `UserDetailsService` to
load user-specific data in the security framework.  Also, sets
the encrypt method used in this application (`BCryptPasswordEncoder`).

```
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
```

- `corsConfigurationSource()`: a method where we can allow/restrict
CORS support.  Here, it is left wide open by permitting requests
from any source `/**`.

```
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
```

### `UserDetailsService`

`UserDetailsService` is an interface that is used to define
concrete implementation of how to retrieve user-related data.

http://www.logicbig.com/tutorials/spring-framework/spring-security/user-details-service/

`UserDetailsService` is not an alternative to `AuthenticationProvider`
but it is used for a different purpose - to load user details.
Typically, an AuthenticationProvider implementation can use
`UserDetailsService` instance to retrieve user details during
its authn process.  E.g. `DaoAuthenticationProvider`, in case of
JDBC-Authn, uses `JdbcuserDetailsManager` as an implementation of
`UserDetailsService`.  In case of in-mem authn, DaoAuthenticationProvider uses
InMemoryUserDetailManager implementation.


This application uses a custom class `UserDetailsServiceImpl`
which implements a single method

```
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
		if (applicationUser == null) {
			throw new UsernameNotFoundException(username);
		}
		return new User(applicationUser.getUsername(), applicationUser.getPassword(), emptyList());
	}
```

When a user tries to authenticate, this method receives the
username, searches the DB for a record containing it, and
if found, returns an instance of `User`.

The properties of this instance (`username` and `password`) are
then checked against the credentials passed by the user in the
login request.  This last step is done outside this class -
by the Spring Security.







