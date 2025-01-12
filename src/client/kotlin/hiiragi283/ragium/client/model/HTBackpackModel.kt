package hiiragi283.ragium.client.model

import hiiragi283.ragium.api.extension.ifPresent
import hiiragi283.ragium.api.model.HTColoredItemModel
import hiiragi283.ragium.common.init.RagiumComponentTypes
import hiiragi283.ragium.common.init.RagiumItems
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor
import net.minecraft.util.Identifier

@Environment(EnvType.CLIENT)
object HTBackpackModel : HTColoredItemModel() {
    @JvmField
    val TEXTURE: Identifier = RagiumItems.BACKPACK.id.withPrefixedPath("item/")

    override fun getSpriteId(stack: ItemStack): Identifier = TEXTURE

    override fun getColor(stack: ItemStack): Int = stack.ifPresent(RagiumComponentTypes.COLOR, -1, DyeColor::getEntityColor)
}
