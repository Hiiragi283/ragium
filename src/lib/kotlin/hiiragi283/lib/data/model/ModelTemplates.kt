package hiiragi283.lib.data.model

import hiiragi283.lib.resource.HTIdOrValue
import hiiragi283.lib.resource.blockId
import hiiragi283.lib.resource.itemId
import net.minecraft.client.data.models.model.ModelInstance
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.resources.Identifier
import java.util.function.BiConsumer
import kotlin.jvm.optionals.getOrElse

/**
 * `models/block`配下のモデルJSONを生成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ModelTemplate.createBlock(
    like: HTIdOrValue<*>,
    textures: TextureMapping,
    output: BiConsumer<Identifier, ModelInstance>
): Identifier = this.create(like.idOrThrow.blockId.withSuffix(this.suffix.getOrElse { "" }), textures, output)

/**
 * `models/item`配下のモデルJSONを生成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ModelTemplate.createItem(
    like: HTIdOrValue<*>,
    textures: TextureMapping,
    output: BiConsumer<Identifier, ModelInstance>
): Identifier = this.create(like.idOrThrow.itemId.withSuffix(this.suffix.getOrElse { "" }), textures, output)
