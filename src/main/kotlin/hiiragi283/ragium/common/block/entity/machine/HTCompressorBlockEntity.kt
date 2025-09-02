package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState

class HTCompressorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemToItemBlockEntity(
        RagiumRecipeTypes.COMPRESSING.get(),
        HTMachineVariant.COMPRESSOR,
        pos,
        state,
    ) {
    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.COMPRESSOR.openMenu(player, title, this, ::writeExtraContainerData)

    override fun createSound(random: RandomSource, pos: BlockPos): SoundInstance = createSound(SoundEvents.ANVIL_PLACE, random, pos, 0.5f)
}
