package hiiragi283.ragium.common.storage

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.extension.buildNbt
import hiiragi283.ragium.api.storage.HTAccessConfiguration
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.neoforged.neoforge.common.util.INBTSerializable
import org.slf4j.Logger

class HTAccessConfigCache :
    HTAccessConfiguration.Holder,
    INBTSerializable<CompoundTag> {
    companion object {
        @JvmField
        val LOGGER: Logger = LogUtils.getLogger()
    }

    private val cache: MutableMap<Direction, HTAccessConfiguration> = mutableMapOf()

    override fun getAccessConfiguration(side: Direction): HTAccessConfiguration =
        cache.computeIfAbsent(side) { _: Direction -> HTAccessConfiguration.BOTH }

    override fun setAccessConfiguration(side: Direction, value: HTAccessConfiguration) {
        val old: HTAccessConfiguration? = cache.put(side, value)
        LOGGER.debug("Updated access config: {} -> {}", old, value)
    }

    //    INBTSerializable    //

    override fun serializeNBT(provider: HolderLookup.Provider): CompoundTag = buildNbt {
        for (side: Direction in Direction.entries) {
            val configuration: HTAccessConfiguration = cache[side] ?: continue
            HTAccessConfiguration.CODEC
                .encode(NbtOps.INSTANCE, configuration)
                .ifSuccess { this.put(side.serializedName, it) }
        }
    }

    override fun deserializeNBT(provider: HolderLookup.Provider, nbt: CompoundTag) {
        for (side: Direction in Direction.entries) {
            val tag: Tag = nbt.get(side.serializedName) ?: continue
            HTAccessConfiguration.CODEC
                .decode(NbtOps.INSTANCE, tag)
                .ifSuccess { setAccessConfiguration(side, it) }
        }
    }
}
