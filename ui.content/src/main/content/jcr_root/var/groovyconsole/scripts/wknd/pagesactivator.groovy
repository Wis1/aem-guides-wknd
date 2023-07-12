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

    AssetManager am = resourceResolver.adaptTo(AssetManager.class)
    StringBuilder stringBuilder= new StringBuilder("Activated pages:\n\n")

    rows.each { row ->
        activate(row.path)
        debug("Activated page: ${row.path}")
        stringBuilder.append(row.path+"\n")
    }

    InputStream stream = new ByteArrayInputStream(stringBuilder.toString().getBytes())
    am.createAsset(savePath, stream, "text/plain", true)

    debug("Results saved to ${savePath}")
}

def createSQL2Query(path) {
    def queryManager = session.workspace.queryManager

    def statement = "SELECT * FROM [cq:Page] WHERE ISDESCENDANTNODE('$path') " +
            "AND ([jcr:content/jcr:lastReplicationAction] = 'Deactivate'" +
            "OR [jcr:content/jcr:lastReplicationAction] IS NULL)"

    queryManager.createQuery(statement, Query.JCR_SQL2)
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