package hiiragi283.lib.data.model

import hiiragi283.lib.resource.HTSimpleValueWithKey
import net.minecraft.client.data.models.model.ModelInstance
import net.minecraft.client.data.models.model.ModelTemplate
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.resources.Identifier
import net.minecraft.world.level.block.Block
import java.util.function.BiConsumer
import kotlin.jvm.optionals.getOrNull

//    ModelTemplate    //

/**
 * `models/block`配下のモデルJSONを生成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ModelTemplate.createBlock(
    block: HTSimpleValueWithKey<*>,
    textures: TextureMapping,
    output: BiConsumer<Identifier, ModelInstance>,
    suffix: String? = null,
): Identifier = this.create(block.blockId().withSuffix(this.suffix.getOrNull() ?: suffix ?: ""), textures, output)

/**
 * `models/item`配下のモデルJSONを生成します。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun ModelTemplate.createItem(
    item: HTSimpleValueWithKey<*>,
    textures: TextureMapping,
    output: BiConsumer<Identifier, ModelInstance>,
    suffix: String? = null,
): Identifier = this.create(item.itemId().withSuffix(this.suffix.getOrNull() ?: suffix ?: ""), textures, output)

//    TexturedModel    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.14
 */
fun TexturedModel.Provider.create(block: HTSimpleValueWithKey<Block>, output: BiConsumer<Identifier, ModelInstance>): Identifier =
    this.create(block.getOrThrow(), output)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.14
 */
fun TexturedModel.Provider.createWithSuffix(
    block: HTSimpleValueWithKey<Block>,
    suffix: String,
    output: BiConsumer<Identifier, ModelInstance>,
): Identifier = this.createWithSuffix(block.getOrThrow(), suffix, output)

//    TextureMapping    //

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.14
 */
inline fun buildTextureMapping(builderAction: TextureMapping.() -> Unit): TextureMapping = TextureMapping().apply(builderAction)

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.14
 */
inline fun buildTextureMapping(parent: TextureMapping, builderAction: TextureMapping.() -> Unit): TextureMapping = parent.copy().apply(builderAction)
