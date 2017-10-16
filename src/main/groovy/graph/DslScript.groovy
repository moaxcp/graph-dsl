package graph

/**
 * Base script for using graph-dsl. This class may be used in scripts to make the dsl part of the script.
 *
 * <pre><code>
 * #!/usr/bin/env groovy
 *
 * @Grapes(
 *     @Grab(group='com.github.moaxcp', module='graph-dsl', version='0.20.0')
 * )
 *
 * import graph.*
 *
 * @groovy.transform.BaseScript DslScript graph
 * edge A, B
 *
 * assert graph.edges.size() == 1
 * assert graph.edges.first().one == 'A'
 * assert graph.edges.first().two == 'B'
 * assert graph.vertices.size() == 2
 * assert graph.vertices.A.name == 'A'
 * assert graph.vertices.B.name == 'B'
 * </code></pre>
 */
abstract class DslScript extends Script {
    Graph graph
    DslScript() {
        graph = new Graph()
    }

    def propertyMissing(String name) {
        graph."$name"
    }

    def propertyMissing(String name, value) {
        graph."$name" = value
    }

    def methodMissing(String name, args) {
        graph.invokeMethod name, args
    }
}
