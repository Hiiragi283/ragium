package hiiragi283.ragium.common.item

import net.minecraft.world.InteractionResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.PotionItem
import net.minecraft.world.item.context.UseOnContext

class HTPotionSodaItem(properties: Properties) : PotionItem(properties.stacksTo(16)) {
    override fun useOn(context: UseOnContext): InteractionResult = InteractionResult.PASS

    override fun getDescriptionId(stack: ItemStack): String = super.getDescriptionId()
}
