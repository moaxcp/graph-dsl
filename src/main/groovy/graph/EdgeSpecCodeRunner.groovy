package graph

class EdgeSpecCodeRunner {
    private Graph graph
    private Edge edge

    Graph getGraph() {
        graph
    }

    Edge getEdge() {
        edge
    }

    void renameOne(String renameOne) {
        EdgeSpec spec = EdgeSpec.newInstance(one:edge.one, two:edge.two, renameOne:renameOne)
        spec.apply(graph)
    }

    void renameTwo(String renameTwo) {
        EdgeSpec spec = EdgeSpec.newInstance(one:edge.one, two:edge.two, renameTwo:renameTwo)
        spec.apply(graph)
    }

    void traits(Class... traits) {
        EdgeSpec spec = EdgeSpec.newInstance(one:edge.one, two:edge.two, traits:traits)
        spec.apply(graph)
    }

    def methodMissing(String name, args) {
        edge.invokeMethod(name, args)
    }

    def propertyMissing(String name) {
        edge[name]
    }

    def propertyMissing(String name, value) {
        if(name == 'one') {
            throw new MissingPropertyException('Cannot set one in dsl. Consider using renameOne method')
        }
        if(name == 'two') {
            throw new MissingPropertyException('Cannot set two in dsl. Consider using renameTwo method')
        }
        edge[name] = value
    }

    void runCode(@DelegatesTo(EdgeSpecCodeRunner) Closure closure) {
        closure.delegate = this
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
    }
}
