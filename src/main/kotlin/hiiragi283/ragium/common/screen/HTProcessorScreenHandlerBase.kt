package hiiragi283.ragium.common.screen

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumEnvironmentBridge
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.extension.machineInventory
import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.entity.HTMachineEntity
import hiiragi283.ragium.api.machine.entity.HTProcessorMachineEntityBase
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.widget.HTFluidWidget
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.networking.NetworkSide
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking
import io.github.cottonmc.cotton.gui.widget.WBar
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WLabel
import io.github.cottonmc.cotton.gui.widget.WTabPanel
import io.github.cottonmc.cotton.gui.widget.data.Insets
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.ScreenHandlerType
import net.minecraft.text.MutableText
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos

@Suppress("LeakingThis")
abstract class HTProcessorScreenHandlerBase(
    type: ScreenHandlerType<*>,
    syncId: Int,
    playerInv: PlayerInventory,
    packet: HTMachinePacket,
    sizeType: HTMachineRecipe.SizeType,
    ctx: ScreenHandlerContext,
) : SyncedGuiDescription(
        type,
        syncId,
        playerInv,
        ctx.machineInventory(sizeType),
        getBlockPropertyDelegate(ctx, HTMachineEntity.MAX_PROPERTIES),
    ) {
    companion object {
        @JvmStatic
        fun createTickId(index: Int): Identifier = RagiumAPI.id("tick/$index")

        @JvmStatic
        fun createFluidId(index: Int): Identifier = RagiumAPI.id("fluid/$index")

        @JvmField
        val FLUID_CODEC: Codec<ResourceAmount<FluidVariant>> = RecordCodecBuilder.create { instance ->
            instance
                .group(
                    FluidVariant.CODEC.fieldOf("resource").forGetter(ResourceAmount<FluidVariant>::resource),
                    Codec.LONG.fieldOf("amount").forGetter(ResourceAmount<FluidVariant>::amount),
                ).apply(instance, ::ResourceAmount)
        }
    }

    protected val machineType: HTMachineType = packet.machineType
    protected val tier: HTMachineTier = packet.tier
    protected val machineText: MutableText = tier.createPrefixedText(machineType)
    protected val pos: BlockPos = packet.pos
    protected val fluidStorage: HTMachineFluidStorage = ctx
        .getMachineEntity()
        ?.let { it as? HTProcessorMachineEntityBase }
        ?.fluidStorage
        ?: HTMachineFluidStorage.create(sizeType)

    init {
        val rootTab = WTabPanel()
        setRootPanel(rootTab)
        titleVisible = false
        // Main Panel
        initMainPanel(rootTab)
        // Fluid Panel
        initFluidPanel(rootTab)
    }

    abstract fun initMainPanel(rootTab: WTabPanel)

    abstract fun initFluidPanel(rootTab: WTabPanel)

    protected fun createPanel(): WGridPanel = WGridPanel().apply {
        setInsets(Insets.ROOT_PANEL)
        add(WLabel(machineText), 0, 0)
    }

    protected fun addItemSlot(
        gridPanel: WGridPanel,
        index: Int,
        x: Int,
        y: Int,
        canInsert: Boolean,
    ) {
        gridPanel.add(WItemSlot.of(blockInventory, index).setInsertingAllowed(canInsert), x, y)
    }

    protected fun addFluidSlot(
        gridPanel: WGridPanel,
        index: Int,
        x: Int,
        y: Int,
    ) {
        val widget: HTFluidWidget = RagiumEnvironmentBridge.getInstance().createFluidWidget(fluidStorage, index)
        widget.setOnTick {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(createTickId(index), Codec.unit(Unit), Unit)
        }
        gridPanel.add(widget, x, y)

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(createTickId(index), Codec.unit(Unit)) {
            ScreenNetworking
                .of(this, NetworkSide.SERVER)
                .send(createFluidId(index), FLUID_CODEC, widget.getResourceAmount())
        }

        ScreenNetworking.of(this, NetworkSide.CLIENT).receive(createFluidId(index), FLUID_CODEC) {
            widget.variant = it.resource
            widget.amount = it.amount
        }
    }

    protected fun completePanel(gridPanel: WGridPanel) {
        // player inventory
        gridPanel.add(createPlayerInventoryPanel(), 0, 3)
        // progress bar
        gridPanel.add(
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
        // validate
        gridPanel.validate(this)
    }
}
