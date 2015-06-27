/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

public class SingletonInstanceFactory implements AstralFactory
{
    private final Object _instance;

    public SingletonInstanceFactory(Object instance) {
        _instance = instance;
    }

    @Override
    public <T> T create(AstralType type, ParameterList dependentObjects, AstralContainer container) throws Exception {
        //noinspection unchecked
        return (T) _instance;
    }

    @Override
    public AstralTypeId[] getDependentTypeIds() {
        return new AstralTypeId[0];
    }
}
