/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import java.lang.reflect.Type;

public class AstralTypeId implements Comparable<AstralTypeId>
{
    private final Type _type;
    private final String _qualifier;
    private final String _typeName;

    public AstralTypeId(Type type) {
       this(type, null);
    }

    public AstralTypeId(Type type, String qualifier) {
        _type = type;
        _qualifier = qualifier;

        StringBuilder stringBuilder = new StringBuilder(getUniqueTypeName(_type));
        if (_qualifier != null) {
            stringBuilder.append('|');
            stringBuilder.append(_qualifier);
        }
        _typeName = stringBuilder.toString();
    }

    public Type getType() { return _type; }
    public String getQualifier() { return _qualifier; }
    public String getTypeName() { return _typeName; }

    public static String getUniqueTypeName(Type clazz) { return clazz.getTypeName(); }

    @Override
    public String toString() { return _typeName; }

    @Override
    public int compareTo(AstralTypeId other) {
        return _typeName.compareTo(other._typeName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AstralTypeId that = (AstralTypeId) o;
        return _typeName.equals(that._typeName);
    }

    @Override
    public int hashCode() {
        return _typeName.hashCode();
    }
}
