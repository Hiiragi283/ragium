package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.enchantment.HTEnchantmentEntry
import hiiragi283.ragium.api.enchantment.HTEnchantmentHolder
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.state.BlockState

/**
 * エンチャント可能な[HTBlockEntity]
 */
abstract class HTMachineBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTBlockEntity(type, pos, state),
    HTEnchantmentHolder,
    HTHandlerBlockEntity {
    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        ItemEnchantments.CODEC
            .encodeStart(registryOps, itemEnchantments)
            .ifSuccess { nbt.put(ENCH_KEY, it) }
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        ItemEnchantments.CODEC
            .parse(registryOps, nbt.get(ENCH_KEY))
            .ifSuccess { itemEnchantments = it }
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        itemEnchantments = componentInput.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        components.set(DataComponents.ENCHANTMENTS, itemEnchantments)
    }

    //    HTEnchantmentHolder    //

    protected var itemEnchantments: ItemEnchantments = ItemEnchantments.EMPTY

    override fun getEnchLevel(key: ResourceKey<Enchantment>): Int {
        val lookup: HolderLookup.RegistryLookup<Enchantment> = level?.registryAccess()?.enchLookup() ?: return 0
        return lookup.get(key).map(itemEnchantments::getLevel).orElse(0)
    }

    override fun getEnchEntries(): Iterable<HTEnchantmentEntry> = itemEnchantments.entrySet().map(::HTEnchantmentEntry)
}
