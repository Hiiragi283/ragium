package hiiragi283.ragium.common.block

import hiiragi283.core.api.item.createItemStack
import hiiragi283.core.common.block.HTBasicEntityBlock
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.Holder
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem

class HTImitationSpawnerBlock(properties: Properties) : HTBasicEntityBlock(RagiumBlockEntityTypes.IMITATION_SPAWNER, properties) {
    companion object {
        @JvmStatic
        fun filterEntityType(entityType: EntityType<*>): Boolean = SpawnEggItem.byId(entityType) != null

        @JvmStatic
        fun createStack(entityType: EntityType<*>): ItemStack = createItemStack(RagiumBlocks.IMITATION_SPAWNER, RagiumDataComponents.SPAWNER_MOB, HTSpawnerMob.of(entityType))

        @JvmStatic
        fun createStack(holder: Holder<EntityType<*>>): ItemStack = createItemStack(RagiumBlocks.IMITATION_SPAWNER, RagiumDataComponents.SPAWNER_MOB, HTSpawnerMob.of(holder))
    }
}
