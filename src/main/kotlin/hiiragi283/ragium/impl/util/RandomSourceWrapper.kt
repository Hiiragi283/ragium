package hiiragi283.ragium.impl.util

import net.minecraft.util.RandomSource
import kotlin.random.Random

internal class RandomSourceWrapper(private val delegate: RandomSource) : Random() {
    override fun nextBits(bitCount: Int): Int = delegate.nextInt().ushr(32 - bitCount) and (-bitCount).shr(31)
}
