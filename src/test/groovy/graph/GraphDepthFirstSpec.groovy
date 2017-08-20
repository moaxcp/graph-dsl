package graph

import spock.lang.Specification

public class GraphDepthFirstSpec extends Specification {


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
                'step1': graph.Graph.TraversalColor.WHITE,
                'step2': graph.Graph.TraversalColor.WHITE,
                'step3': graph.Graph.TraversalColor.WHITE
        ]
    }

    def 'can depthFirstTraversalSpec custom'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
        }
        Closure c = {
            root = 'step1'
            colors = ['step1' : graph.Graph.TraversalColor.WHITE]
            preorder {
                //do nothing
            }
            postorder {
                //do nothing
            }
        }

        when:
        def spec = graph.depthFirstTraversalSpec(c)

        then:
        spec.root == 'step1'
        spec.colors == ['step1' : graph.Graph.TraversalColor.WHITE]
        spec.preorder != null
        spec.postorder != null
    }

    def 'can depthFirstTraversalSpec custom with VertexNameSpec'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
        }
        Closure c = {
            root new NameSpec(name:'step1')
            colors = ['step1' : graph.Graph.TraversalColor.WHITE]
            preorder {
                //do nothing
            }
            postorder {
                //do nothing
            }
        }

        when:
        def spec = graph.depthFirstTraversalSpec(c)

        then:
        spec.root == 'step1'
        spec.colors == ['step1' : graph.Graph.TraversalColor.WHITE]
        spec.preorder != null
        spec.postorder != null
    }

    def 'can depthFirstTraversalSpec'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
        }

        when:
        def spec = graph.depthFirstTraversalSpec {
            preorder {
                //do nothing
            }
            postorder {
                //do nothing
            }
        }

        then:
        spec.root == 'step1'
        spec.colors == ['step1' : graph.Graph.TraversalColor.WHITE]
        spec.preorder != null
        spec.postorder != null
    }

    def 'depthFirstTraversalConnected preorder STOP'() {
        setup:
        def graph = new Graph()
        graph.vertex 'step1'
        graph.vertex 'step2'
        graph.vertex 'step3'
        graph.vertex 'step4'
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step4'
        graph.edge 'step2', 'step3'

        def preorderList = []
        def postorderList = []

        def spec = new DepthFirstTraversalSpec()
        spec.root = 'step1'
        spec.colors = graph.makeColorMap()
        spec.preorder { vertex ->
            preorderList << vertex.name
            if(vertex.name == 'step2') {
                Graph.Traversal.STOP
            }
        }
        spec.postorder { vertex ->
            postorderList << vertex.name
        }

        when:
        def traversal = graph.depthFirstTraversalConnected spec

        then:
        traversal == Graph.Traversal.STOP
        spec.colors == [
                'step1': graph.Graph.TraversalColor.GREY,
                'step2': graph.Graph.TraversalColor.GREY,
                'step3': graph.Graph.TraversalColor.WHITE,
                'step4': graph.Graph.TraversalColor.WHITE
        ]
        preorderList == ['step1', 'step2']
        postorderList == []
    }

    def 'depthFirstTraversalConnected preorder'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            vertex 'step4'
            vertex 'step5'
            vertex 'step6'
            edge 'step1', 'step2'
            edge 'step1', 'step3'
            edge 'step1', 'step4'
            edge 'step2', 'step3'
            edge 'step4', 'step3'
            edge 'step3', 'step5'
            edge 'step4', 'step6'
            edge 'step6', 'step5'


        }

        def preorderList = []

        def spec = new DepthFirstTraversalSpec()
        spec.root = 'step1'
        spec.colors = graph.makeColorMap()
        spec.preorder { vertex ->
            preorderList << vertex.name
        }

        when:
        def traversal = graph.depthFirstTraversalConnected spec

        then:
        traversal != Graph.Traversal.STOP
        spec.colors == [
                'step1': graph.Graph.TraversalColor.BLACK,
                'step2': graph.Graph.TraversalColor.BLACK,
                'step3': graph.Graph.TraversalColor.BLACK,
                'step4': graph.Graph.TraversalColor.BLACK,
                'step5': graph.Graph.TraversalColor.BLACK,
                'step6': graph.Graph.TraversalColor.BLACK
        ]
        preorderList == ['step1', 'step2', 'step3', 'step4', 'step6', 'step5']
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

        def postorderList = []

        def spec = new DepthFirstTraversalSpec()
        spec.root = 'step1'
        spec.colors = graph.makeColorMap()
        spec.postorder { vertex ->
            postorderList << vertex.name
        }

        when:
        def traversal = graph.depthFirstTraversalConnected spec

        then:
        traversal != Graph.Traversal.STOP
        spec.colors == [
                'step1': graph.Graph.TraversalColor.BLACK,
                'step2': graph.Graph.TraversalColor.BLACK,
                'step3': graph.Graph.TraversalColor.BLACK
        ]
        postorderList == ['step2', 'step3', 'step1']
    }

    def 'depthFirstTraversalConnected postorder STOP'() {
        setup:
        def graph = new Graph()
        graph.vertex 'step1'
        graph.vertex 'step2'
        graph.vertex 'step3'
        graph.vertex 'step4'
        graph.edge 'step1', 'step2'
        graph.edge 'step1', 'step4'
        graph.edge 'step2', 'step3'

        def postorderList = []
        def preorderList = []

        def spec = new DepthFirstTraversalSpec()
        spec.root = 'step1'
        spec.colors = graph.makeColorMap()
        spec.postorder { vertex ->
            postorderList << vertex.name
            if(vertex.name == 'step2') {
                Graph.Traversal.STOP
            }
        }
        spec.preorder { vertex ->
            preorderList << vertex.name
        }

        when:
        def traversal = graph.depthFirstTraversalConnected spec

        then:
        traversal == Graph.Traversal.STOP
        spec.colors == [
                'step1': graph.Graph.TraversalColor.GREY,
                'step2': graph.Graph.TraversalColor.BLACK,
                'step3': graph.Graph.TraversalColor.BLACK,
                'step4': graph.Graph.TraversalColor.WHITE
        ]
        postorderList == ['step3', 'step2']
        preorderList == ['step1', 'step2', 'step3']
    }

    def 'can depthFirstTraversal with spec'() {
        setup:
        def graph = new Graph()

        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            vertex 'step4'
            edge 'step1', 'step2'
            edge 'step3', 'step4'
        }

        def spec = graph.depthFirstTraversalSpec {
            preorder {

            }
            postorder {

            }
        }

        when:
        graph.traversal(graph.&depthFirstTraversalConnected, spec)

        then:
        spec.colors == [
                'step1': graph.Graph.TraversalColor.BLACK,
                'step2': graph.Graph.TraversalColor.BLACK,
                'step3': graph.Graph.TraversalColor.BLACK,
                'step4': graph.Graph.TraversalColor.BLACK
        ]
    }

    def 'can depthFirstTraversal STOP with spec'() {
        setup:
        def graph = new Graph()

        graph.with {
            vertex 'step1'
            vertex 'step2'
            vertex 'step3'
            vertex 'step4'
            edge 'step1', 'step2'
            edge 'step3', 'step4'
        }

        def spec = graph.depthFirstTraversalSpec {
            preorder { vertex ->
                if(vertex.name == 'step2') {
                    return Graph.Traversal.STOP
                }
            }
            postorder { vertex ->

            }
        }

        when:
        def traversal = graph.traversal(graph.&depthFirstTraversalConnected, spec)

        then:
        traversal == Graph.Traversal.STOP
        spec.colors == [
                'step1': graph.Graph.TraversalColor.GREY,
                'step2': graph.Graph.TraversalColor.GREY,
                'step3': graph.Graph.TraversalColor.WHITE,
                'step4': graph.Graph.TraversalColor.WHITE
        ]
    }

    def 'edgeClassification'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'A'
            vertex 'B'
            edge 'A', 'B'
        }

        def fromNames = []
        def toNames = []
        def colors = []
        def edges = []

        def spec = graph.depthFirstTraversalSpec {
            root = 'A'
            classifyEdge { edge, from, to, toColor ->
                fromNames << from
                toNames << to
                colors << toColor
                edges << edge
            }
        }

        when:
        graph.depthFirstTraversalConnected spec

        then:
        fromNames[0] == 'A'
        toNames[0] == 'B'
        colors[0] == Graph.TraversalColor.WHITE
        edges[0] == graph.edge('A', 'B')

        fromNames[1] == 'B'
        toNames[1] == 'A'
        colors[1] == Graph.TraversalColor.GREY
        edges[1] == graph.edge('A', 'B')
    }

    def 'can STOP classifyEdge'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'A'
            vertex 'B'
            vertex 'C'
            edge 'A', 'B'
            edge 'B', 'C'
        }

        def spec = graph.depthFirstTraversalSpec {
            root = 'A'
            classifyEdge { edge, from, to, toColor ->
                if(from == 'B' && to == 'C') {
                    return Graph.Traversal.STOP
                }
            }
        }

        when:
        def traversal = graph.depthFirstTraversalConnected spec

        then:
        traversal == Graph.Traversal.STOP
    }
}
