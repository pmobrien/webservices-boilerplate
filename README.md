A simple project that contains just enough code to spin up an embedded Jetty web server on port 8080, with a base resource path of `/api`.

<br>

Build the project with:

```
mvn clean install
```

Run it with:

```
java -jar target/webservices.jar
```

To run on a port other than 8080:

```
java -Dport=<port> -jar target/webservices.jar
```
where `<port>` is your desired port number.

<br>

Contains one resource out of the box: `/hello-world`. This can be quickly tested by using any REST client ([Postman](https://www.getpostman.com/) works well) and sending a `GET` to `http://localhost:8080/api/hello-world`. You should receive back a plaintext response of `Hello World`.

Additional resources can be added by chaining on a `.register(...)` call to the `ResourceConfig` in the `Application` class.