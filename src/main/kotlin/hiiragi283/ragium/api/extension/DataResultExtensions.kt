package hiiragi283.ragium.api.extension

import com.mojang.serialization.DataResult
import org.slf4j.Logger
import java.util.*
import kotlin.jvm.optionals.getOrNull

//    DataResult    //

/**
 * [T]を[DataResult]に変換します。
 * @param errorMessage [DataResult.error]を返す場合のメッセージ
 * @return [T]がnullの場合は[DataResult.error]，それ以外の場合は[DataResult.success]
 */
fun <T : Any> T?.toDataResult(errorMessage: () -> String): DataResult<T> =
    this?.let(DataResult<T>::success) ?: DataResult.error(errorMessage)

/**
 * [Optional]を[DataResult]に変換します。
 * @param errorMessage [DataResult.error]を返す場合のメッセージ
 * @return [Optional.isPresent]がtrueの場合は[DataResult.success]，それ以外の場合は[DataResult.error]
 */
fun <T : Any> Optional<T>.toDataResult(errorMessage: () -> String): DataResult<T> =
    map(DataResult<T>::success).orElse(DataResult.error(errorMessage))

/**
 * [DataResult]の結果を返します。
 * @return 結果がない場合は`null`
 */
fun <T : Any> DataResult<T>.getOrNull(): T? = result().getOrNull()

/**
 * [DataResult]の結果を返します。
 * @return 結果がない場合は[other]
 */
fun <T : Any> DataResult<T>.orElse(other: T): T = result().orElse(other)

/**
 * [DataResult]のエラーメッセージを[logger]に出力します。
 */
fun <T : Any> DataResult<T>.logError(logger: Logger): DataResult<T> = ifError { logger.error(it.message()) }
