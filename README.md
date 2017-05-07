# graph-dsl

A groovy dsl for creating and traversing graphs. 
For project build status check the [wiki](https://github.com/moaxcp/graph-dsl/wiki).

# Usage

graph-dsl is used to create graphs and algorithms for graphs.
It is designed to be groovy, using closures and metaprogramming for minimal setup.

## Building a graph

```groovy
#!/usr/bin/env groovy
@Grab(group='com.github.moaxcp', module='graph-dsl', version='0.11.0')
import static graph.Graph.graph

graph {
    apply DirectedGraphPlugin
    vertex a {
        edgesFirst 'b', 'd'
        edgesSecond 'd'
    }
    
    vertex renameMe {
        rename 'b'
        edgesFirst 'c', 'd'
    }
    
    vertex d([edgesFirst:'c']) {
        edgesFirst 'e'
    }
    
    edge 'f', 'g'
    edge 'g', 'd'
    
    eachBfs {
        println it.name
    }
}
```

# Getting Started

## install git

https://git-scm.com/book/en/v2/Getting-Started-Installing-Git

## install gitflow-avh

https://github.com/petervanderdoes/gitflow-avh/wiki/Installation

## clone the project

git clone https://github.com/moaxcp/graph-dsl.git

## build

`./gradlew build`

# Contributing

Contributions are welcome. Please submit a pull request to the develop branch in github.

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
* [sonarqube.com](https://sonarqube.com/dashboard?id=com.github.moaxcp%3Agraph-dsl)
* [travis-ci.org](https://travis-ci.org/moaxcp/graph-dsl)
* [oss sonatype](https://oss.sonatype.org/#welcome)

# Releases

## x.x.x

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
