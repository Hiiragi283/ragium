package hiiragi283.ragium.block.entity.machine

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.config.HTEnergyConfig
import hiiragi283.ragium.block.entity.RagiumBlockEntityTypes
import hiiragi283.ragium.block.entity.machine.base.HTItemAndFluidToItemBlockEntity
import hiiragi283.ragium.recipe.RagiumRecipeLookups
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTFreezerBlockEntity(pos: BlockPos, state: BlockState) : HTItemAndFluidToItemBlockEntity(RagiumBlockEntityTypes.FREEZER.get(), RagiumRecipeLookups.FREEZING, pos, state) {
    override fun getConfig(): HTEnergyConfig = RagiumConfig.SERVER.machine.freezer
}
