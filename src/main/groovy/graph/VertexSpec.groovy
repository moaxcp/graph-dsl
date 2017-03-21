package graph

import groovy.transform.PackageScope

class VertexSpec {
    private String name
    private Vertex vertex
    private Set<Class> traits = [] as Set<Class>
    private Set<String> connectsTo = [] as Set<String>

    @PackageScope
    Set<Class> getTraits() {
        traits
    }

    @PackageScope
    Set<String> getConnectsTo() {
        connectsTo
    }

    void getName() {
        name
    }

    void getVertex() {
        vertex
    }

    void traits(Class... classes) {
        traits.addAll(classes)
    }

    void connectsTo(String... names) {
        connectsTo.addAll(names)
    }
}
