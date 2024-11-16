package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.createToolAttribute
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.common.init.RagiumToolMaterials
import net.minecraft.block.BlockState
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.ItemStack
import net.minecraft.item.MiningToolItem
import net.minecraft.item.ToolMaterials
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.Rarity

object HTGigantHammerItem : MiningToolItem(
    RagiumToolMaterials.STEEL,
    BlockTags.PICKAXE_MINEABLE,
    itemSettings()
        .rarity(Rarity.EPIC)
        .attributeModifiers(
            createToolAttribute(ToolMaterials.NETHERITE, 15.0, -3.0)
                .add(
                    EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE,
                    EntityAttributeModifier(
                        RagiumAPI.id("gigant_hammer_range"),
                        12.0,
                        EntityAttributeModifier.Operation.ADD_VALUE,
                    ),
                    AttributeModifierSlot.MAINHAND,
                ).add(
                    EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                    EntityAttributeModifier(
                        RagiumAPI.id("gigant_hammer_range"),
                        12.0,
                        EntityAttributeModifier.Operation.ADD_VALUE,
                    ),
                    AttributeModifierSlot.MAINHAND,
                ).build(),
        ),
) {
    override fun getMiningSpeed(stack: ItemStack, state: BlockState): Float = 20.0f

    override fun isCorrectForDrops(stack: ItemStack, state: BlockState): Boolean = true
}
