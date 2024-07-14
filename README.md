# Learning Pulsar with pulsar4s

This repository is dedicated to learning Apache Pulsar using the `pulsar4s` library, which is a Scala wrapper for the Java API of Pulsar. It contains hands-on code examples and exercises that help understand the concepts and practical usage of Pulsar.

## Purpose

- To provide a structured approach for beginners to learn Apache Pulsar.
- To demonstrate the usage of the `pulsar4s` library for interacting with Pulsar.
- To offer practical examples and exercises to reinforce learning.

## Why This Repository?

As a beginner learning Apache Pulsar, this repository provides a hands-on approach to understand its functionalities and how to use the `pulsar4s` library effectively. It aims to be a helpful resource not only for personal learning but also for others who are new to Pulsar and `pulsar4s`.

## Running the Apache Pulsar Docker Container

To run the Apache Pulsar standalone in a Docker container, use the following command:

```sh
docker run -d \
  -p 6650:6650 \
  -p 8080:8080 \
  -v $PWD/data:/pulsar/data \
  --name pulsar \
  apachepulsar/pulsar-standalone
```
## Running the Tests
To run the tests for this project, use the following command:

```sh
sbt test
```