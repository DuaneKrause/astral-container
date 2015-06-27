/**
 * astralContainer
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import org.junit.Assert;
import org.junit.Test;

public class ConstructorInjectionTest
{
    @Test
    public void constructorInjection() throws Exception
    {
        AstralContainer container = new AstralContainer();
        Ghosts.BaseRaw myBaseRaw = new Ghosts.BaseRaw();
        container.register(Ghosts.IBase.class, (AstralContainer c) -> new Ghosts.SingleComposedImpl(myBaseRaw));

        Ghosts.IBase result = container.resolve(Ghosts.IBase.class);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Ghosts.SingleComposedImpl);
        Ghosts.SingleComposedImpl implResult = (Ghosts.SingleComposedImpl) result;
        Assert.assertNotNull(implResult._p1);
        Assert.assertSame(implResult._p1, myBaseRaw);
    }

    @Test
    public void constructorInjectionUsesContainer() throws Exception
    {
        // here is an example on how to qualify ctor parameters without using annotations
        AstralContainer container = new AstralContainer();

        Ghosts.BaseRaw myBaseRaw = new Ghosts.DerivedRaw();
        Ghosts.BaseRaw anotherBaseRaw = new Ghosts.DerivedRaw();
        container.registerSingletonInstance(Ghosts.BaseRaw.class, "A", myBaseRaw);
        container.registerSingletonInstance(Ghosts.BaseRaw.class, "B", anotherBaseRaw);

        container.register(Ghosts.IBase.class, (AstralContainer c) -> new Ghosts.SingleComposedImpl(c.resolve(Ghosts.BaseRaw.class, "A")));

        Ghosts.IBase result = container.resolve(Ghosts.IBase.class);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Ghosts.SingleComposedImpl);
        Ghosts.SingleComposedImpl implResult = (Ghosts.SingleComposedImpl) result;
        Assert.assertNotNull(implResult._p1);
        Assert.assertTrue(implResult._p1 instanceof Ghosts.DerivedRaw);
        Assert.assertSame(implResult._p1, myBaseRaw);
    }
}
