package hiiragi283.ragium.common.storage.item

import hiiragi283.ragium.api.inventory.HTMenuCallback
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.common.inventory.container.HTGenericContainerRows
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor

class HTUniversalBundleManager private constructor(map: Map<DyeColor, HTItemHandler>) {
    constructor() : this(mapOf())

    private val map: MutableMap<DyeColor, HTItemHandler> = map.toMutableMap()

    fun getHandler(color: DyeColor): HTItemHandler =
        map.computeIfAbsent(color) { _: DyeColor -> BundleHandler(HTGenericContainerRows.createHandler(3)) }

    private class BundleHandler(private val delegate: HTItemHandler) :
        HTItemHandler by delegate,
        HTMenuCallback {
        override fun openMenu(player: Player) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.WOOL_PLACE, SoundSource.PLAYERS)
        }

        override fun closeMenu(player: Player) {
            player.level().playSound(null, player.blockPosition(), SoundEvents.WOOL_BREAK, SoundSource.PLAYERS)
        }
    }
}
