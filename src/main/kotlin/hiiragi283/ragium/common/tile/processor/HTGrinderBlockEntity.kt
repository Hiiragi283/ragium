package hiiragi283.ragium.common.tile.processor

import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.level.block.state.BlockState

class HTGrinderBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemMachineBlockEntity(
        RagiumBlockEntityTypes.GRINDER,
        pos,
        state,
        HTRecipeTypes.GRINDER,
    ) {
    override fun checkCondition(level: ServerLevel, pos: BlockPos, simulate: Boolean): Result<Unit> =
        checkEnergyConsume(level, 160, simulate)

    override fun onSucceeded() {
        super.onSucceeded()
        playSound(SoundEvents.GRINDSTONE_USE)
    }
}
