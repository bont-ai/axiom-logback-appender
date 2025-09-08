# axiom-logback-appender

Basic [Axiom](https://axiom.co/) logback appender.

## Usage

In your project.clj dependencies

```clj
[ai.bont/axiom-logback-appender "1.0.1"]
[hato "1.0.0"]
```

or deps.edn

```clj
ai.bont/axiom-logback-appender {:mvn/version "1.0.1"}
hato/hato {:mvn/version "1.0.0"}
```

Configure it inside your `logback.xml` file. Sample below with environment variables used for api token and dataset
name:

```xml

<appender name="AXIOM" class="ai.bont.AxiomLogbackAppender">
    <apiDomain>api.axiom.co</apiDomain>
    <apiToken>${AXIOM_API_TOKEN}</apiToken>
    <datasetName>${AXIOM_DATASET}</datasetName>
    <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
</appender>
<appender name="ASYNC_AXIOM" class="ch.qos.logback.classic.AsyncAppender">
<appender-ref ref="AXIOM"/>
</appender>

<root level="INFO">
<appender-ref ref="ASYNC_AXIOM"/>
</root>
```

## Inspirations

Inspired by the [axiom appender](https://github.com/yHSJ/axiom-appender). Rewritten since we couldn't get it working.

## License

This project is licensed under the [MIT License](https://opensource.org/license/mit/).
