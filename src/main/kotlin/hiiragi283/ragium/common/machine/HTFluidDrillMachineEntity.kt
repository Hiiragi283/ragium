package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.machine.HTRecipeComponentTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.minecraft.block.BlockState
import net.minecraft.component.ComponentMap
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTFluidDrillMachineEntity(tier: HTMachineTier) : HTProcessorMachineEntity(RagiumMachineTypes.FLUID_DRILL, tier) {
    override fun getCustomData(
        world: World,
        pos: BlockPos,
        state: BlockState,
        builder: ComponentMap.Builder,
    ) {
        builder.add(HTRecipeComponentTypes.BIOME, world.getBiome(pos).key.getOrNull())
    }
}
