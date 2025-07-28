package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.HTUniversalRecipeInput
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.inventory.HTSolidifierMenu
import hiiragi283.ragium.common.network.HTFluidSlotUpdatePacket
import hiiragi283.ragium.common.recipe.HTSolidifyingRecipe
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
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

class HTSolidifierBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTUniversalRecipeInput, HTSolidifyingRecipe>(
        RagiumRecipeTypes.SOLIDIFYING.get(),
        RagiumBlockEntityTypes.SOLIDIFIER,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler = HTItemStackHandler(2, this::setChanged)
    private val tank = HTFluidTank(RagiumConfig.COMMON.machineTankCapacity.get(), this::setChanged)
    override val energyUsage: Int get() = RagiumConfig.COMMON.advancedMachineEnergyUsage.get()

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        super.writeNbt(writer)
        writer.write(RagiumConst.TANK, tank)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        super.readNbt(reader)
        reader.read(RagiumConst.TANK, tank)
    }

    override fun sendUpdatePacket(serverLevel: ServerLevel, consumer: (CustomPacketPayload) -> Unit) {
        super.sendUpdatePacket(serverLevel, consumer)
        consumer(HTFluidSlotUpdatePacket(0, tank.fluid))
    }

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTUniversalRecipeInput =
        HTUniversalRecipeInput(listOf(inventory.getStackInSlot(0)), listOf(tank.fluid))

    override fun canProgressRecipe(level: ServerLevel, input: HTUniversalRecipeInput, recipe: HTSolidifyingRecipe): Boolean {
        // アウトプットに搬出できるか判定する
        if (!insertToOutput(1..1, recipe.output.get(), true).isEmpty) {
            return false
        }
        return true
    }

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTUniversalRecipeInput,
        recipe: HTSolidifyingRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(1..1, recipe.output.getChancedStack(level.random), false)
        // インプットを減らす
        tank.drain(recipe.ingredient.amount(), IFluidHandler.FluidAction.EXECUTE)
        inventory.consumeStackInSlot(0, recipe.catalyst.map(SizedIngredient::count).orElse(0), true)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS)
    }

    override fun getItemHandler(direction: Direction?): IItemHandler? = HTFilteredItemHandler(
        inventory,
        object : HTItemFilter {
            override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = slot == 0

            override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = slot == 1
        },
    )

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler =
        HTFilteredFluidHandler(listOf(tank), HTFluidFilter.FILL_ONLY)

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTSolidifierMenu = HTSolidifierMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
