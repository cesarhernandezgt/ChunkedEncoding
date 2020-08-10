# ChunkedEncoding
Test project and wars
ContentTransferHelper.java is the client program to read the content from server response stream
TransferContent.java is the server side code which sends data in chunks.


## Minimum Prerequisites
* Java 8
* Maven 3.3.9

## Run the application using maven and arquillian test:
This will execute the `RestService.getTest` method.

```
mvn clean install
```


## Run the application with TomEE Maven Plugin and test locally

Run tomee:

```
mvn tomee:run
```

Sends an email using SMTP:

```
curl curl http://localhost:8080/chucked-encoding/api/test
```

The above command returns a demo string.
