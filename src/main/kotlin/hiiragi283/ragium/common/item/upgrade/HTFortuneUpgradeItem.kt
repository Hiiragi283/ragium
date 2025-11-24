package hiiragi283.ragium.common.item.upgrade

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.item.HTDynamicUpgradeItem
import hiiragi283.ragium.api.item.HTSubCreativeTabContents
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.item.component.toMutable
import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.math.fraction
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.registry.HTItemHolderLike
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import java.util.function.Consumer

class HTFortuneUpgradeItem(properties: Properties) :
    Item(properties),
    HTDynamicUpgradeItem,
    HTSubCreativeTabContents {
    override fun supportsEnchantment(stack: ItemStack, enchantment: Holder<Enchantment?>): Boolean = enchantment.key == Enchantments.FORTUNE

    override fun getUpgrade(stack: ItemStack): HTMachineUpgrade {
        val fortune: Int = RagiumPlatform.INSTANCE
            .getHolder(null, Enchantments.FORTUNE)
            ?.let(stack::getEnchantmentLevel)
            ?.takeIf { it > 0 }
            ?: 1
        return HTMachineUpgrade.create(HTMachineUpgrade.Key.SUBPRODUCT_CHANCE to fraction(1, 3) * fortune)
    }

    override fun addItems(baseItem: HTItemHolderLike, provider: HolderLookup.Provider, consumer: Consumer<ItemStack>) {
        val fortune: Holder<Enchantment> = RagiumPlatform.INSTANCE.getHolder(provider, Enchantments.FORTUNE) ?: return
        for (level: Int in 1..fortune.value().maxLevel) {
            createItemStack(
                baseItem,
                DataComponents.ENCHANTMENTS,
                ItemEnchantments.EMPTY
                    .toMutable()
                    .apply { set(fortune, level) }
                    .toImmutable(),
            ).let(consumer::accept)
        }
    }
}
