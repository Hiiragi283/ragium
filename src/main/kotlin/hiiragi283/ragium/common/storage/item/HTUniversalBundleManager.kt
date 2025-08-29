package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack

class HTUniversalBundleManager private constructor(map: Map<DyeColor, HTItemStackHandler>) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTUniversalBundleManager> =
            BiCodecs
                .mapOf(BiCodecs.COLOR, HTItemStackHandler.codec(::BundleHandler))
                .xmap(::HTUniversalBundleManager, HTUniversalBundleManager::map)

        @JvmStatic
        fun emptyHandler(): HTItemStackHandler = BundleHandler(27)
    }

    constructor() : this(mapOf())

    private val map: MutableMap<DyeColor, HTItemStackHandler> = map.toMutableMap()

    fun getHandler(color: DyeColor): HTItemHandler = map.computeIfAbsent(color) { _: DyeColor -> emptyHandler() }

    private class BundleHandler :
        HTItemStackHandler,
        HTMenuCallback {
        constructor(size: Int) : super(size)

        constructor(stacks: List<ItemStack>) : super(stacks.toMutableList())

        override val inputSlots: IntArray = intArrayOf()
        override val outputSlots: IntArray = intArrayOf()

        override fun onContentsChanged() {}

        override fun openMenu(player: Player) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.WOOL_PLACE, SoundSource.PLAYERS)
        }

        override fun closeMenu(player: Player) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.WOOL_BREAK, SoundSource.PLAYERS)
        }
    }
}
