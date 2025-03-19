package hiiragi283.ragium.api.registry

import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.network.chat.MutableComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem

class HTDeferredMachine<B : Block, BE : BlockEntity>(
    name: String,
    blockFactory: (BlockBehaviour.Properties) -> B,
    blockEntityFactory: BlockEntityType.BlockEntitySupplier<BE>,
    property: BlockBehaviour.Properties,
) : ItemLike,
    StringRepresentable {
    companion object {
        @JvmStatic
        private val BLOCK_REGISTER = HTBlockRegister(RagiumAPI.MOD_ID)

        @JvmStatic
        private val ITEM_REGISTER = HTItemRegister(RagiumAPI.MOD_ID)

        @JvmStatic
        private val BLOCK_ENTITY_REGISTER = HTBlockEntityTypeRegister(RagiumAPI.MOD_ID)

        @JvmStatic
        fun getBlocks(): List<DeferredBlock<out Block>> = BLOCK_REGISTER.entries

        @JvmStatic
        fun getItems(): List<DeferredItem<out Item>> = ITEM_REGISTER.entries

        @JvmStatic
        fun init(eventBus: IEventBus) {
            BLOCK_REGISTER.register(eventBus)
            ITEM_REGISTER.register(eventBus)
            BLOCK_ENTITY_REGISTER.register(eventBus)
        }
    }

    @JvmField
    val blockHolder: DeferredBlock<B> = BLOCK_REGISTER.registerBlock(name, blockFactory, property)

    @JvmField
    val itemHolder: DeferredItem<BlockItem> = ITEM_REGISTER.registerSimpleBlockItem(blockHolder)

    @JvmField
    val blockEntityHolder: DeferredHolder<BlockEntityType<*>, BlockEntityType<BE>> =
        BLOCK_ENTITY_REGISTER.registerType(name, blockEntityFactory, blockHolder)

    @JvmField
    val id: ResourceLocation = blockHolder.id

    @JvmField
    val blockId: ResourceLocation = id.withPrefix("block/")

    val title: MutableComponent get() = asBlock().name

    fun asBlock(): B = blockHolder.get()

    override fun asItem(): Item = itemHolder.asItem()

    override fun getSerializedName(): String = id.path
}
