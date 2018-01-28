package graph

trait VertexDsl {

    abstract Vertex vertex(ConfigSpec spec)

    /**
     * Finds the {@link Vertex} with the given key or creates a new one.
     * @param key  the key of the {@link Vertex} to find or create.
     * @return the resulting {@link Vertex}
     * @throws {@link IllegalArgumentException} When key is null or empty.
     */
    Vertex vertex(Object key) {
        if(!key) {
            throw new IllegalArgumentException("Invalid key.")
        }
        ConfigSpec spec = new ConfigSpec(map:[key:key])
        vertex(spec)
    }

    /**
     * Finds or creates all vertices returning them in a Set.
     * @param key  key of first {@link Vertex} to find or create.
     * @param keys  of other vertices to find or create.
     * @return set of created vertices
     */
    Set<Vertex> vertex(Object key, Object... keys) {
        Set<Vertex> set = new LinkedHashSet<>()
        set << vertex(key)
        keys.collect {
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
     *     <dt>key</dt>
     *     <dd>key of the vertex to create or update</dd>
     *     <dt>changeKey</dt>
     *     <dd>new key for the vertex</dd>
     *     <dt>connectsTo</dt>
     *     <dd>list of vertex keys the vertex should connect to. Edges will be created with edge.one equal to the
     *     vertex key and edge.two equals to the 'connectTo' key.</dd>
     * </dl>
     * Additional entries may be added by type of the graph.
     * The map must contain key for this method.
     * @param map  configuration of {@link Vertex}
     * @return the resulting {@link Vertex}
     */
    Vertex vertex(Map<String, ?> map) {
        if(!map.key) {
            throw new IllegalArgumentException("Invalid key.")
        }
        ConfigSpec spec = new ConfigSpec(map:map)
        vertex(spec)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph with the given key. {@code closure} is used to further
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
     *     <dt>{@code void changeKey(Object key)}</dt>
     *     <dd>Changes key of vertex</dd>
     *     <dt>{@code void changeKey(NameSpec newName)}</dt>
     *     <dd>renames the vertex using a NameSpec</dd>
     *     <dt>{@code void connectsTo(Object... keys)}</dt>
     *     <dd>Connects the vertex to other vertices. If they do not exist they are created.</dd>
     *     <dt>{@code void connectsTo(ConfigSpec... specs)}</dt>
     *     <dd>Connects the vertex to other vertices. If they do not exist they are created. This method allows for
     *     arbitrarily nested configurations.</dd>
     * </dl>
     * <p>
     * Types may add variables and methods to the passed in closure.
     * @param key  the key of the {@link Vertex} to find or create.
     * @param closure  configuration for graph and vertex
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(Object key, Closure closure) {
        if(!key) {
            throw new IllegalArgumentException("Invalid key.")
        }
        ConfigSpec spec = new ConfigSpec(map:[key:key], closure:closure)
        vertex(spec)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph with the given key. The map contains configuration described
     * in {@link #vertex(Map)}.
     * @param key  the key of the {@link Vertex} to find or create.
     * @param map  configuration of {@link Vertex}
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(Object key, Map<String, ?> map) {
        if(!key) {
            throw new IllegalArgumentException("Invalid key.")
        }
        map.key = map.key ?: key
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
    Vertex vertex(Map<String, ?> map, Closure closure) {
        if(!map.key) {
            throw new IllegalArgumentException("Invalid key.")
        }
        ConfigSpec spec = new ConfigSpec(map:map, closure:closure)
        vertex(spec)
    }

    /**
     * Creates or updates a {@link Vertex} in this graph. The map contains configuration described in
     * {@link #vertex(Map)}. The configuration given by the closure described in {@link #vertex(Object,Closure)}.
     * @param key  the key of the {@link Vertex} to find or create.
     * @param map  configuration of {@link Vertex}
     * @param closure  configuration for graph and vertex
     * @return The resulting {@link Vertex}.
     */
    Vertex vertex(Object key, Map<String, ?> map, Closure closure) {
        if(!key) {
            throw new IllegalArgumentException("Invalid key.")
        }
        map.key = map.key ?: key
        ConfigSpec spec = new ConfigSpec(map:map, closure:closure)
        vertex(spec)
    }
}