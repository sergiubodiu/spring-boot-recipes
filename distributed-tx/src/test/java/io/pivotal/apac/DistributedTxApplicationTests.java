package io.pivotal.apac;

import org.hamcrest.Matcher;
import org.hamcrest.core.SubstringMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.boot.test.OutputCapture;

import static org.junit.Assert.assertThat;


public class DistributedTxApplicationTests {

	@Rule
	public OutputCapture outputCapture = new OutputCapture();

	@Test
	public void testTransactionRollback() throws Exception {
		DistributedTxApplication.main(new String[] {});
		String output = this.outputCapture.toString();
		assertThat(output, containsString(1, "---->"));
		assertThat(output, containsString(1, "----> user-preferences"));
		assertThat(output, containsString(2, "Count is 1"));
		assertThat(output, containsString(1, "Simulated error"));
	}

	private Matcher<? super String> containsString(final int times, String s) {
		return new SubstringMatcher(s) {

			@Override
			protected String relationship() {
				return "containing " + times + " times";
			}

			@Override
			protected boolean evalSubstringOf(String s) {
				int i = 0;
				while (s.contains(this.substring)) {
					s = s.substring(s.indexOf(this.substring) + this.substring.length());
					i++;
				}
				return i == times;
			}

		};
	}


}
