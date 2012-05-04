/*******************************************************************************
 * This file is part of the Polyglot extensible compiler framework.
 *
 * Copyright (c) 2000-2008 Polyglot project group, Cornell University
 * Copyright (c) 2006-2008 IBM Corporation
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
 * monitored by USAF Rome Laboratory, ONR Grant N00014-01-1-0968, NSF
 * Grants CNS-0208642, CNS-0430161, and CCF-0133302, an Alfred P. Sloan
 * Research Fellowship, and an Intel Research Ph.D. Fellowship.
 *
 * See README for contributors.
 ******************************************************************************/

package polyglot.visit;

import java.util.*;
import java.util.HashSet;
import java.util.Stack;

import polyglot.ast.*;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.*;
import polyglot.frontend.Job;
import polyglot.frontend.goals.Goal;
import polyglot.frontend.goals.TypeExists;
import polyglot.main.Report;
import polyglot.types.*;
import polyglot.types.Package;
import polyglot.util.*;

/** Visitor which traverses the AST constructing type objects. */
public class InitImportsVisitor extends ErrorHandlingVisitor
{
    protected ImportTable importTable;
    
    public InitImportsVisitor(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, ts, nf);
    }
    
    public NodeVisitor enterCall(Node n) throws SemanticException {
        if (n instanceof SourceFile) {
            SourceFile sf = (SourceFile) n;
            
            PackageNode pn = sf.package_();
            
            ImportTable it;
            
            if (pn != null) {
                it = ts.importTable(sf.source().getName(), pn.package_());
            }
            else {
                it = ts.importTable(sf.source().getName(), null);
            }
            
            InitImportsVisitor v = (InitImportsVisitor) copy();
            v.importTable = it;
            return v;
        }
        
        return this;
    }
    
    public Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
        if (n instanceof SourceFile) {
            SourceFile sf = (SourceFile) n;
            InitImportsVisitor v_ = (InitImportsVisitor) v;
            ImportTable it = v_.importTable;
            return sf.importTable(it);
        }
        if (n instanceof Import) {
            Import im = (Import) n;
            
            if (im.kind() == Import.CLASS) {
                this.importTable.addClassImport(im.name(), im.position());
            }
            else if (im.kind() == Import.PACKAGE) {
                this.importTable.addPackageImport(im.name(), im.position());
            }
        }

        return n;
    }
}
