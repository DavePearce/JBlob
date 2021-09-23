# JBlob

##### Table of Contents

1. [Overview](#overview)
1. [Blobs](#blobs)
   1. [Mutation](#mutation)
   1. [Insertion](#insertion)
   1. [Replacement](#replacement)
1. [Proxies](#proxies)
   1. [Immutable](#immutable)
   1. [Mutable](#Mutable)
1. [Layouts](#layouts)
1. [JavaDoc API]()

## Overview

A small library for manipulating immutable binary blobs.

## Blobs

A `Blob` is a generic representation for a chunk of binary data.
Suppose we have the following bytes:

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

### Mutation

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
does not clone the original array_.  We can see this by printing out
the blobs:

```Java
System.out.println("b1: " + b1);
System.out.println("b2: " + b2);
```

Running this gives the following output:

```
b1: [1, 2]
b2: {(0;1;[2])}[1, 2]
```

Here, `b2` contains a single replacement over the contents of `b1` ---
namely, the bytes between `0` and upto (but not including) `1` are
replaced with `[2]` (in this case).

#### Optimisation

The library current performs only limited optimisations.  For example,
consider this continuation:

```Java
Blob b3 = b2.writeByte(0,(byte) 0x03);
```

In this case, `b3` has the representation `{(0;1;[3])}[1, 2]` rather
than `{(0;1;[3])}{(0;1;[2])}[1, 2]` as might be expected.

### Insertion

A `Blob` can be resized in various ways.  For example, we can _insert_
bytes into a `Blob` as follows:

```Java
Blob b4 = b1.insertBytes(0,(byte) 0x03, (byte) 0x04);
Blob b5 = b1.insertByte(2,(byte) 0x03);
// Sanity check b4
assert b4.size() == 4;
assert b4.readShort(0) == 0x304;
// Sanity check b5
assert b5.size() == 3;
assert b5.readByte(2) == 0x3;
```

Here, the contents of `b4` looks like `[3,4,1,2]`, whilst `b5` looks
like `[1,2,3]`.  

### Replacement

Another supported operation is replacing one sequence with another, as
the following illustrates:

```Java
Blob b6 = b4.replaceBytes(1, 2, (byte) 0x05);
// Check
assert b6.size() == 3;
assert b6.readByte(1) == 0x05;
assert b6.readByte(2) == 0x02;
```

Here, the size of `b6` has reduced to `3` because we've replaced two
bytes in `b4` with just one byte.

## Proxies

Whilst reading / writing low-level data types is useful, ultimately we
want the ability to read / write objects.  Here, we look at the
simplest way of getting started with this, and in following sections
we consider more advanced approaches.

### Immutable

A _proxy_ object is a essentially a wrapper around a `Blob` which
provides a more useful (e.g. human readable) interface.  A simple
example is the following `Point` class:

```Java
public class Point {
	private final Blob blob;
	private final int offset;

	public Point(Blob blob, int offset) {
		if((blob.size() - offset) < 8) {
			throw new IllegalArgumentException("insufficient space in blob");
		}	
		this.blob = blob;
		this.offset = offset;
	}

	public int getX() {
		return blob.readInt(offset);
	}

	public int getY() {
		return blob.readInt(offset + 4);
	}
}
```

We can create a `Point` from an existing `Blob` (which must be big
enough), and use this proxy object to access data within the blob
(i.e. the `X` and `Y` coordinates).  Observe that the layout of our
`Point` is hard coded into our proxy (e.g. where an `int` is `4`
bytes, etc).

The following illustrates a simple test:

```Java
Blob blob = Blob(new byte[8]);
blob = blob.writeInt(0, 1);
blob = blob.writeInt(4, 2);
Point p = new Point(blob,0);
assert p.getX() == 1;
assert p.getY() == 2;
```

This simply initialises a `Blob` of sufficient size, and creates a new
proxy `Point` as address `0`.  This proxy then provides convenient
methods for reading data out of the blob.

### Mutable


## Layouts
