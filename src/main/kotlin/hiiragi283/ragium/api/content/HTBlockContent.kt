package hiiragi283.ragium.api.content

import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.material.HTMaterialProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock

interface HTBlockContent :
    HTContent<Block>,
    ItemLike {
    val blockId: ResourceLocation
        get() = id.withPrefix("block/")

    override val holder: DeferredBlock<out Block>

    override fun asItem(): Item = get().asItem()

    interface Material :
        HTBlockContent,
        HTMaterialProvider

    interface Tier :
        HTBlockContent,
        HTMachineTierProvider
}
