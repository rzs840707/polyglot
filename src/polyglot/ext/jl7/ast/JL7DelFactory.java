package polyglot.ext.jl7.ast;

import polyglot.ast.JLDel;
import polyglot.ext.jl5.ast.JL5DelFactory;

public interface JL7DelFactory extends JL5DelFactory {

    JLDel delMultiCatch();

}
