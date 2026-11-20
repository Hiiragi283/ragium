package hiiragi283.ragium.common.item.block

import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.lib.transfer.energy.HTEnergyStorageUtil
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block

class HTBatteryBlockItem(block: Block, properties: Properties) : BlockItem(block, properties) {
    override fun isBarVisible(stack: ItemStack): Boolean = HTEnergyCapabilities.getStorage(stack) != null

    override fun getBarWidth(stack: ItemStack): Int = HTEnergyStorageUtil.getEnergyBarWidth(stack)
}
