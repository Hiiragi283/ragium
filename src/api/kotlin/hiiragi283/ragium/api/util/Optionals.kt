package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.text.HTTextResult
import hiiragi283.ragium.api.text.HTTranslation
import net.minecraft.network.chat.Component
import java.util.Optional

fun <T : Any> T?.wrapOptional(): Optional<T> = Optional.ofNullable(this)

fun <T : Any> T?.toTextResult(message: Component): HTTextResult<T> = when {
    this != null -> HTTextResult.success(this)
    else -> HTTextResult.failure(message)
}

fun <T : Any> T?.toTextResult(translation: HTTranslation): HTTextResult<T> = this.toTextResult(translation.translate())

fun <T : Any, R : T> Optional<R>.toTextResult(message: Component): HTTextResult<T> = when {
    this.isPresent -> HTTextResult.success(this.get())
    else -> HTTextResult.failure(message)
}

fun <T : Any, R : T> Optional<R>.toTextResult(translation: HTTranslation): HTTextResult<T> = this.toTextResult(translation.translate())

fun <T : Any, R : T> Optional<R>.toTextResult(translation: HTTranslation, vararg args: Any?): HTTextResult<T> =
    this.toTextResult(translation.translate(*args))
