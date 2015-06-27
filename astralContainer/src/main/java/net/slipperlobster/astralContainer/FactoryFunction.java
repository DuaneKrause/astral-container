/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

@FunctionalInterface
public interface FactoryFunction<T>
{
    T create(AstralContainer container) throws Exception;
}
