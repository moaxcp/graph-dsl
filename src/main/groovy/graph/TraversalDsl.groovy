package graph

import static TraversalState.*
import static graph.TraversalColor.WHITE

trait TraversalDsl {

    Set<? extends Edge> traverseEdges(Object key) {
        type.traverseEdges(key)
    }

    /**
     * Creates and returns a color map in the form of name:color where name is the vertex name and color is
     * TraversalColor.WHITE.
     * @return
     */
    Map<Object, TraversalColor> makeColorMap() {
        vertices.collectEntries { key, vertex ->
            [(key): WHITE]
        } as Map<Object, TraversalColor>
    }

    Map preOrder(Object root = null, Map<Object, TraversalColor> colors = null, Closure action) {
        return traversal(TraversalAlgorithms.&preOrderTraversal.curry(this), root, colors, action)
    }

    /**
     * Performs a full traversal with the given algorithm on all components of the graph. This method calls algorithm
     * on root and continues to call algorithm until all vertices are black. To stop the traversal early action may
     * return TraversalState.STOP.
     * @param algorithm  type of traversal to run on each component
     * @param root  root of component to start traversal
     * @param colors  starting set of colors. Will not be modified.
     * @param action  action to perform by algorithm (on each edge or vertex)
     * @return results containing current root, roots of all components, colors, and results added by algorithm
     */
    Map traversal(Closure algorithm, Object root, Map<Object, TraversalColor> colors, Closure action) {
        if(root && !this.getVertex(root)) {
            throw new IllegalArgumentException("$root not found in vertices")
        }
        Map results = [:]
        results.colors = [:]
        results.colors.putAll(colors ?: makeColorMap())
        results.root = root
        if(!results.root) {
            results.root = getUnvisitedVertexKey((Map) results.colors)
        }
        results.roots = [] as Set
        while (results.root) {
            results.roots << results.root
            results.traversalRoot = results.root
            results.state = algorithm.call(results, action)
            if (results.state == STOP) {
                return results
            }
            results.root = getUnvisitedVertexKey((Map) results.colors)
        }
        results
    }

    Map postOrder(Object root = null, Map<Object, TraversalColor> colors = null, Closure action) {
        return traversal(TraversalAlgorithms.&postOrderTraversal.curry(this), root, colors, action)
    }

    List topologicalSort(Object root = null) {
        List<Object> sorted = []
        postOrder(root) {
            sorted << it.key
            CONTINUE
        }
        sorted.reverse()
    }

    Map reversePostOrder(Object root = null, Map<Object, TraversalColor> colors = null, Closure action) {
        List<Object> sorted = []
        Map result = postOrder(root, colors) {
            sorted << it.key
            CONTINUE
        }
        sorted = sorted.reverse()
        sorted.each {
            action(vertices[it])
        }
        result
    }

    Map classifyEdges(Object root = null, Map<Object, TraversalColor> colors, Closure action) {
        return traversal(TraversalAlgorithms.&classifyEdgesTraversal.curry(this), root, colors, action)
    }

    /**
     * Performs a breadth first traversal on each component of the Graph starting at root.
     * <p>
     *     It is possible to stop the traversal early by returning TraversalState.STOP from closure.
     * @param root optional root to start traversal.
     * @param closure Closure to perform on each vertex
     * @return resulting TraversalState value
     */
    Map breadthFirstTraversal(Object root = null, Map<Object, TraversalColor> colors = null, Closure action) {
        traversal(TraversalAlgorithms.&breadthFirstTraversal.curry(this), root, colors, action)
    }

    /**
     * executes closure on each {@link Vertex} in breadth first order starting at the given root {@link Vertex}. See
     * {@link #breadthFirstTraversal} for details.
     * @param root vertex to start breadth first traversal
     * @param closure execute on each {@link Vertex}
     */
    void eachBfs(String root = null, Closure closure) {
        breadthFirstTraversal(root) { vertex ->
            closure(vertex)
            CONTINUE
        }
    }

    /**
     * Executes closure on each {@link Vertex} in breadth first order starting at root. If the closure returns true the
     * {@link Vertex} is returned.
     * @param root vertex to start breadth first traversal
     * @param closure execute on each {@link Vertex}
     * @return first {@link Vertex} where closure returns true
     */
    Vertex findBfs(String root = null, Closure closure) {
        Vertex result = null
        breadthFirstTraversal(root) { vertex ->
            if (closure(vertex)) {
                result = vertex
                return STOP
            }
            CONTINUE
        }
        result
    }

    /**
     * Executes closure on each vertex in breadth first order starting at root. object is the initial value passed to
     * the closure. Each returned value from the closure is passed to the next call.
     * @param root vertex to start breadth first traversal
     * @param object initial value passed to the closure
     * @param closure execute on each {@link Vertex}
     * @return object returned from the final call to closure.
     */
    Object injectBfs(String root = null, Object object, Closure closure) {
        Object result = object
        breadthFirstTraversal(root) { vertex ->
            result = closure(result, vertex)
            CONTINUE
        }
        result
    }

    /**
     * Runs closure on each vertex in breadth first order starting at root. The vertices where closure returns true are
     * returned.
     * @param root vertex to start breadth first traversal
     * @param closure execute on each {@link Vertex}
     * @return the vertices where closure returns true
     */
    List<? extends Vertex> findAllBfs(String root = null, Closure closure) {
        Closure inject = null
        if (root) {
            inject = this.&injectBfs.curry(root)
        } else {
            inject = this.&injectBfs
        }
        (List<? extends Vertex>) inject([]) { result, vertex ->
            if (closure(vertex)) {
                result << vertex
            }
            result
        }
    }

    /**
     * Runs closure on each vertex in breadth first order, starting at root, collecting returned values from the
     * closure.
     * @param root vertex to start breadth first traversal
     * @param closure execute on each {@link Vertex}
     * @return values returned from each execution of closure
     */
    List<?> collectBfs(String root = null, Closure closure) {
        Closure inject = null
        if (root) {
            inject = this.&injectBfs.curry(root)
        } else {
            inject = this.&injectBfs
        }
        inject.call([] as List<? extends Vertex>) { result, vertex ->
            result << closure(vertex)
        }
    }
}