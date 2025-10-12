package hiiragi283.ragium.common.storage

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTAccessConfiguration
import net.minecraft.core.Direction
import net.minecraft.network.FriendlyByteBuf

class HTAccessConfigCache :
    HTAccessConfiguration.Holder,
    HTValueSerializable {
    companion object {
        @JvmField
        val CODEC: BiCodec<FriendlyByteBuf, Map<Direction, HTAccessConfiguration>> =
            BiCodecs.mapOf(VanillaBiCodecs.DIRECTION, HTAccessConfiguration.CODEC)
    }

    private val cache: MutableMap<Direction, HTAccessConfiguration> = mutableMapOf()

    override fun getAccessConfiguration(side: Direction): HTAccessConfiguration =
        cache.computeIfAbsent(side) { _: Direction -> HTAccessConfiguration.BOTH }

    override fun setAccessConfiguration(side: Direction, value: HTAccessConfiguration) {
        val old: HTAccessConfiguration? = cache.put(side, value)
        RagiumAPI.LOGGER.debug("Updated access config: {} -> {}", old, value)
    }

    //    HTNbtSerializable    //

    override fun serialize(output: HTValueOutput) {
        if (cache.isEmpty()) return
        output.store(RagiumConst.ACCESS_CONFIG, CODEC, cache)
    }

    override fun deserialize(input: HTValueInput) {
        input.read(RagiumConst.ACCESS_CONFIG, CODEC)?.forEach(cache::put)
    }
}
