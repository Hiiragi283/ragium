package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.common.block.storage.HTDrumBlockEntity
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumBlockEntityTypes {
    @JvmField
    val REGISTER: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, RagiumAPI.MOD_ID)

    @JvmStatic
    private fun <T : BlockEntity> register(
        path: String,
        factory: BlockEntityType.BlockEntitySupplier<T>,
        blocks: Collection<HTBlockContent>,
    ): DeferredHolder<BlockEntityType<*>, BlockEntityType<T>> = REGISTER.register(path) { _: ResourceLocation ->
        BlockEntityType(factory, blocks.map(HTBlockContent::get).toSet())
    }

    @JvmField
    val DRUM: DeferredHolder<BlockEntityType<*>, BlockEntityType<HTDrumBlockEntity>> =
        register("drum", ::HTDrumBlockEntity, RagiumBlocks.Drums.entries)
}
