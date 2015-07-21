#messaging-api

This library introduces the concept of a *Message* comprised of *Headers* and *Payload*. *Headers* being plain 
key-value pairs and *Payload* being whatever object needed for your own purposes. 

The api also introduces the concept of *LazyDeserializerSupplier* which is but a way to delay deserialization as much as 
possible (even skipping it altogether for some use cases). This can introduce serious performance gains in some common 
situations. 

It is just a simple proposal for a common problem out there. It's still in an early stage but this model is already 
working on a high-concurrent environment with great results.


#Implementations

There are several different serialization frameworks available and it will be very hard to have implementations for them
all. For now there's an implementation based on Avro. Hopefully more will come!

##messaging-avro

Apache Avro based implementation of message serialization. It defines an Avro specific message format that defines the 
*Headers* format on the wire but delegates to a third party *Serializer* the responsibility to (de)serialize the payloads. 
The nature of this third party *Serializer* is totally irrelevant: this implementation handles just as well Avro payload 
serializers or any other kind of *Serializer*.

###Prepare serializers
```java
// You code this one
Serializer<MyDomainObject> myDomainObjectSerializer = new MyDomainObjectSerializer();
// And the library provides you with this one
Serializer<Message<MyDomainObject>> serializer = new AvroMessageSerializer<>(myDomainObjectSerializer);
```

###Producer
```java
// Create your domain object
MyDomainObject myDomainObject = new MyDomainObject();
// Use it to create a Message
Message<MyDomainObject> message = new Message<>(myDomainObject);
// And set all the headers you want
message.putHeader("year", "2015");

// Now transform the whole message into a byte array
byte[] bytes = serializer.serialize(message);
```

###Consumer
```java
// Get hold of the bytes
byte[] bytes = ...;
// Deserialize the message. Only headers will be deserialized here
Message<MyDomainObject> message = serializer.deserialize(bytes);

// Check the headers
if ("2015".equals(message.getHeader("year").get())) {
    // This is where the payload deserialization occurs, when you're sure you'll need it!
    processThisYearMessage(message.getPayload());
}
```