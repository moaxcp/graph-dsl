package graph

import spock.lang.Specification

public class GraphDepthFirstSearchSpec extends Specification {

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

    def 'can depthFirstTraversalSpec custom'() {
        setup:
        def graph = new Graph()
        graph.with {
            vertex 'step1'
        }
        Closure c = {
            root = 'step1'
            colors = ['step1' : Graph.DepthFirstTraversalColor.WHITE]
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
        spec.colors == ['step1' : Graph.DepthFirstTraversalColor.WHITE]
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
        spec.colors == ['step1' : Graph.DepthFirstTraversalColor.WHITE]
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
        def traversal = graph.depthFirstTraversalConnected 'step1', spec

        then:
        traversal == Graph.Traversal.STOP
        spec.colors == [
                'step1': Graph.DepthFirstTraversalColor.GREY,
                'step2': Graph.DepthFirstTraversalColor.GREY,
                'step3': Graph.DepthFirstTraversalColor.WHITE,
                'step4': Graph.DepthFirstTraversalColor.WHITE
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
        spec.colors = graph.makeColorMap()
        spec.preorder { vertex ->
            preorderList << vertex.name
        }

        when:
        def traversal = graph.depthFirstTraversalConnected 'step1', spec

        then:
        traversal != Graph.Traversal.STOP
        spec.colors == [
                'step1': Graph.DepthFirstTraversalColor.BLACK,
                'step2': Graph.DepthFirstTraversalColor.BLACK,
                'step3': Graph.DepthFirstTraversalColor.BLACK,
                'step4': Graph.DepthFirstTraversalColor.BLACK,
                'step5': Graph.DepthFirstTraversalColor.BLACK,
                'step6': Graph.DepthFirstTraversalColor.BLACK
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
        spec.colors = graph.makeColorMap()
        spec.postorder { vertex ->
            postorderList << vertex.name
        }

        when:
        def traversal = graph.depthFirstTraversalConnected 'step1', spec

        then:
        traversal != Graph.Traversal.STOP
        spec.colors == [
                'step1': Graph.DepthFirstTraversalColor.BLACK,
                'step2': Graph.DepthFirstTraversalColor.BLACK,
                'step3': Graph.DepthFirstTraversalColor.BLACK
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
        def traversal = graph.depthFirstTraversalConnected 'step1', spec

        then:
        traversal == Graph.Traversal.STOP
        spec.colors == [
                'step1': Graph.DepthFirstTraversalColor.GREY,
                'step2': Graph.DepthFirstTraversalColor.BLACK,
                'step3': Graph.DepthFirstTraversalColor.BLACK,
                'step4': Graph.DepthFirstTraversalColor.WHITE
        ]
        postorderList == ['step3', 'step2']
        preorderList == ['step1', 'step2', 'step3']
    }
}
