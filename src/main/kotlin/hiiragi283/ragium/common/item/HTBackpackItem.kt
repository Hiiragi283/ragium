package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.api.extension.openBackpackScreen
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

object HTBackpackItem : Item(
    itemSettings()
        .maxCount(1)
        .component(RagiumComponentTypes.COLOR, DyeColor.WHITE),
) {
    @JvmStatic
    fun createStack(color: DyeColor): ItemStack = defaultStack.apply {
        set(RagiumComponentTypes.COLOR, color)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> = openBackpackScreen(world, user, hand)
}
