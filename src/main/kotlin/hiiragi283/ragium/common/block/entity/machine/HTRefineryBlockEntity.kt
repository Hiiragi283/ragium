package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.inventory.HTFluidOnlyMenu
import hiiragi283.ragium.common.network.HTFluidSlotUpdatePacket
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
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

class HTRefineryBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTSingleFluidRecipeInput, HTRefiningRecipe>(
        RagiumRecipeTypes.REFINING.get(),
        RagiumBlockEntityTypes.REFINERY,
        pos,
        state,
    ),
    HTFluidInteractable {
    override val inventory: HTItemHandler = HTItemStackHandler.EMPTY
    private val tankIn = HTFluidStackTank(RagiumAPI.getConfig().getDefaultTankCapacity(), this::setChanged)
    private val tankOut = HTFluidStackTank(RagiumAPI.getConfig().getDefaultTankCapacity(), this::setChanged)
    override val energyUsage: Int get() = RagiumAPI.getConfig().getAdvancedMachineEnergyUsage()

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConst.TANK_IN, tankIn)
        writer.write(RagiumConst.TANK_OUT, tankOut)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConst.TANK_IN, tankIn)
        reader.read(RagiumConst.TANK_OUT, tankOut)
    }

    override fun sendUpdatePacket(serverLevel: ServerLevel, consumer: (CustomPacketPayload) -> Unit) {
        super.sendUpdatePacket(serverLevel, consumer)
        consumer(HTFluidSlotUpdatePacket(blockPos, 0, tankIn.fluid))
        consumer(HTFluidSlotUpdatePacket(blockPos, 1, tankOut.fluid))
    }

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTSingleFluidRecipeInput = HTSingleFluidRecipeInput(tankIn.fluid)

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(level: ServerLevel, input: HTSingleFluidRecipeInput, recipe: HTRefiningRecipe): Boolean =
        tankOut.canFill(recipe.assembleFluid(input, level.registryAccess()), true)

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTSingleFluidRecipeInput,
        recipe: HTRefiningRecipe,
    ) {
        // 実際にアウトプットに搬出する
        tankOut.fill(recipe.assembleFluid(input, level.registryAccess()), false)
        // インプットを減らす
        tankIn.drain(recipe.ingredient, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.LAVA_POP, SoundSource.BLOCKS)
    }

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler = HTFilteredFluidHandler(tankIn, tankOut)

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult {
        // 初めにアウトプットからの取り出しを試みる
        val result: ItemInteractionResult = interactWith(player, hand, HTFilteredFluidHandler(tankOut, HTFluidFilter.DRAIN_ONLY))
        if (result.consumesAction()) return result
        // 次にインプットとのやり取りを試みる
        return interactWith(player, hand, tankIn)
    }

    //    Menu    //

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTFluidOnlyMenu = HTFluidOnlyMenu(
        RagiumMenuTypes.REFINERY,
        containerId,
        playerInventory,
        blockPos,
        createDefinition(),
    )
}
