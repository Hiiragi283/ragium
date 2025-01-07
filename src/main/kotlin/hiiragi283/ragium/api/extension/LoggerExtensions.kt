package hiiragi283.ragium.api.extension

import com.mojang.serialization.DataResult
import org.slf4j.Logger

fun Logger.error(throwable: Throwable): Unit = error(throwable.message)

fun Logger.error(error: DataResult.Error<*>): Unit = error(error.message())
