package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemWithFluidToChancedItemRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.common.storage.fluid.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
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

class HTWasherBlockEntity(pos: BlockPos, state: BlockState) :
    HTChancedItemOutputBlockEntity<HTItemWithFluidRecipeInput, HTItemWithFluidToChancedItemRecipe>(
        RagiumRecipeTypes.WASHING.get(),
        HTMachineVariant.WASHER,
        pos,
        state,
    ),
    HTFluidInteractable {
    private lateinit var inputTank: HTFluidTank

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder? {
        // input
        inputTank = HTVariableFluidStackTank.input(listener, RagiumConfig.COMMON.washerInputTankCapacity)
        return HTSimpleFluidTankHolder.input(this, inputTank)
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.WASHER.openMenu(player, title, this, ::writeExtraContainerData)

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTItemWithFluidRecipeInput =
        HTItemWithFluidRecipeInput(inputSlot.getStack(), inputTank.getStack())

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTItemWithFluidRecipeInput,
        recipe: HTItemWithFluidToChancedItemRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // インプットを減らす
        inputSlot.shrinkStack(recipe.getIngredientCount(input), false)
        inputTank.shrinkStack(recipe.getIngredientAmount(input), false)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 1f, 0.25f)
    }

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult =
        interactWith(player, hand, inputTank)
}
