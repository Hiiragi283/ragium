package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumRecipes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) :
    HTSimpleItemProcessBlockEntity(RagiumRecipes.EXTRACTING, RagiumBlockEntityTypes.EXTRACTOR, pos, state)
