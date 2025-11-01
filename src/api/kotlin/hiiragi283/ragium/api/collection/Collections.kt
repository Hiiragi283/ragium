package hiiragi283.ragium.api.collection

import net.minecraft.util.RandomSource
import kotlin.NoSuchElementException

fun <T> Collection<T>.random(random: RandomSource): T {
    if (isEmpty()) throw NoSuchElementException("Collection is empty.")
    return this.elementAt(random.nextInt(this.size))
}

fun <T> Collection<T>.randomOrNull(random: RandomSource): T? {
    if (isEmpty()) return null
    return this.elementAt(random.nextInt(this.size))
}
