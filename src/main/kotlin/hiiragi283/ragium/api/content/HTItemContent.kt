package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.material.HTMaterialProvider
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.registries.DeferredItem

interface HTItemContent :
    HTContent<Item>,
    ItemLike {
    override val holder: DeferredItem<out Item>

    override fun asItem(): Item = get()

    interface Material :
        HTItemContent,
        HTMaterialProvider

    interface Tier :
        HTItemContent,
        HTMachineTierProvider
}
