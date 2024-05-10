/*
 * Copyright IBM Corp. and others 2016
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at https://www.eclipse.org/legal/epl-2.0/
 * or the Apache License, Version 2.0 which accompanies this distribution and
 * is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following
 * Secondary Licenses when the conditions for such availability set
 * forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
 * General Public License, version 2 with the GNU Classpath
 * Exception [1] and GNU General Public License, version 2 with the
 * OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] https://openjdk.org/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0-only WITH Classpath-exception-2.0 OR GPL-2.0-only WITH OpenJDK-assembly-exception-1.0
 */
package org.openj9.test.varhandle;

import static java.lang.invoke.MethodHandles.*;

import org.testng.annotations.*;
import org.testng.*;

import java.lang.invoke.*;
import java.nio.*;

/**
 * Test ByteBufferViewVarHandle operations
 *
 * @author Bjorn Vardal
 */
@Test(groups = { "level.extended" })
public class ByteBufferViewVarHandleTests extends ViewVarHandleTests {
	/**
	 * Initialize the expected values based on the provided {@code byteOrder}.
	 * Initialize {@link ByteBufferViewVarHandleTests#_buffer _buffer} based on the provided {@code memoryLocation}.
	 * Initialize the {@link VarHandle VarHandles} based on the provided {@code byteOrder}.
	 *
	 * @param byteOrder "bigEndian" or "littleEndian". Determines the {@link ByteOrder} to use when reading and writing.
	 * @param memoryLocation "onHeap" or "offHeap". Determines whether the ByteBuffer is direct.
	 */
	@Parameters({ "byteOrder", "memoryLocation" })
	public ByteBufferViewVarHandleTests(String byteOrder, String memoryLocation) {
		super(byteOrder);

		if (memoryLocation.equals("onHeap")) {
			_buffer = ByteBufferViewHelper.onHeap;
		} else if (memoryLocation.equals("offHeap")) {
			_buffer = ByteBufferViewHelper.offHeap;
		} else {
			throw new TestException(memoryLocation + " is an invalid memory location. Should be either onHeap or offHeap.");
		}

		vhChar = byteBufferViewVarHandle(char[].class, _byteOrder);
		vhDouble = byteBufferViewVarHandle(double[].class, _byteOrder);
		vhFloat = byteBufferViewVarHandle(float[].class, _byteOrder);
		vhInt = byteBufferViewVarHandle(int[].class, _byteOrder);
		vhLong = byteBufferViewVarHandle(long[].class, _byteOrder);
		vhShort = byteBufferViewVarHandle(short[].class, _byteOrder);
	}

	/**
	 * Perform all the operations available on a ByteBufferViewVarHandle viewed as char elements.
	 */
	@Test
	public void testChar() {

		ByteBufferViewHelper.reset();
		/* Get */
		char cFromVH = (char)vhChar.get(_buffer, 0);
		Assert.assertEquals(FIRST_CHAR, cFromVH);

		/* Set */
		vhChar.set(_buffer, 0, CHANGED_CHAR);
		checkUpdated2(0);

		ByteBufferViewHelper.reset();
		/* GetOpaque */
		try {
			cFromVH = (char) vhChar.getOpaque(_buffer, 0);
			Assert.assertEquals(FIRST_CHAR, cFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetOpaque */
		try {
			vhChar.setOpaque(_buffer, 0, CHANGED_CHAR);
			checkUpdated2(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetVolatile */
		try {
			cFromVH = (char) vhChar.getVolatile(_buffer, 0);
			Assert.assertEquals(FIRST_CHAR, cFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetVolatile */
		try {
			vhChar.setVolatile(_buffer, 0, CHANGED_CHAR);
			checkUpdated2(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetAcquire */
		try {
			cFromVH = (char) vhChar.getAcquire(_buffer, 0);
			Assert.assertEquals(FIRST_CHAR, cFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetRelease */
		try {
			vhChar.setRelease(_buffer, 0, CHANGED_CHAR);
			checkUpdated2(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		@SuppressWarnings("unused")
		boolean casResult;
		@SuppressWarnings("unused")
		char caeResult;

		/* CompareAndSet */
		try {
			casResult = vhChar.compareAndSet(_buffer, 0, '1', '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* compareAndExchange */
		try {
			caeResult = (char)vhChar.compareAndExchange(_buffer, 0, '1', '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* compareAndExchangeAcquire */
		try {
			caeResult = (char)vhChar.compareAndExchangeAcquire(_buffer, 0, '1', '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* compareAndExchangeRelease */
		try {
			caeResult = (char)vhChar.compareAndExchangeRelease(_buffer, 0, '1', '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* WeakCompareAndSet */
		try {
			casResult = vhChar.weakCompareAndSet(_buffer, 0, '1', '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* WeakCompareAndSetAcquire */
		try {
			casResult = vhChar.weakCompareAndSetAcquire(_buffer, 0, '1', '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* WeakCompareAndSetRelease */
		try {
			casResult = vhChar.weakCompareAndSetRelease(_buffer, 0, '1', '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* WeakCompareAndSetPlain */
		try {
			casResult = vhChar.weakCompareAndSetPlain(_buffer, 0, '1', '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndSet */
		try {
			cFromVH = (char)vhChar.getAndSet(_buffer, 0, CHANGED_CHAR);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndSetAcquire */
		try {
			cFromVH = (char)vhChar.getAndSetAcquire(_buffer, 0, CHANGED_CHAR);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndSetRelease */
		try {
			cFromVH = (char)vhChar.getAndSetRelease(_buffer, 0, CHANGED_CHAR);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndAdd */
		try {
			cFromVH = (char)vhChar.getAndAdd(_buffer, 0, '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndAddAcquire */
		try {
			cFromVH = (char)vhChar.getAndAddAcquire(_buffer, 0, '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndAddRelease */
		try {
			cFromVH = (char)vhChar.getAndAddRelease(_buffer, 0, '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseAnd */
		try {
			cFromVH = (char)vhChar.getAndBitwiseAnd(_buffer, 0, '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseAndAcquire */
		try {
			cFromVH = (char)vhChar.getAndBitwiseAndAcquire(_buffer, 0, '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseAndRelease */
		try {
			cFromVH = (char)vhChar.getAndBitwiseAndRelease(_buffer, 0, '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseOr */
		try {
			cFromVH = (char)vhChar.getAndBitwiseOr(_buffer, 0, '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseOrAcquire */
		try {
			cFromVH = (char)vhChar.getAndBitwiseOrAcquire(_buffer, 0, '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseOrRelease */
		try {
			cFromVH = (char)vhChar.getAndBitwiseOrRelease(_buffer, 0, '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseXor */
		try {
			cFromVH = (char)vhChar.getAndBitwiseXor(_buffer, 0, '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseXorAcquire */
		try {
			cFromVH = (char)vhChar.getAndBitwiseXorAcquire(_buffer, 0, '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseXorRelease */
		try {
			cFromVH = (char)vhChar.getAndBitwiseXorRelease(_buffer, 0, '2');
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}
	}

	/**
	 * Get and set the last element of a ByteBufferViewVarHandle viewed as char elements.
	 */
	@Test
	public void testCharLastElement() {
		int lastCompleteIndex = lastCompleteIndex(Character.BYTES);
		ByteBufferViewHelper.reset();

		/* Get */
		char cFromVH = (char)vhChar.get(_buffer, lastCompleteIndex);
		Assert.assertEquals(LAST_CHAR, cFromVH);

		/* Set */
		vhChar.set(_buffer, lastCompleteIndex, CHANGED_CHAR);
		checkUpdated2(lastCompleteIndex);
	}

	/**
	 * Perform all the operations available on a ByteBufferViewVarHandle viewed as double elements.
	 */
	@Test
	public void testDouble() {
		ByteBufferViewHelper.reset();
		/* Get */
		double dFromVH = (double)vhDouble.get(_buffer, 0);
		assertEquals(FIRST_DOUBLE, dFromVH);

		/* Set */
		vhDouble.set(_buffer, 0, CHANGED_DOUBLE);
		checkUpdated8(0);

		ByteBufferViewHelper.reset();
		/* GetOpaque */
		try {
			dFromVH = (double)vhDouble.getOpaque(_buffer, 0);
			assertEquals(FIRST_DOUBLE, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetOpaque */
		try {
			vhDouble.setOpaque(_buffer, 0, CHANGED_DOUBLE);
			assertEquals(FIRST_DOUBLE, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetVolatile */
		try {
			dFromVH = (double)vhDouble.getVolatile(_buffer, 0);
			assertEquals(FIRST_DOUBLE, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetVolatile */
		try {
			vhDouble.setVolatile(_buffer, 0, CHANGED_DOUBLE);
			assertEquals(FIRST_DOUBLE, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetAcquire */
		try {
			dFromVH = (double)vhDouble.getAcquire(_buffer, 0);
			assertEquals(FIRST_DOUBLE, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetRelease */
		try {
			vhDouble.setRelease(_buffer, 0, CHANGED_DOUBLE);
			assertEquals(FIRST_DOUBLE, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndSet - Fail */
		ByteBufferViewHelper.reset();
		boolean casResult = false;
		try {
			casResult = vhDouble.compareAndSet(_buffer, 0, 2.0, 3.0);
			checkNotUpdated8();
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndSet - Succeed */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhDouble.compareAndSet(_buffer, 0, FIRST_DOUBLE, CHANGED_DOUBLE);
			checkUpdated8(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchange - Fail */
		ByteBufferViewHelper.reset();
		double caeResult = 0;
		try {
			caeResult = (double)vhDouble.compareAndExchange(_buffer, 0, 1.0, 2.0);
			checkNotUpdated8();
			assertEquals(FIRST_DOUBLE, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchange - Succeed */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (double)vhDouble.compareAndExchange(_buffer, 0, FIRST_DOUBLE, CHANGED_DOUBLE);
			checkUpdated8(0);
			assertEquals(FIRST_DOUBLE, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchangeAcquire - Fail */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (double)vhDouble.compareAndExchangeAcquire(_buffer, 0, 1.0, 2.0);
			checkNotUpdated8();
			assertEquals(FIRST_DOUBLE, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchangeAcquire - Succeed */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (double)vhDouble.compareAndExchangeAcquire(_buffer, 0, FIRST_DOUBLE, CHANGED_DOUBLE);
			checkUpdated8(0);
			assertEquals(FIRST_DOUBLE, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchangeRelease - Fail */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (double)vhDouble.compareAndExchangeRelease(_buffer, 0, 1.0, 2.0);
			checkNotUpdated8();
			assertEquals(FIRST_DOUBLE, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchangeRelease - Succeed */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (double)vhDouble.compareAndExchangeRelease(_buffer, 0, FIRST_DOUBLE, CHANGED_DOUBLE);
			checkUpdated8(0);
			assertEquals(FIRST_DOUBLE, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSet - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhDouble.weakCompareAndSet(_buffer, 0, 2.0, 3.0);
			checkNotUpdated8();
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSet - Succeed */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhDouble.weakCompareAndSet(_buffer, 0, FIRST_DOUBLE, CHANGED_DOUBLE);
			checkUpdated8(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetAcquire - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhDouble.weakCompareAndSetAcquire(_buffer, 0, 2.0, 3.0);
			checkNotUpdated8();
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetAcquire - Succeed */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhDouble.weakCompareAndSetAcquire(_buffer, 0, FIRST_DOUBLE, CHANGED_DOUBLE);
			checkUpdated8(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetRelease - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhDouble.weakCompareAndSetRelease(_buffer, 0, 2.0, 3.0);
			checkNotUpdated8();
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetRelease - Succeed */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhDouble.weakCompareAndSetRelease(_buffer, 0, FIRST_DOUBLE, CHANGED_DOUBLE);
			checkUpdated8(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetPlain - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhDouble.weakCompareAndSetPlain(_buffer, 0, 2.0, 3.0);
			checkNotUpdated8();
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetPlain - Succeed */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhDouble.weakCompareAndSetPlain(_buffer, 0, FIRST_DOUBLE, CHANGED_DOUBLE);
			checkUpdated8(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSet */
		ByteBufferViewHelper.reset();
		try {
			dFromVH = (double)vhDouble.getAndSet(_buffer, 0, CHANGED_DOUBLE);
			checkUpdated8(0);
			assertEquals(FIRST_DOUBLE, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSet */
		ByteBufferViewHelper.reset();
		try {
			dFromVH = (double)vhDouble.getAndSet(_buffer, 0, CHANGED_DOUBLE);
			checkUpdated8(0);
			assertEquals(FIRST_DOUBLE, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSet */
		ByteBufferViewHelper.reset();
		try {
			dFromVH = (double)vhDouble.getAndSet(_buffer, 0, CHANGED_DOUBLE);
			checkUpdated8(0);
			assertEquals(FIRST_DOUBLE, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndAdd */
		try {
			dFromVH = (double)vhDouble.getAndAdd(_buffer, 0, 2.0);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndAddAcquire */
		try {
			dFromVH = (double)vhDouble.getAndAddAcquire(_buffer, 0, 2.0);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndAddRelease */
		try {
			dFromVH = (double)vhDouble.getAndAddRelease(_buffer, 0, 2.0);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseAnd */
		try {
			dFromVH = (double)vhDouble.getAndBitwiseAnd(_buffer, 0, 2.0);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseAndAcquire */
		try {
			dFromVH = (double)vhDouble.getAndBitwiseAndAcquire(_buffer, 0, 2.0);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseAndRelease */
		try {
			dFromVH = (double)vhDouble.getAndBitwiseAndRelease(_buffer, 0, 2.0);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseOr */
		try {
			dFromVH = (double)vhDouble.getAndBitwiseOr(_buffer, 0, 2.0);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseOrAcquire */
		try {
			dFromVH = (double)vhDouble.getAndBitwiseOrAcquire(_buffer, 0, 2.0);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseOrRelease */
		try {
			dFromVH = (double)vhDouble.getAndBitwiseOrRelease(_buffer, 0, 2.0);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseXor */
		try {
			dFromVH = (double)vhDouble.getAndBitwiseXor(_buffer, 0, 2.0);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseXorAcquire */
		try {
			dFromVH = (double)vhDouble.getAndBitwiseXorAcquire(_buffer, 0, 2.0);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseXorRelease */
		try {
			dFromVH = (double)vhDouble.getAndBitwiseXorRelease(_buffer, 0, 2.0);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}
	}

	/**
	 * Get and set the last element of a ByteBufferViewVarHandle viewed as double elements.
	 */
	@Test
	public void testDoubleLastElement() {
		int lastCompleteIndex = lastCompleteIndex(Double.BYTES);
		ByteBufferViewHelper.reset();
		/* Get */
		double dFromVH = (double)vhDouble.get(_buffer, lastCompleteIndex);
		assertEquals(LAST_DOUBLE, dFromVH);

		/* Set */
		vhDouble.set(_buffer, lastCompleteIndex, CHANGED_DOUBLE);
		checkUpdated8(lastCompleteIndex);
	}

	/**
	 * Perform all the operations available on a ByteBufferViewVarHandle viewed as float elements.
	 */
	@Test
	public void testFloat() {
		ByteBufferViewHelper.reset();
		/* Get */
		float dFromVH = (float)vhFloat.get(_buffer, 0);
		assertEquals(FIRST_FLOAT, dFromVH);

		/* Set */
		vhFloat.set(_buffer, 0, CHANGED_FLOAT);
		checkUpdated4(0);

		ByteBufferViewHelper.reset();
		/* GetOpaque */
		try {
			dFromVH = (float)vhFloat.getOpaque(_buffer, 0);
			assertEquals(FIRST_FLOAT, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetOpaque */
		try {
			vhFloat.setOpaque(_buffer, 0, CHANGED_FLOAT);
			assertEquals(FIRST_FLOAT, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetVolatile */
		try {
			dFromVH = (float)vhFloat.getVolatile(_buffer, 0);
			assertEquals(FIRST_FLOAT, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetVolatile */
		try {
			vhFloat.setVolatile(_buffer, 0, CHANGED_FLOAT);
			assertEquals(FIRST_FLOAT, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetAcquire */
		try {
			dFromVH = (float)vhFloat.getAcquire(_buffer, 0);
			assertEquals(FIRST_FLOAT, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetRelease */
		try {
			vhFloat.setRelease(_buffer, 0, CHANGED_FLOAT);
			assertEquals(FIRST_FLOAT, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndSet - Fail */
		ByteBufferViewHelper.reset();
		boolean casResult = false;
		try {
			casResult = vhFloat.compareAndSet(_buffer, 0, 2.0f, 3.0f);
			checkNotUpdated4(0);
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndSet - Succeed */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhFloat.compareAndSet(_buffer, 0, FIRST_FLOAT, CHANGED_FLOAT);
			checkUpdated4(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchange - Fail */
		ByteBufferViewHelper.reset();
		float caeResult = 0;
		try {
			caeResult = (float)vhFloat.compareAndExchange(_buffer, 0, 1.0f, 2.0f);
			checkNotUpdated4(0);
			assertEquals(FIRST_FLOAT, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchange - Succeed */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (float)vhFloat.compareAndExchange(_buffer, 0, FIRST_FLOAT, CHANGED_FLOAT);
			checkUpdated4(0);
			assertEquals(FIRST_FLOAT, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchangeAcquire - Fail */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (float)vhFloat.compareAndExchangeAcquire(_buffer, 0, 1.0f, 2.0f);
			checkNotUpdated4(0);
			assertEquals(FIRST_FLOAT, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchangeAcquire - Succeed */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (float)vhFloat.compareAndExchangeAcquire(_buffer, 0, FIRST_FLOAT, CHANGED_FLOAT);
			checkUpdated4(0);
			assertEquals(FIRST_FLOAT, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchangeRelease - Fail */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (float)vhFloat.compareAndExchangeRelease(_buffer, 0, 1.0f, 2.0f);
			checkNotUpdated4(0);
			assertEquals(FIRST_FLOAT, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchangeRelease - Succeed */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (float)vhFloat.compareAndExchangeRelease(_buffer, 0, FIRST_FLOAT, CHANGED_FLOAT);
			checkUpdated4(0);
			assertEquals(FIRST_FLOAT, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSet - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhFloat.weakCompareAndSet(_buffer, 0, 2.0f, 3.0f);
			checkNotUpdated4(0);
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSet - Succeed */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhFloat.weakCompareAndSet(_buffer, 0, FIRST_FLOAT, CHANGED_FLOAT);
			checkUpdated4(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetAcquire - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhFloat.weakCompareAndSetAcquire(_buffer, 0, 2.0f, 3.0f);
			checkNotUpdated4(0);
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetAcquire - Succeed */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhFloat.weakCompareAndSetAcquire(_buffer, 0, FIRST_FLOAT, CHANGED_FLOAT);
			checkUpdated4(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetRelease - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhFloat.weakCompareAndSetRelease(_buffer, 0, 2.0f, 3.0f);
			checkNotUpdated4(0);
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetRelease - Succeed */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhFloat.weakCompareAndSetRelease(_buffer, 0, FIRST_FLOAT, CHANGED_FLOAT);
			checkUpdated4(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetPlain - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhFloat.weakCompareAndSetPlain(_buffer, 0, 2.0f, 3.0f);
			checkNotUpdated4(0);
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetPlain - Succeed */
		ByteBufferViewHelper.reset();
		try {
			casResult = vhFloat.weakCompareAndSetPlain(_buffer, 0, FIRST_FLOAT, CHANGED_FLOAT);
			checkUpdated4(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSet */
		ByteBufferViewHelper.reset();
		try {
			dFromVH = (float)vhFloat.getAndSet(_buffer, 0, CHANGED_FLOAT);
			checkUpdated4(0);
			assertEquals(FIRST_FLOAT, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSet */
		ByteBufferViewHelper.reset();
		try {
			dFromVH = (float)vhFloat.getAndSet(_buffer, 0, CHANGED_FLOAT);
			checkUpdated4(0);
			assertEquals(FIRST_FLOAT, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSet */
		ByteBufferViewHelper.reset();
		try {
			dFromVH = (float)vhFloat.getAndSet(_buffer, 0, CHANGED_FLOAT);
			checkUpdated4(0);
			assertEquals(FIRST_FLOAT, dFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}


		/* GetAndAdd */
		try {
			dFromVH = (float)vhFloat.getAndAdd(_buffer, 0, 2.0f);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndAddAcquire */
		try {
			dFromVH = (float)vhFloat.getAndAddAcquire(_buffer, 0, 2.0f);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndAddRelease */
		try {
			dFromVH = (float)vhFloat.getAndAddRelease(_buffer, 0, 2.0f);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseAnd */
		try {
			dFromVH = (float)vhFloat.getAndBitwiseAnd(_buffer, 0, 2.0f);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseAndAcquire */
		try {
			dFromVH = (float)vhFloat.getAndBitwiseAndAcquire(_buffer, 0, 2.0f);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseAndRelease */
		try {
			dFromVH = (float)vhFloat.getAndBitwiseAndRelease(_buffer, 0, 2.0f);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseOr */
		try {
			dFromVH = (float)vhFloat.getAndBitwiseOr(_buffer, 0, 2.0f);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseOrAcquire */
		try {
			dFromVH = (float)vhFloat.getAndBitwiseOrAcquire(_buffer, 0, 2.0f);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseOrRelease */
		try {
			dFromVH = (float)vhFloat.getAndBitwiseOrRelease(_buffer, 0, 2.0f);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseXor */
		try {
			dFromVH = (float)vhFloat.getAndBitwiseXor(_buffer, 0, 2.0f);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseXorAcquire */
		try {
			dFromVH = (float)vhFloat.getAndBitwiseXorAcquire(_buffer, 0, 2.0f);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseXorRelease */
		try {
			dFromVH = (float)vhFloat.getAndBitwiseXorRelease(_buffer, 0, 2.0f);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}
	}

	/**
	 * Get and set the last element of a ByteBufferViewVarHandle viewed as float elements.
	 */
	@Test
	public void testFloatLastElement() {
		int lastCompleteIndex = lastCompleteIndex(Float.BYTES);
		ByteBufferViewHelper.reset();
		/* Get */
		float fFromVH = (float)vhFloat.get(_buffer, lastCompleteIndex);
		assertEquals(LAST_FLOAT, fFromVH);

		/* Set */
		vhFloat.set(_buffer, lastCompleteIndex, CHANGED_FLOAT);
		checkUpdated4(lastCompleteIndex);
	}

	/**
	 * Perform all the operations available on a ByteBufferViewVarHandle viewed as int elements.
	 */
	@Test
	public void testInt() {
		ByteBufferViewHelper.reset();
		/* Get */
		int iFromVH = (int)vhInt.get(_buffer, 0);
		assertEquals(FIRST_INT, iFromVH);

		/* Set */
		vhInt.set(_buffer, 0, CHANGED_INT);
		checkUpdated4(0);

		ByteBufferViewHelper.reset();
		/* GetOpaque */
		try {
			iFromVH = (int) vhInt.getOpaque(_buffer, 0);
			assertEquals(FIRST_INT, iFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetOpaque */
		try {
			vhInt.setOpaque(_buffer, 0, CHANGED_INT);
			checkUpdated4(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetVolatile */
		try {
			iFromVH = (int) vhInt.getVolatile(_buffer, 0);
			assertEquals(FIRST_INT, iFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetVolatile */
		try {
			vhInt.setVolatile(_buffer, 0, CHANGED_INT);
			checkUpdated4(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetAcquire */
		try {
			iFromVH = (int) vhInt.getAcquire(_buffer, 0);
			assertEquals(FIRST_INT, iFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetRelease */
		try {
			vhInt.setRelease(_buffer, 0, CHANGED_INT);
			checkUpdated4(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndSet - Fail */
		ByteBufferViewHelper.reset();
		boolean casResult = false;
		try {
			casResult = (boolean)vhInt.compareAndSet(_buffer, 0, 2, 3);
			checkNotUpdated4(0);
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndSet - Succeed */
		ByteBufferViewHelper.reset();
		try {
			casResult = (boolean)vhInt.compareAndSet(_buffer, 0, FIRST_INT, CHANGED_INT);
			checkUpdated4(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchange - Fail */
		ByteBufferViewHelper.reset();
		int caeResult = 0;
		try {
			caeResult = (int)vhInt.compareAndExchange(_buffer, 0, 2, 3);
			checkNotUpdated4(0);
			assertEquals(FIRST_INT, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchange - Succeed */
		try {
			caeResult = (int)vhInt.compareAndExchange(_buffer, 0, FIRST_INT, CHANGED_INT);
			checkUpdated4(0);
			assertEquals(FIRST_INT, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndExchangeAcquire - Fail */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (int)vhInt.compareAndExchangeAcquire(_buffer, 0, 2, 3);
			checkNotUpdated4(0);
			assertEquals(FIRST_INT, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndExchangeAcquire - Succeed */
		try {
			caeResult = (int)vhInt.compareAndExchangeAcquire(_buffer, 0, FIRST_INT, CHANGED_INT);
			checkUpdated4(0);
			assertEquals(FIRST_INT, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndExchangeRelease - Fail */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (int)vhInt.compareAndExchangeRelease(_buffer, 0, 2, 3);
			checkNotUpdated4(0);
			assertEquals(FIRST_INT, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndExchangeRelease - Succeed */
		try {
			caeResult = (int)vhInt.compareAndExchangeRelease(_buffer, 0, FIRST_INT, CHANGED_INT);
			checkUpdated4(0);
			assertEquals(FIRST_INT, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSet - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = (boolean)vhInt.weakCompareAndSet(_buffer, 0, 2, 3);
			checkNotUpdated4(0);
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSet - Succeed */
		try {
			casResult = (boolean)vhInt.weakCompareAndSet(_buffer, 0, FIRST_INT, CHANGED_INT);
			checkUpdated4(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetAcquire - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = (boolean)vhInt.weakCompareAndSetAcquire(_buffer, 0, 2, 3);
			checkNotUpdated4(0);
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetAcquire - Succeed */
		try {
			casResult = (boolean)vhInt.weakCompareAndSetAcquire(_buffer, 0, FIRST_INT, CHANGED_INT);
			checkUpdated4(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetRelease - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = (boolean)vhInt.weakCompareAndSetRelease(_buffer, 0, 2, 3);
			checkNotUpdated4(0);
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetRelease - Succeed */
		try {
			casResult = (boolean)vhInt.weakCompareAndSetRelease(_buffer, 0, FIRST_INT, CHANGED_INT);
			checkUpdated4(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSet */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndSet(_buffer, 0, CHANGED_INT);
			assertEquals(FIRST_INT, iFromVH);
			checkUpdated4(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSetAcquire */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndSetAcquire(_buffer, 0, CHANGED_INT);
			assertEquals(FIRST_INT, iFromVH);
			checkUpdated4(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSetRelease */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndSetRelease(_buffer, 0, CHANGED_INT);
			assertEquals(FIRST_INT, iFromVH);
			checkUpdated4(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndAdd */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndAdd(_buffer, 0, 2);
			assertEquals(FIRST_INT, iFromVH);
			checkIntAddition((byte)2, 0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndAddAcquire */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndAddAcquire(_buffer, 0, 2);
			assertEquals(FIRST_INT, iFromVH);
			checkIntAddition((byte)2, 0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndAddRelease */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndAddRelease(_buffer, 0, 2);
			assertEquals(FIRST_INT, iFromVH);
			checkIntAddition((byte)2, 0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseAnd*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseAnd(_buffer, 0, 7);
			assertEquals(FIRST_INT, iFromVH);
			checkIntBitwiseAnd(7, 0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseAndAcquire*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseAndAcquire(_buffer, 0, 7);
			assertEquals(FIRST_INT, iFromVH);
			checkIntBitwiseAnd(7, 0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseAndRelease*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseAndRelease(_buffer, 0, 7);
			assertEquals(FIRST_INT, iFromVH);
			checkIntBitwiseAnd(7, 0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseOr*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseOr(_buffer, 0, 1);
			assertEquals(FIRST_INT, iFromVH);
			checkIntBitwiseOr(1, 0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseOrAcquire*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseOrAcquire(_buffer, 0, 1);
			assertEquals(FIRST_INT, iFromVH);
			checkIntBitwiseOr(1, 0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseOrRelease*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseOrRelease(_buffer, 0, 1);
			assertEquals(FIRST_INT, iFromVH);
			checkIntBitwiseOr(1, 0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseXor*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseXor(_buffer, 0, 6);
			assertEquals(FIRST_INT, iFromVH);
			checkIntBitwiseXor(6, 0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseXorAcquire*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseXorAcquire(_buffer, 0, 6);
			assertEquals(FIRST_INT, iFromVH);
			checkIntBitwiseXor(6, 0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseXorRelease*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseXorRelease(_buffer, 0, 6);
			assertEquals(FIRST_INT, iFromVH);
			checkIntBitwiseXor(6, 0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}
	}

	/**
	 * Get and set the last element of a ByteBufferViewVarHandle viewed as int elements.
	 */
	@Test
	public void testIntLastElement() {
		int lastCompleteIndex = lastCompleteIndex(Integer.BYTES);
		ByteBufferViewHelper.reset();
		/* Get */
		int fFromVH = (int)vhInt.get(_buffer, lastCompleteIndex);
		assertEquals(LAST_INT, fFromVH);

		/* Set */
		vhInt.set(_buffer, lastCompleteIndex, CHANGED_INT);
		checkUpdated4(lastCompleteIndex);
	}

	/**
	 * Tests all VarHandle access modes on a non-zero index in a ByteBuffer, viewed as int.
	 */
	@Test
	public void testIntNonZeroIndex() {
		ByteBufferViewHelper.reset();
		/* Get */
		int iFromVH = (int)vhInt.get(_buffer, 4);
		assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);

		/* Set */
		vhInt.set(_buffer, 4, CHANGED_INT);
		checkUpdated4(4);

		ByteBufferViewHelper.reset();
		/* GetOpaque */
		try {
			iFromVH = (int) vhInt.getOpaque(_buffer, 4);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetOpaque */
		try {
			vhInt.setOpaque(_buffer, 4, CHANGED_INT);
			checkUpdated4(4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetVolatile */
		try {
			iFromVH = (int) vhInt.getVolatile(_buffer, 4);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetVolatile */
		try {
			vhInt.setVolatile(_buffer, 4, CHANGED_INT);
			checkUpdated4(4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetAcquire */
		try {
			iFromVH = (int) vhInt.getAcquire(_buffer, 4);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetRelease */
		try {
			vhInt.setRelease(_buffer, 4, CHANGED_INT);
			checkUpdated4(4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndSet - Fail */
		ByteBufferViewHelper.reset();
		boolean casResult = false;
		try {
			casResult = (boolean)vhInt.compareAndSet(_buffer, 4, 2, 3);
			checkNotUpdated4(4);
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndSet - Succeed */
		ByteBufferViewHelper.reset();
		try {
			casResult = (boolean)vhInt.compareAndSet(_buffer, 4, INITIAL_INT_AT_INDEX_4, CHANGED_INT);
			checkUpdated4(4);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchange - Fail */
		ByteBufferViewHelper.reset();
		int caeResult = 0;
		try {
			caeResult = (int)vhInt.compareAndExchange(_buffer, 4, 2, 3);
			checkNotUpdated4(4);
			assertEquals(INITIAL_INT_AT_INDEX_4, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchange - Succeed */
		try {
			caeResult = (int)vhInt.compareAndExchange(_buffer, 4, INITIAL_INT_AT_INDEX_4, CHANGED_INT);
			checkUpdated4(4);
			assertEquals(INITIAL_INT_AT_INDEX_4, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndExchangeAcquire - Fail */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (int)vhInt.compareAndExchangeAcquire(_buffer, 4, 2, 3);
			checkNotUpdated4(4);
			assertEquals(INITIAL_INT_AT_INDEX_4, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndExchangeAcquire - Succeed */
		try {
			caeResult = (int)vhInt.compareAndExchangeAcquire(_buffer, 4, INITIAL_INT_AT_INDEX_4, CHANGED_INT);
			checkUpdated4(4);
			assertEquals(INITIAL_INT_AT_INDEX_4, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndExchangeRelease - Fail */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (int)vhInt.compareAndExchangeRelease(_buffer, 4, 2, 3);
			checkNotUpdated4(4);
			assertEquals(INITIAL_INT_AT_INDEX_4, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndExchangeRelease - Succeed */
		try {
			caeResult = (int)vhInt.compareAndExchangeRelease(_buffer, 4, INITIAL_INT_AT_INDEX_4, CHANGED_INT);
			checkUpdated4(4);
			assertEquals(INITIAL_INT_AT_INDEX_4, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSet - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = (boolean)vhInt.weakCompareAndSet(_buffer, 4, 2, 3);
			checkNotUpdated4(4);
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSet - Succeed */
		try {
			casResult = (boolean)vhInt.weakCompareAndSet(_buffer, 4, INITIAL_INT_AT_INDEX_4, CHANGED_INT);
			checkUpdated4(4);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetAcquire - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = (boolean)vhInt.weakCompareAndSetAcquire(_buffer, 4, 2, 3);
			checkNotUpdated4(4);
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetAcquire - Succeed */
		try {
			casResult = (boolean)vhInt.weakCompareAndSetAcquire(_buffer, 4, INITIAL_INT_AT_INDEX_4, CHANGED_INT);
			checkUpdated4(4);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetRelease - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = (boolean)vhInt.weakCompareAndSetRelease(_buffer, 4, 2, 3);
			checkNotUpdated4(4);
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetRelease - Succeed */
		try {
			casResult = (boolean)vhInt.weakCompareAndSetRelease(_buffer, 4, INITIAL_INT_AT_INDEX_4, CHANGED_INT);
			checkUpdated4(4);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSet */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndSet(_buffer, 4, CHANGED_INT);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkUpdated4(4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSetAcquire */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndSetAcquire(_buffer, 4, CHANGED_INT);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkUpdated4(4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSetRelease */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndSetRelease(_buffer, 4, CHANGED_INT);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkUpdated4(4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndAdd */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndAdd(_buffer, 4, 2);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkIntAddition((byte)2, 4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndAddAcquire */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndAddAcquire(_buffer, 4, 2);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkIntAddition((byte)2, 4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndAddRelease */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndAddRelease(_buffer, 4, 2);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkIntAddition((byte)2, 4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseAnd*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseAnd(_buffer, 4, 7);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkIntBitwiseAnd(7, 4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseAndAcquire*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseAndAcquire(_buffer, 4, 7);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkIntBitwiseAnd(7, 4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseAndRelease*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseAndRelease(_buffer, 4, 7);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkIntBitwiseAnd(7, 4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseOr*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseOr(_buffer, 4, 1);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkIntBitwiseOr(1, 4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseOrAcquire*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseOrAcquire(_buffer, 4, 1);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkIntBitwiseOr(1, 4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseOrRelease*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseOrRelease(_buffer, 4, 1);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkIntBitwiseOr(1, 4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseXor*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseXor(_buffer, 4, 6);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkIntBitwiseXor(6, 4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseXorAcquire*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseXorAcquire(_buffer, 4, 6);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkIntBitwiseXor(6, 4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseXorRelease*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (int)vhInt.getAndBitwiseXorRelease(_buffer, 4, 6);
			assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);
			checkIntBitwiseXor(6, 4);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}
	}

	/**
	 * Perform all the operations available on a ByteBufferViewVarHandle viewed as long elements.
	 */
	@Test
	public void testLong() {
		ByteBufferViewHelper.reset();
		/* Get */
		long iFromVH = (long)vhLong.get(_buffer, 0);
		assertEquals(FIRST_LONG, iFromVH);

		/* Set */
		vhLong.set(_buffer, 0, CHANGED_LONG);
		checkUpdated8(0);

		ByteBufferViewHelper.reset();
		/* GetOpaque */
		try {
			iFromVH = (long) vhLong.getOpaque(_buffer, 0);
			assertEquals(FIRST_LONG, iFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetOpaque */
		try {
			vhLong.setOpaque(_buffer, 0, CHANGED_LONG);
			checkUpdated8(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetVolatile */
		try {
			iFromVH = (long) vhLong.getVolatile(_buffer, 0);
			assertEquals(FIRST_LONG, iFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetVolatile */
		try {
			vhLong.setVolatile(_buffer, 0, CHANGED_LONG);
			checkUpdated8(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetAcquire */
		try {
			iFromVH = (long) vhLong.getAcquire(_buffer, 0);
			assertEquals(FIRST_LONG, iFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetRelease */
		try {
			vhLong.setRelease(_buffer, 0, CHANGED_LONG);
			checkUpdated8(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndSet - Fail */
		ByteBufferViewHelper.reset();
		boolean casResult = false;
		try {
			casResult = (boolean)vhLong.compareAndSet(_buffer, 0, 2L, 3L);
			checkNotUpdated8();
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndSet - Succeed */
		ByteBufferViewHelper.reset();
		try {
			casResult = (boolean)vhLong.compareAndSet(_buffer, 0, FIRST_LONG, CHANGED_LONG);
			checkUpdated8(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchange - Fail */
		ByteBufferViewHelper.reset();
		long caeResult = 0;
		try {
			caeResult = (long)vhLong.compareAndExchange(_buffer, 0, 2L, 3L);
			checkNotUpdated8();
			assertEquals(FIRST_LONG, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* compareAndExchange - Succeed */
		try {
			caeResult = (long)vhLong.compareAndExchange(_buffer, 0, FIRST_LONG, CHANGED_LONG);
			checkUpdated8(0);
			assertEquals(FIRST_LONG, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndExchangeAcquire - Fail */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (long)vhLong.compareAndExchangeAcquire(_buffer, 0, 2L, 3L);
			checkNotUpdated8();
			assertEquals(FIRST_LONG, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndExchangeAcquire - Succeed */
		try {
			caeResult = (long)vhLong.compareAndExchangeAcquire(_buffer, 0, FIRST_LONG, CHANGED_LONG);
			checkUpdated8(0);
			assertEquals(FIRST_LONG, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndExchangeRelease - Fail */
		ByteBufferViewHelper.reset();
		try {
			caeResult = (long)vhLong.compareAndExchangeRelease(_buffer, 0, 2L, 3L);
			checkNotUpdated8();
			assertEquals(FIRST_LONG, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* CompareAndExchangeRelease - Succeed */
		try {
			caeResult = (long)vhLong.compareAndExchangeRelease(_buffer, 0, FIRST_LONG, CHANGED_LONG);
			checkUpdated8(0);
			assertEquals(FIRST_LONG, caeResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSet - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = (boolean)vhLong.weakCompareAndSet(_buffer, 0, 2L, 3L);
			checkNotUpdated8();
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSet - Succeed */
		try {
			casResult = (boolean)vhLong.weakCompareAndSet(_buffer, 0, FIRST_LONG, CHANGED_LONG);
			checkUpdated8(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetAcquire - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = (boolean)vhLong.weakCompareAndSetAcquire(_buffer, 0, 2L, 3L);
			checkNotUpdated8();
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetAcquire - Succeed */
		try {
			casResult = (boolean)vhLong.weakCompareAndSetAcquire(_buffer, 0, FIRST_LONG, CHANGED_LONG);
			checkUpdated8(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetRelease - Fail */
		ByteBufferViewHelper.reset();
		try {
			casResult = (boolean)vhLong.weakCompareAndSetRelease(_buffer, 0, 2L, 3L);
			checkNotUpdated8();
			Assert.assertFalse(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* WeakCompareAndSetRelease - Succeed */
		try {
			casResult = (boolean)vhLong.weakCompareAndSetRelease(_buffer, 0, FIRST_LONG, CHANGED_LONG);
			checkUpdated8(0);
			Assert.assertTrue(casResult);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSet */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndSet(_buffer, 0, CHANGED_LONG);
			assertEquals(FIRST_LONG, iFromVH);
			checkUpdated8(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSetAcquire */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndSetAcquire(_buffer, 0, CHANGED_LONG);
			assertEquals(FIRST_LONG, iFromVH);
			checkUpdated8(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndSetRelease */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndSetRelease(_buffer, 0, CHANGED_LONG);
			assertEquals(FIRST_LONG, iFromVH);
			checkUpdated8(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndAdd */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndAdd(_buffer, 0, 2);
			assertEquals(FIRST_LONG, iFromVH);
			checkLongAddition();
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndAddAcquire */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndAddAcquire(_buffer, 0, 2);
			assertEquals(FIRST_LONG, iFromVH);
			checkLongAddition();
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndAddRelease */
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndAddRelease(_buffer, 0, 2);
			assertEquals(FIRST_LONG, iFromVH);
			checkLongAddition();
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseAnd*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndBitwiseAnd(_buffer, 0, 15);
			assertEquals(FIRST_LONG, iFromVH);
			checkLongBitwiseAnd();
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseAndAcquire*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndBitwiseAndAcquire(_buffer, 0, 15);
			assertEquals(FIRST_LONG, iFromVH);
			checkLongBitwiseAnd();
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseAndRelease*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndBitwiseAndRelease(_buffer, 0, 15);
			assertEquals(FIRST_LONG, iFromVH);
			checkLongBitwiseAnd();
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseOr*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndBitwiseOr(_buffer, 0, 2);
			assertEquals(FIRST_LONG, iFromVH);
			checkLongBitwiseOr();
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseOrAcquire*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndBitwiseOrAcquire(_buffer, 0, 2);
			assertEquals(FIRST_LONG, iFromVH);
			checkLongBitwiseOr();
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseOrRelease*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndBitwiseOrRelease(_buffer, 0, 2);
			assertEquals(FIRST_LONG, iFromVH);
			checkLongBitwiseOr();
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseXor*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndBitwiseXor(_buffer, 0, 10);
			assertEquals(FIRST_LONG, iFromVH);
			checkLongBitwiseXor();
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseXorAcquire*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndBitwiseXorAcquire(_buffer, 0, 10);
			assertEquals(FIRST_LONG, iFromVH);
			checkLongBitwiseXor();
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* GetAndBitwiseXorRelease*/
		ByteBufferViewHelper.reset();
		try {
			iFromVH = (long)vhLong.getAndBitwiseXorRelease(_buffer, 0, 10);
			assertEquals(FIRST_LONG, iFromVH);
			checkLongBitwiseXor();
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}
	}

	/**
	 * Get and set the last element of a ByteBufferViewVarHandle viewed as long elements.
	 */
	@Test
	public void testLongLastElement() {
		int lastCompleteIndex = lastCompleteIndex(Long.BYTES);
		ByteBufferViewHelper.reset();
		/* Get */
		long fFromVH = (long)vhLong.get(_buffer, lastCompleteIndex);
		assertEquals(LAST_LONG, fFromVH);

		/* Set */
		vhLong.set(_buffer, lastCompleteIndex, CHANGED_LONG);
		checkUpdated8(lastCompleteIndex);
	}

	/**
	 * Perform all the operations available on a ByteBufferViewVarHandle viewed as short elements.
	 */
	@Test
	public void testShort() {

		ByteBufferViewHelper.reset();
		/* Get */
		short sFromVH = (short)vhShort.get(_buffer, 0);
		Assert.assertEquals(FIRST_SHORT, sFromVH);

		/* Set */
		vhShort.set(_buffer, 0, CHANGED_SHORT);
		checkUpdated2(0);

		ByteBufferViewHelper.reset();
		/* GetOpaque */
		try {
			sFromVH = (short) vhShort.getOpaque(_buffer, 0);
			Assert.assertEquals(FIRST_SHORT, sFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetOpaque */
		try {
			vhShort.setOpaque(_buffer, 0, CHANGED_SHORT);
			checkUpdated2(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetVolatile */
		try {
			sFromVH = (short) vhShort.getVolatile(_buffer, 0);
			Assert.assertEquals(FIRST_SHORT, sFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetVolatile */
		try {
			vhShort.setVolatile(_buffer, 0, CHANGED_SHORT);
			checkUpdated2(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		ByteBufferViewHelper.reset();
		/* GetAcquire */
		try {
			sFromVH = (short) vhShort.getAcquire(_buffer, 0);
			Assert.assertEquals(FIRST_SHORT, sFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		/* SetRelease */
		try {
			vhShort.setRelease(_buffer, 0, CHANGED_SHORT);
			checkUpdated2(0);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		@SuppressWarnings("unused")
		boolean casResult;
		@SuppressWarnings("unused")
		short caeResult;

		/* CompareAndSet */
		try {
			casResult = vhShort.compareAndSet(_buffer, 0, (short)1, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* compareAndExchange */
		try {
			caeResult = (short)vhShort.compareAndExchange(_buffer, 0, (short)1, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* compareAndExchangeAcquire */
		try {
			caeResult = (short)vhShort.compareAndExchangeAcquire(_buffer, 0, (short)1, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* compareAndExchangeRelease */
		try {
			caeResult = (short)vhShort.compareAndExchangeRelease(_buffer, 0, (short)1, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* WeakCompareAndSet */
		try {
			casResult = vhShort.weakCompareAndSet(_buffer, 0, (short)1, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* WeakCompareAndSetAcquire */
		try {
			casResult = vhShort.weakCompareAndSetAcquire(_buffer, 0, (short)1, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* WeakCompareAndSetRelease */
		try {
			casResult = vhShort.weakCompareAndSetRelease(_buffer, 0, (short)1, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* WeakCompareAndSetPlain */
		try {
			casResult = vhShort.weakCompareAndSetPlain(_buffer, 0, (short)1, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndSet */
		try {
			sFromVH = (short)vhShort.getAndSet(_buffer, 0, CHANGED_SHORT);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndSetAcquire */
		try {
			sFromVH = (short)vhShort.getAndSetAcquire(_buffer, 0, CHANGED_SHORT);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndSetRelease */
		try {
			sFromVH = (short)vhShort.getAndSetRelease(_buffer, 0, CHANGED_SHORT);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndAdd */
		try {
			sFromVH = (short)vhShort.getAndAdd(_buffer, 0, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndAddAcquire */
		try {
			sFromVH = (short)vhShort.getAndAddAcquire(_buffer, 0, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndAddRelease */
		try {
			sFromVH = (short)vhShort.getAndAddRelease(_buffer, 0, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseAnd */
		try {
			sFromVH = (short)vhShort.getAndBitwiseAnd(_buffer, 0, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseAndAcquire */
		try {
			sFromVH = (short)vhShort.getAndBitwiseAndAcquire(_buffer, 0, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseAndRelease */
		try {
			sFromVH = (short)vhShort.getAndBitwiseAndRelease(_buffer, 0, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseOr */
		try {
			sFromVH = (short)vhShort.getAndBitwiseOr(_buffer, 0, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseOrAcquire */
		try {
			sFromVH = (short)vhShort.getAndBitwiseOrAcquire(_buffer, 0, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseOrRelease */
		try {
			sFromVH = (short)vhShort.getAndBitwiseOrRelease(_buffer, 0, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseXor */
		try {
			sFromVH = (short)vhShort.getAndBitwiseXor(_buffer, 0, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseXorAcquire */
		try {
			sFromVH = (short)vhShort.getAndBitwiseXorAcquire(_buffer, 0, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}

		/* GetAndBitwiseXorRelease */
		try {
			sFromVH = (short)vhShort.getAndBitwiseXorRelease(_buffer, 0, (short)2);
			Assert.fail("Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {}
	}

	/**
	 * Get and set the last element of a ByteBufferViewVarHandle viewed as short elements.
	 */
	@Test
	public void testShortLastElement() {
		int lastCompleteIndex = lastCompleteIndex(Short.BYTES);
		ByteBufferViewHelper.reset();

		/* Get */
		short fFromVH = (short)vhShort.get(_buffer, lastCompleteIndex);
		Assert.assertEquals(LAST_SHORT, fFromVH);

		/* Set */
		vhShort.set(_buffer, lastCompleteIndex, CHANGED_SHORT);
		checkUpdated2(lastCompleteIndex);
	}

	/**
	 * Attempts to get a ByteBufferViewVarHandle with byte[] as the view type.
	 */
	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void testByte() {
		MethodHandles.byteBufferViewVarHandle(byte[].class, _byteOrder);
	}

	/**
	 * Attempts to get a ByteBufferViewVarHandle with Object[] as the view type.
	 */
	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void testReference() {
		MethodHandles.byteBufferViewVarHandle(Object[].class, _byteOrder);
	}

	/**
	 * Attempts to get a ByteBufferViewVarHandle with boolean[] as the view type.
	 */
	@Test(expectedExceptions = UnsupportedOperationException.class)
	public void testBoolean() {
		MethodHandles.byteBufferViewVarHandle(boolean[].class, _byteOrder);
	}

	/**
	 * Attempt to get an element outside the bounds of the {@link ByteBuffer}.
	 */
	@Test(expectedExceptions = IndexOutOfBoundsException.class)
	public void testInvalidIndex() {
		vhLong.get(_buffer, _buffer.capacity());
	}

	/**
	 * Attempt to get an element that crosses the end of the {@link ByteBuffer}.
	 */
	@Test(expectedExceptions = IndexOutOfBoundsException.class)
	public void testIndexOverflow() {
		vhLong.get(_buffer, _buffer.capacity() - 1);
	}

	/**
	 * Cast the {@link ByteBuffer} receiver object to {@link Object}.
	 */
	@Test
	public void testObjectReceiver() {
		vhInt.set((Object)_buffer, 0, CHANGED_INT);
		checkUpdated4(0);
	}

	/**
	 * Pass an invalid type, cast to {@link Object}, as the {@link ByteBuffer} receiver object.
	 */
	@Test(expectedExceptions = ClassCastException.class)
	public void testInvalidObjectReceiver() {
		vhInt.set((Object)vhInt, 0, CHANGED_INT);
		checkUpdated4(0);
	}

	/**
	 *  Operate on a sliced {@link ByteBuffer}. Expect the elements to be shifted by the slice position.
	 */
	@Test
	public void testSlicedByteBuffer() {
		ByteBufferViewHelper.reset();

		// Create a sliced ByteBuffer, then reset the original _buffer
		_buffer.mark();
		_buffer.position(4);
		ByteBuffer bufferSlice = _buffer.slice();
		_buffer.reset();

		/* Get */
		int iFromVH = (int)vhInt.get(bufferSlice, 0);
		assertEquals(INITIAL_INT_AT_INDEX_4, iFromVH);

		/* Set */
		vhInt.set(bufferSlice, 0, CHANGED_INT);
		Assert.assertEquals((byte)10, _buffer.get(4));
		Assert.assertEquals((byte)20, _buffer.get(5));
		Assert.assertEquals((byte)30, _buffer.get(6));
		Assert.assertEquals((byte)40, _buffer.get(7));
	}

	/**
	 * Operate on a ByteBuffer which wraps an array with an offset. Expect the elements to <b>not</b> be shifted.
	 */
	@Test
	public void testByteBufferWithArrayOffset() {
		byte[] array = new byte[] {1, 2, 3, 4, 5, 6, 7, 8};
		ByteBuffer buffer = ByteBuffer.wrap(array, 4, 4);

		/* Get */
		int iFromVH = (int)vhInt.get(buffer, 0);
		assertEquals(FIRST_INT, iFromVH);

		/* Set */
		vhInt.set(buffer, 0, CHANGED_INT);
		Assert.assertEquals((byte)10, buffer.get(0));
		Assert.assertEquals((byte)20, buffer.get(1));
		Assert.assertEquals((byte)30, buffer.get(2));
		Assert.assertEquals((byte)40, buffer.get(3));
	}

	/**
	 * Perform all the operations available on a ByteBufferViewVarHandle viewed as int elements.
	 * Use a <b>read-only</b> {@link ByteBuffer}.
	 */
	@Test
	public void testReadOnlyByteBuffer() {
		ByteBufferViewHelper.reset();

		ByteBuffer buffer = _buffer.asReadOnlyBuffer();

		@SuppressWarnings("unused")
		boolean casResult;
		@SuppressWarnings("unused")
		int caeResult;

		int iFromVH = (int)vhInt.get(buffer, 0);
		assertEquals(FIRST_INT, iFromVH);

		try {
			vhInt.set(buffer, 0, CHANGED_INT);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) { }

		try {
			iFromVH = (int)vhInt.getVolatile(buffer, 0);
			assertEquals(FIRST_INT, iFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			vhInt.setVolatile(buffer, 0, CHANGED_INT);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getOpaque(buffer, 0);
			assertEquals(FIRST_INT, iFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			vhInt.setOpaque(buffer, 0, CHANGED_INT);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAcquire(buffer, 0);
			assertEquals(FIRST_INT, iFromVH);
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			vhInt.setRelease(buffer, 0, CHANGED_INT);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			casResult = (boolean)vhInt.compareAndSet(buffer, 0, 2, 3);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			caeResult = (int)vhInt.compareAndExchange(buffer, 0, 2, 3);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			caeResult = (int)vhInt.compareAndExchangeAcquire(buffer, 0, 2, 3);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			caeResult = (int)vhInt.compareAndExchangeRelease(buffer, 0, 2, 3);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			casResult = (boolean)vhInt.weakCompareAndSet(buffer, 0, 2, 3);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			casResult = (boolean)vhInt.weakCompareAndSetAcquire(buffer, 0, 2, 3);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			casResult = (boolean)vhInt.weakCompareAndSetRelease(buffer, 0, 2, 3);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndSet(buffer, 0, CHANGED_INT);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndSetAcquire(buffer, 0, CHANGED_INT);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndSetRelease(buffer, 0, CHANGED_INT);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndAdd(buffer, 0, 2);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndAddAcquire(buffer, 0, 2);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndAddRelease(buffer, 0, 2);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndBitwiseAnd(buffer, 0, 7);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndBitwiseAndAcquire(buffer, 0, 7);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndBitwiseAndRelease(buffer, 0, 7);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndBitwiseOr(buffer, 0, 1);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndBitwiseOrAcquire(buffer, 0, 1);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndBitwiseOrRelease(buffer, 0, 1);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndBitwiseXor(buffer, 0, 6);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndBitwiseXorAcquire(buffer, 0, 6);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}

		try {
			iFromVH = (int)vhInt.getAndBitwiseXorRelease(buffer, 0, 6);
			failReadOnlyAccess();
		} catch (ReadOnlyBufferException e) {
			Assert.assertTrue(versionMajor < 23);
		} catch (IllegalStateException iae) {
			Assert.assertTrue(versionMajor >= 23);
		}
	}

	/**
	 * Perform all the operations with unaligned access crossing an 8-boundary.
	 */
	@Test
	public void testUnalignedAccessCrossBoundary() {
		ByteBufferViewHelper.reset();

		_buffer.mark();
		_buffer.position(3);
		ByteBuffer bufferSlice = _buffer.slice();
		_buffer.reset();

		@SuppressWarnings("unused")
		boolean casResult;
		@SuppressWarnings("unused")
		int caeResult;

		/* Get */
		int iFromVH = (int)vhInt.get(bufferSlice, 0);
		assertEquals(INITIAL_INT_AT_INDEX_3, iFromVH);

		/* Set */
		vhInt.set(bufferSlice, 0, CHANGED_INT);
		checkUpdated4(3);

		try {
			iFromVH = (int) vhInt.getOpaque(bufferSlice, 0);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			vhInt.setOpaque(bufferSlice, 0, CHANGED_INT);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int) vhInt.getVolatile(bufferSlice, 0);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			vhInt.setVolatile(bufferSlice, 0, CHANGED_INT);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int) vhInt.getAcquire(bufferSlice, 0);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			vhInt.setRelease(bufferSlice, 0, CHANGED_INT);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			casResult = (boolean)vhInt.compareAndSet(bufferSlice, 0, 2, 3);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			caeResult = (int)vhInt.compareAndExchange(bufferSlice, 0, 2, 3);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			caeResult = (int)vhInt.compareAndExchangeAcquire(bufferSlice, 0, 2, 3);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			caeResult = (int)vhInt.compareAndExchangeRelease(bufferSlice, 0, 2, 3);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			casResult = (boolean)vhInt.weakCompareAndSet(bufferSlice, 0, 2, 3);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			casResult = (boolean)vhInt.weakCompareAndSetAcquire(bufferSlice, 0, 2, 3);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			casResult = (boolean)vhInt.weakCompareAndSetRelease(bufferSlice, 0, 2, 3);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndSet(bufferSlice, 0, CHANGED_INT);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndSetAcquire(bufferSlice, 0, CHANGED_INT);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndSetRelease(bufferSlice, 0, CHANGED_INT);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndAdd(bufferSlice, 0, 2);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndAddAcquire(bufferSlice, 0, 2);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndAddRelease(bufferSlice, 0, 2);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndBitwiseAnd(bufferSlice, 0, 7);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndBitwiseAndAcquire(bufferSlice, 0, 7);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndBitwiseAndRelease(bufferSlice, 0, 7);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndBitwiseOr(bufferSlice, 0, 1);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndBitwiseOrAcquire(bufferSlice, 0, 1);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndBitwiseOrRelease(bufferSlice, 0, 1);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndBitwiseXor(bufferSlice, 0, 6);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndBitwiseXorAcquire(bufferSlice, 0, 6);
			failUnalignedAccess();
		} catch (IllegalStateException e) { }

		try {
			iFromVH = (int)vhInt.getAndBitwiseXorRelease(bufferSlice, 0, 6);
			failUnalignedAccess();
		} catch (IllegalStateException e) {	}
	}

	/**
	 * Get and GetVolatile with unaligned access <b>not</b> crossing an 8-boundary.
	 */
	public void testUnalignedAccessWithinBoundary() {
		ByteBufferViewHelper.reset();

		/* Get */
		short sFromVH = (short)vhShort.get(_buffer, 1);
		assertEquals(INITIAL_SHORT_AT_INDEX_1, sFromVH);

		/* GetVolatile */
		try {
			sFromVH = (short)vhShort.getVolatile(_buffer, 1);
			failUnalignedAccess();
		} catch (IllegalStateException e) {	}
	}
}
