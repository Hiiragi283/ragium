package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.HTFluidTransformRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.storage.fluid.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTRefineryBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTItemWithFluidRecipeInput, HTFluidTransformRecipe>(
        RagiumRecipeTypes.FLUID_TRANSFORM.get(),
        HTMachineVariant.REFINERY,
        pos,
        state,
    ),
    HTFluidInteractable {
    private lateinit var inputSlot: HTItemSlot
    private lateinit var outputSlot: HTItemSlot

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // input
        inputSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(3.5), HTSlotHelper.getSlotPosY(0))
        // output
        outputSlot = HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(4.5), HTSlotHelper.getSlotPosY(2))
        return HTSimpleItemSlotHolder(this, listOf(inputSlot), listOf(outputSlot))
    }

    private lateinit var inputTank: HTVariableFluidStackTank
    private lateinit var outputTank: HTVariableFluidStackTank

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        inputTank = HTVariableFluidStackTank.input(listener, RagiumConfig.CONFIG.refineryInputTankCapacity)
        outputTank = HTVariableFluidStackTank.output(listener, RagiumConfig.CONFIG.refineryOutputTankCapacity)
        return HTSimpleFluidTankHolder(this, inputTank, outputTank, null)
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.REFINERY.openMenu(player, title, this, ::writeExtraContainerData)

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTItemWithFluidRecipeInput =
        HTItemWithFluidRecipeInput(inputSlot, inputTank)

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(level: ServerLevel, input: HTItemWithFluidRecipeInput, recipe: HTFluidTransformRecipe): Boolean {
        val registries: HolderLookup.Provider = level.registryAccess()
        val bool1: Boolean = outputSlot.insertItem(recipe.assemble(input, registries), true, HTStorageAccess.INTERNAl).isEmpty
        val bool2: Boolean = outputTank.insert(recipe.assembleFluid(input, registries), true, HTStorageAccess.INTERNAl).isEmpty
        return bool1 && bool2
    }

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTItemWithFluidRecipeInput,
        recipe: HTFluidTransformRecipe,
    ) {
        // 実際にアウトプットに搬出する
        val registries: HolderLookup.Provider = level.registryAccess()
        outputSlot.insertItem(recipe.assemble(input, registries), false, HTStorageAccess.INTERNAl)
        outputTank.insert(recipe.assembleFluid(input, registries), false, HTStorageAccess.INTERNAl)
        // インプットを減らす
        inputSlot.shrinkStack(recipe.itemIngredient, false)
        inputTank.shrinkStack(recipe.fluidIngredient, false)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1f, 0.5f)
    }

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult {
        // 初めにアウトプットからの取り出しを試みる
        val result: ItemInteractionResult = interactWith(player, hand, outputTank)
        if (result.consumesAction()) return result
        // 次にインプットとのやり取りを試みる
        return interactWith(player, hand, inputTank)
    }
}
