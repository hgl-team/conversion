# Conversion

## Description

Another boring type conversion library. 
Written from scratch in pure java. Zero external dependencies 
to keep it light but powerful.

### Features

* Zero external dependencies to prevent your pom tree from exploding.
* Allows generic type conversions.
* Accepts parameterized conversions.
* Can be bound to spring

## Getting Started

### Maven

We've defined a convenient bom to handle artifact versions for you.

```xml
<dependency-management>
  <dependency>
      <groupId>org.hglteam</groupId>
      <artifactId>conversion-bom</artifactId>
      <version>3.0.5-SNAPSHOT</version>
      <type>pom</type>
  </dependency>
</dependency-management>
```

Then import the core dependency

```xml
<dependency>
  <groupId>org.hglteam</groupId>
  <artifactId>conversion-core</artifactId>
</dependency>
```

### Use

#### Step 1. Define your type conversion

```java

public class MyCustomTypeConverter 
        implements TypeConverter<SourceType, TargetType> { 
    public T convert(ConversionContext context, SourceType source) {
        TargetType result;
        
        result = ...;
        
        return result;
    }
}
```

#### Step 2. Register your type converters

```java
Converter converter = new DefaultConverter(
        new DefaultConversionMap()
                .register(new MyCustomTypeConverter())
                .register(new MyCustomTypeConverter2()
                .register(...)));
```

#### Step 3. Use your converter

##### Direct approach:

```java
var result = converter.convert(source, TargetType.class);
```

##### Functional approach:
```java
results = sourceList.stream()
        .map(converter.convertTo(TargetType.class))
        .collect(Collectors.toList());
```

##### Contextual conversions:

```java
result = converter.withContext(TargetType.class)
        .withArg("dateFormat", EXPECTED_DATE_TIME_FORMAT)
        .convert(source);
```

##### Contextual functional conversion:

```java
results = sourceList.stream()
        .map(converter.withContext(TargetType.class)
                .withArg("dateFormat", EXPECTED_DATE_TIME_FORMAT)
                ::convert)
        .collect(Collectors.toList());
```

##### Explicit type conversions

```java
// Using direct approach
var result = converter.convert(source, SuperSourceType.class, TargetType.class);

// Using contextual conversion
var result = converter.withContext(TargetType.class)
        .withSourceType(SuperSourceType.class)
        .withArg("dateFormat", EXPECTED_DATE_TIME_FORMAT)
        .convert(source);
```

##### Generic type conversion

```java
// Using direct approach
var result = converter.convert(source, 
        new TypeDescriptor<GenericTarget<String>>(){}.getType());

// Using contextual conversion
var result = converter.withContext(new TypeDescriptor<GenericTarget<String>>(){})
        .withSourceType(new TypeDescriptor<GenericTarget<String>>(){}.getType())
        .withArg("dateFormat", EXPECTED_DATE_TIME_FORMAT)
        .convert(source);
```

#### Spring binding

*ConversionConfiguration* provides ready-to-use instances of 
Converter and ConversionMap. After import, you can inject them 
anywhere. You can register your converters as shown below.

```java
@Configuration
@Import({ ConversionConfiguration.class })
public class ApplicationConversionConfig {
    private ConversionMap conversionMap;
    
    @Autowired
    public ApplicationConversionConfig(ConversionMap conversionMap) {
        this.conversionMap = conversionMap;
    }
    
    @PostConstruct
    void registerConverters() {
        conversionMap
                .register(new CustomTypeConverter1())
                .register(new CustomTypeConverter2())
                .register(new CustomTypeConverter3())
                ...;
    }
}
```
## Contribution

We're open to comments! Help us to improve our work.
If you found a bug or want to propose an improvement, please leave let us know.

## Authors
  
[@jlhuerfanor](https://github.com/jlhuerfanor)

## Version History

* 3.0.3
    * Baseline

## License

This project is licensed under the Apache License version 2.0 - see the LICENSE file for details