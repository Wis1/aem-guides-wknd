/* groovy Pages Activator  */
/**************************************************************************************************
 * {the script finds all deactivated pages on a specific path, then activates them and writes
 * to a file when and which pages have been activated }
 *************************************************************************************************/

import com.day.cq.dam.api.AssetManager
import groovy.transform.Field
import javax.jcr.query.Query
import java.time.LocalDateTime

// ############## Input variables  ##############

// set to false to save changes
@Field final boolean DRY_RUN = true

// set to true to print debug messages
@Field final boolean DEBUG = true

def path = "/content/wknd/us/en/magazine"

def query = createSQL2Query(path)

debug("query = ${query.statement}")

def result = query.execute()

def rows = result.rows

def actualDate = LocalDateTime.now().withNano(0).toString().replaceAll("[:-]", ".")

def savePath = "/content/dam/wknd/resultspageactivator/" + actualDate + ".txt"

// ############## Implementation  ##############

def debug(String message) {
    if (DEBUG) {
        println message
        log.info(message)
    }
}

debug("found ${rows.size} result(s)")

def activatePagesAndWriteToFile(rows, savePath) {

    slingRequest = slingRequest.getResourceResolver()
    AssetManager am = slingRequest.adaptTo(AssetManager.class)

    ByteArrayOutputStream baos = new ByteArrayOutputStream()
    ObjectOutputStream oos = new ObjectOutputStream(baos)

    rows.each { row ->
        activate(row.path)
        debug("Activated page: ${row.path}")
        oos.writeObject(row.path + "\n")
    }

    oos.flush()
    oos.close()

    InputStream stream = new ByteArrayInputStream(baos.toByteArray())
    am.createAsset(savePath, stream, "", true)

    debug("Results saved to ${savePath}")
}

def createSQL2Query(path) {
    def queryManager = session.workspace.queryManager

    def statement = "SELECT * FROM [cq:Page] WHERE ISDESCENDANTNODE('$path') " +
            "AND ([jcr:content/jcr:lastReplicationAction] = 'isDeactivated'" +
            "OR [jcr:content/jcr:lastReplicationAction] IS NULL)"

    def query = queryManager.createQuery(statement, Query.JCR_SQL2)

    query
}

// ############## Execution  ##############

try {

    activatePagesAndWriteToFile(rows, savePath)

} catch (Exception e) {
    session.refresh(false)
    throw e
} finally {
    if (DRY_RUN) {
        debug('Dry run finished. No changes saved')
        session.refresh(false)
    } else {
        debug('Migration finished. Changes saved')
        session.save()
    }
}