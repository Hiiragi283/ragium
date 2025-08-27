package hiiragi283.ragium.api.storage.item

import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemContainerContents
import net.neoforged.neoforge.common.MutableDataComponentHolder
import net.neoforged.neoforge.items.ComponentItemHandler

/**
 * [ComponentItemHandler.updateContents]が[ItemContainerContents.EMPTY]の時にコンポーネントを消す[ComponentItemHandler]
 */
open class HTComponentItemHandler(parent: MutableDataComponentHolder, size: Int) :
    ComponentItemHandler(
        parent,
        DataComponents.CONTAINER,
        size,
    ) {
    override fun updateContents(contents: ItemContainerContents, stack: ItemStack, slot: Int) {
        super.updateContents(contents, stack, slot)
        if (parent
                .getOrDefault(component, ItemContainerContents.EMPTY)
                .nonEmptyStream()
                .toList()
                .isEmpty()
        ) {
            parent.remove(component)
        }
    }
}
