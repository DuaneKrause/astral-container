/**
 * astralContainer
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import org.junit.Assert;
import org.junit.Test;

public class CircularReferenceTest
{
    @Test
    public void circularReferenceDetected() throws Exception
    {
        AstralContainer container = new AstralContainer();
        container.register(Ghosts.IBase.class, Ghosts.SingleComposedImpl.class);
        container.register(Ghosts.BaseRaw.class, Ghosts.BackReferenceComposedImpl.class);

        try {
            container.resolve(Ghosts.IBase.class);
            Assert.fail("expected a ConfigurationException");
        } catch (AstralException aex) {
            System.out.println("Caught expected exception " + aex.toString());
            String message = aex.getMessage();
            StringAssert.containsIgnoreCase(message, "circular reference detected");
            StringAssert.contains(message, "IBase");
            StringAssert.contains(message, "SingleComposedImpl");
            StringAssert.contains(message, "BackReferenceComposedImpl");
        }
    }

    @Test
    public void circularReferenceDetectedInValidate() throws Exception
    {
        AstralContainer container = new AstralContainer();
        container.register(Ghosts.IBase.class, Ghosts.SingleComposedImpl.class);
        container.register(Ghosts.BaseRaw.class, Ghosts.BackReferenceComposedImpl.class);

        try {
            container.validate();
            Assert.fail("expected a ConfigurationException");
        } catch (AstralException aex) {
            System.out.println("Caught expected exception " + aex.toString());
            String message = aex.getMessage();
            StringAssert.containsIgnoreCase(message, "circular reference detected");
            StringAssert.contains(message, "IBase");
            StringAssert.contains(message, "SingleComposedImpl");
            StringAssert.contains(message, "BackReferenceComposedImpl");
        }
    }
}
