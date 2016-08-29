package com.folkol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * <p>SerializableDB is a naive object store that can save
 * and load any object implementing Serializable.</p>
 */
public class SerializableDB
{
    private static final String DB_NAME = "data";

    /**
     * Save the given object under a random key.
     * @return the key under which the object was stored.
     * @throws IOException
     */
    public static String save(Serializable s)
        throws IOException
    {
        return save(UUID.randomUUID().toString(), s);
    }

    /**
     * Save the given object under the given key.
     * @return the key under which the object was stored
     * @throws IOException
     */
    public static String save(String key, Serializable s)
        throws IOException
    {
        Path path = Paths.get(DB_NAME, key);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(s);
        Files.write(path, baos.toByteArray());

        return key;
    }

    /**
     * <p>Load the object stored under the given key.</p>
     * <p>The return value is parameterized for convenience, but it is up
     * to the programmer to make sure that the object stored under this
     * key is of the correct type.</p>
     * @return the deserialized object
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T load(String key)
        throws ClassNotFoundException, IOException
    {
        return load(key, null);
    }

    /**
     * Same as {@link SerializableDB#load(String)}, but will return the given default object
     * if there was no object stored under the given key.
     * @return the deserialized object
     * @throws ClassNotFoundException
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T load(String key, T def)
        throws ClassNotFoundException, IOException
    {
        Path path = Paths.get(DB_NAME, key);
        try {
            byte[] bytes = Files.readAllBytes(path);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (NoSuchFileException e) {
            return def;
        }
    }
}
