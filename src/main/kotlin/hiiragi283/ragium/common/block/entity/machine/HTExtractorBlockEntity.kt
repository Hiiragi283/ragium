package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTSimpleItemProcessBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) :
    HTSimpleItemProcessBlockEntity(RagiumRecipeTypes.EXTRACTING.get(), RagiumBlockEntityTypes.EXTRACTOR, pos, state)
