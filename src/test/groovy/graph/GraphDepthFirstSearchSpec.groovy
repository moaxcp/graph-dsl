package graph

import spock.lang.Specification

public class GraphDepthFirstSearchSpec extends Specification {

    def graph = new Graph()

    def setup() {
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            vertex 'step4'
            edge 'step1', 'step2'
            edge 'step1', 'step3'
            edge 'step4', 'step1'
            edge 'step1', 'step1'
        }
    }

    def 'can get correct first unvisited vertex'() {
        setup:
        def graph = new Graph()
        graph.vertex 'step1'
        def colors = []

        when:
        def name = graph.getUnvisitedVertexName(colors)

        then:
        name == 'step1'
    }

    def 'can get correct first unvisited white vertex'() {
        setup:
        def graph = new Graph()
        graph.vertex 'step1'
        def colors = ['step1': Graph.DepthFirstTraversalColor.WHITE]

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
        def colors = ['step1': Graph.DepthFirstTraversalColor.GREY]

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
                'step1': Graph.DepthFirstTraversalColor.GREY,
                'step2': Graph.DepthFirstTraversalColor.GREY
        ]

        when:
        def childName = graph.getUnvisitedChildName(colors, 'step1')

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
                'step1': Graph.DepthFirstTraversalColor.GREY,
                'step3': Graph.DepthFirstTraversalColor.GREY
        ]

        when:
        def childName = graph.getUnvisitedChildName(colors, 'step1')

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
                'step1': Graph.DepthFirstTraversalColor.GREY,
                'step2': Graph.DepthFirstTraversalColor.GREY
        ]

        when:
        def childName = graph.getUnvisitedChildName(colors, 'step1')

        then:
        childName == null

    }

    def 'can get adjacent edges'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            edge 'step1', 'step2'
            edge 'step1', 'step3'
        }

        when:
        def edges = graph.adjacentEdges('step1')

        then:
        edges.size() == 2
        edges.contains(new Edge(one: 'step1', two: 'step2'))
        edges.contains(new Edge(one: 'step1', two: 'step3'))
    }

    def 'can make color map'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
        }

        when:
        def colors = graph.makeColorMap()

        then:
        colors == [
                'step1': Graph.DepthFirstTraversalColor.WHITE,
                'step2': Graph.DepthFirstTraversalColor.WHITE,
                'step3': Graph.DepthFirstTraversalColor.WHITE
        ]
    }

    def 'depthFirstTraversalConnected preorder STOP'() {
        setup:
        def graph = new Graph()
        graph.vertex 'step1'

        def colors = graph.makeColorMap()
        def preorderList = []

        when:
        def traversal = graph.depthFirstTraversalConnected 'step1', colors, { vertex ->
            preorderList << vertex.name
            Graph.Traversal.STOP
        }, null

        then:
        traversal == Graph.Traversal.STOP
        colors == ['step1': Graph.DepthFirstTraversalColor.WHITE]
        preorderList == ['step1']
    }

    def 'depthFirstTraversalConnected preorder'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            edge 'step1', 'step2'
            edge 'step1', 'step3'
        }

        def colors = graph.makeColorMap()
        def preorderList = []

        when:
        def traversal = graph.depthFirstTraversalConnected 'step1', colors, { vertex ->
            preorderList << vertex.name
        }, null

        then:
        traversal != Graph.Traversal.STOP
        colors == [
                'step1': Graph.DepthFirstTraversalColor.BLACK,
                'step2': Graph.DepthFirstTraversalColor.BLACK,
                'step3': Graph.DepthFirstTraversalColor.BLACK
        ]
        preorderList == ['step1', 'step2', 'step3']
    }

    def 'depthFirstTraversalConnected postorder'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            edge 'step1', 'step2'
            edge 'step1', 'step3'
        }

        def colors = graph.makeColorMap()
        def postorderList = []

        when:
        def traversal = graph.depthFirstTraversalConnected 'step1', colors, null, { vertex ->
            postorderList << vertex.name
        }

        then:
        traversal != Graph.Traversal.STOP
        colors == [
                'step1': Graph.DepthFirstTraversalColor.BLACK,
                'step2': Graph.DepthFirstTraversalColor.BLACK,
                'step3': Graph.DepthFirstTraversalColor.BLACK
        ]
        postorderList == ['step2', 'step3', 'step1']
    }
}
