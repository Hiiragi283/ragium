package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.block.entity.HTMultiItemMachineBlockEntity
import hiiragi283.ragium.api.energy.HTMachineEnergyData
import hiiragi283.ragium.api.multiblock.HTMultiblockMap
import hiiragi283.ragium.api.recipe.base.HTMachineRecipeInput
import hiiragi283.ragium.api.recipe.base.HTMultiItemRecipe
import hiiragi283.ragium.api.recipe.base.HTRecipeGetter
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumMultiblockMaps
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockState

class HTBlastFurnaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTMultiItemMachineBlockEntity(RagiumBlockEntityTypes.BLAST_FURNACE, pos, state, RagiumMachineKeys.BLAST_FURNACE) {
    override fun getRequiredEnergy(level: ServerLevel, pos: BlockPos): HTMachineEnergyData = HTMachineEnergyData.Consume.DEFAULT

    override val recipeGetter: HTRecipeGetter<HTMachineRecipeInput, out HTMultiItemRecipe> =
        HTRecipeGetter.Cached(RagiumRecipeTypes.BLAST_FURNACE.get())

    override fun getMultiblockMap(): HTMultiblockMap.Relative = RagiumMultiblockMaps.BLAST_FURNACE
}
