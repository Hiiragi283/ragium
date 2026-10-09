package hiiragi283.ragium.common.block.storage

import hiiragi283.lib.item.HTItemDropHelper
import hiiragi283.lib.item.alchemy.HTBottleType
import hiiragi283.lib.item.alchemy.HTPotionHelper
import hiiragi283.lib.registry.HTDeferredBlockEntityType
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.transfer.HTTransferAccess
import hiiragi283.lib.transfer.fluid.HTFluidTank
import hiiragi283.lib.transfer.fluid.toResourcePair
import hiiragi283.lib.transfer.useTransaction
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.fluids.FluidStackTemplate
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.Transaction

class HTPotionTankBlock(type: HTDeferredBlockEntityType<*>, properties: Properties) :
    HTTankBlock(type, properties) {
    override fun interactTank(
        itemStack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
        tank: HTFluidTank
    ): Boolean = handlePotionBottle(itemStack, player, tank) ||
        super.interactTank(itemStack, state, level, pos, player, hand, hitResult, tank)

    private fun handlePotionBottle(itemStack: ItemStack, player: Player, tank: HTFluidTank): Boolean {
        useTransaction { transaction: Transaction ->
            // ポーション瓶 -> 液体ポーション + 瓶
            val emptyBottle: HTSimpleDeferredItem? = HTBottleType.getFromFilled(itemStack)?.emptyItem
            if (emptyBottle != null) {
                HTPotionHelper.createFluid(
                    itemStack,
                    HTPotionHelper.BOTTLE_AMOUNT
                ).onSuccess { template: FluidStackTemplate ->
                    val (resource: FluidResource, amount: Int) = template.toResourcePair()
                    if (tank.insert(resource, amount, transaction, HTTransferAccess.EXTERNAL) == amount) {
                        val newStack: ItemStack = itemStack.transmuteCopy(emptyBottle, 1)
                        newStack.remove(DataComponents.POTION_CONTENTS)
                        HTItemDropHelper.giveStackTo(player, newStack)
                        itemStack.shrink(1)
                        transaction.commit()
                        return true
                    }
                }
            }
            // 液体ポーション + 瓶 -> ポーション瓶
            val resourceIn: FluidResource = tank.resource
            if (resourceIn.isEmpty) return false
            val filledBottle: HTBottleType = HTBottleType.getFromEmpty(itemStack) ?: return false
            val bottleAmount: Int = HTPotionHelper.BOTTLE_AMOUNT
            if (tank.extract(resourceIn, bottleAmount, transaction, HTTransferAccess.EXTERNAL) == bottleAmount) {
                HTPotionHelper.createFilled(resourceIn, filledBottle).onSuccess {
                    HTItemDropHelper.giveStackTo(player, it)
                }
                itemStack.shrink(1)
                transaction.commit()
                return true
            }
            return false
        }
    }
}
