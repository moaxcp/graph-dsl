package graph

import spock.lang.Specification

class GraphTraversalMethodsSpec extends Specification {

    def 'can get correct first unvisited vertex'() {
        setup:
        def graph = new Graph()
        graph.vertex 'step1'
        def colors = [:]

        when:
        def name = graph.getUnvisitedVertexName(colors)

        then:
        name == 'step1'
    }

    def 'can get correct first unvisited white vertex'() {
        setup:
        def graph = new Graph()
        graph.vertex 'step1'
        def colors = ['step1': graph.Graph.TraversalColor.WHITE]

        when:
        def name = graph.getUnvisitedVertexName(colors)

        then:
        name == 'step1'
    }

    def 'can get correct second unvisited vertex'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
        }
        def colors = ['step1': graph.Graph.TraversalColor.GREY]

        when:
        def name = graph.getUnvisitedVertexName(colors)

        then:
        name == 'step2'
    }

    def 'can get unvisited child right'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            edge 'step1', 'step2'
            edge 'step1', 'step3'
        }
        def colors = [
                'step1': graph.Graph.TraversalColor.GREY,
                'step2': graph.Graph.TraversalColor.GREY
        ]

        when:
        def childName = graph.getUnvisitedChildName('step1', colors)

        then:
        childName == 'step3'
    }

    def 'can get unvisited child edge.two is parent'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            edge 'step2', 'step1'
            edge 'step3', 'step1'
        }
        def colors = [
                'step1': graph.Graph.TraversalColor.GREY,
                'step2': graph.Graph.TraversalColor.GREY
        ]

        when:
        def childName = graph.getUnvisitedChildName('step1', colors)

        then:
        childName == 'step3'
    }

    def 'can get unvisited child right reversed edge for left'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            edge 'step2', 'step1'
            edge 'step1', 'step3'
        }
        def colors = [
                'step1': graph.Graph.TraversalColor.GREY,
                'step2': graph.Graph.TraversalColor.GREY
        ]

        when:
        def childName = graph.getUnvisitedChildName('step1', colors)

        then:
        childName == 'step3'
    }

    def 'can get unvisited child right. left child is black'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            edge 'step1', 'step2'
            edge 'step1', 'step3'
        }
        def colors = [
                'step1': graph.Graph.TraversalColor.BLACK,
                'step2': graph.Graph.TraversalColor.BLACK
        ]

        when:
        def childName = graph.getUnvisitedChildName('step1', colors)

        then:
        childName == 'step3'
    }

    def 'can get unvisited child left'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            edge 'step1', 'step2'
            edge 'step1', 'step3'
        }
        def colors = [
                'step1': graph.Graph.TraversalColor.GREY,
                'step3': graph.Graph.TraversalColor.GREY
        ]

        when:
        def childName = graph.getUnvisitedChildName('step1', colors)

        then:
        childName == 'step2'
    }

    def 'can get no unvisited child'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
            edge 'step1', 'step2'
        }
        def colors = [
                'step1': graph.Graph.TraversalColor.GREY,
                'step2': graph.Graph.TraversalColor.GREY
        ]

        when:
        def childName = graph.getUnvisitedChildName('step1', colors)

        then:
        childName == null

    }
}
