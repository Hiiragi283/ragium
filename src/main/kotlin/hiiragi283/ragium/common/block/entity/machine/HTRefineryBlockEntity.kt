package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.storage.item.HTSlotProvider
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTRefineryBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTItemWithFluidRecipeInput, HTRefiningRecipe>(
        RagiumRecipeTypes.REFINING.get(),
        HTMachineVariant.REFINERY,
        pos,
        state,
    ),
    HTFluidInteractable,
    HTSlotProvider.Empty {
    override val inventory: HTItemHandler = HTItemStackHandler.Builder(1).build(this)
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

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.REFINERY.openMenu(player, title, this, ::writeExtraContainerData)

    override fun createSound(random: RandomSource, pos: BlockPos): SoundInstance = createSound(SoundEvents.LAVA_AMBIENT, random, pos)

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTItemWithFluidRecipeInput =
        HTItemWithFluidRecipeInput(inventory.getStackInSlot(0), tankIn.fluid)

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(level: ServerLevel, input: HTItemWithFluidRecipeInput, recipe: HTRefiningRecipe): Boolean =
        tankOut.canFill(recipe.assembleFluid(input, level.registryAccess()), true)

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTItemWithFluidRecipeInput,
        recipe: HTRefiningRecipe,
    ) {
        // 実際にアウトプットに搬出する
        tankOut.fill(recipe.assembleFluid(input, level.registryAccess()), false)
        // インプットを減らす
        tankIn.drain(recipe.ingredient, false)
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
}
