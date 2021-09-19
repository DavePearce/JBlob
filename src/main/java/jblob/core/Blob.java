package jblob.core;

/**
 * Represents an immutable binary blob of data representing a piece of content..
 * Blobs are immutable data structures which, when written, construct new blobs.
 * Blobs are also elastic in that they automatically resize to accommodate
 * writing beyond their current bounds.
 *
 * @author David J. Pearce
 *
 */
public interface Blob {
	/**
	 * Get the total number of bytes in this blob.
	 *
	 * @return
	 */
	public int size();

	/**
	 * Get the complete contents of this value as a sequence of bytes.
	 *
	 * @return
	 */
	public byte[] readAll();

	/**
	 * Read a given byte from a given position in the blob. The index must be within
	 * bounds.
	 *
	 * @param index
	 * @return
	 */
	public byte readByte(int index);

	/**
	 * Read a 16-bit signed integer starting at a given position in the blob
	 * assuming big endian orientation. All indices must be entirely within bounds.
	 *
	 * @param index
	 * @return
	 */
	public short readShort(int index);

	/**
	 * Read a 32-bit signed integer starting at a given position in the blob
	 * assuming big endian orientation. All indices must be entirely within bounds.
	 *
	 * @param index
	 * @return
	 */
	public int readInt(int index);

	/**
	 * Read a given sequence of bytes from a given position in the blob. The entire
	 * region must be within bounds.
	 *
	 * @param index
	 * @param length
	 * @return
	 */
	public byte[] readBytes(int index, int length);

	/**
	 * Read a given sequence of bytes from a given position in the blob into a
	 * prexisting array. The entire region must be within bounds.
	 *
	 * @param index     The index within this blob to start reading from
	 * @param length    The number of bytes to read
	 * @param dest      The destination byte array
	 * @param destStart starting position within destination
	 * @return
	 */
	public void readBytes(int index, int length, byte[] dest, int destStart);

	/**
	 * Write a given byte to a given position within this value. The index does not
	 * need to be in bounds since blobs are elastic. Thus, writing beyond bounds
	 * increases the size of the blob accordingly.
	 *
	 * @param index Position to overwrite
	 * @param b     data byte to written
	 * @return
	 */
	public Diff writeByte(int index, byte b);

	/**
	 * Write a given 16-bit signed integer byte to a given position within this blob
	 * assuming a big endian orientation. The index does not need to be in bounds
	 * since blobs are elastic. Thus, writing beyond bounds increases the size of
	 * the blob accordingly.
	 *
	 * @param index Position to overwrite
	 * @param b     data byte to written
	 * @return
	 */
	public Diff writeShort(int index, short i16);

	/**
	 * Write a given 32-bit signed integer byte to a given position within this blob
	 * assuming a big endian orientation. The index does not need to be in bounds
	 * since blobs are elastic. Thus, writing beyond bounds increases the size of
	 * the blob accordingly.
	 *
	 * @param index Position to overwrite
	 * @param b     data byte to written
	 * @return
	 */
	public Diff writeInt(int index, int i32);

	/**
	 * Replace a given section of this value with a new sequence of bytes. The index
	 * does not need to be in bounds since blobs are elastic. Thus, writing beyond
	 * bounds increases the size of the blob accordingly.
	 *
	 * @param index Position to overwrite
	 * @param b     data byte to written
	 * @return
	 */
	public Diff writeBytes(int index, byte... bytes);

	/**
	 * Replace a given section of this value with a new sequence of bytes. The new
	 * byte sequence does not need to be the same length as the section replaced.
	 * The index does not need to be in bounds since blobs are elastic. Thus,
	 * writing beyond bounds increases the size of the blob accordingly.
	 *
	 * @param index  starting offset of section being replaced
	 * @param length size of section being replaced
	 * @param b      data byte to written
	 * @return
	 */
	public Diff replaceBytes(int index, int length, byte... bytes);

	/**
	 * Insert a given byte to a given position within this value. The index does not
	 * need to be in bounds since blobs are elastic. Thus, inserting beyond bounds
	 * increases the size of the blob accordingly.
	 *
	 * @param index Position to insert before
	 * @param b     data byte to written
	 * @return
	 */
	public Diff insertByte(int index, byte b);

	/**
	 * Insert a given 16-bit signed integer byte to a given position within this
	 * blob assuming a big endian orientation. The index does not need to be in
	 * bounds since blobs are elastic. Thus, insert beyond bounds increases the size
	 * of the blob accordingly.
	 *
	 * @param index Position to insert before
	 * @param b     data byte to written
	 * @return
	 */
	public Diff insertShort(int index, short i16);

	/**
	 * Insert a given 32-bit signed integer byte to a given position within this
	 * blob assuming a big endian orientation. The index does not need to be in
	 * bounds since blobs are elastic. Thus, insert beyond bounds increases the size
	 * of the blob accordingly.
	 *
	 * @param index Position to insert before
	 * @param b     data byte to written
	 * @return
	 */
	public Diff insertInt(int index, int i32);

	/**
	 * Insert a given section of this value with a new sequence of bytes. The index
	 * does not need to be in bounds since blobs are elastic. Thus, insert beyond
	 * bounds increases the size of the blob accordingly.
	 *
	 * @param index Position to insert before
	 * @param b     data byte to written
	 * @return
	 */
	public Diff insertBytes(int index, byte... bytes);

	/**
	 * Merge two (disjoint) siblings together into one. For this to be valid, they
	 * must either: both have the same parent; or one is the immediate parent of the
	 * other.
	 *
	 * @param parent
	 * @return
	 */
	public Blob merge(Blob sibling);

	/**
	 * Replacements cannot overlap.
	 *
	 * @author David J. Pearce
	 *
	 */
	public interface Diff extends Blob {
		/**
		 * Get the parent of this blob, if one exists.
		 *
		 * @return
		 */
		public Blob parent();

		/**
		 * Get the number of replacements in this diff.
		 *
		 * @return
		 */
		public int count();

		/**
		 * Get a specific replacement.
		 * @return
		 */
		public Replacement getReplacement(int i);

	}

	public interface Replacement {
		/**
		 * Get the starting offset of this replacement.
		 * @return
		 */
		public int offset();

		/**
		 * Get the length of this replacement.
		 *
		 * @return
		 */
		public int size();

		/**
		 * Get the array of replacement bytes.
		 *
		 * @return
		 */
		public byte[] bytes();
	}
}
