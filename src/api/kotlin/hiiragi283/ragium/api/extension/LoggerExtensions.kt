package hiiragi283.ragium.api.extension

import com.mojang.serialization.DataResult
import org.apache.logging.log4j.Logger

//    DataResult    //

/**
 * この[DataResult]のエラーメッセージを[logger]に出力します。
 */
fun <T : Any> DataResult<T>.logError(logger: org.slf4j.Logger): DataResult<T> = ifError { logger.error(it.message()) }

//    Result    //

fun Result<*>.logError(logger: Logger) {
    onFailure { logger.error(it.message) }
}
