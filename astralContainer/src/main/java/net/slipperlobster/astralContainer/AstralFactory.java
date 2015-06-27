/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

public interface AstralFactory
{
    <T> T create(AstralType type, ParameterList dependentObjects, AstralContainer container)  throws Exception;
    AstralTypeId[] getDependentTypeIds();
}
