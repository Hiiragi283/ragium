package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) :
    HTItemToItemBlockEntity(
        RagiumMenuTypes.EXTRACTOR,
        RagiumRecipeTypes.EXTRACTING.get(),
        RagiumBlockEntityTypes.EXTRACTOR,
        pos,
        state,
    ) {
    override val energyUsage: Int get() = RagiumAPI.getConfig().getBasicMachineEnergyUsage()

    override fun playSound(level: Level, pos: BlockPos) {
        level.playSound(null, pos, SoundEvents.SLIME_BLOCK_BREAK, SoundSource.BLOCKS, 0.5f, 1f)
    }
}
