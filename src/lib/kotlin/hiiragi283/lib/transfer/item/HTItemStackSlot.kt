package hiiragi283.lib.transfer.item

import hiiragi283.lib.transfer.HTStackSlot
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.transfer.item.ItemResource

/**
 * [ItemStack]向けの[HTStackSlot]の拡張インターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.12
 */
interface HTItemStackSlot : HTStackSlot<ItemStack, ItemResource>
