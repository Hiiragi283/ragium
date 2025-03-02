package hiiragi283.ragium.common.block.addon

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.extension.getExpAmountFromLevel
import hiiragi283.ragium.api.extension.getExpLevelFromAmount
import hiiragi283.ragium.api.extension.getTotalExpAmount
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidSlotHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidVariant
import hiiragi283.ragium.common.init.RagiumVirtualFluids
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import kotlin.math.max

class HTExpBankBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(TODO(), pos, state),
    HTHandlerBlockEntity,
    HTFluidSlotHandler {
    private val fluidTank: HTFluidTank = HTFluidTank
        .Builder()
        .setCapacity(Int.MAX_VALUE)
        .setCallback(this::setChanged)
        .setValidator(Tags.Fluids.EXPERIENCE)
        .build("fluid")

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        fluidTank.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        fluidTank.readNbt(nbt, registryOps)
    }

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult {
        if (level.isClientSide) return InteractionResult.SUCCESS
        // Calculate exp fluid amount
        val expLevel: Int = player.experienceLevel
        val playerExpAmount: Int = player.getTotalExpAmount()
        val expAmountMayRemove: Int = playerExpAmount - max(0, expLevel - 1)
        if (expAmountMayRemove < 0) return InteractionResult.FAIL
        val fluidAmount: Int = expAmountMayRemove * 20
        // Try to insert exp fluid
        var expVariant: HTFluidVariant = HTFluidVariant.of(RagiumVirtualFluids.EXPERIENCE.get())
        if (!fluidTank.isEmpty) {
            expVariant = fluidTank.resource
        }
        if (fluidTank.canInsert(expVariant, fluidAmount)) {
            // Convert player exp into fluid
            val inserted: Int = fluidTank.insert(expVariant, fluidAmount, true)
            val expAmountToRemove: Int = inserted / 20
            val newExpAmount: Int = max(0, playerExpAmount - expAmountToRemove)
            player.totalExperience = newExpAmount
            player.experienceLevel = getExpLevelFromAmount(newExpAmount)
            val expAmount1: Int = getExpAmountFromLevel(player.experienceLevel)
            player.experienceProgress = (newExpAmount - expAmount1) / player.xpNeededForNextLevel.toFloat()
            return InteractionResult.CONSUME
        }
        return super.onRightClicked(state, level, pos, player, hitResult)
    }

    //    HTHandlerBlockEntity    //

    override fun getFluidHandler(direction: Direction?): IFluidHandler? = this

    //    HTFluidSlotHandler    //

    override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.GENERIC

    override fun getFluidTank(tank: Int): HTFluidTank? = fluidTank

    override fun getTanks(): Int = 1
}
