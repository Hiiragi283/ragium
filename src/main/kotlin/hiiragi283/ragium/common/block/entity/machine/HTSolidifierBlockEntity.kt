package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTFluidWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.inventory.HTItemWithFluidToItemMenu
import hiiragi283.ragium.common.network.HTFluidSlotUpdatePacket
import hiiragi283.ragium.common.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTSolidifierBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTItemWithFluidRecipeInput, HTFluidWithCatalystToItemRecipe>(
        RagiumRecipeTypes.SOLIDIFYING.get(),
        RagiumBlockEntityTypes.SOLIDIFIER,
        pos,
        state,
    ),
    HTFluidInteractable {
    override val inventory: HTItemHandler = HTItemStackHandler(2, this::setChanged)
    override val itemFilter: HTItemFilter = HTItemFilter.simple(intArrayOf(0), intArrayOf(1))
    private val tank = HTFluidTank(RagiumAPI.getConfig().getDefaultTankCapacity(), this::setChanged)
    override val energyUsage: Int get() = RagiumAPI.getConfig().getAdvancedMachineEnergyUsage()

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
        consumer(HTFluidSlotUpdatePacket(blockPos, 0, tank.fluid))
    }

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTItemWithFluidRecipeInput =
        HTItemWithFluidRecipeInput(inventory.getStackInSlot(0), tank.fluid)

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(
        level: ServerLevel,
        input: HTItemWithFluidRecipeInput,
        recipe: HTFluidWithCatalystToItemRecipe,
    ): Boolean = insertToOutput(recipe.assemble(input, level.registryAccess()), true).isEmpty

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTItemWithFluidRecipeInput,
        recipe: HTFluidWithCatalystToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(recipe.assemble(input, level.registryAccess()), false)
        // インプットを減らす
        tank.drain(recipe.ingredient, IFluidHandler.FluidAction.EXECUTE)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS)
    }

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler =
        HTFilteredFluidHandler(listOf(tank), HTFluidFilter.FILL_ONLY)

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = interactWith(player, hand, tank)

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTItemWithFluidToItemMenu =
        HTItemWithFluidToItemMenu(
            RagiumMenuTypes.SOLIDIFIER,
            containerId,
            playerInventory,
            blockPos,
            createDefinition(inventory),
        )
}
