package graph

trait VertexDsl {

    abstract Vertex vertex(ConfigSpec spec)

    /**
     * Finds the {@link Vertex} with the given id or creates a new one.
     * @param id  the id of the {@link Vertex} to find or create.
     * @return the resulting {@link Vertex}
     * @throws {@link IllegalArgumentException} When id is null or empty.
     */
    Vertex vertex(Object id) {
        if(!id) {
            throw new IllegalArgumentException("Invalid id.")
        }
        ConfigSpec spec = new ConfigSpec(map:[id:id])
        vertex(spec)
    }

    /**
     * Finds or creates all vertices returning them in a Set.
     * @param id  id of first {@link Vertex} to find or create.
     * @param ids  of other vertices to find or create.
     * @return set of created vertices
     */
    Set<Vertex> vertex(Object id, Object... ids) {
        Set<Vertex> set = new LinkedHashSet<>()
        set << vertex(id)
        ids.collect {
            vertex(it)
        }.each {
            set << it
        }
        set
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. The map may contain configuration for the vertex. Default
     * configuration can be:
     * <dl>
     *     <dt>id</dt>
     *     <dd>id of the vertex to create or update</dd>
     *     <dt>changeId</dt>
     *     <dd>new id for the vertex</dd>
     *     <dt>connectsTo</dt>
     *     <dd>list of vertex keys the vertex should connect to. Edges will be created with edge.one equal to the
     *     vertex id and edge.two equals to the 'connectTo' id.</dd>
     * </dl>
     * Additional entries may be added by type of the graph.
     * The map must contain id for this method.
     * @param map  configuration of {@link Vertex}
     * @return the resulting {@link Vertex}
     */
    Vertex vertex(Map<String, ?> map) {
        if(!map.id) {
            throw new IllegalArgumentException("Invalid id.")
        }
        ConfigSpec spec = new ConfigSpec(map:map)
        vertex(spec)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph with the given id. {@code closure} is used to further
     * customize the vertex and graph. In the closure getting and setting properties delegates to the vertex. Methods
     * also delegate to the vertex.
     * <p>
     * Variables accessible within the closure:
     * <dl>
     *     <dt>graph</td>
     *     <dd>{@link Graph} vertex was added to</dd>
     *     <dt>vertex</dt>
     *     <dd>{@link Vertex} added to graph</dd>
     * </dl>
     * <p>
     * By default there are several methods added in the closure.
     * <p>
     * <dl>
     *     <dt>{@code void changeId(Object id)}</dt>
     *     <dd>Changes id of vertex</dd>
     *     <dt>{@code void changeId(NameSpec newName)}</dt>
     *     <dd>renames the vertex using a NameSpec</dd>
     *     <dt>{@code void connectsTo(Object... keys)}</dt>
     *     <dd>Connects the vertex to other vertices. If they do not exist they are created.</dd>
     *     <dt>{@code void connectsTo(ConfigSpec... specs)}</dt>
     *     <dd>Connects the vertex to other vertices. If they do not exist they are created. This method allows for
     *     arbitrarily nested configurations.</dd>
     * </dl>
     * <p>
     * Types may add variables and methods to the passed in closure.
     * @param id  the id of the {@link Vertex} to find or create.
     * @param closure  configuration for graph and vertex
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(Object id, @DelegatesTo(VertexSpecCodeRunner) Closure closure) {
        if(!id) {
            throw new IllegalArgumentException("Invalid id.")
        }
        ConfigSpec spec = new ConfigSpec(map:[id:id], closure:closure)
        vertex(spec)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph with the given id. The map contains configuration described
     * in {@link #vertex(Map)}.
     * @param id  the id of the {@link Vertex} to find or create.
     * @param map  configuration of {@link Vertex}
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(Object id, Map<String, ?> map) {
        if(!id) {
            throw new IllegalArgumentException("Invalid id.")
        }
        map.id = map.id ?: id
        ConfigSpec spec = new ConfigSpec(map:map)
        vertex(spec)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. The map contains configuration described in
     * {@link #vertex(Map)}. The configuration given by the closure is described in {@link #vertex(Object,Closure)}.
     * @param map  configuration of {@link Vertex}
     * @param closure  configuration for graph and vertex
     * @return  The resulting {@link Vertex}.
     */
    Vertex vertex(Map<String, ?> map, @DelegatesTo(VertexSpecCodeRunner) Closure closure) {
        if(!map.id) {
            throw new IllegalArgumentException("Invalid id.")
        }
        ConfigSpec spec = new ConfigSpec(map:map, closure:closure)
        vertex(spec)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. The map contains configuration described in
     * {@link #vertex(Map)}. The configuration given by the closure described in {@link #vertex(Object,Closure)}.
     * @param id  the id of the {@link Vertex} to find or create.
     * @param map  configuration of {@link Vertex}
     * @param closure  configuration for graph and vertex
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(Object id, Map<String, ?> map, @DelegatesTo(VertexSpecCodeRunner) Closure closure) {
        if(!id) {
            throw new IllegalArgumentException("Invalid id.")
        }
        map.id = map.id ?: id
        ConfigSpec spec = new ConfigSpec(map:map, closure:closure)
        vertex(spec)
    }
}