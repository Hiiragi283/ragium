package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTEnchantableBlockEntity
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.common.capability.item.HTSingleItemVariantHandler
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

class HTCrateBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.CRATE, pos, state),
    HTEnchantableBlockEntity,
    HTHandlerBlockEntity {
    private val itemHandler = HTSingleItemVariantHandler(Int.MAX_VALUE, this::setChanged)

    override fun writeNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        itemHandler.encode(nbt, dynamicOps)
        ItemEnchantments.CODEC
            .encodeStart(dynamicOps, enchantments)
            .ifSuccess { nbt.put(ENCH_KEY, it) }
    }

    override fun readNbt(nbt: CompoundTag, dynamicOps: RegistryOps<Tag>) {
        itemHandler.decode(nbt, dynamicOps)
        ItemEnchantments.CODEC
            .parse(dynamicOps, nbt.get(ENCH_KEY))
            .ifSuccess(::updateEnchantments)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        // Fluid
        val content: ItemContainerContents =
            componentInput.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
        itemHandler.setStackInSlot(0, content.copyOne())
        // Enchantment
        val enchantments: ItemEnchantments = componentInput.get(DataComponents.ENCHANTMENTS) ?: return
        updateEnchantments(enchantments)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        // Fluid
        components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(listOf(itemHandler.getStackInSlot(0))))
        // Enchantment
        if (!enchantments.isEmpty) {
            components.set(DataComponents.ENCHANTMENTS, enchantments)
        }
    }

    //    HTEnchantableBlockEntity    //

    override var enchantments: ItemEnchantments = ItemEnchantments.EMPTY

    override fun updateEnchantments(newEnchantments: ItemEnchantments) {
        this.enchantments = newEnchantments
        // itemHandler.updateCapacity(this)
    }

    //    HTHandlerBlockEntity    //

    override fun getItemHandler(direction: Direction?): HTSingleItemVariantHandler = itemHandler
}
