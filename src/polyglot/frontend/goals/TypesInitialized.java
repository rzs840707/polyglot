/*
 * BuildTypesForFile.java
 * 
 * Author: nystrom
 * Creation date: Dec 19, 2004
 */
package polyglot.frontend.goals;

import polyglot.ast.NodeFactory;
import polyglot.frontend.*;
import polyglot.types.TypeSystem;
import polyglot.visit.TypeBuilder;


public class TypesInitialized extends SourceFileGoal {
    public TypesInitialized(Job job) { super(job); }
    
    public Pass createPass(ExtensionInfo extInfo) {
        TypeSystem ts = extInfo.typeSystem();
        NodeFactory nf = extInfo.nodeFactory();
        return new VisitorPass(this, new TypeBuilder(this, ts, nf));
    }
}