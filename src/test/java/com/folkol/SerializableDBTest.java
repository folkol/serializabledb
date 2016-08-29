package com.folkol;

import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SerializableDBTest
{
    private Random rng = new Random();

    @Test
    public void saveAndLoadString()
        throws Exception
    {
        String s = UUID.randomUUID().toString();

        String key = SerializableDB.save(s);
        String loaded = SerializableDB.load(key);

        assertEquals(s, loaded);
    }

    @Test
    public void saveAndLoadInteger()
        throws Exception
    {
        Integer i = rng.nextInt();

        String key = SerializableDB.save(i);
        Integer loaded = SerializableDB.load(key);

        assertEquals(i, loaded);
    }

    @Test
    public void saveAndLoadCustomClass()
        throws Exception
    {
        TestClass tc = new TestClass(
            UUID.randomUUID().toString(),
            rng.nextInt(),
            rng.nextInt());

        String key = SerializableDB.save(tc);
        TestClass loaded = SerializableDB.load(key);

        assertEquals(tc, loaded);
    }

    @Test
    public void saveAndLoadListOfStrings()
        throws Exception
    {
        ArrayList<String> ss = new ArrayList<>();
        ss.add(UUID.randomUUID().toString());
        ss.add(UUID.randomUUID().toString());

        String key = SerializableDB.save(ss);
        List<String> loaded = SerializableDB.load(key);

        assertEquals(ss, loaded);
    }

    @Test
    public void loadMissingObject()
        throws Exception
    {
        String missingKey = UUID.randomUUID().toString();

        Serializable loaded = SerializableDB.load(missingKey);

        assertNull(loaded);
    }

    @Test
    public void loadWithDefaultMissingObject()
        throws Exception
    {
        String missingKey = UUID.randomUUID().toString();
        TestClass tc = new TestClass(UUID.randomUUID().toString(), 100, 200);

        Serializable loaded = SerializableDB.load(missingKey, tc);

        assertEquals(tc, loaded);
    }

    public static class TestClass implements Serializable
    {
        String foo;
        int i, j;

        public TestClass(String foo, int i, int j)
        {
            this.foo = foo;
            this.i = i;
            this.j = j;
        }

        @Override
        public String toString()
        {
            return String.format("[%s, %d, %d]", foo, i, j);
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            TestClass testClass = (TestClass) o;

            if (i != testClass.i) return false;
            if (j != testClass.j) return false;
            return foo.equals(testClass.foo);
        }

        @Override
        public int hashCode()
        {
            int result = foo.hashCode();
            result = 31 * result + i;
            result = 31 * result + j;
            return result;
        }
    }
}
