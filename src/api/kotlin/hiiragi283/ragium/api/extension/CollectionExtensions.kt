package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.RagiumPlatform
import net.minecraft.util.RandomSource
import kotlin.random.Random

//    RandomSource    //

fun RandomSource.asKotlinRandom(): Random = RagiumPlatform.INSTANCE.wrapRandom(this)
