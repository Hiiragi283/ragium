package hiiragi283.lib.data.model

import hiiragi283.lib.resource.HTIdLike
import hiiragi283.lib.resource.itemId
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.resources.Identifier

fun interface HTTexturedModelProvider {
    companion object {
        @JvmField
        val FLAT_ITEM = HTTexturedModelProvider { value: HTIdLike, output: ModelOutput ->
            ModelTemplates.FLAT_ITEM.create(value.itemId, TextureMapping.layer0(Material(value.itemId)), output)
        }
    }

    fun create(value: HTIdLike, output: ModelOutput): Identifier
}
