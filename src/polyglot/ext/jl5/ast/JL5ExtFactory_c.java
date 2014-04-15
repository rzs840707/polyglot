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

import polyglot.ast.AbstractExtFactory_c;
import polyglot.ast.Ext;
import polyglot.ast.ExtFactory;

public class JL5ExtFactory_c extends AbstractExtFactory_c implements
        JL5ExtFactory {

    public JL5ExtFactory_c() {
        super();
    }

    public JL5ExtFactory_c(ExtFactory nextExtFactory) {
        super(nextExtFactory);
    }

    @Override
    protected Ext extCaseImpl() {
        return new JL5CaseExt();
    }

    @Override
    protected Ext extCatchImpl() {
        return new JL5CatchExt();
    }

    @Override
    protected Ext extAssertImpl() {
        return new JL5AssertExt();
    }

    @Override
    protected Ext extAssignImpl() {
        return new JL5AssignExt();
    }

    @Override
    protected Ext extBinaryImpl() {
        return new JL5BinaryExt();
    }

    @Override
    protected Ext extCanonicalTypeNodeImpl() {
        return new JL5CanonicalTypeNodeExt();
    }

    @Override
    protected Ext extClassBodyImpl() {
        return new JL5ClassBodyExt();
    }

    @Override
    protected Ext extClassLitImpl() {
        return new JL5ClassLitExt();
    }

    @Override
    protected Ext extConditionalImpl() {
        return new JL5ConditionalExt();
    }

    @Override
    protected Ext extConstructorCallImpl() {
        return new JL5ConstructorCallExt();
    }

    @Override
    protected Ext extFieldImpl() {
        return new JL5FieldExt();
    }

    @Override
    protected Ext extFormalImpl() {
        return new JL5FormalExt();
    }

    @Override
    protected Ext extCallImpl() {
        return new JL5CallExt();
    }

    @Override
    protected Ext extSpecialImpl() {
        return new JL5SpecialExt();
    }

    @Override
    protected Ext extSwitchImpl() {
        return new JL5SwitchExt();
    }

    @Override
    protected Ext extUnaryImpl() {
        return new JL5UnaryExt();
    }

    @Override
    protected Ext extFieldDeclImpl() {
        return new JL5FieldDeclExt();
    }

    @Override
    protected Ext extLocalDeclImpl() {
        return new JL5LocalDeclExt();
    }

    @Override
    protected Ext extClassDeclImpl() {
        return new JL5ClassDeclExt();
    }

    @Override
    protected Ext extConstructorDeclImpl() {
        return new JL5ConstructorDeclExt();
    }

    @Override
    protected Ext extImportImpl() {
        return new JL5ImportExt();
    }

    @Override
    protected Ext extMethodDeclImpl() {
        return new JL5MethodDeclExt();
    }

    @Override
    protected Ext extNewImpl() {
        return new JL5NewExt();
    }

    @Override
    protected Ext extNewArrayImpl() {
        return new JL5NewArrayExt();
    }

    @Override
    public Ext extEnumDecl() {
        Ext e = extEnumDeclImpl();

        if (nextExtFactory() != null) {
            Ext e2;
            if (nextExtFactory() instanceof JL5ExtFactory) {
                e2 = ((JL5ExtFactory) nextExtFactory()).extEnumDecl();
            }
            else {
                e2 = nextExtFactory().extClassDecl();
            }
            e = composeExts(e, e2);
        }
        return postExtEnumDecl(e);
    }

    @Override
    public Ext extExtendedFor() {
        Ext e = extExtendedForImpl();

        if (nextExtFactory() != null) {
            Ext e2;
            if (nextExtFactory() instanceof JL5ExtFactory) {
                e2 = ((JL5ExtFactory) nextExtFactory()).extExtendedFor();
            }
            else {
                e2 = nextExtFactory().extLoop();
            }
            e = composeExts(e, e2);
        }
        return postExtExtendedFor(e);
    }

    @Override
    public Ext extEnumConstantDecl() {
        Ext e = extEnumConstantDeclImpl();

        if (nextExtFactory() != null) {
            Ext e2;
            if (nextExtFactory() instanceof JL5ExtFactory) {
                e2 = ((JL5ExtFactory) nextExtFactory()).extEnumConstantDecl();
            }
            else {
                e2 = nextExtFactory().extClassMember();
            }
            e = composeExts(e, e2);
        }
        return postExtEnumConstantDecl(e);
    }

    @Override
    public Ext extEnumConstant() {
        Ext e = extEnumConstantImpl();

        if (nextExtFactory() != null) {
            Ext e2;
            if (nextExtFactory() instanceof JL5ExtFactory) {
                e2 = ((JL5ExtFactory) nextExtFactory()).extEnumConstant();
            }
            else {
                e2 = nextExtFactory().extField();
            }
            e = composeExts(e, e2);
        }
        return postExtEnumConstant(e);
    }

    @Override
    public Ext extParamTypeNode() {
        Ext e = extParamTypeNodeImpl();

        if (nextExtFactory() != null) {
            Ext e2;
            if (nextExtFactory() instanceof JL5ExtFactory) {
                e2 = ((JL5ExtFactory) nextExtFactory()).extParamTypeNode();
            }
            else {
                e2 = nextExtFactory().extTypeNode();
            }
            e = composeExts(e, e2);
        }
        return postExtParamTypeNode(e);
    }

    @Override
    public Ext extAnnotationElemDecl() {
        Ext e = extAnnotationElemDeclImpl();

        if (nextExtFactory() != null) {
            Ext e2;
            if (nextExtFactory() instanceof JL5ExtFactory) {
                e2 = ((JL5ExtFactory) nextExtFactory()).extAnnotationElemDecl();
            }
            else {
                e2 = nextExtFactory().extMethodDecl();
            }
            e = composeExts(e, e2);
        }
        return postExtAnnotationElemDecl(e);
    }

    @Override
    public Ext extNormalAnnotationElem() {
        Ext e = extNormalAnnotationElemImpl();

        if (nextExtFactory() != null) {
            Ext e2;
            if (nextExtFactory() instanceof JL5ExtFactory) {
                e2 =
                        ((JL5ExtFactory) nextExtFactory()).extNormalAnnotationElem();
            }
            else {
                e2 = nextExtFactory().extTerm();
            }
            e = composeExts(e, e2);
        }
        return postExtNormalAnnotationElem(e);
    }

    @Override
    public Ext extMarkerAnnotationElem() {
        Ext e = extMarkerAnnotationElemImpl();

        if (nextExtFactory() != null) {
            Ext e2;
            if (nextExtFactory() instanceof JL5ExtFactory) {
                e2 =
                        ((JL5ExtFactory) nextExtFactory()).extMarkerAnnotationElem();
            }
            else {
                e2 = nextExtFactory().extTerm();
            }
            e = composeExts(e, e2);
        }
        return postExtMarkerAnnotationElem(e);
    }

    @Override
    public Ext extSingleElementAnnotationElem() {
        Ext e = extSingleElementAnnotationElemImpl();

        if (nextExtFactory() != null) {
            Ext e2;
            if (nextExtFactory() instanceof JL5ExtFactory) {
                e2 =
                        ((JL5ExtFactory) nextExtFactory()).extSingleElementAnnotationElem();
            }
            else {
                e2 = nextExtFactory().extTerm();
            }
            e = composeExts(e, e2);
        }
        return postExtSingleElementAnnotationElem(e);
    }

    @Override
    public Ext extElementValuePair() {
        Ext e = extElementValuePairImpl();

        if (nextExtFactory() != null) {
            Ext e2;
            if (nextExtFactory() instanceof JL5ExtFactory) {
                e2 = ((JL5ExtFactory) nextExtFactory()).extElementValuePair();
            }
            else {
                e2 = nextExtFactory().extTerm();
            }
            e = composeExts(e, e2);
        }
        return postExtElementValuePair(e);
    }

    @Override
    public Ext extElementValueArrayInit() {
        Ext e = extElementValueArrayInitImpl();

        if (nextExtFactory() != null) {
            Ext e2;
            if (nextExtFactory() instanceof JL5ExtFactory) {
                e2 =
                        ((JL5ExtFactory) nextExtFactory()).extElementValueArrayInit();
            }
            else {
                e2 = nextExtFactory().extTerm();
            }
            e = composeExts(e, e2);
        }
        return postExtElementValueArrayInit(e);
    }

    protected Ext extEnumDeclImpl() {
        return new JL5EnumDeclExt();
    }

    protected Ext extExtendedForImpl() {
        return this.extLoopImpl();
    }

    protected Ext extEnumConstantDeclImpl() {
        return new EnumConstantDeclExt();
    }

    protected Ext extEnumConstantImpl() {
        return this.extFieldImpl();
    }

    protected Ext extParamTypeNodeImpl() {
        return this.extTypeNodeImpl();
    }

    @Override
    protected Ext extNodeImpl() {
        return new JL5Ext();
    }

    public Ext postExtEnumDecl(Ext ext) {
        return this.postExtClassDecl(ext);
    }

    public Ext postExtExtendedFor(Ext ext) {
        return this.postExtLoop(ext);
    }

    public Ext postExtEnumConstantDecl(Ext ext) {
        return this.postExtClassMember(ext);
    }

    public Ext postExtEnumConstant(Ext ext) {
        return this.postExtField(ext);
    }

    public Ext postExtParamTypeNode(Ext ext) {
        return this.postExtTypeNode(ext);
    }

    protected Ext extAnnotationElemDeclImpl() {
        return this.extClassMemberImpl();
    }

    protected Ext extNormalAnnotationElemImpl() {
        return this.extTermImpl();
    }

    protected Ext extMarkerAnnotationElemImpl() {
        return this.extNormalAnnotationElemImpl();
    }

    protected Ext extSingleElementAnnotationElemImpl() {
        return this.extNormalAnnotationElemImpl();
    }

    protected Ext extElementValuePairImpl() {
        return this.extTermImpl();
    }

    protected Ext extElementValueArrayInitImpl() {
        return this.extTermImpl();
    }

    protected Ext postExtAnnotationElemDecl(Ext ext) {
        return ext;
    }

    protected Ext postExtNormalAnnotationElem(Ext ext) {
        return ext;
    }

    protected Ext postExtMarkerAnnotationElem(Ext ext) {
        return ext;
    }

    protected Ext postExtSingleElementAnnotationElem(Ext ext) {
        return ext;
    }

    protected Ext postExtElementValuePair(Ext ext) {
        return ext;
    }

    protected Ext postExtElementValueArrayInit(Ext ext) {
        return ext;
    }

}
