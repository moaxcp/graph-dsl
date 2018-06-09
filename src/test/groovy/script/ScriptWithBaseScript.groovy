package script

import graph.Graph
import spock.lang.Specification

class ScriptWithBaseScript extends Specification {
    GroovyShell shell = new GroovyShell(this.class.classLoader)

    def 'can change BaseScript'() {
        expect:
        shell.evaluate '''
            import graph.DslScript
            @groovy.transform.BaseScript DslScript graph
        '''
    }

    def 'can change graph with BaseScript'() {
        expect:
        Graph graph = shell.evaluate '''
            import graph.DslScript
            @groovy.transform.BaseScript DslScript script
            edge 'a', 'b'
            assert vertices.size() == 2
            assert vertices['a'].id == 'a'
            assert vertices['b'].id == 'b'
        '''
    }
}
