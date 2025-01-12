package hiiragi283.ragium.api.model

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.model.BakedModel
import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.render.model.Baker
import net.minecraft.client.render.model.ModelBakeSettings
import net.minecraft.client.render.model.UnbakedModel
import net.minecraft.client.render.model.json.ModelOverrideList
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.screen.PlayerScreenHandler
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import java.util.function.Function

@Environment(EnvType.CLIENT)
interface HTBakedModel :
    UnbakedModel,
    BakedModel {
    val sprite: Sprite

    fun getSprite(id: Identifier): Sprite = MinecraftClient
        .getInstance()
        .getSpriteAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE)
        .apply(id)

    //    UnbakedModel    //

    override fun getModelDependencies(): Collection<Identifier> = listOf()

    override fun setParents(modelLoader: Function<Identifier, UnbakedModel>): Unit = Unit

    override fun bake(baker: Baker, textureGetter: Function<SpriteIdentifier, Sprite>, rotationContainer: ModelBakeSettings): BakedModel =
        this

    //    BakedModel    //

    override fun getQuads(state: BlockState?, face: Direction?, random: Random): List<BakedQuad> = listOf()

    override fun useAmbientOcclusion(): Boolean = true

    override fun hasDepth(): Boolean = false

    override fun isSideLit(): Boolean = false

    override fun isBuiltin(): Boolean = false

    override fun getParticleSprite(): Sprite = sprite

    override fun getOverrides(): ModelOverrideList = ModelOverrideList.EMPTY

    //    FabricBakedModel    //

    override fun isVanillaAdapter(): Boolean = false
}
