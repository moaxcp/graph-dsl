package dsl

import graph.Graph
import graph.plugin.EdgeMapPlugin
import spock.lang.Specification

import static graph.Graph.graph

class PluginMethods extends Specification {

    def 'can apply plugin with Class'() {
        given:
        Graph graph = graph {
            apply EdgeMapPlugin
        }

        expect:
        graph.plugins.contains(EdgeMapPlugin)
    }

    def 'can apply plugin with String'() {
        given:
        Graph graph = graph {
            apply 'edge-map'
        }

        expect:
        graph.plugins.contains(EdgeMapPlugin)
    }
}
