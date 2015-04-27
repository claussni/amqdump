# ActiveMQ dump

A utility to connect to [Fedora Commons](http://www.fedora-commons.org/) ActiveMQ Server
and dump all messages from APIM topics to STDOUT.

## Description

Fedora Commons send ATOM.XML messages to a particular ActiceMQ topic on execution
of management API methods such as ingest, getObject, modifyDatastream etc. See
[Server Reference, Messaging](https://wiki.duraspace.org/display/FEDORA36/Messaging) for details
on how the server has to be configured.

There is no concise documentation about the set of API methods that can be send and which
parameters are submitted. However, as this mechanism uses Java Reflection, one can reason
about possible methods by looking at the source code of the [management API subsystem](https://github.com/fcrepo/fcrepo/blob/master/fcrepo-server/src/main/java/org/fcrepo/server/management/Management.java).

## Building

The amqdump program is a Maven project and as such can be build with the Maven package command:

```
$ mvn package
```

This will generate a executable JAR file ```target/amqdump-<VERSION>.jar``` directory containing
only necessary dependencies can classes needed for listening to and dumping out Fedora ActiveMQ messages.

## Usage

After building the JAR file one can execute the program with ```java -jar amqdump-<VERSION>.jar```. It will silently
connect to the default ActiveMQ URL and start listen to all messages and print them.

### Changing defaults

The default ActiveMQ URL is ```tcp://localhost:61616```. The default topic filter ```fedora.apim.*```. To change this,
one has to provide appropriate Java system properties (java.naming.provider.url, topic.fedora) to the runtime on startup.
For example to listen to some other server and only dump update method messages run:

    $ java  \
        -Djava.naming.provider.url=tcp://some.other.amq:71717 \
        -Dtopic.fedora=fedora.apim.update
        -jar amqdump-<VERSION>.jar \


### Stop listening

Just hit ```Ctrl-C``` in the console. It will shutdown the connection to Fedoras ActiveMQ endpoint and exit the program.

## Licence

The program is licenced under [GPLv3](http://www.gnu.org/licenses/gpl.html). See the COPYING file for details.

