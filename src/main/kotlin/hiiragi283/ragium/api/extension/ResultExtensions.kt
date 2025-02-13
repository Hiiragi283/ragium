package hiiragi283.ragium.api.extension

import org.apache.logging.log4j.Logger

//    Result    //

fun Result<*>.logError(logger: Logger) {
    onFailure { logger.error(it.message) }
}
