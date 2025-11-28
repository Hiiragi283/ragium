package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.registry.HTItemHolderLike
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import java.util.function.Consumer

/**
 * @see mekanism.common.registration.impl.CreativeTabDeferredRegister.ICustomCreativeTabContents
 */
fun interface HTSubCreativeTabContents {
    fun addItems(baseItem: HTItemHolderLike, provider: HolderLookup.Provider, consumer: Consumer<ItemStack>)

    fun shouldAddDefault(): Boolean = true
}
