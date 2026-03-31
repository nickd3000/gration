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
Send a message containing a list, and let the `split` component turn each element into a separate message:
```java
// Create a direct message channel
DirectChannel inChannel = new DirectChannel();

// Build a message with a list payload using MessageBuilder
Msg<List<String>> msg = MessageBuilder.withPayload(List.of("one", "two", "three"))
    .setHeader("myHeader", "myValue")
    .build();

// Set up the flow: print each message, split list payloads, print split messages
MessageFlow.of(inChannel)
    .peek(m -> System.out.println("Before split: " + m))
    .split()
    .transform(m -> MessageBuilder.fromMessage(m).setHeader("processed", true).build())
    .handle(m -> {
        System.out.println("Processing: " + m);
        return m;
    })
    .peek(m -> System.out.println("After split: " + m));

// Send the message
inChannel.send(msg);
```

## Spring Integration Familiarity
Gration adopts naming conventions and patterns familiar to Spring Integration users:
- **MessageBuilder:** Fluent API for creating messages and managing headers.
- **MessageHandler:** Interface for custom message processing logic (`handle` method).
- **Transformer:** Interface for message transformations (`transform` method).
- **DSL:** Flow definitions use `handle()`, `transform()`, `filter()`, `split()`, and `bridgeTo()`.
- **Channels:** Support for `subscribe()` and `unsubscribe()`.

## Extending
- Implement additional types to transform or route messages. `FlowComponent`
- Use or customize for your own payload and header needs. `Msg<T>`
