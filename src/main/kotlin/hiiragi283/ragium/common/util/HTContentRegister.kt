package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.util.blockSettings
import hiiragi283.ragium.api.util.itemSettings
import hiiragi283.ragium.common.block.HTBlockWithEntity
import hiiragi283.ragium.common.init.RagiumToolMaterials
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.component.type.FoodComponent
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import kotlin.jvm.optionals.getOrNull

interface HTContentRegister {
    fun <T : Any> register(registry: Registry<in T>, name: String, value: (T)): T = Registry.register(registry, RagiumAPI.id(name), value)

    //    Block    //

    fun <T : Block> registerBlock(name: String, block: T): T = register(Registries.BLOCK, name, block)

    fun registerBlock(delegated: HTEntryDelegated<Block>, block: Block): Block = registerBlock(delegated.id.path, block)

    fun registerBlock(name: String, settings: AbstractBlock.Settings = blockSettings()): Block = registerBlock(name, Block(settings))

    fun registerCopy(name: String, parent: Block): Block = registerBlock(name, blockSettings(parent))

    fun registerWithBE(name: String, type: BlockEntityType<*>, parent: Block): Block = registerWithBE(name, type, blockSettings(parent))

    fun registerWithBE(name: String, type: BlockEntityType<*>, settings: AbstractBlock.Settings = blockSettings()): Block =
        registerBlock(name, HTBlockWithEntity.build(type, settings))

    fun registerHorizontalWithBE(name: String, type: BlockEntityType<*>, parent: Block): Block =
        registerHorizontalWithBE(name, type, blockSettings(parent))

    fun registerHorizontalWithBE(name: String, type: BlockEntityType<*>, settings: AbstractBlock.Settings = blockSettings()): Block =
        registerBlock(name, HTBlockWithEntity.buildHorizontal(type, settings))

    //    Item    //

    fun <T : Item> registerItem(name: String, item: T): T = register(Registries.ITEM, name, item)

    fun registerItem(name: String, settings: Item.Settings = itemSettings()): Item = registerItem(name, Item(settings))

    fun registerItem(delegated: HTEntryDelegated<Item>, item: Item): Item = registerItem(delegated.id.path, item)

    fun registerFoodItem(name: String, component: FoodComponent): Item = registerItem(name, Item(itemSettings().food(component)))

    fun registerBlockItem(block: Block, settings: Item.Settings = itemSettings(), factory: (Block, Item.Settings) -> Item = ::BlockItem) {
        Registries.BLOCK
            .getKey(block)
            .getOrNull()
            ?.value
            ?.let { registerItem(it.path, factory(block, settings)) }
    }

    fun <T : ToolItem> registerToolItem(
        name: String,
        material: ToolMaterial,
        factory: (ToolMaterial, Item.Settings) -> T,
        attributeComponent: AttributeModifiersComponent,
        settings: Item.Settings = itemSettings(),
    ): T = registerItem(name, factory(material, settings.attributeModifiers(attributeComponent)))

    fun registerSwordItem(name: String, material: ToolMaterial, settings: Item.Settings = itemSettings()): SwordItem = registerToolItem(
        name,
        material,
        ::SwordItem,
        RagiumToolMaterials.createAttributeComponent(material, 3.0, -2.0),
        settings,
    )

    fun registerShovelItem(name: String, material: ToolMaterial, settings: Item.Settings = itemSettings()): ShovelItem = registerToolItem(
        name,
        material,
        ::ShovelItem,
        RagiumToolMaterials.createAttributeComponent(material, -2.0, -3.0),
        settings,
    )

    fun registerPickaxeItem(name: String, material: ToolMaterial, settings: Item.Settings = itemSettings()): PickaxeItem = registerToolItem(
        name,
        material,
        ::PickaxeItem,
        RagiumToolMaterials.createAttributeComponent(material, -2.0, -2.8),
        settings,
    )

    fun registerAxeItem(name: String, material: ToolMaterial, settings: Item.Settings = itemSettings()): AxeItem = registerToolItem(
        name,
        material,
        ::AxeItem,
        RagiumToolMaterials.createAttributeComponent(material, 3.0, -2.9),
        settings,
    )

    fun registerHoeItem(name: String, material: ToolMaterial, settings: Item.Settings = itemSettings()): HoeItem = registerToolItem(
        name,
        material,
        ::HoeItem,
        RagiumToolMaterials.createAttributeComponent(material, -4.0, 0.0),
        settings,
    )
}
