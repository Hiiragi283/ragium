package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTSingleItemMachineBlockEntity
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTRecipeGetter
import hiiragi283.ragium.api.recipe.base.HTSingleItemRecipe
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class HTLaserAssemblyBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemMachineBlockEntity(RagiumBlockEntityTypes.GRINDER, pos, state, RagiumMachineKeys.GRINDER) {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.PRECISION

    override val recipeGetter: HTRecipeGetter<HTMachineRecipeInput, out HTSingleItemRecipe> =
        HTRecipeGetter.Cached(RagiumRecipeTypes.LASER_ASSEMBLY.get())
}
