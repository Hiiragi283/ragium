package hiiragi283.ragium.common.block.entity.storage

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI
import com.lowdragmc.lowdraglib2.gui.ui.UIElement
import com.lowdragmc.lowdraglib2.gui.ui.elements.ItemSlot
import com.lowdragmc.lowdraglib2.gui.ui.style.LayoutStyle
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.capability.slotRange
import hiiragi283.core.api.storage.HTHandlerProvider
import hiiragi283.core.common.block.entity.HTExtendedBlockEntity
import hiiragi283.core.util.HTModularUIHelper
import hiiragi283.ragium.common.item.HTUniversalChestManager
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import net.minecraft.server.MinecraftServer
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable
import org.appliedenergistics.yoga.YogaFlexDirection

class HTUniversalChestBlockEntity(pos: BlockPos, state: BlockState) :
    HTExtendedBlockEntity(RagiumBlockEntityTypes.UNIVERSAL_CHEST, pos, state),
    HTHandlerProvider {
    @DescSynced
    @Persisted
    var color: DyeColor = DyeColor.WHITE

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        componentInput.get(RagiumDataComponents.COLOR)?.let(::color::set)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        components.set(RagiumDataComponents.COLOR, color)
    }

    //    UI    //

    fun createUI(holder: BlockUIMenuType.BlockUIHolder): ModularUI =
        HTModularUIHelper.createUIWithInv(holder.player, blockState.block.name) {
            val rows: List<UIElement> = List(3) {
                UIElement()
                    .layout { style: LayoutStyle -> style.setFlexDirection(YogaFlexDirection.ROW) }
            }

            val handler: IItemHandlerModifiable = getItemHandler(null) ?: return@createUIWithInv
            for (i: Int in handler.slotRange) {
                rows[i / 9].addChild(ItemSlot().bind(handler, i))
            }
            addChildren(*rows.toTypedArray())
        }

    //    HTHandlerProvider    //

    override fun getItemHandler(direction: Direction?): IItemHandlerModifiable? {
        val server: MinecraftServer = HiiragiCoreAPI.getActiveServer() ?: return null
        return HTUniversalChestManager.getHandler(server, color)
    }

    override fun getFluidHandler(direction: Direction?): IFluidHandler? = null

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = null
}
