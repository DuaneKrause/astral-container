/**
 * astralContainer
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import org.junit.Assert;
import org.junit.Test;

public class SingletonInstanceTest
{
    @Test
    public void sameRegisteredInstanceReturned() throws Exception
    {
        AstralContainer container = new AstralContainer();
        Ghosts.DerivedRaw registeredInstance = new Ghosts.DerivedRaw();
        container.registerSingletonInstance(Ghosts.BaseRaw.class, registeredInstance);

        Ghosts.BaseRaw resolvedInstance1 = container.resolve(Ghosts.BaseRaw.class);
        Ghosts.BaseRaw resolvedInstance2 = container.resolve(Ghosts.BaseRaw.class);

        Assert.assertNotNull(resolvedInstance1);
        Assert.assertNotNull(resolvedInstance2);
        Assert.assertSame(registeredInstance, resolvedInstance1);
        Assert.assertSame(registeredInstance, resolvedInstance2);
    }

    @Test
    public void genericInstanceReturned() throws Exception
    {
        AstralContainer container = new AstralContainer();
        Ghosts.DerivedGeneric<String> registeredInstance = new Ghosts.DerivedGeneric<>();
        container.registerSingletonInstance(new TypeRef<Ghosts.BaseGeneric<String>>() {}, registeredInstance);

        Ghosts.BaseGeneric<String> resolvedInstance1 = container.resolve(new TypeRef<Ghosts.BaseGeneric<String>>() {});
        Ghosts.BaseGeneric<String> resolvedInstance2 = container.resolve(new TypeRef<Ghosts.BaseGeneric<String>>() {});

        Assert.assertNotNull(resolvedInstance1);
        Assert.assertNotNull(resolvedInstance2);
        Assert.assertSame(registeredInstance, resolvedInstance1);
        Assert.assertSame(registeredInstance, resolvedInstance2);
    }
}
