package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.createToolAttribute
import hiiragi283.ragium.api.extension.itemSettings
import net.minecraft.block.BlockState
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.UnbreakableComponent
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterials
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object HTGigantHammerItem :
    Item(
        itemSettings()
            .component(DataComponentTypes.UNBREAKABLE, UnbreakableComponent(true))
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
    override fun getMiningSpeed(stack: ItemStack, state: BlockState): Float = 12f

    override fun isCorrectForDrops(stack: ItemStack, state: BlockState): Boolean = true

    override fun postMine(
        stack: ItemStack,
        world: World,
        state: BlockState,
        pos: BlockPos,
        miner: LivingEntity,
    ): Boolean = true

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean = true
}
