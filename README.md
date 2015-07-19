#Motivation

Nowadays with the ever increasing number of journaling systems and serialization frameworks used in our systems there's 
the need to use some kind of mechanism to help us keep the performance levels. This is even more true when one is 
consuming high amounts of messages but are only interested in a (small) subset of them. 

This library was created to tackle this particular problem. It proposes a Message format which comprises a pre-defined 
headers object and an abstract payload. Having this separation allows us to blindly deserialize the headers and use them 
to decide whether or not payload deserialization is required. 

#Implementations

There are several different serialization frameworks out there and it will be very hard to have implementations for them
all. For now there's an implementation based on Avro. Hopefully more will come!

##Avro

Apache Avro based implementation of message serialization. It defines an Avro specific message format that defines the 
headers format on the wire but delegates to a third party Serializer the responsibility to (de)serialize the payloads. 
The nature of this third party Serializer is totally irrelevant: this implementation handles just as well Avro payload 
serializers or any other Serializer.