/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import org.junit.Test;

import org.junit.Assert;

import java.lang.reflect.Type;
import java.util.List;

public class TypeRefTest
{
    @Test
    public void testNonGeneric() throws Exception
    {
        TypeRef<String> typeRef = new TypeRef<String>() {};
        Type type = typeRef.getType();
        Assert.assertNotNull(type);
        Assert.assertEquals(
                "java.lang.String",
                type.getTypeName());
    }

    @Test
    public void testLibraryGeneric() throws Exception
    {
        TypeRef<List<String>> typeRef = new TypeRef<List<String>>() {};
        Type type = typeRef.getType();
        Assert.assertNotNull(type);
        Assert.assertEquals(
                "java.util.List<java.lang.String>",
                type.getTypeName());
    }

    @Test
    public void testGhostGeneric() throws Exception
    {
        TypeRef<Ghosts.BaseGeneric<String>> typeRef = new TypeRef<Ghosts.BaseGeneric<String>>() {};
        Type type = typeRef.getType();
        Assert.assertNotNull(type);
        Assert.assertEquals(
                "net.slipperlobster.astralContainer.Ghosts.net.slipperlobster.astralContainer.Ghosts$BaseGeneric<java.lang.String>",
                type.getTypeName());
    }
}