/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import org.junit.Assert;

public class StringAssert
{
    public static void containsIgnoreCase(String message, String token) {
        Assert.assertTrue(
                String.format("Expected string to contain '%s', was '%s'", token, message),
                message.toUpperCase().contains(token.toUpperCase()));
    }

    public static void contains(String message, String token) {
        Assert.assertTrue(
                String.format("Expected string to contain '%s', was '%s'", token, message),
                message.contains(token));
    }
}
