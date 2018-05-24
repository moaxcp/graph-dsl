package graph

import static graph.TraversalColor.WHITE
import static graph.TraversalState.CONTINUE
import static graph.TraversalState.STOP

trait TraversalDsl {

    /**
     * Performs a pre-order depth first traversal of the graph calling action on each {@link Vertex} returning results.
     * <p>
     * Returned map contains:
     * <dl>
     *     <dt>root</dt>
     *     <dd>ending root of traversal</dd>
     *     <dt>colors</dt>
     *     <dd>ending colors of traversal</dd>
     *     <dt>state</dt>
     *     <dd>ending state of traversal</dd>
     * </dl>
     * <p>
     * Values returned can be used to restart traversal.
     * @param root  starting {@Vertex} key
     * @param colors  of graph
     * @param action  action to perform on each {@link Vertex}
     * @return results map
     */
    Map preOrder(Object root = null, Map<Object, TraversalColor> colors = null, Closure action) {
        return traversal(TraversalAlgorithms.&preOrderTraversal, root, colors, action).subMap(['root', 'state', 'colors'])
    }

    /**
     * Performs a full traversal with the given {@code algorithm} on all components of this {@link Graph}.
     * {@code algorithm} must accept the following parameters.
     * <p>
     * {@code algorithm} must meet the following:
     *     <ul>
     *         <li>each call to {@code algorithm} must visit part of the graph by updating {@code spec.colors}. This
     *         will eventually cause calls to {@link Graph#getUnvisitedVertexKey} to return null which signals the end
     *         of the traversal.</li>
     *         <li>must call action on each vertex or edge</li>
     *         <li>must stop when action returns {@link TraversalState#STOP}</li>
     *         <li>must throw exception when action does not return a {@link TraversalState}</li>
     *     </ul>
     * </p>
     *
     * @param algorithm  type of traversal to run on each component
     * @param root  root of component to start traversal
     * @param colors  starting set of colors. Will not be modified.
     * @param action  action to perform by algorithm (on each edge or vertex)
     * @return results containing current root, roots of all components, colors, and results added by algorithm
     */
    Map traversal(Closure algorithm, Object root, Map<Object, TraversalColor> colors, Closure action) {
        if(root && !getVertex(root)) {
            throw new IllegalArgumentException("$root not found in vertices")
        }
        Map spec = [:]
        spec.colors = [:]
        spec.colors.putAll(colors ?: makeColorMap())
        spec.root = root
        if(!spec.root) {
            spec.root = getUnvisitedVertexKey((Map) spec.colors)
        }
        spec.roots = [] as Set
        spec.components = [] as Set
        while (spec.root) {
            spec.roots << spec.root
            spec.componentRoot = spec.root
            spec.components << spec.root
            spec.state = algorithm.call(this, spec, action)
            if (spec.state == STOP) {
                return spec
            }
            spec.root = getUnvisitedVertexKey((Map) spec.colors)
        }
        spec
    }

    /**
     * Returns edges to be traversed for a given {@link Vertex} key.
     * @param key
     * @return edges to traverse
     */
    Set<? extends Edge> traverseEdges(Object key) {
        type.traverseEdges(key)
    }

    /**
     * Creates and returns a color map in the form of name:color where name is the vertex name and color is
     * TraversalColor.WHITE.
     * @return color map
     */
    Map<Object, TraversalColor> makeColorMap() {
        vertices.collectEntries { key, vertex ->
            [(key): WHITE]
        } as Map<Object, TraversalColor>
    }

    /**
     * Performs a topological sort of the graph returning a list of keys of each {@link Vertex} in topological order.
     * @param root  starting {@link Vertex}
     * @return list of keys of each {@link Vertex} in topological order
     */
    List topologicalSort(Object root = null) {
        List<Object> sorted = []
        postOrder(root) {
            sorted << it.key
            CONTINUE
        }
        sorted.reverse()
    }

    /**
     * Performs a post-order depth first traversal of the graph calling action on each {@link Vertex} returning results.
     * <p>
     * Returned map contains:
     * <dl>
     *     <dt>root</dt>
     *     <dd>ending root of traversal</dd>
     *     <dt>colors</dt>
     *     <dd>ending colors of traversal</dd>
     *     <dt>state</dt>
     *     <dd>ending state of traversal</dd>
     * </dl>
     * <p>
     * Values returned can be used to restart traversal.
     * @param root  starting {@Vertex} key
     * @param colors  of graph
     * @param action  action to perform on each {@link Vertex}
     * @return results map
     */
    Map postOrder(Object root = null, Map<Object, TraversalColor> colors = null, Closure action) {
        return traversal(TraversalAlgorithms.&postOrderTraversal, root, colors, action).subMap(['root', 'state', 'colors'])
    }

    /**
     * Performs a reverse post-order depth first traversal of the graph calling action on each {@link Vertex} returning
     * results.
     * <p>
     * Returned map contains:
     * <dl>
     *     <dt>state</dt>
     *     <dd>ending state of traversal</dd>
     * </dl>
     * @param root  starting {@Vertex} key
     * @param action  action to perform on each {@link Vertex}
     * @return results map
     */
    Map reversePostOrder(Object root = null, Closure action) {
        List<Vertex> sorted = []
        Map result = postOrder(root) { Vertex it ->
            sorted << it
            CONTINUE
        }
        sorted = sorted.reverse()
        sorted.find {
            result.state = action(it)
            result.state == STOP
        }
        result.subMap(['state'])
    }

    /**
     * Performs a pre-order depth first traversal of the graph calling action for each edge returning results.
     * <p>
     * Returned map contains:
     * <dl>
     *     <dt>root</dt>
     *     <dd>ending root of traversal</dd>
     *     <dt>colors</dt>
     *     <dd>ending colors of traversal</dd>
     *     <dt>state</dt>
     *     <dd>ending state of traversal</dd>
     * </dl>
     * <p>
     * Values returned can be used to restart traversal.
     * <p>
     * {@code action} params are:
     * <dl>
     *     <dt>Object from</dt>
     *     <dd>from vertex key</dd>
     *     <dt>Object to</dt>
     *     <dd>to vertex key</dd>
     *     <dt>EdgeType type</dt>
     *     <dd>type of edge</dd>
     * </dl>
     * @param root  starting {@Vertex} key
     * @param colors  of graph
     * @param action  action to perform
     * @return results map
     */
    Map classifyEdges(Object root = null, Map<Object, TraversalColor> colors = null, Closure action) {
        return traversal(TraversalAlgorithms.&classifyEdgesTraversal, root, colors, action).subMap('root', 'colors', 'state')
    }

    Map connectedComponent(Object root = null, Map<Object, TraversalColor> colors = null, Closure action) {
        return traversal(TraversalAlgorithms.&connectedComponentTraversal, root, colors, action).subMap('root', 'colors', 'state')
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
     * Performs a breadth first traversal on each component of the Graph calling action on each {@link Vertex}
     * returning results.
     * <p>
     * Returned map contains:
     * <dl>
     *     <dt>root</dt>
     *     <dd>ending root of traversal</dd>
     *     <dt>colors</dt>
     *     <dd>ending colors of traversal</dd>
     *     <dt>state</dt>
     *     <dd>ending state of traversal</dd>
     * </dl>
     * <p>
     * Values returned can be used to restart traversal.
     * @param root  starting {@Vertex} key
     * @param colors  of graph
     * @param action  action to perform on each {@link Vertex}
     * @return results map
     */
    Map breadthFirstTraversal(Object root = null, Map<Object, TraversalColor> colors = null, Closure action) {
        traversal(TraversalAlgorithms.&breadthFirstTraversal, root, colors, action)
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
        Closure inject
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