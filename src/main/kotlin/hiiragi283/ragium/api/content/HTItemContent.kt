package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.material.HTMaterialProvider
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredItem

interface HTItemContent : HTContent<Item> {
    override val holder: DeferredItem<out Item>

    interface Material :
        HTItemContent,
        HTMaterialProvider
}
