package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.machine.HTRecipeComponentTypes
import hiiragi283.ragium.common.init.RagiumMachineTypes
import net.minecraft.block.BlockState
import net.minecraft.component.ComponentMap
import net.minecraft.predicate.entity.EntityPredicates
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.world.World

class HTMobExtractorMachineEntity(tier: HTMachineTier) : HTProcessorMachineEntity(RagiumMachineTypes.MOB_EXTRACTOR, tier) {
    override fun getCustomData(
        world: World,
        pos: BlockPos,
        state: BlockState,
        builder: ComponentMap.Builder,
    ) {
        builder.add(
            HTRecipeComponentTypes.ENTITY_TYPE,
            world
                .getOtherEntities(null, Box(pos.up()), EntityPredicates.VALID_LIVING_ENTITY)
                .firstOrNull()
                ?.type,
        )
    }
}
