package hiiragi283.ragium.mixin;

import hiiragi283.ragium.api.RagiumAPI;
import hiiragi283.ragium.api.machine.HTMachineKey;
import hiiragi283.ragium.api.machine.HTMachineRegistry;
import hiiragi283.ragium.api.machine.HTMachineType;
import hiiragi283.ragium.api.material.HTMaterialKey;
import hiiragi283.ragium.api.material.HTMaterialRegistry;
import hiiragi283.ragium.api.material.HTTagPrefix;
import hiiragi283.ragium.api.tags.RagiumBlockTags;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagEntry;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(TagGroupLoader.class)
public abstract class TagGroupLoaderMixin {

    @Final
    @Shadow
    private String dataType;

    @Unique
    private <T> void add(Map<Identifier, List<TagGroupLoader.TrackedEntry>> map, TagKey<T> tagKey, T obj, Registry<T> registry) {
        registry.getKey(obj).map(RegistryKey::getValue).ifPresent(id -> map.computeIfAbsent(tagKey.id(), k -> new ArrayList<>()).add(new TagGroupLoader.TrackedEntry(TagEntry.create(id), RagiumAPI.MOD_NAME)));
    }

    @Unique
    private <T> void addTag(Map<Identifier, List<TagGroupLoader.TrackedEntry>> map, TagKey<T> tagKey, TagKey<T> child) {
        map.computeIfAbsent(tagKey.id(), k -> new ArrayList<>()).add(new TagGroupLoader.TrackedEntry(TagEntry.createTag(child.id()), RagiumAPI.MOD_NAME));
    }

    @Inject(method = "loadTags", at = @At("RETURN"))
    private void ragium$injectMaterialTags(ResourceManager resourceManager, CallbackInfoReturnable<Map<Identifier, List<TagGroupLoader.TrackedEntry>>> cir) {
        Map<Identifier, List<TagGroupLoader.TrackedEntry>> map = cir.getReturnValue();
        if (dataType.endsWith("block")) {
            // machine tags
            HTMachineType.getEntries().forEach(type -> addTag(map, RagiumBlockTags.MACHINES, type.getBlockTag()));
            RagiumAPI.getInstance().getMachineRegistry().getEntryMap().forEach((HTMachineKey key, HTMachineRegistry.Entry entry) -> {
                // machine type tag
                addTag(map, entry.getType().getBlockTag(), key.getBlockTag());
                // pickaxe mineable tag
                addTag(map, BlockTags.PICKAXE_MINEABLE, key.getBlockTag());
                // machine key tag
                entry.getBlocks().forEach(block -> add(map, key.getBlockTag(), block, Registries.BLOCK));
            });
        }
        if (dataType.endsWith("item")) {
            // machine tags
            RagiumAPI.getInstance().getMachineRegistry().getEntryMap().forEach((HTMachineKey key, HTMachineRegistry.Entry entry) -> {
                // machine type tag
                addTag(map, entry.getType().getItemTag(), key.getItemTag());
                // machine key tag
                entry.getBlocks().forEach(block -> add(map, key.getItemTag(), block.asItem(), Registries.ITEM));
            });
            // material tags
            RagiumAPI.getInstance().getMaterialRegistry().getEntryMap().forEach((HTMaterialKey key, HTMaterialRegistry.Entry entry) -> entry.getItemMap().forEach((HTTagPrefix prefix, Set<Item> items) -> {
                TagKey<Item> tagKey = prefix.createTag(key);
                // add tag entries
                if (!items.isEmpty()) {
                    addTag(map, prefix.getCommonTagKey(), tagKey);
                }
                // add item entries
                items.forEach((Item item) -> add(map, tagKey, item, Registries.ITEM));
            }));
            RagiumAPI.getLogger().info("Registered runtime item tags!");
        }
    }

}
