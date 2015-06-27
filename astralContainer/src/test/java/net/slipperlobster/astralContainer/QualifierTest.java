/**
 * astralContainer
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import org.junit.Assert;
import org.junit.Test;

public class QualifierTest
{
    @Test
    public void qualifiersIdentifyMultipleDerivedTypes() throws Exception
    {
        AstralContainer container = new AstralContainer();
        Ghosts.RawImplementationB singletonB = new Ghosts.RawImplementationB();
        Ghosts.GenericImplementationD<String> singletonD = new Ghosts.GenericImplementationD<>();

        container.register(Ghosts.IBase.class, "A", Ghosts.RawImplementationA.class);
        container.registerSingletonInstance(Ghosts.IBase.class, "B", singletonB);
        container.register(Ghosts.IBase.class, "C", new TypeRef<Ghosts.GenericImplementationC<Integer>>() {});
        container.registerSingletonInstance(Ghosts.IBase.class, "D", singletonD);

        Ghosts.IBase instanceA = container.resolve(Ghosts.IBase.class, "A");
        Ghosts.IBase instanceB = container.resolve(Ghosts.IBase.class, "B");
        Ghosts.IBase instanceC = container.resolve(Ghosts.IBase.class, "C");
        Ghosts.IBase instanceD = container.resolve(Ghosts.IBase.class, "D");

        Assert.assertNotNull(instanceA);
        Assert.assertNotNull(instanceB);
        Assert.assertNotNull(instanceC);
        Assert.assertNotNull(instanceD);

        Assert.assertTrue(instanceA instanceof Ghosts.RawImplementationA);
        Assert.assertSame(singletonB, instanceB);
        Assert.assertTrue(instanceC instanceof Ghosts.GenericImplementationC<?>);
        //noinspection unchecked
        Ghosts.GenericImplementationC<Integer> typedInstanceC = (Ghosts.GenericImplementationC<Integer>) instanceC;
        Assert.assertTrue(typedInstanceC.aMethod(69));
        Assert.assertSame(singletonD, instanceD);
    }
}
