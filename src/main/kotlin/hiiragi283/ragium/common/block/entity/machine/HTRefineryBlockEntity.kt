package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.inventory.HTRefineryMenu
import hiiragi283.ragium.common.network.HTFluidSlotUpdatePacket
import hiiragi283.ragium.common.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTRefineryBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTSingleFluidRecipeInput, HTRefiningRecipe>(
        RagiumRecipeTypes.REFINING.get(),
        RagiumBlockEntityTypes.REFINERY,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler = HTItemStackHandler(0, this::setChanged)
    private val tankIn = HTFluidTank(RagiumConfig.COMMON.machineTankCapacity.get(), this::setChanged)
    private val tankOut = HTFluidTank(RagiumConfig.COMMON.machineTankCapacity.get(), this::setChanged)
    override val energyUsage: Int get() = RagiumConfig.COMMON.advancedMachineEnergyUsage.get()

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write("${RagiumConst.TANK}_in", tankIn)
        writer.write("${RagiumConst.TANK}_out", tankOut)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read("${RagiumConst.TANK}_in", tankIn)
        reader.read("${RagiumConst.TANK}_out", tankOut)
    }

    override fun sendUpdatePacket(serverLevel: ServerLevel, consumer: (CustomPacketPayload) -> Unit) {
        super.sendUpdatePacket(serverLevel, consumer)
        consumer(HTFluidSlotUpdatePacket(0, tankIn.fluid))
        consumer(HTFluidSlotUpdatePacket(1, tankOut.fluid))
    }

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTSingleFluidRecipeInput = HTSingleFluidRecipeInput(tankIn.fluid)

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(level: ServerLevel, input: HTSingleFluidRecipeInput, recipe: HTRefiningRecipe): Boolean =
        tankOut.canFill(recipe.fluidResults[0].get(), true)

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTSingleFluidRecipeInput,
        recipe: HTRefiningRecipe,
    ) {
        // 実際にアウトプットに搬出する
        val firstOutput: FluidStack = recipe.fluidResults[0].get()
        tankOut.fill(firstOutput, IFluidHandler.FluidAction.EXECUTE)
        // インプットを減らす
        tankIn.drain(recipe.ingredient, IFluidHandler.FluidAction.EXECUTE)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.LAVA_POP, SoundSource.BLOCKS)
    }

    override fun getFluidHandler(direction: Direction?): IFluidHandler? = HTFilteredFluidHandler(
        listOf(tankIn, tankOut),
        object : HTFluidFilter {
            override fun canFill(tank: IFluidTank, stack: FluidStack): Boolean = tank == tankIn

            override fun canDrain(tank: IFluidTank, stack: FluidStack): Boolean = tank == tankOut

            override fun canDrain(tank: IFluidTank, maxDrain: Int): Boolean = tank == tankOut
        },
    )

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTRefineryMenu = HTRefineryMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
