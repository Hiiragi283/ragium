package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTDeferredRegister
import hiiragi283.ragium.common.world.feature.HTBlockBlobFeature
import hiiragi283.ragium.common.world.feature.HTFilteredBlockConfiguration
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.levelgen.feature.Feature
import java.util.function.Supplier

object RagiumFeatures {
    @JvmField
    val REGISTER: HTDeferredRegister<Feature<*>> = HTDeferredRegister(Registries.FEATURE, RagiumAPI.MOD_ID)

    @JvmField
    val ROCK: Supplier<HTBlockBlobFeature> =
        REGISTER.register("rock") { _ -> HTBlockBlobFeature(HTFilteredBlockConfiguration.CODEC) }
}
