package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
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

    override fun createSound(random: RandomSource, pos: BlockPos): SoundInstance = createSound(SoundEvents.HONEY_BLOCK_SLIDE, random, pos)
}
