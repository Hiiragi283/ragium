package hiiragi283.ragium.common.screen

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.extension.getMachineRecipes
import hiiragi283.ragium.api.extension.machineInventory
import hiiragi283.ragium.api.machine.HTMachineEntity
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.recipe.HTIngredient
import hiiragi283.ragium.api.recipe.HTRecipeResult
import hiiragi283.ragium.api.recipe.machine.HTMachineRecipe
import hiiragi283.ragium.common.init.RagiumScreenHandlerTypes
import hiiragi283.ragium.common.machine.HTProcessorMachineEntity
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.networking.NetworkSide
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking
import io.github.cottonmc.cotton.gui.widget.*
import io.github.cottonmc.cotton.gui.widget.data.Insets
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeEntry
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import kotlin.math.max
import kotlin.math.min

class HTProcessorScreenHandler(
    syncId: Int,
    playerInv: PlayerInventory,
    packet: HTMachinePacket,
    ctx: ScreenHandlerContext = ScreenHandlerContext.EMPTY,
) : SyncedGuiDescription(
        RagiumScreenHandlerTypes.PROCESSOR,
        syncId,
        playerInv,
        ctx.machineInventory(7),
        getBlockPropertyDelegate(ctx, HTMachineEntity.MAX_PROPERTIES),
    ) {
    companion object {
        @JvmField
        val UPDATE_INDEX: Identifier = RagiumAPI.id("update_index")

        @JvmField
        val UPDATE_PREVIEW: Identifier = RagiumAPI.id("update_preview")

        @JvmField
        val MAX_INDEX: Identifier = RagiumAPI.id("max_index")

        @JvmStatic
        private val defaultPreview: List<ItemStack>
            get() = listOf(Items.GLASS_PANE.defaultStack)

        @JvmStatic
        private fun itemOf(input: HTIngredient?): WItem = WItem(input?.matchingStacks ?: defaultPreview)

        @JvmStatic
        private fun itemOf(catalyst: Ingredient?): WItem =
            WItem(catalyst?.matchingStacks?.takeIf(Array<ItemStack>::isNotEmpty)?.toList() ?: defaultPreview)

        @JvmStatic
        private fun itemOf(output: HTRecipeResult?): WItem = WItem(output?.toStack()?.let(::listOf) ?: defaultPreview)

        @JvmStatic
        private fun createPreview(recipe: HTMachineRecipe?): Array<WItem> = arrayOf(
            itemOf(recipe?.getInput(0)),
            itemOf(recipe?.getInput(1)),
            itemOf(recipe?.getInput(2)),
            itemOf(recipe?.catalyst),
            itemOf(recipe?.getOutput(2)),
            itemOf(recipe?.getOutput(2)),
            itemOf(recipe?.getOutput(2)),
        )
    }

    private val pos: BlockPos = packet.pos
    private val recipePreviews: Array<WItem>
    private val recipes: List<RecipeEntry<HTMachineRecipe>>

    init {
        val (machineType: HTMachineType, machineTier: HTMachineTier) = packet
        val machineText: MutableText = machineTier.createPrefixedText(machineType)

        recipes = world.recipeManager.getMachineRecipes(machineType, machineTier)
        val firstRecipe: HTMachineRecipe? = recipes.getOrNull(0)?.value
        firstRecipe?.let(::updateRecipe)
        recipePreviews = createPreview(firstRecipe)
        ScreenNetworking.of(this, NetworkSide.CLIENT).send(MAX_INDEX, Codec.INT, recipes.size)

        val rootTab = WTabPanel()
        setRootPanel(rootTab)
        titleVisible = false
        // Main Panel
        val mainPanel = WGridPanel()
        mainPanel.setInsets(Insets.ROOT_PANEL)
        mainPanel.add(WLabel(machineText), 0, 0)
        // input slots
        mainPanel.add(WItemSlot.of(blockInventory, 0), 1, 1)
        mainPanel.add(WItemSlot.of(blockInventory, 1), 2, 1)
        mainPanel.add(WItemSlot.of(blockInventory, 2), 3, 1)
        // catalyst slot
        mainPanel.add(WItemSlot.of(blockInventory, 3), 4, 2)
        // output slots
        mainPanel.add(WItemSlot.of(blockInventory, 4).setInsertingAllowed(false), 5, 1)
        mainPanel.add(WItemSlot.of(blockInventory, 5).setInsertingAllowed(false), 6, 1)
        mainPanel.add(WItemSlot.of(blockInventory, 6).setInsertingAllowed(false), 7, 1)
        // player inventory
        mainPanel.add(createPlayerInventoryPanel(), 0, 3)
        // right arrow
        mainPanel.add(
            WBar(
                RagiumAPI.id("textures/gui/progress_base.png"),
                RagiumAPI.id("textures/gui/progress_bar.png"),
                0,
                1,
                WBar.Direction.RIGHT,
            ),
            4,
            1,
        )
        mainPanel.validate(this)
        // Add to tab
        rootTab.add(mainPanel) {
            it.icon(ItemIcon(machineType.createItemStack(machineTier)))
            it.tooltip(machineText)
        }
        // Recipe Panel
        val recipePanel = WGridPanel()
        recipePanel.setInsets(Insets.ROOT_PANEL)
        recipePanel.add(WLabel(Text.literal("Recipe Selecting")), 0, 0)
        // Back Button
        recipePanel.add(
            WButton(ItemIcon(Items.RED_DYE)).setOnClick {
                val currentIndex: Int = propertyDelegate.get(2)
                ScreenNetworking
                    .of(this, NetworkSide.CLIENT)
                    .send(UPDATE_INDEX, Codec.INT, max(0, currentIndex - 1))
            },
            2,
            2,
        )
        // Recipe Previews
        recipePanel.add(recipePreviews[0], 1, 1)
        recipePanel.add(recipePreviews[1], 2, 1)
        recipePanel.add(recipePreviews[2], 3, 1)
        recipePanel.add(recipePreviews[3], 4, 2)
        recipePanel.add(recipePreviews[4], 5, 1)
        recipePanel.add(recipePreviews[5], 6, 1)
        recipePanel.add(recipePreviews[6], 7, 1)
        // recipePanel.add(WDynamicLabel { "Current index; ${propertyDelegate.get(2)}" }, 4, 2)
        // next Button
        recipePanel.add(
            WButton(ItemIcon(Items.BLUE_DYE)).setOnClick {
                val currentIndex: Int = propertyDelegate.get(2)
                ScreenNetworking
                    .of(this, NetworkSide.CLIENT)
                    .send(UPDATE_INDEX, Codec.INT, min(currentIndex + 1, propertyDelegate.get(3) - 1))
            },
            6,
            2,
        )
        // player inventory
        recipePanel.add(createPlayerInventoryPanel(), 0, 3)
        // Add to tab
        rootTab.add(recipePanel) {
            it.icon(ItemIcon(Items.WRITABLE_BOOK))
            it.tooltip(Text.literal("Recipe Selecting"))
        }

        // Networking
        ScreenNetworking.of(this, NetworkSide.SERVER).receive(UPDATE_INDEX, Codec.INT) { index: Int ->
            propertyDelegate.set(2, index)
            val recipe: HTMachineRecipe = recipes.getOrNull(index)?.value ?: return@receive
            updateRecipe(recipe)
            ScreenNetworking.of(this, NetworkSide.SERVER).send(UPDATE_PREVIEW, HTMachineRecipe.CODEC.codec(), recipe)
        }

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(MAX_INDEX, Codec.INT) { index: Int ->
            propertyDelegate.set(3, index)
        }

        ScreenNetworking
            .of(this, NetworkSide.CLIENT)
            .receive(UPDATE_PREVIEW, HTMachineRecipe.CODEC.codec()) { recipe: HTMachineRecipe ->
                createPreview(recipe).forEachIndexed { index: Int, wItem: WItem ->
                    recipePreviews[index].items = wItem.items
                }
            }
    }

    private fun updateRecipe(recipe: HTMachineRecipe) {
        (world.getMachineEntity(pos) as? HTProcessorMachineEntity)?.currentRecipe = recipe
    }
}
