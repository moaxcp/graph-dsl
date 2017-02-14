# graph-dsl

A groovy dsl for creating and traversing graphs. 
For project build status check the [wiki](https://github.com/moaxcp/graph-dsl/wiki).

# Usage

graph-dsl is used to create graphs and algorithms for graphs.
It is designed to be groovy, using closures and metaprogramming for minimal setup.

NOTE: This project is not currently in a repository for use.

## Building a graph

```groovy
def graph = new Graph()

graph.with {
    vertex 'step1'
    vertex 'step2'
    vertex 'step3'
    vertex 'step4'
    edge 'step1', 'step2'
    edge 'step3', 'step4'
}
```

## Apply a plugin

Plugins may be applied to graphs to change their behavior.

```groovy
graph.apply DirectedGraph
```

This plugin makes the graph into a directed graph.

## depth first traversal

Depth first traversal supports preorder and postorder traversal.

```groovy
def preorderList = []

graph.depthFirstTraversal {
    preorder { vertex ->
        preorderList << vertex.name
    }
}

println preorderList
```

The closure is used to build a DepthFirstTraversalSpec. The spec defines the behavoir of the traversal.

# Getting Started

## install git
## install gitflow-avh
## clone the project

git clone https://github.com/moaxcp/graph-dsl.git

## build

`./gradlew build`

# Contributing

Contributions are welcome. Please submit a pull request to the develop branch in github. If there are any issues contact me moaxcp@gmail.com.

## Technology

* [github](https://github.com/)
* [git](https://git-scm.com/)
* [gitflow-avh](https://github.com/petervanderdoes/gitflow-avh)
* [gradle](https://gradle.org/) 
    * [gradle-gitflow](https://github.com/amkay/gradle-gitflow)
* [groovy](http://www.groovy-lang.org/)
* [spock](http://spockframework.org/)
* [jacoco](http://www.eclemma.org/jacoco/)
* [sonarqube.com](https://sonarqube.com/dashboard?id=com.github.moaxcp%3Agraph-dsl)
* [travis-ci.org](https://travis-ci.org/moaxcp/graph-dsl)

# Releases

## 0.3.0

* Added delegate support for Edges and Vertices
* Added VertexMapPlugin and EdgeMapPlugin

## 0.2.0

* Optimized codenarc configuration using the ruleset method.
* Updated gradle to version 3.3.
* Added Plugin interface for all Plugins to implement.
* Added plugins property to Graph which contains all plugins applied to the Graph.
* Adde DirectedGraph plugin which converts a Graph into a DirectedGraph.

## 0.1.2

sonarqube.com doesn't seem to show any issues for groovy projects. This release adds codenarc support for local development. This allows developers to view issues right away without worrying about sonar.

## 0.1.1

* Changed travis.sh so master gets published to sonar instead of develop.

## 0.1.0

* initial plugin support
* DirectedGraph plugin

## 0.0.0

* Support for vertices, edges, and depth first traversal

# License

The license can be found in the LICENSE file.

MIT License

Copyright (c) 2017 John Mercier