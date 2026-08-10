package hiiragi283.lib.data.tag

import hiiragi283.lib.registry.HTFluidContent
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.common.Tags

/**
 * [Fluid]向けの[HTTagBuilder]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTFluidTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, modId: String) : HTTagsProvider<Fluid>(output, Registries.FLUID, lookupProvider, modId) {
    //    Extensions    //

    /**
     * [HTFluidContent.fluidTag]に基づいてタグを生成します。
     * @param contents 対象となる液体の一覧
     */
    fun addContents(contents: Sequence<HTFluidContent>) {
        for (content: HTFluidContent in contents) {
            val fluidTag: TagKey<Fluid> = content.fluidTag
            builder(fluidTag).addContent(content)
            if (content.getFluidType().isLighterThanAir) {
                builder(Tags.Fluids.GASEOUS).addTag(fluidTag)
            }
        }
    }

    /**
     * 指定した要素をタグに追加します。[content]が[HTFluidContent.Flowing]の場合，[HTFluidContent.Flowing.flowingHolder]もタグに追加します。
     * @param content 液体の提供元
     */
    protected fun HTTagBuilder<Fluid>.addContent(content: HTFluidContent): HTTagBuilder<Fluid> {
        this.add(content)
        if (content is HTFluidContent.Flowing) {
            this.add(content.flowingHolder)
        }
        return this
    }

    /**
     * [HTFluidContent.fluidTag]に基づいて子タグをタグに追加します。
     * @param content 子タグの提供元
     */
    protected fun HTTagBuilder<Fluid>.addContentTag(content: HTFluidContent): HTTagBuilder<Fluid> = this.addTag(content.fluidTag)
}
