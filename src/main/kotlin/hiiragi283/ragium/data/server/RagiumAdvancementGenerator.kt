package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.toStack
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.advancements.AdvancementType
import net.minecraft.advancements.DisplayInfo
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import java.util.*
import java.util.function.Consumer

object RagiumAdvancementGenerator : AdvancementProvider.AdvancementGenerator {
    @JvmStatic
    private lateinit var root: AdvancementHolder

    @JvmStatic
    private lateinit var output: Consumer<AdvancementHolder>

    override fun generate(registries: HolderLookup.Provider, saver: Consumer<AdvancementHolder>, existingFileHelper: ExistingFileHelper) {
        output = saver
        val rawRaginite: ItemLike =
            RagiumItems.getMaterialItem(HTTagPrefix.RAW_MATERIAL, RagiumMaterials.RAGINITE)
        root = Advancement.Builder
            .advancement()
            .display(
                DisplayInfo(
                    rawRaginite.toStack(),
                    Component.literal(RagiumAPI.MOD_NAME),
                    Component.literal("Welcome to Ragium!"),
                    Optional.of(ResourceLocation.withDefaultNamespace("textures/block/bricks.png")),
                    AdvancementType.TASK,
                    true,
                    true,
                    false,
                ),
            ).hasItem("has_raginite", rawRaginite)
            .save("root")

        registerTier1()
        registerTier2()
        registerTier4()
    }

    private fun registerTier1() {
        val ragiAlloy: AdvancementHolder = createMaterial(
            root,
            HTTagPrefix.INGOT,
            RagiumMaterials.RAGI_ALLOY,
            Component.empty(),
        )
        val grinder: AdvancementHolder = createSimple(
            ragiAlloy,
            RagiumBlocks.MANUAL_GRINDER,
            Component.empty(),
        )
        val mixer: AdvancementHolder = createMachine(ragiAlloy, RagiumMachineKeys.MIXER)

        val casing: AdvancementHolder = createSimple(
            ragiAlloy,
            RagiumBlocks.Casings.BASIC,
            Component.empty(),
        )
        val pbf: AdvancementHolder = createSimple(casing, RagiumBlocks.PRIMITIVE_BLAST_FURNACE, Component.empty())
        val steel: AdvancementHolder = createMaterial(
            pbf,
            HTTagPrefix.INGOT,
            CommonMaterials.STEEL,
            Component.empty(),
        )
        val blastFurnace: AdvancementHolder = createMachine(steel, RagiumMachineKeys.BLAST_FURNACE)
        val deepSteel: AdvancementHolder = createMaterial(
            blastFurnace,
            HTTagPrefix.INGOT,
            RagiumMaterials.DEEP_STEEL,
            Component.empty(),
        )

        val compressor: AdvancementHolder = createMachine(casing, RagiumMachineKeys.COMPRESSOR)
    }

    private fun registerTier2() {
        val ragiSteel: AdvancementHolder = createMaterial(
            root,
            HTTagPrefix.INGOT,
            RagiumMaterials.RAGI_STEEL,
            Component.empty(),
        )
        val casing: AdvancementHolder = createSimple(
            ragiSteel,
            RagiumBlocks.Casings.ADVANCED,
            Component.empty(),
        )

        val assembler: AdvancementHolder = createMachine(casing, RagiumMachineKeys.ASSEMBLER)
        val basicCircuit: AdvancementHolder = createSimple(
            assembler,
            RagiumItems.BASIC_CIRCUIT,
            Component.empty(),
        )
        val advancedCircuit: AdvancementHolder = createSimple(
            basicCircuit,
            RagiumItems.ADVANCED_CIRCUIT,
            Component.empty(),
        )
        val eliteCircuit: AdvancementHolder = createSimple(
            advancedCircuit,
            RagiumItems.ELITE_CIRCUIT,
            Component.empty(),
        )
        val ultimateCircuit: AdvancementHolder = createSimple(
            eliteCircuit,
            RagiumItems.ULTIMATE_CIRCUIT,
            Component.empty(),
            type = AdvancementType.GOAL,
        )

        val extractor: AdvancementHolder = createMachine(casing, RagiumMachineKeys.EXTRACTOR)

        val grinder: AdvancementHolder = createMachine(casing, RagiumMachineKeys.GRINDER)
    }

    private fun registerTier4() {
        val ragium: AdvancementHolder = createMaterial(
            root,
            HTTagPrefix.INGOT,
            RagiumMaterials.RAGIUM,
            Component.empty(),
            type = AdvancementType.GOAL,
        )
        val casing: AdvancementHolder = createSimple(
            ragium,
            RagiumBlocks.Casings.ULTIMATE,
            Component.empty(),
        )
    }

    @JvmStatic
    private fun create(parent: AdvancementHolder): Advancement.Builder = Advancement.Builder.advancement().parent(parent)

    @JvmStatic
    private fun <T> createSimple(
        parent: AdvancementHolder,
        holder: T,
        desc: Component,
        title: Component = holder.toStack().hoverName,
        type: AdvancementType = AdvancementType.TASK,
        showToast: Boolean = true,
        showChat: Boolean = true,
        hidden: Boolean = false,
    ): AdvancementHolder where T : DeferredHolder<*, *>, T : ItemLike = create(parent)
        .display(
            DisplayInfo(
                ItemStack(holder),
                title,
                desc,
                Optional.empty(),
                type,
                showToast,
                showChat,
                hidden,
            ),
        ).hasItem("has_${holder.id.path}", holder)
        .save(holder.id.path)

    @JvmStatic
    private fun createSimple(
        parent: AdvancementHolder,
        content: HTBlockContent,
        desc: Component,
        title: Component = content.toStack().hoverName,
        type: AdvancementType = AdvancementType.TASK,
        showToast: Boolean = true,
        showChat: Boolean = true,
        hidden: Boolean = false,
    ): AdvancementHolder = create(parent)
        .display(
            DisplayInfo(
                ItemStack(content),
                title,
                desc,
                Optional.empty(),
                type,
                showToast,
                showChat,
                hidden,
            ),
        ).hasItem("has_${content.id.path}", content)
        .save(content.id.path)

    @JvmStatic
    private fun createMaterial(
        parent: AdvancementHolder,
        prefix: HTTagPrefix,
        material: HTMaterialKey,
        desc: Component,
        title: Component = prefix.createText(material),
        type: AdvancementType = AdvancementType.TASK,
        showToast: Boolean = true,
        showChat: Boolean = true,
        hidden: Boolean = false,
    ): AdvancementHolder {
        val item: DeferredItem<out Item> = RagiumItems.getMaterialItem(prefix, material)
        return create(parent)
            .display(
                DisplayInfo(
                    item.toStack(),
                    title,
                    desc,
                    Optional.empty(),
                    type,
                    showToast,
                    showChat,
                    hidden,
                ),
            ).hasItemTag("has_${item.id.path}", prefix.createTag(material))
            .save(item.id.path)
    }

    @JvmStatic
    private fun createMachine(
        parent: AdvancementHolder,
        machine: HTMachineKey,
        type: AdvancementType = AdvancementType.TASK,
        showToast: Boolean = true,
        showChat: Boolean = true,
        hidden: Boolean = false,
    ): AdvancementHolder = create(parent)
        .display(
            DisplayInfo(
                ItemStack(machine.getBlock()),
                machine.text,
                machine.descriptionText,
                Optional.empty(),
                type,
                showToast,
                showChat,
                hidden,
            ),
        ).hasItem("has_${machine.name}", machine.getBlock())
        .save(machine.name)

    @JvmStatic
    private fun Advancement.Builder.hasItem(key: String, item: ItemLike): Advancement.Builder =
        addCriterion(key, InventoryChangeTrigger.TriggerInstance.hasItems(item))

    @JvmStatic
    private fun Advancement.Builder.hasItemTag(key: String, tagKey: TagKey<Item>): Advancement.Builder = addCriterion(
        key,
        InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tagKey)),
    )

    @JvmStatic
    private fun Advancement.Builder.save(path: String): AdvancementHolder = save(output, RagiumAPI.id(path).toString())
}
