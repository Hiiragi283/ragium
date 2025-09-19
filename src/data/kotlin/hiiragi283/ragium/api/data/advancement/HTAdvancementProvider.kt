package hiiragi283.ragium.api.data.advancement

import hiiragi283.ragium.api.extension.wrapOptional
import net.minecraft.advancements.Advancement
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.conditions.ICondition
import net.neoforged.neoforge.common.conditions.WithConditions
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.nio.file.Path
import java.util.concurrent.CompletableFuture

/**
 * @see [AdvancementProvider]
 */
open class HTAdvancementProvider(
    output: PackOutput,
    private val registries: CompletableFuture<HolderLookup.Provider>,
    private val helper: ExistingFileHelper,
    private val subProviders: List<HTAdvancementGenerator>,
) : DataProvider {
    private val pathProvider: PackOutput.PathProvider =
        output.createRegistryElementsPathProvider(Registries.ADVANCEMENT)

    override fun run(output: CachedOutput): CompletableFuture<*> = registries.thenCompose { provider: HolderLookup.Provider ->
        val set: MutableSet<ResourceLocation> = mutableSetOf()
        val list: MutableList<CompletableFuture<*>> = mutableListOf()
        val output = HTAdvancementOutput { id: ResourceLocation, adv: Advancement, conditions: List<ICondition> ->
            check(set.add(id)) { "Duplicate advancement $id" }
            val path: Path = pathProvider.json(id)
            list.add(
                DataProvider.saveStable(
                    output,
                    provider,
                    Advancement.CONDITIONAL_CODEC,
                    WithConditions(conditions, adv).wrapOptional(),
                    path,
                ),
            )
        }
        for (subProvider: HTAdvancementGenerator in subProviders) {
            subProvider.generate(provider, output, helper)
        }

        CompletableFuture.allOf(*list.toTypedArray())
    }

    override fun getName(): String = "Advancements"
}
