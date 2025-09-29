package hiiragi283.ragium.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

object RagiumConfig {
    @JvmField
    val COMMON_SPEC: ModConfigSpec

    @JvmField
    val CLIENT_SPEC: ModConfigSpec

    @JvmField
    val COMMON: RagiumCommonConfig

    @JvmField
    val CLIENT: RagiumClientConfig

    init {
        val commonPair: Pair<RagiumCommonConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::RagiumCommonConfig)
        COMMON_SPEC = commonPair.right
        COMMON = commonPair.left

        val clientPair: Pair<RagiumClientConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::RagiumClientConfig)
        CLIENT_SPEC = clientPair.right
        CLIENT = clientPair.left
    }
}
