/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;


import org.junit.Test;

import org.junit.Assert;

import javax.swing.plaf.basic.BasicHTML;

public class AstralContainerTest
{
    @Test
    public void createRawDerived() throws Exception
    {
        AstralContainer container = new AstralContainer();
        container.register(Ghosts.BaseRaw.class, Ghosts.DerivedRaw.class);

        Ghosts.BaseRaw baseRaw = container.resolve(Ghosts.BaseRaw.class);

        Assert.assertNotNull(baseRaw);
        Assert.assertTrue(baseRaw instanceof Ghosts.DerivedRaw);
    }

    @Test
    public void createGenericDerived() throws Exception
    {
        AstralContainer container = new AstralContainer();
        container.register(new TypeRef<Ghosts.BaseGeneric<String>>() {}, new TypeRef<Ghosts.DerivedGeneric<String>>() {});

        Ghosts.BaseGeneric<String> baseRaw = container.resolve(new TypeRef<Ghosts.BaseGeneric<String>>() {});

        Assert.assertNotNull(baseRaw);
        Assert.assertTrue(baseRaw instanceof Ghosts.DerivedGeneric<?>);
    }

    @Test
    public void createSingleComposedImpl() throws Exception
    {
        AstralContainer container = new AstralContainer();
        container.register(Ghosts.BaseRaw.class, Ghosts.DerivedRaw.class);
        container.register(Ghosts.IBase.class, Ghosts.SingleComposedImpl.class);

        Ghosts.IBase iBase = container.resolve(Ghosts.IBase.class);

        Assert.assertNotNull(iBase);
        Assert.assertTrue(iBase instanceof Ghosts.SingleComposedImpl);
        Ghosts.SingleComposedImpl singleComposed = (Ghosts.SingleComposedImpl) iBase;
        Assert.assertNotNull(singleComposed._p1);
        Assert.assertTrue(singleComposed._p1 instanceof Ghosts.DerivedRaw);
    }

}

