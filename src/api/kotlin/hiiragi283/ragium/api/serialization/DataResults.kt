package hiiragi283.ragium.api.serialization

import com.mojang.serialization.DataResult
import kotlin.jvm.optionals.getOrNull

fun <R : Any> DataResult<R>.resultOrNull(): R? = this.result().getOrNull()
