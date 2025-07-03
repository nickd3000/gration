# Gration
**Gration** is a lightweight Java framework for building flexible, modular message-driven applications. It provides a simple but powerful model for composing message processing pipelines, defining in-memory channels, and working with generic messages that can carry both payloads and headers. Gration is designed for extensibility, letting you chain together components to create expressive flows tailored to your application's needs.

## Features
- **Message Channels:** Pass messages through in-memory queues.
- **Generic Messages ():`Msg<T>`** Carry any payload and headers.
- **Message Splitting:** Use the `split` component to fan out list payloads into separate messages.
- **Extensible Flow:** Chain processing steps such as `peek`,`split` etc. 


## Getting Started
### Requirements
- Java 17
- Maven

### Building
Clone the repository and build with:

    mvn clean install

### Adding to Your Project
If you want to use this module in another Maven project, add:

### Testing
Tests are written in Groovy/Spock. To run tests:

    mvn test

## Example Usage
Send a message containing a list, and let the component turn each element into a separate message: `Split`
```
// Create a direct message channel
DirectChannel inChannel = new DirectChannel();

// Build a message with a list payload
Msg<List<String>> msg = new Msg<>(List.of("one", "two", "three"));

// Set up the flow: print each message, split list payloads, print split messages
MessageFlow.of(inChannel)
.peek(m -> System.out.println(m))
.split()
.peek(m -> System.out.println(m));

// Send the message
inChannel.send(msg);
```

## Extending
- Implement additional types to transform or route messages. `FlowComponent`
- Use or customize for your own payload and header needs. `Msg<T>`
