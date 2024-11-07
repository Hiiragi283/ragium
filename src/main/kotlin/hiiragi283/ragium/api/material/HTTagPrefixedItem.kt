package hiiragi283.ragium.api.material

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

open class HTTagPrefixedItem(val prefix: HTTagPrefix, val key: HTMaterialKey, settings: Settings) : Item(settings) {
    override fun getName(): Text = prefix.getText(key)

    override fun getName(stack: ItemStack): Text = prefix.getText(key)
}
