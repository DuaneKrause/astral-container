/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import org.junit.Assert;
import org.junit.Test;

public class AstralTypeTest
{
    @Test
    public void rawType() throws Exception
    {
        AstralType baseType = new AstralType(Ghosts.BaseRaw.class);
        AstralType derivedType = new AstralType(Ghosts.DerivedRaw.class);
        Assert.assertNotEquals(baseType, derivedType);
        Assert.assertNotEquals(baseType.getTypeName(), derivedType.getTypeName());
    }

    @Test
    public void genericType() throws Exception
    {
        AstralType baseType = new AstralType(new TypeRef<Ghosts.BaseGeneric<String>>() {});
        AstralType derivedType = new AstralType(new TypeRef<Ghosts.DerivedGeneric<String>>() {});
        Assert.assertNotEquals(baseType, derivedType);
        Assert.assertNotEquals(baseType.getTypeName(), derivedType.getTypeName());
    }

    @Test
    public void delegatingGenericType() throws Exception
    {
        AstralType baseType = new AstralType(new TypeRef<Ghosts.BaseGeneric<Ghosts.BaseRaw>>() {});
        AstralType derivedType = new AstralType(new TypeRef<Ghosts.BaseGeneric<Ghosts.DerivedRaw>>() {});
        Assert.assertNotEquals(baseType, derivedType);
        Assert.assertNotEquals(baseType.getTypeName(), derivedType.getTypeName());
    }

    @Test
    public void nestedGenericType() throws Exception
    {
        AstralType baseType = new AstralType(new TypeRef<Ghosts.BaseGeneric<Ghosts.BaseGeneric<?>>>() {});
        AstralType comparisonType = new AstralType(new TypeRef<Ghosts.BaseGeneric<Ghosts.BaseGeneric<Ghosts.BaseRaw>>>() {});
        Assert.assertNotEquals(baseType, comparisonType);
        Assert.assertNotEquals(baseType.getTypeName(), comparisonType.getTypeName());
    }


}

