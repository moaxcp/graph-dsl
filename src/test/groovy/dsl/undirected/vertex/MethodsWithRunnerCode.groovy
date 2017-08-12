package dsl.undirected.vertex

import graph.Graph
import graph.trait.Mapping
import spock.lang.Specification

import static graph.Graph.graph

class MethodsWithRunnerCode extends Specification {

    def 'add a vertex with runnerCode in Map'() {
        given:
        def executed = false
        Graph graph = graph {
            vertex([
                    name:'A',
                    runnerCode:{executed = true }
            ])
        }

        expect:
        graph.vertices.size() == 1
        graph.vertices.A.name == 'A'
        executed
    }
}
