package hiiragi283.ragium.common.block

import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.common.block.HTBlockWithEntity
import hiiragi283.core.common.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.Holder
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.level.block.Block

class HTImitationSpawnerBlock(properties: Properties) :
    Block(properties),
    HTBlockWithEntity {
    companion object {
        @JvmStatic
        fun filterEntityType(entityType: EntityType<*>): Boolean = SpawnEggItem.byId(entityType) != null

        @JvmStatic
        fun createStack(entityType: EntityType<*>): ItemStack =
            createItemStack(RagiumBlocks.IMITATION_SPAWNER, RagiumDataComponents.SPAWNER_MOB, HTSpawnerMob(entityType))

        @JvmStatic
        fun createStack(holder: Holder<EntityType<*>>): ItemStack =
            createItemStack(RagiumBlocks.IMITATION_SPAWNER, RagiumDataComponents.SPAWNER_MOB, HTSpawnerMob(holder))
    }

    override fun getBlockEntityType(): HTDeferredBlockEntityType<*> = RagiumBlockEntityTypes.IMITATION_SPAWNER
}
