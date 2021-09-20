# JBlob

A small library for manipulating immutable binary blobs.  For example,
suppose we have the following bytes:

```Java
byte[] bytes = {0x01,0x02};
```

Then, we can create a `ByteBlob` from as follows:

```Java
Blob b1 = new ByteBlob(bytes);
```

Using this, we can read the original bytes in various ways.

```Java
// Read bytes individually
assert b1.readByte(0) == 0x01;
assert b1.readByte(1) == 0x02;
// Read bytes together
assert b1.readShort(0) == 0x102;
```

This extends to other primitive layouts, such as `int`, `long`, etc.

## Mutation

A `Blob` is always _immutable_ which means, for example, the `byte`
array behind a `ByteBlob` cannot be modified the `Blob` API.  However,
the `Blob` API supports _functional writes_.  That is, writing to a
`Blob` produces another `Blob`.  For example, consider the following
continuation from our example above:

```Java
Blob b2 = b1.writeByte(0,(byte) 0x02);
// Check nothing changed in b1
assert b1.readShort(0) == 0x102;
// Check contents of b2
assert b2.readShort(0) == 0x202;
```

A key aspect of the library is that such blobs are maintained as
instances of `Blob.Diff` over their parents.  _Thus, the above write
does not clone the original array_.