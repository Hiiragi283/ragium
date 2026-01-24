package hiiragi283.ragium.common.block.entity

import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.read
import hiiragi283.core.api.serialization.value.write
import hiiragi283.core.common.block.entity.HTExtendedBlockEntity
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Spawner
import net.minecraft.world.level.block.state.BlockState

class HTImitationSpawnerBlockEntity(pos: BlockPos, state: BlockState) :
    HTExtendedBlockEntity(RagiumBlockEntityTypes.IMITATION_SPAWNER, pos, state),
    Spawner {
    var spawnerMob: HTSpawnerMob? = null

    //    Save & Load    //

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.write("spawner", HTSpawnerMob.CODEC, spawnerMob)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        input.read("spawner", HTSpawnerMob.CODEC).let(::spawnerMob::set)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        spawnerMob = componentInput.get(RagiumDataComponents.SPAWNER_MOB)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumDataComponents.SPAWNER_MOB, spawnerMob)
    }

    //    Spawner    //

    override fun setEntityId(entityType: EntityType<*>, random: RandomSource) {
        spawnerMob = HTSpawnerMob(entityType)
        setChanged()
    }

    override fun initReducedUpdateTag(output: HTValueOutput) {
        super.initReducedUpdateTag(output)
        output.write("spawner", HTSpawnerMob.CODEC, spawnerMob)
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        input.read("spawner", HTSpawnerMob.CODEC).let(::spawnerMob::set)
    }
}
