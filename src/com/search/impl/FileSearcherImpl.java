package com.search.impl;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.search.FileSearcher;

/**
 * Simple searcher class
 *
 * @author Jules Jay Paulynice
 *
 */
public class FileSearcherImpl implements FileSearcher {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    /*
     * (non-Javadoc)
     * 
     * @see com.search.FileSearcher#search(java.lang.String, int)
     */
    @Override
    public void search(final String queryStr, final int maxHits)
            throws IOException, ParseException {
        final DirectoryReader ireader = DirectoryReader.open(Utils
                .getIndexDir());
        final IndexSearcher searcher = new IndexSearcher(ireader);

        searchIndex(searcher, queryStr, maxHits);
        ireader.close();
    }

    /**
     * Search the index for given query and return only specified hits.
     *
     * @param searcher
     * @param queryStr
     * @param maxHits
     * @throws IOException
     * @throws ParseException
     */
    private void searchIndex(final IndexSearcher searcher,
            final String queryStr, final int maxHits) throws IOException,
            ParseException {
        final Query query = Utils.getQueryParser().parse(queryStr);
        final ScoreDoc[] hits = searcher.search(query, null, maxHits).scoreDocs;
        LOG.info(String.format("Found %d documents matching the query: %s",
                hits.length, queryStr));

        getResults(searcher, hits);
    }

    /**
     * Get search results
     * 
     * @param searcher
     * @param hits
     * @throws IOException
     */
    private void getResults(final IndexSearcher searcher, final ScoreDoc[] hits)
            throws IOException {
        LOG.info("Search results: \n");
        for (final ScoreDoc d : hits) {
            final Document doc = searcher.doc(d.doc);
            LOG.info(doc.get("filepath"));
        }
    }
}
