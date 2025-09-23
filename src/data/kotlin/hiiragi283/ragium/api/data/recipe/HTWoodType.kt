package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.extension.toId
import hiiragi283.ragium.api.material.HTMaterialType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import java.util.Optional

interface HTWoodType : HTMaterialType {
    val log: TagKey<Item>
    val planks: ItemLike

    fun getModId(): String = log.location.namespace

    fun getId(path: String): ResourceLocation = getModId().toId(path)
    
    fun getSlab(): Optional<Item> = BuiltInRegistries.ITEM.getOptional(getId("${serializedName}_slab"))

    fun getStairs(): Optional<Item> = BuiltInRegistries.ITEM.getOptional(getId("${serializedName}_stairs"))
}
