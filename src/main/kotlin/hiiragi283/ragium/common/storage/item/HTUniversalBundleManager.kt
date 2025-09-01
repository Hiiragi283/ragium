package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.inventory.container.HTGenericContainerRows
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor

class HTUniversalBundleManager private constructor(map: Map<DyeColor, HTItemHandler>) {
    companion object {
        @JvmStatic
        private val RAW_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemHandler> =
            HTItemStackHandler.codec(::BundleHandler)

        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTUniversalBundleManager> =
            BiCodecs
                .mapOf(BiCodecs.COLOR, RAW_CODEC)
                .xmap(::HTUniversalBundleManager, HTUniversalBundleManager::map)
    }

    constructor() : this(mapOf())

    private val map: MutableMap<DyeColor, HTItemHandler> = map.toMutableMap()

    fun getHandler(color: DyeColor): HTItemHandler = map.computeIfAbsent(color) { _: DyeColor -> BundleHandler() }

    private class BundleHandler(private val delegate: HTItemHandler) :
        HTItemHandler by delegate,
        HTMenuCallback {
        constructor() : this(HTGenericContainerRows.createHandler(3))

        constructor(slots: List<HTItemSlot>) : this(HTItemStackHandler(slots, null))

        override fun openMenu(player: Player) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.WOOL_PLACE, SoundSource.PLAYERS)
        }

        override fun closeMenu(player: Player) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.WOOL_BREAK, SoundSource.PLAYERS)
        }
    }
}
