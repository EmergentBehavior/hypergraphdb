package hgtest.storage.bje.DefaultIndexImpl;


import org.hypergraphdb.HGException;
import org.hypergraphdb.storage.bje.DefaultIndexImpl;
import org.powermock.api.easymock.PowerMock;
import org.junit.Test;

import static hgtest.storage.bje.TestUtils.assertExceptions;
import static org.easymock.EasyMock.replay;


/**
 * @author Yuriy Sechko
 */
public class DefaultIndexImpl_addEntryTest extends DefaultIndexImplTestBasis
{
	@Test
	public void indexIsNotOpened() throws Exception
	{
		final Exception expected = new HGException(
				"Attempting to operate on index 'sample_index' while the index is being closed.");

        replay(mockedStorage);

		index = new DefaultIndexImpl(INDEX_NAME, mockedStorage, transactionManager,
				keyConverter, valueConverter, comparator, null);

		try
		{
			index.addEntry(1, "one");
		}
		catch (Exception occurred)
		{
			assertExceptions(occurred, expected);
		}
	}

	@Test
	public void keyIsNull() throws Exception
	{
		final Exception expected = new NullPointerException();

		startupIndex();

		try
		{
			index.addEntry(null, "key is null");
		}
		catch (Exception occurred)
		{
			assertExceptions(occurred, expected);
		}
		closeDatabase(index);
	}

	@Test
	public void valueIsNull() throws Exception
	{
		final Exception expected = new NullPointerException();

		startupIndex();

		try
		{
			index.addEntry(2, null);
		}
		catch (Exception occurred)
		{
			assertExceptions(occurred, expected);
		}
		closeDatabase(index);
	}

	@Test
	public void transactionManagerThrowsException() throws Exception
	{
		final Exception expected = new HGException(
				"Failed to add entry to index 'sample_index': java.lang.IllegalStateException: This exception is thrown by fake transaction manager.");

		startupIndexWithFakeTransactionManager();

		try
		{
			index.addEntry(2, "two");
		}
		catch (Exception occurred)
		{
			assertExceptions(occurred, expected);
		}
		finally
		{
			index.close();
		}
	}
}
