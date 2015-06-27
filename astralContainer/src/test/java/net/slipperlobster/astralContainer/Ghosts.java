/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import org.junit.Assert;
import org.junit.Test;

/**
 * a collection of test classes that don't really exist in the physical plane at all
 */
public class Ghosts
{
    static public class BaseRaw {}
    static public class DerivedRaw extends BaseRaw {}

    static public class BaseGeneric<T> {}
    static public class DerivedGeneric<T> extends BaseGeneric<T> {}

    public interface IBase {}

    static public class SingleComposedImpl implements IBase {
        public BaseRaw _p1;
        public SingleComposedImpl(BaseRaw p1) { _p1 = p1; }
    }

    static public class BackReferenceComposedImpl extends BaseRaw {
        public IBase _p1;
        public BackReferenceComposedImpl(IBase p1) { _p1 = p1; }
    }

    static public class RawImplementationA implements IBase {}
    static public class RawImplementationB implements IBase {}
    static public class GenericImplementationC<T> implements IBase {
        public boolean aMethod(T param) { return true; }
    }
    static public class GenericImplementationD<T> implements IBase {}

    @Test
    public void creatable() throws IllegalAccessException, InstantiationException
    {
        DerivedRaw derivedRaw = DerivedRaw.class.newInstance();
        Assert.assertNotNull(derivedRaw);
    }
}
