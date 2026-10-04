package hiiragi283.ragium.common.item.block

import hiiragi283.lib.item.HTSubCreativeTabContents
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.util.HTStorageHelper
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.block.Block

class HTPotionTankBlockItem(block: Block, properties: Properties) :
    BlockItem(block, properties),
    HTSubCreativeTabContents {
    override fun addItems(
        baseItem: Holder<Item>,
        parameters: CreativeModeTab.ItemDisplayParameters,
        output: CreativeModeTab.Output
    ) {
        for (potion: Holder<Potion> in parameters.filteredElements(Registries.POTION)) {
            if (potion == Potions.WATER) continue
            val stack = ItemStack(baseItem)
            HTStorageHelper.updateFluid(
                stack,
                HTPotionHelper.createFluid(potion, amount = RagiumConfig.SERVER.tankCapacity.asInt)
            )
            output.accept(stack)
        }
    }
}
