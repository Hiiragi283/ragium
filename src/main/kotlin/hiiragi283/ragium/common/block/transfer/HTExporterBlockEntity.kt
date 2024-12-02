package hiiragi283.ragium.common.block.transfer

import hiiragi283.ragium.api.extension.*
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.tags.RagiumItemTags
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumTranslationKeys
import net.fabricmc.fabric.api.transfer.v1.storage.StorageUtil
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.NbtString
import net.minecraft.registry.RegistryCodecs
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.state.property.Properties
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTExporterBlockEntity(pos: BlockPos, state: BlockState) :
    HTTransporterBlockEntityBase(RagiumBlockEntityTypes.EXPORTER, pos, state) {
    constructor(pos: BlockPos, state: BlockState, tier: HTMachineTier) : this(pos, state) {
        this.tier = tier
    }

    var fluidFilter: RegistryEntryList<Fluid> = RegistryEntryList.empty()
    var itemFilter: RegistryEntryList<Item> = RegistryEntryList.empty()

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        RegistryCodecs
            .entryList(RegistryKeys.FLUID)
            .encodeStart(wrapperLookup.getOps(NbtOps.INSTANCE), fluidFilter)
            .ifSuccess { nbt.put("fluid_filter", it) }
        RegistryCodecs
            .entryList(RegistryKeys.ITEM)
            .encodeStart(wrapperLookup.getOps(NbtOps.INSTANCE), itemFilter)
            .ifSuccess { nbt.put("item_filter", it) }
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        RegistryCodecs
            .entryList(RegistryKeys.FLUID)
            .parse(wrapperLookup.getOps(NbtOps.INSTANCE), nbt.get("fluid_filter"))
            .ifSuccess { fluidFilter = it }
        RegistryCodecs
            .entryList(RegistryKeys.ITEM)
            .parse(wrapperLookup.getOps(NbtOps.INSTANCE), nbt.get("item_filter"))
            .ifSuccess { itemFilter = it }
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val stack: ItemStack = player.getStackInActiveHand()
        if (!world.isClient) {
            when {
                stack.isIn(RagiumItemTags.FLUID_EXPORTER_FILTERS) -> {
                    RegistryCodecs
                        .entryList(RegistryKeys.FLUID)
                        .parse(world.registryManager.getOps(NbtOps.INSTANCE), NbtString.of(stack.name.string))
                        .ifSuccess { fluidFilter = it }
                }

                stack.isIn(RagiumItemTags.ITEM_EXPORTER_FILTERS) -> {
                    RegistryCodecs
                        .entryList(RegistryKeys.ITEM)
                        .parse(world.registryManager.getOps(NbtOps.INSTANCE), NbtString.of(stack.name.string))
                        .ifSuccess { itemFilter = it }
                }

                else -> {
                    player.sendMessage(
                        Text.translatable(
                            RagiumTranslationKeys.EXPORTER_FLUID_FILTER,
                            fluidFilter.asText(Fluid::name),
                        ),
                        false,
                    )
                    player.sendMessage(
                        Text.translatable(
                            RagiumTranslationKeys.EXPORTER_ITEM_FILTER,
                            itemFilter.asText(Item::getName),
                        ),
                        false,
                    )
                }
            }
        }
        return ActionResult.success(world.isClient)
    }

    private val front: Direction
        get() = cachedState.get(Properties.FACING)

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        if (world.isClient) return
        if (world.isReceivingRedstonePower(pos)) return
        // transfer containment
        StorageUtil.move(
            getBackItemStorage(world, pos, front),
            getFrontItemStorage(world, pos, front),
            { itemFilter.isEmpty || itemFilter.any(it::isOf) },
            type.getItemCount(tier),
            null,
        )
        StorageUtil.move(
            getBackFluidStorage(world, pos, front),
            getFrontFluidStorage(world, pos, front),
            { fluidFilter.isEmpty || fluidFilter.any(it::isOf) },
            type.getFluidCount(tier),
            null,
        )
    }
}
