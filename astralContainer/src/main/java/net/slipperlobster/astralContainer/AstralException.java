/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

public class AstralException extends Exception
{
    AstralException(String message) { super(message); }
    AstralException(String msgFormat, Object... parts) { super(String.format(msgFormat, parts)); }
    AstralException(Exception inner, String message) { super(message, inner); }
    AstralException(Exception inner, String msgFormat, Object... parts) { super(String.format(msgFormat, parts), inner); }
}
