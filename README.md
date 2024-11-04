# Scalar Product with Producer-Consumer Model

This project demonstrates the computation of the scalar (dot) product of two vectors using a producer-consumer model implemented with threads in Java. It highlights thread synchronization using a shared buffer and locks.

## Overview

The program consists of the following components:

- **ScalarProduct Class**: Manages the shared buffer between the producer and consumer. It synchronizes access using `synchronized` blocks and a lock object.
- **Producer Class**: Computes the product of corresponding elements of two vectors and places the result in the buffer.
- **Consumer Class**: Consumes products from the buffer and accumulates them to compute the scalar product.
- **Main Class**: Initializes vectors with random values, starts the producer and consumer threads, and displays the final scalar product.

## Features

- Implements a producer-consumer pattern with a fixed buffer size of 1.
- Synchronization using `wait()` and `notify()` ensures thread-safe communication between producer and consumer.
- Randomly generates two vectors and computes their scalar product.

## How to Run

1. Compile the Java program:
   ```bash
   javac ScalarProductThreadExample.java
