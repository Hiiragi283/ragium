package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTEnchantableBlockEntity
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.item.component.ItemContainerContents
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandlerModifiable

class HTCrateBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.CRATE, pos, state),
    HTEnchantableBlockEntity,
    HTHandlerBlockEntity {
    private val itemHandler: HTItemSlot = HTItemSlot
        .Builder()
        .setCapacity(Int.MAX_VALUE)
        .setCallback(this::setChanged)
        .build("item")

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        itemHandler.writeNbt(nbt, dynamicOps)
        ItemEnchantments.CODEC
            .encodeStart(dynamicOps, enchantments)
            .ifSuccess { nbt.put(ENCH_KEY, it) }
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        itemHandler.readNbt(nbt, dynamicOps)
        ItemEnchantments.CODEC
            .parse(dynamicOps, nbt.get(ENCH_KEY))
            .ifSuccess(::onUpdateEnchantment)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        // Item
        val content: ItemContainerContents =
            componentInput.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
        itemHandler.insert(content.copyOne(), false)
        // Enchantment
        val enchantments: ItemEnchantments = componentInput.get(DataComponents.ENCHANTMENTS) ?: return
        onUpdateEnchantment(enchantments)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        // Item
        components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(listOf(itemHandler.stack)))
        // Enchantment
        if (!enchantments.isEmpty) {
            components.set(DataComponents.ENCHANTMENTS, enchantments)
        }
    }

    //    HTEnchantableBlockEntity    //

    override var enchantments: ItemEnchantments = ItemEnchantments.EMPTY

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        this.enchantments = newEnchantments
        itemHandler.onUpdateEnchantment(newEnchantments)
    }

    //    HTHandlerBlockEntity    //

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable = HTStorageIO.GENERIC.wrapItemSlot(itemHandler)
}
