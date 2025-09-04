package hiiragi283.ragium.common.storage

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.storage.HTAccessConfiguration
import hiiragi283.ragium.api.storage.value.HTValueInput
import hiiragi283.ragium.api.storage.value.HTValueOutput
import hiiragi283.ragium.api.storage.value.HTValueSerializable
import net.minecraft.core.Direction
import net.minecraft.network.FriendlyByteBuf
import org.slf4j.Logger

class HTAccessConfigCache :
    HTAccessConfiguration.Holder,
    HTValueSerializable {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()

        @JvmField
        val CODEC: BiCodec<FriendlyByteBuf, Map<Direction, HTAccessConfiguration>> =
            BiCodecs.mapOf(BiCodecs.DIRECTION, HTAccessConfiguration.CODEC)
    }

    private val cache: MutableMap<Direction, HTAccessConfiguration> = mutableMapOf()

    override fun getAccessConfiguration(side: Direction): HTAccessConfiguration =
        cache.computeIfAbsent(side) { _: Direction -> HTAccessConfiguration.BOTH }

    override fun setAccessConfiguration(side: Direction, value: HTAccessConfiguration) {
        val old: HTAccessConfiguration? = cache.put(side, value)
        LOGGER.debug("Updated access config: {} -> {}", old, value)
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
