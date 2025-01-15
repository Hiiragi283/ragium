package hiiragi283.ragium.api.extension

import java.util.*
import java.util.function.Supplier

//    Result    //

fun <T : Any> Optional<T>.toResult(supplier: Supplier<Throwable>): Result<T> = runCatching { orElseThrow(supplier) }
