package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumMachineTypes

class HTMobExtractorMachineEntity(tier: HTMachineTier) : HTLargeProcessorMachineEntity(RagiumMachineTypes.MOB_EXTRACTOR, tier) {
    /*override fun getCustomData(
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
    }*/
}
