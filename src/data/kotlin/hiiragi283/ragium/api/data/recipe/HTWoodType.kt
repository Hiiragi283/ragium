package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.registry.toId
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.Item

interface HTWoodType : StringRepresentable {
    val log: TagKey<Item>
    val planks: HTItemHolderLike

    fun getModId(): String = log.location.namespace

    fun getId(path: String): ResourceLocation = getModId().toId(path)

    fun getSlab(): HTItemHolderLike = HTItemHolderLike.fromId(getId("${serializedName}_slab"))

    fun getStairs(): HTItemHolderLike = HTItemHolderLike.fromId(getId("${serializedName}_stairs"))
}
