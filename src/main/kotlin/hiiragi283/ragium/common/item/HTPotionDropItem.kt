package hiiragi283.ragium.common.item

import hiiragi283.core.api.item.HTSubCreativeTabContents
import hiiragi283.core.api.item.alchemy.HTPotionHelper
import hiiragi283.core.api.registry.HTItemHolderLike
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import kotlin.jvm.optionals.getOrNull

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

    override fun getCreatorModId(itemStack: ItemStack): String? = itemStack
        .getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
        .potion()
        .flatMap(Holder<Potion>::unwrapKey)
        .map(ResourceKey<Potion>::location)
        .map(ResourceLocation::getNamespace)
        .getOrNull()
        ?: super.getCreatorModId(itemStack)

    //    HTSubCreativeTabContents    //

    override fun addItems(baseItem: HTItemHolderLike<*>, context: HTSubCreativeTabContents.Context) {
        for (holder: Holder<Potion> in context.provider.lookupOrThrow(Registries.POTION).listElements()) {
            if (holder.value().isEnabled(context.enabledFeatures)) {
                HTPotionHelper.createPotion(baseItem, holder).let(context)
            }
        }
    }

    override fun shouldAddDefault(): Boolean = false
}
