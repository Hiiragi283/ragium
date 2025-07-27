package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.inventory.HTMelterMenu
import hiiragi283.ragium.common.network.HTFluidSlotUpdatePacket
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTUniversalRecipeInput, HTMeltingRecipe>(
        RagiumRecipeTypes.MELTING.get(),
        RagiumBlockEntityTypes.MELTER,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler = HTItemStackHandler(1, this::setChanged)
    private val tank = HTFluidTank(RagiumConfig.COMMON.machineTankCapacity.get(), this::setChanged)
    override val energyUsage: Int get() = RagiumConfig.COMMON.advancedMachineEnergyUsage.get()

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        super.writeNbt(writer)
        writer.write(RagiumConstantValues.TANK, tank)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        super.readNbt(reader)
        reader.read(RagiumConstantValues.TANK, tank)
    }

    override fun sendUpdatePacket(serverLevel: ServerLevel, consumer: (CustomPacketPayload) -> Unit) {
        super.sendUpdatePacket(serverLevel, consumer)
        consumer(HTFluidSlotUpdatePacket(0, tank.fluid))
    }

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTUniversalRecipeInput =
        HTUniversalRecipeInput.fromItems(inventory.getStackInSlot(0))

    override fun canProgressRecipe(level: ServerLevel, input: HTUniversalRecipeInput, recipe: HTMeltingRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        if (!tank.canFill(recipe.output.get(), true)) {
            return false
        }
        return true
    }

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        recipe: HTMeltingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        tank.fill(recipe.output.get(), IFluidHandler.FluidAction.EXECUTE)
        // インプットを減らす
        inventory.consumeStackInSlot(0, recipe.ingredient.count(), false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.BUCKET_FILL_LAVA, SoundSource.BLOCKS)
    }

    override fun getItemHandler(direction: Direction?): HTFilteredItemHandler = HTFilteredItemHandler(inventory, HTItemFilter.INSERT_ONLY)

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler =
        HTFilteredFluidHandler(listOf(tank), HTFluidFilter.DRAIN_ONLY)

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTMelterMenu = HTMelterMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
