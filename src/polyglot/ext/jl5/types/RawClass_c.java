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
package polyglot.ext.jl5.types;

import java.util.List;
import java.util.Set;

import polyglot.frontend.Job;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.MethodInstance;
import polyglot.types.Package;
import polyglot.types.ReferenceType;
import polyglot.types.Resolver;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.SerialVersionUID;

public class RawClass_c extends JL5ClassType_c implements RawClass {
    private static final long serialVersionUID = SerialVersionUID.generate();

    private JL5ParsedClassType base;
    private transient JL5SubstClassType erased;

    public RawClass_c(JL5ParsedClassType t, Position pos) {
        super((JL5TypeSystem) t.typeSystem(), pos);
        this.base = t;
        this.setDeclaration(base);
    }

    @Override
    public boolean isRawClass() {
        return true;
    }

    @Override
    public JL5ParsedClassType base() {
        return this.base;
    }

    @Override
    public JL5SubstClassType erased() {
        if (this.erased == null) {
            JL5TypeSystem ts = (JL5TypeSystem) this.ts;
            JL5Subst es = ts.erasureSubst(this.base);
            this.erased =
                    new JL5SubstClassType_c(ts, base.position(), base, es);
        }
        return this.erased;
    }

    @Override
    public List<EnumInstance> enumConstants() {
        return this.erased().enumConstants();
    }

    @Override
    public Job job() {
        return null;
    }

    @Override
    public Kind kind() {
        return this.erased().kind();
    }

    @Override
    public ClassType outer() {
        ClassType t = this.erased().outer();
        if (t == null) {
            return t;
        }
        JL5TypeSystem ts = (JL5TypeSystem) this.typeSystem();

        return (ClassType) ts.erasureType(this.erased().outer());
    }

    @Override
    public String name() {
        return this.erased().name();
    }

    @Override
    public Package package_() {
        return this.erased().package_();
    }

    @Override
    public Flags flags() {
        return this.erased().flags();
    }

    private transient List<? extends ConstructorInstance> constructors = null;

    @Override
    public List<? extends ConstructorInstance> constructors() {
        if (constructors == null) {
            this.constructors = this.erased().constructors();
        }
        return this.constructors;
    }

    private transient List<? extends ClassType> memberClasses = null;

    @Override
    public List<? extends ClassType> memberClasses() {
        if (memberClasses == null) {
            this.memberClasses = this.erased().memberClasses();
        }
        return this.memberClasses;
    }

    private transient List<? extends MethodInstance> methods = null;

    @Override
    public List<? extends MethodInstance> methods() {
        if (methods == null) {
            this.methods = this.erased().methods();
        }
        return this.methods;
    }

    private transient List<? extends FieldInstance> fields = null;

    @Override
    public List<? extends FieldInstance> fields() {
        if (fields == null) {
            this.fields = this.erased().fields();
            for (FieldInstance fi : this.fields) {
                fi.setContainer(this);
            }
        }
        return this.fields;
    }

    @Override
    public List<? extends ReferenceType> interfaces() {
        return this.erased().interfaces();
    }

    @Override
    public Type superType() {
        return this.erased().superType();
    }

    @Override
    public Set<? extends Type> superclasses() {
        return this.erased().superclasses();
    }

    @Override
    public boolean equalsImpl(TypeObject t) {
        if (super.equalsImpl(t)) {
            return true;
        }
        if (t instanceof RawClass) {
            RawClass rt = (RawClass) t;
            return typeSystem().equals(this.base(), rt.base());
        }
        return false;
    }

    @Override
    public boolean typeEqualsImpl(Type t) {
        if (super.typeEqualsImpl(t)) {
            return true;
        }
        if (t instanceof RawClass) {
            RawClass rt = (RawClass) t;
            return typeSystem().typeEquals(this.base(), rt.base());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.base().hashCode();
    }

    @Override
    public String translateAsReceiver(Resolver c) {
        return this.translate(c);
    }

    @Override
    public boolean descendsFromImpl(Type ancestor) {
//        System.err.println("   Raw class " + this + " descends from " + ancestor + " ?  interfaces is " + this.interfaces() + "  ::: " + super.descendsFromImpl(ancestor));
//        System.err.println("    base interfaces are "  + this.base.interfaces());
        if (super.descendsFromImpl(ancestor)) {
            return true;
        }
//        Type ra = ((JL5TypeSystem)ts).toRawType(ancestor);
//        if (!ra.equals(ancestor)) {
//            return ts.isSubtype(this, ra);
//        }
        return false;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        // TODO Auto-generated method stub
        return super.clone();
    }

    @Override
    public boolean inStaticContext() {
        return this.erased().inStaticContext();
    }

    @Override
    public void setFlags(Flags flags) {
        throw new InternalCompilerError("Should never be called");
    }

    @Override
    public void setContainer(ReferenceType container) {
        throw new InternalCompilerError("Should never be called");
    }

    @Override
    public AnnotationTypeElemInstance annotationElemNamed(String name) {
        return this.erased().annotationElemNamed(name);
    }

    @Override
    public List<AnnotationTypeElemInstance> annotationElems() {
        return this.erased().annotationElems();
    }

    @Override
    public Annotations annotations() {
        return ((JL5TypeSystem) this.typeSystem()).NoAnnotations();
    }

}
