/*******************************************************************************
 * This file is part of the Polyglot extensible compiler framework.
 *
 * Copyright (c) 2000-2012 Polyglot project group, Cornell University
 * Copyright (c) 2006-2012 IBM Corporation
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This program and the accompanying materials are made available under
 * the terms of the Lesser GNU Public License v2.0 which accompanies this
 * distribution.
 * 
 * The development of the Polyglot project has been supported by a
 * number of funding sources, including DARPA Contract F30602-99-1-0533,
 * monitored by USAF Rome Laboratory, ONR Grants N00014-01-1-0968 and
 * N00014-09-1-0652, NSF Grants CNS-0208642, CNS-0430161, CCF-0133302,
 * and CCF-1054172, AFRL Contract FA8650-10-C-7022, an Alfred P. Sloan 
 * Research Fellowship, and an Intel Research Ph.D. Fellowship.
 *
 * See README for contributors.
 ******************************************************************************/
package polyglot.ext.jl5.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import polyglot.ast.ClassDecl;
import polyglot.ast.ClassDeclOps;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.ext.jl5.types.AnnotationTypeElemInstance;
import polyglot.ext.jl5.types.Annotations;
import polyglot.ext.jl5.types.JL5Context;
import polyglot.ext.jl5.types.JL5Flags;
import polyglot.ext.jl5.types.JL5ParsedClassType;
import polyglot.ext.jl5.types.JL5TypeSystem;
import polyglot.ext.jl5.types.TypeVariable;
import polyglot.ext.jl5.visit.AnnotationChecker;
import polyglot.ext.jl5.visit.JL5Translator;
import polyglot.ext.param.types.MuPClass;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.Declaration;
import polyglot.types.Flags;
import polyglot.types.ParsedClassType;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Copy;
import polyglot.util.ListUtil;
import polyglot.util.SerialVersionUID;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class JL5ClassDeclExt extends JL5AnnotatedElementExt implements
        ClassDeclOps {
    private static final long serialVersionUID = SerialVersionUID.generate();

    protected List<TypeNode> paramTypes = new ArrayList<TypeNode>();

    public JL5ClassDeclExt(List<TypeNode> paramTypes, List<Term> annotations) {
        super(annotations);
        this.paramTypes = ListUtil.copy(paramTypes, true);
    }

    @Override
    public ClassDecl node() {
        return (ClassDecl) super.node();
    }

    public List<TypeNode> paramTypes() {
        return this.paramTypes;
    }

    public Node paramTypes(List<TypeNode> types) {
        return paramTypes(node(), types);
    }

    protected <N extends Node> N paramTypes(N n, List<TypeNode> types) {
        JL5ClassDeclExt ext = (JL5ClassDeclExt) JL5Ext.ext(n);
        if (CollectionUtil.equals(ext.paramTypes, types)) return n;
        if (n == node) {
            n = Copy.Util.copy(n);
            ext = (JL5ClassDeclExt) JL5Ext.ext(n);
        }
        ext.paramTypes = ListUtil.copy(types, true);
        return n;
    }

    @Override
    public Node annotationCheck(AnnotationChecker annoCheck)
            throws SemanticException {
        super.annotationCheck(annoCheck);

        ClassDecl n = this.node();

        // check annotation circularity
        if (JL5Flags.isAnnotation(n.flags())) {
            JL5ParsedClassType ct = (JL5ParsedClassType) n.type();
            for (AnnotationTypeElemInstance ai : ct.annotationElems()) {
                if (ai.type() instanceof ClassType
                        && ((ClassType) ((ClassType) ai.type()).superType()).fullName()
                                                                            .equals("java.lang.annotation.Annotation")) {
                    JL5ParsedClassType other = (JL5ParsedClassType) ai.type();
                    for (Object element2 : other.annotationElems()) {
                        AnnotationTypeElemInstance aj =
                                (AnnotationTypeElemInstance) element2;
                        if (aj.type().equals(ct)) {
                            throw new SemanticException("cyclic annotation element type",
                                                        aj.position());
                        }
                    }
                }
            }
        }
        return n;
    }

    @Override
    public void setAnnotations(Annotations annotations) {
        ClassDecl n = this.node();
        JL5ParsedClassType pct = (JL5ParsedClassType) n.type();
        pct.setAnnotations(annotations);
        pct.setAnnotationsResolved(true);
    }

    @Override
    protected Declaration declaration() {
        ClassDecl n = this.node();
        return n.type();
    }

    private Node reconstruct(Node n, List<TypeNode> paramTypes) {
        n = paramTypes(n, paramTypes);
        return n;
    }

    @Override
    public Node visitChildren(NodeVisitor v) {
        List<TypeNode> typeParams = visitList(paramTypes, v);
        Node n = super.visitChildren(v);
        return reconstruct(n, typeParams);
    }

    @Override
    public Node buildTypes(TypeBuilder tb) throws SemanticException {
        ClassDecl n = (ClassDecl) superLang().buildTypes(this.node(), tb);

        JL5TypeSystem ts = (JL5TypeSystem) tb.typeSystem();
        JL5ParsedClassType ct = (JL5ParsedClassType) n.type();

        MuPClass<TypeVariable, ReferenceType> pc =
                ts.mutablePClass(ct.position());
        ct.setPClass(pc);
        pc.clazz(ct);

        if (paramTypes != null && !paramTypes.isEmpty()) {
            List<TypeVariable> typeVars =
                    new ArrayList<TypeVariable>(paramTypes.size());
            for (TypeNode ptn : paramTypes) {
                TypeVariable tv = (TypeVariable) ptn.type();
                typeVars.add(tv);
                tv.setDeclaringClass(ct);
            }
            ct.setTypeVariables(typeVars);
            pc.formals(new ArrayList<TypeVariable>(typeVars));
        }

        return n;
    }

    /*
     * (non-Javadoc)
     * 
     * @see polyglot.ast.NodeOps#enterScope(polyglot.types.Context)
     */
    @Override
    public Context enterChildScope(Node child, Context c) {
        ClassDecl n = this.node();

        if (child == n.body()) {
            TypeSystem ts = c.typeSystem();
            c = c.pushClass(n.type(), ts.staticTarget(n.type()).toClass());
        }
        else {
            // Add this class to the context, but don't push a class scope.
            // This allows us to detect loops in the inheritance
            // hierarchy, but avoids an infinite loop.
            c = ((JL5Context) c).pushExtendsClause(n.type());
            c.addNamed(n.type());
        }
        for (TypeNode tn : paramTypes) {
            ((JL5Context) c).addTypeVariable((TypeVariable) tn.type());
        }
        return c.lang().enterScope(child, c);
    }

    @Override
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        ClassDecl n = this.node();
        ParsedClassType type = n.type();
        JL5ClassDeclExt ext = (JL5ClassDeclExt) JL5Ext.ext(n);

        JL5TypeSystem ts = (JL5TypeSystem) tc.typeSystem();

        Type superType = type.superType();
        if (superType != null && JL5Flags.isEnum(superType.toClass().flags())) {
            throw new SemanticException("Cannot extend enum type", n.position());
        }

        if (ts.equals(ts.Object(), type) && !ext.paramTypes.isEmpty()) {
            throw new SemanticException("Type: " + type
                    + " cannot declare type variables.", n.position());
        }

        if (JL5Flags.isAnnotation(n.flags()) && n.flags().isPrivate()) {
            throw new SemanticException("Annotation types cannot have explicit private modifier",
                                        n.position());
        }

        ts.checkDuplicateAnnotations(ext.annotations);

        // check not extending java.lang.Throwable (or any of its subclasses)
        // with a generic class
        if (superType != null && ts.isSubtype(superType, ts.Throwable())
                && !ext.paramTypes.isEmpty()) {
            // JLS 3rd ed. 8.1.2
            throw new SemanticException("Cannot subclass java.lang.Throwable or any of its subtypes with a generic class",
                                        n.superClass().position());
        }

        // check duplicate type variable decls
        for (int i = 0; i < ext.paramTypes.size(); i++) {
            TypeNode ti = ext.paramTypes.get(i);
            for (int j = i + 1; j < ext.paramTypes.size(); j++) {
                TypeNode tj = ext.paramTypes.get(j);
                if (ti.name().equals(tj.name())) {
                    throw new SemanticException("Duplicate type variable declaration.",
                                                tj.position());
                }
            }
        }

        for (TypeNode paramType : ext.paramTypes)
            ts.checkCycles(paramType.type().toReference());

        return super.typeCheck(tc);
    }

    public void prettyPrintModifiers(CodeWriter w, PrettyPrinter tr) {
        ClassDecl n = this.node();
        Flags f = n.flags();
        if (f.isInterface()) {
            f = f.clearInterface().clearAbstract();
        }
        if (JL5Flags.isEnum(f)) {
            f = JL5Flags.clearEnum(f).clearStatic().clearAbstract();
        }
        if (JL5Flags.isAnnotation(f)) {
            f = JL5Flags.clearAnnotation(f);
        }
        w.write(f.translate());

        if (n.flags().isInterface()) {
            if (JL5Flags.isAnnotation(n.flags())) {
                w.write("@interface ");
            }
            else {
                w.write("interface ");
            }
        }
        else if (JL5Flags.isEnum(n.flags())) {
            w.write("enum ");
        }
        else {
            w.write("class ");
        }
    }

    public void prettyPrintName(CodeWriter w, PrettyPrinter tr) {
        ClassDecl n = this.node();
        w.write(n.id().id());
    }

    public void prettyPrintHeaderRest(CodeWriter w, PrettyPrinter tr) {
        ClassDecl n = this.node();
        if (n.superClass() != null
                && ((!JL5Flags.isEnum(n.flags()) && !JL5Flags.isAnnotation(n.flags())))) {
            w.write(" extends ");
            print(n.superClass(), w, tr);
        }

        if (!n.interfaces().isEmpty() && !JL5Flags.isAnnotation(n.flags())) {
            if (n.flags().isInterface()) {
                w.write(" extends ");
            }
            else {
                w.write(" implements ");
            }

            for (Iterator<TypeNode> i = n.interfaces().iterator(); i.hasNext();) {
                TypeNode tn = i.next();
                print(tn, w, tr);

                if (i.hasNext()) {
                    w.write(", ");
                }
            }
        }

        w.write(" {");
    }

    @Override
    public void prettyPrint(CodeWriter w, PrettyPrinter pp) {
        superLang().prettyPrint(node(), w, pp);
    }

    @Override
    public void prettyPrintHeader(CodeWriter w, PrettyPrinter tr) {
        w.begin(0);
        super.prettyPrint(w, tr);
        w.end();

        prettyPrintModifiers(w, tr);
        prettyPrintName(w, tr);
        // print type variables
        boolean printTypeVars = true;
        if (tr instanceof JL5Translator) {
            JL5Translator jl5tr = (JL5Translator) tr;
            printTypeVars = !jl5tr.removeJava5isms();
        }
        if (printTypeVars && !paramTypes.isEmpty()) {
            w.write("<");
            for (Iterator<TypeNode> iter = paramTypes.iterator(); iter.hasNext();) {
                TypeNode ptn = iter.next();
                tr.lang().prettyPrint(ptn, w, tr);
                if (iter.hasNext()) {
                    w.write(", ");
                }
            }
            w.write(">");
        }
        prettyPrintHeaderRest(w, tr);
    }

    @Override
    public void prettyPrintFooter(CodeWriter w, PrettyPrinter tr) {
        superLang().prettyPrintFooter(this.node(), w, tr);
    }

    @Override
    public Node addDefaultConstructor(TypeSystem ts, NodeFactory nf,
            ConstructorInstance defaultConstructorInstance)
            throws SemanticException {
        return superLang().addDefaultConstructor(this.node(),
                                                 ts,
                                                 nf,
                                                 defaultConstructorInstance);
    }
}
