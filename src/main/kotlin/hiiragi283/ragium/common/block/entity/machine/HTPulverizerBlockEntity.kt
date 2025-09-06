package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.block.state.BlockState

class HTPulverizerBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity.ToItem<HTItemToChancedItemRecipe>(
        RagiumRecipeTypes.CRUSHING.get(),
        HTMachineVariant.PULVERIZER,
        pos,
        state,
    ) {
    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.PULVERIZER.openMenu(player, title, this, ::writeExtraContainerData)

    //    Ticking    //

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToChancedItemRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // インプットを減らす
        inputSlot.shrinkStack(recipe.getIngredientCount(input), false)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.25f, 1f)
    }
}
