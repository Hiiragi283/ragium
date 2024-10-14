package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.component.ComponentChanges
import net.minecraft.enchantment.Enchantment
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.world.WorldView

//    Item    //

fun itemSettings(): Item.Settings = Item.Settings()

fun Item.Settings.machineType(type: HTMachineConvertible): Item.Settings = component(HTMachineType.COMPONENT_TYPE, type.asMachine())

fun Item.Settings.tier(tier: HTMachineTier): Item.Settings = component(HTMachineTier.COMPONENT_TYPE, tier)

//    ItemStack    //

fun buildItemStack(item: ItemConvertible?, count: Int = 1, builderAction: ComponentChanges.Builder.() -> Unit = {}): ItemStack {
    if (item == null) return ItemStack.EMPTY
    val item1: Item = item.asItem()
    if (item1 == Items.AIR) return ItemStack.EMPTY
    val entry: RegistryEntry<Item> = Registries.ITEM.getEntry(item1)
    val changes: ComponentChanges = ComponentChanges.builder().apply(builderAction).build()
    return ItemStack(entry, count, changes)
}

fun ItemStack.hasEnchantment(world: WorldView, key: RegistryKey<Enchantment>): Boolean = world
    .getEnchantment(key)
    ?.let(EnchantmentHelper.getEnchantments(this)::getLevel)
    ?.let { it > 0 }
    ?: false

//    ItemUsageContext    //

val ItemUsageContext.blockState: BlockState
    get() = world.getBlockState(blockPos)

val ItemUsageContext.blockEntity: BlockEntity?
    get() = world.getBlockEntity(blockPos)
