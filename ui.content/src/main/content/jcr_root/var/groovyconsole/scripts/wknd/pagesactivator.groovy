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

StringBuilder stringBuilder = new StringBuilder("Activated pages:\n\n")

// ############## Implementation  ##############

def debug(String message) {
    if (DEBUG) {
        println message
        log.info(message)
    }
}

def activatePages(rows,stringBuilder) {
    rows.each { row ->
        activate(row.path)
        stringBuilder.append(row.path + "\n")
    }
}

def createReport(stringBuilder) {
    AssetManager am = resourceResolver.adaptTo(AssetManager.class)
    InputStream stream = new ByteArrayInputStream(stringBuilder.toString().getBytes())
    def actualDate = LocalDateTime.now().withNano(0).toString().replaceAll("[:-]", ".")
    def savePath = "/content/dam/wknd/resultspageactivator/" + actualDate + ".txt"
    am.createAsset(savePath, stream, "text/plain", !DRY_RUN)
}

def findDeactivatePages(path) {
    def queryManager = session.workspace.queryManager

    def statement = "SELECT * FROM [cq:Page] WHERE ISDESCENDANTNODE('$path') " +
            "AND ([jcr:content/jcr:lastReplicationAction] = 'Deactivate'" +
            "OR [jcr:content/jcr:lastReplicationAction] IS NULL)"
    queryManager.createQuery(statement, Query.JCR_SQL2).execute().rows
}

// ############## Execution  ##############

try {
    if (!DRY_RUN) {
        activatePages(findDeactivatePages(path), stringBuilder)
    }
    createReport(stringBuilder)
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