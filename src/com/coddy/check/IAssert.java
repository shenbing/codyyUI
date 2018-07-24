package com.coddy.check;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Map;
import java.util.Map.Entry;
import org.assertj.core.internal.Failures;

import com.coddy.utils.ScreenCapture;

public class IAssert {

	/**
	 * 
	 * @param actual
	 */
	public static void assertIsNull(Object actual) {
		try {
			assertThat(actual).isNull();
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 */
	public static void assertIsTrue(Boolean actual) {
		try {
			assertThat(actual).isTrue();
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 */
	public static void assertIsFalse(Boolean actual) {
		try {
			assertThat(actual).isFalse();
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 */
	public static void assertContainsSubsequence(String actual, String expected) {
		try {
			assertThat(actual).contains(expected);
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 */
	public static void assertEqualse(String actual, String expected) {
		try {
			assertThat(actual).isEqualTo(expected);
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 */
	public static void assertEqualse(Long actual, Long expected) {
		try {
			assertThat(actual).isEqualTo(expected);
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 */
	public static void assertEqualse(boolean actual, boolean expected) {
		try {
			assertThat(actual).isEqualTo(expected);
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 */
	public static void assertEqualse(int actual, int expected) {
		try {
			assertThat(actual).isEqualTo(expected);
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 */
	public static void assertLess(int actual, int expected) {
		try {
			assertThat(actual).isLessThan(expected);
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 */
	public static void assertLessOrEqualse(int actual, int expected) {
		try {
			assertThat(actual).isLessThanOrEqualTo(expected);
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 */
	public static void assertGreater(int actual, int expected) {
		try {
			assertThat(actual).isGreaterThan(expected);
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 */
	public static void assertGreaterOrEqualse(int actual, int expected) {
		try {
			assertThat(actual).isGreaterThanOrEqualTo(expected);
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 */
	public static void assertIgnoringEqualse(String actual, String expected) {
		try {
			assertThat(actual).isEqualToIgnoringCase(expected);
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 */
	public static void assertIgnoringWhitespaceEqualse(String actual, String expected) {
		try {
			assertThat(actual).isEqualToIgnoringWhitespace(expected);
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 */
	public static <T> void assertMapAllEntity(Map<String, T> actual, Map<String, T> expected) {
		try {
			assertThat(actual).containsAllEntriesOf(expected);
		} catch (AssertionError e) {
			ScreenCapture.screenShot(
					e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "").replaceAll(">", " ").replaceAll("\"",""));
			throw new AssertionError(e);
		}
	}

	/**
	 * 
	 * @param actual
	 * @param expected
	 */
	public static <T> void assertMapContainsEntity(Map<String, T> actual, Map<String, T> expected) {
		if (expected.isEmpty()) {
			throw Failures.instance().failure("expected is empty");
		}
		for (Entry<String, T> entry : expected.entrySet()) {
			try {
				assertThat(actual).contains(entry);
			} catch (AssertionError e) {
				ScreenCapture.screenShot(e.getMessage().replaceAll("\r\n", "").replaceAll(":", "").replaceAll("<", "")
						.replaceAll(">", " ").replaceAll("\"",""));
				throw new AssertionError(e);
			}
		}
	}

	public static void main(String[] args) {
		IAssert.assertContainsSubsequence("21", "22");
	}

}
