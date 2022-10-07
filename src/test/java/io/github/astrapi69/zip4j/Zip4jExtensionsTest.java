/**
 * The MIT License
 *
 * Copyright (C) 2015 Asterios Raptis
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.astrapi69.zip4j;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.meanbean.test.BeanTester;

import io.github.astrapi69.file.write.WriteFileExtensions;
import io.github.astrapi69.io.file.filter.SuffixFileFilter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The unit test class for the class {@link Zip4jExtensions}.
 *
 * @version 1.0
 * @author Asterios Raptis
 */
public class Zip4jExtensionsTest extends FileTestCase
{

	/**
	 * {@inheritDoc}
	 */
	@Override
	@BeforeEach
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@AfterEach
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	/**
	 * Test for {@link Zip4jExtensions#extract(ZipFile, File, String)}
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException
	 *             is thrown if the given file is not found.
	 */
	@Test
	public void testExtract() throws FileNotFoundException, IOException
	{
		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "Zip4j.zip");

		final ZipFile zip4jZipFile = new ZipFile(zipFile);

		final String file1 = "testZip1.txt";
		final File unzippedFile1 = new File(this.unzipDir, file1);
		final File testFile1 = new File(this.testDir.getAbsoluteFile(), file1);

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");

		Zip4jExtensions.zipFiles(zip4jZipFile, testFile1);
		// Test to extract...
		Zip4jExtensions.extract(zip4jZipFile, this.unzipDir, null);

		assertTrue(unzippedFile1.exists(),
			"File" + unzippedFile1.getName() + " should be extracted.");

	}

	/**
	 * Test for {@link Zip4jExtensions#extract(ZipFile, File, String)}
	 */
	@Test
	@Disabled
	public void testExtractWithPassword() throws ZipException
	{
		final File zipFile = new File(this.testResources, "autotextWithPassword.zip");
		final File unzippedFile = new File(this.unzipDir, "autotext");
		// unzipped file should not exists in file system...
		expected = false;
		actual = unzippedFile.exists();
		assertEquals(expected, actual);
		final String password = "Hallo";

		final ZipFile zip4jZipFile = new ZipFile(zipFile);

		// Test to extract...
		Zip4jExtensions.extract(zip4jZipFile, this.unzipDir, password);
		// unzipped file should be extracted and should exists in file system...
		expected = true;
		actual = unzippedFile.exists();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for {@link Zip4jExtensions}
	 */
	@Test
	public void testWithBeanTester()
	{
		final BeanTester beanTester = new BeanTester();
		beanTester.testBean(Zip4jExtensions.class);
	}

	@Test
	public void testZipFilesWithPredicate() throws IOException
	{
		String suffix;

		suffix = ".txt";
		final File testFile1 = new File(this.testDir, "testFindFilesFileString.txt");
		final File testFile2 = new File(this.testDir, "testFindFilesFileString.tft");
		final File testFile3 = new File(this.deepDir, "testFindFilesFileString.cvs");
		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");
		WriteFileExtensions.string2File(testFile3, "Its a beautifull night!!!");
		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "Zip4j-predicate.zip");

		final ZipParameters parameters = new ZipParameters();
		final ZipFile zip4jZipFile = new ZipFile(zipFile);
		Zip4jExtensions.zipFiles(zip4jZipFile, parameters, this.testDir, (file -> {
			if (file.isDirectory())
			{
				return false;
			}
			FileFilter fileFilter = SuffixFileFilter.of(suffix, false);
			return !fileFilter.accept(file);
		}));
		System.out.println(zip4jZipFile);
	}

	/**
	 * Test for {@link Zip4jExtensions#zipFiles(ZipFile, File...)}
	 */
	@Test
	public void testZipFilesZipFileFileArray() throws IOException
	{

		final File zipFile = new File(this.zipDir.getAbsoluteFile(), "Zip4j.zip");

		final ZipFile zip4jZipFile = new ZipFile(zipFile);

		final String file1 = "testZip1.txt";
		final File unzippedFile1 = new File(this.unzipDir, file1);
		final File testFile1 = new File(this.testDir.getAbsoluteFile(), file1);

		final String file2 = "testZip2.tft";
		final File unzippedFile2 = new File(this.unzipDir, file2);
		final File testFile2 = new File(this.testDir.getAbsoluteFile(), file2);

		WriteFileExtensions.string2File(testFile1, "Its a beautifull day!!!");
		WriteFileExtensions.string2File(testFile2, "Its a beautifull evening!!!");

		Zip4jExtensions.zipFiles(zip4jZipFile, testFile1, testFile2);


		Zip4jExtensions.extract(zip4jZipFile, this.unzipDir, null);

		assertTrue(unzippedFile1.exists(),
			"File" + unzippedFile1.getName() + " should be extracted.");
		assertTrue(unzippedFile2.exists(),
			"File" + unzippedFile2.getName() + " should be extracted.");
	}

	/**
	 * Test for
	 * {@link Zip4jExtensions#zipFiles(ZipFile, CompressionMethod, CompressionLevel, File...)}
	 */
	@Test
	@Disabled
	public void testZipFilesZipFileIntIntFileArray()
	{
		// Zip4jExtensions.zipFiles(zipFile4j, compressionMethod, compressionLevel, toAdd);
	}

	/**
	 * Test for
	 * {@link Zip4jExtensions#zipFiles(ZipFile, net.lingala.zip4j.model.ZipParameters, File...)}
	 */
	@Test
	@Disabled
	public void testZipFilesZipFileZipParametersFileArray()
	{
		// Zip4jExtensions.zipFiles(zipFile4j, parameters, toAdd);
	}

}
