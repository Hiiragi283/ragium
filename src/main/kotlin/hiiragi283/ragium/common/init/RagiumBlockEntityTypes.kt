package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.block.machine.HTManualGrinderBlockEntity
import hiiragi283.ragium.common.block.machine.processor.HTDefaultMachineBlockEntity
import hiiragi283.ragium.common.block.machine.processor.HTLargeMachineBlockEntity
import hiiragi283.ragium.common.block.machine.processor.HTMultiSmelterBlockEntity
import hiiragi283.ragium.common.block.storage.HTDrumBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object RagiumBlockEntityTypes {
    @JvmField
    val REGISTER: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : BlockEntity> register(
        path: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
        block: Supplier<out Block>,
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = REGISTER.register(path) { _: ResourceLocation ->
        BlockEntityType.Builder.of(factory, block.get()).build(null)
    }

    @JvmStatic
    private fun <T : BlockEntity> register(
        path: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
        blocks: Collection<Supplier<out Block>> = listOf(),
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = REGISTER.register(path) { _: ResourceLocation ->
        BlockEntityType.Builder.of(factory, *blocks.map(Supplier<out Block>::get).toTypedArray()).build(null)
    }

    @JvmStatic
    private fun <T : HTMachineBlockEntity> registerMachine(
        path: String,
        factory: (BlockPos, BlockState, HTMachineKey) -> T,
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = REGISTER.register(path) { _: ResourceLocation ->
        BlockEntityType.Builder
            .of(
                { pos: BlockPos, state: BlockState ->
                    val machine: HTMachineKey =
                        (state.block as? HTMachineBlock)?.machineKey ?: RagiumMachineKeys.ASSEMBLER
                    factory(pos, state, machine)
                },
            ).build(null)
    }

    //    Manual Machine    //

    @JvmField
    val MANUAL_GRINDER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTManualGrinderBlockEntity>> =
        register("manual_grinder", ::HTManualGrinderBlockEntity, RagiumBlocks.MANUAL_GRINDER)

    //    Machine    //

    @JvmField
    val DEFAULT_MACHINE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTDefaultMachineBlockEntity>> =
        registerMachine("default_machine", ::HTDefaultMachineBlockEntity)

    @JvmField
    val LARGE_MACHINE: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTLargeMachineBlockEntity>> =
        registerMachine("large_machine", ::HTLargeMachineBlockEntity)

    @JvmField
    val MULTI_SMELTER: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTMultiSmelterBlockEntity>> =
        register("multi_smelter", ::HTMultiSmelterBlockEntity)

    //    Storage    //

    @JvmField
    val DRUM: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTDrumBlockEntity>> =
        register("drum", ::HTDrumBlockEntity, RagiumBlocks.Drums.entries)
}
