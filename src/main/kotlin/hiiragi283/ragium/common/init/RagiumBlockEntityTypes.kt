package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.block.machine.HTBasicMachineBlockEntity
import hiiragi283.ragium.common.block.machine.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.block.storage.HTDrumBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object RagiumBlockEntityTypes {
    @JvmField
    val REGISTER: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : BlockEntity> register(
        path: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
        blocks: Collection<HTBlockContent> = listOf(),
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = REGISTER.register(path) { _: ResourceLocation ->
        BlockEntityType.Builder.of(factory, *blocks.map(HTBlockContent::get).toTypedArray()).build(null)
    }

    //    Machine    //

    @JvmField
    val BASIC_MACHINE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTBasicMachineBlockEntity>> =
        REGISTER.register("basic_machine") { _: ResourceLocation? ->
            BlockEntityType.Builder
                .of(
                    { pos: BlockPos, state: BlockState ->
                        val machine: HTMachineKey =
                            (state.block as? HTMachineBlock)?.machineKey ?: RagiumMachineKeys.ASSEMBLER
                        HTBasicMachineBlockEntity(pos, state, machine)
                    },
                ).build(null)
        }

    @JvmField
    val MULTI_SMELTER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTMultiSmelterBlockEntity>> =
        register("multi_smelter", ::HTMultiSmelterBlockEntity)

    //    Storage    //

    @JvmField
    val DRUM: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTDrumBlockEntity>> =
        register("drum", ::HTDrumBlockEntity, RagiumBlocks.Drums.entries)
}
