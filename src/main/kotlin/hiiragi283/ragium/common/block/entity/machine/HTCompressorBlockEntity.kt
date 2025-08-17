package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTCompressorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemToItemBlockEntity(
        RagiumMenuTypes.COMPRESSOR,
        RagiumRecipeTypes.COMPRESSING.get(),
        HTMachineVariant.COMPRESSOR,
        pos,
        state,
    ) {
    override fun playSound(level: Level, pos: BlockPos) {
        level.playSound(null, pos, SoundEvents.ANVIL_FALL, SoundSource.BLOCKS, 0.5f, 1f)
    }
}
