package org.purl.linkedepcis.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.openrdf.model.Graph;
import org.openrdf.model.Model;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.BindingSet;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.Update;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.query.resultio.sparqljson.SPARQLResultsJSONWriter;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.UnknownTransactionStateException;
import org.openrdf.repository.config.RepositoryConfig;
import org.openrdf.repository.config.RepositoryConfigException;
import org.openrdf.repository.manager.RemoteRepositoryManager;
import org.openrdf.repository.manager.RepositoryManager;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;
import org.openrdf.rio.UnsupportedRDFormatException;
import org.openrdf.rio.helpers.StatementCollector;
import org.openrdf.rio.rdfxml.RDFXMLWriter;
import org.openrdf.rio.rdfxml.util.RDFXMLPrettyWriter;
import org.openrdf.rio.turtle.TurtleWriter;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;

import java.math.BigDecimal;
import org.purl.linkedepcis.query.QueryEPCISEvents;

//This class contains common repository level functions
/**
 * @author monika
 *
 */
public class CommonFunctions {

    // The repository manager
    private RepositoryManager repositoryManager;

	// From repositoryManager.getRepository(...) - the actual repository we
    // will
    // work with
    private Repository repository;

	// From repository.getConnection() - the connection through which we
    // will
    // use the repository
    private RepositoryConnection repositoryConnection = null;
    private String repositoryURL=null;

	// methods that use the repository ***********
    // get number of statements in the repos.
    public int getNumberOfStatementsInRepos(InputStream config, String defaultNS) {
        int numberOfStatements = 0;

        repositoryConnection = setUpSesameRepository(config, repositoryURL, defaultNS, false);
        try {
            RepositoryResult<Statement> statements = repositoryConnection
                    .getStatements(null, null, null, false);
            ArrayList<Statement> art = new ArrayList<Statement>();
            while (statements.hasNext()) {
                art.add(statements.next());
            }
            numberOfStatements = art.size();
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        closeRepository(repositoryConnection);
        return numberOfStatements;

    }

    public RepositoryConnection setupDummyRepos() {

        try {

            repository = new SailRepository(new MemoryStore());
            repository.initialize();
            repositoryConnection = repository.getConnection();
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return repositoryConnection;
    }

    /*
     * public void updateRepository(String updateString) { RepositoryConnection
     * repositoryConnection=null; try { Update update =
     * repositoryConnection.prepareUpdate( QueryLanguage.SPARQL, updateString);
     * update.execute(); } catch (RepositoryException e) { // TODO
     * Auto-generated catch block e.printStackTrace(); } catch
     * (MalformedQueryException e) { // TODO Auto-generated catch block
     * e.printStackTrace(); } catch (UpdateExecutionException e) { // TODO
     * Auto-generated catch block e.printStackTrace(); }
     * System.out.println("data updated"); }
     */
    public void dumpFileInRepository(File f, String defaultNS,
            RDFFormat format, URI contextURI) {
        try {
            repositoryConnection.add(f, defaultNS, format, contextURI);

        } catch (RDFParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            System.out.println(" dump file in  "+repositoryConnection);
            closeRepository(repositoryConnection);
        }
    }

    public void updateRepository(String updateString, RepositoryConnection rc) {
        try {

            Update update = rc
                    .prepareUpdate(QueryLanguage.SPARQL, updateString);
            update.execute();
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedQueryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UpdateExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        closeRepository(rc);
        System.out.println("data updated");
    }

    public GraphQueryResult getGraphQueryResults(String query,
            RepositoryConnection repositoryConnection) {
        GraphQueryResult result = null;
        try {
            File f = new File("rdf-result.rdf");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream fs = new FileOutputStream(f);

            File ft = new File("ttl-results.ttl");
            FileOutputStream fst = new FileOutputStream(ft);

            RDFXMLWriter rdfwriter = new RDFXMLWriter(fs);

            TurtleWriter ttlWriter = new TurtleWriter(fst);
            GraphQuery graphQuery = repositoryConnection.prepareGraphQuery(
                    QueryLanguage.SPARQL, query);
            result = graphQuery.evaluate();
            // produces RDF output
            graphQuery.evaluate(rdfwriter);
            // produces Turtle output
            graphQuery.evaluate(ttlWriter);
            System.out.println("done");
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedQueryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (QueryEvaluationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RDFHandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    public GraphQueryResult getGraphQueryResults(String query,
            InputStream config, String defaultNS) {
        GraphQueryResult result = null;
        File fpath = new File(QueryEPCISEvents.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        System.out.println("*********" + fpath.getParent());
        File fdir = new File((new File(fpath.getParent()).getParent() + "/dataFiles"));
        System.out.println("*********" + fdir.getPath());
        if (!fdir.exists()) {
            fdir.mkdir();
        }
	//	File f = new File(fdir.getPath()+"/query-results.json");

        try {
            File f = new File(fdir.getPath() + "/rdf-result.rdf");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream fs = new FileOutputStream(f);

            File ft = new File(fdir.getPath() + "/ttl-results.ttl");
            //	File ftj = new File("json-results.json");
            FileOutputStream fst = new FileOutputStream(ft);
		//	FileOutputStream fj = new FileOutputStream(ftj);

            RDFXMLPrettyWriter rdfwriter = new RDFXMLPrettyWriter(fs);

            TurtleWriter ttlWriter = new TurtleWriter(fst);

            RepositoryConnection rc = setUpSesameRepository(config, repositoryURL, defaultNS,
                    false);

            GraphQuery graphQuery = rc.prepareGraphQuery(QueryLanguage.SPARQL,
                    query);

		//	result = graphQuery.evaluate();
            // produces RDF output
            graphQuery.evaluate(rdfwriter);
            // produces Turtle output
            graphQuery.evaluate(ttlWriter);
            //	graphQuery.evaluate(rdfw);
            fst.flush();
            fst.close();
		//	fj.flush();
            //	fj.close();
            System.out.println(" turtle file exists" + ft.exists());
            System.out.println(ft.getAbsolutePath());
            fs.flush();
            fs.close();
            //		closeRepository(rc);
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedQueryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (QueryEvaluationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RDFHandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    // a generic tuple query method for the class
    public ArrayList<BindingSet> returnQueryResults(String query,
            InputStream config, String defaultNS) {

        System.out.println(query);

        RepositoryConnection rc = setUpSesameRepository(config, repositoryURL, defaultNS,
                false);
        TupleQueryResult result = getQueryResults(query, rc);
        // cf.saveQueryResultsInJSON(query);

        ArrayList<BindingSet> bst = new ArrayList<BindingSet>();
        try {
            while (result.hasNext()) {
                bst.add(result.next());
            }
        } catch (QueryEvaluationException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        closeRepository(rc);
        try {
            result.close();
        } catch (QueryEvaluationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // System.out.println(bst.size());
        rc = null;
        return bst;
    }

    public TupleQueryResult getQueryResults(String query,
            RepositoryConnection repositoryConnection) {

        TupleQueryResult result = null;

        try {
            TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(
                    QueryLanguage.SPARQL, query);

            result = tupleQuery.evaluate();

            SPARQLResultsJSONWriter srj = null;
            RDFXMLWriter rdf = null;

            File fpath = new File(QueryEPCISEvents.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            System.out.println("*********" + fpath.getParent());
            File fdir = new File((new File(fpath.getParent()).getParent() + "/dataFiles"));
            System.out.println("*********" + fdir.getPath());
            if (!fdir.exists()) {
                fdir.mkdir();
            }
            File f = new File(fdir.getPath() + "/query-results.json");

            if (f.exists()) {
                f.delete();
            }
            FileOutputStream fs = new FileOutputStream(f);
            srj = new SPARQLResultsJSONWriter(fs);

            tupleQuery.evaluate(srj);

        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedQueryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (QueryEvaluationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TupleQueryResultHandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        repositoryConnection = null;
        return result;
    }

    public void saveQueryResultsInJSON(
            RepositoryConnection repositoryConnection, String query) {

        try {
            TupleQuery tupleQuery = repositoryConnection.prepareTupleQuery(
                    QueryLanguage.SPARQL, query);
            SPARQLResultsJSONWriter srj = null;

            FileOutputStream fs = new FileOutputStream(new File(
                    "query-results.json"));
            srj = new SPARQLResultsJSONWriter(fs);
            tupleQuery.evaluate(srj);

        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedQueryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (QueryEvaluationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TupleQueryResultHandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        closeRepository(repositoryConnection);
        repositoryConnection = null;
    }

    public void closeRepository(RepositoryConnection reposConn) {
        System.out.println("close repo..."+reposConn);
        flushRepository(reposConn);
    }

    /**
     *
     * @param namespacePrefixes The data structure holding the prefixes of the
     * namespaces.
     */
    public void setnamespacesInRepository(Map namespacePrefixes) {
        Set keySet = namespacePrefixes.keySet();
        Iterator itk = keySet.iterator();
        try {
            while (itk.hasNext()) {
                String key = itk.next().toString();
                System.out.println("key is ..." + key);
                String value = namespacePrefixes.get(key).toString();
                System.out.println("value is ..." + value);
                System.out.println(repositoryConnection);
                repositoryConnection.setNamespace(key, value);

            }
            closeRepository(repositoryConnection);
        } catch (RepositoryException e) {

            e.printStackTrace();
        } finally {

        }

    }

    // write the graph to a file
    public void persistToFile(Graph myGraph, String f) {
        try {
            FileOutputStream fout = new FileOutputStream(f);

            RDFWriter turtleWriter = Rio.createWriter(RDFFormat.TURTLE, fout);
			// WriterConfig config = writer.getWriterConfig();
            // config.set(BasicWriterSettings.PRETTY_PRINT, true);
            turtleWriter.startRDF();
            for (Statement statement : myGraph) {
                // System.out.println(statement);
                turtleWriter.handleStatement(statement);
            }
            turtleWriter.endRDF();
            fout.close();
            // System.out.println(f.getAbsolutePath() + " "+f.exists());
        } catch (UnsupportedRDFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RDFHandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

	// print graph
    public void printGraph(Graph myGraph) {
        try {

            RDFWriter turtleWriter = Rio.createWriter(RDFFormat.JSONLD,
                    System.out);
            turtleWriter.startRDF();
            for (Statement statement : myGraph) {
                System.out.println(statement);
                turtleWriter.handleStatement(statement);
            }
            turtleWriter.endRDF();
        } catch (UnsupportedRDFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RDFHandlerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Model parseFile(InputStream configurationFile, RDFFormat format,
            String defaultNamespace) throws Exception {
        final Graph graph = new GraphImpl();
        RDFParser parser = Rio.createParser(format);
        org.openrdf.model.Model myGraph = new org.openrdf.model.impl.LinkedHashModel();

        /*
         * RDFHandler handler = new RDFHandler() { public void endRDF() throws
         * RDFHandlerException { }
         * 
         * public void handleComment(String arg0) throws RDFHandlerException { }
         * 
         * public void handleNamespace(String arg0, String arg1) throws
         * RDFHandlerException { }
         * 
         * public void handleStatement(Statement statement) throws
         * RDFHandlerException { graph.add(statement); }
         * 
         * public void startRDF() throws RDFHandlerException { } };
         */
        parser.setRDFHandler(new StatementCollector(myGraph));

        parser.parse(configurationFile, defaultNamespace);
        return myGraph;
    }

    public RepositoryConnection getCurrentRepositoryConnection() {
        return this.repositoryConnection;
    }

    public RepositoryConnection setUpSesameRepository(InputStream config, String url,
            String defaultNS, boolean addData) {

        repositoryConnection = null;
        try {
          
            repositoryURL=url;
            repositoryManager = new RemoteRepositoryManager(repositoryURL);

            // Parse the configuration file, assuming it is in Turtle format
            final Model model = parseFile(config, RDFFormat.TURTLE, defaultNS);
			// Look for the subject of the first matching statement for
            // "?s type Repository"
            Iterator<Statement> iter = model
                    .filter(null,
                            RDF.TYPE,
                            new URIImpl(
                                    "http://www.openrdf.org/config/repository#Repository"))
                    .iterator();
            Resource repositoryNode = null;
            if (iter.hasNext()) {
                Statement st = iter.next();
                System.out.println(st);
                repositoryNode = st.getSubject();
            }

            iter = model
                    .filter(null,
                            new URIImpl(
                                    "http://www.openrdf.org/config/repository#repositoryID"),
                            null).iterator();
            String repositoryID = null;
            if (iter.hasNext()) {
                Statement st = iter.next();
                Value valueString = st.getObject();
                repositoryID = valueString.stringValue();
            }

            System.out.println("the repos ID" + repositoryID);
            // Initialise the repository manager
            repositoryManager.initialize();

            /*// remove the repository if there is an existing repository with the
             // same ID
			
             if (addData) { if
             (repositoryManager.hasRepositoryConfig(repositoryID)) {
             removeRepository(repositoryID);
			  
             } }*/
			// Create a configuration object from the configuration file and add
            // it
            // to the repositoryManager
            RepositoryConfig repositoryConfig = RepositoryConfig.create(model,
                    repositoryNode);
            repositoryManager.addRepositoryConfig(repositoryConfig);

            repository = repositoryManager.getRepository(repositoryID);

            // Open a connection to this repository
            repositoryConnection = repository.getConnection();
            repositoryConnection.begin();

        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RepositoryConfigException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            System.out.println("connection setup+repo"
                    + repositoryConnection.isActive());
        } catch (UnknownTransactionStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return repositoryConnection;
    }

    /*
     * This method removes a repository from the repository maanger
     */
    private void removeRepository(String repositoryID) {

        try {
            System.out.println("repository removed..."
                    + repositoryManager.removeRepository(repositoryID));

        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RepositoryConfigException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /*
     * This method checks if a repository is instantiated, it then commits the
     * contents and then
     */
    /*
     * private void flushRepository() { System.out.println("Flushing..."); try {
     * 
     * if (repository != null) { repositoryConnection.commit();
     * repositoryConnection.close(); repository.shutDown(); if
     * (repositoryManager != null) repositoryManager.shutDown();
     * 
     * } } catch (RepositoryException e) { // TODO Auto-generated catch block
     * e.printStackTrace(); }
     * 
     * }
     */
    private void flushRepository(RepositoryConnection reposConn) {
        System.out.println("Flushing..."+reposConn);

        try {
            System.out.println(reposConn);
            reposConn.commit();
            reposConn.close();
            reposConn.getRepository().shutDown();

            repository = null;
            repositoryConnection = null;
            repositoryManager = null;

            if (reposConn.getRepository() != null) {
                reposConn.commit();
                reposConn.close();

                reposConn.getRepository().shutDown();
                reposConn = null;
            }
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // repositoryManager.shutDown();
        System.out.println("repository..." + reposConn);
    }

    public void dumpdataInRepository(RepositoryConnection repositoryConnection, Graph myGraph, String contextStr,
            boolean clear) {

        System.out.println("size of graph is..." + myGraph.size());
        try {
            URI context = new URIImpl(contextStr);
            System.out.println(repositoryConnection);
            System.out.println(context);
			// System.out.println((repositoryConnection.getStatements(null,
            // null,
            // null, true, context)).asList().size());
            if (clear) {
                repositoryConnection.clear(context);
                repositoryConnection.commit();
            }
			// System.out.println((repositoryConnection.getStatements(null,
            // null, null, true, context)).asList().size());
            System.out.println("Actual size. 1.. "
                    + repositoryConnection.size(context));

            for (Statement statement : myGraph) {
                repositoryConnection.add(statement, context);
            }
            System.out.println("Actual size..2. "
                    + repositoryConnection.size(context));
            flushRepository(repositoryConnection);
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void dumpdataInRepository(Graph myGraph, String contextStr,
            boolean clear) {

        System.out.println("size of graph is..." + myGraph.size());
        try {
            URI context = new URIImpl(contextStr);
            System.out.println(repositoryConnection);
            System.out.println(context);
			// System.out.println((repositoryConnection.getStatements(null,
            // null,
            // null, true, context)).asList().size());
            if (clear) {
                repositoryConnection.clear(context);
                repositoryConnection.commit();
            }
			// System.out.println((repositoryConnection.getStatements(null,
            // null, null, true, context)).asList().size());
            System.out.println("Actual size. 1.. "
                    + repositoryConnection.size(context));

            for (Statement statement : myGraph) {
                repositoryConnection.add(statement, context);
            }
            System.out.println("Actual size..2. "
                    + repositoryConnection.size(context));
            flushRepository(repositoryConnection);
        } catch (RepositoryException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    
    
    
    public double roundOfToSeven(double number) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(7, BigDecimal.ROUND_DOWN);
        number = bd.doubleValue();
        return number;
    }

    public double roundOfToTwo(double number) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
        number = bd.doubleValue();
        return number;
    }

    public String roundOfToSeven(String number) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(7, BigDecimal.ROUND_DOWN);
        number = bd.toString();
        return number;
    }

    public String roundOfToTwo(String number) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(2, BigDecimal.ROUND_DOWN);
        number = bd.toString();
        return number;
    }

    public void copyFileFromURL(String path, String dir) {
        // System.out.println(vStr);
        URL url = null;
        java.net.URI uri = null;
        BufferedReader bufReader = null;
        BufferedWriter bufWriter = null;

        try {
            url = new URL(path);
            uri = new java.net.URI(path);

            HttpURLConnection ht = (HttpURLConnection) url.openConnection();
            int code = ht.getResponseCode();

            if (uri != null && code == 200) {
                bufReader = new BufferedReader(new InputStreamReader(
                        url.openStream()));
                if (bufReader != null) {
                    String line = null;
                    String filename = path.substring(path.lastIndexOf(":") + 1)
                            + ".rdf";
                    System.out.println(filename);

                    File f = new File(dir, filename);

                    if (f.exists()) {
                        f.delete();
                    }

                    bufWriter = new BufferedWriter(new FileWriter(f));

                    boolean flag = false;
                    while ((line = bufReader.readLine()) != null) {
                        // Process the data, here we just print it out

                        bufWriter.append(line);
                        bufWriter.append("\n");

                    }
                }
                bufReader.close();
                bufWriter.close();

            }
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
