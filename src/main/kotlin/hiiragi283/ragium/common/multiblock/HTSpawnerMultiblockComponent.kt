package hiiragi283.ragium.common.multiblock

import hiiragi283.ragium.api.multiblock.HTControllerDefinition
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.item.component.HTSpawnerContent
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.BaseSpawner
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.SpawnerBlockEntity
import net.minecraft.world.level.block.state.BlockState

object HTSpawnerMultiblockComponent : HTMultiblockComponent {
    override fun getBlockName(definition: HTControllerDefinition): Component = Blocks.SPAWNER.name

    override fun checkState(definition: HTControllerDefinition, pos: BlockPos): Boolean = definition.getBlockState(pos).`is`(Blocks.SPAWNER)

    override fun getPlacementState(definition: HTControllerDefinition): BlockState? = Blocks.SPAWNER.defaultBlockState()

    override fun collectData(definition: HTControllerDefinition, pos: BlockPos, builder: DataComponentMap.Builder) {
        val spawnerEntity: SpawnerBlockEntity = definition.getBlockEntity(pos) as? SpawnerBlockEntity ?: return
        val spawner: BaseSpawner = spawnerEntity.spawner
        val entity: Entity = spawner.getOrCreateDisplayEntity(definition.level, pos) ?: return
        builder.set(RagiumComponentTypes.SPAWNER_CONTENT, HTSpawnerContent(entity.type))
    }
}
