/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Similar to TypeReference described in 'Super Type Tokens' (http://gafter.blogspot.com/2006/12/super-type-tokens.html),
 * this class grabs type information for a class, including generics.
 * To use it, instantiate an anonymous class like this (where Pony<Unicorn> is the target class):
 *      new TypeRef<Pony<Unicorn>>() {}
 */
public abstract class TypeRef<T>
{
    private Type _type;

    public TypeRef()
    {
        _type = null;
        Type superDuper = getClass().getGenericSuperclass();
        if (superDuper instanceof ParameterizedType) {
            Type[] typeParams = ((ParameterizedType) superDuper).getActualTypeArguments();
            if (typeParams != null && typeParams.length > 0)
                _type = typeParams[0];
        }
        if (_type == null) {
            // instantiated as TypeRef<>
            throw new RuntimeException("Must supply a type parameter for T like this: TypeRef<T>() {}");
        }
    }

    public Type getType() { return _type; }
}
