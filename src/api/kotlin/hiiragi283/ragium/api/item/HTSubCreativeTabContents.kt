package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.registry.HTItemHolderLike
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import java.util.function.Consumer

/**
 * @see mekanism.common.registration.impl.CreativeTabDeferredRegister.ICustomCreativeTabContents
 */
fun interface HTSubCreativeTabContents {
    fun addItems(baseItem: HTItemHolderLike, parameters: CreativeModeTab.ItemDisplayParameters, consumer: Consumer<ItemStack>)

    fun shouldAddDefault(): Boolean = true
}
