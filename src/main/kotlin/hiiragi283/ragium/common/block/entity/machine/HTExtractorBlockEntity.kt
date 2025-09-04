package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToItemRecipe
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

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemToItemBlockEntity(
        RagiumRecipeTypes.EXTRACTING.get(),
        HTMachineVariant.EXTRACTOR,
        pos,
        state,
    ) {
    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.EXTRACTOR.openMenu(player, title, this, ::writeExtraContainerData)

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToItemRecipe,
    ) {
        super.completeRecipe(level, pos, state, input, recipe)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.SPONGE_ABSORB, SoundSource.BLOCKS, 1f, 0.5f)
    }
}
