package graph

class EdgeWeightPlugin implements Plugin {

    @Override
    void apply(Graph graph) {
        graph.edgeTraits(Weight)

        Comparator comparator = { a, b ->
            a.weight <=> b.weight
        }

        graph.replaceEdgesSet(new TreeSet<>(comparator))
    }
}
