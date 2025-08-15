package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumToolTiers
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.component.Tool
import net.neoforged.neoforge.common.ItemAbility
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet
import java.util.*

/**
 * @see [mekanism.common.item.gear.ItemAtomicDisassembler]
 */
class HTDestructionHammerItem(properties: Properties) :
    Item(
        properties
            .rarity(Rarity.RARE)
            .durability(RagiumToolTiers.RAGI_ALLOY.uses * 8)
            .stacksTo(1)
            .component(
                DataComponents.TOOL,
                Tool(
                    listOf(
                        Tool.Rule.deniesDrops(RagiumModTags.Blocks.INCORRECT_FOR_DESTRUCTION_TOOL),
                        Tool.Rule(
                            AnyHolderSet(BuiltInRegistries.BLOCK.asLookup()),
                            Optional.of(12f),
                            Optional.of(true),
                        ),
                    ),
                    1f,
                    1,
                ),
            ),
    ) {
    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean = itemAbility.name().endsWith("_dig")
}
