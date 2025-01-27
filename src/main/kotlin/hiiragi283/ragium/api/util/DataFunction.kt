package hiiragi283.ragium.api.util

import com.mojang.serialization.DataResult
import java.util.function.Function

/**
 * [T]を[DataResult]に変換する[Function]
 */
fun interface DataFunction<T : Any> :
    Function<T, DataResult<T>>,
    ((T) -> DataResult<T>) {
    companion object {
        /**
         * 常に[DataResult.success]を返す[DataFunction]
         */
        @JvmStatic
        fun <T : Any> success(): DataFunction<T> = DataFunction<T>(DataResult<T>::success)
    }

    override fun invoke(p1: T): DataResult<T> = apply(p1)
}
