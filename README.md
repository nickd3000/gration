# Gration
**Gration** is a lightweight Java framework for building flexible, modular message-driven applications. It provides a simple but powerful model for composing message processing pipelines, defining in-memory channels, and working with generic messages that can carry both payloads and headers. Gration is designed for extensibility, letting you chain together components to create expressive flows tailored to your application's needs.

## Features
- **Message Channels:** Support for both subscribable (`DirectChannel`) and pollable (`QueueChannel`) channels.
- **Generic Messages (`Msg<T>`):** Carry any payload and headers with a fluent `MessageBuilder`.
- **Message Sources:** Connect to external systems using the `MessageSource` interface (e.g., `FileMessageSource`).
- **Pollers:** Control message processing from pollable sources using `FixedRatePoller` or `ManualPoller`.
- **Extensible Flow DSL:** Chain processing steps such as `peek`, `transform`, `filter`, `split`, and `bridgeTo`.


## Getting Started
### Requirements
- Java 21
- Maven

### Building
Clone the repository and build with:

    mvn clean install

### Adding to Your Project
If you want to use this module in another Maven project, add:

```xml
<dependency>
    <groupId>com.physmo</groupId>
    <artifactId>gration</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Testing
Tests are written in Groovy/Spock. To run tests:

    mvn test

## Example Usage

### Simple Message Flow
Send a message through a direct channel and process it with a DSL flow:
```java
DirectChannel inChannel = new DirectChannel();

MessageFlow.of(inChannel)
    .filter(m -> m.getPayload() != null)
    .peek(m -> System.out.println("Processing: " + m.getPayload()))
    .transform(m -> MessageBuilder.withPayload(m.getPayload().toString().toUpperCase()).build())
    .handle(m -> {
        System.out.println("Result: " + m.getPayload());
        return m;
    });

inChannel.send(MessageBuilder.withPayload("hello world").build());
```

### Polling from a Message Source
Poll files from a directory every 2 seconds:
```java
FileMessageSource fileSource = new FileMessageSource("./input");
FixedRatePoller poller = new FixedRatePoller(2000);

MessageFlow.of(fileSource, poller)
    .split() // FileMessageSource returns Msg<List<File>>
    .handle(m -> {
        System.out.println("Processing file: " + m.getPayload());
        return m;
    });

poller.init();
```

## Spring Integration Familiarity
Gration adopts naming conventions and patterns familiar to Spring Integration users:
- **MessageBuilder:** Fluent API for creating messages and managing headers.
- **MessageHandler:** Interface for custom message processing logic (`handle`).
- **Transformer:** Interface for message transformations (`transform`).
- **Filter:** Interface for message filtering (`filter`).
- **MessageSource:** Interface for retrieving messages from external sources (`poll`).
- **Poller:** Mechanism to trigger polling from `PollableChannel` or `MessageSource`.
- **DSL:** `MessageFlow` provides `handle()`, `transform()`, `filter()`, `split()`, `peek()`, and `bridgeTo()`.

## Extending
- Implement `FlowComponent` or `Processor` to create custom processing nodes.
- Implement `MessageSource` to pull data from new sources (databases, APIs, etc.).
- Customize `Msg<T>` for your own payload and header needs.
