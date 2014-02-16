package com.github.linfro.core;

import com.github.linfro.core.value.HasValue;
import com.github.linfro.core.value.TestGetValue;
import org.junit.Test;

import static com.github.linfro.core.common.ObjectUtil.nvl;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author Dmitry Ermakov
 * @version 2014-02-16
 * @since 1.0.0
 */
public class Flow_Union_Test {
    @Test
    public void testOutFlowUnion() throws Exception {
        TestGetValue<Integer> a = TestGetValue.newGetValue();
        TestGetValue<Integer> b = TestGetValue.newGetValue();
        TestGetValue<Integer> c = TestGetValue.newGetValue();

        HasValue<Integer> res = Flow.newHasValue();

        a.flow().union(b, c).map(
                (args) -> args.stream().mapToInt((arg) -> nvl(arg, 0)).sum()
        ).to(res);

        assertNull(res.getValue());

        a.update(1);
        assertEquals(new Integer(1), res.getValue());

        b.update(2);
        assertEquals(new Integer(3), res.getValue());

        c.update(3);
        assertEquals(new Integer(6), res.getValue());
    }
}
