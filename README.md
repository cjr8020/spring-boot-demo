# SpringBoot Demo

seeded with http://start.spring.io/

OOB Tomcat replaced with Jetty

`/users` resources returns JSON-encoded data, therefore
the corresponding `RequestMapping` method needs `@ResponseBody`
annotation.

`@RequestBody` maps the `HttpRequest` body to a transfer or domain object
enabling automatic deserialization of the input `HttpRequest` body
onto a Java object.


`@ResponseBody` tells a controller that the object returned is
automatically serialized into JSON and passed back into the
`HttpResponse` object.

### to package

    $ mvn clean package

### to run

    $ mvn spring-boot:run
or
    $ java -jar target/demo-0.0.1-SNAPSHOT.jar


### external YAML property file

    application.yml

Default spring-boot console log pattern:

```
%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(18971){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wEx
```


### resources

#### get `hello-world`

```
$ curl \
    -H "Content-Type: application/json" \
    -X GET http://localhost:8080
```

#### get `/users

```
$ curl \
    -H "Content-Type: application/json" \
    -X GET http://localhost:8080/users
```

response:
```
{
    "users": [
        {
            "firstname": "Richard",
            "lastname": "Feynman"
        },
        {
            "firstname": "Marie",
            "lastname": "Curie"
        }
    ]
}
```

#### post `/tasks`

```
$ curl -H "Content-Type: application/json" -X POST -d '{
    "description": "Buy some milk(shake)"
}'  http://localhost:8080/tasks
```


#### get `/tasks`

```
$ curl \
    -H "Content-Type: application/json" \
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
$ curl -H "Content-Type: application/json" -X PUT -d '{
    "description": "Buy some milk"
}'  http://localhost:8080/tasks/1
```

#### delete `/tasks/#

```
$ curl -X DELETE http://localhost:8080/tasks/1
```

