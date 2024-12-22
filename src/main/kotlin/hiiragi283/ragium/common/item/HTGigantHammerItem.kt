package hiiragi283.ragium.common.item

import net.minecraft.block.BlockState
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTGigantHammerItem(settings: Settings) : Item(settings) {
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
