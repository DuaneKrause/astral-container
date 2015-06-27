/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import java.lang.reflect.Type;

public class AstralType implements Comparable<AstralType>
{
    private final AstralTypeId _typeId;
    private final AstralFactory _factory;
    private final AstralTypeId[] _dependentTypeIds;

    public AstralType(Type type) { this(type, null, null); }
    public AstralType(Type type, String qualifier) { this(type, qualifier, null); }
    public AstralType(Type type, AstralFactory factory) { this(type, null, factory); }
    public AstralType(TypeRef<?> typeRef) { this(typeRef, null, null); }
    public AstralType(TypeRef<?> typeRef, AstralFactory factory) { this(typeRef, null, factory); }
    public AstralType(TypeRef<?> typeRef, String qualifier) { this(typeRef, qualifier, null); }
    public AstralType(TypeRef<?> typeRef, String qualifier, AstralFactory factory) {
        this(typeRef.getType(), qualifier, factory);
    }

    public AstralType(Type type, String qualifier, AstralFactory factory) {
        _typeId = new AstralTypeId(type, qualifier);
        _factory = factory == null ? new ConstructingFactory(type) : factory;
        _dependentTypeIds = _factory.getDependentTypeIds();
    }

    public AstralTypeId getTypeId() { return _typeId; }
    public String getTypeName() { return _typeId.getTypeName(); }
    public AstralTypeId[] getDependentTypeIds() { return _dependentTypeIds; }
    public AstralFactory getFactory() { return _factory; }

    @Override
    public int compareTo(AstralType other) { return getTypeName().compareTo(other.getTypeName()); }
}

