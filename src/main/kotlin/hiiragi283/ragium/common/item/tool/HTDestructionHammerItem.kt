package hiiragi283.ragium.common.item.tool

import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.setup.RagiumEquipmentMaterials
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TieredItem
import net.minecraft.world.item.component.Tool
import net.neoforged.neoforge.common.ItemAbility
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet

/**
 * @see mekanism.common.item.gear.ItemAtomicDisassembler
 */
class HTDestructionHammerItem(properties: Properties) :
    TieredItem(
        RagiumEquipmentMaterials.RAGI_CRYSTAL,
        properties
            .rarity(Rarity.RARE)
            .stacksTo(1)
            .component(
                DataComponents.TOOL,
                Tool(
                    listOf(
                        Tool.Rule.deniesDrops(RagiumModTags.Blocks.INCORRECT_FOR_DESTRUCTION_TOOL),
                        Tool.Rule(
                            AnyHolderSet(BuiltInRegistries.BLOCK.asLookup()),
                            12f.wrapOptional(),
                            true.wrapOptional(),
                        ),
                    ),
                    1f,
                    1,
                ),
            ),
    ) {
    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean = itemAbility.name().endsWith("_dig")
}
