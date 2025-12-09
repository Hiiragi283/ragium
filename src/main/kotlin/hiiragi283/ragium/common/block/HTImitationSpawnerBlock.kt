package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.level.block.Block

class HTImitationSpawnerBlock(properties: Properties) :
    Block(properties),
    HTBlockWithEntity {
    companion object {
        @JvmStatic
        fun filterEntityType(entityType: EntityType<*>): Boolean = SpawnEggItem.byId(entityType) != null
    }

    override fun getBlockEntityType(): HTDeferredBlockEntityType<*> = RagiumBlockEntityTypes.IMITATION_SPAWNER
}
