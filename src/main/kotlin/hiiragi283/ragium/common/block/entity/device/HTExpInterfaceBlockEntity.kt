package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.serialization.value.HTValueSerializable
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.ImmutableFluidStack
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.common.util.HTExperienceHelper
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.UUID

class HTExpInterfaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(TODO(), pos, state),
    HTFluidInteractable {
    private lateinit var tank: ExpTank

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        tank = ExpTank()
        return HTSimpleFluidTankHolder.generic(null, tank)
    }

    override fun setPlacedBy(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
        super.setPlacedBy(level, pos, state, placer, stack)
        if (::tank.isInitialized && placer is Player) {
            tank.player = placer
        }
    }

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.store(RagiumConst.OWNER, VanillaBiCodecs.UUID, tank.player?.uuid)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        val uuid: UUID = input.read(RagiumConst.OWNER, VanillaBiCodecs.UUID) ?: return
        tank.player = RagiumPlatform.INSTANCE
            .getCurrentServer()
            ?.playerList
            ?.getPlayer(uuid)
    }

    override fun onUpdateServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = when {
        level.isClientSide -> ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        else -> interactWith(player, hand, tank)
    }

    //    ExpTank    //

    private class ExpTank :
        HTFluidTank.Mutable(),
        HTValueSerializable.Empty {
        private val multiplier: Int get() = RagiumConfig.COMMON.expCollectorMultiplier.asInt
        var player: Player? = null

        override fun getStack(): ImmutableFluidStack {
            val amount: Int = player?.let(HTExperienceHelper::getPlayerExp) ?: return ImmutableFluidStack.EMPTY
            return RagiumFluidContents.EXPERIENCE.toStorageStack(amount * multiplier)
        }

        override fun getCapacityAsLong(stack: ImmutableFluidStack): Long = when (isValid(stack)) {
            true -> Long.MAX_VALUE
            false -> 0
        }

        override fun isValid(stack: ImmutableFluidStack): Boolean = RagiumFluidContents.EXPERIENCE.isOf(stack)

        override fun onContentsChanged() {}

        override fun setStack(stack: ImmutableFluidStack) {
            if (isValid(stack) && player != null) {
                HTExperienceHelper.setPlayerExp(player!!, stack.amountAsInt() / multiplier)
            }
        }
    }
}
