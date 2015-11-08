import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import java.io.IOException;

/**
 * Created by vargar2 on 08-Nov-15.
 */
public class Run {

    public static void main(String[] args) throws IOException, ParseException {

        Analyzer analyzer = new WhitespaceAnalyzer();
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, config);

        Document doc = new Document();
        String text = "Lucene is an Information Retrieval library written in Java.";
        doc.add(new TextField("Content", text, Field.Store.YES));
        indexWriter.addDocument(doc);

        doc = new Document();
        text = "Lucene is and Information Retrieval library written in Java.";
        doc.add(new TextField("Content", text, Field.Store.YES));
        indexWriter.addDocument(doc);

        doc = new Document();
        text = "Ervian";
        doc.add(new TextField("Content", text, Field.Store.YES));
        indexWriter.addDocument(doc);

        doc = new Document();
        text = "Erviand";
        doc.add(new TextField("Content", text, Field.Store.YES));
        indexWriter.addDocument(doc);

        doc = new Document();
        text = "Ervia";
        doc.add(new TextField("Content", text, Field.Store.YES));
        indexWriter.addDocument(doc);

        indexWriter.deleteDocuments(new Term("id", "1"));
        indexWriter.close();

        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        QueryParser parser = new QueryParser("Content", analyzer);
        Query query = parser.parse("Lucene is and");
//      Query query = parser.parse("ervi");

        TopDocs docs = indexSearcher.search(query, 10);
        ScoreDoc[] hits = docs.scoreDocs;

        for (int i = 0; i < hits.length; i++) {
            Document d = indexSearcher.doc(hits[i].doc);
            System.out.println("Content: " + d.get("Content"));
        }
    }
}
