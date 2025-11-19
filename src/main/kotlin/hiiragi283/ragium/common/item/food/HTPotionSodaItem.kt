package hiiragi283.ragium.common.item.food

import hiiragi283.ragium.api.item.HTSubCreativeTabContents
import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.common.util.HTPotionHelper
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.PotionItem
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.context.UseOnContext
import java.util.function.Consumer

class HTPotionSodaItem(properties: Properties) :
    PotionItem(properties.stacksTo(16)),
    HTSubCreativeTabContents {
    override fun useOn(context: UseOnContext): InteractionResult = InteractionResult.PASS

    override fun getDescriptionId(stack: ItemStack): String = super.getDescriptionId()

    override fun addItems(baseItem: HTItemHolderLike, consumer: Consumer<ItemStack>) {
        BuiltInRegistries.POTION
            .holders()
            .forEach { holder: Holder<Potion> ->
                consumer.accept(HTPotionHelper.createPotion(baseItem, holder))
            }
    }

    override fun shouldAddDefault(): Boolean = false
}
