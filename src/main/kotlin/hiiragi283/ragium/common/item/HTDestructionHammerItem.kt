package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.setup.RagiumToolTiers
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TieredItem
import net.neoforged.neoforge.common.ItemAbility

/**
 * @see [mekanism.common.item.gear.ItemAtomicDisassembler]
 */
class HTDestructionHammerItem(properties: Properties) :
    TieredItem(
        RagiumToolTiers.RAGI_CRYSTAL,
        properties
            .rarity(Rarity.RARE)
            .stacksTo(1)
            .component(
                DataComponents.TOOL,
                RagiumToolTiers.RAGI_CRYSTAL.createToolProperties(RagiumModTags.Blocks.INCORRECT_FOR_DESTRUCTION_TOOL),
            ),
    ) {
    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean = itemAbility.name().endsWith("_dig")
}
