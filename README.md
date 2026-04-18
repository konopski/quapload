# Quapload

Demo project showcasing file upload capabilities in Quarkus with Groovy.

## Technologies

- **Quarkus 3.22.3** - application framework
- **Groovy 4.0.24** - programming language
- **Quarkus REST** - REST endpoint handling
- **Jackson** - JSON serialization
- **Maven** - dependency management

## Requirements

- Java 17+
- Maven 3.8+

## Running the Application

### Dev Mode

```bash
mvn quarkus:dev
```

The application starts on port 8080 by default. If there's a port conflict, use a different port:

```bash
mvn quarkus:dev -Dquarkus.http.port=8090
```

### Build

```bash
mvn clean package
```

## Usage

### Upload Form

Open in browser:

```
http://localhost:8080/
```

Select a file and click "Upload". Files are saved to the `my-uploads/` directory.

### API Endpoint

Upload a file via curl:

```bash
curl -X POST http://localhost:8080/upload -F "file=@/path/to/file"
```

## Project Structure

```
src/main/groovy/          # Groovy source code
src/main/resources/       # Resources (HTML, configuration)
my-uploads/               # Directory for uploaded files
```

## Configuration

Main settings in `src/main/resources/application.properties`:

- File size limits
- HTTP port
- Other Quarkus options

## Development

Quarkus dev mode provides:
- Live reload - code changes are automatically reloaded
- Dev UI - available at `/q/dev`
- Continuous testing
