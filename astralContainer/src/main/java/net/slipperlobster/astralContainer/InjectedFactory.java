/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

public class InjectedFactory implements AstralFactory
{
    private final FactoryFunction<?> _ctor;

    public InjectedFactory(FactoryFunction<?> ctor) {_ctor = ctor;}


    @Override
    public <T> T create(AstralType type, ParameterList dependentObjects, AstralContainer container) throws Exception {
        return (T) _ctor.create(container);
    }

    @Override
    public AstralTypeId[] getDependentTypeIds() {
        return new AstralTypeId[0];
    }
}
