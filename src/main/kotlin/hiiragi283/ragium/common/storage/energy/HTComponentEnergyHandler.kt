package hiiragi283.ragium.common.storage.energy

import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.energy.HTEnergyHandler
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack

/**
 * [HTEnergyHandler]に基づいたコンポーネント向けの実装
 */
class HTComponentEnergyHandler(private val parent: ItemStack, private val battery: HTEnergyBattery) : HTEnergyHandler {
    override fun getEnergyBattery(side: Direction?): HTEnergyBattery = battery
}
