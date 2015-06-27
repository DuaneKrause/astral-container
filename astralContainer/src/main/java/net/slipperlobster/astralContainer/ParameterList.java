/**
 * astral
 * (c) Copyright 2015 Duane Krause
 * All rights reserved.
 */

package net.slipperlobster.astralContainer;

import javafx.util.Pair;

import java.util.ArrayList;

public class ParameterList extends ArrayList<Pair<AstralTypeId, Object>>
{
    public ParameterList() {}
    public ParameterList(int initialSize) { super(initialSize); }

    public void add(AstralTypeId typeId, Object value)
    {
        super.add(new Pair<>(typeId, value));
    }

    public Object[] getValues()
    {
        Object[] values = new Object[size()];
        for (int i = 0; i < size(); ++i)
        {
            values[i] = get(i).getValue();
        }
        return values;
    }
}
