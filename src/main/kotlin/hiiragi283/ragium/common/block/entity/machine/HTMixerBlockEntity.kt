package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToFluidRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.inventory.HTMixerMenu
import hiiragi283.ragium.common.network.HTFluidSlotUpdatePacket
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.util.variant.HTMachineVariant
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

class HTMixerBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTItemWithFluidRecipeInput, HTItemWithFluidToFluidRecipe>(
        RagiumRecipeTypes.MIXING.get(),
        HTMachineVariant.MIXER,
        pos,
        state,
    ),
    HTFluidInteractable {
    override val inventory: HTItemHandler = HTItemStackHandler.Builder(1).addInput(0).build(this)
    private val tankIn = HTFluidStackTank(variant.tankCapacity, this)
    private val tankOut = HTFluidStackTank(variant.tankCapacity, this)

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

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTItemWithFluidRecipeInput =
        HTItemWithFluidRecipeInput(inventory.getStackInSlot(0), tankIn.fluid)

    override fun canProgressRecipe(level: ServerLevel, input: HTItemWithFluidRecipeInput, recipe: HTItemWithFluidToFluidRecipe): Boolean =
        tankOut.canFill(recipe.assembleFluid(input, level.registryAccess()), true)

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTItemWithFluidRecipeInput,
        recipe: HTItemWithFluidToFluidRecipe,
    ) {
        // 実際にアウトプットに搬出する
        tankOut.fill(recipe.assembleFluid(input, level.registryAccess()), false)
        // インプットを減らす
        inventory.shrinkStack(0, recipe.itemIngredient, false)
        tankIn.drain(recipe.fluidIngredient, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS)
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

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTMixerMenu = HTMixerMenu(
        containerId,
        playerInventory,
        blockPos,
        createDefinition(inventory),
    )
}
