# graph-dsl

A groovy dsl for creating and traversing graphs. Graphs can be extended with types which allows developers to create a
graph with the desired behavior and values for their algorithm.

[![Travis](https://img.shields.io/travis/moaxcp/graph-dsl.svg)]()
[![Build Status](https://travis-ci.org/moaxcp/graph-dsl.svg?branch=master)](https://travis-ci.org/moaxcp/graph-dsl)
[![Javadocs](https://www.javadoc.io/badge/com.github.moaxcp/graph-dsl.svg)](https://www.javadoc.io/doc/com.github.moaxcp/graph-dsl)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.moaxcp/graph-dsl.svg)]()
[![Libraries.io for GitHub](https://img.shields.io/librariesio/github/moaxcp/graph-dsl.svg)]()
[![license](https://img.shields.io/github/license/moaxcp/graph-dsl.svg)]()

# Usage

There are two entry points into the dsl: The `graph` method, and `BaseScript`.

## BaseScript

`BaseScript` is a transformation which allows developers to change the base script class for the current script. By
changing the script to `DslScript` a new `Graph` becomes the delegate of the script.

```groovy
#!/usr/bin/env groovy
@Grab(group='com.github.moaxcp', module='graph-dsl', version='latest.revision')
import graph.*
@groovy.transform.BaseScript DslScript graph
edge step1, step2
assert graph.vertices.keySet() == ['step1', 'step2'] as Set //vertices were created!
assert graph.edges.size() == 1
assert graph.edges.first() == new Edge(one:'step1', two:'step2') //edge was created!
```

This example of a graph creates two vertices named 'step1' and 'step2' as well as an edge between them. The basic graph
structure is held in a map of named Vertex objects and a set of Edge objects. Each Edge contains the names of the two
vertices it connects.

The same graph can be created using the `graph` method.

## graph method

The entry-point to the dsl is the `Graph.graph(Closure)` method. This method applies the closure to a new Graph object 
and returns it. 

```groovy
#!/usr/bin/env groovy
@Grab(group='com.github.moaxcp', module='graph-dsl', version='latest.revision')
import static graph.Graph.*
def graph = graph {
    edge step1, step2
}
```

## dsl policy

There are a few rules the dsl follows as it is being processed.

1. All referenced vertices are created if they don't exist.
2. If an edge or vertex already exists it will be reused by and operation.

## directed graphs

The Default behavior of a graph is undirected. These graphs have a set of edges where only one edge
can connect any two vertices. An undirected graph can be changed to a directed graph at any time using 
`DirectedGraphType`.

```groovy
//lots of code
type 'directed-graph'
//lots of code
```

## Traversing a graph

Once a graph is created there is a dsl for depthFirstTraversal and breadthFirstTraversal.

```groovy
type 'directed-graph'
vertex A {
    connectsTo B, D, E
    connectsFrom D
}

vertex D {
    connectsTo C, E
    connectsFrom B
}

edge B, C
depthFirstTraversal {
    root A
    preorder { vertex ->
        println vertex.name
    }
}

breadthFirstTraversal {
    root A
    visit { vertex ->
        println "bft $vertex.name"
    }
}
```

`depthFirstTraversal` provides preorder and postorder methods.

## Functional search methods

There are functional search methods built on the depthFirstTraversal and breadthFirstTraversal method. These methods 
follow the standard names in groovy: each, find, inject, findAll, collect. The methods can specify which type of 
search to perform such as `eachBfs` or `eachDfs`.

```groovy
eachBfs {
    println it.name
}
    
def vertex = findBfs {
    it.name == 'A'
}
    
def bfsOrder = collectBfs {
    it.name
}
```

Note: These methods are not yet implemented for depth first traversal. The depth first search methods will be the
defaults when a search type is not specified.

## Edge Classification

Depth first traversal supports edge classification where an edge is classified as:

* tree-edge - when the destination vertex is white
* back-edge - when the destination vertex is grey
* forward-edge - when the destination vertex is black
* cross-edge - when the destination vertex is black and in a different tree

To classify edges use the `classifyEdges(Closure)` method.

```groovy
//setup graph
classifyEdges { from, to, edgeType ->
    println "$from $to is $edgeType"
}
```

`classifyEdges` returns an EdgeClassification object. This object contains lists of all back edges, tree-edges,
forward edges, and cross edges. There is also a new Graph called forest that gets created. 
`EdgeClassification.forrest` contains the forrest created by tree edges. It uses the vertex and edge objects
from the original graph object.

Calling `classifyEdges` on an undirected graph will result in two classifications for each edge. The first classification
is what the edge would be in a directed graph. The second classification is always back-edge. This is because edges in 
an undirected graph are considered bi-directional in `classifyEdges`.

# Edge and Vertex

## Vertex keys

Vertex keys are used to refer to a Vertex in the dsl. Keys can be any object which implements equals and hashCode.

## Edge and Vertex are Maps

Since Edge and Vertex are maps, all of the syntax for maps apply to them. In a dsl closure assignments create
and entry in the Edge or Vertex.

```groovy
edge(A, B) {
    key = 'value'
}

vertex step1, {
    label = 'first step in process'
}
```

If non dsl entries are used in a dsl method they are added to the object.

```groovy
edge(A, B, [weight:10]) //weight is a non dsl entry

vertex(step1, [connectsTo:step1, color:'red']) //color is a non dsl entry
```

# Types

Types can be changed with the type methods.

```groovy
type 'directed-graph'
type graph.type.DirectedGraphType
```

A graph's type determines specific behavior. A type can change a normal graph to an directed-graph. It can add wieght
to the graph. It can change the graph to a DAG. Developers can make their own type and apply it to a graph. Types can:

1. replace all edges and vertices
2. add methods to the edge and vertex dsl
3. change the data structures used by the graph
4. add constraints to the structure of the graph

The type of a graph may be changed at any time. An undirected-graph may be changed to a directed-graph which can be
changed back to an undirected-graph.


## DirectedGraphType

```groovy
type 'directed-graph'
```

The DirectedGraphType changes the behavior of a graph to that of a directed graph. In a directed graph edges
become directional. Only two edges can exist between any two vertices. In that case, the edges need to go in
opposite directions.

Traversal methods will only follow out edges from a vertex.

## WeightedGraphType

```groovy
type 'weighted-graph'
```

WeightedGraphType is an undirected graph where edges can have weight. Traversal methods follow edges with the least
weight.

### Adding weight

Edges can be assigned weight within the dsl.

```groovy
type 'weighted-graph'

edge A, B, [weight:10]

edge (A, B) { weight = 10 }
```

## WeightedDirectedGraphType

```groovy
type 'weighted-directed-graph'
```

WeightedDirectedGraphType is a directed graph where edges can have weight. Traversal methods follow edges with the least
weight.


# Plugins

Plugins may be applied with the plugin methods.

```groovy
plugin 'graphviz'
plugin graph.plugin.GraphViz
```

## Graphviz

Graphviz is a graph visualization toolset. The project provides a dsl called dot for visualizing graphs. The graphviz
plugin provides methods to create dot strings, BufferedImages and to view the graph. Edge and Vertex attributes will be used as dot attributes.

```groovy
type 'directed-graph'
plugin 'graphviz'
vertex A {
    connectsTo B {
        connectsTo C, D
    }
    connectsTo D {
        connectsTo C
        connectsTo E
    }
    connectsFrom D
}
vertex F, [connectsTo:G]
edge G, D
image 'images/graphviz.png
```
![Image of graph](/images/graphviz.png?raw=true "Grpah")

# Getting Started With Development/Contributing

## install git

Follow this guide to install git.

https://git-scm.com/book/en/v2/Getting-Started-Installing-Git

## install gitflow-avh

This project uses gitflow-avh. It is a plugin for git that provides the `git flow` commands. These commands are used to
follow the gitflow pattern for developing software. For more information see
[Workflow](https://github.com/moaxcp/graph-dsl/wiki/Workflow) in the wiki.

https://github.com/petervanderdoes/gitflow-avh/wiki/Installation

## clone the project

clone the project from github.

git clone https://github.com/moaxcp/graph-dsl.git

## build

The project uses gradle for the build system. Using gradlew, you do not need to install gradle to build the project.

`./gradlew build`

## Contributing

All contributions are welcome. Please submit a pull request to the develop branch in github.

## Ways to contribute

Even if you are just learning groovy there are many ways to contribute.

* Fix a codenarc issue. See [codenarc report](https://moaxcp.github.io/graph-dsl/reports/codenarc/main.html)
* Add/fix groovydoc
* create a wiki page
* create a plugin
* add a graph algorithm

If there are any issues contact me moaxcp@gmail.com.

# Technology

* [github](https://github.com/)
* [git](https://git-scm.com/)
* [gitflow-avh](https://github.com/petervanderdoes/gitflow-avh)
* [gradle](https://gradle.org/) 
    * [gradle-gitflow](https://github.com/amkay/gradle-gitflow)
* [codenarc](http://codenarc.sourceforge.net/)
* [groovy](http://www.groovy-lang.org/)
* [spock](http://spockframework.org/)
* [jacoco](http://www.eclemma.org/jacoco/)
* [travis-ci.org](https://travis-ci.org/moaxcp/graph-dsl)
* [oss sonatype](https://oss.sonatype.org/#welcome)

# Releases

## x.x.x

Adding graphviz plugin.

Vertices and edges are now maps. Closures in entries are not resolved as was the case when dynamic properties were set.

Example

```groovy
edge.weight = { queue.size() }
assert edge.weight == 10 //in 0.22.0 would call the closure. In this release the closure is returned.
```

## 0.22.0

[#102](https://github.com/moaxcp/graph-dsl/issues/102) Switch from name to key

In this release Vertex.name has been repalced with Vertex.key. The key may be any object that implements equals and
hashCode.

## 0.21.0

This release represents a major change in how graphs are typed. Additional functionality is no only added through a
Type. Type is the delegate of a Graph. A graph's type provides additional methods and can change any aspect of how a
graph works. Types provide a graph with edges and vertices. They define the EdgeSpec and VertexSpec which means a type
may add new methods to the dsl for building edges and vertices.

* upgraded to gradle 4.2
* replaced plugins with type
* edges and vertices can now add properties dynamically. This removes the need for traits.

## 0.20.0

* [#98](https://github.com/moaxcp/graph-dsl/issues/98)

Adding DslScript which can be used with @BaseScript to make a new Graph object the delegate of the current script.

## 0.19.0

* [#91](https://github.com/moaxcp/graph-dsl/issues/91) Plugins should be called Type
 
This change adds names for types of graphs and plugins. Instead of the developers needing to import and use classes
they can simply use a name.

```
type 'directed-graph'
plugin 'edge-map'
```

## 0.18.0

* [#93](https://github.com/moaxcp/graph-dsl/issues/89) NameSpec and ConfigSpec
* runnerCode can no longer be set in map parameters to vertex and edge methods

This release includes a major refactor in how graphs are built. Configurations for vertex and edge methods have been 
refactored to use NameSpec and ConfigSpec object. A NameSpec is a wrapper for a string. It represents the name of 
something. A ConfigSpec is a wrapper around a map and closure. It represents the configuration for something. NameSpec
and ConfigSpec are a common interface for graph types to share. VertexSpec and EdgeSpec are now controlled by the graph
type. Types can add new properties and methods for use in a ConfigSpec. An example of this is connectsFrom in a 
directed graph. `connectsFrom` can only be used after applying `DirectedGraphPlugin`. Graph types can override methods 
like connectsTo and connectsFrom to perform checks before adding edges. This will be important when the DAG type is 
added.

For the first time packages have been introduced to the project.

<dl>
  <dt>plugin</dt>
  <dd>these are plugins which add functionality not related to specific types of graphs</dd>
  <dt>trait</dt>
  <dd>these are traits which can be used on vertices and edges</dd>
  <dt>type</dt>
  <dd>these are types of graphs and related classes</dd>
</dl>

Factories for EdgeSpec and VertexSpec are now used by Graph. This allows plugins to add new behavior to the edge and 
vertex methods. New attributes and methods can be added to the ConfigSpec used in these methods. DirectedGraphPlugin
uses this to add connectsFrom to the map and closure in the ConfigSpec.

## 0.17.0

* [#82](https://github.com/moaxcp/graph-dsl/issues/82) Methods that take vertex names should also take VertexNameSpec
* [#84](https://github.com/moaxcp/graph-dsl/issues/84) Configure gradle to fail for codenarc violations and jacoc verification
* [#87](https://github.com/moaxcp/graph-dsl/issues/87) TraversalSpec should return VertexNameSpec when property or method is missing
* [#85](https://github.com/moaxcp/graph-dsl/issues/85) Traversal methods should accept a root param

## 0.16.0

* [#80](https://github.com/moaxcp/graph-dsl/issues/80) Removed sonarqube since it no longer supports groovy
* [#78](https://github.com/moaxcp/graph-dsl/issues/78) Changed edgesFirst and edgesSecond in dsl to connectsTo and connectsFrom.
* [#77](https://github.com/moaxcp/graph-dsl/issues/77) renameOne and renameTwo in edge methods will not add extra vertex objects to the graph.

## 0.15.0

This release combines fixes for a few issues on github.

* [#75](https://github.com/moaxcp/graph-dsl/issues/75) Vertex.name, Edge.one, and Edge.two is now @PackageScope. This only 
affects code that is @CompileStatic for now due to [GROOVY-3010](https://issues.apache.org/jira/browse/GROOVY-3010).
* [#74](https://github.com/moaxcp/graph-dsl/issues/74)Vertices and edges may now be deleted. A vertex cannot be deleted if
there are edges referencing it.
* [#73](https://github.com/moaxcp/graph-dsl/issues/73) Added EdgeWeightPlugin. This plugin adds the Weight trait to each 
edge. Traversal methods are ordered by edge weight.

There were also several other changes that were not an issue on github:

Updated gradle to 3.5. Refactored gradle script to use the plugins closure when possible. gradle-gitflow does not work 
with the closure because it is not in the gradle repository. This is another reason to update the plugin. Spock was also 
updated to 1.1.

Added edgeTraits and vertexTraits. These methods will ensure traits are applied to current and future edges and vertices.

Added tests to provide more code coverage in jacoco.

Added more usage details to README.md

## 0.14.0

Just as in 0.13.0, where the config closure was removed from VertexSpec, this release removes the config closure from 
EdgeSpec. Traits can be added and configured for an Edge all within the same closure.

```groovy
graph.with {
    edge (step1, step2) {
        traits Mapping
        message = "hello from edge between $one and $two"
        queue = [] as LinkedList
        traits Weight
        weight { queue.size() }
    }
}
```

As in 0.13.0 this represents a major step in finalizing the edge api.

## 0.13.0

This release removes the need for the config closure when creating a vertex. It is now possible to apply traits and configure
them all within the closure passed to vertex methods.

```groovy
graph.with {
   vertex step1 {
       traits Mapping
       message = 'hello from step1'
       queue = [] as LinkedList
       traits Weight
       weight { queue.size() }
   }
}
```

This change represents a major step in finalizing the vertex api.

## 0.12.0

This release introduces a new way of creating a Vertex. The new methods allow vertices to be created without using strings for names.
Vertices can now be added with the following syntax.

```groovy
graph.with {
    vertex step1
    vertex step2(traits:Mapping)
    vertex step3 {
        traits Mapping
        config {
            label = 'step3'
        }
    }
    vertex step4(traits:Mapping) {
        config {
            label = 'step4'
        }
    }
}
```

This was achieve by adding propertyMissing and methodMissing to Graph which return a VertexSpec. A vertex(VertexSpec) method was
added to allow this syntax.

## 0.11.0

Removing connectsTo from VertexSpec and replacing it with edgesFirst and edgesSecond. This helps in creating directed graphs
using the vertex methods. It also helps when creating vertices first then applying the DirectedGraphPlugin.

## 0.10.3

Publishing of gh-pages was still broken. This release fixed the issue. Also added reports to gh-pages so we can now view
test results, code coverage, and static analysis.

## 0.10.2

Fixed publishing of gh-pages.

## 0.10.1

Release to maven central for 0.10.0 failed. Upgraded gradle-nexus-staging-plugin which fixed the problem.

## 0.10.0

This release features breadth first search methods. These are methods that follow the standard collection methods
that come with groovy: eachBfs, findBfs, findAllBfs, injectBfs, and collectBfs. There are two overrides for each.
The first takes a closure and starts at the first vertex in the vertices map. The second can specify the root to
start at in the breadth first traversal. Here is a short example using findBfs:

```groovy
findBfs {
    it.work > 0
}
```

Finds the first vertex with `work > 0`

```groovy
findBfs('step4') {
    it.work > 0
}
```

Finds the first vertex with `work > 0` starting at step4.

## 0.9.0

Added reversePostOrder to DirectedGraphPlugin. This will allow a DirectedGraph to perform a
closure on each vertex in reverse post order (topological order). Also added reversePostOrderSort which
returns the vertex names in reverse post order of a graph.

DirectedGraphPlugin was reorganized making it possible to javadoc methods dynamically added to the graph.
The methods added are static in DirectedGraphPlugin. They are added to Graph's metaClass using method pointers
while currying the graph param to the apply method. This could be used in future plugins.

## 0.8.2

* fixing publishing of javadoc and groovydoc. gh-pages branch must exist.

## 0.8.1

* Automatically publishing javadoc and groovydoc to gh-pages.

## 0.8.0

This is a major update to the api. It focuses on the edge and vertex methods in Graph. These methods
provide developers with the flexibility to create and configure the objects as they see fit. All edge
and vertex methods return the object which allows the object to be configured with the `<<` operator.
The methods use *Spec objects to store specifications for the edge or vertex and then applies it to the
graph. See the groovydoc for more details.

* upgraded to groovy-2.4.10
* Adding edge and vertex methods to Graph which provides many new ways to manipulate a graph.
* Vertices can be renamed and edges can be moved to different vertices.
* Anytime a vertex is referenced it will be created.

## 0.7.0

* Added support for classifying edges. This can be used to detect cycles in a graph.
* Added test coverage.
* Added javadoc.

## 0.6.0

* fixed issue with logging when optimizations are turned off
* updating gradle to 3.4.1
* fixed many codenarc issues
* added support for breadth first traversal

## 0.5.1

* Added warning when optimizations are turned off
* Adjusting retry time for nexus-staging-plugin

## 0.5.0

* Fixed vertex and edge methods so they can modify existing objects
* Added entry point to dsl though graph method
* Added trait key to vertex and edge map. This makes it easier to add a single trait.

## 0.4.4

* Following suggestions from [#5](https://github.com/Codearte/gradle-nexus-staging-plugin/issues/5) to fix nexus release issue

## 0.4.3

* Adding fix for closeAndPromoteRepository

## 0.4.2

* Switched from maven-publish to maven plugin

## 0.4.1

* Fixes for maven-publish plugin.

## 0.4.0

* Artifacts publish to nexus automatically

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
