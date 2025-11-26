package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack

fun interface HTDynamicUpgradeItem {
    fun getUpgrade(provider: HolderLookup.Provider?, stack: ItemStack): HTMachineUpgrade?
}
