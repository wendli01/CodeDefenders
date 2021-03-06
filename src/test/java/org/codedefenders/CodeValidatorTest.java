package org.codedefenders;

import static org.codedefenders.validation.CodeValidator.validMutant;
import static org.codedefenders.validation.CodeValidator.validTestCode;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.codedefenders.exceptions.CodeValidatorException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * FIXME: Maybe we split this test case into validating tests vs validating mutants?
 *
 * @author Jose Rojas
 */
public class CodeValidatorTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void testInfiniteParserRecursionWithSingleTokens() throws IOException, CodeValidatorException {
        File tempClassFile = temporaryFolder.newFile();

        try (PrintWriter pw = new PrintWriter(tempClassFile)) {
            pw.println("public class XmlElementTest {");
            pw.println("	");
            pw.println("	@Test(timeout = 4000)");
            pw.println("	public void test() throws Throwable {");
            // This one is problematic
            pw.println("		XmlElement x = new XmlElement('Test');");
            pw.println("		assertNotNull(x.getData());");
            pw.println("	}");
            pw.println("}");
            //
            pw.flush();
        }
        assertTrue(validTestCode(tempClassFile.getAbsolutePath()));
    }

    @Test
    public void testInvalidSuiteWithTwoClasses() throws IOException, CodeValidatorException {

        URL url = Thread.currentThread().getContextClassLoader().getResource("TwoClasses.java");
        assertFalse("Should be invalid; file contains a two classes", validTestCode(url.getPath()));
    }

    @Test
    public void testInvalidEmptyTest() throws IOException, CodeValidatorException {

        URL url = Thread.currentThread().getContextClassLoader().getResource("EmptyTest.java");
        assertFalse(validTestCode(url.getPath()));
    }

    @Test
    public void testInvalidTwoTests() throws IOException, CodeValidatorException {

        URL url = Thread.currentThread().getContextClassLoader().getResource("TwoTests.java");
        assertFalse("Should be invalid; class contains two tests", validTestCode(url.getPath()));
    }

    @Test
    public void testInvalidTestWithTooManyAssertions() throws IOException, CodeValidatorException {

        URL url = Thread.currentThread().getContextClassLoader().getResource("TestWithTooManyAssertions.java");
        assertFalse("Should be invalid; test has too many assertions", validTestCode(url.getPath()));
    }

    @Test
    public void testInvalidTestWithIf() throws IOException, CodeValidatorException {

        URL url = Thread.currentThread().getContextClassLoader().getResource("TestWithIf.java");
        assertFalse("Should be invalid; test contains if statement", validTestCode(url.getPath()));
    }

    @Test
    public void testInvalidTestWithSystemCalls() throws IOException, CodeValidatorException {

        URL url = Thread.currentThread().getContextClassLoader().getResource("TestWithSystemCall.java");
        assertFalse("Should be invalid; test contains system call", validTestCode(url.getPath()));

        url = Thread.currentThread().getContextClassLoader().getResource("TestWithSystemCall2.java");
        assertFalse("Should be invalid; test contains system call", validTestCode(url.getPath()));

        url = Thread.currentThread().getContextClassLoader().getResource("TestWithSystemCall3.java");
        assertFalse("Should be invalid; test contains system call", validTestCode(url.getPath()));

    }

    @Test
    public void testInvalidMutantAddStmt() throws IOException {
        String originalCode = new String(
                Files.readAllBytes(new File("src/test/resources/itests/sources/Lift/Lift.java").toPath()),
                Charset.defaultCharset());
        String mutatedCode = new String(
                Files.readAllBytes(new File("src/test/resources/itests/mutants/Lift/InvalidMutantLift1.java").toPath()),
                Charset.defaultCharset());
        assertFalse(validMutant(originalCode, mutatedCode));
    }

    @Test
    public void testValidMutantInitializeString() throws IOException {
        String originalCode = new String(
                Files.readAllBytes(new File("src/test/resources/itests/sources/XmlElement/XmlElement.java").toPath()),
                Charset.defaultCharset());
        String mutatedCode = new String(
                Files.readAllBytes(new File("src/test/resources/itests/mutants/XmlElement/MutantXmlElement1.java").toPath()),
                Charset.defaultCharset());
        assertTrue(validMutant(originalCode, mutatedCode));
    }


    @Test
    public void testValidMutant() throws IOException {
        String originalCode = new String(
                Files.readAllBytes(new File("src/test/resources/itests/sources/Lift/Lift.java").toPath()),
                Charset.defaultCharset());
        String mutatedCode = new String(
                Files.readAllBytes(new File("src/test/resources/itests/mutants/Lift/MutantLift1.java").toPath()),
                Charset.defaultCharset());
        assertTrue(validMutant(originalCode, mutatedCode));
    }

    @Test
    public void testValidTest() throws IOException, CodeValidatorException {

        URL url = Thread.currentThread().getContextClassLoader().getResource("ValidTest.java");
        assertTrue("Should be valid", validTestCode(url.getPath()));
    }

    @Test
    public void testValidMutant1() {
        String orig = "int x = x + 0;";
        String mutant = "int x = x + 1;";
        assertTrue(validMutant(orig, mutant));
    }

    @Test
    public void testValidMutant2() {
        String orig = "int x = x + 1;";
        String mutant = "int x = x - 1;";

        assertTrue(validMutant(orig, mutant));
    }

    @Test
    public void testInValidMutant3() {
        String orig = "int x = 0;";
        String mutant = "int x = 0; x++;";

        assertFalse("Should be invalid as mutant has multiple statements per line", validMutant(orig, mutant));
    }

    @Test
    public void testInvalidMutant1() {
        String orig = "int x = 0;";
        String mutant = "int x = 0; if (x>0) {return false;}";

        assertFalse(validMutant(orig, mutant));
    }

    @Test
    public void testInvalidMutant2() {
        String orig = "int x = 0;";
        String mutant = "int x = 0; while (x>0) {return false;}";
        assertFalse(validMutant(orig, mutant));
    }

    @Test
    public void testInvalidMutantContainsIf() {
        String orig = "if (x >= 0) return 1; else return -x;";
        String mutant = "if (x >= 0) if (x >= 0) { return 1; } else return -x;";
        assertFalse(validMutant(orig, mutant));
    }

    @Test
    public void testInvalidMutantContainsSystemCall() {
        String orig = "if (x >= 0) return 1; else return -x;";
        String mutant = "if (x >= 0) { System.currentTimeMillis(); return x; } else return -x;";
        assertFalse(validMutant(orig, mutant));
    }

    @Test
    public void testInvalidMutantWithTernaryOperator1() {
        String orig = "x = 1;";
        String mutant = "x = x == 0 ? 1 : 0;";
        assertFalse(validMutant(orig, mutant));
    }

    @Test
    public void testInvalidMutantWithTernaryOperator2() {
        String orig = "currentFloor--;";
        String mutant = "currentFloor = currentFloor + currentFloor % 8 == 0 ? (-1) : 0;";
        assertFalse(validMutant(orig, mutant));
    }

    @Test
    public void testValidWithString() {
        String orig = "if (!isHierachic(path))";
        String mutant = "if (!isHierachic(\"test.value\"))"; // raises
        // com.github.javaparser.TokenMgrException
        assertTrue(validMutant(orig, mutant));
    }

    @org.junit.Test
    public void testMultipleStatements() throws Exception {
        assertFalse(validMutant("mul();", "mul(); add();"));
        assertFalse(validMutant("for (int i = 0; i <= 10; i ++) {", "mul(); for (int i = 0; i <= 10; i ++) {"));
    }

    @org.junit.Test
    public void testBitshifts() throws Exception {
        assertFalse(validMutant("r.num = r.num;", "r.num = r.num | ((r.num & (1 << 29)) << 1);"));
        assertFalse(validMutant("r.num = r.num;", "r.num = r.num << 1+344;"));
    }

    @org.junit.Test
    public void testSignatureChange() throws Exception {
        assertFalse(validMutant("public class Rational  {", "public final class Rational  {"));
        assertFalse(validMutant("class Rational  {", "public class Rational  {"));
        assertFalse(validMutant("class Rational  {", "final class Rational  {"));
        assertFalse(validMutant("public class Rational  {", "class Rational  {"));
        assertFalse(validMutant("public class Rational  {", "protected class Rational  {"));
        assertFalse(validMutant("final class Rational  {", "class Rational  {"));

    }

    // @org.junit.Test
    // public void testLiterals() throws Exception {
    // assertTrue(validMutant("format(\"first\", \"second\", \"third\");",
    // "format(\"\", \"sec\", \"third\");"));
    // assertTrue(validMutant("String s = \"\";", "String s = \" \";"));
    // assertTrue(validMutant("String s = \"stringval\";", "String s =
    // \"stringval \";"));
    // for (String p : CodeValidator.PROHIBITED_OPERATORS) {
    // assertTrue(p + " in a String should be valid", validMutant("String s =
    // \"\";",
    // "String s = \"" + p + "\";"));
    // }
    // assertTrue(validMutant("String s = \"\";", "String s = \";?{} <<\";"));
    // assertTrue(validMutant("String s = \"\";", "String s = \"public final
    // protected\";"));
    // assertTrue(validMutant("Char c = \'c\';", "Char c = \';\';"));
    // }

    @org.junit.Test
    public void testComments() throws Exception {
        assertFalse(validMutant("String s = \"\";", "String s = \"\";// added comment"));
        assertFalse(validMutant("String s = \" \";", "String s = \"\";// added comment"));
        assertFalse(validMutant("if(x > 0) \n\t return x;", "if(x > 1) \n\t return x; // comment"));
        assertFalse(validMutant("if(x > 0) \n\t return x; //x is positive", "if(x > 1) \n\t return x; //x is gt 1"));
        assertTrue(validMutant("String s = \"old\";// comment", "String s = \"new\";// comment"));
        assertFalse(validMutant("String s = \"\";", "String s = \"\"; /*added comment*/"));

    }

}
