# astral-container
A pure Java 8 dependency injection library, inspired by the Unity Library

### Requires:
* Java 1.8.0_40 or higher
* gradle 2.3

## Overview

### Why use dependency injection?
Using dependency injection automates the creation of objects in your application.  This can be very useful for scaling large projects.  As the number of classes in the system grows, it can be harder and harder to maintain the graph of dependencies between objects.  This is especially true in Java, where everything is an object.  Object creation can become a significant portion of the codebase.  One can alleviate this by creating factories, but these can be tedious to maintain, having access to every class in the system and doing work that can essentially be automated.

### Why do we need another dependency injection library?
We don't really, but I wanted to build this anyway :).  I had trouble using Google's Guice in a practical manner (you can't put attributes on classes you don't own), and Spring is a complete framework that includes many things you may not want.  I really liked using the Unity library in C#, and wanted to do the same thing in Java.  In particular, I like configuring object dependencies in **code**, rather than in xml, since it can be syntax-checked and tested easily.

## How do I use the Astral Container?
Besides including the library in your application, you should perform these 3 distinct steps in your application:

* Registration
* Validation
* Resolving (creating objects)

The first 2 (Registration and Validation) are done during application startup.  Resolving happens throughout the rest of the lifetime of the application.

### Registration
Registration is creating the container and telling it about your classes.

```java
AstralContainer container = new AstralContainer();
container.register(MyInterface.class, MyImplementation.class);
```

You don't have to use a different interface and implementation class, just mention the same class twice.

```java
AstralContainer container = new AstralContainer();
container.register(MyFoo.class, MyFoo.class);
```

As your code grows into mulitple modules, I recommend having each module register their classes.  This can help eliminate a giant list of all classes in the application, and can be used in tests to test just the modules you need.  For example:

```java
AstralContainer container = new AstralContainer();
ModuleA.registerClasses(container);
ModuleB.registerClasses(container);
//...
```

I think it is best not to create any classes during registration, if it can be avoided.  Just register all the classes and let the container figure out what order to create them in.

You can create as many containers as you want in an application, there is no static data or default instance. You can make it a singleton if you want, but that can be the only one your own code maintains (all the rest can be accessed via the container).

If you register a class twice, the latest entry will be used. I find it useful in unit tests tests to create containers and override interfaces with mock implementations. 

### Validation
Validation looks at all the registrations and tells you if there are any missing classes or circular dependencies. This step is optional but recommended because it catches runtime problems earlier. If these problems are found an exception is thrown.

```java
AstralContainer container = new AstralContainer();
// registration here...
container.validateAll();
```

It's possible to go through all the registration, and call validate without creating any objects.  It's recommended to do so in an automated test.

Unlike other libraries I have used, Astral will include in the exception messgae the actual path of the circular reference.  This exception will also be thrown while resolving (in case you ignored the validate exception or didn't validate).

```
net.slipperlobster.astralContainer.AstralException: Circular reference detected for type:
 net.slipperlobster.astralContainer.Ghosts$BackReferenceComposedImpl referencing: net.slipperlobster.astralContainer.Ghosts$IBase through from path:
 net.slipperlobster.astralContainer.Ghosts$IBase -> net.slipperlobster.astralContainer.Ghosts$SingleComposedImpl ->
 net.slipperlobster.astralContainer.Ghosts$BaseRaw -> net.slipperlobster.astralContainer.Ghosts$BackReferenceComposedImpl ->
 net.slipperlobster.astralContainer.Ghosts$IBase
```

Note: this library can't proactivly detect circular references via injected constructors, either during validation or during runtime. (more about this later)  If you really want this feature let me know and I can add it, but with a language like Java it requries a bit of (somewhat ugly) trickery.

### Resolving
Resolving (creating objects) is easy, simply call resolve on the interface you wish to use, and the implementing object will be created.

```java
AstralContainer container = new AstralContainer();
container.register(MyInterface.class, MyImplementation.class);

MyInterface mine = container.resolve(MyInterface.class);
```

Astral is actually looking at all of the constructors and creating the graph of dependent objects -- the entire graph is created.  When a class has multiple constructors, the constructor with the least number of parameters is used.

The simplest cases are described above, for more use cases, see the features section.

## Features
Astral Container supports several useful features, including:

* Parameterized Types
* String qualifiers
* Singletons
* Constructor Injection

All of these can be used together (with one exception described below).  For example you can have a string-qualified parameterized singleton.

The main feature that's currently missing (but also missing from a lot of libraries) is the ability to register an injected constructor for a singleton, such that it is lazily created.  Again, if you really want this feature let me know, it's not that hard to add.

### Parameterized Types
For parameterized types, you need to create a TypeRef-derived anonymous class in order for the correct type to get instantiated:

```java
AstralContainer container = new AstralContainer();
container.register(new TypeRef<MyInterface<String>>() {}, new TypeRef<MyImplementation<String>>() {});
container.register(new TypeRef<MyInterface<BigDecimal>>() {}, new TypeRef<MyImplementation<BigDecimal>>() {});

//...

MyInterface<String> mine = container.resolve(new TypeRef<MyInterface<String>>() {});
```

Note the use of `{}` to create an anonymous derived class.  Sadly, this semi-ugly syntax is just a fact of life with Java's type erasure.

### String qualifiers
You can register multiple implementations of the same interface, qualified with a string.  For example:

```java
AstralContainer container = new AstralContainer();
container.register(MyInterface.class, "Stringy", new TypeRef<MyImplementation<String>>() {});
container.register(MyInterface.class, "Mathy", new TypeRef<MyImplementation<BigDecimal>>() {});

//...

MyInterface mine = container.resolve(MyInterface.class, "Stringy");
```

### Singletons
You can register an instance of an object as a singleton.  Any calls to resolve will return that object instead of creating a new one.

```java
AstralContainer container = new AstralContainer();
container.registerSingletonInstance(MyInterface.class, new MyImplementation<String>());

//...

MyInterface mine = container.resolve(MyInterface.class);
```

### Constructor Injection
You can provide a lambda or other factory function to explicity let Astral know how to call the constructor.  This is useful in providing fixed arguments to third-party classes.

```java
AstralContainer container = new AstralContainer();
AuthInfo authinfo = getSecureToken();
container.register(SqlConnection.class, (AstralContainer c) -> new SqlConnection(authInfo));
```

The `new SqlConnection(authInfo)` will get executed whenever a `SqlConnection` is created, but not during registration.

The `AstralContainer` is passed into the factory function so you can still call resolve for some constructor parameters.  For example:

```java
AstralContainer container = new AstralContainer();
AuthInfo authinfo = getSecureToken();
container.register(SqlConnectionPool.class, (AstralContainer c) -> new SqlConnectionPool(MAX_CONNECTIONS));
container.register(SqlConnection.class, (AstralContainer c) -> new SqlConnection(authInfo, c.resolve(SqlConnectionPool.class));
```

## Feedback Welcome
Please submit any questions, suggestions, bugs, or other feedback via the issues system.  Thanks!  --Duane


