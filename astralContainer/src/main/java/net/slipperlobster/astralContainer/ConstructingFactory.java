/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ConstructingFactory implements AstralFactory
{
    private final Constructor<?> _ctor;
    private final AstralTypeId[] _dependentTypeIds;

    public ConstructingFactory(Type type)
    {
        _ctor = getConstructor(type);
        _dependentTypeIds = getDependentTypeIds(_ctor);
    }

    @Override
    public <T> T create(AstralType type, ParameterList dependentObjects, AstralContainer container) throws Exception
    {
        //noinspection unchecked
        return (T) _ctor.newInstance(dependentObjects.getValues());
    }

    @Override
    public AstralTypeId[] getDependentTypeIds() {
        return _dependentTypeIds;
    }

    private AstralTypeId[] getDependentTypeIds(Constructor<?> ctor)
    {
        if (ctor == null)
            return new AstralTypeId[0];
        AstralTypeId[] typeIds = new AstralTypeId[ctor.getParameterCount()];
        Type[] parameterTypes = ctor.getGenericParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++)
        {
            typeIds[i] = new AstralTypeId(parameterTypes[i]);
        }
        return typeIds;
    }

    private Constructor<?> getConstructor(Type type)
    {
        Class<?> rawType;
        if (type instanceof Class<?>)
            rawType = (Class<?>) type;
        else if (type instanceof ParameterizedType)
            rawType = (Class<?>) ((ParameterizedType) type).getRawType();
        else
            throw new IllegalArgumentException("Couldn't find the raw type of " + type.getTypeName());

        // find ctor with least # of arguments
        int numArgsSoFar = 0;
        Constructor<?> bestCtor = null;
        for (Constructor<?> ctor : rawType.getConstructors()) {
            int numArgs = ctor.getParameterCount();
            if (bestCtor == null || numArgs < numArgsSoFar) {
                bestCtor = ctor;
                numArgsSoFar = numArgs;
            }
        }
        return bestCtor;
    }

}
