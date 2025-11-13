package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.text.HTTextResult
import net.minecraft.network.chat.Component
import java.util.Optional

fun <T : Any> T?.wrapOptional(): Optional<T> = Optional.ofNullable(this)

fun <T : Any, R : T> Optional<R>.toTextResult(message: Component): HTTextResult<T> = when {
    this.isPresent -> HTTextResult.success(this.get())
    else -> HTTextResult.failure(message)
}
