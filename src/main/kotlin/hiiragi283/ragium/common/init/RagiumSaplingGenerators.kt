package hiiragi283.ragium.common.init

import net.minecraft.block.SaplingGenerator
import java.util.*

object RagiumSaplingGenerators {
    @JvmField
    val RUBBER = SaplingGenerator(
        "rubber",
        Optional.empty(),
        Optional.of(RagiumConfiguredFeatures.RUBBER),
        Optional.empty(),
    )
}
