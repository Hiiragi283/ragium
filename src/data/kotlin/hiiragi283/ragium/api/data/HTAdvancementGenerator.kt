package hiiragi283.ragium.api.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.critereon.ConsumeItemTrigger
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import java.util.function.Consumer

abstract class HTAdvancementGenerator(protected val prefix: String) : AdvancementProvider.AdvancementGenerator {
    protected lateinit var root: AdvancementHolder

    protected lateinit var output: Consumer<AdvancementHolder>

    final override fun generate(
        registries: HolderLookup.Provider,
        saver: Consumer<AdvancementHolder>,
        existingFileHelper: ExistingFileHelper,
    ) {
        output = saver
        root = createRoot()
        generate(registries)
    }

    protected abstract fun createRoot(): AdvancementHolder

    protected abstract fun generate(registries: HolderLookup.Provider)

    //    Extension    //

    protected fun create(path: String, builderAction: Advancement.Builder.() -> Unit): AdvancementHolder = Advancement.Builder
        .advancement()
        .apply(builderAction)
        .save(path)

    @Suppress("DEPRECATION", "removal")
    protected fun create(path: String, parent: ResourceLocation, builderAction: Advancement.Builder.() -> Unit): AdvancementHolder =
        Advancement.Builder
            .advancement()
            .parent(parent)
            .apply(builderAction)
            .save(path)

    protected fun create(path: String, parent: AdvancementHolder, builderAction: Advancement.Builder.() -> Unit): AdvancementHolder =
        Advancement.Builder
            .advancement()
            .parent(parent)
            .apply(builderAction)
            .save(path)

    protected fun <T> createSimple(
        parent: AdvancementHolder,
        holder: T,
        builderAction: HTDisplayInfoBuilder.() -> Unit = {},
    ): AdvancementHolder where T : DeferredHolder<*, *>, T : ItemLike = create(holder.id.path, parent) {
        display {
            setIcon(holder)
            setTitleFromItem(holder)
            builderAction()
        }
        hasItem("has_${holder.id.path}", holder)
    }

    protected fun <T> createSimpleConsume(
        parent: AdvancementHolder,
        holder: T,
        builderAction: HTDisplayInfoBuilder.() -> Unit = {},
    ): AdvancementHolder where T : DeferredHolder<*, *>, T : ItemLike = create(holder.id.path, parent) {
        display {
            setIcon(holder)
            setTitleFromItem(holder)
            builderAction()
        }
        useItem("use_${holder.id.path}", holder)
    }

    protected fun createMaterial(
        parent: AdvancementHolder,
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        builderAction: HTDisplayInfoBuilder.() -> Unit = {},
    ): AdvancementHolder {
        val item: DeferredItem<out Item> = RagiumItems.getMaterialItem(prefix, material)
        return create(item.id.path, parent) {
            display {
                setIcon(item)
                title = prefix.createText(material)
                builderAction()
            }
            hasItemTag("has_${item.id.path}", prefix.createTag(material))
        }
    }

    protected fun createMachine(
        parent: AdvancementHolder,
        machine: HTMachineType,
        builderAction: HTDisplayInfoBuilder.() -> Unit = {},
    ): AdvancementHolder = create(machine.serializedName, parent) {
        display {
            setIcon(machine)
            title = machine.text
            description = machine.descriptionText
            builderAction()
        }
        hasItem("has_machine", machine)
    }

    protected inline fun Advancement.Builder.display(builderAction: HTDisplayInfoBuilder.() -> Unit): Advancement.Builder =
        display(HTDisplayInfoBuilder.create(builderAction))

    protected fun Advancement.Builder.hasItem(key: String, vararg item: ItemLike): Advancement.Builder =
        addCriterion(key, InventoryChangeTrigger.TriggerInstance.hasItems(*item))

    protected fun Advancement.Builder.hasItemTag(key: String, tagKey: TagKey<Item>): Advancement.Builder = addCriterion(
        key,
        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tagKey)),
    )

    protected fun Advancement.Builder.useItem(key: String, item: ItemLike): Advancement.Builder =
        addCriterion(key, ConsumeItemTrigger.TriggerInstance.usedItem(item))

    protected fun Advancement.Builder.save(path: String): AdvancementHolder = save(output, RagiumAPI.id(path).withPrefix(prefix).toString())
}
