package hiiragi283.ragium.common.uti

import net.minecraft.block.BlockState
import net.minecraft.component.ComponentChanges
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.enums.EnumEntries

fun <T : Enum<T>> nextEnum(entry: T, values: Array<T>): T = values[(entry.ordinal + 1) % values.size]

fun <T : Enum<T>> nextEnum(entry: T, values: EnumEntries<T>): T = values[(entry.ordinal + 1) % values.size]

//    BlockState    //

fun World.modifyBlockState(pos: BlockPos, mapping: (BlockState) -> BlockState): Boolean {
    val stateIn: BlockState = getBlockState(pos)
    return setBlockState(pos, mapping(stateIn))
}

//    ItemStack    //

fun buildItemStack(
    item: ItemConvertible?,
    count: Int = 1,
    builderAction: ComponentChanges.Builder.() -> Unit = {},
): ItemStack {
    if (item == null) return ItemStack.EMPTY
    val item1: Item = item.asItem()
    if (item1 == Items.AIR) return ItemStack.EMPTY
    val entry: RegistryEntry<Item> = Registries.ITEM.getEntry(item1)
    val changes: ComponentChanges = ComponentChanges.builder().apply(builderAction).build()
    return ItemStack(entry, count, changes)
}