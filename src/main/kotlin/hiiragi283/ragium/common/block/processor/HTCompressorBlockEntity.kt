package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.block.entity.HTSingleItemMachineBlockEntity
import hiiragi283.ragium.api.capability.energy.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTRecipeConverters
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeGetter
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class HTCompressorBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemMachineBlockEntity(RagiumBlockEntityTypes.COMPRESSOR, pos, state, HTMachineType.COMPRESSOR) {
    override val recipeGetter: HTRecipeGetter<HTMachineRecipeInput, out HTSingleItemRecipe> =
        HTRecipeGetter.listed(HTRecipeConverters::compressor)

    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.DEFAULT
}
