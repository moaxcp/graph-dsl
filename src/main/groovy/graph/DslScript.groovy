package graph

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
