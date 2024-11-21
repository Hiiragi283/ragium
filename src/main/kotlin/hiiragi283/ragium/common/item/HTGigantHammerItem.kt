package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.createToolAttribute
import hiiragi283.ragium.api.extension.getStackInActiveHand
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.block.BlockState
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterials
import net.minecraft.util.Rarity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import team.reborn.energy.api.base.SimpleEnergyItem

object HTGigantHammerItem :
    Item(
        itemSettings()
            .component(EnergyStorage.ENERGY_COMPONENT, HTMachineTier.PRIMITIVE.recipeCost * 1024)
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
    ),
    SimpleEnergyItem {
    private fun canUse(stack: ItemStack): Boolean = getStoredEnergy(stack) >= HTMachineTier.PRIMITIVE.recipeCost

    override fun getMiningSpeed(stack: ItemStack, state: BlockState): Float = if (canUse(stack)) 20f else 1f

    override fun isCorrectForDrops(stack: ItemStack, state: BlockState): Boolean = true

    override fun canMine(
        state: BlockState,
        world: World,
        pos: BlockPos,
        miner: PlayerEntity,
    ): Boolean = miner.isCreative || canUse(miner.getStackInActiveHand())

    override fun postMine(
        stack: ItemStack,
        world: World,
        state: BlockState,
        pos: BlockPos,
        miner: LivingEntity,
    ): Boolean = canUse(stack) && tryUseEnergy(stack, HTMachineTier.PRIMITIVE.recipeCost)

    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean = true

    override fun postDamageEntity(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        tryUseEnergy(stack, HTMachineTier.PRIMITIVE.recipeCost)
    }

    //    SimpleEnergyItem    //

    override fun getEnergyCapacity(stack: ItemStack): Long = HTMachineTier.PRIMITIVE.recipeCost * 1024

    override fun getEnergyMaxInput(stack: ItemStack): Long = Long.MAX_VALUE

    override fun getEnergyMaxOutput(stack: ItemStack): Long = Long.MAX_VALUE
}
