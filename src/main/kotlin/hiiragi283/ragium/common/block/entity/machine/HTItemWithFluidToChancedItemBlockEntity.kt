package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTBlockEntityFactory
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.registry.impl.HTDeferredRecipeType
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.util.HTStackSlotHelper
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.config.RagiumConfig
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.state.BlockState

class HTItemWithFluidToChancedItemBlockEntity(
    private val sound: SoundEvent,
    recipeType: RecipeType<HTItemWithFluidToChancedItemRecipe>,
    variant: HTMachineVariant,
    pos: BlockPos,
    state: BlockState,
) : HTChancedItemOutputBlockEntity<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe>(
        recipeType,
        variant,
        pos,
        state,
    ) {
    companion object {
        @JvmStatic
        fun create(
            sound: SoundEvent,
            recipeType: HTDeferredRecipeType<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe>,
            variant: HTMachineVariant,
        ): HTBlockEntityFactory<HTItemWithFluidToChancedItemBlockEntity> = HTBlockEntityFactory { pos: BlockPos, state: BlockState ->
            HTItemWithFluidToChancedItemBlockEntity(sound, recipeType.get(), variant, pos, state)
        }
    }

    override fun createTank(listener: HTContentListener): HTFluidTank =
        HTVariableFluidStackTank.input(listener, RagiumConfig.COMMON.washerTankCapacity)

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTItemWithFluidRecipeInput =
        HTItemWithFluidRecipeInput(inputSlot, inputTank)

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTItemWithFluidRecipeInput,
        recipe: HTItemWithFluidToChancedItemRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // インプットを減らす
        HTStackSlotHelper.shrinkStack(inputSlot, recipe::getRequiredCount, HTStorageAction.EXECUTE)
        HTStackSlotHelper.shrinkStack(inputTank, recipe::getRequiredAmount, HTStorageAction.EXECUTE)
        // SEを鳴らす
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1f, 0.25f)
    }
}
