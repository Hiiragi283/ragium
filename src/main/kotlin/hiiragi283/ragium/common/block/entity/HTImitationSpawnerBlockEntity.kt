package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.item.component.HTSpawnerMob
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Spawner
import net.minecraft.world.level.block.state.BlockState

class HTImitationSpawnerBlockEntity(pos: BlockPos, state: BlockState) :
    ExtendedBlockEntity(RagiumBlockEntityTypes.IMITATION_SPAWNER, pos, state),
    Spawner {
    var spawnerMob: HTSpawnerMob? = null

    //    Save & Load    //

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)
        RagiumPlatform.INSTANCE
            .createValueInput(registries, tag)
            .readAndSet("spawner", HTSpawnerMob.CODEC, ::spawnerMob::set)
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)
        RagiumPlatform.INSTANCE
            .createValueOutput(registries, tag)
            .store("spawner", HTSpawnerMob.CODEC, spawnerMob)
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
        output.store("spawner", HTSpawnerMob.CODEC, spawnerMob)
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        input.readAndSet("spawner", HTSpawnerMob.CODEC, ::spawnerMob::set)
    }
}
