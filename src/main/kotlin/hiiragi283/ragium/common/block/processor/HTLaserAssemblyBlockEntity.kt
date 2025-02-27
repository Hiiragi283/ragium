package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.block.entity.HTSingleItemMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineEnergyData
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTRecipeType
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class HTLaserAssemblyBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemMachineBlockEntity(RagiumBlockEntityTypes.LASER_ASSEMBLY, pos, state, HTMachineType.LASER_ASSEMBLY) {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.PRECISION

    override val recipeType: HTRecipeType<out HTSingleItemRecipe> = HTRecipeTypes.LASER_ASSEMBLY
}
