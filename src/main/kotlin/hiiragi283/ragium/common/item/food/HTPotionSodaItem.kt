package hiiragi283.ragium.common.item.food

import hiiragi283.ragium.api.item.HTSubCreativeTabContents
import hiiragi283.ragium.api.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.registry.HTItemHolderLike
import net.minecraft.core.Holder
import net.minecraft.core.registries.Registries
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.CreativeModeTab
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

    override fun addItems(baseItem: HTItemHolderLike, parameters: CreativeModeTab.ItemDisplayParameters, consumer: Consumer<ItemStack>) {
        for (holder: Holder<Potion> in parameters.holders().lookupOrThrow(Registries.POTION).listElements()) {
            if (holder.value().isEnabled(parameters.enabledFeatures())) {
                consumer.accept(HTPotionHelper.createPotion(baseItem, holder))
            }
        }
    }

    override fun shouldAddDefault(): Boolean = false
}
