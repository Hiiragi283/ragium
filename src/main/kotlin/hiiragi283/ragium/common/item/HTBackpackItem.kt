package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.buildItemStack
import hiiragi283.ragium.api.extension.descriptions
import hiiragi283.ragium.api.extension.openBackpackScreen
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItemsNew
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.DyeColor
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class HTBackpackItem(settings: Settings) :
    Item(
        settings
            .maxCount(1)
            .component(RagiumComponentTypes.COLOR, DyeColor.WHITE)
            .descriptions(Text.translatable(RagiumTranslationKeys.BACKPACK)),
    ) {
    companion object {
        @JvmStatic
        fun createStack(color: DyeColor): ItemStack = buildItemStack(RagiumItemsNew.BACKPACK) {
            add(RagiumComponentTypes.COLOR, color)
        }
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> = openBackpackScreen(world, user, hand)
}
