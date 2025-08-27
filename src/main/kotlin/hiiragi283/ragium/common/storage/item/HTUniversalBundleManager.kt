package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.DyeColor

class HTUniversalBundleManager private constructor(map: Map<DyeColor, HTItemStackHandler>) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTUniversalBundleManager> =
            BiCodecs
                .mapOf(BiCodecs.COLOR, HTItemStackHandler.CODEC)
                .xmap(::HTUniversalBundleManager, HTUniversalBundleManager::map)

        @JvmStatic
        fun emptyHandler(): HTItemStackHandler = HTItemStackHandler.Builder(27).build(HTContentListener.NONE)
    }

    constructor() : this(mapOf())

    private val map: MutableMap<DyeColor, HTItemStackHandler> = map.toMutableMap()

    fun getHandler(color: DyeColor): HTItemHandler = map.computeIfAbsent(color) { _: DyeColor -> emptyHandler() }
}
