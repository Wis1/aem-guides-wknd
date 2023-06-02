/* groovy Pages Activator  */
/**************************************************************************************************
 * {the script finds all deactivated pages on a specific path, then activates them and writes
 * to a file, when and which pages have been activated }
 *************************************************************************************************/

import com.day.cq.dam.api.AssetManager

import javax.jcr.query.Query
import java.time.LocalDateTime

// ############## Input variables  ##############

def path = "/content/wknd/us/en"

def query = createSQL2Query(path)

println "query = ${query.statement}"

def result = query.execute()

def rows = result.rows

def actualDate= LocalDateTime.now().withNano(0).toString().replaceAll("[:-]", ".")

def savePath = "/content/dam/wknd/resultspageactivator/"+actualDate+".txt" ;

// ############## Implementation  ##############

println "found ${rows.size} result(s)"

slingRequest=slingRequest.getResourceResolver();
AssetManager am = slingRequest.adaptTo(AssetManager.class)

ByteArrayOutputStream baos = new ByteArrayOutputStream();
ObjectOutputStream oos = new ObjectOutputStream(baos);

rows.each { row ->
    activate(row.path)
    println "Activated page: ${row.path}"
    oos.writeObject(row.path);
}

oos.flush();
oos.close();

InputStream stream= new ByteArrayInputStream(baos.toByteArray());
am.createAsset(savePath,stream,"",true);

println "Results saved to ${savePath}"

def createSQL2Query(path) {
    def queryManager = session.workspace.queryManager

    def statement = "SELECT * FROM [cq:Page] WHERE ISDESCENDANTNODE('$path') " +
            "AND ([jcr:content/jcr:lastReplicationAction] = 'isDeactivated'"+
            "OR [jcr:content/jcr:lastReplicationAction] IS NULL)"

    def query = queryManager.createQuery(statement, Query.JCR_SQL2)

    query
}