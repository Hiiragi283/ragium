package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.tag.createTagKey
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

data class HTMaterialPrefix(val name: String, private val commonTagPath: String, private val tagPath: String) {
    fun <T : Any> createCommonTagKey(key: RegistryKey<T>): TagKey<T> = key.createTagKey(ResourceLocation.parse(commonTagPath))

    fun <T : Any> createTagKey(key: RegistryKey<T>, material: HTMaterialLike): TagKey<T> = createTagKey(key, material.asMaterialName())

    fun <T : Any> createTagKey(key: RegistryKey<T>, name: String): TagKey<T> =
        key.createTagKey(ResourceLocation.parse(tagPath.replace("%s", name)))

    val blockCommonTag: TagKey<Block> = createCommonTagKey(Registries.BLOCK)
    val itemCommonTag: TagKey<Item> = createCommonTagKey(Registries.ITEM)

    fun blockTagKey(material: HTMaterialLike): TagKey<Block> = createTagKey(Registries.BLOCK, material)

    @Deprecated("Use `blockTagKey(HTMaterialLike) instead`")
    fun blockTagKey(name: String): TagKey<Block> = createTagKey(Registries.BLOCK, name)

    fun itemTagKey(material: HTMaterialLike): TagKey<Item> = createTagKey(Registries.ITEM, material)

    @Deprecated("Use `itemTagKey(HTMaterialLike) instead`")
    fun itemTagKey(name: String): TagKey<Item> = createTagKey(Registries.ITEM, name)

    fun toIngredient(material: HTMaterialLike): Ingredient = Ingredient.of(itemTagKey(material))
}
