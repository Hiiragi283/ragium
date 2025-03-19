package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.recipe.RagiumRecipes
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class HTExtractorBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemRecipeBlockEntity(RagiumRecipes.EXTRACTING, RagiumBlockEntityTypes.EXTRACTOR, pos, state)
