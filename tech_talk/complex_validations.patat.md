---
title: Complax Validations
author: Denys Yahofarov
...

# Why so Complex?

* Real world is complex and chaotic
* We want to model only a subset of _anything_ of real world
* Most of real world cases make no sense for our domain

---

# Static vs. Dynamic

Variance of input:

* Static Types - less of variance
* Dynamic Types (or almost no at all) - more variance

Effort to tame chaos:

* Static Types - more effort to tame chaos at the app input
* Dynamic Types (or almost no at all) - more effort to tame chaos… well… everywhere

---

# Declarative vs. Imperative

* Declarative - one sees the top of the iceberg, and has no much control over complexity in the depth
* Imperative - one sees all the implementation details, and has all chances to lose the overall picture

---

# Motivation

* Test the statement “Jakarta EE validators are good for anything. Why use anything else?”
* Compare Dynamically typed Ruby and Grape with Statically Typed Kotlin and Spring
* Compare Declarative and Imperative styles of validation in Kotlin (and JVM in general)

---

# Use Case

* Simple API for creating an Order with Line Items
* Make it bit more complicated…

---

# Demo Time!
