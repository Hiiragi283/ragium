package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.item.HTSubCreativeTabContents
import hiiragi283.ragium.api.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.registry.HTItemHolderLike
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.alchemy.Potion
import java.util.function.Consumer

class HTPotionDropItem(properties: Properties) :
    Item(properties),
    HTSubCreativeTabContents {
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        stack
            .get(DataComponents.POTION_CONTENTS)
            ?.addPotionTooltip(tooltips::add, 1f, context.tickRate())
    }

    override fun addItems(baseItem: HTItemHolderLike, parameters: CreativeModeTab.ItemDisplayParameters, consumer: Consumer<ItemStack>) {
        for (holder: Holder<Potion> in parameters.holders().lookupOrThrow(Registries.POTION).listElements()) {
            if (holder.value().isEnabled(parameters.enabledFeatures())) {
                consumer.accept(HTPotionHelper.createPotion(baseItem, holder))
            }
        }
    }

    override fun shouldAddDefault(): Boolean = false
}
