package hiiragi283.ragium.client.model

import hiiragi283.ragium.common.Ragium
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel
import net.minecraft.block.BlockState
import net.minecraft.client.render.model.*
import net.minecraft.client.render.model.json.ModelOverrideList
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.texture.Sprite
import net.minecraft.client.util.ModelIdentifier
import net.minecraft.client.util.SpriteIdentifier
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.random.Random
import java.util.function.Function

object HTFluidCubeModel : UnbakedModel, BakedModel, FabricBakedModel {
    @JvmField
    val IDENTIFIER: Identifier = Ragium.id("fluid_cube")

    @JvmField
    val MODEL_ID = ModelIdentifier(IDENTIFIER, "")

    //    UnbakedModel    //

    override fun getModelDependencies(): Collection<Identifier> = listOf()

    override fun setParents(modelLoader: Function<Identifier, UnbakedModel>) {
    }

    override fun bake(baker: Baker, textureGetter: Function<SpriteIdentifier, Sprite>, rotationContainer: ModelBakeSettings): BakedModel {
        TODO("Not yet implemented")
    }

    //    BakedModel    //

    override fun getQuads(state: BlockState?, face: Direction?, random: Random): List<BakedQuad> = listOf()

    override fun useAmbientOcclusion(): Boolean = true

    override fun hasDepth(): Boolean = false

    override fun isSideLit(): Boolean = false

    override fun isBuiltin(): Boolean = false

    override fun getParticleSprite(): Sprite {
        TODO("Not yet implemented")
    }

    override fun getTransformation(): ModelTransformation? = null

    override fun getOverrides(): ModelOverrideList = ModelOverrideList.EMPTY

    //    FabricBakedModel    //

    override fun isVanillaAdapter(): Boolean = false
}
