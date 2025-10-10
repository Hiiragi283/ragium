package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumPlatform
import net.minecraft.core.NonNullList
import net.minecraft.util.RandomSource
import kotlin.random.Random

//    NonNullList    //

fun <T : Any> Collection<T>.toNonNullList(): NonNullList<T> = NonNullList.copyOf(this)

//    RandomSource    //

fun RandomSource.asKotlinRandom(): Random = RagiumPlatform.INSTANCE.wrapRandom(this)
