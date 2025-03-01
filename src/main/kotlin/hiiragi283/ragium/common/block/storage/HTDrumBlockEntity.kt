package hiiragi283.ragium.common.block.storage

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTEnchantableBlockEntity
import hiiragi283.ragium.api.block.entity.HTHandlerBlockEntity
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTDrumBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.DRUM, pos, state),
    HTEnchantableBlockEntity,
    HTHandlerBlockEntity {
    private val fluidTank: HTFluidTank = HTFluidTank.Builder().setCallback(this::setChanged).build("fluid")

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        fluidTank.writeNbt(nbt, registryOps)
        ItemEnchantments.CODEC
            .encodeStart(registryOps, enchantments)
            .ifSuccess { nbt.put(ENCH_KEY, it) }
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        fluidTank.readNbt(nbt, registryOps)
        ItemEnchantments.CODEC
            .parse(registryOps, nbt.get(ENCH_KEY))
            .ifSuccess(::onUpdateEnchantment)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        super.applyImplicitComponents(componentInput)
        // Enchantment
        val enchantments: ItemEnchantments = componentInput.get(DataComponents.ENCHANTMENTS) ?: return
        onUpdateEnchantment(enchantments)
        // Fluid
        val content: SimpleFluidContent =
            componentInput.getOrDefault(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.EMPTY)
        fluidTank.insert(content.copy(), false)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        super.collectImplicitComponents(components)
        // Enchantment
        if (!enchantments.isEmpty) {
            components.set(DataComponents.ENCHANTMENTS, enchantments)
        }
        // Fluid
        components.set(RagiumComponentTypes.FLUID_CONTENT, SimpleFluidContent.copyOf(fluidTank.stack))
    }

    override fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult = when {
        fluidTank.interactWithFluidStorage(player, HTStorageIO.GENERIC) -> ItemInteractionResult.SUCCESS

        else -> super.onRightClickedWithItem(stack, state, level, pos, player, hand, hitResult)
    }

    //    HTEnchantableBlockEntity    //

    override var enchantments: ItemEnchantments = ItemEnchantments.EMPTY

    override fun onUpdateEnchantment(newEnchantments: ItemEnchantments) {
        this.enchantments = newEnchantments
        fluidTank.onUpdateEnchantment(newEnchantments)
    }

    //    HTHandlerBlockEntity    //

    override fun getFluidHandler(direction: Direction?): IFluidHandler = HTStorageIO.GENERIC.wrapFluidTank(fluidTank)
}
