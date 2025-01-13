package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.material.HTMaterialProvider
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

interface HTItemContent :
    HTContent<Item>,
    ItemLike {
    override fun asItem(): Item = get()

    fun registerSimpleItem(register: DeferredRegister.Items): DeferredItem<Item> = register.registerSimpleItem(id.path)

    interface Material :
        HTItemContent,
        HTMaterialProvider
}
