package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.util.HTContentListener
import hiiragi283.ragium.api.util.access.HTAccessConfig
import hiiragi283.ragium.common.storage.holder.HTBasicFluidTankHolder
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTExpInterfaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity(TODO() as Holder<Block>, pos, state),
    HTFluidInteractable {
    private lateinit var tank: ExpTank

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        val builder: HTBasicFluidTankHolder.Builder = HTBasicFluidTankHolder.builder(this)
        tank = builder.addSlot(HTAccessConfig.BOTH, ExpTank())
        return builder.build()
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = when {
        level.isClientSide -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        else -> interactWith(player, hand, tank)
    }

    //    ExpTank    //

    private inner class ExpTank :
        HTFluidTank.Basic(),
        HTContentListener.Empty,
        HTValueSerializable.Empty {
        private val multiplier: Int get() = RagiumConfig.COMMON.expCollectorMultiplier.asInt
        val player: Player? get() = RagiumPlatform.INSTANCE.getPlayer(this@HTExpInterfaceBlockEntity.getOwner())

        override fun getStack(): ImmutableFluidStack? {
            val amount: Int = player?.let(HTExperienceHelper::getPlayerExp) ?: return null
            return RagiumFluidContents.EXPERIENCE.toStorageStack(amount * multiplier)
        }

        override fun getCapacity(stack: ImmutableFluidStack?): Int = when {
            stack == null || !isValid(stack) -> 0
            else -> Int.MAX_VALUE
        }

        override fun isValid(stack: ImmutableFluidStack): Boolean = RagiumFluidContents.EXPERIENCE.isOf(stack)

        override fun setStack(stack: ImmutableFluidStack?) {
            if (stack != null && isValid(stack) && player != null) {
                HTExperienceHelper.setPlayerExp(player!!, stack.amountAsInt() / multiplier)
            }
        }
    }
}
