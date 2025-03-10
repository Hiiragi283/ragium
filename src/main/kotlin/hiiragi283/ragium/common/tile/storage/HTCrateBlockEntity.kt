package hiiragi283.ragium.common.tile.storage

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTEnchantableBlockEntity
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.api.util.HTCrateVariant
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandlerModifiable

class HTCrateBlockEntity(pos: BlockPos, state: BlockState, var variant: HTCrateVariant = HTCrateVariant.IRON) :
    HTBlockEntity(RagiumBlockEntityTypes.CRATE, pos, state),
    HTEnchantableBlockEntity,
    HTHandlerBlockEntity {
    private var itemSlot: HTItemSlot = HTItemSlot
        .Builder()
        .setCapacity(variant.capacity)
        .setCallback(this::setChanged)
        .build("item")

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        ItemEnchantments.CODEC
            .encodeStart(registryOps, enchantments)
            .ifSuccess { nbt.put(ENCH_KEY, it) }
        HTCrateVariant.CODEC
            .encodeStart(registryOps, variant)
            .ifSuccess { nbt.put("variant", it) }
        itemSlot.writeNbt(nbt, registryOps)
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        ItemEnchantments.CODEC
            .parse(registryOps, nbt.get(ENCH_KEY))
            .ifSuccess(::onUpdateEnchantment)
        HTCrateVariant.CODEC
            .parse(registryOps, nbt.get("variant"))
            .ifSuccess { variant = it }
        itemSlot.readNbt(nbt, registryOps)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        // Enchantment
        val enchantments: ItemEnchantments = componentInput.get(DataComponents.ENCHANTMENTS) ?: return
        onUpdateEnchantment(enchantments)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        // Enchantment
        if (!enchantments.isEmpty) {
            components.set(DataComponents.ENCHANTMENTS, enchantments)
        }
    }

    //    HTEnchantableBlockEntity    //

    override var enchantments: ItemEnchantments = ItemEnchantments.EMPTY

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        this.enchantments = newEnchantments
        itemSlot.onUpdateEnchantment(newEnchantments)
    }

    //    HTHandlerBlockEntity    //

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable? = HTStorageIO.GENERIC.wrapItemSlot(itemSlot)
}
